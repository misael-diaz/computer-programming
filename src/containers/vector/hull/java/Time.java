import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Time	// Time Complexity Experiment Class
{

  public static void main (String [] args) throws FileNotFoundException
  {
    Time time = new Time();
    // exports time complexity results
    time.exportBruteForce();
    time.exportDivideAndConquer();
  }


  // repeats the numeric experiment
  private double [] replicateBruteForce (int size)
  {
    int reps = 64;
    double etime = 0;
    double operations = 0;
    for (int i = 0; i != reps; ++i)
    {
      // NOTE:
      // Tries to obtain the convex hull from the dataset. If the dataset produces a
      // "bad hull" it catches the exception thrown by the invoked method to produce a
      // new dataset and tries again until an acceptable convex hull is found.
      int sw = 1;
      Stack data = ConvexHull.genDataSet(size);
      ConvexHull hull = new ConvexHull(data);
      while (sw != 0)
      {
	try
	{
	  hull.bruteForce();
	  sw = 0;
	}
	catch (RejectedHullException e)
	{
	  data = ConvexHull.genDataSet(size);
	  hull = new ConvexHull(data);
	}
      }
      // updates the elapsed-time
      etime += hull.getElapsedTime();
    }

    // computes the averages and return statistics
    double avg_etime = (etime / reps);
    double sz = size;
    // NOTE: the brute force method does the same #operations
    double avg_operations = sz * sz * (sz - 1) / 2;
    double [] statistics = {avg_etime, avg_operations};
    return statistics;
  }


  // obtains the elapsed-time and #operations with respect to size
  private double [][] experimentsBruteForce ()
  {
    int size = 16;
    final int runs = 8;
    double [][] statistics = new double[3][runs];
    double [] sizes = statistics[0];
    double [] etimes = statistics[1];
    double [] operations = statistics[2];
    for (int i = 0; i != runs; ++i)
    {
      // repeats the numeric experiment
      double [] stat = replicateBruteForce(size);
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


  // exports the elapsed-time and #operations with respect to size
  private void exportBruteForce () throws FileNotFoundException
  {
    final String file = ("timeBruteForce.dat");
    PrintWriter out = new PrintWriter(file);
    // conducts the experiments
    final double [][] stats = experimentsBruteForce();
    // gets the number of experiments (or runs)
    final int runs = stats[1].length;
    for (int i = 0; i != runs; ++i)
    {
      // gets size, elapsed-time, and #operations
      final double size  = stats[0][i];
      final double etime = stats[1][i];
      final double opers = stats[2][i];
      // writes data to file in tabulated format
      final String fmt = ("%.16e %.16e %.16e\n");
      out.printf(fmt, size, etime, opers);
    }
    out.close();
  }


  private double replicateDivideAndConquer (int size)
  {
    double etime = 0;
    final int reps = 1024;
    for (int i = 0; i != reps; ++i)
    {
      int sw = 1;
      Stack data = ConvexHull.genDataSet(size);
      ConvexHull hull = new ConvexHull(data);
      while (sw != 0)
      {
	try
	{
	  hull.bruteForce();
	  sw = 0;
	}
	catch (RejectedHullException e)
	{
	  data = ConvexHull.genDataSet(size);
	  hull = new ConvexHull(data);
	}
      }
      hull.convexHull();
      etime += hull.getElapsedTime();
    }

    final double avg_elapsedTimes = ( etime / ( (double) reps ) );
    final double statistics = avg_elapsedTimes;
    return statistics;
  }


  private double [][] experimentsDivideAndConquer ()
  {
    int size = 8;
    final int runs = 8;
    double [][] statistics = new double[2][runs];
    double [] sizes = statistics[0];
    double [] elapsedTimes = statistics[1];
    for (int i = 0; i != runs; ++i)
    {
      double elapsedTime = replicateDivideAndConquer(size);
      sizes[i] = ( (double) size );
      elapsedTimes[i] = elapsedTime;
      size *= 2;
    }

    return statistics;
  }


  private void exportDivideAndConquer () throws FileNotFoundException
  {
    final String file = ("timeDivideAndConquer.dat");
    PrintWriter out = new PrintWriter(file);
    final double [][] stats = experimentsDivideAndConquer();
    final int runs = stats[1].length;
    for (int i = 0; i != runs; ++i)
    {
      final double size = stats[0][i];
      final double elapsedTime = stats[1][i];
      final String fmt = ("%.16e %.16e\n");
      out.printf(fmt, size, elapsedTime);
    }
    out.close();
  }
}


/*

Algorithms and Complexity                               October 21, 2022
IST 4310
Prof. M. Diaz-Maldonado

Synopsis:
Conducts Time Complexity Experiments of the Convex Hull problem.

Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
[1] JJ McConnell, Analysis of Algorithms, 2nd edition

*/
