import mpi.*;   // for mpiJava

public class MyProgram {
    private final static int aSize = 100; // the size of dArray
    private final static int master = 0;  // the master rank
    private final static int tag = 0;     // Send/Recv's tag is always 0.

    public static void main( String[] args ) throws MPIException {
	// Start the MPI library.
	MPI.Init( args );
	
	// compute my own stripe
	int stripe = aSize / MPI.COMM_WORLD.Size( ); // each portion of array
	double[] dArray = null;

	if ( MPI.COMM_WORLD.Rank( ) == 0 ) { // master

	    // initialize dArray[100].
	    dArray = new double[aSize];
	    for ( int i = 0; i < aSize; i++ )
		dArray[i] = i;

	    // send a portion of dArray[100] to each slave
		// (1) implement by yourself
		for(int i = 1; i < MPI.COMM_WORLD.Size( ); i++){
			MPI.COMM_WORLD.Send(dArray, stripe*i, stripe, MPI.DOUBLE, i, tag);
		}

	}
	else { // slaves: rank 1 to rank n - 1

	    // allocates dArray[25].
	    dArray = new double[stripe];

	    // receive a portion of dArray[100] from the master
		// (2) implement by yourself
		MPI.COMM_WORLD.Recv(dArray, 0, stripe, MPI.DOUBLE, master, tag);

	}

	// compute the square root of each array element
	for ( int i = 0; i < stripe; i++ )
	    dArray[i] = Math.sqrt( dArray[i] );

	if ( MPI.COMM_WORLD.Rank( ) == 0 ) { // master

	    // receive answers from each slave
		// (3) implement by yourself
		for(int i = 1; i < MPI.COMM_WORLD.Size( ); i++){
			MPI.COMM_WORLD.Recv(dArray, stripe*i, stripe, MPI.DOUBLE, i, tag);
		}

	    // print out the results
	    for ( int i = 0; i < aSize; i++ )
		System.out.println( "dArray[ " + i + " ] = " + dArray[i] );
	}
	else { // slaves: rank 0 to rank n - 1
 
	    // send the results back to the master
		// (4) implement by yourself
		MPI.COMM_WORLD.Send(dArray, 0, stripe, MPI.DOUBLE, master, tag);

	}

	// Terminate the MPI library.
	MPI.Finalize( );
    }
}
