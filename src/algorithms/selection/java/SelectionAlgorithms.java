/*
 * Algorithms and Programming II                          April 05, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of selection algorithms in Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */


public class SelectionAlgorithms
{

	public static void main (String[] args)
	{
		FindsKthLargest();	// uses the implementation
		testFindKthLargest();	// validates the implementation
		return;
	}


	// examples:
	public static void FindsKthLargest()
	{
		int list [] = ulist();			// unsorted list
		int sorted [] = new int [list.length];	// sorted list

		// sorts original list
		for (int k = 0; k != list.length; ++k)
			sorted[k] = FindKthLargest(k + 1, list);


		// prints the original list
		System.out.printf("\n\nOriginal List:\n\n");
		for (int i = 0; i != list.length; ++i)
			System.out.printf("%4d", list[i]);
		System.out.printf("\n\n");


		// prints the sorted list
		System.out.printf("\n\nSorted List:\n\n");
		for (int i = 0; i != sorted.length; ++i)
			System.out.printf("%4d", sorted[i]);
		System.out.printf("\n\n");

		return;
	}


	private static int [] ulist ()
	/*
	 * Synopsis:
	 * Returns a list of unsorted integers.
	 *
	 */
	{
		int x[] = {
			16,  7, 10,  1, 5, 11,  3,  8,
			14,  4,  2, 12, 6, 13,  9, 15
		};

		return x;
	}


	private static int [] descending()
	/*
	 * Synopsis:
	 * Returns a descending list of integers.
	 *
	 */
	{
		int x[] = new int [16];
		for (int i = 0; i != 16; ++i)
			x[i] = (16 - i);

		return x;
	}


	// interfaces:
	public static int FindKthLargest(int K, int [] list)
	/*
	 * Synopsis:
	 * Possible implementation of the FindKthLargest Method.
	 *
	 * Inputs
	 * K		integer indicating 1st, 2nd, 3rd, or kth largest
	 * list		array of (presumed) unsorted integers
	 *
	 * Output
	 * KthLargest	the kth largest element in the list
	 *
	 */
	{
		int max = list[0];		// caters init warning
		int N = list.length;		// gets the list size
		int ls [] = copy (list);	// creates a shallow copy
		for (int i = 0; i != K; ++i)
		{
			// swaps (current) max with last (current) element
			max = swap ( N, ls, argmax(N, ls) );
			// decrements size to exclude max on the next pass
			--N;
		}

		return max; // returns the kth largest: ls[list.length - K]
	}


	// implementations:
	private static int [] copy (int [] list)
	/*
	 * Synopsis:
	 * Creates a shallow copy of a list.
	 *
	 */
	{
		int ls [] = new int [list.length];
		for (int i = 0; i != list.length; ++i)
			ls[i] = list[i];

		return ls;
	}


	private static int argmax (int N, int [] list)
	/*
	 * Synopsis:
	 * Possible implementation of argmax function in Java.
	 * Returns the index of the element of the list having the maximum
	 * value.
	 *
	 * Input
	 * N		number of elements in list
	 * list		array of integers
	 *
	 * Output
	 * idx		index of the element having the maximum value
	 *
	 */
	{
		int idx = 0;
		int max = list[idx];
		for (int i = 1; i != N; ++i)
		{
			if (list[i] > max)
			{
				idx = i;
				max = list[idx];
			}

		}

		return idx;
	}


	private static int swap (int N, int [] ls, int i)
	/*
	 * Synopsis:
	 * Swaps (or interchanges) the ith element with the last one.
	 *
	 * Input
	 * N		list size
	 * ls		list, array of integers
	 * i		the index of the ith element of the list
	 *
	 * Output
	 * list		the list after the interchange of elements
	 *
	 */
	{
		int e = N - 1;	// gets the index of the end value
		int end = ls[e];// creates placeholder for the end value
		int ith = ls[i];// creates placeholder for the ith value
		ls[e] = ls[i];	// writes the ith value at the end of list
		ls[i] = end;	// writes the end value on the ith position
		return ith;
	}


	private static void consistency (int [] x, int [] y)
	// complains if the lists have a different number of elements
	{
		if (x.length != y.length)
		{
			throw new RuntimeException(
				"lists must have the same length"
			);
		}
	}


	private static boolean equality (int [] x, int [] y)
	/*
	 * Synopsis:
	 * Returns true if the lists are identical, false otherwise.
	 *
	 */
	{
		// checks that the list have the same number of elements
		consistency (x, y);

		int diff = 0;
		// computes differences elementwise
		for (int i = 0; i != x.length; ++i)
			diff += (x[i] - y[i]);

		if (diff == 0)
			return true;
		else
		return false;
	}


	// tests:
	private static void testFindKthLargest()
	/*
	 * Synopsis:
	 * Tests the implementation of FindKthLargest().
	 * The test uses FindKthLargest() to sort a list of integers in
	 * descending order and compares it against the expected list.
	 * The implementation passes the test if the lists are identical.
	 *
	 */
	{
		int list []   = ulist();		// unsorted list
		int sorted [] = new int [list.length];	// sorted list
		int arange [] = descending ();		// expected list


		// sorts list in descending order
		for (int k = 0; k != list.length; ++k)
			sorted[k] = FindKthLargest(k + 1, list);


		// tests the implementation
		System.out.printf("\ntest::FindKthLargest(): ");
		if ( equality(sorted, arange) )
			System.out.printf("pass\n\n");
		else
			System.out.printf("FAIL\n\n");

		return;
	}
}


/*
 * COMMENTS:
 * The FindKthLargest() method creates a shallow copy of the list because
 * the method should not change the original ordering of the list since
 * its only objective is to return the kth largest value.
 *
 * A shallow copy means that we create another list (or object) having the
 * same numeric values. What a method does on the copy does not affect the
 * original.
 *
 */
