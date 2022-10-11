/*
 * Algorithms and Complexity                               October 08, 2022
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

public class Ensemble	// Particle Ensemble Class
{
	public static long operations = 0;	// operations counter
	public static double etime = 0;		// elapsed time (nanosecs)

	public static void main (String [] args)
	{
		// defines the system size
		int size = (0x00000100);
		// creates the data set of coordinates without duplicates
		Vector data = genDataSet (size);


		// finds the closest pair with brute force
		double minDistBruteForce = bruteforce (data);


		// initializes operations counter
		operations = 0;
		// finds the closest pair via divide and conquer algorithm
		double minDistRecursive = recursiveClosestPair (data);


		/*

		Reports the test outcome on the console. If the algorithms
		find a closest pair whose distance differ the test fails.
		Note that it is not unlikely for the algorithms to find
		different closest pairs but of equal distance.

		*/


		System.out.printf("test: ");
		if (minDistRecursive > minDistBruteForce)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		// reports system and runtime info on the console
		System.out.printf("size:  %d\n", size);
		System.out.printf("opers: %d\n", operations);
		return;
	}


	public static Vector genDataSet (int size)
	/*

	Synopsis:
	Generates a distinct data set of coordinates by sampling values
	from a uniform pseudo-random number generator PRNG.

	Inputs:
	size		size of the data set (number of particles)

	Output:
	vector		a vector that stores the data set of coordinates

	*/
	{
		// creates a vector for storing the coordinates
		Vector vector = new Vector();
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
			while (vector.search(c) >= 0)
			// generates a new coordinate if already in vector
			{
				x = random.nextInt(size);
				y = random.nextInt(size);
				c = new Coord (x, y);
			}
			// pushes (distinct) coordinate unto back of vector
			vector.push_back(c);
			// sorts to use binary search on next pass
			vector.sort();
		}

		return vector;
	}


	private static double distance (Vector part, int i, int j)
	/*

	Synopsis:
	Returns the squared distance of a pair of particles (i, j) located
	in the same partition.

	Inputs:
	part		partition (or whole data set of coordinates)
	i		index of the ith particle in the left partition
	j		index of the jth particle in the right partition

	Output:
	d		squared distance of the ith and jth particles

	*/
	{
		// gets the coordinate objects of the particles P and Q
		Coord p = part.getData(i), q = part.getData(j);
		// delegates the computation of the squared distance
		return Coord.distance(p, q);
	}


	private static double distance (Vector L, Vector R, int i, int j)
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
		Coord p = L.getData(i), q = R.getData(j);
		// delegates the computation of the squared distance
		return Coord.distance(p, q);
	}


	private static double distance (Vector part, Coord [] closestPair)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole data set.

	Inputs:
	part		partition (or whole data set of coordinates)
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
					closestPair[0] = part.getData(i);
					closestPair[1] = part.getData(j);
					d_min = d;
				}
				++operations;
			}
		}

		return d_min;
	}


	private static double distance (Vector L, Vector R, Coord [] pair,
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
		Note that the pairs considered are formed by a particle
		from the left and another particle from right partition,
		for we have already considered pairs within the same
		partition.

		*/


		for (int i = b1; i != e1; ++i)
		{
			for (int j = b2; j != e2; ++j)
			{
				double d = distance(L, R, i, j);
				if (d < d_min)
				{
					// gets shallow copies of the data
					pair[0] = L.getData(i);
					pair[1] = R.getData(j);
					d_min = d;
				}
				++operations;
			}
		}

		return d_min;
	}


	private static double bruteforce (Vector data)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair.

	Input:
	data		coordinates of the particles

	Output:
	d_min		distance of the closest pair

	*/
	{
		// allocates placeholder for storing the closest pair
		Coord [] pair = new Coord[2];
		// applies brute force algorithm on the whole data set
		double d_min = distance (data, pair);

		/*
		Coord [] closestPair = pair;
		// unpacks the (x, y) coordinates of the closest pair
		int x1 = pair[0].getX(), y1 = pair[0].getY();
		int x2 = pair[1].getX(), y2 = pair[1].getY();

		// reports findings on the console
		System.out.printf("\nBrute Force Algorithm:\n");
		System.out.printf("d_min: %f\n", Math.sqrt(d_min));
		System.out.printf("(x1, y1): %2d, %2d\n", x1, y1);
		System.out.printf("(x2, y2): %2d, %2d\n", x2, y2);
		*/
		return Math.sqrt(d_min);
	}


	private static Vector [] divide (Vector Px)
	/*

	Synopsis:
	Divides data set into left and right partitions.

	Input:
	Px		original x-y sorted particle coordinates

	Outputs:
	Lx		left partition
	Rx		right partition

	*/
	{
			// initializes left partition via copy constructor
			Vector Lx = new Vector(Px);
			// creates left and right partitions
			Vector Rx = Lx.bisect();
			// creates placeholder for returning partitions
			Vector [] partitions = {Lx, Rx};
			return partitions;
	}


	private static double select (
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


	private static double combine (
		Vector Lx, Vector Rx, Coord [] closestPair, double d_min
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
			int x = Lx.getData(last).getX();
			double dx = (Rx.getData(i).getX() - x);
			if (dx < d_min)
				++e2;
			else
				break;
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
			int x = Rx.getData(0).getX();
			double dx = (x - Lx.getData(j).getX());
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


	private static double recurse (Vector Px, Coord [] closestPair)
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
			return distance (Px, closestPair);
		}
		else
		{
			// creates alias
			Coord [] pair = closestPair;
			// allocates (temporary) placeholders
			Coord [] pairLeft  = new Coord[2];
			Coord [] pairRight = new Coord[2];
			// divides domain into left and right partitions
			Vector [] partitions = divide (Px);
			// gets left and right partitions
			Vector Lx = partitions[0], Rx = partitions[1];
			// finds the closest pair in the left partition
			double minDistLeft  = recurse(Lx, pairLeft);
			// finds the closest pair in the right partition
			double minDistRight = recurse(Rx, pairRight);
			// selects the closest from the two partitions
			double minDist = select(pair, pairLeft, pairRight,
						minDistLeft, minDistRight);
			// combines left and right partitions
			return combine(Lx, Rx, pair, minDist);
		}
	}


	public static double recursiveClosestPair (Vector data)
	/*

	Synopsis:
	Applies the Divide and Conquer Algorithm to find the closest pair.

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
		return Math.sqrt(d_min);
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
 * that divides the system into quadrants is better suited in that case.
 *
 */
