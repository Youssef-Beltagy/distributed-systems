// An experiment to see the execution speed of mobile agents

import java.io.*;
import java.util.*;
import java.net.InetAddress;

import UWAgent.*;

public class UnixAgent extends UWAgent implements Serializable{

private boolean print = false;
private int nNodes;
private String[] nodes;
private int nCommands;
private String[] commands;
private String[][][] output;
private String destination = null;
private int me;

private String[] args = null;

// for the timer
private Date startTime;

// constructors call setup.
public UnixAgent( String[] args ) {
    this.args = args;
}

public UnixAgent( ) { 
    this.args = null;
}

public void init( ) {

    setup(args);
    args = null;

    startTime = new Date( );
    me = 1 % nodes.length;
    hop( nodes[me], "run", null );
}

// Processes all the commands for this server and calls the next server
public void run() {
    if(me != 0){

        // call all of the commands
        for(int c = 0; c < nCommands; c++){

            Vector vec = this.execute(commands[c]);
            
            Object[] objArray = vec.toArray(); 

            // Convert Object[] to String[] 
            output[me - 1][c] = Arrays.copyOf(objArray, objArray.length, String[].class); 

        }

        me += 1;
        me %= nodes.length;
        hop(nodes[me], "run", null);

    } else {

        Date endTime = new Date( );

        if(print) printOutput();
        else{
            int count = 0;
            for(int i = 0; i < nNodes; i++){
                for(int j = 0; j < nCommands; j++){
                    count += output[i][j].length;
                }
            }
            System.out.println("Count: " + count);
        }
                
        System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()) );
        System.err.println("Execution Time: " + (endTime.getTime() - startTime.getTime()) );
    }
}

//Description: reads input arguments assuming they are valid
public void setup(String[] args){

    try {
        // Assume the input is valid
        int offset = 0;
        
        print = args[0].equals("P"); offset++;

        nNodes = Integer.parseInt(args[offset]); offset++;

        if ( nNodes <= 0 ){
            throw new Exception( );
        }

        nodes = new String[nNodes + 1]; // add the host to the list of nodes
        
        {
            InetAddress inetaddr = InetAddress.getLocalHost( );
            nodes[0] = inetaddr.getHostName( );
        }

        for(int i = 1; i < nNodes + 1; i++){
            nodes[i] = args[offset]; offset++;
        }

        nCommands = Integer.parseInt(args[offset]); offset++;

        if ( nCommands <= 0 ){
            throw new Exception( );
        }

        commands = new String[nCommands];
        for(int i = 0; i < nCommands; i++){ // leave the last command
            commands[i] = args[offset]; offset++;

            if(i == nCommands -1){
                for(; offset < args.length; offset++){
                    commands[i] += " " + args[offset];
                }
            }
        }

        me = 0;

        output = new String[nNodes][nCommands][1];

        // print arguments
        System.out.println("print: " + print);
        System.out.println("nNodes: " + nNodes);
        printArray(nodes);
        System.out.println();
        System.out.println("nCommands: " + nCommands);
        printArray(commands);
        System.out.println();

    } catch ( Exception e ) {
        System.err.println( "Arguments are incorrect" );
        System.exit( -1 );
    }

}

// executes a command and returns its ouput in a vector of lines
public Vector execute( String command ) {
	Vector<String> outVec = new Vector<String>( );
	String line;
	try {
	    Runtime runtime = Runtime.getRuntime( );
	    Process process = runtime.exec( command );
	    InputStream input = process.getInputStream();
	    BufferedReader bufferedInput
		= new BufferedReader( new InputStreamReader( input ) );
	    while ( ( line = bufferedInput.readLine( ) ) != null ) {
		    outVec.addElement( line );
	    }
	} catch ( IOException e ) {
	    e.printStackTrace( );
	    return outVec;
	}
	return outVec;
}

// Description: prints the output of the client
public void printOutput(){

    for(int n = 0; n < nNodes; n++){
        System.out.println("---------------------------------------------------");
        System.out.println(nodes[n + 1]);

        for(int c = 0; c < nCommands; c++){
            System.out.println("| " + nodes[n + 1] + " " + commands[c]);
            
            for(int i = 0; i < output[n][c].length; i++){
                System.out.println("| | " + output[n][c][i]);
            }

            System.out.println("| | ");
            System.out.println("| | count: " + output[n][c].length);
            System.out.println();

        }

        System.out.println();
        System.out.println();

    }

}

// prints an array
public void printArray(String[] arr){
    System.out.print("{");
    for(int i = 0; i < arr.length; i++){
        System.out.print(arr[i]);
        if(i < arr.length -1) System.out.print(", ");
    }
    System.out.print("}");
}

}
