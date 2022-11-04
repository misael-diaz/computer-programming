/*
 * Algorithms and Complexity                               October 25, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Implements the Brute Force and Divide and Conquer Algorithms that solve
 * the Closest Pair problem.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] Sorting Custom Objects Tutorial: (
 *      www.codejava.net/java-core/collections/
 *      sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */


import java.util.Random;


public class Ensemble
// Particle Ensemble Class
{


	/* data members */


	private double etime;		// elapsed time (nanoseconds)
	private long operations;	// #operations
	private List particles;		// particle data placeholder
	private int ensembleSize;	// #particles


	/* constructors */


	Ensemble ()
	// (useless) default constructor
	{
		this.etime = ( (double) 0x00000000 );
		this.operations = ( (long) 0x00000000 );
		this.particles = new List();
		this.ensembleSize = particles.size();
	}


	Ensemble (int size)
	// constructs an ensemble of size `size'
	{
		this.etime = ( (double) 0x00000000 );
		this.operations = ( (long) 0x00000000 );
		this.particles = create(size);
		this.ensembleSize = size;
	}


	/* getters */


	public double getOperations ()
	// returns a copy of the number of operations
	{
		return ( (double) this.operations );
	}


	public double getElapsedTime ()
	// returns a copy of the elapsed-time in nanoseconds
	{
		return this.etime;
	}


	/* methods */


	public double BruteForceMethod ()
	// Brute Force Method
	{
		this.operations = 0L;
		// solves the closest-pair problem if it makes sense
		if (this.ensembleSize >= 2)
			return this.bruteforce(this.particles);
		else
			return 0;
	}


	public double iBruteForceMethod ()
	// improved Brute Force Method
	{
		this.operations = 0L;
		// solves the closest-pair problem if it makes sense
		if (this.ensembleSize >= 2)
			return this.iBruteForce(this.particles);
		else
			return 0;
	}


	public double RecursiveMethod ()
	// Divide and Conquer Method
	{
		this.operations = 0L;
		// solves the closest-pair problem if it makes sense
		if (this.ensembleSize >= 2)
			return this.recursiveClosestPair(this.particles);
		else
			return 0;
	}


	public static void main (String [] args)
	{
		/*

		tests the implementation of the Brute Force and Divide and
		Conquer Algorithms.

		*/


		test();	// note: code has passed the test


		/* executes a sample run */


		// defines the system size
		int size = (0x00000100);
		// creates the system of particles (ensemble)
		Ensemble sys = new Ensemble(size);


		/* finds the Closest Pair with Brute Force */


		double minDistBruteForce = sys.BruteForceMethod();

		// reports system and runtime info on the console
		System.out.printf("\nBrute Force Method:\n");
		System.out.printf("size:  %d\n", size);
		System.out.printf("operations: %f\n", sys.getOperations());
		System.out.printf("runtime: %f\n", sys.getElapsedTime());


		/* applies the Divide and Conquer Method */


		double minDistRecursive = sys.RecursiveMethod();

		// reports system and runtime info on the console
		System.out.printf("\nDivide and Conquer Method:\n");
		System.out.printf("size:  %d\n", size);
		System.out.printf("operations: %f\n", sys.getOperations());
		System.out.printf("runtime: %f\n", sys.getElapsedTime());
	}


	/* implementations */


	private List create (int size)
	// generates a dataset of particle coordinates
	{
		return genDataSet(size);
	}


	private List genDataSet (int size)
	/*

	Synopsis:
	Generates a distinct dataset of coordinates by sampling values
	from a uniform pseudo-random number generator PRNG.

	Inputs:
	size		size of the dataset (number of particles)

	Output:
	list		a list that stores the dataset of coordinates

	*/
	{
		// creates a list for storing the coordinates
		List list = new List();
		// creates a pseudo-random number generator PRNG
		Random random = new Random();
		// defines limits for the particle coordinates
		int x_min = -size, x_max = size;
		int y_min = -2, y_max = 2;
		for (int i = 0; i != size; ++i)
		// generates the distinct set of (x, y) coordinates
		{
			int x = x_min + random.nextInt(x_max - x_min);
			int y = y_min + random.nextInt(y_max - y_min);
			Coord c = new Coord (x, y);
			while (list.search(c) >= 0)
			// generates a new coordinate if already in list
			{
				x = random.nextInt(size);
				y = random.nextInt(size);
				c = new Coord (x, y);
			}
			// inserts coordinate while keeping the list sorted
			list.insort(c);
		}

		return list;
	}


	private double bruteforce (List data)
	/*

	Synopsis:
	We use this method to time the Brute Force Algorithm that
	finds the closest pair. This version is analogous to the one
	that uses a Vector to store the coordinates of the particles.
	This was the initial implementation of the algorithm with a List.

	Input:
	data		coordinates of the particles

	Output:
	d_min		distance of the closest pair

	*/
	{
		// allocates placeholder for storing the closest pair
		Coord [] closestPair = new Coord[2];
		double tstart = System.nanoTime();
		// applies brute force algorithm on the whole dataset
		double d_min = distance (data, closestPair);
		double tend = System.nanoTime();

		// computes the elapsed time (nanoseconds)
		etime = (tend - tstart);

		/*

		Coord [] pair = closestPair;
		// unpacks the (x, y) coordinates of the closest pair
		int x1 = pair[0].getX(), y1 = pair[0].getY();
		int x2 = pair[1].getX(), y2 = pair[1].getY();

		// reports findings on the console
		System.out.printf("\nBrute Force Algorithm:\n");
		System.out.printf("d_min: %f\n", Math.sqrt(d_min));
		System.out.printf("(x1, y1): %2d, %2d\n", x1, y1);
		System.out.printf("(x2, y2): %2d, %2d\n", x2, y2);

		*/

		return (d_min);
	}


	private double iBruteForce (List data)
	/*

	Synopsis:
	Uses the improved Brute Force Algorithm to find the closest pair.
	This version uses forward iterators to reduce calls to the get()
	method, which always starts from the front (or head) of the list.

	Input:
	data		coordinates of the particles

	Output:
	d_min		distance of the closest pair

	*/
	{
		Coord [] closestPair = new Coord[2];
		double tstart = System.nanoTime();
		// applies brute force algorithm on the whole dataset
		double d_min = iBruteForce (data, closestPair);
		double tend = System.nanoTime();

		// computes the elapsed time (nanoseconds)
		etime = (tend - tstart);

		/*

		Coord [] pair = closestPair;
		// unpacks the (x, y) coordinates of the closest pair
		x1 = pair[0].getX(); y1 = pair[0].getY();
		x2 = pair[1].getX(); y2 = pair[1].getY();

		// reports findings on the console
		System.out.printf("\nBrute Force Algorithm:\n");
		System.out.printf("d_min: %f\n", Math.sqrt(minDist));
		System.out.printf("(x1, y1): %2d, %2d\n", x1, y1);
		System.out.printf("(x2, y2): %2d, %2d\n", x2, y2);

		*/

		return (d_min);
	}


	private double iBruteForce (List part, Coord [] closestPair)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole dataset.
	This version uses forward iterators.

	Inputs:
	part		partition (or whole dataset of coordinates)
	closestPair	placeholder for storing the closest pair

	Outputs:
	closestPair	the coordinates of the closest pair in partition
	d_min		distance of the closest pair

	*/
	{
		int size = part.size();
		double d_min = Double.POSITIVE_INFINITY;
		List.forwardIterator iIter = part.forwardIterator();
		for (int i = 0; i != (size - 1); ++i)
		{
			Coord P = iIter.get();

			List.forwardIterator jIter =
				part.forwardIterator(i + 1);
			for (int j = (i + 1); j != size; ++j)
			{

				Coord Q = jIter.get();

				double d = Coord.distance(P, Q);
				if (d < d_min)
				{
					closestPair[0] = P;
					closestPair[1] = Q;
					d_min = d;
				}

				++operations;
				jIter.next();
			}

			iIter.next();
		}

		return d_min;
	}


	/* Divide and Conquer */


	private List [] divide (List Px)
	/*

	Synopsis:
	Divides the dataset into left and right partitions.

	Input:
	Px		original x-y sorted particle coordinates

	Outputs:
	Lx		left partition
	Rx		right partition

	*/
	{
			// initializes left partition via copy constructor
			List Lx = new List(Px);
			// creates left and right partitions by division
			List Rx = Lx.bisect();
			// creates placeholder for returning partitions
			List [] partitions = {Lx, Rx};
			return partitions;
	}


	private double select (
		Coord[] closestPair, Coord[] pairLeft, Coord[] pairRight,
		double minDistLeft, double minDistRight
	)
	/*

	Synopsis:
	Selects the closest pair from the left and right partitions.

	Inputs:
	closestPair	placeholder for storing the closest pair
	pairLeft	closest pair in the left partition
	pairRight	closest pair in the right partition
	minDistLeft	distance of the closest pair in the left partition
	minDistRight	distance of the closest pair in the right partition

	Outputs:
	closestPair	the closest of the left and right closest pairs
	minDist		the distance of the new closest pair

	*/
	{
		double minDist;
		if (minDistLeft < minDistRight)
		{
			minDist = minDistLeft;
			// invokes copy constructor to get a shallow copy
			closestPair[0] = new Coord(pairLeft[0]);
			closestPair[1] = new Coord(pairLeft[1]);
		}
		else
		{
			minDist = minDistRight;
			// invokes copy constructor to get a shallow copy
			closestPair[0] = new Coord(pairRight[0]);
			closestPair[1] = new Coord(pairRight[1]);
		}

		/*

		NOTE:
		Generally speaking, it is an error prone practice to get
		a copy of the reference of a local object. This is why we
		opt to copy the object data instead into the placeholder
		that stores the closest pair. Note that the placeholder
		was allocated prior to invoking the recursive method so
		it won't get destroyed until the closest pair is found.
		On the other hand, the placeholders that store the pairs
		in the left and right partitions will get destroyed and
		we could stumble upon a NullPointerException if we get a
		copy of any of their references.

		*/

		return minDist;
	}


	private double combine (
		List Lx, List Rx, Coord [] closestPair, double d_min
	)
	/*

	Synopsis:
	Looks for the closest pair at the interface of the left and right
	partitions (dubbed as the middle partition Mx).

	Inputs:
	Lx		left partition
	Rx		right partition
	closestPair	placeholder for the closest pair coordinates
	d_min		distance of the (current) closest pair

	Output:
	d_min		distance of the (possibly new) closest pair

	*/
	{
		/*

		Right Partition Traversal

		Assumes that there are no particles on the right partition
		that could form a pair with any of the particles on the
		left partition which could be closer than the (current)
		closest pair. (Recall that at this point we have already
		considered all possible pairs within the partitions, thus
		we need not to do that again.)

		To keep track of the number of potential candidates we use
		the asymmetric range (arange for short) [b, e), where `b'
		is the beginning limit and `e' is the end limit. The number
		of candidates is computed via the difference (e - b).

		By initializing the range to zero (or empty) we are saying
		that there are no potential closest pair candidates between
		the left and right partitions in accordance with our
		initial assumption.

		The algorithm that follows (for-loop) determines how many
		particles in the right partition could potentially form
		a pair closer than the (current) closest pair.

		Candidate particles on the right partition are selected
		by incremening the end of the arange [b, e). Note that the
		first particle on the right partition is the closest to
		the particles on the left partition and so the arange must
		start with b = 0; also, this is why we only adjust the end
		limit. We update the end limit by traversing the particles
		in the right partition in sequential order. We can afford
		to do that because of the inherent ordering which stems
		from the imposed x-axis sorting.

		If it turns out that there are no candidates, the arange
		remains empty [b, e) = [0, 0), as it should be.

		*/

		// initializes arange [b, e) to empty [0, 0)
		int b2 = 0, e2 = 0;
		List.forwardIterator iter = Rx.forwardIterator();
		for (int i = 0; i != Rx.size(); ++i)
		/*

		Computes the distance between the rightmost particle in the
		left partition and the ith particle on the right partition.
		If the distance of the pair (along the x-axis) is smaller
		than the (current) distance of the closest pair, the ith
		particle is included in the list of (potential) candidates
		by incrementing the end limit of the arange. We stop the
		traversal as soon as there is a pair farther than the
		closest pair, for it is obvious that the next one is going
		to be even farther away owing to the sorting.

		*/
		{
			int last = (Lx.size() - 1);
			int x = Lx.get(last).getX();
			double dx = (iter.get().getX() - x);
			if (dx < d_min)
				++e2;
			else
				break;

			iter.next();
		}


		/*

		Left Partition Traversal

		As above but the logic is reversed. Again, recall that the
		rightmost particle in the left partition is the closest to
		the right partition and so the end limit is fixed and the
		begin limit of the arange [b, e) gets decremented to add
		particles into the list of closest pair candidates.

		*/

		// initializes arange [b, e) to empty [size, size)
		int b1 = Lx.size(), e1 = Lx.size();
		for (int i = 0; i != Lx.size(); ++i)
		{
			int j = Lx.size() - (i + 1);
			int x = Rx.get(0).getX();
			double dx = (x - Lx.get(j).getX());
			if (dx < d_min)
				--b1;
			else
				break;
		}


		/*

		Applies the brute force algorithm to find the closest pair
		in the middle partition. The middle partition consists of
		the possible closest pairs formed by particles in the left
		and right partitions. The number of candidates in the left
		partition is given by (e1 - b1) and those in the right
		partition is given by (e2 - b2). If it turns out that there
		are no candidates the (current) distance of the closest
		is returned.

		*/


		int [][] arange = { {b1, e1}, {b2, e2} };
		return distance(Lx, Rx, closestPair, arange, d_min);
	}


	private double recurse (List Px, Coord [] closestPair)
	/*

	Synopsis:
	Applies the Divide and Conquer Algorithm to find the closest pair.

	Inputs:
	Px		x-y sorted coordinates of the particles
	closestPair	placeholder for storing the closest pair

	Outputs:
	closestPair	coordinates of the closest pair
	minDist		distance of the closest pair

	*/
	{
		if (Px.size() <= 3)
		{
			// uses brute force on the smaller partition
			return iBruteForce (Px, closestPair);
		}
		else
		{
			// creates alias
			Coord [] pair = closestPair;
			// allocates (temporary) placeholders
			Coord [] pairLeft  = new Coord[2];
			Coord [] pairRight = new Coord[2];
			// divides domain into left and right partitions
			List [] partitions = divide (Px);
			// gets left and right partitions
			List Lx = partitions[0], Rx = partitions[1];
			// finds the closest pair in the left partition
			double minDistLeft  = recurse(Lx, pairLeft);
			// finds the closest pair in the right partition
			double minDistRight = recurse(Rx, pairRight);
			// selects the closest from the two partitions
			double minDist = select(pair, pairLeft, pairRight,
						minDistLeft, minDistRight);

			/*

			combines the left and right partitions and looks
			for a new closest pair near the partition interface

			*/

			return combine(Lx, Rx, pair, minDist);
		}
	}


	private double recursiveClosestPair (List data)
	/*

	Synopsis:
	Applies the Divide and Conquer Algorithm to find the closest pair.
	This one is used to time the execution of the recursive algorithm.

	Input:
	data		coordinates of the particles

	Output:
	d_min		distance of the closest pair

	*/
	{
		Coord [] closestPair = new Coord[2];
		double tstart = System.nanoTime();
		// looks for the closest pair recursively
		double d_min = recurse (data, closestPair);
		double tend = System.nanoTime();

		// computes the elapsed time (nanoseconds)
		etime = (tend - tstart);

		/*

		Coord [] pair = closestPair;
		int x1 = pair[0].getX(), y1 = pair[0].getY();
		int x2 = pair[1].getX(), y2 = pair[1].getY();
		System.out.printf("\nDivide and Conquer Algorithm:\n");
		System.out.printf("d_min: %f\n", Math.sqrt(d_min));
		System.out.printf("(x1, y1): %2d, %2d\n", x1, y1);
		System.out.printf("(x2, y2): %2d, %2d\n", x2, y2);

		*/

		return (d_min);
	}


	/* Distance Computing and Brute Force Algorithm Implementations */


	private double distance (List part, int i, int j)
	/*

	Synopsis:
	Returns the squared distance of a pair of particles (i, j) located
	in the same partition.

	Inputs:
	part		partition (or whole dataset of coordinates)
	i		index of the ith particle in the left partition
	j		index of the jth particle in the right partition

	Output:
	d		squared distance of the ith and jth particles

	*/
	{
		// gets the coordinate objects of the particles P and Q
		Coord p = part.get(i), q = part.get(j);
		// delegates the computation of the squared distance
		return Coord.distance(p, q);
	}


	private double distance (List L, List R, int i, int j)
	/*

	Synopsis:
	Returns the squared distance of a pair of particles (i, j) located
	in different partitions.

	Inputs:
	L		left partition coordinates
	R		right partition coordinates
	i		index of the ith particle in the left partition
	j		index of the jth particle in the right partition

	Output:
	d		squared distance of the ith and jth particles

	*/
	{
		// gets the coordinate objects of the particles P and Q
		Coord p = L.get(i), q = R.get(j);
		// delegates the computation of the squared distance
		return Coord.distance(p, q);
	}


	private double distance (List part, Coord [] closestPair)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole dataset.

	Inputs:
	part		partition (or whole dataset of coordinates)
	closestPair	placeholder for storing the closest pair

	Outputs:
	closestPair	the coordinates of the closest pair in partition
	d_min		distance of the closest pair

	*/
	{
		int size = part.size();
		double d_min = Double.POSITIVE_INFINITY;
		for (int i = 0; i != (size - 1); ++i)
		{
			for (int j = (i + 1); j != size; ++j)
			{
				double d = distance (part, i, j);
				if (d < d_min)
				{
					// gets shallow copies of the data
					closestPair[0] = part.get(i);
					closestPair[1] = part.get(j);
					d_min = d;
				}
				++operations;
			}
		}

		return d_min;
	}


	private double distance (List L, List R, Coord [] pair,
				int [][] arange, double d_min)
	/*

	Synopsis:
	Applies Brute Force Algorithm on the middle partition M.

	Inputs:
	L		left partition
	R		right partition
	pair		current closest pair coordinates
	arange		asymmetric ranges of the left and right partitions
	d_min		current distance of the closest pair

	Outputs:
	pair		most recent coordinates of the closest pair
	d_min		distance of the newest closest pair

	 */
	{
		// gets the asymmetric range of the left partition
		int b1 = arange[0][0], e1 = arange[0][1];
		// gets the asymmetric range of the right partition
		int b2 = arange[1][0], e2 = arange[1][1];


		/*

		NOTE:
		The aranges define which particles in each partition
		could form a closest pair closer than the current one.
		Note that the pairs are formed by a particle from the left
		and another from the right partition, for we have already
		considered all the pairs within the same partition.

		*/


		List.forwardIterator iIter = L.forwardIterator(b1);
		for (int i = b1; i != e1; ++i)
		{
			Coord P = iIter.get();
			List.forwardIterator jIter = R.forwardIterator(b2);
			for (int j = b2; j != e2; ++j)
			{
				Coord Q = jIter.get();
				double d = Coord.distance(P, Q);
				if (d < d_min)
				{
					pair[0] = P;
					pair[1] = Q;
					d_min = d;
				}
				jIter.next();
				++operations;
			}
			iIter.next();
		}

		return d_min;
	}


	/* tests */


	private static void test ()
	/*

	Synopsis:
	Complains if the distance of the closest pair found by the Brute
	Force and Recursive Methods do not match. They may find another
	closest pair who has the same distance though and that is fine.

	*/
	{
		int runs = 8;
		int size = 16;
		int reps = 256;
		for (int i = 0; i != runs; ++i)
		{
			repeat(size, reps);
			size *= 2;
		}
	}


	private static void repeat (int size, int reps)
	// repeats the test `reps' times
	{
		for (int i = 0; i != reps; ++i)
		{
			Ensemble e = new Ensemble(size);
			// uses brute force to find the closest pair
			double minDistBruteForce = e.iBruteForceMethod();
			// finds the closest pair recursively
			double minDistRecursive = e.RecursiveMethod();
			// tests for inequality
			if (minDistBruteForce != minDistRecursive)
			/*

			NOTE:
			In general it is a bad idea to check if a pair of
			floating-point numbers are (not) equal because of
			possible rounding-off errors.

			However, we are actually comparing the squared
			distance which has an exact binary floating-point
			representation owing to the fact that we used
			integers to define the particle coordinates; thus,
			we can get away with the inequality test.

			*/
			{
				String errmsg = (
					"ClosestPair:ImplementationError"
				);
				throw new RuntimeException(errmsg);
			}
		}
	}
}


/*
 * COMMENTS:
 * The recursive algorithm could find another closest pair if it turns out
 * that there is more than one closest pair with equal distance. Note that
 * this is not unlikely because the limits imposed on the coordinates of
 * the particles along the y-axis is small. One could increase the limits
 * to try to avoid that but that will be inefficient in terms computations,
 * for there will be partitions where the particles are too far along the
 * y-axis for the partitioning to be meaningful. Note that the algorithm
 * that divides the system into quadrants is better suited for that case.
 *
 */
