# Lab2b

In this lab, I wrote a program that uses a message passing interface daemon. The program calculates the square root of every element in an array of 100 elements from 0 to 99 by dividing the array into subarrays and sending a subarray to each computing node.

The program has a bug, though. The number of computing nodes has to divide into 100. Otherwise, a remainder number of elements will remain as they are at the end of the array.

## Code

```java
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

```

## Output

```shell
[ybeltagy@cssmpi1h Program2]$ javac MyProgram.java
[ybeltagy@cssmpi1h Program2]$ mpirun -n 4 java MyProgram
dArray[ 0 ] = 0.0
dArray[ 1 ] = 1.0
dArray[ 2 ] = 1.4142135623730951
dArray[ 3 ] = 1.7320508075688772
dArray[ 4 ] = 2.0
dArray[ 5 ] = 2.23606797749979
dArray[ 6 ] = 2.449489742783178
dArray[ 7 ] = 2.6457513110645907
dArray[ 8 ] = 2.8284271247461903
dArray[ 9 ] = 3.0
dArray[ 10 ] = 3.1622776601683795
dArray[ 11 ] = 3.3166247903554
dArray[ 12 ] = 3.4641016151377544
dArray[ 13 ] = 3.605551275463989
dArray[ 14 ] = 3.7416573867739413
dArray[ 15 ] = 3.872983346207417
dArray[ 16 ] = 4.0
dArray[ 17 ] = 4.123105625617661
dArray[ 18 ] = 4.242640687119285
dArray[ 19 ] = 4.358898943540674
dArray[ 20 ] = 4.47213595499958
dArray[ 21 ] = 4.58257569495584
dArray[ 22 ] = 4.69041575982343
dArray[ 23 ] = 4.795831523312719
dArray[ 24 ] = 4.898979485566356
dArray[ 25 ] = 5.0
dArray[ 26 ] = 5.0990195135927845
dArray[ 27 ] = 5.196152422706632
dArray[ 28 ] = 5.291502622129181
dArray[ 29 ] = 5.385164807134504
dArray[ 30 ] = 5.477225575051661
dArray[ 31 ] = 5.5677643628300215
dArray[ 32 ] = 5.656854249492381
dArray[ 33 ] = 5.744562646538029
dArray[ 34 ] = 5.830951894845301
dArray[ 35 ] = 5.916079783099616
dArray[ 36 ] = 6.0
dArray[ 37 ] = 6.082762530298219
dArray[ 38 ] = 6.164414002968976
dArray[ 39 ] = 6.244997998398398
dArray[ 40 ] = 6.324555320336759
dArray[ 41 ] = 6.4031242374328485
dArray[ 42 ] = 6.48074069840786
dArray[ 43 ] = 6.557438524302
dArray[ 44 ] = 6.6332495807108
dArray[ 45 ] = 6.708203932499369
dArray[ 46 ] = 6.782329983125268
dArray[ 47 ] = 6.855654600401044
dArray[ 48 ] = 6.928203230275509
dArray[ 49 ] = 7.0
dArray[ 50 ] = 7.0710678118654755
dArray[ 51 ] = 7.14142842854285
dArray[ 52 ] = 7.211102550927978
dArray[ 53 ] = 7.280109889280518
dArray[ 54 ] = 7.3484692283495345
dArray[ 55 ] = 7.416198487095663
dArray[ 56 ] = 7.483314773547883
dArray[ 57 ] = 7.54983443527075
dArray[ 58 ] = 7.615773105863909
dArray[ 59 ] = 7.681145747868608
dArray[ 60 ] = 7.745966692414834
dArray[ 61 ] = 7.810249675906654
dArray[ 62 ] = 7.874007874011811
dArray[ 63 ] = 7.937253933193772
dArray[ 64 ] = 8.0
dArray[ 65 ] = 8.06225774829855
dArray[ 66 ] = 8.12403840463596
dArray[ 67 ] = 8.18535277187245
dArray[ 68 ] = 8.246211251235321
dArray[ 69 ] = 8.306623862918075
dArray[ 70 ] = 8.366600265340756
dArray[ 71 ] = 8.426149773176359
dArray[ 72 ] = 8.48528137423857
dArray[ 73 ] = 8.54400374531753
dArray[ 74 ] = 8.602325267042627
dArray[ 75 ] = 8.660254037844387
dArray[ 76 ] = 8.717797887081348
dArray[ 77 ] = 8.774964387392123
dArray[ 78 ] = 8.831760866327848
dArray[ 79 ] = 8.888194417315589
dArray[ 80 ] = 8.94427190999916
dArray[ 81 ] = 9.0
dArray[ 82 ] = 9.055385138137417
dArray[ 83 ] = 9.1104335791443
dArray[ 84 ] = 9.16515138991168
dArray[ 85 ] = 9.219544457292887
dArray[ 86 ] = 9.273618495495704
dArray[ 87 ] = 9.327379053088816
dArray[ 88 ] = 9.38083151964686
dArray[ 89 ] = 9.433981132056603
dArray[ 90 ] = 9.486832980505138
dArray[ 91 ] = 9.539392014169456
dArray[ 92 ] = 9.591663046625438
dArray[ 93 ] = 9.643650760992955
dArray[ 94 ] = 9.695359714832659
dArray[ 95 ] = 9.746794344808963
dArray[ 96 ] = 9.797958971132712
dArray[ 97 ] = 9.848857801796104
dArray[ 98 ] = 9.899494936611665
dArray[ 99 ] = 9.9498743710662
[ybeltagy@cssmpi1h Program2]$
```

