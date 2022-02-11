/*
 * Algorithms and Programming II                          February 10, 2022
 * IST 2048
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Code shows how to obtain the total, minimum and maximum value, and
 * the average of the values of an array in Java.
 *
 *
 * Sinopsis:
 * Muestra como obtener el total, el minimo y maximo, y el promedio de los
 * valores de un arreglo en Java.
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

import java.util.Arrays;

class Aggregates
{

	public static void main (String[] args)
	{

		System.out.printf("\nAggregate Examples:\n");


		/* Sum of Squares in the asymmetric range [0, 256) */
		int size = 256;			// defines the array size
		int idx[] = new int [size];	// prealloctes index array
		int ary[] = new int [size];	// prealloctes array


		// stores the index values
		for (int i = 0; i != size; ++i)
			idx[i] = i;


		// stores the squares
		for (int i = 0; i != size; ++i)
			ary[i] = i * i;


		int sum = 0;
		// obtains the sum of the squares via a for-loop
		for (int i = 0; i != size; ++i)
			sum += ary[i];


		/* prints the sum of squares */
		System.out.printf("\nSum of Squares in [0, 256):\n");

		System.out.printf(
			"total (exact)   : %d\n",
			size * (size - 1) * (2 * (size - 1) + 1) / 6
		);

		System.out.printf("total (stream)  : %d\n",
				  Arrays.stream(ary).sum());

		System.out.printf("total (for-loop): %d\n", sum);



		/* obtains minimum, maximum, and average */
		int    min = Arrays.stream(ary).min().getAsInt();
		int    max = Arrays.stream(ary).max().getAsInt();
		double avg = Arrays.stream(ary).average().getAsDouble();


		System.out.printf("\nMinimum, Maximum, and Average:\n");
		System.out.printf("min: %d\n", min);
		System.out.printf("max: %d\n", max);
		System.out.printf("avg: %f\n", avg);
		System.out.printf("\n");

	}
}



/*
 * COMMENTS:
 * [0] Note that the integer type would seldom be adequate to store the
 *     average, use a double instead.
 * [1] The getAsInt() and getAsDouble() methods are required to cast unto
 *     an adequate numeric type.
 *
 */
