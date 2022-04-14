import java.io.*;
import UWAgent.*;

public class MyAgent extends UWAgent implements Serializable {

    private String destination = null;

    public MyAgent( String[] args ) {

	    System.out.println( "Injected" );
    
    }

    public MyAgent( ) { 

        System.out.println( "Injected" );

    }

    public void init( ) {

        System.out.println("Wash the potatos and cut them into french fries.");
        // testing sending integers
        String[] args = new String[1];
        args[0] = "" + 20;
        hop( "cssmpi2h", "fry", args);

    }

    public void fry(String[] args) {
        // testing receiving integers
        System.out.println("Fry the potatos in oil." + Integer.parseInt(args[0]));
        hop( "cssmpi3h", "eat", null);
    }

    public void eat(){
        System.out.println("Eat the potatos");
    }
}
