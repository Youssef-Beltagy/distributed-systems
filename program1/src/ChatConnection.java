/**
 * A connection between a server and a client
 * chating program.
 *
 * @author  Youssef Beltagy
 * @version 4/20/2020
 */

import java.net.*;          // for Socket
import java.io.*;           // for IOException

public class ChatConnection {
    private Socket socket;           // a socket connection to a chat client
    private InputStream rawIn;       // an input stream from the client
    private DataInputStream in;      // a filtered input stream from the client
    private DataOutputStream out;    // a filtered output stream to the client
    public String name;              // The name of the client
    public boolean valid;            // Whether this connection is valid or not

     /**
     * Initializes a connection given a socket.
     * 
     * @param newSocket the socket of the connection
     */
    public ChatConnection(Socket newSocket){
        if(newSocket != null){
            try{
            
                socket = newSocket;
                rawIn = socket.getInputStream();
                in = new DataInputStream(rawIn);
                out = new DataOutputStream(socket.getOutputStream());
                valid = true;
                name = in.readUTF();
    
            }catch(Exception e ) {
                e.printStackTrace( );
            }

        }else{// newSocket is null
            socket = null;
            rawIn = null;
            in = null;
            out = null;
            name = null;
            valid = false;
        }

    }

    /**
     * @return whether the connection has a message or not.
     */
    public boolean hasMessage(){
        try{
            return (rawIn.available() > 0);
        }catch(Exception e ) {
            return false;
        }
    }

    /**
     * Assumes there is a message in the ObjectInputStream
     * 
     * @return reads and returns the message 
     */
    public String getMessage(){
        try{
            return in.readUTF();
        }catch(Exception e ) {
            return null;
        }

    }

    /**
     * Attempts to write to the object output stream.
     * If fails to write, changes valid to false to signify
     * that the connection is no longer valid.
     * 
     * @return a boolean that represents whether the writing operation
     *          was successful or not.
     */
    public boolean writeMessage(String message){
        try{
            out.writeUTF(message);
            return true;
        }catch(Exception e ) {
            valid = false;
            return false;
        }
    }

}