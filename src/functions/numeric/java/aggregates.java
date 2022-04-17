/*
 * Algorithms and Programming II                          February 18, 2022
 * IST 2048
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of some aggregate functions in Java.
 *
 *
 * Sinopsis:
 * Posible implementacion de algunas funciones agregadas en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] www.javatpoint.com/how-to-return-an-array-in-java
 * [1] www.javatpoint.com/throw-keyword
 */


class Aggregates
{

	public static void main (String[] args)
	{

		System.out.println("\nAggregate Functions:\n");

		double xi = 64;	// initial array value
		double xf = 1;	// final array value
		int numel = 64;	// number of elements in the array
		double x[] = linspace (xi, xf, numel);

		// finds the min, max, mean, and stdev of the array values
		System.out.printf("min: %20.15f %21d\n", min(x), 1);
		System.out.printf("max: %20.15f %21d\n", max(x), 64);
		System.out.printf("sum: %20.15f %21d\n", sum(x), 32*(65));
		System.out.printf("avg: %20.15f %21.2f\n", mean(x), 32.5);
		System.out.printf("std: %20.15f %21.15f\n\n",
			std(x), 18.618986725025255);

	}


	public static double[] linspace (double xi, double xf, int numel)
	/*
	 * Synopsis:
	 * Implements the linspace function.
	 *
	 * Inputs:
	 * xi		initial value of the array
	 * xf		final value of the array
	 * numel	number of elements of the array
	 *
	 * Output:
	 * x		array [xi, xf] with equally spaced elements
	 *
	 */
	{

		// caters invalid number of elements
		if (numel <= 1)
		{
			throw new RuntimeException(
				"number of elements must be greater " +
				"than one"
			);
		}

		double dx  = (xf - xi) / ( (double) (numel - 1) );
		double x[] = new double [numel];

		x[0] = xi;
		for (int i = 1; i != numel; ++i)
			x[i] = x[i-1] + dx;

		return x;
	}


	public static double min (double [] x)
	/*
	 * Synopsis:
	 * Implements the min function.
	 *
	 * Input:
	 * x		array of doubles
	 *
	 * Output:
	 * small	smallest value in the array of doubles
	 *
	 */
	{


		int numel = x.length;

		double small = x[0];
		for (int i = 1; i != numel; ++i)
		{
			if (x[i] < small)
				small = x[i];
		}

		return small;
	}


	public static double max (double [] x)
	/*
	 * Synopsis:
	 * Implements the max function.
	 *
	 * Input:
	 * x		array of doubles
	 *
	 * Output:
	 * small	largest value in the array of doubles
	 *
	 */
	{


		int numel = x.length;

		double large = x[0];
		for (int i = 1; i != numel; ++i)
		{
			if (x[i] > large)
				large = x[i];
		}

		return large;
	}


	public static double sum (double [] x)
	/*
	 * Synopsis:
	 * Implements the sum function.
	 *
	 * Input:
	 * x		array of doubles
	 *
	 * Output:
	 * small	sums the elements in the array of doubles
	 *
	 */
	{


		int numel = x.length;

		double sum = 0;
		for (int i = 0; i != numel; ++i)
			sum += x[i];

		return sum;
	}


	public static double mean (double [] x)
	/*
	 * Synopsis:
	 * Implements the mean function.
	 *
	 * Input:
	 * x		array of doubles
	 *
	 * Output:
	 * avg		average of the array of doubles
	 *
	 */
	{

		int numel = x.length;

		return ( sum(x) / ( (double)  numel ) );
	}


	public static double std (double [] x)
	/*
	 * Synopsis:
	 * Implements the standard deviation function.
	 *
	 * Input:
	 * x		array of doubles
	 *
	 * Output:
	 * stdev	standard deviation of the array of doubles
	 *
	 */
	{


		int numel = x.length;
		double avg = mean(x);

		double stdev = 0;
		for (int i = 0; i != numel; ++i)
			stdev += (x[i] - avg) * (x[i] - avg);

		stdev /= ( (double) (numel - 1) );
		stdev = Math.sqrt(stdev);

		return stdev;
	}

}
