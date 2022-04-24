/*
 * Algorithms and Programming II                          February 18, 2022
 * IST 2089
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
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [1] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 * [2] www.javatpoint.com/how-to-return-an-array-in-java
 * [3] www.javatpoint.com/throw-keyword
 * [4] www.javatpoint.com/post/java-random
 *
 */

import java.util.Random;

class Aggregates
{

	public static void main (String[] args)
	{

		System.out.println("\nAggregate Functions:\n");

		double xi = 64;	// initial array value
		double xf = 1;	// final array value
		int numel = 64;	// number of elements in the array
		double x[] = linspace (xi, xf, numel);

		// finds the min, max, sum, median, mean, and stdev
		System.out.printf("min: %20.15f %21d\n", min(x), 1);
		System.out.printf("max: %20.15f %21d\n", max(x), 64);
		System.out.printf("sum: %20.15f %21d\n", sum(x), 32*(65));
		System.out.printf("mid: %20.15f %21.2f\n", median(x),32.5);
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


	public static double median (double [] list)
	/*
	 * Synopsis
	 * Returns the middle value of a list of doubles.
	 *
	 * Input:
	 * list		array of doubles
	 *
	 * Output:
	 * mid		middle value
	 *
	 */
	{

		int b = 0;		// begin
		int e = list.length;	// end, non-inclusive
		int M = (b + e) / 2;	// middle

		// obtains the middle value
		double mid;
		if (list.length % 2 == 0)
			mid = ( FindKthLargest(M, list) +
			        FindKthLargest(M - 1, list) ) / 2.0;
		else
			mid = FindKthLargest(M, list);

		return mid;
	}


	// implementations:
	private static double FindKthLargest (int K, double [] list)
	/*
	 * Synopsis:
	 * Possible implementation of the Find Kth Largest Algorithm.
	 *
	 * Inputs
	 * K		integer indicating 0th, 1st, 2nd, or kth largest
	 * list		array of (presumed) unsorted elements
	 *
	 * Ouput
	 * list[p]	returns the pivot element, the kth largest element
	 * 		counting from zero.
	 *
	 */
	{
		int p;			// pivot
		int b = 0;		// begin
		int e = list.length;	// end, non-inclusive
		/* creates a shallow copy of the list */
		double [] ls = copy(list);

		do
		// partitions the list until it reaches the middle element
		{
			p = partition (ls, b, e);

			if (p > K)
				e = p;		// removes [p, e) elements
			else
				b = (p + 1);	// removes [b, p] elements

		} while (p != K);

		return ls[p];
	}


	private static int partition (double [] list, int b, int e)
	/*
	 * Synopsis:
	 * Partitions a list of integers about its middle element after
	 * conducting N - 1 comparisons, where N is the number of elements
	 * in the list.
	 *
	 * Inputs
	 * list		list of (presumed) unsorted elements
	 * b		beginning of the list
	 * e		end of the list, non-inclusive
	 *
	 * Output
	 * list		partitioned list
	 *
	 * Return
	 * p		pivot location (or index)
	 *
	 */
	{
		int m = (b + e) / 2;			// middle
		int p = ( (m - 1) > b )? (m - 1) : b;	// pivot location

		// swaps smaller elements with large ones on the left half
		p = left (list, b, e, p);

		// swaps larger elements with small ones on the right half
		p = right (list, b, e, p);

		return p;
	}


	private static int left (double [] list, int b, int e, int p)
	/*
	 * Synopsis:
	 * Swaps smaller elements with larger ones in the left sublist
	 * (first half) in the range [b, m) so that elements to the
	 * left of the pivot are larger than the pivot element, and those
	 * to the right of the pivot are smaller. The algorithm completes
	 * its task in N / 2 - 1 comparisons, where N is the total number
	 * of elements in the list.
	 *
	 * Inputs
	 * list		list of (presumed) unsorted elements
	 * m		middle index, non-inclusive
	 * e		end index, non-inclusive
	 * p		pivot index
	 *
	 * Output
	 * p		returns the pivot location after the swaps
	 *
	 */
	{
		int m = (b + e) / 2;			// middle
		double pivot = list[p];			// pivot value

		int n = 0;				// comparisons
		int i = ( (p - 1) > b )? (p - 1) : b;	// index
		int l = b;				// list lower limit
		// swaps elements smaller than pivot in the range [b, m)
		while ( n < ( (m - b) - 1) )
		{
			if (list[i] < pivot)
			{
				// swaps smaller element with pivot element
				swap (list, i--, p--);
			}
			else
				swap (list, i, l++);
			++n;
		}

		return p;

	}


	private static int right (double [] list, int b, int e, int p)
	/*
	 * Synopsis:
	 * Swaps larger elements with small ones in the right sublist
	 * (second half) in the range [m, e) so that elements to the
	 * left of the pivot are larger than the pivot element, and those
	 * to the right of the pivot are smaller. The algorithm does the
	 * remaining N / 2 comparisons for a total of N - 1 comparisons,
	 * where N is the total number of elements in the list.
	 *
	 * Inputs
	 * list		list of (presumed) unsorted elements
	 * b		begin index
	 * e		end index, non-inclusive
	 * p		pivot index
	 *
	 * Output
	 * p		returns the pivot location after the swaps
	 *
	 */
	{
		int m = (b + e) / 2;			// middle
		double pivot = list[p];			// pivot value
		int r = remove (list, m, e, p);		// removes small

		/* right sublist limits [l, u] */
		int l = ( (p + 1) < e )? (p + 1) : p;	// lower limit
		int u = e - (r + 1);			// upper limit

		// sets the current number of comparisons
		int n = ( (m - b) - 1);
		// sets the total number of comparisons (N - 1)
		int M = ( ( (e - b) - 1 ) > 0 )? ( (e - b) - 1 ) : 0;
		while (n < M)
		{
			if (list[l] > pivot)
				swap (list, l++, p++);	// swaps larger
			else
				swap(list, l, u--);	// swaps smaller
			++n;
		}

		return p;
	}


	private static int remove (double [] list, int m, int e, int p)
	/*
	 * Synopsis:
	 * Effectively removes smaller elements from the left sublist
	 * (first half [b, m) of the list) by swapping elements from
	 * the end of the list.
	 *
	 * Inputs
	 * list		list of (presumed) unsorted elements
	 * m		middle index
	 * e		end index, non-inclusive
	 * p		pivot index
	 *
	 * Output
	 * n		returns the number of removed elements
	 *
	 */
	{
		int n = 0;
		int R = ( m - (p + 1) );		// #removals
		int u = ( (m - 1) >= p )? (m - 1) : p;	// upper limit
		// moves smaller elements in [p + 1, m) to the end
		while (n < R)
		{
			// swaps smaller elements with those at the end
			swap( list, u--, e - (n + 1) );
			++n;
		}

		return n;	// returns number of removed elements
	}


	private static void swap (double [] list, int i, int j)
	// swaps the (smaller) ith element with the (larger) jth element
	{
		double smaller = list[i];
		list[i] = list[j];
		list[j] = smaller;
		return;
	}


        private static double [] copy (double [] list)
	/*
	 * Synopsis:
	 * Creates a shallow copy of a list.
	 *
	 */
	{
		double ls [] = new double [list.length];
		for (int i = 0; i != list.length; ++i)
			ls[i] = list[i];

		return ls;
	}


        private static void print (double [] list)
	// prints the elements of a list to the console
	{
		int i;
		for (i = 0; i != list.length - 1; ++i)
			System.out.printf("%4.1f ", list[i]);
		System.out.printf("%4.1f\n", list[i]);

		return;
	}


	// tests:
	private static void isAscending(double [] list)
	// complains if the list is not sorted in ascending order
	{
		for (int i = 0; i != (list.length - 1); ++i)
		{
			if (list[i] > list[i + 1])
				throw new RuntimeException(
					"list is not in ascending order"
				);
		}
	}


	private static void testMedian()
	// finds the median of a list of random numbers for testing
	{
		int size = 255;
		double [] list = new double [size];
		double [] sort = new double [size];
		Random rand = new Random();

		// fills the list with random values in the range [0, 1024)
		for (int i = 0; i != size; ++i)
			list[i] = ( (double) rand.nextInt(1024) );

		for (int k = 0; k != size; ++k)
			sort[k] = FindKthLargest(size - (k + 1), list);

		isAscending (sort);	// complains if list is not sorted


		/* computes median from the sorted list */
		double middle;
		int mid = size / 2;
		if (size % 2 == 1)
			middle = sort[mid];
		else
			middle = (sort[mid] + sort[mid - 1]) / 2.0;


		// prints the median on the console
		System.out.printf("median: %f\n", middle);
		System.out.printf("median: %f\n", median(list));


		// informs the user about the test results (pass or fail)
		System.out.printf("median.test(): ");
		if ( Math.abs(median(list) - middle) > 1.0e-12 )
			System.out.printf("FAIL\n");
		else
			System.out.printf("pass\n");

		return;
	}

}
