/*
 * Algorithms and Complexity                               October 11, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Conducts Time Complexity Experiments of the Closest Pair Problem.
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

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Time	// Time Complexity Experiment Class
{

	public static void main (String [] args)
	{
		export();	// exports time complexity results
	}


	private static double [] replicate (int size)
	// repeats the numeric experiment
	{
		int reps = 512;
		double etime = 0;
		double operations = 0;
		for (int i = 0; i != reps; ++i)
		{
			// resets counter
			Ensemble.operations = 0;
			Tree data = Ensemble.genDataSet (size);
			// solves the closest pair problem recursively
			Ensemble.recursiveClosestPair (data);
			// updates the elapsed-time and #operations
			etime += Ensemble.etime;
			operations += ( (double) Ensemble.operations );
		}

		// computes the averages and return statistics
		double avg_etime = (etime / reps);
		double avg_operations = (operations / reps);
		double [] statistics = {avg_etime, avg_operations};
		return statistics;
	}


	private static double [][] experiments ()
	// obtains the elapsed-time and #operations with respect to size
	{
		int runs = 14;
		int size = 16;
		double [][] statistics = new double[3][runs];
		double [] sizes = statistics[0];
		double [] etimes = statistics[1];
		double [] operations = statistics[2];
		for (int i = 0; i != runs; ++i)
		{
			// repeats the numeric experiment
			double [] stat = replicate (size);
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


	private static void export ()
	// exports the elapsed-time and #operations with respect to size
	{
		try
		{
			String file = ("time.dat");
			PrintWriter out = new PrintWriter(file);
			// conducts the experiments
			double [][] stats = experiments();
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
