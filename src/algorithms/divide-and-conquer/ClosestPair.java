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

class ClosestPair_DivideAndConquerAlgorithm
{

	public static void main (String[] args)
	{
		// divides into left and right subsets
		halves ();
		// divides into quadrants and finds the closest pairs
		quadrants ();

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
		for (int i = 0; i != P.length; ++i)
			coords.push_back(coords, P[i]);

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

		System.out.println("\noriginal dataset:\n");
		Lx.print(Lx);

		System.out.println("\nsorted dataset:\n");
		// sorts original dataset to obtain Px
		Lx.sort (Lx);
		Lx.print(Lx);

		// pops elements of Px in the range [m, e) to obtain Rx:
		int size = Lx.size (Lx);		// size
		int m = size / 2, e = size;		// middle and end
		Vector Rx = new Vector();		// Right subset, Rx
		Rx.push_back (Rx, Lx.pop(Lx, m, e) );	// pops by range

		// fits vectors to size to reduce memory usage
		Lx.fit (Lx);
		Rx.fit (Rx);

		// prints Left subset on the console
		System.out.println("\nLx:\n");
		Lx.print (Lx);

		// prints Right subset on the console
		System.out.println("\nRx:\n");
		Rx.print (Rx);
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
		int idx = 0;
		size = Ly.size (Ly);
		Vector Ry = new Vector ();
		int [] coord = new int [2];
		// pops elements in Py matching Rx while inserting into Ry
		// to obtain both Ly and Ry
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
				Ry.push_back( Ry, Ly.pop(Ly, coord) );
			else
				++idx;
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
	}
}
