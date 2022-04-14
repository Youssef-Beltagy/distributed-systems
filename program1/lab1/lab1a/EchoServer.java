/**
 * @author Youssef Beltagy
 */

import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main( String args[] ) {

		if ( args.length != 1 ) {
			System.err.println( "usage: java TcpServer port" );
			return;
        }
		try {	
			// create a server socket
			ServerSocket server = new ServerSocket( Integer.parseInt( args[0] ) );
			while ( true ) {
			// accept a new connection from a new client
				Socket client = server.accept( );

				// read a message
				InputStream is = client.getInputStream( );
				
				// message size can only be 512 char becaus java characters
				// require two bytes
				byte[] data = new byte[1024];
				

				// read a message
				is.read( data );

				// print the message
				System.out.println( new String( data ) );

				OutputStream os = client.getOutputStream( );

				// repeat the message
				os.write( data );

				client.close( );
	    	}
		} catch ( IOException e ) {
			e.printStackTrace( );
		}
    }
}
