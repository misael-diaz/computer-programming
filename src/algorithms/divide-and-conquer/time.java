/*
 * Algorithms and Programming II                               May 20, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Obtains the average number of comparisons done by the implementation of
 * the Divide and Conquer Algorithm that solves the Closest Pair problem
 * as a function of the system size. Results are stored in a plain text
 * file for post-processing.
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
import java.util.Random;

class time
{
	//import static recursiveClosestPair.gCounter;

	public static void main (String[] args)
	{

		String file = ("results/time.txt");
		// creates plain text file for writing results
		try
		{
			File f = new File (file);
			f.createNewFile();
		}
		catch (IOException err)
		{
			err.printStackTrace();
		}

		// writes the average number of comparisons as a function
		// of the system size
		try
		{
			PrintWriter out = new PrintWriter (file);

			double comparisons;
			String fmt = ("%8d %16.1f\n");	// format
			for (int size = 16; size <= 16384; size*=2)
			{
				comparisons = average (size, 256);
				out.printf(fmt, size, comparisons);
			}

			out.close();
		}
		catch (FileNotFoundException err)
		{
			err.printStackTrace();
		}

		return;
	}


	private static double average (int size, int reps)
	/*
	 * Synopsis:
	 * Returns the average number of comparisons done by the
	 * implementation of the Divide and Conquer Algorithm that solves
	 * the closest pair problem.
	 *
	 * Inputs:
	 * size		system size (number of particles in the system)
	 * reps		repetitions
	 *
	 * Output:
	 * avg		average number of comparisons done
	 */
	{
		Vector P = Dataset (size);
		int [][] closestPair = new int [2][2];

		int count = 0;
		for (int i = 0; i != reps; ++i)
		// increments counter by number of comparisons done
		{
			recursiveClosestPair.gCounter = 0;
			recursiveClosestPair.ClosestPair (P, closestPair);
			count += recursiveClosestPair.gCounter;
		}

		// obtains and returns the average number of comparisons
		return ( ( (double) count ) / ( (double) reps ) );
	}


	private static Vector Dataset (int size)
	/*
	 * Synopsis:
	 * Creates a dataset of x, y coordinates from uniformly distributed
	 * random integers. The coordinates represent the positions of a
	 * system of N particles, where N = `size'. Note that the number
	 * density of the system is kept constant by specifying a
	 * size-dependent upper limit for the pseudo-random number
	 * generator.
	 *
	 * Input:
	 * size		number of elements in the dataset
	 *
	 * Output:
	 * coords	dataset of size `size' stored in a vector
	 *
	 */
	{
		Random rand = new Random();
		Vector coords = new Vector();

		int [] coord = new int [2];
		// pushes distinct coordinates unto the back of the vector
		for (int i = 0; i != size; ++i)
		{
			do
			// creates new x, y coordinates
			{
				coord[0] = rand.nextInt (16 * size);
				coord[1] = rand.nextInt (16 * size);
			}
			while ( coords.search(coords, coord) != 0 );
			// pushes coordinates into back of vector
			coords.push_back(coords, coord);
		}

		return coords;
	}
}
