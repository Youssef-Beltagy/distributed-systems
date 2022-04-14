import java.net.*;
import java.io.*;

public class TcpServerByte {
    public static void main( String args[] ) {
	if ( args.length != 2 ) {
	    System.err.println( "usage: java TcpServer port size" );
	    return;
	}
	try {
	    ServerSocket svr = new ServerSocket( Integer.parseInt( args[0] ) );
	    byte multiplier = 1;
	    int size = Integer.parseInt( args[1] );
	    while ( true ) {
		// establslih a connection
		Socket socket = svr.accept( );    
		InputStream in = socket.getInputStream( );
		OutputStream out = socket.getOutputStream( );
	    
		byte[] data = new byte[size];      // receive data
		in.read( data );
		for ( int i = 0; i < size; i++ )   // modify data
		    data[i] *= multiplier;
		out.write( data );                 // send back data
		
		socket.close( );                   // close the connection
		multiplier *= 2;
	    }

	} catch( Exception e ) {
	    e.printStackTrace( );
	}
    }
}
