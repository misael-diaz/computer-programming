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
 *
 * References:
 * [0] www.geeksforgeeks.org/find-max-min-value-array-primitives-using-java
 * [1] www.delftstack.com/howto/java/find-sum-of-array-in-java
 * [2] www.geeksforgeeks.org/arrays-stream-method-in-java
 *
 */

import java.util.Arrays;		// for casting array into list
import java.util.Collections;		// provides min, max methods
import static java.lang.Math.pow;	// provides the power (pow) method

class Vectors
{

  public static void main (String[] args)
  {

    System.out.printf("\nArray Examples:\n");


    // example 1.6-C:
    // y = f(x) = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7
    int size = 8;						// sets array size
    double x[] = {0, 1, 2, 3, 4, 5, 6, 7};			// initializes array
    double y[] = new double [size];				// prealloctes array


    // calculates the elements of the array y = f(x)
    for (int i = 0; i != size; ++i)
    {
      y[i] = 0.4 * pow(x[i], 4) + 3.1 * pow(x[i], 2) - 162.3 * x[i] - 80.7;
    }


    System.out.printf("Example 1.6-C:");
    // performs element addressing
    System.out.printf("references the 4th element of x: %12.6f\n", x[3]);
    System.out.printf("references the 4th element of y: %12.6f\n", y[3]);
    System.out.printf("\n");


    // finding min and max
    // Arrays.aslist() method does not work with arrays of (primitive) ints
    Integer ary[] = new Integer [100];

    for (int i = 0; i != 100; ++i)
      ary[i] = (i+1);

    int min = Collections.min(Arrays.asList(ary));
    int max = Collections.max(Arrays.asList(ary));

    System.out.printf("min: %d\n", min); 
    System.out.printf("max: %d\n", max); 



    // finds the sum and the mean
    int n[] = new int [100];
    double s[] = new double [100];

    for (int i = 0; i != 100; ++i)
      n[i] = (i+1);

    for (int i = 0; i != 100; ++i)
      s[i] = pow(n[i], 2);

    double sum = 0;
    for (int i = 0; i != 100; ++i)
      sum += pow(n[i], 2);

    System.out.printf(
	"sum: %d   %f   %f\n",
	100 * (100 + 1) * (2 * 100 + 1) / 6,
	sum,
	Arrays.stream(s).sum()
    );

    System.out.printf("avg: %f\n", Arrays.stream(s).average().getAsDouble());
    System.out.printf("\n");

  }
}


/*
 * COMMENTS:
 * [0] As in Python the index of the array starts at zero and ends in N-1,
 *     where N is the number of elements of the array. Note that the
 *     construction of the for loops uses an inequality which is analogous
 *     to using asymmetric ranges in Python.
 */
