/**
 * @author Youssef Beltagy
*/

import java.net.*;
import java.io.*;

public class TcpClientDouble {
    public static void main( String args[] ) {
	if ( args.length != 3 ) {
	    System.err.println( "usage: java TcpClient port size server_ip" );
	    return;
	}
	try {
	    // establish a connection
		Socket socket = new Socket( args[2], Integer.parseInt( args[0] ) );

		OutputStream oStream = socket.getOutputStream( );
		InputStream iStream = socket.getInputStream( );
		
		ObjectOutputStream objectOut = new ObjectOutputStream(oStream);
		ObjectInputStream objectIn = new ObjectInputStream(iStream);

	    int size = Integer.parseInt( args[1] );
		double[] data = new double[size];       // initialize data
		
	    for ( int i = 0; i < size; i++ )
		data[i] = ( double )( i % 128);

	    objectOut.writeObject( data );                  // send data
		data = (double[]) objectIn.readObject();         // receive data
		
	    for ( int i = 0; i < size; i++ )    // print results
		System.out.println( data[i] );

	    socket.close( );                    // close the connection
	} catch( Exception e ) {
	    e.printStackTrace( );
	}
    }
}
