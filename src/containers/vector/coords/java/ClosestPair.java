/*
 * Algorithms and Programming II                             April 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Solves the Closest Pair problem with the Brute Force Algorithm.
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

public class ClosestPair
{

	public static void main (String[] args)
	{
		Vector coords = dataset();	// dataset placeholder
		Vector closest = new Vector();	// closest pair placeholder

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


	private static Vector dataset ()
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

		return Math.sqrt(d_min);	// returns min distance
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
}
