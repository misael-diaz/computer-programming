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
	}


	private static void example16C ()
	/*

	Example 1.6-C:
	Computes y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 for x = 0:7.

	*/
	{

		int size = 8;			// sets the size of the array `x'
		double xi = 0.0;		// sets the initial value of the array `x'
		double xf = 7.0;		// sets the final value of the array `x'
		double [] y = new double [size];// allocates the `y' array of doubles


		// copies the numeric sequence in the symmetric range [xi, xf]  into `x':
		double [] x = linspace(xi, xf, size);


		// stores f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 in `y' array:
		for (int i = 0; i != size; ++i)
		// invariant: we have computed `i' elements of the `y' array
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


	private static void example16D ()
	/*

	Example 1.6-D:
	Computes y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 for x = 0:14:2
	(Note: x = 0, 2, 4, ..., 12, 14).

	*/
	{

		int size = 8;			// sets the size of the array `x'
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


	private static void log (double [] x)
	// logs the values stored in the array `x' (as in the Python examples)
	{

		/*

		Note:
		Java Arrays are objects which contain useful data such as the number
		of stored elements (or equivalently, the array size).

		*/

		// gets the array size
		int size = x.length;
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


	private static double [] linspace (double xi, double xf, int numel)
	// implements a Python-like linspace() method
	{

		// user input checks:


		if (numel <= 0)
		// complains if the user supplies an invalid array size
		{
			String err = "linspace(): expects sizes greater than zero";
			throw new IllegalArgumentException(err);
		}


		// loop-invariant: thus far we have computed `i' elements of the `x' array


		// allocates the array
		double [] x = new double [numel];
		// computes the step-size
		double dx = (xf - xi) / ( (double) (numel - 1) );
		for (int i = 0; i != numel; ++i)
		{
			x[i] = xi + ( (double) i ) * dx;
		}

		return x;
	}


	private static double f(double x)
	// implements f(x) as a private static method
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
