//An experiment to see the execution speed of RMI

import java.io.*;   // for output stream
import java.rmi.*;  // for rmi
import java.util.*; // for vector
import java.net.*;  // inetaddr

public class UnixClient {

private static boolean print = false;
private static int port;
private static int nServers;
private static String[] servers;
private static int nCommands;
private static String[] commands;
private static String[][][] output;

// parses arguments and executes RMI
public static void main( String args[] ) {

    setup(args); // Read program arguments

    // start a timer
    Date startTime = new Date( );

    try {
        // for every server
        for(int s = 0; s < nServers; s++){

            ServerInterface serverObject =  ( ServerInterface )
            Naming.lookup( "rmi://" + servers[s] + ":" + port + "/unixserver" );

            // call all of the commands
            for(int c = 0; c < nCommands; c++){

                Vector vec = serverObject.execute(commands[c]);
                
                Object[] objArray = vec.toArray(); 
  
                // Convert Object[] to String[] 
                output[s][c] = Arrays.copyOf(objArray, objArray.length, String[].class); 

            }

        }
    }
    catch ( Exception e ) {
        e.printStackTrace( );
        System.exit( -1 );
    }

    Date endTime = new Date( );

    if(print) printOutput();
    else{
        int count = 0;
        for(int i = 0; i < nServers; i++){
            for(int j = 0; j < nCommands; j++){
                count += output[i][j].length;
            }
        }
        System.out.println("Count: " + count);
    }
            
    System.err.println("Execution Time: " + (endTime.getTime() - startTime.getTime()) );
    System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()) );
    // prints to err to avoid getting caught in the pipe
}

//Description: reads input arguments assuming they are valid
public static void setup(String[] args){

    try {
        // Assume the input is valid
        int offset = 0;
        
        print = args[0].equals("P"); offset++;

        port = Integer.parseInt(args[offset]); offset++;

        if ( port < 5001 || port > 65535 ){
            throw new Exception( );
        }

        nServers = Integer.parseInt(args[offset]); offset++;

        if ( nServers <= 0 ){
            throw new Exception( );
        }

        servers = new String[nServers];
        for(int i = 0; i < nServers; i++){
            servers[i] = args[offset]; offset++;
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

        output = new String[nServers][nCommands][1];

        System.out.println("print: " + print);
        System.out.println("nServers: " + nServers);
        printArray(servers);
        System.out.println();
        System.out.println("nCommands: " + nCommands);
        printArray(commands);
        System.out.println();
        

    } catch ( Exception e ) {
        System.err.println( "Arguments are incorrect" );
        System.exit( -1 );
    }

}

// Description: prints the output of the client
public static void printOutput(){

    for(int s = 0; s < nServers; s++){
        System.out.println("---------------------------------------------------");
        System.out.println(servers[s]);

        for(int c = 0; c < nCommands; c++){
            System.out.println("| " + servers[s] + " " + commands[c]);
            
            for(int i = 0; i < output[s][c].length; i++){
                System.out.println("| | " + output[s][c][i]);
            }

            System.out.println("| | ");
            System.out.println("| | count: " + output[s][c].length);
            System.out.println();

        }

        System.out.println();
        System.out.println();

    }

}

// prints an array
public static void printArray(String[] arr){
    System.out.print("{");
    for(int i = 0; i < arr.length; i++){
        System.out.print(arr[i]);
        if(i < arr.length -1) System.out.print(", ");
    }
    System.out.print("}");
}

}
