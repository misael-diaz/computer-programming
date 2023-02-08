/*
 * Algorithms and Complexity                              November 12, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines a generic vector class.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] www.geeksforgeeks.org/generics-in-java
 * [1] www.geeksforgeeks.org/function-interface-in-java-with-examples
 * [2] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 *
 */


import java.util.Arrays;
import java.util.Random;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;


public final class Vector<T extends Comparable<T>> implements Iterable<T>
// Generic Vector Class
{

	class Iterator<T extends Comparable<T>> implements java.util.Iterator<T>
	// Vector Iterator Class
	{
		private int index;	// iterator (data array index)
		private int limit;	// iterator limit
		private T[] array;	// reference to the stored data

		Iterator (Vector<T> vector)
		// constructor
		{
			this.index = vector.begin;
			this.limit = vector.avail;
			this.array = vector.array;
		}

		@Override
		public boolean hasNext ()
		// returns true if there is a next element, false otherwise
		{
			return (this.index != this.limit);
		}

		@Override
		public T next ()
		// returns a copy of the ith element and advances iterator
		{
			int i = this.index;
			++(this.index);
			return ( Arrays.copyOfRange(this.array, i, i + 1) )[0];
		}
	}


	/* defines the default vector size */


	private static final int DEFAULT_SIZE = 16;


	/* component(s) */


	private Function<Integer, T[]> allocator;	// data allocator
	private T[] array;				// data placeholder
	private int begin;				// beginning of the data
	private int avail;				// available for writing
	private int limit;				// storage limit
	private boolean isSorted;			// true if sorted
	private boolean isView;				// true if a view
	private boolean isResized;			// true if resized


	/* constructor(s) */


	Vector ()
	// constructs an empty view
	{
		this.allocator = null;
		this.array = null;
		this.begin = 0;
		this.avail = 0;
		this.limit = 0;
		this.isSorted = false;
		this.isView = true;
		this.isResized = false;
	}


	Vector (Function<Integer, T[]> allocator)
	// constructs a vector<T> from the allocator of the object type
	{
		int size = DEFAULT_SIZE;

		this.allocator = allocator;
		this.array = allocator.apply(size);
		this.begin = 0;
		this.avail = 0;
		this.limit = size;
		this.isSorted = false;
		this.isView = false;
		this.isResized = false;
	}


	Vector (int size, Function<Integer, T[]> allocator)
	// constructs a vector<T> with a storage capacity equal to `size'
	{
		this.allocator = allocator;
		this.array = allocator.apply(size);
		this.begin = 0;
		this.avail = 0;
		this.limit = size;
		this.isSorted = false;
		this.isView = false;
		this.isResized = false;
	}


	Vector (Vector<T> vector)
	// copy constructor (fits vector to size for saving space)
	{
		this.allocator = vector.allocator;
		this.array = vector.get();
		this.begin = 0;
		this.avail = vector.size();
		this.limit = vector.size();
		this.isSorted = vector.isSorted;
		this.isView = false;
		this.isResized = false;
	}

	Vector (Vector<T> vector, int begin, int end)
	// creates a view of the given range [b, e) of a vector
	{
		// creates an empty view if an invalid range is supplied
		end = (end < begin)? begin : end;
		// applies offset (for the given vector might be a view itself)
		int b = (vector.begin + begin), e = (vector.begin + end);
		this.allocator = vector.allocator;
		// gets a copy of the reference for speed
		this.array = vector.array;
		// fits view to range size [b, e)
		this.begin = b;
		this.avail = e;
		this.limit = e;
		this.isSorted = vector.isSorted;
		this.isView = true;
		this.isResized = false;
	}


	/* methods */


	@Override
	public java.util.Iterator<T> iterator ()
	// implements the iterator interface
	{
		return ( new Iterator<>(this) );
	}


	public void clear ()
	// effectively clears the vector elements
	{
		this.avail = this.begin;
	}


	public int size ()
	// returns the number of elements stored in the vector
	{
		return (this.avail - this.begin);
	}


	public int capacity ()
	// returns the storage capacity of the vector
	{
		return (this.limit - this.begin);
	}


	public boolean isResized ()
	// returns the resized state
	{
		return this.isResized;
	}


	public void view (Vector<T> vector, int begin, int end)
	// makes a view of the given vector in the range [b, e)
	{
		// applies offset (for the given vector might be a view itself)
		int b = (vector.begin + begin), e = (vector.begin + end);
		this.allocator = vector.allocator;
		// gets a copy of the reference for speed
		this.array = vector.array;
		// fits view to range size [b, e)
		this.begin = b;
		this.avail = e;
		this.limit = e;
		this.isSorted = vector.isSorted;
		this.isView = true;
		this.isResized = false;
	}


	public T[] get ()
	// returns a copy of the data array stored in the vector
	{
		return Arrays.copyOfRange(this.array, this.begin, this.avail);
	}


	public T get (int i)
	// returns a copy of the ith element
	{
		i += this.begin;	// applies offset to account for views
		return ( Arrays.copyOfRange(this.array, i, i + 1) )[0];
	}


	public int search (T key)
	/*

	Synopsis:
	Returns the positional index of the key. If the key is present,
	the method returns a positional index in the asymmetric range
	[this.begin, this.avail); otherwise it returns an invalid index.

	Input:
	key	the element being searched for

	Output:
	pos	equal to the positional index if present,
		otherwise it is equal to -1 (invalid index)

	*/
	{
		T k = key;
		int b = this.begin;
		int e = this.avail;
		T[] data = this.array;

		if (this.isSorted)
		{
			// uses binary search when sorted
			return Arrays.binarySearch(data, b, e, k);
		}
		else
		{
			// otherwise uses linear search
			return this.linearSearch(data, b, e, k);
		}
	}


	public int search (T key, Comparator<T> comp, String method)
	/*

	Synopsis:
	Returns the positional index of the key. If the key is present,
	the method returns a positional index in the asymmetric range
	[this.begin, this.avail); otherwise it returns an invalid index.
	Uses the comparator supplied by the user to compare the vector
	elements and applies the search algorithm requested by the user.

	Inputs:
	key		the element being searched for
	comp		comparator
	method		either 'binary' or 'linear' search

	Output:
	pos		equal to the positional index if present,
			otherwise it is equal to -1 (invalid index)

	*/
	{
		T k = key;
		Comparator<T> c = comp;
		int b = this.begin;
		int e = this.avail;
		T[] data = this.array;

		if ( method.equalsIgnoreCase("binary") )
		{
			// uses binary search by request of the user
			return Arrays.binarySearch(data, b, e, k, c);
		}
		else
		{
			// otherwise uses linear search
			return this.linearSearch(data, b, e, k, c);
		}
	}



	public boolean contains (T key)
	// returns true if the key is contained, false otherwise
	{
		if ( this.search(key) < 0)
			return false;
		else
			return true;
	}


	public boolean contains (T key, Comparator<T> comp, String method)
	// as above but uses the comparator and the requested method
	{
		if ( this.search(key, comp, method) < 0)
			return false;
		else
			return true;
	}


	public void push_back (Supplier<T> copyConstructor)
	/*

	Synopsis:
	Pushes a copy of the given element at the back of the vector.
	The user must supply the copy constructor to the method instead
	of the element itself.

	*/
	{
		if (!this.isView)
		// disables the push-back() method for views
		{
			this.back_inserter(copyConstructor);
		}
	}


	public void sort ()
	// sorts the vector elements
	{
		if (!this.isView)
		// disables the sort() method for views
		{
			Arrays.sort(this.array, this.begin, this.avail);
			this.isSorted = true;
		}
	}


	public void sort (Comparator<T> comp)
	// uses the supplied comparator for sorting the vector elements
	{
		if (!this.isView)
		// disables the sort() method for views
		{
			Arrays.sort(array, begin, avail, comp);

			/*

			sets to false because the user-defined comparator
			might be incompatible with the default sorting
			of the type T

			*/

			this.isSorted = false;
		}
	}


	/* implementations */


	private void back_inserter (Supplier<T> copyConstructor)
	// pushes (a copy of the) data unto the back of the vector
	{


		// increases the memory allocation for storage if needed
		if (this.avail == this.limit)
			this.grow();


		// stores a copy of the data object at the available location
		this.array[this.avail] = copyConstructor.get();

		// increments the vector size accordingly
		++(this.avail);

		// assumes that the new element is out-of-order
		this.isSorted = false;
	}


	private void grow ()
	// doubles the vector size
	{
		// creates a backup of the stored data
		T [] data = this.get();

		/* doubles the memory allocation and restores the data */

		this.limit *= 2;
		this.array = this.allocator.apply(this.limit);
		for (int i = 0; i != this.avail; ++i)
			this.array[i] = data[i];

		// sets the resized state because the storage capacity has increased
		this.isResized = true;
	}


	private int linearSearch (T [] data, int begin, int end, T key)
	/*

	Synopsis:
	Searches linearly for the given key in the range [begin, end). Returns
	its positional index if present, otherwise returns -1 (invalid index).

	*/
	{
		for (int i = begin; i != end; ++i)
		{
			if (key.compareTo(data[i]) == 0)
				return i;
		}

		return (0xFFFFFFFF);	// returns invalid if not present
	}


	private int linearSearch (T[] data, int begin, int end, T key, Comparator<T> comp)
	// searches linearly for the key and returns its positional index
	{
		for (int i = begin; i != end; ++i)
		{
			if (comp.compare(key, data[i]) == 0)
				return i;
		}

		return (0xFFFFFFFF);	// returns invalid if not present
	}


	public static void main (String [] args)
	// tests (some of) the methods of the vector class
	{
		testPushBackMethod();
		testSortMethod();
		testViews();
		testBisect();
		testIterator();
		return;
	}


	private static void testPushBackMethod ()
	/*

	Synopsis:
	Checks if the storage order is preserved as it should be.

	*/
	{
		// constructs a vector of points from allocator
		int size = (0x00100000);
		Vector<Point> vector = new Vector<> (
			size, (Integer sz) -> new Point[sz]
		);

		Random r = new Random();
		Point [] points = new Point[size];
		for (int i = 0; i != size; ++i)
		// effectively pushes points unto the back of the vector
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			points[i] = p;
			// passes the copy constructor
			vector.push_back( () -> new Point(p) );
		}


		// checks the size of the vector
		System.out.printf("test-push-back[0]: ");
		if (vector.size() != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		int fails = 0;
		for (int i = 0; i != size; ++i)
		// checks if the vector preserves the storing order
		{
			Point P = points[i];
			Point Q = vector.get(i);
			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;
		}


		System.out.printf("test-push-back[1]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-push-back[2]: ");
		if ( vector.isResized() )
			System.out.println("FAIL");
		else
			System.out.println("pass");


		vector.push_back ( () -> new Point() );
		System.out.printf("test-push-back[3]: ");
		if ( !vector.isResized() )
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testSortMethod ()
	// checks if a sorted vector is actually sorted
	{
		// constructs a vector of points from allocator
		Vector<Point> vector = new Vector<> (
			(Integer size) -> new Point[size]
		);

		int size = (0x00001000);
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// effectively pushes points unto the back of the vector
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			// passes the copy constructor
			vector.push_back( () -> new Point(p) );
		}


		vector.sort();


		int fails = 0;
		Point [] points = vector.get();
		for (int i = 0; i != (size - 1); ++i)
		// checks if the vector elements are not in ascending order
		{
			Point P = points[i];
			Point Q = points[i + 1];
			if (P.compareTo(Q) > 0)
				fails += 1;
			else
				fails += 0;
		}

		System.out.printf("test-sort[0]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");

		Vector<Point> yxSortedPoints = new Vector<>(vector);
		yxSortedPoints.sort( new Point.Comparator() );


		Point [] data = yxSortedPoints.get();
		for (int i = 0; i != (size - 1); ++i)
		// checks if y-x sorted elements are not in ascending order
		{
			Point P = data[i];
			Point Q = data[i + 1];
			if (new Point.Comparator().compare(P, Q) > 0)
				fails += 1;
			else
				fails += 0;
		}

		System.out.printf("test-sort[1]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");

		// checks if the elements of the vector `yxSortedPoints' are y-x sorted;
		// if they are, the binary search algorithm must be able to find all the
		// elements without failures
		for (Point p : vector)
		{
			boolean isContained = yxSortedPoints.contains(
				p, new Point.Comparator(), "binary"
			);

			if (!isContained)
				fails += 1;
			else
				fails += 0;
		}

		System.out.printf("test-search[0]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		for (Point p : vector)
		// as in the above test but uses the linear search algorithm
		{
			boolean isContained = yxSortedPoints.contains(
				p, new Point.Comparator(), "linear"
			);

			if (!isContained)
				fails += 1;
			else
				fails += 0;
		}

		System.out.printf("test-search[1]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");

	}


	private static void testViews ()
	/*

	Synopsis:
	Checks that when views go out-of-scope the data present in the original vector
	is not released from memory. The expectation is that references to the stored
	data get nullified.

	*/
	{
		// constructs a vector of points from allocator
		Vector<Point> vector = new Vector<> (
			(Integer size) -> new Point[size]
		);


		// checks for data differences in the views
		createViews(vector);


		for(int i = 0; i != vector.size(); ++i)
		/*

		if we do not get a null pointer exception, this means
		that the views were nullified and the contained data
		in the original vector was preserved as it should be

		*/
		{
			Point point = vector.get(i);
			double x = point.getX(), y = point.getY();
			System.out.printf("x: %f, y: %f\n", x, y);
		}
	}


	private static void createViews (Vector<Point> vector)
	// creates views each containing half of the contained data
	{


		int size = (0x00000010);
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// effectively pushes points unto the back of the vector
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			// passes the copy constructor
			vector.push_back( () -> new Point(p) );
		}


		int begin = 0, end = (size / 2);
		// creates a view of the first contained half
		Vector<Point> first = new Vector<>(vector, begin, end);

		begin = (size / 2); end = size;
		// creates a view of the second contained half
		Vector<Point> second = new Vector<>(vector, begin, end);


		int numel = 0;
		int diffs = 0;
		for (int i = 0; i != first.size(); ++i)
		// counts #differences between the view and the original
		{
			Point P = vector.get(i);
			Point Q = first.get(i);

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++numel;
		}

		System.out.printf("test-view[0]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		for (int i = 0; i != second.size(); ++i)
		// counts #differences between the view and the original
		{
			// Note: applies offset to get the same data
			Point P = vector.get(first.size() + i);
			Point Q = second.get(i);

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++numel;
		}

		System.out.printf("test-view[1]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-view[2]: ");
		/*

		checks that the vector has been traversed, meaning that
		we have considered all the elements in the previous tests

		*/
		if (vector.size() != numel)
			System.out.println("FAIL");
		else
			System.out.println("pass");

	}


	private static void testBisect ()
	/*

	Synopsis:
	Bisects a vector into two views having each half of the data.
	Then, it checks that the data contained in the views is consistent
	with that of the vector as it should be.

	*/
	{
		// constructs a vector of points from allocator
		Vector<Point> vector = new Vector<> (
			(Integer size) -> new Point[size]
		);


		int size = (0x00001000);
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// effectively pushes points unto the back of the vector
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			// passes the copy constructor
			vector.push_back( () -> new Point(p) );
		}


		/*

		allocates memory for the views

		*/


		Vector<Point> first = new Vector<> ();
		Vector<Point> second = new Vector<> ();


		// bisects the original vector into two partitions
		bisect(vector, first, second);


		int numel = 0;
		int diffs = 0;
		for (int i = 0; i != first.size(); ++i)
		// counts differences between the view and the original
		{
			Point P = vector.get(i);
			Point Q = first.get(i);

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++numel;
		}

		System.out.printf("test-view[3]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		for (int i = 0; i != second.size(); ++i)
		// counts differences between the view and the original
		{
			// Note: applies offset to get the same data
			Point P = vector.get(first.size() + i);
			Point Q = second.get(i);

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++numel;
		}

		System.out.printf("test-view[4]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-view[5]: ");
		if (vector.size() != numel)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void bisect (Vector<Point> vector, Vector<Point> first,
				    Vector<Point> second)
	/*

	Synopsis:
	Creates views of the first and second halves of the data stored in the passed
	vector.

	Inputs:
	vector		vector storing all of the data
	first		unset view
	second		unset view

	Output:
	first		view of the first half of the data
	second 		view of the second half of the data

	*/
	{
		int size = vector.size();
		int begin = 0, end = (size / 2);
		// creates a view of the first contained half
		first.view(vector, begin, end);

		begin = (size / 2);	end = size;
		// creates a view of the second contained half
		second.view(vector, begin, end);
	}


	private static void testIterator ()
	/*

	Synopsis:
	Checks that by means of an iterator all the vector elements are traversed.

	*/
	{
		// constructs a vector of points from allocator
		Vector<Point> points = new Vector<> (
			(Integer size) -> new Point[size]
		);


		int size = (0x00000100);
		Random r = new Random();
		Point [] data = new Point[size];
		for (int i = 0; i != size; ++i)
		// effectively pushes points unto the back of the vector
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			data[i] = p;
			// passes the copy constructor
			points.push_back( () -> new Point(p) );
		}

		int numel = 0;
		for (Point p : points)
		// counts the number of elements in the vector
		{
			++numel;
		}

		System.out.printf("test-iterator[0]: ");
		/*

		informs if the number of elements is not equal to the vector size

		*/
		if (numel != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		int n = 0;
		int diffs = 0;
		for (Point P : points)
		/*

		checks for differences between the vector elements and the original array

		*/
		{
			Point Q = data[n];

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++n;
		}


		System.out.printf("test-iterator[1]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}
}
