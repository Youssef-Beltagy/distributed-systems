// Author: Youssef Beltagy
// Last Edited: 5/8/2020 --> 4/12/2022

// Description: Heat conduction simulation using euler's equation.
// Read the report for more details.

import java.util.Date;
import mpi.*;

public class Heat2DPAdvanced {
	private static double a = 1.0; // heat speed
	private static double dt = 1.0; // time quantum
	private static double dd = 2.0; // change in system

	private static double[] z = null; // The matrix representing heat

	private static int size = 100; // length of one side of the matrix
	private static int max_time = 1; // max execution iterations
	private static int heat_time = 1; // num of iterations to heat
	private static int interval = 0; // print at every interval
	private static double r = 1; // heat transfer coefficient

	private static int myrank = 0; // This process's rank
	private static int nprocs = 1; // number of processes
	private static int tag = 0; // tag to use mpi send and receive

	private static int[] offCols = { 0 }; // array of offsets for every process
	private static int[] numCols = { 100 }; // array of number of cols for every process

	public static void main(String[] args) throws MPIException {

		// verify arguments
		if (args.length != 4) {

			System.out.println("usage: " +
					"java Heat2DAdvanced size max_time heat_time interval");

			System.exit(-1);
		}

		// initialize MPI
		MPI.Init(args);

		// Read the program arguments and set the data variables
		setup(args);

		// start a timer
		Date startTime = new Date();

		// compute the matrix
		compute();

		// finish the timer and print the execution time
		if (myrank == 0) {

			Date endTime = new Date();
			System.out.println("Elapsed time = " +
					(endTime.getTime() - startTime.getTime()));
		}

		// finalize MPI
		MPI.Finalize();

	}

	// Description: initializes the matrix and calculates the elements of
	// offCols and numCols
	// Preconditions: There are four arguments and MPI.init() was called
	// Postconditions: The Matrix is initialized and ready for simulation.
	public static void setup(String[] args) throws MPIException {

		size = Integer.parseInt(args[0]);
		max_time = Integer.parseInt(args[1]);
		heat_time = Integer.parseInt(args[2]);
		interval = Integer.parseInt(args[3]);
		r = a * dt / (dd * dd);

		myrank = MPI.COMM_WORLD.Rank();
		nprocs = MPI.COMM_WORLD.Size();
		tag = 0;

		offCols = new int[nprocs];
		numCols = new int[nprocs];

		int stripe = size / nprocs;
		int remainder = size % nprocs;

		for (int i = 0; i < nprocs; i++) {
			numCols[i] = stripe + ((i < remainder) ? 1 : 0);
			offCols[i] = stripe * i + ((i < remainder) ? i : remainder);
		}

		// create a space
		if (myrank == 0) {

			z = new double[2 * size * size];

		} else if (myrank == nprocs - 1) {

			// add an auxiliary column
			z = new double[2 * size * (numCols[myrank] + 1)];

		} else {

			// add two auxiliary columns. One on each side
			z = new double[2 * size * (numCols[myrank] + 2)];
		}

		for (int i = 0; i < z.length; i++) {
			// neither hot nor cold.
			z[i] = 0;
		}

	}

	// Description: gets the offset of an array element, given its
	// coordinates. Considers the differnt sizes of arrays
	// for different processes.
	// Preconditions: The sizes ans offsets of the sub-matrices are known
	// Postconditions: given the coordinates of a point inside the whole matrix,
	// will return its offset inside this process.
	public static int pos(int p, int x, int y) {

		if (myrank == 0) {
			return p * size * size + x * size + y;
		}

		if (myrank == nprocs - 1) {
			return p * size * (numCols[myrank] + 1) + (x - offCols[myrank] + 1) * size + y;
		}

		return p * size * (numCols[myrank] + 2) + (x - offCols[myrank] + 1) * size + y;

	}

	// Description: Shares all the sub-matrices with the parent process (rank 0)
	// then prints the matrix inside the parent process
	// Preconditions: The matrix and processes are initialized
	// Postconditions: rank 0 contains all the information inside the other ranks.
	// The matrix is printed to the console.
	public static void printMatrix(int t) throws MPIException {

		if(myrank != 0) return;

		int p = t % 2;
		
		System.out.println("time = " + t);

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++)
				System.out.print((int) (Math.floor(z[pos(p, x, y)] / 2)) + " ");

			System.out.println();
		}

		System.out.println();

	}

	// Description: Shares all the sub-matrices with the parent process (rank 0)
	// Preconditions: The matrix and processes are initialized
	// Postconditions: rank 0 contains all the information inside the other ranks.
	public static void shareMatrix(int p) throws MPIException {
		if (nprocs < 2)
			return;

		if (myrank == 0) {

			for (int i = 1; i < nprocs; i++) {
				MPI.COMM_WORLD.Recv(z, p * size * size + offCols[i] * size,
						numCols[i] * size, MPI.DOUBLE, i, tag);
			}

		} else {

			if (myrank == nprocs - 1) {
				MPI.COMM_WORLD.Send(z, p * (numCols[myrank] + 1) * size + size,
						numCols[myrank] * size, MPI.DOUBLE, 0, tag);
			} else {
				MPI.COMM_WORLD.Send(z, p * (numCols[myrank] + 2) * size + size,
						numCols[myrank] * size, MPI.DOUBLE, 0, tag);
			}

		}

	}

	// Description: Shares the edge columns between the processes
	// Preconditions: the mpi processes and the matrix are initialized
	// Postconitions: the edges are shared between processes
	public static void shareEdges(int p) throws MPIException {

		if (nprocs < 2)
			return; // Don't share if there is only one process.

		// To avoid deadlocks:
		// If the process is even, send then receive.
		// If the pcocess is odd, receive then send.

		// The first and last ranks communicate with only one process.
		// Other ranks communicate with two processes.
		if (myrank == 0) {

			// Send the right most column of rank 0
			MPI.COMM_WORLD.Send(z, p * size * size + (numCols[myrank] - 1) * size,
					size, MPI.DOUBLE, myrank + 1, tag);

			// Assign the leftmost column of rank 1 next to rank 0's columns
			MPI.COMM_WORLD.Recv(z, p * size * size + numCols[myrank] * size,
					size, MPI.DOUBLE, myrank + 1, tag);

		} else if (myrank == nprocs - 1) {

			// If it is even
			if (myrank % 2 == 0) {

				// send the left most column
				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 1) + size,
						size, MPI.DOUBLE, myrank - 1, tag);

				// Assign the rightmost column of the previous rank into the auxiliary column
				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 1),
						size, MPI.DOUBLE, myrank - 1, tag);

			} else { // Do the same but in opposite order

				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 1),
						size, MPI.DOUBLE, myrank - 1, tag);

				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 1) + size,
						size, MPI.DOUBLE, myrank - 1, tag);

			}

		} else {

			if (myrank % 2 == 0) {

				// send rht rightmost column to the next rank
				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 2) + numCols[myrank] * size,
						size, MPI.DOUBLE, myrank + 1, tag);

				// send the leftmost column to the previous rank
				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 2) + size,
						size, MPI.DOUBLE, myrank - 1, tag);

				// receive the leftmost column of the next rank into the auxiliary column
				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 2) + (numCols[myrank] + 1) * size,
						size, MPI.DOUBLE, myrank + 1, tag);

				// receive the rightmost column of the previous rank into the left auxiliary
				// column
				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 2),
						size, MPI.DOUBLE, myrank - 1, tag);

			} else { // do the same but in opposite order

				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 2),
						size, MPI.DOUBLE, myrank - 1, tag);

				MPI.COMM_WORLD.Recv(z, p * size * (numCols[myrank] + 2) + (numCols[myrank] + 1) * size,
						size, MPI.DOUBLE, myrank + 1, tag);

				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 2) + size,
						size, MPI.DOUBLE, myrank - 1, tag);

				MPI.COMM_WORLD.Send(z, p * size * (numCols[myrank] + 2) + numCols[myrank] * size,
						size, MPI.DOUBLE, myrank + 1, tag);

			}

		}

	}

	// Description: The heat simulation. The loop that runs euler's equation.
	// Precondition: The matrix and mpi process are initialized
	// Postconditions: the simulation is done and printed to the console
	public static void compute() throws MPIException {

		// simulate heat diffusion
		for (int t = 0; t < max_time; t++) {
			int p = t % 2; // p = 0 or 1: indicates the phase

			// two left-most columns are made identical
			if (myrank == 0) {
				for (int y = 0; y < size; y++) {
					z[pos(p, 0, y)] = z[pos(p, 1, y)];
				}
			}

			// right most columns are made identical
			if (myrank == nprocs - 1) {
				for (int y = 0; y < size; y++) {
					z[pos(p, size - 1, y)] = z[pos(p, size - 2, y)];
				}
			}

			// Get the starting and ending position for every stripe
			int initialX = offCols[myrank];
			int maxX = offCols[myrank] + numCols[myrank];

			// two upper and lower rows are made identical
			for (int x = initialX; x < maxX; x++) {
				z[pos(p, x, 0)] = z[pos(p, x, 1)];
				z[pos(p, x, size - 1)] = z[pos(p, x, size - 2)];
			}

			// keep heating the top as long as t < heat_time
			// Could be made more efficient with a bunch of if-statements
			// but I don't feel it a good investment of my time.
			if (t < heat_time) {
				for (int x = size / 3; x < size / 3 * 2; x++)
					if (x >= initialX && x < maxX)
						z[pos(p, x, 0)] = 19.0; // heat
			}

			// Synchronize the processes
			shareEdges(p);

			// display intermediate results
			if (interval != 0 && (t % interval == 0 || t == max_time - 1)) {
				shareMatrix(p);
				printMatrix(t);
			}

			// perform forward Euler method
			int p2 = (p + 1) % 2;
			initialX = (myrank == 0) ? 1 : offCols[myrank];
			maxX = (myrank == nprocs - 1) ? size - 1 : offCols[myrank] + numCols[myrank];
			for (int x = initialX; x < maxX; x++) {

				for (int y = 1; y < size - 1; y++) {

					z[pos(p2, x, y)] = z[pos(p, x, y)] +
							r * (z[pos(p, x + 1, y)] - 2 * z[pos(p, x, y)] + z[pos(p, x - 1, y)]) +
							r * (z[pos(p, x, y + 1)] - 2 * z[pos(p, x, y)] + z[pos(p, x, y - 1)]);

				}

			}

		} // end of simulation

	}
}
