/*
 * Algorithms and Programming II                             April 29, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a vector of coordinates (x, y) in Java.
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

import java.util.Random;

public class Vector
{
	private int begin = 0;				// beginning
	private int avail = 0;				// available
	private int limit = 16;				// default size
	private boolean sorted = false;			// unsorted state
	private Coord [] array = new Coord [limit];	// data placeholder

	static class Coord		// coordinates class
	{
		private int x;		// x-axis coordinate
		private int y;		// y-axis coordinate

		Coord ()		// default constructor
		{
			x = 0;
			y = 0;
		}

		Coord (int xi, int yi)
		// constructs from (xi, yi) coordinates
		{
			x = xi;
			y = yi;
		}

		Coord (int [] coords)
		// constructs from (xi, yi) coordinates
		{
			x = coords[0];
			y = coords[1];
		}


		interface Comparator
		// defines abstract comparison method
		{
			public int compare (Coord P, Coord Q);
		}


		public static int [] toArray (Coord coord)
		// returns a 2nd rank array with a copy of the coordinates
		{
			int [] array = {coord.x, coord.y};
			return array;
		}


		public static void print (Coord coord)
		// prints x, y coordinates on the console
		{
			System.out.printf("%6d%6d\n", coord.x, coord.y);
		}


		public static boolean equals (Coord P, Coord Q)
		// returns `true' if P == Q, `false' otherwise
		{
			if ( (P.x == Q.x) && (P.y == Q.y) )
				return true;
			else
				return false;
		}


		public static int compare (Coord P, Coord Q)
		// returns 1 if P > Q, 0 if P == Q, and -1 if P < Q.
		{
			if (P.x > Q.x)
				return 1;
			else
			{
				if ( P.x == Q.x )
				{
					if ( P.y == Q.y )
						return  0;
					else if ( P.y > Q.y )
						return  1;
					else
						return -1;
				}
				else
					return -1;
			}
		}
	}


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


	public static void push_back (Vector vector, int x, int y)
	// pushes coordinates unto the back of vector
	{
		vector.back_inserter ( vector, new Coord(x, y) );
	}


	public static void push_back (Vector vector, int [] coords)
	// pushes coordinates unto the back of vector
	{
		vector.back_inserter ( vector, new Coord(coords) );
	}


	public static void push_back (Vector vector, int [][] coords)
	// pushes coordinates unto the back of vector
	{
		if (coords[0].length != 2)
			throw new RuntimeException(
				"Vector.push_back(): expects array[*][2]"
			);

		for (int i = 0; i != coords.length; ++i)
			vector.back_inserter(vector, new Coord(coords[i]));
	}


	public static int [] index (Vector vector, int i)
	// returns indexed coordinates as an array of two elements
	{
		// bounds check
		if (i < vector.begin || i >= vector.avail)
			throw new RuntimeException("out-of-bounds error");

		// delegates the task to the Coord's method toArray()
		return vector.array[i].toArray (vector.array[i]);
	}



	public static void delete (Vector vector)
	// effectively deletes the last element
	{
		vector.avail = (vector.avail > 0)? (--vector.avail) : 0;
	}


	public static int [] pop (Vector vector)
	// pops the last element, complains if there are no elements to pop
	{
		Coord last = new Coord();
		if (vector.avail > 0)
			last = vector.array[--vector.avail];
		else
			throw new RuntimeException("Vector.pop(): empty");

		return last.toArray(last);
	}


	public static int [] pop (Vector vector, int [] coords)
	// pops selected coordinates from vector
	{
		Coord coord = vector.popper ( vector, new Coord(coords) );
		return coord.toArray(coord);
	}


	public static int [][] pop (Vector vector, int begin, int end)
	// pops elements in the asymmetric range [begin, end)
	{

		// bounds check
		if (begin < vector.begin || end >= vector.avail)
			throw new RuntimeException("out-of-bounds error");

		// gets elements to be popped
		int size = (end - begin);
		Coord [] coords = new Coord [size];

		for (int i = 0; i != size; ++i)
			coords[i] = vector.array[i + begin];

		int [] pair = new int [2];
		int [][] matrix = new int [size][2];
		for (int i = 0; i != size; ++i)
		{
			pair = coords[i].toArray(coords[i]);
			matrix[i][0] = pair[0];
			matrix[i][1] = pair[1];
		}

		// gets number of invalidated elements
		int avail = vector.avail;
		size = (avail - end);

		// shifts invalidated elements to available range [b, e)
		if (size != 0)
		{
			Coord [] array = new Coord [size];
			for (int i = 0; i != size; ++i)
				array[i] = vector.array[i + end];

			for (int i = 0; i != size; ++i)
				vector.array[i + begin] = array[i];
		}

		// decrements according to the number of popped elements
		size = (end - begin);
		vector.avail -= size;

		return matrix;
	}


	public static void print (Vector vector)
	// prints the elements of the vector on the console
	{
		int size = vector.avail;
		for (int i = 0; i != size; ++i)
			vector.array[i].print( vector.array[i] );

		return;
	}


	public static int search (Vector vector, int [] target)
	// returns the positional index of the target value in the vector
	{
		return vector.searcher ( vector, new Coord(target) );
	}


	public static int [] select (Vector vector, int K)
	// selects the kth largest element in vector
	{
		// complains if K is outside the bounds of the vector
		checkBounds (vector, K);
		int numel = size (vector);
		// delegates the task to the Find Kth Largest Algorithm
		Coord coord = FindKthLargest (numel, vector.array, K);
		return coord.toArray(coord);
	}


	public static void sort (Vector vector)
	// sorts elements in vector in ascending order
	{

		int numel = size (vector);
		// delegates the task to the Insertion Sort Algorithm
		InsertionSort (numel, vector.array);
		vector.sorted = true;
	}


	public static void sort (Vector vector, Coord.Comparator comp)
	// sorts elements in vector based on the comparator function
	{

		int numel = size (vector);
		// delegates the task to the Insertion Sort Algorithm
		InsertionSort (numel, vector.array, comp);
		vector.sorted = true;
	}


	// implementations:
	private static void back_inserter (Vector vector, Coord coord)
	// pushes data into the back of vector
	{
		int avail = vector.avail;
		int limit = vector.limit;

		if (avail == limit)
			grow (vector);

		vector.array[vector.avail++] = coord;
		vector.sorted = false;

		return;
	}


	private static void grow (Vector vector)
	// doubles the vector size limit
	{
		int limit = 2 * vector.limit;
		int numel = vector.avail;
		Coord array [] = new Coord [numel];

		// copies existing data into temporary placeholder
		for (int i = 0; i != numel; ++i)
			array[i] = vector.array[i];

		// doubles the vector size limit and restores the data
		vector.array = null;
		vector.array = new Coord [limit];
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


	private static Coord popper (Vector vector, Coord target)
	// pops target element, complains if it does not exist
	{

		// caters compiler initialization warning
		Coord popped = new Coord();

		// searches for the target
		int pos = vector.searcher (vector, target);
		if (pos == 0)
			throw new RuntimeException(
				"Vector.pop(): target not found in vector"
			);
		else
			popped = popit (vector, pos);	// pops target

		return popped;	// returns a copy of the target
	}


	private static Coord popit (Vector vector, int pos)
	// pops element by its positional index [1, N]
	{
		int p = (pos - 1);
		int avail = vector.avail;
		Coord popped = vector.array[p];

		if (pos != avail)
		{
			// copies invalidated slice in [pos, avail)
			int size = (avail - pos);
			Coord [] array = new Coord [size];
			for (int i = 0; i != size; ++i)
				array[i] = vector.array[i + pos];

			// shifts slice to fill vacant location
			for (int i = 0; i != size; ++i)
				vector.array[i + p] = array[i];
		}

		--vector.avail;	// decrements #stored elements
		return popped;	// returns copy of popped element
	}


	private static int searcher (Vector vector, Coord target)
	// returns the positional index of the target value in the vector
	{
		int N = size (vector);
		if (vector.sorted)
			return BinarySearch (N, vector.array, target);
		else
			return SequentialSearch (N, vector.array, target);
	}


	private static int BinarySearch (int N, Coord [] coords, Coord t)
	/*
	 * Synopsis:
	 * Implements the Binary Search Algorithm.
	 * Searches until a match is found or until the list
	 * is traversed completely.
	 *
	 * Inputs
	 * coords	array of sorted coordinates
	 * t		target
	 *
	 * Output
	 * idx		positional index [1, NUMEL], otherwise zero
	 *
	 */
	{
		int m;
		int b = 1, e = N;
		while (b <= e)
		{
			m = (b + e) / 2;
			if (t.compare(t, coords[m - 1]) == 1)
				b = m + 1;
			else if (t.compare(t, coords[m - 1]) == -1)
				e = m - 1;
			else
				return m;
		}

		return 0;
	}


	private static int SequentialSearch(int N, Coord[] coords, Coord t)
	/*
	 * Synopsis:
	 * Possible implementation of the Sequential Search Algorithm.
	 * Searches sequentially until a match is found or until the array
	 * is traversed completely.
	 *
	 * Inputs
	 * N		number of elements in the array
	 * coords	array of (presumed) unsorted coordinates
	 * t		target
	 *
	 * Output
	 * idx		positional index [1, NUMEL], otherwise zero
	 *
	 */
	{
		int idx = 0;
		for (int i = 0; i != N; ++i)
		{
			if ( coords[i].equals(coords[i], t) )
			{
				idx = (i + 1);
				break;
			}
		}

		return idx;
	}


	private static Coord FindKthLargest (int N, Coord [] array, int K)
	/*
	 * Synopsis:
	 * Possible implementation of the Find Kth Largest Algorithm.
	 *
	 * Inputs
	 * N		number of elements in the array
	 * K		integer indicating kth largest counting from zero
	 * array	array of (presumed) unsorted coordinates
	 *
	 * Output
	 * max		the kth largest element in the array
	 *
	 */
	{
		Coord max = array[0];		// caters init warning
		Coord [] ary = copy (array);	// creates a shallow copy
		for (int i = 0; i != (K + 1); ++i)
		{
			max = swap ( N, ary, argmax(N, ary) );
			--N;
		}

		return max; 			// returns the kth largest
	}


	private static Coord [] copy (Coord [] array)
	/*
	 * Synopsis:
	 * Creates a shallow copy of an array.
	 *
	 */
	{
		Coord [] ary = new Coord [array.length];
		for (int i = 0; i != array.length; ++i)
			ary[i] = array[i];

		return ary;
	}


	private static Coord swap (int N, Coord [] x, int i)
	/*
	 * Synopsis:
	 * Swaps (or interchanges) the ith element with the last one.
	 *
	 * Input
	 * N		array size
	 * x		array of coordinates
	 * i		the index of the ith element of the array
	 *
	 * Output
	 * x		the array after the interchange of elements
	 *
	 */
	{
		int e = N - 1;		// gets index of the last element
		Coord end = x[e];	// copies last element
		Coord ith = x[i];	// copies the ith element
		x[e] = x[i];		// moves the ith element to the end
		x[i] = end;		// swaps the elements
		return ith;		// returns the ith element
	}


	private static int argmax (int N, Coord [] coords)
	/*
	 * Synopsis:
	 * Possible implementation of argmax function in Java.
	 * Returns the index of the element of the array having the maximum
	 * value.
	 *
	 * Input
	 * N		number of elements in array
	 * coords	array of coordinates
	 *
	 * Output
	 * idx		index of the element having the maximum value
	 *
	 */
	{
		int idx = 0;
		Coord max = coords[idx];
		for (int i = 1; i != N; ++i)
		{
			if ( coords[i].compare(coords[i], max) == 1 )
			{
				idx = i;
				max = coords[idx];
			}

		}

		return idx;
	}


	private static void checkBounds (Vector vector, int i)
	{
		if (i < vector.begin || i >= vector.avail)
			throw new RuntimeException("out-of-bounds error");
	}


	private static void InsertionSort (int N, Coord [] coords)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 *
	 * Inputs
	 * N		number of elements
	 * coords	array of (presumed) unsorted coordinates
	 *
	 * Output
	 * array	the sorted array of coordinates
	 *
	 */
	{
		int loc;
		Coord ins;
		for (int i = 1; i != N; ++i)
		{
			ins = coords[i];	// insertion element
			loc = (i - 1);		// sorted range [0, loc]
			shift(coords, ins, loc);// inserts in right place
		}

		return;
	}


	private static void InsertionSort (int N, Coord [] coords,
					   Coord.Comparator comp)
	/*
	 * Synopsis:
	 * Possible implementation of the Insertion Sort Algorithm.
	 *
	 * Inputs
	 * comp		function that defines comparison of coordinates
	 * N		number of elements
	 * coords	array of (presumed) unsorted coordinates
	 *
	 * Output
	 * array	the sorted array of coordinates
	 *
	 */
	{
		int loc;
		Coord ins;
		for (int i = 1; i != N; ++i)
		{
			ins = coords[i];	// insertion element
			loc = (i - 1);		// sorted range [0, loc]

			// inserts in the correct location
			shift (comp, coords, ins, loc);
		}

		return;
	}


	private static void shift (Coord [] ary, Coord in, int loc)
	/*
	 * Synopsis:
	 * Shifts elements in the array whose value are greater than the
	 * insertion element. Returns the array having the new element in
	 * its rigthful location so that the ordered structure of the array
	 * is preserved.
	 *
	 * Inputs
	 * ary		array with sorted elements in the range [0, loc]
	 * in		insertion (or new) element
	 * loc		index indicating the sorted portion of the array
	 *
	 * Ouput
	 * array	the ordered array containing the new element
	 *
	 */
	{
		// shifts larger elements to the right
		while (
			(loc >= 0) && (ary[loc].compare(ary[loc], in) == 1)
		      )
		{
			ary[loc + 1] = ary[loc];
			--loc;
		}

		// inserts element in the correct location
		ary[loc + 1] = in;
		return;
	}


	private static void shift (Coord.Comparator f, Coord [] ary,
				   Coord in, int loc)
	/*
	 * Synopsis:
	 * Shifts elements in the array to sort them in accordance with
	 * the supplied comparator method.
	 *
	 * Inputs
	 * f		function that defines comparison of coordinates
	 * ary		array with sorted elements in the range [0, loc]
	 * in		insertion (or new) element
	 * loc		index indicating the sorted portion of the array
	 *
	 * Ouput
	 * array	the ordered array containing the new element
	 *
	 */
	{
		// shifts larger elements to the right
		while ( (loc >= 0) && (f.compare(ary[loc], in) == 1) )
		{
			ary[loc + 1] = ary[loc];
			--loc;
		}

		// inserts element in the correct location
		ary[loc + 1] = in;
		return;
	}


	static Coord.Comparator comparator = (Coord P, Coord Q) -> {
	/*
	 * Synopsis:
	 * User-defined comparator of coordinates which compares primarily
	 * with respect to the y-coordinate and secondarily with respect to
	 * the x-coordinate:
	 *
	 * 	Returns 1 if P > Q, 0 if P == Q, and -1 if P < Q
	 *
	 */
		if (P.y > Q.y)
			return 1;
		else
		{
			if ( P.y == Q.y )
			{
				if ( P.x == Q.x )
					return  0;
				else if ( P.x > Q.x )
					return  1;
				else
					return -1;
			}
				else
					return -1;
			}
	};


	public static void main (String[] args)
	{
		// pushes coordinates unto back of vector
		test_vectorPushBack();
		// sorts vector of coordinates in ascending order
		test_vectorSort();
		// pops elements into another vector (filters elements)
		test_vectorPop();
		// creates a dataset without duplicate elements
		dataset();

		return;
	}


	// examples:
	private static void test_vectorPushBack ()
	// pushes coordinates unto back of vector
	{
		System.out.println();
		System.out.println("\ntest::Vector.push_back():\n");

		int x, y;
		Random rand = new Random();
		Coord coord = new Coord();
		Vector vector = new Vector();
		for (int i = 0; i != 16; ++i)
		{
			x = rand.nextInt(16);
			y = rand.nextInt(16);
			vector.push_back (vector, x, y);
		}

		// prints the vector size
		System.out.printf("vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("\nstored data:\n");
		print (vector);
	}


	private static void test_vectorSort ()
	// sorts vector and searches for min and max values
	{

		System.out.println();
		System.out.println("\ntest::Vector.sort():\n");

		int x, y;
		Random rand = new Random();
		Coord coord = new Coord();
		Vector vector = new Vector();
		for (int i = 0; i != 16; ++i)
		{
			x = rand.nextInt(16);
			y = rand.nextInt(16);
			vector.push_back (vector, x, y);
		}

		// prints the vector size
		System.out.printf("vector size: %d\n", size(vector) );
		// prints the stored data
		System.out.printf("\noriginal data:\n");
		print (vector);


		// prints positional index of smallest element
		int [] pair = vector.select(vector, 15);
		int pos = vector.search(vector, pair);
		System.out.println();
		System.out.printf("position of smallest: %2d\n", pos);

		// prints positional index of largest element
		pair = vector.select(vector, 0);
		pos = vector.search(vector, pair);
		System.out.printf("position of largest:  %2d\n", pos);

		// sorts vector
		System.out.printf("\nsorted data:\n");
		vector.sort(vector);
		print (vector);

		System.out.println();

		// prints positional index of smallest element
		pair = vector.select(vector, 15);
		pos = vector.search(vector, pair);
		System.out.println();
		System.out.printf("position of smallest: %2d\n", pos);

		// prints positional index of largest element
		pair = vector.select(vector, 0);
		pos = vector.search(vector, pair);
		System.out.printf("position of largest:  %2d\n", pos);
	}


	private static void test_vectorPop ()
	// pops elements in a range
	{
		System.out.println();
		System.out.println("\ntest::Vector.pop():\n");

		int x, y;
		Random rand = new Random();
		Coord coord = new Coord();
		Vector vector = new Vector();
		for (int i = 0; i != 16; ++i)
		{
			x = rand.nextInt(16);
			y = rand.nextInt(16);
			vector.push_back (vector, x, y);
		}

		// prints the vector size
		System.out.printf("vector size: %d\n", size(vector) );

		// sorts vector
		System.out.printf("\nsorted data:\n");
		vector.sort(vector);
		print (vector);

		System.out.println();

		// pops elements in [0, 8)
		Vector popped = new Vector();
		popped.push_back ( popped, vector.pop(vector, 0, 8) );

		System.out.printf("\npopped data:\n");
		print (popped);

		System.out.printf("\nremaining data:\n");
		print (vector);
	}


	private static void dataset ()
	// creates dataset of coordinates without duplicates
	{
		int size = 16;
		Random rand = new Random();
		Vector coords = new Vector();

		int pos;
		int [] coord = new int [2];
		// pushes distinct coordinates unto the back of the vector
		for (int i = 0; i != size; ++i)
		{
			do
			// creates new coordinates if these exist in vector
			{
				coord[0] = rand.nextInt(16);
				coord[1] = rand.nextInt(16);
			}
			while ( coords.search(coords, coord) != 0 );
			// inserts new coordinates
			coords.push_back(coords, coord);
		}

		System.out.println("\noriginal dataset:\n");
		coords.print(coords);

		// sorts primarily with respect to the x-coordinate and
		// secondarily with respect to the y-coordinate
		System.out.println("\nx-y sorted dataset:\n");
		coords.sort(coords);
		coords.print(coords);
		System.out.println();

		// sorts primarily with respect to the y-coordinate and
		// secondarily with respect to the x-coordinate
		System.out.println("\ny-x sorted dataset:\n");
		coords.sort(coords, comparator);
		coords.print(coords);
		System.out.println();

		return;
	}

}
