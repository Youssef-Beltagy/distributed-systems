import java.net.*;
import java.io.*;

public class TcpClientByte {
    public static void main( String args[] ) {
	if ( args.length != 3 ) {
	    System.err.println( "usage: java TcpClient port size server_ip" );
	    return;
	}
	try {
	    // establish a connection
	    Socket socket = new Socket( args[2], Integer.parseInt( args[0] ) );
	    OutputStream out = socket.getOutputStream( );
	    InputStream in = socket.getInputStream( );

	    int size = Integer.parseInt( args[1] );
	    byte[] data = new byte[size];       // initialize data
	    for ( int i = 0; i < size; i++ )
		data[i] = ( byte )( i % 128);

	    out.write( data );                  // send data
	    in.read( data );                    // receive data
	    for ( int i = 0; i < size; i++ )    // print results
		System.out.println( data[i] );

	    socket.close( );                    // close the connection
	} catch( Exception e ) {
	    e.printStackTrace( );
	}
    }
}
