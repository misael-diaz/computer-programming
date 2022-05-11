/*
 * Algorithms and Programming II                               May 08, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Solves the Closest Pair problem with the Divide and Conquer Algorithm.
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
 *
 */

import static java.lang.Math.sqrt;
import static java.lang.Math.ceil;

class recursiveClosestPair
{

	public static void main (String[] args)
	{


		/* Divide and Conquer Algorithm */


		Vector P = Dataset();
		int [][] closestPair = new int [2][2];
		double d_min = ClosestPair (P, closestPair);

		// prints distance and coordinates of closest pair
		System.out.printf("\nDivide and Conquer Algorithm:\n");
		System.out.printf("distance: %.14f\n", d_min);
		System.out.printf("coordinates:\n");

		Vector.Coord first  = new Vector.Coord( closestPair[0] );
		Vector.Coord second = new Vector.Coord( closestPair[1] );
		first.print(first);
		second.print(second);


		/* Brute Force Algorithm */


		d_min = distance (P, closestPair);

		// prints distance and coordinates of closest pair
		System.out.printf("\nBrute Force Algorithm:\n");
		System.out.printf("distance: %.14f\n", d_min);
		System.out.printf("coordinates:\n");

		first  = new Vector.Coord( closestPair[0] );
		second = new Vector.Coord( closestPair[1] );
		first.print(first);
		second.print(second);

		return;
	}


	private static double ClosestPair (Vector P, int [][] closestPair)
	/*
	 * Synopsis:
	 * Closest Pair Method.
	 * Applies the direct solution (or Brute Force Method) if available,
	 * otherwise delegates the task to the method that implements the
	 * Divide and Conquer Algorithm.
	 *
	 * Inputs:
	 * P		vector, a container of the dataset of coordinates
	 * closestPair	uninitialized 2nd-rank array
	 *
	 * Outputs:
	 * d_min	distance of the closest pair
	 * closestPair	2nd-rank array holding the coordinates of the pair
	 *
	 */
	{
		String errmsg = ("ClosestPair(): single particle system");
		if (P.size (P) <= 1)
			throw new RuntimeException (errmsg);


		if (P.size (P) <= 3)

			return distance (P, closestPair);

		else
		{
			Vector Px = P.copy (P);		// x-y sorted data
			Vector Py = P.copy (P);		// y-x sorted data

			Px.sort (Px);			// x-y sorts
			Py.sort (Py, Py.comparator);	// y-x sorts

			// invokes recursive Divide and Conquer method
			return recurse (Px, Py, closestPair);
		}
	}


	private static double recurse (Vector Px, Vector Py, int [][] Pair)
	// implements Divide and Conquer Algorithm
	{

		int [][] closestPair = Pair;

		if ( Px.size(Px) <= 3 )

			return distance (Px, closestPair);

		else
		{
			// creates shallow copies to work on the boundaries
			Vector Mx = Px.copy (Px);
			Vector My = Py.copy (Py);

			// builds x-y sorted Left and Right subsets
			Vector Lx = Px.copy (Px);
			Vector Rx = build_xySortedHalves (Lx);

			// builds y-x sorted Left and Right subsets
			Vector Ly = Py.copy (Py) ;
			Vector Ry = build_yxSortedHalves (Ly, Rx);

			// finds closest pair in the Left and Right subsets
			double minDist;
			minDist = divide (Lx, Rx, Ly, Ry, Pair);

			// looks for the closest pair near the boundaries
			double d_min;
			d_min = combine (Mx, My, minDist, Pair);
			return d_min;
		}
	}


	private static Vector halves (Vector Px)
	// divides dataset into left (lower) and right (upper) subsets
	{
		return build_xySortedHalves (Px);
	}


	private static Vector build_xySortedHalves (Vector Px)
	// divides dataset and returns ``Left'' and ``Right'' subsets
	{
		// creates reference to original dataset (or alias)
		Vector Lx = Px;
		Vector Rx = new Vector();

		// pops elements of Px in the range [m, e) to obtain Rx:
		int size = Px.size (Px);		// size
		int b = (size / 2), e = size;		// middle and end
		Rx.push_back (Rx, Px.pop(Px, b, e) );	// pops by range

		// fits vectors to size to reduce memory usage
		Lx.fit (Lx);
		Rx.fit (Rx);

		return Rx;
	}


	private static Vector quads (Vector P, Vector R)
	{
		return build_yxSortedHalves (P, R);
	}


	private static Vector build_yxSortedHalves (Vector Py, Vector Rx)
	/*
	 * Synopsis:
	 * Constructs y-x sorted, Left (Ly) and Right (Ry) subsets by
	 * popping elements in Py (y-x sorted, original dataset)
	 * found in Rx (x-y sorted Right subset).
	 *
	 *
	 * Inputs:
	 * Py		y-x sorted dataset
	 * Rx		x-y sorted, right, subset
	 *
	 * Outputs:
	 * Ly		y-x sorted, left, subset
	 * Ry		y-x sorted, right, subset
	 *
	 *
	 * Note that Ly is just a reference to Py so that the elements
	 * that remain in Py after the popping are the elements in Ly.
	 * (Thus it is not necessary to create another copy just a
	 * reference as is done here to save space.)
	 *
	 */
	{
		Vector Ly = Py;			// creates alias for Py
		Vector Ry = new Vector();	// placeholder for Ry

		int pos;			// positional index
		int idx = 0;			// element index
		int size = Ly.size (Ly);	// number of elements
		int [] coord = new int [2];	// coordinate placeholder
		// pops elements in Py matching Rx while inserting into Ry
		// to obtain both Ly and Ry subsets
		for (int n = 0; n != size; ++n)
		// NOTE: pops elements in Py in order (beginning to end)
		//       so that Ry ends up with the same ordering of Py
		{
			coord = Ly.index(Ly, idx);
			// Note that when popping elements from beginning
			// to end it is not necessary to increment the
			// index when an element is popped because the next
			// element in the vector ends up in the popped
			// location. On the other hand, if the current
			// element is not popped, the index needs to be
			// incremented to consider the next element on the
			// next iteration.
			pos = Rx.search(Rx, coord);
			if (pos != 0)
				Ry.push_back( Ry, Ly.pop(Ly, idx) );
			else
				++idx;
		}

		// fits vectors to size to reduce memory usage
		Ly.fit(Ly);
		Ry.fit(Ry);

		return Ry;
	}


	private static
	double divide (Vector Lx, Vector Rx, Vector Ly, Vector Ry,
		       int [][] closestPair)
	// divides halves into quadrants and returns closest pair
	{
		int [][] closestPairLeft  = new int [2][2];
		int [][] closestPairRight  = new int [2][2];
		double minDistLeft  = divide (Lx, Ly, closestPairLeft);
		double minDistRight = divide (Rx, Ry, closestPairRight);


		double minDist;
		// selects the closest pair from the halves
		if (minDistLeft < minDistRight)
		{
			minDist = minDistLeft;

			closestPair[0][0] = closestPairLeft[0][0];
			closestPair[0][1] = closestPairLeft[0][1];

			closestPair[1][0] = closestPairLeft[1][0];
			closestPair[1][1] = closestPairLeft[1][1];
		}
		else
		{
			minDist = minDistRight;

			closestPair[0][0] = closestPairRight[0][0];
			closestPair[0][1] = closestPairRight[0][1];

			closestPair[1][0] = closestPairRight[1][0];
			closestPair[1][1] = closestPairRight[1][1];
		}

		return minDist;
	}


	private static double divide (Vector Px, Vector Py, int [][] Pair)
	// divides into lower and upper subsets (quadrants)
	{
		int [][] closestPair = Pair;

		if ( Px.size(Px) <= 3 )

			return distance (Px, closestPair);

		else
		{
			// divides into Lower and Upper subsets (quadrants)
			Vector Ly = Py.copy (Py);	// y-x sorted lower
			Vector Ry = halves  (Ly);	// y-x sorted upper

			Vector Lx = Px.copy (Px);	// x-y sorted lower
			Vector Rx = quads (Lx, Ry);	// x-y sorted upper


			// finds the closest pair in the quadrants
			int [][] closestPairLeft  = new int [2][2];
			int [][] closestPairRight = new int [2][2];
			double minDistLeft, minDistRight;
			minDistLeft  = recurse (Lx, Ly, closestPairLeft);
			minDistRight = recurse (Rx, Ry, closestPairRight);


			double minDist;
			// selects the closest pair from the quadrants
			if (minDistLeft < minDistRight)
			{
				minDist = minDistLeft;

				closestPair[0][0] = closestPairLeft[0][0];
				closestPair[0][1] = closestPairLeft[0][1];

				closestPair[1][0] = closestPairLeft[1][0];
				closestPair[1][1] = closestPairLeft[1][1];
			}
			else
			{
				minDist = minDistRight;

				closestPair[0][0] = closestPairRight[0][0];
				closestPair[0][1] = closestPairRight[0][1];

				closestPair[1][0] = closestPairRight[1][0];
				closestPair[1][1] = closestPairRight[1][1];
			}

			return minDist;
		}
	}


	private static double combine (Vector Mx, Vector My, double d_min,
				       int [][] closestPair)
	// looks for the closest pair in the middles
	{
		int axis = 1;
		// y-axis search
		d_min = combine (d_min, My, axis, closestPair);

		axis = 0;
		// x-axis search
		d_min = combine (d_min, Mx, axis, closestPair);
		return d_min;
	}


	private static double combine (double d_min, Vector M, int axis,
				       int [][] closestPair)
	// updates the closest pair if a closer one is found in the middle
	{
		int [][] pair = new int [2][2];
		int ceilMinDist = ( (int) ceil(d_min) );
		// considers the left (lower) and right (upper) halves
		double d = ClosestPairBound (M, pair, axis, ceilMinDist);

		// updates the closest pair
		if (d < d_min)
		{
			d_min = d;

			closestPair[0][0] = pair[0][0];
			closestPair[0][1] = pair[0][1];

			closestPair[1][0] = pair[1][0];
			closestPair[1][1] = pair[1][1];
		}

		return d_min;
	}


	private static
	double ClosestPairBound (Vector V, int [][] closestPair, int axis,
			         int d_min)
	/*
	 * Synopsis:
	 * Looks for the closest pair near the boundaries. It does it work
	 * by dividing the dataset V into left (lower) and right (upper)
	 * subsets. Then it prunes elements in those subsets which are too
	 * far to be closest-pair candidates. If either of the subsets does
	 * not have any elements after pruning (empty) the method returns
	 * the maximum double; otherwise, it returns the distance of the
	 * closest pair between the left (lower) and right (upper) subsets.
	 *
	 * Inputs:
	 * V		shallow copy of a Px (or Py) dataset
	 * closestPair	uninitialized 2nd-rank array
	 * axis		if axis == 0, the x-axis; if axis == 1, the y-axis
	 * d_min	integer greater than or equal to the min distance
	 *
	 * Outputs:
	 * min		smallest distance of particles near boundary
	 * closestPair	coordinates of the respective particles
	 *
	 */
	{
		String errmsg = ("bound(): axis must be zero or one");
		if (axis < 0 || axis > 1)
			throw new RuntimeException(errmsg);

		int d;	// distance
		int ax;	// either the x or y-axis coordinate

		// gets first half, Left (lower) subset
		Vector L = V;
		// pops the second half into the Right (upper) subset
		Vector R = halves (V);


		/* prunes particles on the left or lower subset */

		int [] coord;
		// gets coordinates of the particle closest to boundary
		// NOTE: The first particle in the Right (upper) subset
		//       is closer
		int [] middle = R.index (R, 0);
		// gets the x or y-coordinate of the selected particle
		int ax_m = middle[axis];

		int idx = (L.size (L) - 1);
		int size = L.size (L);
		for (int n = 0; n != size; ++n)
		// pops particles farther than the closest pair in `Left'
		// NOTE: Removes elements from the back for speed.
		{
			coord = L.index (L, idx);
			ax = coord[axis];

			d = (ax > ax_m)? (ax - ax_m): (ax_m - ax);
			if (d > d_min)
				L.pop (L, idx);
			--idx;
		}


		// returns if Left subset has no elements after pruning,
		// meaning that there are no closest-pair candidates
		if ( L.size(L) == 0 )
			return Double.MAX_VALUE;


		/* prunes particles on the right or upper subset */



		// pops elements from the back of vector since these are
		// farther from the boundary
		idx = (R.size (R) - 1);
		// gets coordinates of the particle closest to boundary
		// NOTE: The last particle in the lower quadrant is closer
		middle = L.index (L, L.size(L) - 1);
		// gets the x or y-coordinate of the selected particle
		ax_m = middle[axis];
		size = R.size (R);
		for (int n = 0; n != size; ++n)
		// pops particles farther than the closest pair in `Right'
		{
			coord = R.index (R, idx);
			ax = coord[axis];

			d = (ax > ax_m)? (ax - ax_m): (ax_m - ax);
			if (d > d_min)
				R.pop (R, idx);
			else
				break;
			--idx;
		}

		if ( L.size(L) != 0 && R.size(R) != 0 )
			return distance (L, R, closestPair);
		else
			return Double.MAX_VALUE;
	}


	private static double distance (Vector data, int [][] closestPair)
	// finds the Closest Pair via the Brute Force Algorithm
	{
		Vector coords = data;
		// complains if the 2nd-rank array is not 2 x 2
		shape (closestPair);

		double d;			// distance from P to Q
		double x_p, y_p;		// x, y coords of P
		double x_q, y_q;		// x, y coords of Q
		int [] P = new int [2];		// x, y coords of P
		int [] Q = new int [2];		// x, y coords of Q

		int N = coords.size (coords);
		double d_min = Double.MAX_VALUE;// uses maximum double
		for (int i = 0; i != (N - 1); ++i)
		{
			for (int j = (i + 1); j != N; ++j)
			{
				// gets coordinates of P and Q
				P = data.index (data, i);
				Q = data.index (data, j);

				// unpacks coordinates
				x_p = ( (double) P[0] );
				y_p = ( (double) P[1] );

				x_q = ( (double) Q[0] );
				y_q = ( (double) Q[1] );

				// computes the squared distance for speed
				d = (x_p - x_q) * (x_p - x_q) +
				    (y_p - y_q) * (y_p - y_q);

				if (d < d_min)
				{
					d_min = d;
					closestPair[0] = P;
					closestPair[1] = Q;
				}
			}

		}

		return sqrt(d_min);	// returns min distance
	}


	private static double distance (Vector V1, Vector V2, int[][] Pair)
	// Finds the Closest Pair near boundaries with Brute Force.
	// The dataset `coords1' contains the particles in the left (lower)
	// subset and the dataset `coords2' contains the particles in the
	// right (upper) subset. The method finds the closest pair by
	// comparing the distance of the particles in the Left subset
	// with those on the Right subset. Note that at this point the
	// algorithm has considered the distances within the quadrants,
	// thus all that's left to do is consider the boundaries as it is
	// done here.
	{
		Vector coords1 = V1;
		Vector coords2 = V2;
		// complains if the 2nd-rank array is not 2 x 2
		int [][] closestPair = Pair;
		shape (closestPair);

		double d;			// distance from P to Q
		double x_p, y_p;		// x, y coords of P
		double x_q, y_q;		// x, y coords of Q
		int [] P = new int [2];		// x, y coords of P
		int [] Q = new int [2];		// x, y coords of Q

		int N = coords1.size (coords1);
		int M = coords2.size (coords2);
		double d_min = Double.MAX_VALUE;// uses maximum double
		for (int i = 0; i != N; ++i)
		{
			for (int j = 0; j != M; ++j)
			{
				// gets coordinates of P and Q
				P = coords1.index (coords1, i);
				Q = coords2.index (coords2, j);

				// unpacks coordinates
				x_p = ( (double) P[0] );
				y_p = ( (double) P[1] );

				x_q = ( (double) Q[0] );
				y_q = ( (double) Q[1] );

				// computes the squared distance for speed
				d = (x_p - x_q) * (x_p - x_q) +
				    (y_p - y_q) * (y_p - y_q);

				if (d < d_min)
				{
					d_min = d;
					closestPair[0] = P;
					closestPair[1] = Q;
				}
			}

		}

		return sqrt(d_min);	// returns min distance
	}


	private static void shape (int [][] closestPair)
	// complains if the shape of the array is not 2 x 2
	{
		// checks the number of rows
		if (closestPair.length != 2)
			throw new RuntimeException(
				"distance(): expects array of two rows"
			);

		// checks the number of columns
		if (closestPair[0].length != 2)
			throw new RuntimeException(
				"distance(): expects array of two columns"
			);
	}


	private static Vector Dataset()
	// creates dataset for non-recursive Divide and Conquer Solution
	{

		/* test sets */

		//int [][] P = {
		//	{ 2,  7}, { 4, 13}, { 5,  7}, {10,  5},
		//	{13,  9}, {15,  5}, {17,  7}, {19, 10},
		//	{22,  7}, {25, 10}, {29, 14}, {30,  2}
		//};
		//

		//int [][] P = {
		//	{ 1, 2}, { 1, 11}, { 7,  8}, { 9,  9}, {12, 13},
		//      {13, 4}, {20,  8}, {22,  3}, {23, 12}, {25, 14},
		//	{26, 7}, {31, 10}
		//};
		//

		int [][] P = {
			{-118,   26}, { 70, -46}, {-26, -43}, {  87,  -23},
			{ -60, -103}, {-79,  66}, {  6,  99}, {  94,   23},
			{  36,  -81}, { 52,   4}, { 72, -69}, {-119, -114},
			{-113,   58}, {-30, -35}, {-78,  18}, {  29,  117},
			{-123, -100}, { 13, -47}, {-52,-128}, {  44,   40},
			{  80,  -33}, {-99,   1}, {127,  81}, {  46,   95},
			{  85,   11}, {-64,  43}, {114,  30}, {  21,  109},
			{  69,   90}, {-42,-110}, {120,  24}, { -10,   35}
		};

		Vector coords = new Vector();
		coords.push_back(coords, P);

		return coords;
	}
}


/*
 * TODO:
 * [x] implement the recursive Divide and Conquer Algorithm
 * [ ] improve the vector class so that it can use binary search
 *     when dividing x-y sorted datasets into x-y sorted quadrants.
 */
