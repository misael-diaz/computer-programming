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


	private double etime;		// elapsed time (nanoseconds)
	private double operations;	// operations (computed distances) counter
	private int size;		// ensemble size (or number of points)


	/* constructors */


	Ensemble (int size)
	// creates an ensemble of distinct points of size `size'.
	{
		this.isInvalidEnsembleSize(size);	// complains if invalid
		this.etime = 0.0;			// zeroes the elapsed time
		this.operations = 0.0;			// zeroes the #operations
		this.size = size;			// sets the ensemble size
	}


	/* getters */


	public double getOperations ()
	// gets the number of operations spent on finding the closest pair
	{
		return this.operations;
	}


	public double getElapsedTime ()
	// gets the elapsed-time invested on finding the closest pair
	{
		return this.etime;
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
		Vector<Point> points = this.createDataset1D();
		// saves the closest pair found by the recursive algorithm
		Pair closestPairRecursive = this.recursive1DMethod(points);

		// uses the brute force algorithm to find the closest pair
		Pair closestPair = this.bruteForceMethod(points);

		if ( !closestPair.equalTo(closestPairRecursive) )
		// complains if the closest pairs are different
		{
			String errmsg = "different closest pairs found";
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
		Vector<Point> points = this.createDataset1D();
		// saves the closest pair found by the brute force method
		Pair closestPairBruteForce = this.bruteForceMethod(points);

		// finds the closest pair via divide and conquer algorithm
		Pair closestPair = this.recursive1DMethod(points);

		if ( !closestPair.equalTo(closestPairBruteForce) )
		// complains if the closest pairs are different
		{
			String errmsg = "different closest pairs found";
			throw new ImplementErrorException(errmsg);
		}

		return closestPair;
	}


	public Pair recursive2D () throws ImplementErrorException
	/*

	Synopsis:
	Finds the closest pair via the 2D Divide and Conquer Algorithm.

	Inputs:
	None

	Output:
	closestPair	the closest pair

	*/
	{
		// creates a new x-y sorted dataset of distinct points
		Vector<Point> Px = this.createDataset2D();
		// creates another vector with the y-x sorted points
		Vector<Point> Py = new Vector<>(Px);
		Py.sort( new Point.Comparator() );

		// saves the closest pair found by the brute force method
		Pair closestPairBruteForce = this.bruteForceMethod(Px);

		// finds the closest pair via divide and conquer algorithm
		Pair closestPair = this.recursive2DMethod(Px, Py);

		if ( !closestPair.equalTo(closestPairBruteForce) )
		// complains if the closest pairs are different
		{
			String errmsg = "different closest pairs found";
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
		// tests the 1D and 2D Divide and Conquer Algorithms
		test1D();
		test2D();

		//test();
	}


	/* implementations */


	private Pair bruteForceMethod (Vector<Point> points)
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

		double tstart = System.nanoTime();
		// times the Brute Force Algorithm
		Tuple data = this.distance(points);
		double tend = System.nanoTime();

		// sets the elapsed time (nanoseconds)
		this.etime = (tend - tstart);
		// sets the number of operations
		this.operations = data.getNumOperations();

		Pair closestPair = data.getClosestPair();
		return closestPair;
	}


	private Pair recursive1DMethod (Vector<Point> points)
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

		double tstart = System.nanoTime();
		// times the 1D Divide and Conquer Algorithm
		Tuple data = this.recurse(points);
		double tend = System.nanoTime();

		// sets the elapsed time
		this.etime = (tend - tstart);
		// sets the number of operations
		this.operations = data.getNumOperations();

		Pair closestPair = data.getClosestPair();
		return closestPair;
	}


	private Pair recursive2DMethod (Vector<Point> Px, Vector<Point> Py)
	/*

	Synopsis:
	Applies the 2D Divide and Conquer Algorithm to find the closest pair.
	Sets the elapsed-time (nanoseconds) and the number of operations
	invested in finding the closest pair. It also sets the total number of
	operations (or equivalently, the total number of distance computations)
	executed by the Divide And Conquer algorithm to find the closest pair.

	Input:
	Px		x-y sorted dataset of distinct points
	Py		same dataset of points but y-x sorted

	Output:
	closestPair	the closest pair

	*/
	{

		// complains if invalid
		this.isInvalidData(Px);

		double tstart = System.nanoTime();
		// times the 2D Divide and Conquer Algorithm
		Tuple data = this.recurse(Px, Py);
		double tend = System.nanoTime();

		// sets the elapsed time
		this.etime = (tend - tstart);
		// sets the number of operations
		this.operations = data.getNumOperations();

		Pair closestPair = data.getClosestPair();
		return closestPair;
	}


	private Tuple recurse (Vector<Point> Px)
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
			// allocates the left and right partitions
			Vector<Point> Lx = new Vector<>();
			Vector<Point> Rx = new Vector<>();

			// divides dataset into left and right partitions
			this.divide(Px, Lx, Rx);

			// finds the closest pair in the left partition
			Tuple dataLeft = this.recurse(Lx);
			Pair closestPairLeft = dataLeft.getClosestPair();
			// finds the closest pair in the right partition
			Tuple dataRight = this.recurse(Rx);
			Pair closestPairRight = dataRight.getClosestPair();

			// selects the closest from the two partitions
			Pair closestPair = Pair.min(
				closestPairLeft, closestPairRight
			);

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


	private Tuple distance (Vector<Point> part)
	/*

	Synopsis:
	Applies the Brute Force Algorithm to find the closest pair in a
	partition. Note that the partition could be the whole dataset.
	Increments the operations counter.

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
				double d = Point.distance(p, q);
				Pair pair = new Pair(p, q, d);
				// updates the closest pair
				closestPair = Pair.min(pair, closestPair);
			}
		}

		double N = part.size();
		double numOperations = ( ( (N * (N - 1) ) / 2 ) );

		return ( new Tuple(closestPair, numOperations) );
	}


	private double distance (Vector<Point> part, int i, int j)
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
		// gets the coordinate objects of the particles P and Q
		Point p = part.get(i), q = part.get(j);
		// delegates the computation of the squared distance
		return Point.distance(p, q);
	}


	private Tuple distance (Vector<Point> L, Vector<Point> R,
				Pair closestPair)
	/*

	Synopsis:
	Applies Brute Force Algorithm on the middle partition M. Note
	that the middle partition is comprised by closest pair candidates
	from the left and right partitions; it is not constructed
	explicitly. Increments the operations counter.

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
				double d = Point.distance(p, q);
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


	private void divide (
		Vector<Point> Px, Vector<Point> Lx, Vector<Point> Rx
	)
	/*

	Synopsis:
	Divides dataset of points into left and right partitions.

	Inputs:
	Px		original x-y sorted particle coordinates
	Lx		an unset view
	Rx		an unset view

	Outputs:
	Lx		view of the left partition
	Rx		view of the right partition

	*/
	{
			// gets the size of the given dataset
			int size = Px.size();
			// gets the half of the dataset size
			int half = (Px.size() / 2);

			// creates views of the left and right parititions

			Lx.view(Px, 0, half);		// left partition
			Rx.view(Px, half, size);	// right partition
	}


	private Tuple combine (
		Vector<Point> L, Vector<Point> R, Pair closestPair
	)
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

		Assumes that there are no particles on the right partition
		that could form a pair with any of the particles on the
		left partition which could be closer than the (current)
		closest pair. (Recall that at this point we have already
		considered all possible pairs within the partitions, thus
		we need not to do that again.)

		To keep track of the number of potential candidates we use
		the asymmetric range (arange for short) [b, e), where `b'
		is the beginning limit and `e' is the end limit. The number
		of candidates in a particular partition is computed via the
		difference (e - b).

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

		double d_min = closestPair.getDistance();
		// initializes arange [b, e) to empty [0, 0)
		int b2 = 0, e2 = 0;
		for (Point Q : R)
		/*

		Computes the distance between the rightmost particle P in
		the left partition and the particle Q on the right
		partition.

		If the distance of the pair PQ (along the x-axis) is
		smaller than the (current) distance of the closest pair,
		the particle Q is included in the list of (potential)
		candidates by incrementing the end limit of the asymmetric
		range.

		We stop the traversal as soon as there is a pair farther
		than the closest pair, for it is obvious that the next one
		is going to be even farther away owing to the sorting.

		*/
		{
			// gets the x-axis position of the rigthmost point
			int last = (L.size() - 1);
			Point P = L.get(last);

			// computes the x-axis distance of the candidate
			double x1 = P.getX(), x2 = Q.getX();
			double d = (x2 - x1) * (x2 - x1);

			if (d < d_min)
				++e2;
			else
				break;
		}


		/*

		Left Partition Traversal

		As above but the traversal is reversed. Again, recall that
		the rightmost particle in the left partition is the closest
		to the right partition and so the end limit is fixed and
		the begin limit of the arange [b, e) gets decremented to
		add particles into the list of closest pair candidates.

		*/

		// initializes arange [b, e) to empty [size, size)
		int b1 = L.size(), e1 = L.size();
		for (int i = 0; i != L.size(); ++i)
		{
			// gets the x-axis position of the leftmost point
			int j = L.size() - (i + 1);

			Point P = L.get(j);
			Point Q = R.get(0);

			// computes the x-axis distance of the candidate
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

		// creates views containing the closest pair candidates
		Vector<Point> smallerL = new Vector<>(L, b1, e1);
		Vector<Point> smallerR = new Vector<>(R, b2, e2);
		return this.distance(smallerL, smallerR, closestPair);
	}


	private Tuple combine (	Vector<Point> L, Vector<Point> R,
				Pair closestPair, Distance quickDistance )
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

	*/
	{
		double d_min = closestPair.getDistance();
		// initializes arange [b, e) to empty [0, 0)
		int b2 = 0, e2 = 0;
		for (Point Q : R)
		{
			// gets the x-axis position of the rigthmost point
			int last = (L.size() - 1);
			Point P = L.get(last);

			// computes the distance (along the axis)
			double d = quickDistance.getDistance(P, Q);

			if (d < d_min)
				++e2;
			else
				break;
		}


		// initializes arange [b, e) to empty [size, size)
		int b1 = L.size(), e1 = L.size();
		for (int i = 0; i != L.size(); ++i)
		{
			// gets the x-axis position of the leftmost point
			int j = L.size() - (i + 1);

			Point P = L.get(j);
			Point Q = R.get(0);

			// computes the distance (along the axis)
			double d = quickDistance.getDistance(P, Q);

			if (d < d_min)
				--b1;
			else
				break;
		}


		// creates views containing the closest pair candidates
		Vector<Point> smallerL = new Vector<>(L, b1, e1);
		Vector<Point> smallerR = new Vector<>(R, b2, e2);
		return this.distance(smallerL, smallerR, closestPair);
	}


	private Tuple recurse (Vector<Point> Px, Vector<Point> Py)
	/*

	Synopsis:
	Implements the 2D Divide and Conquer Algorithm. If the partition
	P is small enough, the method uses Brute Force to find the closest
	pair. Otherwise, the method divides the partition P into left and
	right partitions to look for the closest pair in each. Note that
	the division step continues until the partitions are small enough
	to use Brute Force. Then, the method combines the solutions by
	selecting the smallest of the closest pair candidates and by
	looking for the closest pair between partitions.

	The method returns a tuple containing the closest pair and the
	number of operations (distance computations) invested to find the
	closest pair.

	Inputs:
	Px		the x-y sorted partition
	Py		the y-x sorted partition

	Output:
	tuple		the closest pair and the number of operations


	COMMENTS:
	This 2D version of the Divide and Conquer Algorithm divides along
	the y dimension as well whenever it makes sense to do so. The task
	of dividing along the y dimension is carried out by the overloaded
	divide() method.

	*/
	{
		if (Px.size() <= 3)
		{
			// uses brute force on the smaller partition
			return this.distance(Px);
		}
		else
		{
			// allocs the x-y sorted left and right partitions
			Vector<Point> Lx = new Vector<>();
			Vector<Point> Rx = new Vector<>();

			// divides dataset into left and right partitions
			this.divide(Px, Lx, Rx);

			// allocs the y-x sorted left and right partitions
			Vector<Point> Ly = new Vector<>(
				Lx.size(), (Integer sz) -> new Point[sz]
			);

			Vector<Point> Ry = new Vector<>(
				Rx.size(), (Integer sz) -> new Point[sz]
			);

			// builds the y-x sorted left and right partitions
			this.sort(Py, Lx, Ly, Ry);

			// finds the closest pair in the left partition
			Tuple dataLeft = this.divide(Lx, Ly);
			Pair closestPairLeft = dataLeft.getClosestPair();

			// finds the closest pair in the right partition
			Tuple dataRight = this.divide(Rx, Ry);
			Pair closestPairRight = dataRight.getClosestPair();

			// selects the closest from the two partitions
			Pair closestPair = Pair.min(
				closestPairLeft, closestPairRight
			);

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


	private Tuple divide (Vector<Point> Px, Vector<Point> Py)
	/*

	Synopsis:
	If the partition is small enough, the method applies the Brute
	Force Method to find the closest pair; otherwise it divides the
	partition into two of four possible quadrants to find the closest pair.
	If the partition P corresponds to the left partition, the method
	divides it into the second and third quadrants; otherwise, the
	method divides it into the first and fourth quadrants.

	Inputs:
	Px		the x-y sorted left (right) partition
	Py		the y-x sorted left (right) partition

	Output:
	tuple		the closest pair and the number of operations

	*/
	{
		if (Px.size() <= 3)
		{
			return this.distance(Px);
		}
		else
		{
			// allocs the y-x sorted left and right partitions
			Vector<Point> Ly = new Vector<>();
			Vector<Point> Ry = new Vector<>();

			// divides into left and right partitions
			this.divide(Py, Ly, Ry);

			// constructs x-y sorted left and right partitions
			Vector<Point> Lx = new Vector<>(Ly);
			Vector<Point> Rx = new Vector<>(Ry);
			Lx.sort();
			Rx.sort();

			// finds the closest pair in the left partition
			Tuple dataLeft = this.recurse(Lx, Ly);
			Pair closestPairLeft = dataLeft.getClosestPair();

			// finds the closest pair in the right partition
			Tuple dataRight = this.recurse(Rx, Ry);
			Pair closestPairRight = dataRight.getClosestPair();

			// selects the closest from the two partitions
			Pair closestPair = Pair.min(
				closestPairLeft, closestPairRight
			);

			Distance yAxisDist = (Point P, Point Q) ->
			{
				double y1 = P.getY(), y2 = Q.getY();
				double d = (y2 - y1) * (y2 - y1);
				return d;
			};

			// combines the left and right partitions
			Tuple data = this.combine(
				Ly, Ry, closestPair, yAxisDist
			);

			// updates the number of operations
			double numOperations = (dataLeft.getNumOperations() +
						dataRight.getNumOperations() +
						data.getNumOperations());

			closestPair = data.getClosestPair();
			return ( new Tuple(closestPair, numOperations) );
		}
	}


	private void sort (	Vector<Point> Py, Vector<Point> Lx,
				Vector<Point> Ly, Vector<Point> Ry     )
	/*

	Synopsis:
	Sorts Py into left Ly and right Ry partitions. It does it work by
	searching in Py for elements in the left partition Lx. Elements
	found to be contained in the left partition Lx are copied into Ly.
	And those that are not contained in the left must be in the right
	partition; thus, the method copies them into Ry.

	Inputs:
	Py			y-x sorted vector of points
	Lx			x-y sorted left partition
	Ly			preallocated vector of size Lx.size()
	Ry			preallocated vector of size Rx.size()

	Outputs:
	Ly			y-x sorted left partition
	Ry			y-x sorted right partition

	*/
	{
		for (Point p : Py)
		{
			if ( Lx.contains(p) )
				Ly.push_back( () -> new Point(p) );
			else
				Ry.push_back( () -> new Point(p) );
		}
	}


	private Vector<Point> createDataset1D ()
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
	dataset		dataset of points with a unique closest pair

	*/
	{
		// creates a trial dataset of points
		Vector<Point> dataset = this.create();

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


	private Vector<Point> createDataset2D ()
	/*

	Synopsis:
	Creates a dataset of points that has no duplicate closest pairs;
	that is, the second closest pair is farther away than the first
	closest pair. This version invokes the method that creates points
	uniformly distributed in a squared domain. The possible range of
	x, y coordinates grow with the ensemble size to keep the point
	density constant regardless of the ensemble size.

	Input:
	None

	Output:
	dataset		dataset of points with a unique closest pair

	*/
	{
		// allocates memory for the dataset of points
		Vector<Point> dataset = new Vector<>(
			this.size, (Integer sz) -> new Point[sz]
		);

		// creates a trial dataset of points
		this.create(dataset);

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
				this.create(dataset);
			}
		}

		return dataset;
	}


	private Vector<Point> create ()
	/*

	Synopsis:
	Generates a distinct dataset of Cartesian points by sampling
	values from the uniform Pseudo-Random Number Generator PRNG
	utility.

	Inputs:
	None		size of the dataset (number of particles)

	Output:
	points		a vector that stores the dataset of points

	*/
	{

		// creates a vector for storing the points
		Vector<Point> points = new Vector<>(
			(Integer sz) -> new Point[sz]
		);


		// creates a new Pseudo-Random Number Generator PRNG
		Ensemble.Random r = new Ensemble.Random();


		// defines limits for the point coordinates along the axes
		double limit = (this.size * this.size);
		double x_min = -limit, x_max = limit;
		double y_min = -4, y_max = 4;


		for (int i = 0; i != this.size; ++i)
		// creates the set of distinct points
		{

			double x = r.nextDouble(x_min, x_max);
			double y = r.nextDouble(y_min, y_max);

			Point p = new Point(x, y);
			while ( points.contains(p) )
			// creates a new point if already present in vector
			{
				x = r.nextDouble(x_min, x_max);
				y = r.nextDouble(y_min, y_max);
				p = new Point(x, y);
			}

			// pushes (new) point unto the back of the vector
			Point point = new Point(p);
			points.push_back( () -> new Point(point) );
		}

		// sorts to support the divide and conquer algorithm
		points.sort();

		return points;
	}


	private Vector<Point> create (Vector<Point> Px)
	/*

	Synopsis:
	Generates a distinct dataset of Cartesian points by sampling
	values from the uniform Pseudo-Random Number Generator PRNG
	utility.

	Inputs:
	Px		an allocated (but empty) vector of points

	Output:
	Px		the vector contains the x-y sorted points
	Py		the vector stores the same points but y-x sorted

	*/
	{

		// creates a new Pseudo-Random Number Generator PRNG
		Ensemble.Random r = new Ensemble.Random();


		// defines limits for the point coordinates along the axes
		double limit = (this.size * this.size);
		double x_min = -limit, x_max = limit;
		double y_min = -limit, y_max = limit;


		for (int i = 0; i != this.size; ++i)
		// creates the set of distinct points
		{

			double x = r.nextDouble(x_min, x_max);
			double y = r.nextDouble(y_min, y_max);

			Point p = new Point(x, y);
			while ( Px.contains(p) )
			// creates a new point if already present in vector
			{
				x = r.nextDouble(x_min, x_max);
				y = r.nextDouble(y_min, y_max);
				p = new Point(x, y);
			}

			// pushes (new) point unto the back of the vector
			Point point = new Point(p);
			Px.push_back( () -> new Point(point) );
		}

		// sorts to support the divide and conquer algorithm
		Px.sort();

		Vector<Point> Py = new Vector<>(Px);
		Py.sort( new Point.Comparator() );

		return Py;
	}


	/* input validation */


	private void hasDuplicateClosestPair (Vector<Point> points)
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
		// initializes the distance of the first closest pair
		double d_min = Double.POSITIVE_INFINITY;
		// initializes the distance of the second closest pair
		double d_2nd = Double.POSITIVE_INFINITY;
		for (int i = 0; i != (sz - 1); ++i)
		{
			for (int j = (i + 1); j != sz; ++j)
			{

				double d = this.distance(points, i, j);

				if (d <= d_min)
				// updates distances of the closest pairs
				{
					d_2nd = d_min;
					d_min = d;
				}
			}
		}

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
		if (size < 2)
		{
			String err = (	"ensemble size must be greater " +
					"or equal to two"  );
			throw new IllegalArgumentException(err);
		}
	}


	private void isInvalidData (Vector<Point> data) throws IllegalArgumentException
	/*

	Synopsis:
	Complains if the dataset of points is not x-y sorted or if it has
	duplicates.

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
			String err = (
				"points must be distinct"
			);

			throw new IllegalArgumentException(err);
		}
	}


	private boolean hasDuplicates (Vector<Point> points)
	/*

	Synopsis:
	Returns true if there are duplicated points, returns false
	otherwise.

	Input:
	points		dataset of points

	Output:
	hasDuplicates	true if there are duplicates, false otherwise

	*/
	{
		// sorts to check for duplicates in pairs
		points.sort();

		int duplicates = 0;
		for (int i = 0; i != (points.size() - 1); ++i)
		// obtains the total number of duplicates
		{
			Point P = points.get(i);
			Point Q = points.get(i + 1);

			if (P.compareTo(Q) == 0)
				duplicates += 1;
			else
				duplicates += 0;
		}

		if (duplicates != 0)
			return true;
		else
			return false;
	}


	private boolean isSorted (Vector<Point> points)
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
		Pair closestPair = ens.recursive2D();
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


	private static void test2D () throws ImplementErrorException
	/*

	Synopsis:
	Checks for implementation errors. If the closest pair found by the
	Brute Force algorithm is not the same as that found by the Divide
	And Conquer algorithm an Implementation Error Exception is thrown.
	This version uses the 2D Divide And Conquer Algorithm that divides
	along the x and y axes, for the points span a squared domain in
	space.

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
				Pair closestPair = ens.recursive2D();
			}
		}
	}
}
