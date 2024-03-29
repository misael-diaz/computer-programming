/*
 * Algorithms and Programming II                          February 10, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Illustrates elementwise (or elemental) Array operations in Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] Java Arrays: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html
 * [1] A Gilat, MATLAB: An Introduction with Applications, 6th edition.
 * [2] R Johansson, Numerical Python: Scientific Computing and Data
 *     Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
 * [3] A Koenig and B Moo, Accelerated C++: Practical Programming by Example.
 *
 */

import java.util.Arrays;

public class Elemental
{

  public static void main (String[] args)
  {
    example16C();	// example 1.6-C (based on problems from reference [1])
    example16D();	// example 1.6-D (based on problems from reference [1])
    example16E();	// example 1.6-E (based on problems from reference [1])
    sumSquares();	// tabulates sequence and its cumulative sum
  }


  // Example 1.6-C:
  // Computes y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 for x = 0:7
  private static void example16C ()
  {
    int size = 8;			// sets the size of the array `x'
    double xi = 0.0;			// sets the initial value of the array `x'
    double xf = 7.0;			// sets the final value of the array `x'
    double [] y = new double [size];	// allocates the `y' array of doubles


    // copies the numeric sequence in the symmetric range [xi, xf]  into `x':
    double [] x = linspace(xi, xf, size);


    // stores f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 in `y' array:
    // loop-invariant: we have computed `i' elements of the `y' array
    for (int i = 0; i != size; ++i)
    {
      y[i] = f(x[i]);
    }


    // logs on the console the sequence of values (as in the Python example):
    System.out.println("Example 1.6-C: i = [0, 8)");
    log(x);


    // defines the format string for logging on the console
    String fstring = ("references the 4th element of x: %+.15e\n");
    // displays the 4th element on the console (recall zero-starting index)
    System.out.printf(fstring, x[3]);
    fstring = ("references the 4th element of y: %+.15e\n");
    System.out.printf(fstring, y[3]);
    System.out.println();
  }



  // Example 1.6-D:
  // Computes y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 for x = 0:14:2
  // (Note: x = 0, 2, 4, ..., 12, 14).
  private static void example16D ()
  {
    int size = 8;		// sets the size of the array `x'
    double xi = 0.0;		// sets the initial value of the array `x'
    double xf = 14.0;		// sets the final value of the array `x'


    // copies the numeric sequence in the symmetric range [xi, xf]  into `x',
    // note that linspace() handles the computation of the step-size `dx'
    double [] x = linspace(xi, xf, size);


    // maps streamed `x' into array `y' via elemental operations
    double [] y = Arrays.stream(x).map( elem -> f(elem) ).toArray();


    System.out.println("Example 1.6-D: 0, 2, 4, ..., 12, 14");
    log(x);


    // displays the 8th element on the console (recall zero-starting index)
    String fstring = ("references the 8th element of x: %+.15e\n");
    System.out.printf(fstring, x[7]);
    fstring = ("references the 8th element of y: %+.15e\n");
    System.out.printf(fstring, y[7]);
    System.out.println();
  }


  // Example 1.6-E:
  // Computes y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 for x = 14:2:-2
  // (Note: x = 14, 12, 10, ..., 4, 2).
  private static void example16E ()
  {

    int size = 7;			// sets the size of the array `x'
    double xi = 14.0;			// sets the initial value of the array `x'
    double xf =  2.0;			// sets the final value of the array `x'


    // generates the `x' array with values in the symmetric range [xi, xf]
    double [] x = linspace(xi, xf, size);


    // maps streamed `x' into array `y' via elemental operations
    double [] y = Arrays.stream(x).map( elem -> f(elem) ).toArray();


    System.out.println("Example 1.6-E: 14, 12, 10, ..., 4, 2");

    // tabulates results in a Pandas-like format
    log(x, y);
  }


  // displays the sum of squares of the numbers in the arange [1, 9).
  private static void sumSquares ()
  {
    int numel = 8;				// number of sequence elements
    int [][] res = new int [3][numel];		// creates data placeholder
    int [] idx = res[0];			// series index, n
    int [] seq = res[1];			// sequence s(n)
    int [] cumsum = res[2];			// cumulative sum cumsum(n)


    // fills the placeholder array according to storage order in memory to
    // optimize the use of the CPU cache:


    for (int i = 0; i != numel; ++i)
    {
      idx[i] = (i + 1);
    }


    for (int i = 0; i != numel; ++i)
    {
      seq[i] = (idx[i] * idx[i]);
    }


    int sum = 0;
    for (int i = 0; i != numel; ++i)
    {
      sum += seq[i];
      cumsum[i] = sum;
    }


    System.out.println("\nSum of Squares:\n");


    // displays Pandas-like table header
    System.out.printf(" %3d %3d %4d\n", 0, 1, 2);
    // tabulates the results in Pandas style
    for (int i = 0; i != numel; ++i)
    {
      String fstr = ("%1d %2d %3d %4d\n");
      System.out.printf(fstr, i, idx[i], seq[i], cumsum[i]);
    }
  }


  // logs the values stored in the array `x' (as in the Python examples)
  private static void log (double [] x)
  {
    // Note:
    // Java Arrays are objects which contain useful data such as the number
    // of stored elements (or equivalently, the array size).
    int size = x.length;		// gets the array size
    for (int i = 0; i != size; ++i)
    {
      int last = (size - 1);

      if (i == 0)
      {
	System.out.printf("[%.0f", x[i]);
      }
      else if (i == last)
      {
	System.out.printf(" %.0f]\n", x[i]);
      }
      else
      {
	System.out.printf(" %.0f", x[i]);
      }
    }
  }


  // overloads the log() method to display the results of exercise 1.6-E
  private static void log (double [] x, double [] y)
  {
    // displays a Pandas-like header for exercise 1.6-E
    System.out.printf("\n%7d %8d\n", 0, 1);
    for (int i = 0; i != x.length; ++i)
    {
      String fstring = ("%1d %5.1f %8.1f\n");
      System.out.printf(fstring, i, x[i], y[i]);
    }
  }


  // implements a Python-like linspace() method
  private static double [] linspace (double xi, double xf, int numel)
  {

    // user input checks:


    if (numel <= 0)	// complains if the user supplies an invalid array size
    {
      String err = "linspace(): expects sizes greater than zero";
      throw new IllegalArgumentException(err);
    }


    // allocates the array
    double [] x = new double [numel];
    // computes the step-size
    double dx = (xf - xi) / ( (double) (numel - 1) );


    // loop-invariant: thus far we have computed `i' elements of the `x' array
    for (int i = 0; i != numel; ++i)
    {
      x[i] = xi + ( (double) i ) * dx;
    }

    return x;
  }


  // implements f(x) as a private static method
  private static double f(double x)
  {
    double y = 0.4 * Math.pow(x, 4) + 3.1 * Math.pow(x, 2) - 162.3 * x - 80.7;
    return y;
  }
}


/*
 * COMMENTS:
 * As in Python the index of the array starts at zero and ends in N - 1, where N is the
 * number of elements of the array. Note that the for-loops are constructed with
 * inequality conditional statements to simulate Python's arange() method. This type
 * of for-loop construction abounds in C++ as well (see reference [3]).
 *
 */
