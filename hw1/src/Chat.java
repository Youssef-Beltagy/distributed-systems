/**
 *  A distributed peer to peer chat application that receives and multicasts messages.
 * Implements vector clock for causal ordering.
 *
 * @author 	Youssef Beltagy
 * @version 4/20/2020
 */

import java.net.*;  // ServerSocket, Socket
import java.io.*;   // InputStream, ObjectInputStream, ObjectOutputStream
import java.util.*; // Vector

public class Chat {

    // Each element i of the follwoing arrays represent a chat member[i]
    private Socket[] sockets = null;             // connection to i
    private InputStream[] indata = null;         // used to check data from i
    private ObjectInputStream[] inputs = null;   // a message from i
	private ObjectOutputStream[] outputs = null; // a message to ij
	private int[] vClock = null;				 // vector clock for causal ordering
	private Vector<MessageData> messages = null; // Waiting messages

    /**
     * Is the main body of the Chat application. This constructor establishes
     * a socket to each remote chat member, broadcasts a local user's message
     * to all the remote chat members, and receive a message from each of them.
     *
     * @param port  IP port used to connect to a remote node as well as to
     *              accept a connection from a remote node.
     * @param rank  this local node's rank (one of 0 through to #members - 1)
     * @param hosts a list of all computing nodes that participate in chatting
     */
    public Chat( int port, int rank, String[] hosts ) throws IOException {
	// print out my port, rank and local hostname
	System.out.println( "port = " + port + ", rank = " + rank +
			    ", localhost = " + hosts[rank] );

	// create sockets, inputs, outputs, vector clock, and vector of messages
	sockets = new Socket[hosts.length];
	indata = new InputStream[hosts.length];
	inputs = new ObjectInputStream[hosts.length];
	outputs = new ObjectOutputStream[hosts.length];
	vClock = new int[hosts.length];
	messages = new Vector<MessageData>();



	// establish a complete network
	ServerSocket server = new ServerSocket( port );
	for ( int i = hosts.length - 1; i >= 0; i-- ) {

	    if ( i > rank ) {
		// accept a connection from others with a higher rank
		Socket socket = server.accept( );
		String src_host = socket.getInetAddress( ).getHostName( );

		// find this source host's rank
		for ( int j = 0; j < hosts.length; j++ )
		    if ( src_host.startsWith( hosts[j] ) ) {
			// j is this source host's rank
			System.out.println( "accepted from " + src_host );

			// store this source host j's connection, input stream
			// and object intput/output streams.
			sockets[j] = socket;
			indata[j]= socket.getInputStream( );
			inputs[j] = 
			    new ObjectInputStream( indata[j] );
			outputs[j] = 
			    new ObjectOutputStream( socket.getOutputStream( ));
		    }
	    }
	    if ( i < rank ) {
		// establish a connection to others with a lower rank
		sockets[i] = new Socket( hosts[i], port );
		System.out.println( "connected to " + hosts[i] );

		// store this destination host j's connection, input stream
		// and object intput/output streams.
		outputs[i] 
		    = new ObjectOutputStream( sockets[i].getOutputStream( ) );
		indata[i] = sockets[i].getInputStream( );
		inputs[i] 
		    = new ObjectInputStream( indata[i] );
	    }
	}

	for ( int j = 0; j < hosts.length; j++ ){
		vClock[j] = 0; // initialize vector clock to zeros
	}
		   

	// create a keyboard stream
	BufferedReader keyboard
	    = new BufferedReader( new InputStreamReader( System.in ) );

	// now goes into a chat
	while ( true ) {
	    // read a message from keyboard and broadcast it to all the others.
	    if ( keyboard.ready( ) ) {
		// since keyboard is ready, read one line.
			String message = keyboard.readLine( );
			if ( message == null ) {
				// keyboard was closed by "^d"
				break; // terminate the program
			}
			
			vClock[rank] = vClock[rank] + 1;

			// broadcast a message to each of the chat members.
			for ( int i = 0; i < hosts.length; i++ ){
				if ( i != rank ) {
					// of course I should not send a message to myself
		
					MessageData tempMessage = new MessageData(hosts[rank]+ ": " + message, vClock.clone(), rank);
					outputs[i].writeObject(tempMessage);
					outputs[i].flush(); // make sure the message was sent
				}
			}

	    }

	    // read a message from each of the chat members
	    for ( int i = 0; i < hosts.length; i++ ) {
		// to intentionally create a misordered message deliveray, 
		// let's slow down the chat member #2.
		try {
		    if ( rank == 2 )
			Thread.currentThread( ).sleep( 5000 ); // sleep 5 sec.
		} catch ( InterruptedException e ) {}

		// check if chat member #i has something
		if ( i != rank && indata[i].available( ) > 0 ) {
		    // read a message from chat member #i and add it the the vector of messages
		    try {

				MessageData inputMessage = ( MessageData) inputs[i].readObject( );
				messages.add(inputMessage);
			
		    } catch ( ClassNotFoundException e ) {}
		}
		}

		// loop through the message and use compareVectors to find the 
		// next message to print in the correct order.
		// Uses the vector clock algorithm
		boolean loopAgain = true;
		while(loopAgain){
			loopAgain = false;
			
			// If you couldn't print any message, then the while loop 
			// will end.
			for(int k = 0; k < messages.size(); k++){

				// If you could print a message, delete the current message
				// from the vector of messages, exit the for loop, 
				// and restart the while loop
				if(compareVectors(messages.get(k))){

					vClock[messages.get(k).senderRank]++;
					System.out.println(messages.get(k).message);
					loopAgain = true;
					messages.remove(k);
					break;

				}
			}
		}

	}
    }

    /**
     * Is the main function that verifies the correctness of its arguments and
     * starts the application.
     *
     * @param args receives <port> <ip1> <ip2> ... where port is an IP port
     *             to establish a TCP connection and ip1, ip2, .... are a
     *             list of all computing nodes that participate in a chat.
     */
    public static void main( String[] args ) {

	// verify #args.
	if ( args.length < 2 ) {
	    System.err.println( "Syntax: java Chat <port> <ip1> <ip2> ..." );
	    System.exit( -1 );
	}

	// retrieve the port
	int port = 0;
	try {
	    port = Integer.parseInt( args[0] );
	} catch ( NumberFormatException e ) {
	    e.printStackTrace( );
	    System.exit( -1 );
	}
	if ( port <= 5000 ) {
	    System.err.println( "port should be 5001 or larger" );
	    System.exit( -1 );
	}

	// retireve my local hostname
	String localhost = null;
	try {
	    localhost = InetAddress.getLocalHost( ).getHostName( );
	} catch ( UnknownHostException e ) {
	    e.printStackTrace( );
	    System.exit( -1 );
	}

	// store a list of computing nodes in hosts[] and check my rank
	int rank = -1;
	String[] hosts = new String[args.length - 1];
	for ( int i = 0; i < args.length - 1; i++ ) {
	    hosts[i] = args[i + 1];
	    if ( localhost.startsWith( hosts[i] ) ) 
		// found myself in the i-th member of hosts
		rank = i;
	}

	// now start the Chat application
	try {
	    new Chat( port, rank, hosts );
	} catch ( IOException e ) {
	    e.printStackTrace( );
	    System.exit( -1 );
	}
	}
	
	/*
	* Uses the causal ordering algorithm to determine if the message should be printed
	*
	* @param messageData The message that is to be printed
	* @return true if the message should be printed
	*/
	public boolean compareVectors(MessageData messageData){
		if(vClock.length != (messageData.vClock).length){
			return false;
		}

		for(int i = 0; i < vClock.length; i++){

			if(i != messageData.senderRank){
				//if messageData.vClock[i] > vClock[i] return false
				if( !(messageData.vClock[i] <= vClock[i]) ) return false; 
			}else if(i == messageData.senderRank){ // the second if is not necessary, but just for clarity.
				if(messageData.vClock[i] != vClock[i] + 1) return false;
			}
			
		}

		return true;
	}

}
