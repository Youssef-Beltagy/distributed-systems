import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ServerImplementation extends UnicastRemoteObject
    implements ServerInterface {
    public ServerImplementation( ) throws RemoteException {
	super( );
    }

    public synchronized void echo( ClientInterface client, String message ) 
	throws RemoteException {
        client.receiveMessage(message);
    }
}
