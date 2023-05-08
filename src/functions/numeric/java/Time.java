/*
 * Algorithms and Complexity                               October 21, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Conducts Time Complexity Experiments of the Fibonacci problem.
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

  public static void main (String [] args) // exports time complexity results
  {
    exportIterative();
    exportRecursive();
  }


  private static double [] replicateIterative (int size) // repeats the numeric experiment
  {
    int reps = 2048;
    double etime = 0;
    double operations = 0;
    for (int i = 0; i != reps; ++i)
      etime += Fibonacci.iterative(size);

    // computes the averages and return statistics
    double avg_etime = (etime / reps);
    double sz = size;
    // computes the number of iterations
    double avg_operations = (sz - 2) + 1;
    double [] statistics = {avg_etime, avg_operations};
    return statistics;
  }


  private static double [][] experimentsIterative ()
  {
    // obtains the elapsed-time and number of operations with respect to size
    int runs = 20;
    int size = 16;
    double [][] statistics = new double[3][runs];
    double [] sizes = statistics[0];
    double [] etimes = statistics[1];
    double [] operations = statistics[2];
    for (int i = 0; i != runs; ++i)
    {
      // repeats the numeric experiment
      double [] stat = replicateIterative (size);
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


  private static void exportIterative ()
  {
    // exports the elapsed-time and number of operations with respect to size
    try
    {
      String file = ("timeIterative.dat");
      PrintWriter out = new PrintWriter(file);
      // conducts the experiments
      double [][] stats = experimentsIterative();
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


  private static double [] replicateRecursive (int size) // repeats the numeric experiment
  {
    int reps = 256;
    double etime = 0;
    double operations = 0;
    for (int i = 0; i != reps; ++i)
    {
      etime += Fibonacci.recursive(size);
      operations += ( (double) Fibonacci.recursions);
    }

    // computes the averages and return statistics
    double avg_etime = (etime / reps);
    double sz = size;
    // computes the number of iterations
    double avg_operations = (operations / reps);
    double [] statistics = {avg_etime, avg_operations};
    return statistics;
  }


  private static double [][] experimentsRecursive ()
  {
    // obtains the elapsed-time and number of operations with respect to size
    int runs = 4;
    int size = 16;
    double [][] statistics = new double[3][runs];
    double [] sizes = statistics[0];
    double [] etimes = statistics[1];
    double [] operations = statistics[2];
    for (int i = 0; i != runs; ++i)
    {
      // repeats the numeric experiment
      double [] stat = replicateRecursive (size);
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


  private static void exportRecursive ()
  {
    // exports the elapsed-time and number of operations with respect to size
    try
    {
      String file = ("timeRecursive.dat");
      PrintWriter out = new PrintWriter(file);
      // conducts the experiments
      double [][] stats = experimentsRecursive();
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
