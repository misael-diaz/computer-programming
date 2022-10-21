/*
 * Algorithms and Complexity                               October 21, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Conducts Time Complexity Experiments of the Convex Hull problem.
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

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Time	// Time Complexity Experiment Class
{

	public static void main (String [] args)
	{
		exportBruteForce();	// exports time complexity results
	}


	private static double [] replicateBruteForce (int size)
	// repeats the numeric experiment
	{
		int reps = 64;
		double etime = 0;
		double operations = 0;
		for (int i = 0; i != reps; ++i)
		{
			/*

			Tries to obtain the convex hull from the dataset.
			If the dataset produces a "bad hull" it catches the
			exception thrown by the invoked method to produce a
			new dataset and tries again until an acceptable
			convex hull is found.

			*/

			int sw = 1;
			Vector data = ConvexHull.genDataSet(size);
			while (sw != 0)
			{
				try
				{
					ConvexHull.BruteForce(data);
					sw = 0;
				}
				catch (RuntimeException e)
				{
					data = ConvexHull.genDataSet(size);
				}
			}
			// updates the elapsed-time
			etime += ConvexHull.etimeBruteForce;
		}

		// computes the averages and return statistics
		double avg_etime = (etime / reps);
		// NOTE: the brute force method does the same #operations
		double avg_operations = size * size * (size - 1) / 2;
		double [] statistics = {avg_etime, avg_operations};
		return statistics;
	}


	private static double [][] experimentsBruteForce ()
	// obtains the elapsed-time and #operations with respect to size
	{
		int runs = 8;
		int size = 16;
		double [][] statistics = new double[3][runs];
		double [] sizes = statistics[0];
		double [] etimes = statistics[1];
		double [] operations = statistics[2];
		for (int i = 0; i != runs; ++i)
		{
			// repeats the numeric experiment
			double [] stat = replicateBruteForce (size);
			// gets the average elapsed time and #operations
			double etime = stat[0], count = stat[1];
			sizes[i] = ( (double) size );
			etimes[i] = etime;
			operations[i] = count;
			// doubles the system size for the next experiment
			size *= 2;
		}

		return statistics;
	}


	private static void exportBruteForce ()
	// exports the elapsed-time and #operations with respect to size
	{
		try
		{
			String file = ("timeBruteForce.dat");
			PrintWriter out = new PrintWriter(file);
			// conducts the experiments
			double [][] stats = experimentsBruteForce();
			// gets the number of experiments (or runs)
			int runs = stats[1].length;
			for (int i = 0; i != runs; ++i)
			{
				// gets size, elapsed-time, and #operations
				double size  = stats[0][i];
				double etime = stats[1][i];
				double opers = stats[2][i];
				// writes data to file in tabulated format
				String fmt = ("%16.8e %16.8e %16.8e\n");
				out.printf(fmt, size, etime, opers);
			}
			out.close();
		}
		catch (FileNotFoundException err)
		{
			err.printStackTrace();
		}
	}
}
