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

import java.util.function.Function;

public class Ensemble	// Particle Ensemble Class
{

	interface Distance
	{
		// interface of distance computing methods
		public double getDistance(Point P, Point Q);
	}

	private final class Tuple
	/*

	Defines an auxiliary Tuple Class to encapsulate the closest pair
	and the number of operations (or equivalently, the number of distance
	computations) done to find the closest pair.

	*/
	{
		final private Pair closestPair;		// closest pair
		final private double numOperations;	// number of operations

		Tuple (Pair closestPair, double numOperations)
		{
			this.closestPair = closestPair;
			this.numOperations = numOperations;
		}

		/* getters */

		public Pair getClosestPair ()
		{
			return this.closestPair;
		}

		public double getNumOperations ()
		{
			return this.numOperations;
		}
	}


	/* components */


	private double elapsedTime;	// elapsed time (nanoseconds)
	private double numOperations;	// operations (computed distances) counter
	private int size;		// ensemble size (or number of points)


	/* constructors */


	Ensemble (int size)
	// creates an ensemble of distinct points of size `size'.
	{
		this.isInvalidEnsembleSize(size);	// complains if invalid
		this.elapsedTime = 0.0;			// zeroes the elapsed time
		this.numOperations = 0.0;		// zeroes the number of operations
		this.size = size;			// sets the ensemble size
	}


	/* getters */


	public double getOperations ()
	// gets the number of operations spent on finding the closest pair
	{
		return this.numOperations;
	}


	public double getElapsedTime ()
	// gets the elapsed-time invested on finding the closest pair
	{
		return this.elapsedTime;
	}


	public int size ()
	// gets the ensemble size (or equivalently, the number of points)
	{
		return this.size;
	}


	public Pair bruteForce () throws ImplementErrorException
	/*

	Synopsis:
	Applies the Brute Force Algorithm to obtain the closest pair.

	Inputs:
	None

	Output:
	closestPair	the closest pair

	*/
	{

		// creates a new dataset of distinct points
		List<Point> points = this.createDataset1D();
		// saves the closest pair found by the recursive algorithm
		Pair closestPairRecursive = this.recursive1DMethod(points);

		// uses the brute force algorithm to find the closest pair
		Pair closestPair = this.bruteForceMethod(points);

		if ( !closestPair.equalTo(closestPairRecursive) )
		// complains if the closest pairs are different
		{
			String errmsg = ("different closest pairs found");
			throw new ImplementErrorException(errmsg);
		}

		return closestPair;
	}


	public Pair recursive1D () throws ImplementErrorException
	/*

	Synopsis:
	Finds the closest pair via the 1D Divide and Conquer Algorithm.

	Inputs:
	None

	Output:
	closestPair	the closest pair

	*/
	{
		// creates a new dataset of distinct points
		List<Point> points = this.createDataset1D();
		// saves the closest pair found by the brute force method
		Pair closestPairBruteForce = this.bruteForceMethod(points);

		// finds the closest pair via divide and conquer algorithm
		Pair closestPair = this.recursive1DMethod(points);

		if ( !closestPair.equalTo(closestPairBruteForce) )
		// complains if the closest pairs are different
		{
			String errmsg = ("different closest pairs found");
			throw new ImplementErrorException(errmsg);
		}

		return closestPair;
	}


	public class Random
	// defines the Pseudo Random Number Generator PRNG Utility
	{
		/* components */

		private java.util.Random rand;	// PRNG

		/* constructor(s) */

		Random ()
		// constructs a PRNG
		{
			this.rand = new java.util.Random();
		}

		/* method(s) */

		double nextDouble (double minValue, double maxValue)
		// returns a double in the asymmetric range [min, max)
		{
			double min = Math.floor(minValue);
			double max = Math.floor(maxValue);
			double r = min + rand.nextDouble() * (max - min);
			return Math.floor(r);
		}
	}


	public static void main (String [] args) throws ImplementErrorException
	/*

	Synopsis:
	Executes the test codes to check for implementation errors. An
	implementation error happens if the Brute Force and Divide and
	Conquer Algorithms find a different closest pair. We are certain
	of that because we have made sure that the dataset has a unique
	closest pair; that is, there are no other pairs having the same
	separating distance.

	*/
	{
		// tests the 1D Divide and Conquer Algorithms
		test1D();
	}


	/* implementations */


	private Pair bruteForceMethod (List<Point> points)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to obtain the closest pair.
	Sets the elapsed-time (nanoseconds) invested in determining the
	closest pair. It also sets the number of operations (or the number
	of distance computations) executed by the Brute Force algorithm to
	find the closest pair.

	Inputs:
	points		dataset of distinct points

	Output:
	closestPair	the closest pair

	*/
	{
		// complains if invalid
		this.isInvalidData(points);

		double startTime = System.nanoTime();
		// times the Brute Force Algorithm
		Tuple data = this.distance(points);
		double endTime = System.nanoTime();

		// sets the elapsed time (nanoseconds)
		this.elapsedTime = (endTime - startTime);
		// sets the number of operations
		this.numOperations = data.getNumOperations();

		Pair closestPair = data.getClosestPair();
		return closestPair;
	}


	private Pair recursive1DMethod (List<Point> points)
	/*

	Synopsis:
	Applies the 1D Divide and Conquer Algorithm to find the closest pair.
	Sets the elapsed-time (nanoseconds) and the number of operations
	invested in finding the closest pair. It also sets the total number of
	operations (or equivalently, the total number of distance computations)
	executed by the Divide And Conquer algorithm to find the closest pair.

	Input:
	points		x-y sorted dataset of distinct points

	Output:
	closestPair	the closest pair

	*/
	{

		// complains if invalid
		this.isInvalidData(points);

		double startTime = System.nanoTime();
		// times the 1D Divide and Conquer Algorithm
		Tuple data = this.recurse(points);
		double endTime = System.nanoTime();

		// sets the elapsed time
		this.elapsedTime = (endTime - startTime);
		// sets the number of operations
		this.numOperations = data.getNumOperations();

		Pair closestPair = data.getClosestPair();
		return closestPair;
	}


	private Tuple recurse (List<Point> Px)
	/*

	Synopsis:
	Applies the 1D Divide and Conquer Algorithm to find the closest pair.
	If the partition P is small enough, the method uses Brute Force to find
	the closest pair. Otherwise, the method divides the partition P into
	left and right partitions to look for the closest pair in each. Note
	that the division step continues until the partitions are small enough
	to use Brute Force (or the direct method). Then, the method combines
	the solutions by selecting the smallest of the closest pair candidates
	and by looking for the closest pair between partitions.

	The method returns a tuple containing the closest pair and the
	number of operations (distance computations) invested to find the
	closest pair.

	Input:
	Px		x-y sorted coordinates of the particles

	Output:
	tuple		the closest pair and the number of operations

	*/
	{
		if (Px.size() <= 3)
		{
			// uses brute force on the smaller partition
			return this.distance(Px);
		}
		else
		{
			// divides dataset into left and right partitions
			List<Point> Lx = Px.sublist( 0, Px.size() / 2 );
			List<Point> Rx = Px.sublist( Px.size() / 2, Px.size() );

			// finds the closest pair in the left partition
			Tuple dataLeft = this.recurse(Lx);
			Pair closestPairLeft = dataLeft.getClosestPair();
			// finds the closest pair in the right partition
			Tuple dataRight = this.recurse(Rx);
			Pair closestPairRight = dataRight.getClosestPair();

			// selects the closest from the two partitions
			Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

			// combines the left and right partitions
			Tuple data = this.combine(Lx, Rx, closestPair);

			// updates the number of operations
			double numOperations = (dataLeft.getNumOperations() +
						dataRight.getNumOperations() +
						data.getNumOperations());

			closestPair = data.getClosestPair();
			return ( new Tuple(closestPair, numOperations) );
		}
	}


	private Tuple combine (List<Point> L, List<Point> R, Pair closestPair)
	/*

	Synopsis:
	Looks for the closest pair at the interface of the left and right
	partitions (dubbed as the middle partition M).

	Inputs:
	L		left partition
	R		right partition
	closestPair	current closest pair

	Output:
	tuple		the closest pair and the number of operations


	COMMENTS:
	Even though are refering in the comments of this method to the
	distance along the x axis, we are really computing the squared
	(x2 - x1)^2 because we are comparing that quantity against the
	squared distance of the current closest pair

			d^2 = (x2 - x1)^2 + (y2 - y1)^2

	to determine if the considered pair of points could be closer.

	*/
	{
		/*

		Right Partition Traversal

		*/

		double d_min = closestPair.getDistance();
		// initializes arange [b, e) to empty [0, 0)
		int b2 = 0, e2 = 0;
		for (Point Q : R)
		{
			// gets the index of the rigthmost point in the left partition
			int last = (L.size() - 1);
			Point P = L.get(last);

			// computes the x-axis distance of the closest pair candidate
			double x1 = P.getX(), x2 = Q.getX();
			double d = (x2 - x1) * (x2 - x1);

			if (d < d_min)
				++e2;
			else
				break;
		}


		/*

		Left Partition Traversal

		*/

		// initializes arange [b, e) to empty [size, size)
		int b1 = L.size(), e1 = L.size();
		for (int i = 0; i != L.size(); ++i)
		{
			// gets the index of the (current) rightmost point in the left
			int j = L.size() - (i + 1);

			Point P = L.get(j);
			Point Q = R.get(0);

			// computes the x-axis distance of the closest pair candidate
			double x1 = P.getX(), x2 = Q.getX();
			double d = (x2 - x1) * (x2 - x1);

			if (d < d_min)
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
		are no candidates the (current) closest pair is returned.

		*/

		// creates sublists containing the closest pair candidates
		List<Point> smallerL = L.sublist(b1, e1);
		List<Point> smallerR = R.sublist(b2, e2);
		return this.distance(smallerL, smallerR, closestPair);
	}


	private Tuple distance (Point[] part)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole dataset.

	Inputs:
	part		partition (or whole data set of points)

	Outputs:
	tuple		the closest pair and the number of operations

	COMMENTS:
	Brute Force method for the skeptical who believes that the values
	of the z coordinates are not taken into account by the 3D Divide
	And Conquer Algorithm.

	*/
	{
		// gets the partition size
		int sz = part.length;
		// initializes the closest pair
		Pair closestPair = new Pair();
		// considers all the distinct pairs to find the closest pair
		for (int i = 0; i != (sz - 1); ++i)
		{
			for (int j = (i + 1); j != sz; ++j)
			{
				Point p = part[i];
				Point q = part[j];
				// uses (x, y, z) coordinates to compute the distance
				double d = this.distance(p, q);
				Pair pair = new Pair(p, q, d);
				// updates the closest pair
				closestPair = Pair.min(pair, closestPair);
			}
		}

		double N = part.length;
		double numOperations = ( ( (N * (N - 1) ) / 2 ) );

		return ( new Tuple(closestPair, numOperations) );
	}


	private Tuple distance (List<Point> part)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole dataset.

	Inputs:
	part		partition (or whole data set of points)

	Outputs:
	tuple		the closest pair and the number of operations

	*/
	{
		// gets the partition size
		int sz = part.size();
		// initializes the closest pair
		Pair closestPair = new Pair();
		// considers all the distinct pairs to find the closest pair
		for (int i = 0; i != (sz - 1); ++i)
		{
			for (int j = (i + 1); j != sz; ++j)
			{
				Point p = part.get(i);
				Point q = part.get(j);
				double d = p.distance(q);
				Pair pair = new Pair(p, q, d);
				// updates the closest pair
				closestPair = Pair.min(pair, closestPair);
			}
		}

		double N = part.size();
		double numOperations = ( ( (N * (N - 1) ) / 2 ) );

		return ( new Tuple(closestPair, numOperations) );
	}


	private double distance (Point P, Point Q)
	/*

	Synopsis:
	Returns the squared distance of a pair of 3D Points.

	Inputs:

	Output:
	sqDist		squared distance between the pair of points

	*/
	{
		// complains of the points belong to the base Point class
		Point3D p = (Point3D) P, q = (Point3D) Q;

		double x1 = p.getX(), x2 = q.getX();
		double y1 = p.getY(), y2 = q.getY();
		double z1 = p.getZ(), z2 = q.getZ();

		double sqDist = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) +
				(z2 - z1) * (z2 - z1);

		return sqDist;
	}


	private double distance (List<Point> part, int i, int j)
	/*

	Synopsis:
	Returns the squared distance of a pair of particles (i, j) located
	in the same partition.

	Inputs:
	part		partition (or whole data set of points)
	i		index of the ith particle in the left partition
	j		index of the jth particle in the right partition

	Output:
	d		squared distance of the ith and jth particles

	*/
	{
		// gets the points P and Q
		Point p = part.get(i), q = part.get(j);
		// delegates the computation of the squared distance
		return p.distance(q);
	}


	private Tuple distance (List<Point> L, List<Point> R, Pair closestPair)
	/*

	Synopsis:
	Applies Brute Force Algorithm on the middle partition M. Note
	that the middle partition is comprised by closest pair candidates
	from the left and right partitions; it is not constructed
	explicitly.

	Inputs:
	L		closest pair candidates in left partition
	R		closest pair candidates right partition
	closestPair	current closest pair

	Outputs:
	tuple		the closest pair and the number of operations

	*/
	{
		for (Point p : L)
		{
			for (Point q : R)
			{
				double d = p.distance(q);
				Pair pair = new Pair(p, q, d);
				// updates the closest pair accordingly
				closestPair = Pair.min(pair, closestPair);
			}
		}

		// note: includes the distance computations in combine()
		double N1 = L.size(), N2 = R.size();
		double numOperations = ( (N1 * N2) + (N1 + 1) + (N2 + 1) );

		return ( new Tuple(closestPair, numOperations) );
	}


	private List<Point> createDataset1D ()
	/*

	Synopsis:
	Creates a dataset of points that has no duplicate closest pairs;
	that is, the second closest pair is farther away than the first
	closest pair. This version invokes the method that creates points
	uniformly distributed in a rectangular domain; that is, the
	range of possible y-axis coordinates is fixed.

	Input:
	None

	Output:
	dataset		dataset of distinct points with a unique closest pair

	*/
	{
		// creates a trial dataset of points
		List<Point> dataset = this.create();

		boolean hasDuplicateClosestPair = true;
		while (hasDuplicateClosestPair)
		// creates a new dataset until there are no duplicates
		{
			try
			{
				// checks for duplicated closest pairs
				this.hasDuplicateClosestPair(dataset);
				hasDuplicateClosestPair = false;
			}
			catch (DuplicatedClosestPairException e)
			{
				// creates a new dataset
				dataset = this.create();
			}
		}

		return dataset;
	}


	private List<Point> create ()
	/*

	Synopsis:
	Generates a distinct dataset of Cartesian points by sampling
	values from the uniform Pseudo-Random Number Generator PRNG
	utility.

	Inputs:
	None

	Output:
	points		a list that stores the dataset of points

	*/
	{

		// creates a new list for storing the points
		List<Point> points = new List<>();


		// creates a new Pseudo-Random Number Generator PRNG
		Ensemble.Random r = new Ensemble.Random();


		// defines limits for the point coordinates along the axes
		double size = this.size;
		double limit = (size * size);
		double x_min = -limit, x_max = limit;
		double y_min = -4, y_max = 4;


		for (int i = 0; i != this.size; ++i)
		// creates the set of distinct points
		{

			// creates a new point from random (x, y) coordinates
			double x = r.nextDouble(x_min, x_max);
			double y = r.nextDouble(y_min, y_max);
			Point p = new Point(x, y);

			while ( points.contains(p) )
			// generates a new point if it is already in the list (duplicate)
			{
				x = r.nextDouble(x_min, x_max);
				y = r.nextDouble(y_min, y_max);
				p = new Point(x, y);
			}

			// creates a copy of the distinct point for the compiler
			Point point = new Point(p);

			// inserts point at the correct location to preserve x-y sorting
			points.insort( () -> new Point(point) );
		}

		return points;
	}


	/* input validation */


	private void hasDuplicateClosestPair (List<Point> points)
	throws DuplicatedClosestPairException
	/*

	Synopsis:
	Uses the Brute Force Algorithm to find the first and the second
	closest pairs. Throws an exception if their distances are equal.

	Input:
	points		dataset of distinct points

	Output:
	None

	*/
	{
		// gets the total number of points
		int sz = points.size();
		// initializes the first closest pair
		Pair firstClosestPair = new Pair();
		// initializes the the second closest pair
		Pair secondClosestPair = new Pair();
		// uses Brute Force to find the first and second closest pairs
		for (int i = 0; i != (sz - 1); ++i)
		{
			for (int j = (i + 1); j != sz; ++j)
			{
				Point p = points.get(i);
				Point q = points.get(j);
				double d = p.distance(q);
				Pair pair = new Pair(p, q, d);

				if (pair.compareTo(firstClosestPair) <= 0)
				// updates the first and second closest pairs
				{
					secondClosestPair = firstClosestPair;
					firstClosestPair = pair;
				}
			}
		}

		double d_2nd = secondClosestPair.getDistance();
		double d_min = firstClosestPair.getDistance();

		if (d_2nd == d_min)
		// complains if the closest pairs have equal distances
		{
			throw new DuplicatedClosestPairException();
		}
	}


	private void isInvalidEnsembleSize (int size)
	/*

	Synopsis:
	Complains if the requested ensemble size is invalid.

	Input:
	size	requested ensemble size

	Output:
	None

	*/
	{
		String err = ("the ensemble size must be greater or equal to two");
		if (size < 2)
		{
			throw new IllegalArgumentException(err);
		}
	}


	private void isInvalidData (List<Point> data) throws IllegalArgumentException
	/*

	Synopsis:
	Complains if the dataset of points is not x-y sorted or if it has duplicates.

	Input:
	data		dataset of points

	Ouput:
	None

	*/
	{

		if ( !this.isSorted(data) )
		// complains if the points are not x-y sorted
		{
			String err = (
				"points must be sorted with respect to " +
				"the x-axis in ascending order"
			);

			throw new IllegalArgumentException(err);
		}


		if ( this.hasDuplicates(data) )
		// complains if there are duplicated points
		{
			String err = ("points must be distinct");
			throw new IllegalArgumentException(err);
		}
	}


	private boolean hasDuplicates (List<Point> points)
	/*

	Synopsis:
	Returns true if there are duplicated points, returns false otherwise.

	Input:
	points		dataset of points

	Output:
	hasDuplicates	true if there are duplicates, false otherwise

	*/
	{

		int duplicates = 0;
		// uses Brute Force to obtain the total number of duplicates
		for (Point P : points)
		{
			for (Point Q : points)
			{
				if (P.compareTo(Q) == 0)
					duplicates += 1;
				else
					duplicates += 0;
			}
		}


		/*

		Note:
		For a distinct dataset of points the variable `duplicates' will be equal
		to the list size because we are not omitting comparisons against the point
		itself (P == P) for performance reasons. We could but at the expense of
		using an algorithm that runs on cubic time.

		*/

		if (points.size() != duplicates)
			return true;
		else
			return false;
	}


	private boolean isSorted (List<Point> points)
	/*

	Synopsis:
	Returns true if the points are sorted in ascending order with
	respect to their x-axis coordinates, returns false otherwise.

	Input:
	points		dataset of points

	Output:
	isSorted	true if sorted, false otherwise

	*/
	{

		int misplacements = 0;
		for (int i = 0; i != (points.size() - 1); ++i)
		// gets the number of misplaced (or out-of-order) points
		{
			Point P = points.get(i);
			Point Q = points.get(i + 1);

			if (P.compareTo(Q) > 0)
				misplacements += 1;
			else
				misplacements += 0;
		}

		if (misplacements != 0)
			return false;
		else
			return true;
	}


	/* tests */


	private static void test () throws ImplementErrorException
	/*

	Synopsis:
	Checks for implementation errors. If the closest pair found by the
	Brute Force algorithm is not the same as that found by the Divide
	And Conquer algorithm an Implementation Error Exception is thrown.
	This version uses the 2D Divide And Conquer Algorithm that divides
	along the x and y axes, for the points span a squared domain in
	space.

	The ensemble has a predetermined size.

	Inputs:
	None

	Outputs:
	None

	*/
	{
		int size = 2;
		Ensemble ens = new Ensemble(size);
		Pair closestPair = ens.recursive1D();
	}


	private static void test1D () throws ImplementErrorException
	/*

	Synopsis:
	Checks for implementation errors. If the closest pair found by the
	Brute Force algorithm is not the same as that found by the Divide
	And Conquer algorithm an Implementation Error Exception is thrown.
	This version uses the 1D Divide And Conquer Algorithm that divides
	along the largest dimension (the x-axis by design).

	The test is performed for ensembles having sizes in the asymmetric
	range [2, 4096). A new ensemble is created each time to change the
	location of the closest pair. Note that the closest pair may be
	present in or between (the smaller) partitions (or subdomains).

	Inputs:
	None

	Outputs:
	None

	*/
	{
		for (int size = 2; size != (0x00001000); size *= 2)
		{
			for (int i = 0; i != 256; ++i)
			{
				Ensemble ens = new Ensemble(size);
				Pair closestPair = ens.recursive1D();
			}
		}
	}
}
