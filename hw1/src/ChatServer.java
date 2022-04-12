
/**
 * ChatServer.java
 * A central chat server.
 *
 * @author  Youssef Beltagy
 * @version 4/20/2020
 */

import java.net.*;          // for Socket
import java.util.*;         // for Vector
import java.io.*;           // for IOException

public class ChatServer {

    public static void main( String args[] ) {
        // Check # args.
        if ( args.length != 1) {
            System.err.println( "Syntax: java ChatClient <port>" );
            System.exit( 1 );
        }
    
        // convert args[0] into an integer that will be used as port.
        int port = Integer.parseInt( args[0] );

        // A vector of all the connections
        Vector<ChatConnection> connections = new Vector<ChatConnection>();
    
        // instantiate the main body of ChatServer application.
        try {
            ServerSocket svr = new ServerSocket(port);
            svr.setSoTimeout(500);  // set the timeout
            
            while ( true ) {
                Socket newClient;
                try{
                    newClient = svr.accept();
                } catch(SocketTimeoutException te){
                    newClient = null;
                }

                // Add a new conecction if newClient is not null
                if(newClient != null){
                    connections.add(new ChatConnection(newClient));
                }

                Set<Integer> toDelete = new HashSet<Integer>();
                 

                //loop through all connections.
                for(int i = 0; i < connections.size(); i++){

                    // while there is a message in the current connection,
                    while(connections.get(i).hasMessage()){
                        
                        // read the message and loop through all other connections
                        String message = connections.get(i).name + ": "
                                     + connections.get(i).getMessage();
                        for(int j = 0; j < connections.size(); j++){

                            
                            connections.get(j).writeMessage(message);
                            

                            // if you fail to write to a connection,
                            // then assume it is bad. Store its indices in a set.
                            if(connections.get(j).valid == false){
                                toDelete.add(j);
                            }

                        }
                    }
                }

                // Delete all indices in the set
                for(Integer cur : toDelete){
                    connections.remove(cur.intValue());
                }

            }
    
        } catch( Exception e ) {
            e.printStackTrace( );
        }
    }

}