/*
 * Algorithms and Programming II                             April 21, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a vector (or dynamic array) in Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Vector
{

	private int begin = 0;			// beginning of vector
	private int avail = 0;			// available for writing
	private int limit = 16;			// default size limit
	private int array [] = new int [limit];	// data placeholder


	// interfaces:
	public static void clear (Vector vector)
	// effectively clears the vector elements
	{
		vector.avail = 0;
		return;
	}


	public static int size (Vector vector)
	// returns the number of elements stored in the vector
	{
		return (vector.avail - vector.begin);
	}


	public static void push_back (Vector vector, int data)
	// pushes data into the back of vector
	{
		int avail = vector.avail;
		int limit = vector.limit;

		if (avail == limit)
			grow (vector);

		vector.array[vector.avail++] = data;

		return;
	}


	public static void delete (Vector vector)
	// effectively deletes the last element
	{
		vector.avail = (vector.avail > 0)? (--vector.avail) : 0;
	}


	public static int pop (Vector vector)
	// pops the last element, complains if there are no elements to pop
	{
		int last = 0;
		if (vector.avail > 0)
			last = vector.array[--vector.avail];
		else
			throw new RuntimeException("Vector.pop(): empty");

		return last;
	}


	public static int pop (Vector vector, int target)
	// pops target element, complains if it does not exist
	{

		int popped = 0;	// caters compiler initialization warning

		// searches for the target
		int pos = search (vector, target);
		if (pos == 0)
			throw new RuntimeException(
				"Vector.pop(): target not found in vector"
			);
		else
			popped = popit (vector, pos);	// pops target

		return popped;	// returns a copy of the target
	}


	public static void print (Vector vector)
	// prints the elements of the vector on the console
	{
		int size = vector.avail;
		for (int i = 0; i != size; ++i)
			System.out.printf("%d\n", vector.array[i]);

		return;
	}


	public static int search (Vector vector, int target)
	// returns the positional index of the target value in the vector
	{
		int numel = size (vector);
		// delegates the task to the Sequential Search Algorithm
		return SequentialSearch (numel, vector.array, target);
	}


	public static int select (Vector vector, int K)
	// selects the kth largest element in vector
	{
		// complains if K is outside the bounds of the vector
		checkBounds (vector, K);
		int numel = size (vector);
		// delegates the task to the Find Kth Largest Algorithm
		return FindKthLargest (numel, vector.array, K);
	}


	public static void sort (Vector vector)
	// sorts elements in vector in ascending order
	{

		int numel = size (vector);
		// delegates the task to the Insertion Sort Algorithm
		InsertionSort (numel, vector.array);
	}


	public static int median (Vector vector)
	// returns the median of the vector elements
	{
		isEmpty (vector);
		int N = vector.size (vector);
		vector.sort (vector);

		int m = N / 2;	// middle
		if (N % 2 == 0)
			return (vector.array[m] + vector.array[m - 1]) / 2;
		else
			return (vector.array[m]);
	}


	public static int sum (Vector vector)
	// sums the vector elements
	{
		int s = 0;
		int avail = vector.avail;
		for (int i = 0; i != avail; ++i)
			s += vector.array[i];

		return s;
	}


	// implementations:
	private static void grow (Vector vector)
	// doubles the vector size limit
	{
		int limit = 2 * vector.limit;
		int numel = vector.avail;
		int array [] = new int [numel];

		// copies existing data into temporary placeholder
		for (int i = 0; i != numel; ++i)
			array[i] = vector.array[i];

		// doubles the vector size limit and restores the data
		vector.array = null;
		vector.array = new int [limit];
		vector.limit = limit;
		for (int i = 0; i != numel; ++i)
			vector.array[i] = array[i];

		return;
	}


	private static void isEmpty (Vector vector)
	{
		if (vector.size(vector) == 0)
			throw new RuntimeException(
				"cannot obtain median of an empty vector"
			);
	}


	private static int popit (Vector vector, int pos)
	// pops element by its positional index [1, N]
	{
		int p = (pos - 1);
		int avail = vector.avail;
		int popped = vector.array[p];

		if (pos != avail)
		{
			// copies invalidated slice in [pos, avail)
			int size = (avail - pos);
			int [] array = new int [size];
			for (int i = 0; i != size; ++i)
				array[i] = vector.array[i + pos];

			// shifts slice to fill vacant location
			for (int i = 0; i != size; ++i)
				vector.array[i + p] = array[i];
		}

		--vector.avail;	// decrements #stored elements
		return popped;	// returns copy of popped element
	}


	private static int SequentialSearch (int N, int [] array, int t)
	/*
	 * Synopsis:
	 * Possible implementation of the Sequential Search Algorithm.
	 * Searches sequentially until a match is found or until the array
	 * is traversed completely.
	 *
	 * Inputs
	 * N		number of elements in the array
	 * array	array of (presumed) unsorted integers
	 * t		target value
	 *
	 * Output
	 * idx		positional index [1, NUMEL], otherwise zero
	 *
	 */
	{
		int idx = 0;
		for (int i = 0; i != N; ++i)
		{
			if (array[i] == t)
			{
				idx = (i + 1);
				break;
			}


		}

		return idx;
	}


	private static int FindKthLargest (int N, int [] array, int K)
	/*
	 * Synopsis:
	 * Possible implementation of the Find Kth Largest Algorithm.
	 *
	 * Inputs
	 * N		number of elements in the array
	 * K		integer indicating kth largest counting from zero
	 * array	array of (presumed) unsorted integers
	 *
	 * Output
	 * max		the kth largest element in the array
	 *
	 */
	{
		int max = array[0];		// caters init warning
		int ary [] = copy (array);	// creates a shallow copy
		for (int i = 0; i != (K + 1); ++i)
		{
			max = swap ( N, ary, argmax(N, ary) );
			--N;
		}

		return max; 			// returns the kth largest
	}


	private static int [] copy (int [] array)
	/*
	 * Synopsis:
	 * Creates a shallow copy of an array.
	 *
	 */
	{
		int ary [] = new int [array.length];
		for (int i = 0; i != array.length; ++i)
			ary[i] = array[i];

		return ary;
	}


	private static int swap (int N, int [] x, int i)
	/*
	 * Synopsis:
	 * Swaps (or interchanges) the ith element with the last one.
	 *
	 * Input
	 * N		array size
	 * x		array of integers
	 * i		the index of the ith element of the array
	 *
	 * Output
	 * x		the array after the interchange of elements
	 *
	 */
	{
		int e = N - 1;	// gets the index of the end value
		int end = x[e];	// creates placeholder for the end value
		int ith = x[i];	// creates placeholder for the ith value
		x[e] = x[i];	// writes the ith value at the end of array
		x[i] = end;	// writes the end value on the ith position
		return ith;
	}


	private static int argmax (int N, int [] array)
	/*
	 * Synopsis:
	 * Possible implementation of argmax function in Java.
	 * Returns the index of the element of the array having the maximum
	 * value.
	 *
	 * Input
	 * N		number of elements in array
	 * array		array of integers
	 *
	 * Output
	 * idx		index of the element having the maximum value
	 *
	 */
	{
		int idx = 0;
		int max = array[idx];
		for (int i = 1; i != N; ++i)
		{
			if (array[i] > max)
			{
				idx = i;
				max = array[idx];
			}

		}

		return idx;
	}


	private static void checkBounds (Vector vector, int i)
	{
		if (i < vector.begin || i >= vector.avail)
			throw new RuntimeException("out-of-bounds error");
	}


	private static void InsertionSort (int N, int [] array)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 *
	 * Inputs
	 * N		number of elements
	 * array	array of (presumed) unsorted integers
	 *
	 * Output
	 * array	the sorted array
	 *
	 */
	{
		int ins, loc;
		for (int i = 1; i != N; ++i)
		{
			ins = array[i];		// insertion element
			loc = (i - 1);		// sorted range [0, loc]
			shift (array, ins, loc);// inserts in right place
		}

		return;
	}


	private static void shift (int [] array, int ins, int loc)
	/*
	 * Synopsis:
	 * Shifts elements in the array whose value are greater than the
	 * insertion element. Returns the array having the new element in
	 * its rigthful location so that the ordered structure of the array
	 * is preserved.
	 *
	 * Inputs
	 * array	the array with sorted elements in the range [0, loc]
	 * ins		insertion (or new) element
	 * loc		index indicating the sorted portion of the array
	 *
	 * Ouput
	 * array	the ordered array containing the new element
	 *
	 */
	{
		// shifts larger elements to the right
		while ( (loc >= 0) && (array[loc] > ins) )
		{
			array[loc + 1] = array[loc];
			--loc;
		}

		// inserts element in the correct location
		array[loc + 1] = ins;
		return;
	}


	public static void main (String[] args)
	{
		// pushes elements unto back of vector
		test_vectorPushBack();
		// clears elements
		test_vectorClear();
		// pops elements
		test_vectorPop();
		// pops elements by their value
		test_vectorPopByValue();
		// searches the last element
		test_vectorSearch();
		// selects the largest element
		test_vectorSelect();
		// sorts vector elements
		test_vectorSort();

		// pushes random numbers in file unto the back of vector
		read();

		return;
	}


	// examples:
	private static void test_vectorPushBack ()
	// pushes integers in the range [0, 32) unto back of vector
	{

		System.out.println();
		System.out.println("Vector.push_back():");

		Vector vector = new Vector();
		for (int i = 0; i != 32; ++i)
			vector.push_back (vector, i);

		// prints the vector size
		System.out.printf( "vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("stored data:\n");
		print (vector);

		System.out.println();

		return;
	}


	private static void test_vectorClear ()
	// clears the elements of a vector
	{
		System.out.println();
		System.out.println("Vector.clear():");

		// pushes integers in the range [0, 32) unto back of vector
		Vector vector = new Vector();
		for (int i = 0; i != 32; ++i)
			vector.push_back (vector, i);

		// prints the vector size
		System.out.printf( "vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("stored data:\n");
		print (vector);

		/* clears the stored data in the vector */
		System.out.printf("clearing vector elements ... ");
		clear (vector);
		System.out.println("done");

		// prints the current vector size (empty)
		System.out.printf( "vector size: %d\n", size(vector) );
		print (vector);

		System.out.println();

		return;
	}


	private static void test_vectorPop ()
	// pops elements from vector and prints them to the console
	{
		System.out.println();
		System.out.println("Vector.pop():");

		Vector vector = new Vector();
		// pushes integers in the range [0, 32) unto back of vector
		for (int i = 0; i != 32; ++i)
			vector.push_back (vector, i);

		System.out.println("popping the following elements:");
		// pops elements from the back of vector
		for (int i = 0; i != 32; ++i)
			System.out.printf(
				"x[%2d]: %2d\n", i, vector.pop (vector)
			);

		// prints the current vector size (empty)
		System.out.printf( "vector size: %d\n", size(vector) );
		System.out.println();

		return;
	}


	private static void test_vectorPopByValue ()
	// test pop by value method
	{
		Vector vector = new Vector();
		// pushes integers in the range [0, 32) unto back of vector
		for (int i = 0; i != 32; ++i)
			vector.push_back (vector, i);

		// creates a list of elements to be popped from vector
		int [] odd = new int [16];
		for (int i = 0; i != 16; ++i)
			odd[i] = 2 * i + 1;

		// pops selected elements
		System.out.println("popping the odd elements:");
		for (int i = 0; i != 16; ++i)
			System.out.println( vector.pop (vector, odd[i]) );

		System.out.println();
		// shows the vector elements after the removals
		System.out.println("remaining (even) elements in vector:");
		print (vector);
		System.out.println();
	}


	private static void test_vectorSearch ()
	// prints the positional index of the last element in vector
	{
		System.out.println();
		System.out.println("Vector.search():");

		Vector vector = new Vector();
		for (int i = 0; i != 32; ++i)
			vector.push_back (vector, i);

		// prints table header
		System.out.println("pos    value");
		System.out.println("============");
		// prints position and values
		for (int i = 0; i != 32; ++i)
			System.out.printf(
				"%3d %4d\n", i + 1, vector.array[i]
			);

		// searches for the last element (target)
		int t = 31;
		String msg = "element %d is located on position %d\n";
		System.out.printf( msg, t, vector.search(vector, t) );

		System.out.println();

		return;
	}


	private static void test_vectorSelect ()
	// selects the largest value stored in vector
	{
		System.out.println();
		System.out.println("Vector.Select():");

		Vector vector = new Vector();
		for (int i = 32; i != 0; --i)
			vector.push_back (vector, i);

		// prints the vector size
		System.out.printf( "vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("stored data:\n");
		print (vector);

		// selects largest element in vector
		System.out.printf( "max: %d\n", vector.select(vector, 0) );

		System.out.println();

		return;
	}


	private static void test_vectorSort ()
	// sorts vector elements in ascending order
	{
		System.out.println();
		System.out.println("Vector.Sort():");

		Random random = new Random();
		Vector vector = new Vector();
		for (int i = 32; i != 0; --i)
			vector.push_back ( vector, random.nextInt(1024) );

		// prints the vector size
		System.out.printf( "vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("\noriginal data:\n");
		print (vector);

		// sorts vector elements
		vector.sort (vector);
		// complains if the vector elements are not sorted
		isAscending (vector.size(vector), vector.array);

		System.out.printf("\nsorted data:\n");
		print (vector);

		System.out.println();

		return;
	}


	private static void isAscending(int N, int [] array)
	// complains if the array is not sorted in ascending order
	{
		for (int i = 0; i != (N - 1); ++i)
		{
			if (array[i] > array[i + 1])
				throw new RuntimeException(
					"array is not in ascending order"
				);
		}

		return;
	}


	private static void read ()
	// reads the file contents into a vector
	{
		String filename = "random.txt";
		File f = new File (filename);

		try
		{
			Scanner in = new Scanner (f);
			Vector vector = new Vector();

			// reads random numbers into vector
			while ( in.hasNextInt() )
				vector.push_back( vector, in.nextInt() );

			System.out.printf("\nrandom numbers in vector:\n");
			print (vector);

			String msg = "vector stores %d random numbers\n";
			System.out.printf( msg, vector.size(vector) );

			// displays the median of the random numbers
			System.out.printf("median: %d\n",
					  vector.median(vector) );

		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			System.out.println("IO Error:");
			err.printStackTrace();
		}

		return;
	}
}

/*
 * TODO:
 * [x] implement removal of elements from the back
 * [x] implement search, selection, and sort algorithms for vectors
 *
 */
