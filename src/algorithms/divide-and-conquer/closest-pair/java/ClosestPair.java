/*
 * Algorithms and Programming II                               May 02, 2022
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

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;
import static java.lang.Math.sqrt;
import static java.lang.Math.ceil;

class ClosestPair_DivideAndConquerAlgorithm
{

	public static void main (String[] args)
	{

		/* initial examples have been disabled */


		// divides into left and right subsets
		//halves ();
		// divides into quadrants and finds the closest pairs
		//quadrants ();


		/* Non-recursive Divide and Conquer Algorithm */
		// NOTE: Applies for datasets consisting of 12 coordinates


		Vector P = Dataset();
		int [][] closestPair = new int [2][2];
		double d_min = ClosestPair (P, closestPair);

		// prints coordinates of the closest pair
		Vector.Coord first  = new Vector.Coord( closestPair[0] );
		Vector.Coord second = new Vector.Coord( closestPair[1] );

		System.out.println("\n");
		System.out.printf("distance: %.4f\n", d_min);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);
		System.out.println("\n");

		return;
	}


	private static Vector Dataset()
	// creates dataset for non-recursive Divide and Conquer Solution
	{
		int [][] P = {
			{ 2,  7}, { 4, 13}, { 5,  7}, {10,  5},
			{13,  9}, {15,  5}, {17,  7}, {19, 10},
			{22,  7}, {25, 10}, {29, 14}, {30,  2}
		};

		Vector coords = new Vector();
		coords.push_back(coords, P);

		return coords;
	}


	private static void BruteForce ()
	{
		// dataset placeholder
		Vector coords = random_dataset();
		// closest pair placeholder
		Vector closest = new Vector();

		// finds the closest pair
		int [][] closestPair = new int [2][2];
		double d_min = distance (coords, closestPair);

		// prints coordinates of the closest pair on the console
		closest.push_back (closest, closestPair);
		coords.print (coords);
		System.out.printf("\nClosest Pair:\n");
		System.out.printf("distance: %.12f\n", d_min);
		closest.print (closest);

		// exports dataset of coordinates
		String file = ("coordinates.txt");
		export (coords, file);
		// exports coordinates of closest pair
		file = ("closest-pair.txt");
		export (closest, file);
		return;
	}


	private static Vector random_dataset ()
	// creates dataset of coordinates without duplicates
	{
		int size = 16;
		Random rand = new Random();
		Vector coords = new Vector();

		int [] coord = new int [2];
		// pushes distinct coordinates unto the back of the vector
		for (int i = 0; i != size; ++i)
		{
			do
			// creates new coordinates if these exist in vector
			{
				coord[0] = rand.nextInt(64);
				coord[1] = rand.nextInt(64);
			}
			while ( coords.search(coords, coord) != 0 );
			// inserts new coordinates
			coords.push_back(coords, coord);
		}
		coords.sort(coords);
		return coords;
	}


	private static double distance(Vector coords, int [][] closestPair)
	// finds the Closest Pair via the Brute Force Algorithm
	{
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
				P = coords.index (coords, i);
				Q = coords.index (coords, j);

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


	private static
	double distance (Vector coords1, Vector coords2, int [][] Pair)
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



	private static double distance (int b, int e, Vector coords,
					int [][] closestPair)
	// finds the Closest Pair in [b, e) via the Brute Force Algorithm
	{
		// complains if the 2nd-rank array is not 2 x 2
		shape (closestPair);

		double d;			// distance from P to Q
		double x_p, y_p;		// x, y coords of P
		double x_q, y_q;		// x, y coords of Q
		int [] P = new int [2];		// x, y coords of P
		int [] Q = new int [2];		// x, y coords of Q

		double d_min = Double.MAX_VALUE;// uses maximum double
		for (int i = b; i != (e - 1); ++i)
		{
			for (int j = (i + 1); j != e; ++j)
			{
				// gets coordinates of P and Q
				P = coords.index (coords, i);
				Q = coords.index (coords, j);

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


	private static void export (Vector vector, String filename)
	// writes coordinates in vector to a file
	{
		// creates datafile
		try
		{
			File f = new File (filename);
			f.createNewFile();
		}
		catch (IOException err)
		{
			err.printStackTrace();
		}

		// writes data to file
		try
		{
			PrintWriter out = new PrintWriter (filename);
			int size = vector.size (vector);
			int [][] data = new int [size][2];
			for (int i = 0; i != size; ++i)
				data[i] = vector.pop (vector);

			int x, y;
			for (int i = 0; i != size; ++i)
			{
				x = data[i][0];
				y = data[i][1];
				out.printf("%6d %6d\n", x, y);
			}

			out.close();
		}
		catch (FileNotFoundException err)
		{
			err.printStackTrace();
		}
	}


	private static void halves ()
	// divides the dataset into ``Left'' and ``Right'' subsets
	{
		// stores original dataset P as the Left subset, Lx
		Vector Lx = Dataset();

		// sorts original dataset to obtain Px
		Lx.sort (Lx);

		// pops elements of Px in the range [m, e) to obtain Rx:
		int size = Lx.size (Lx);		// size
		int m = size / 2, e = size;		// middle and end
		Vector Rx = new Vector();		// Right subset, Rx
		Rx.push_back (Rx, Lx.pop(Lx, m, e) );	// pops by range

		// fits vectors to size to reduce memory usage
		Lx.fit (Lx);
		Rx.fit (Rx);
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


	private static void quadrants ()
	// divides the dataset into ``quadrant'' subsets, Ly and Ry, and
	// finds the closest pairs in each quadrant
	{
		// stores original dataset P as the Left subset, Lx
		Vector Lx = Dataset();

		System.out.println("\noriginal dataset:\n");
		Lx.print(Lx);

		System.out.println("\nsorted dataset:\n");
		// sorts original dataset to obtain Px
		Lx.sort (Lx);
		Lx.print(Lx);

		// pops elements of Px in the range [m, e) to obtain Rx:
		int size = Lx.size (Lx);		// size
		int b = size / 2, e = size;		// middle and end
		Vector Rx = new Vector();		// Right subset, Rx
		Rx.push_back (Rx, Lx.pop(Lx, b, e) );	// pops by range

		// fits vectors to size to reduce memory usage
		Lx.fit (Lx);
		Rx.fit (Rx);

		// prints Left subset on the console
		System.out.println("\nLx:\n");
		Lx.print (Lx);

		// prints Right subset on the console
		System.out.println("\nRx:\n");
		Rx.print (Rx);


		// stores original dataset in Ly
		System.out.println("\nPy:\n");
		Vector Ly = Dataset();
		// sorts dataset to obtain Py
		Ly.sort (Ly, Ly.comparator);
		Ly.print(Ly);


		int pos;
		size = Ly.size (Ly);
		Vector Ry = new Vector ();
		int [] coord = new int [2];
		for (int idx = 0; idx != size; ++idx)
		// pushes Rx elements in Py into Ry
		{
			coord = Ly.index(Ly, idx);
			pos = Rx.search(Rx, coord);
			if (pos != 0)
				Ry.push_back( Ry, coord );
		}

		int idx;
		for (int i = 0; i != size; ++i)
		// pops Rx elements in Py from back to front to obtain Ly
		{
			idx = size - (i + 1);
			coord = Ly.index(Ly, idx);
			pos = Rx.search(Rx, coord);
			if (pos != 0)
				Ly.pop(Ry, idx);
		}

		System.out.println("\nLy:\n");
		Ly.print(Ly);
		System.out.println("\nRy:\n");
		Ry.print(Ry);


		// creates placeholders for closest pairs in the quadrants
		int [][] pairI   = new int[2][2];
		int [][] pairII  = new int[2][2];
		int [][] pairIII = new int[2][2];
		int [][] pairIV  = new int[2][2];


		// finds the closest pair in the fourth quadrant, IV
		size = Ry.size(Ry);
		b = 0;	e = size / 2;
		double dIV = distance(b, e, Ry, pairIV);
		System.out.println();
		System.out.printf("quadrant IV:\n");
		System.out.printf("distance: %.4f\n", dIV);
		// prints coordinates of closest pair in the quadrant
		Vector.Coord first  = new Vector.Coord(pairIV[0]);
		Vector.Coord second = new Vector.Coord(pairIV[1]);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);


		// finds the closest pair in the first quadrant, I
		b = size / 2;	e = size;
		double dI = distance(b, e, Ry, pairI);
		System.out.println();
		System.out.printf("quadrant I:\n");
		System.out.printf("distance: %.4f\n", dI);
		// prints coordinates of closest pair in the quadrant
		first  = new Vector.Coord(pairI[0]);
		second = new Vector.Coord(pairI[1]);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);


		// finds the closest pair in the third quadrant, III
		size = Ly.size(Ly);
		b = 0;	e = size / 2;
		double dIII = distance(b, e, Ly, pairIII);
		System.out.println();
		System.out.printf("quadrant III:\n");
		System.out.printf("distance: %.4f\n", dIII);
		// prints coordinates of closest pair in the quadrant
		first  = new Vector.Coord(pairIII[0]);
		second = new Vector.Coord(pairIII[1]);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);


		// finds the closest pair in the second quadrant, II
		b = size / 2;	e = size;
		double dII = distance(b, e, Ly, pairII);
		System.out.println();
		System.out.printf("quadrant II:\n");
		System.out.printf("distance: %.4f\n", dII);
		// prints coordinates of closest pair in the quadrant
		first  = new Vector.Coord(pairII[0]);
		second = new Vector.Coord(pairII[1]);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);


		// finds the closest pair on the right half
		double minDistanceRight;
		int [][] closestPairRight = new int [2][2];
		if (dI < dIV)
		{
			minDistanceRight = dI;
			closestPairRight = pairI;
		}
		else
		{
			minDistanceRight = dIV;
			closestPairRight = pairIV;
		}


		// finds the closest pair on the left half
		double minDistanceLeft;
		int [][] closestPairLeft = new int [2][2];
		if (dII < dIII)
		{
			minDistanceLeft = dII;
			closestPairLeft = pairII;
		}
		else
		{
			minDistanceLeft = dIII;
			closestPairLeft = pairIII;
		}


		// finds the closest pair from the quadrants
		double d_min;
		int [][] closestPair = new int [2][2];
		if (minDistanceLeft < minDistanceRight)
		{
			d_min = minDistanceLeft;
			closestPair = closestPairLeft;
		}
		else
		{
			d_min = minDistanceRight;
			closestPair = closestPairRight;
		}


		System.out.println();
		System.out.printf("closest pair in quadrants:\n");
		System.out.printf("distance: %.4f\n", d_min);
		// prints coordinates of closest pair in the quadrant
		first  = new Vector.Coord(closestPair[0]);
		second = new Vector.Coord(closestPair[1]);
		System.out.printf("coordinates:\n");
		first.print(first);
		second.print(second);
	}


	private static Vector build_yxSortedHalves (Vector Py, Vector Rx)
	/*
	 * Synopsis:
	 * Constructs y-x sorted, Left (Ly) and Right (Ry) subsets by
	 * popping elements in Py (a y-x sorted dataset) that are present
	 * in Rx (x-y sorted Right subset). The construction is carried out
	 * so that no sorting of Ly and Ry is not required, that is the y-x
	 * ordering of Py is preserved.
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
		int size = Ly.size (Ly);	// number of elements
		int [] coord = new int [2];	// coordinate placeholder
		for (int idx = 0; idx != size; ++idx)
		// pops Rx elements in Py from back to front to obtain Ly
		{
			coord = Ly.index(Ly, idx);
			pos = Rx.search(Rx, coord);
			if (pos != 0)
				Ry.push_back( Ry, coord );
		}

		int idx;
		for (int i = 0; i != size; ++i)
		// pops Rx elements in Py from back to front to obtain Ly
		{
			idx = size - (i + 1);
			coord = Ly.index(Ly, idx);
			pos = Rx.search(Rx, coord);
			if (pos != 0)
				Ly.pop(Ly, idx);
		}

		// fits vectors to size to reduce memory usage
		Ly.fit(Ly);
		Ry.fit(Ry);

		return Ry;
	}


	private static
	double dirClosestPairHalf (Vector P, int [][] Pair)
	// Uses the direct method to find the closest pair in a half subset
	{
		Vector coords = P;
		int [][] closestPair = Pair;
		return distance (coords, closestPair);
	}


	private static
	double dirClosestPairQuad (int b, int e, Vector P, int [][] Pair)
	// Uses the direct method to find the closest pair in a quadrant
	{
		Vector coords = P;
		int [][] closestPair = Pair;
		return distance (b, e, coords, closestPair);
	}


	private static
	double dirClosestPairQuad (Vector P, int [][] closestPair)
	/*
	 * Synopsis:
	 * Uses the direct solution to determine the closest pair in the
	 * quadrants I (II) and IV (III). Note that the method is meant
	 * to used on the Left subset Ly or the Right subset Ry.
	 *
	 */
	{
		// complains if the direct solution should not be applied
		is_direct (P);

		// creates placeholders for closest pairs in the quadrants
		int [][] closestPairQuadL = new int[2][2];	// lower
		int [][] closestPairQuadU = new int[2][2];	// upper
		int [][] closestPairQuadB = new int[2][2];	// boundary
		// creates a shallow copy for working on the middle section
		Vector V = P.copy (P);


		// divides so that there are three (or fewer) particles
		int b = 0, m = (P.size(P) / 2), e = P.size(P);
		// finds closest pair in quadrants I (II) and IV (III)
		double dL = dirClosestPairQuad (b, m, P, closestPairQuadL);
		double dU = dirClosestPairQuad (m, e, P, closestPairQuadU);


		double minDistQuads;
		// selects the closest of the closest pairs
		if (dL < dU)
		{
			minDistQuads = dL;

			// copies x, y coordinates of first particle
			closestPair[0][0] = closestPairQuadL[0][0];
			closestPair[0][1] = closestPairQuadL[0][1];

			// copies x, y coordinates of second particle
			closestPair[1][0] = closestPairQuadL[1][0];
			closestPair[1][1] = closestPairQuadL[1][1];
		}
		else
		{
			minDistQuads = dU;

			// copies x, y coordinates of first particle
			closestPair[0][0] = closestPairQuadU[0][0];
			closestPair[0][1] = closestPairQuadU[0][1];

			// copies x, y coordinates of second particle
			closestPair[1][0] = closestPairQuadU[1][0];
			closestPair[1][1] = closestPairQuadU[1][1];
		}

		return minDistQuads;
	}


	private static
	double ClosestPairBound (Vector V, int [][] closestPair,
				 int axis, int d_min)
	/*
	 * Synopsis:
	 * Looks for the closest pair near the boundaries. It does it work
	 * by dividing the dataset V into left (lower) and right (upper)
	 * subsets. Then it prunes elements in those subsets which are too
	 * far to be closest-pair candidates. If either of the subsets does
	 * not have any elements after pruning (empty) the method returns
	 * the maximum double; otherwise, it returns the distance of the
	 * closest pair between the left (lower) and right (upper) subsets.
	 * The method is generic in the sense that it can be used to look
	 * for closest pairs between the left and right halves (axis == 0)
	 * and between the lower and upper halves (axis == 1).
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

		// gets first half, Left (or lower) subset
		Vector L = V;
		// pops the second half into the Right (or upper) subset
		Vector R = halves (V);


		/* prunes particles on the Left (or lower) subset */

		int [] coord;
		// gets coordinates of the particle closest to boundary
		// NOTE: Selects the first particle in the Right (upper)
		//       half is for it is closer to those in the Left
		//       (lower) half.
		int [] middle = R.index (R, 0);
		// gets the x or y-coordinate of the selected particle
		int ax_m = middle[axis];

		int idx = (L.size(L) - 1);
		int size = L.size (L);
		for (int n = 0; n != size; ++n)
		// pops particles farther than the closest pair in `Left'
		{
			// gets x (y) coordinate of the referenced particle
			coord = L.index (L, idx);
			ax = coord[axis];

			d = (ax > ax_m)? (ax - ax_m): (ax_m - ax);
			// calculates distance (magnitude) and prunes those
			// too far to be closest-pair candidates
			if (d > d_min)
				L.pop (L, idx);
			--idx;
		}


		// returns if Left subset has no elements after pruning,
		// meaning that there are no closest-pair candidates
		if ( L.size(L) == 0 )
			return Double.MAX_VALUE;


		/* prunes particles on the Right (or upper) subset */

		// pops elements from the back of vector since these are
		// known to be farther from the Left (lower) boundary
		idx = (R.size (R) - 1);
		// gets coordinates of the particle closest to boundary
		// NOTE: The last particle in the Left (lower) half is
		//       closer to the boundary.
		middle = L.index (L, L.size(L) - 1);
		// gets the x or y-coordinate of the selected particle
		ax_m = middle[axis];
		size = R.size (R);
		for (int n = 0; n != size; ++n)
		// pops particles farther than the closest pair in `Right'
		{
			coord = R.index (R, idx);
			ax = coord[axis];

			// calculates the distance and prunes those too far
			// to be closest-pair candidates
			d = (ax > ax_m)? (ax - ax_m): (ax_m - ax);
			if (d > d_min)
				R.pop (R, idx);
			else
				break;
			--idx;
		}

		// returns the closest pair in the boundary if there is any
		// otherwise returns infinity to signal the caller that
		// there were no pairs closer than the current one.
		if ( L.size(L) != 0 && R.size(R) != 0 )
			return distance (L, R, closestPair);
		else
			return Double.MAX_VALUE;
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

		String errmsg = ("ClosestPair(): no pairs to compare");
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

			// invokes divide and conquer implementation
			return findClosestPair (Px, Py, closestPair);
		}
	}


	private static
	double findClosestPair (Vector Px, Vector Py, int [][] closestPair)
	// implements non-recursive divide and conquer algorithm
	{


		/* assumes that dividing into halves is required: */


		// creates a shallow copies for working on the boundaries
		Vector Mx = Px.copy (Px);
		Vector My = Py.copy (Py);

		// builds x-y sorted Left and Right subsets
		Vector Rx = build_xySortedHalves (Px);
		Vector Lx = Px;

		// prints Left x-y sorted subset on the console
		System.out.println("\nLx:\n");
		Lx.print (Lx);

		// prints Right x-y sorted subset on the console
		System.out.println("\nRx:\n");
		Rx.print (Rx);


		/* assumes the direct method is applicable on quadrants: */


		// builds y-x sorted Left and Right subsets
		Vector Ry = build_yxSortedHalves (Py, Rx);
		Vector Ly = Py;

		// prints Left y-x sorted subset on the console
		System.out.println("\nLy:\n");
		Ly.print (Ly);

		// prints Right y-x sorted subset on the console
		System.out.println("\nRy:\n");
		Ry.print (Ry);


		double minDistLeft;
		double minDistRight;
		int [][] closestPairLeft   = new int [2][2];
		int [][] closestPairRight  = new int [2][2];
		// finds closest pair in quadrants via direct method
		minDistLeft  = dirClosestPairQuad (Ly, closestPairLeft);
		minDistRight = dirClosestPairQuad (Ry, closestPairRight);


		double minDistQuads;
		// selects the closest pair from the quadrants
		if (minDistLeft < minDistRight)
		{
			minDistQuads = minDistLeft;

			closestPair[0][0] = closestPairLeft[0][0];
			closestPair[0][1] = closestPairLeft[0][1];

			closestPair[1][0] = closestPairLeft[1][0];
			closestPair[1][1] = closestPairLeft[1][1];
		}
		else
		{
			minDistQuads = minDistRight;

			// copies x, y coordinates of first particle
			closestPair[0][0] = closestPairRight[0][0];
			closestPair[0][1] = closestPairRight[0][1];

			// copies x, y coordinates of second particle
			closestPair[1][0] = closestPairRight[1][0];
			closestPair[1][1] = closestPairRight[1][1];
		}


		/* looks for the closest pair near the boundaries: */


		int axis = 1;	// selects y-axis coordinates
		int ceilMinDistQuads = ( (int) ceil(minDistQuads) );
		int [][] closestPairQuads = new int [2][2];
		// considers the Lower and Upper halves
		double minDist = ClosestPairBound (My, closestPairQuads,
				                   axis, ceilMinDistQuads);

		double d_min = minDistQuads;
		// updates the closest pair
		if (minDist < minDistQuads)
		{
			d_min = minDist;

			// copies x, y coordinates of first particle
			closestPair[0][0] = closestPairQuads[0][0];
			closestPair[0][1] = closestPairQuads[0][1];

			// copies x, y coordinates of second particle
			closestPair[1][0] = closestPairQuads[1][0];
			closestPair[1][1] = closestPairQuads[1][1];
		}



		axis = 0;	// selects x-axis coordinates
		int ceilMinDist = ( (int) ceil(d_min) );
		int [][] closestPairHalves = new int [2][2];
		// considers the Left and Right halves
		minDist = ClosestPairBound (Mx, closestPairHalves,
				            axis, ceilMinDist);

		// updates the closest pair
		if (minDist < d_min)
		{
			d_min = minDist;

			// copies x, y coordinates of first particle
			closestPair[0][0] = closestPairHalves[0][0];
			closestPair[0][1] = closestPairHalves[0][1];

			// copies x, y coordinates of second particle
			closestPair[1][0] = closestPairHalves[1][0];
			closestPair[1][1] = closestPairHalves[1][1];
		}

		return d_min;
	}


	private static void is_direct (Vector P)
	// Complains if the direct solution cannot be applied as is
	{
		int size = P.size (P);
		String errmsg = ("direct(): #particles is less than two");
		if (size < 2)
			throw new RuntimeException (errmsg);

		errmsg = ("direct(): #particles is greater than six");
		if (size > 6)
			throw new RuntimeException (errmsg);
	}
}
