/*
 * Algorithms and Programming II                             April 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible recursive implementation of the factorial in Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition, 2007.
 *
 */

import java.util.Random;

class Factorial
{
	public static void main (String [] args)
	{
		// selects an integer in the asymmetric range [1, 16)
		int min = 1, max = 16;
		Random rand = new Random();
		int N = min + rand.nextInt(max - min);

		// defines format string
		String fmt = ("fact(%d): %d\n");
		// obtains the factorial iteratively and recursively
		System.out.printf(fmt, N, iterativeFactorial(N) );
		System.out.printf(fmt, N, recursiveFactorial(N) );
		return;
	}


	private static int iterativeFactorial (int N)
	// iterative implementation of the factorial
	{
		int p = 1;	// product
		for (int i = N; i > 1; --i)
			p *= i;

		return p;
	}


	private static int recursiveFactorial (int N)
	// recursive implementation of the factorial
	{
		int p;					// product
		if (N == 1)
			return 1;			// direct solution
		else
			p = recursiveFactorial (N - 1);	// divides

		return (p * N);				// combines
	}
}


/*
 * COMMENTS:
 * A more robust version of the code should validate the user input, it
 * has not been done here for simplicity.
 *
 * Note that the random numbers have been restricted to a range to avert
 * potential overflows. If the input is large enough its factorial might
 * not fit in a signed 32-bit integer (type int) leading to an overflow.
 * Recall that the largest signed 32-bit integer is equal to 2^31 - 1.
 *
 */
