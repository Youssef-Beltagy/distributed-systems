import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class ClientImplementation extends UnicastRemoteObject
    implements ClientInterface {
  
    public ClientImplementation( ) throws RemoteException {
	super( );
    }

    public void receiveMessage( String message ) {
	    System.out.println(message);
    }
}
