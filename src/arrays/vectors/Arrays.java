/*
 * Algorithms and Programming II                          February 10, 2022
 * IST 2048
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Code shows how to create arrays in Java.
 *
 *
 * Sinopsis:
 * Muestra como crear arreglos en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */


public class Arrays
{

	public static void main (String[] args)
	{

		System.out.println("\nArray Examples:\n");



		/* example 1.6-C: */
		/* y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 */
		int size = 8;			// defines the array size
		double dx  = 1.0;		// sets spacing or step
		double x[] = new double [size];	// creates array of doubles
		double y[] = new double [size];	// y = f(x)


		x[0] = 0.0;
		/* assigns values to the array elements */
		for (int i = 0; i != size; ++i)
			x[i] = x[0] + ( (float) i ) * dx;



		/* calculates the elements of the array y = f(x) */
		for (int i = 0; i != size; ++i)
		{
			y[i] = 0.4 * Math.pow(x[i], 4) +
			       3.1 * Math.pow(x[i], 2) - 162.3 * x[i] -
			       80.7;
		}


		System.out.println("Example 1.6-C:");
		/* performs element addressing */
		System.out.printf(
			"references the 4th element of x: %12.6f\n", x[3]
		);
		System.out.printf(
			"references the 4th element of y: %12.6f\n", y[3]
		);
		System.out.println("");



		/* example 1.6-D: */
		/* y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7 */
		size = 8;	// uses the same array size as above
		dx   = 2.0;	// sets spacing or step


		x[0] = 0.0;
		/* assigns values to the array elements */
		for (int i = 0; i != size; ++i)
			x[i] = x[0] + ( (float) i ) * dx;



		/* calculates the elements of the array y = f(x) */
		for (int i = 0; i != size; ++i)
		{
			y[i] = 0.4 * Math.pow(x[i], 4) +
			       3.1 * Math.pow(x[i], 2) - 162.3 * x[i] -
			       80.7;
		}


		System.out.println("Example 1.6-D:");
		/* performs element addressing */
		System.out.printf(
			"references the 7th element of x: %12.6f\n", x[6]
		);
		System.out.printf(
			"references the 7th element of y: %12.6f\n", y[6]
		);
		System.out.println("");


		// tabulates results in Pandas style
		for (int i = 0; i != size; ++i)
		{
			String fstr = ("%2d %5.1f %8.1f\n"); 	// format-string
			System.out.printf(fstr, i, x[i], y[i]);
		}

	}
}



/*
 * COMMENTS:
 * [0] As in Python the index of the array starts at zero and ends in N-1,
 *     where N is the number of elements of the array. Note that the
 *     construction of the for loops uses an inequality which is analogous
 *     to using asymmetric ranges in Python.
 */
