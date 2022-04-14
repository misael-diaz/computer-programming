/*
 * Algorithms and Programming II                          April 06, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of (some) sorting algorithms in Java.
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
 * [1] lambdas: www.geeksforgeeks.org/java-lambda-expressions-parameters
 *
 */


interface comparator
{
	// defines abstract comparison function f(x, y)
	public boolean f(int x, int y);
}


public class SortingAlgorithms
{

	// implements comparison (lambda) functions
	static comparator isGreater = (int x, int y) -> {return (x > y);};
	static comparator isSmaller = (int x, int y) -> {return (x < y);};


	public static void main (String[] args)
	{
		/* sorts a list with duplicates */
		sort();
		// sorts a list in ascending order
		ascend();
		// sorts a list in descending order
		descend();

		// tests the implementation
		testInsertionSort();
		return;
	}


	// examples:
	public static void sort()
	{
		int list [] = {6, 2, 4, 7, 1, 3, 8, 5, 1};
		boolean iters = true;

		// prints the original list
		System.out.printf("\n\nInsertion Sort:\n\n");
		System.out.printf("\n\nOriginal List:\n\n");
		print (list);

		// prints the list on every pass while sorting it
		InsertionSort (list, iters);

		// prints the sorted list
		System.out.printf("\n\nSorted List:\n\n");
		print (list);

		return;
	}


	public static void ascend()
	// sorts in ascending order example
	{
		int list [] = {6, 2, 4, 7, 1, 3, 8, 5};

		System.out.printf("\n\nSorts in Ascending Order:\n\n");
		System.out.printf("\n\nOriginal List:\n\n");
		print (list);

		// passes the respective comparison function
		InsertionSort (list, isGreater);

		System.out.printf("\n\nSorted List:\n\n");
		print (list);

		return;
	}


	public static void descend()
	// sorts in descending order example
	{
		int list [] = {6, 2, 4, 7, 1, 3, 8, 5};

		System.out.printf("\n\nSorts in Descending Order:\n\n");
		System.out.printf("\n\nOriginal List:\n\n");
		print (list);

		// passes the respective comparison function
		InsertionSort (list, isSmaller);

		System.out.printf("\n\nSorted List:\n\n");
		print (list);

		return;
	}


	private static int [] small ()
	{
		int list[] = {
			6, 2, 4, 7, 1, 3, 8, 5
		};

		return list;

	}


	private static int [] ulist ()
	/*
	 * Synopsis:
	 * Returns a list of unsorted integers.
	 *
	 */
	{
		int list[] = {
			16,  7, 10,  1,  5, 11,  3,  8,
			14,  4,  2, 12,  6, 13,  9, 15
		};

		return list;
	}


	private static int [] arange ()
	/*
	 * Synopsis:
	 * Returns an asymmetric range of integers.
	 *
	 */
	{
		int size = 16;
		int x[] = new int [size];
		for (int i = 0; i != size; ++i)
			x[i] = (i + 1);

		return x;
	}


	// interfaces:
	public static void InsertionSort (int [] list)
	{
		defaultInsertionSort (list);
	}


	public static void InsertionSort (int [] list, boolean verbose)
	// overloads Insertion Sort with the verbose implementation
	{
		if (verbose)
			verboseInsertionSort (list);
		else
			defaultInsertionSort (list);
	}


	// implementations:
	private static void defaultInsertionSort (int [] list)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 *
	 * Inputs
	 * list		array of (presumed) unsorted integers
	 *
	 * Output
	 * list		the sorted list
	 *
	 */
	{
		int ins, loc;
		for (int i = 1; i != list.length; ++i)
		{
			ins = list[i];		// insertion element
			loc = (i - 1);		// sorted range [0, loc]
			shift (list, ins, loc);	// inserts in right place
		}

		return;
	}


	private static void verboseInsertionSort (int [] list)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 * Verbose version. Prints the elements of the list on each pass.
	 *
	 * Inputs
	 * list		array of (presumed) unsorted integers
	 *
	 * Output
	 * list		the sorted list
	 *
	 */
	{
		int i, ins, loc;
		System.out.printf("\n\nPasses:\n\n");
		for (i = 1; i != list.length; ++i)
		{
			System.out.printf("%4d:\t", i - 1);
			print (list);		// prints the list
			ins = list[i];		// insertion element
			loc = (i - 1);		// sorted range [0, loc]
			shift (list, ins, loc);	// inserts in right place
		}

		System.out.printf("%4d:\t", i - 1);
		print (list);
		return;
	}


	public static void InsertionSort (int [] list, comparator compare)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 *
	 * Inputs
	 * list		array of (presumed) unsorted integers
	 * compare	function that defines comparison of list elements
	 *
	 * Output
	 * list		the sorted list
	 *
	 */
	{
		int ins, loc;
		for (int i = 1; i != list.length; ++i)
		{
			ins = list[i];
			loc = (i - 1);
			shift (list, compare, ins, loc);
		}

		return;
	}


	private static void shift (int [] list, int ins, int loc)
	/*
	 * Synopsis:
	 * Shifts elements in the list whose value are greater than the
	 * insertion element. Returns the list having the new element in
	 * its rigthful location so that the ordered structure of the list
	 * is preserved.
	 *
	 * Inputs
	 * list		the list with sorted elements in the range [0, loc]
	 * ins		insertion (or new) element
	 * loc		index indicating the sorted portion of the list
	 *
	 * Ouput
	 * list		the ordered list containing the new element
	 *
	 */
	{
		// shifts larger elements to the right
		while ( (loc >= 0) && (list[loc] > ins) )
		{
			list[loc + 1] = list[loc];
			--loc;
		}

		// inserts element in the correct location
		list[loc + 1] = ins;
		return;
	}


	private static void shift (int [] list, comparator compare,
				   int ins, int loc)
	/*
	 * Synopsis:
	 * Shifts elements in the list. Delegates the comparison of list
	 * elements to the passed comparator method. Returns the list
	 * having the new element in its rigthful location so that the
	 * (sought) ordered structure of the list is preserved.
	 *
	 * Inputs
	 * list		the list with sorted elements in the range [0, loc]
	 * compare	function that defines comparison of list elements
	 * ins		insertion (or new) element
	 * loc		index indicating the sorted portion of the list
	 *
	 * Ouput
	 * list		the ordered list containing the new element
	 *
	 */
	{
		// shifts targets (either smaller or larger) to the right
		while ( (loc >= 0) && compare.f(list[loc], ins) )
		{
			list[loc + 1] = list[loc];
			--loc;
		}

		// inserts element in the correct location
		list[loc + 1] = ins;
		return;
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


	private static void print (int [] list)
	// prints the elements of the list to the console
	{
		for (int i = 0; i != list.length; ++i)
			System.out.printf("%4d", list[i]);
		System.out.println("\n");
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


	// tests:
	private static void testInsertionSort ()
	/*
	 * Synopsis:
	 * Tests the implementation of InsertionSort().
	 * Compares the sorted list against the expected one.
	 * The implementation passes the test if the lists are identical.
	 *
	 */
	{
		int list [] = ulist ();		// unsorted list
		int incr [] = arange ();	// expected list

		InsertionSort(list);	// sorts list in ascending order

		System.out.printf("test::InsertionSort(): ");
		if ( equality(list, incr) )
			System.out.printf("pass\n");
		else
			System.out.printf("FAIL\n");

		return;
	}
}
