/*
 * Algorithms and Complexity                           December 11, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines the Time Complexity Class for analyzing the algorithms that
 * solve the closest pair problem.
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

public class TimeComplexity
{

	int runs;	// number of time complexity experiments


	TimeComplexity (int runs)
	// constructs object from the number of experiments (or runs)
	{
		this.runs = runs;
	}


	/* method(s) */

	public void exportTimeComplexity_DivideAndConquer1D ()
	throws ImplementErrorException, FileNotFoundException
	/*

	Synopsis:
	Exports the time complexity results of the 1D Divide and Conquer
	Algorithm that solves the closest pair problem to a plain text
	data file.

	Inputs:
	None

	Outputs:
	None

	*/
	{
		// creates a new output stream
		String filename = "timeDivideAndConquer1D.dat";
		PrintWriter out = new PrintWriter(filename);

		// gets the time complexity data
		double [][] stats = this.timeDivideAndConquer1D();
		double [] sizes = stats[0];
		double [] times = stats[1];
		double [] opers = stats[2];

		for (int i = 0; i != this.runs; ++i)
		{
			String fmt = "%.15e %.15e %.15e\n";
			// writes the data in exponential notation
			out.printf(fmt, sizes[i], times[i], opers[i]);
		}

		// closes the output stream
		out.close();
	}


	public void exportTimeComplexity_DivideAndConquer2D ()
	throws ImplementErrorException, FileNotFoundException
	/*

	Synopsis:
	Exports the time complexity results of the 2D Divide and Conquer
	Algorithm that solves the closest pair problem to a plain text
	data file.

	Inputs:
	None

	Outputs:
	None

	*/
	{
		// creates a new output stream
		String filename = "timeDivideAndConquer2D.dat";
		PrintWriter out = new PrintWriter(filename);

		// gets the time complexity data
		double [][] stats = this.timeDivideAndConquer2D();
		double [] sizes = stats[0];
		double [] times = stats[1];
		double [] opers = stats[2];

		for (int i = 0; i != this.runs; ++i)
		{
			String fmt = "%.15e %.15e %.15e\n";
			// writes the data in exponential notation
			out.printf(fmt, sizes[i], times[i], opers[i]);
		}

		// closes the output stream
		out.close();
	}


	/* implementation(s) */

	private double [][] timeDivideAndConquer1D () throws ImplementErrorException
	/*

	Synopsis:
	Times the 1D Divide and Conquer Algorithm that finds the closest
	pair in an ensmble of particles (or just points).

	Input:
	None

	Output:
	statistics	a second-rank array that stores the ensemble
			sizes, the average elapsed-times, and the
			average number of operations used by the
			Divide and Conquer Algorithm to find the
			closest pair.

	*/
	{
		// allocates the statistics array
		double [][] statistics = new double [3][this.runs];
		// defines meaningful references for the rows of the array
		double [] sizes = statistics[0];
		double [] avgElapsedTimes = statistics[1];
		double [] avgNumOperations = statistics[2];

		int size = 16;	// defines the initial ensemble size
		int reps = 256;	// defines the number of repetitions
		for (int i = 0; i != this.runs; ++i)
		{
			double etime = 0;
			double opers = 0;
			for (int j = 0; j != reps; ++j)
			// accumulates the elapsed-time and the #operations
			{
				// creates a new ensemble of requested size
				Ensemble ens = new Ensemble(size);
				// times the Divide and Conquer Algorithm
				ens.recursive1D();
				// gets the elapsed time (nanoseconds)
				etime += ens.getElapsedTime();
				// gets the #operations
				opers += ens.getOperations();
			}

			// stores the ensemble size
			sizes[i] = size;
			// stores the average elapsed-time
			avgElapsedTimes[i] = (etime / reps);
			// stores the average #operations
			avgNumOperations[i] = (opers / reps);

			// doubles the ensemble size for the next run
			size *= 2;
		}

		return statistics;
	}


	private double [][] timeDivideAndConquer2D () throws ImplementErrorException
	/*

	Synopsis:
	Times the 2D Divide and Conquer Algorithm that finds the closest
	pair in an ensmble of particles (or just points).

	Input:
	None

	Output:
	statistics	a second-rank array that stores the ensemble
			sizes, the average elapsed-times, and the
			average number of operations used by the
			Divide and Conquer Algorithm to find the
			closest pair.

	*/
	{
		// allocates the statistics array
		double [][] statistics = new double [3][this.runs];
		// defines meaningful references for the rows of the array
		double [] sizes = statistics[0];
		double [] avgElapsedTimes = statistics[1];
		double [] avgNumOperations = statistics[2];

		int size = 16;	// defines the initial ensemble size
		int reps = 256;	// defines the number of repetitions
		for (int i = 0; i != this.runs; ++i)
		{
			double etime = 0;
			double opers = 0;
			for (int j = 0; j != reps; ++j)
			// accumulates the elapsed-time and the #operations
			{
				// creates a new ensemble of requested size
				Ensemble ens = new Ensemble(size);
				// times the Divide and Conquer Algorithm
				ens.recursive2D();
				// gets the elapsed time (nanoseconds)
				etime += ens.getElapsedTime();
				// gets the #operations
				opers += ens.getOperations();
			}

			// stores the ensemble size
			sizes[i] = size;
			// stores the average elapsed-time
			avgElapsedTimes[i] = (etime / reps);
			// stores the average #operations
			avgNumOperations[i] = (opers / reps);

			// doubles the ensemble size for the next run
			size *= 2;
		}

		return statistics;
	}


	public static void main (String [] args)
	throws ImplementErrorException, FileNotFoundException
	{
		TimeComplexity t = new TimeComplexity(10);
		t.exportTimeComplexity_DivideAndConquer1D();
	}
}
