/*
 * Algorithms and Complexity                              November 10, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines a generic, iterable, singly, linked-list in Java.
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
 * [1] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 *
 */


import java.util.Arrays;
import java.util.Random;
import java.util.Iterator;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.function.Function;


public final class List<T extends Comparable<T>> implements Iterable<T>
{


	private static final class Node<T>
	{


		/* Component(s) */


		// reference to the next node
		private Node<T> next;
		// copy of the data (object of) type T
		private final T data;
		// functional interface of the constructor of the data (object of) type T
		private final Supplier<T> supplier;


		/* Constructor(s) */


		Node (Supplier<T> supplier)
		/*

		Synopsis:
		Constructs a node from the functional interface of the constructor of the
		data (object of) type T.

		*/
		{
			this.next = null;
			this.data = supplier.get();
			this.supplier = supplier;
		}


		Node (Node<T> node)
		// copy constructor
		{
			this.next = null;
			this.data = node.supplier.get();
			this.supplier = node.supplier;
		}


		/* Getter(s) */


		T get()
		// returns a copy of the data object of type T by invoking its constructor
		{
			return this.supplier.get();
		}
	}


	static class Iterator<T> implements java.util.Iterator<T>
	{


		/* Component(s) */


		private Node<T> iter;	// iterator of generic nodes


		/* Constructor(s) */


		Iterator (Node<T> node)
		// constructs a List::Iterator<T> from a node
		{
			this.iter = node;
		}


		/* Method(s) */


		@Override
		public boolean hasNext()
		// returns true if there is a next node, returns false otherwise
		{
			return (this.iter != null);
		}


		@Override
		public T next()
		// returns a copy of stored data object of type T and advances iterator
		{
			T data = this.iter.get();
			this.iter = this.iter.next;
			return data;
		}
	}


	/* Component(s) */


	private Node<T> head;
	private Node<T> tail;
	private int size;


	/* Constructor(s) */


	List ()
	// default constructor, creates an empty list
	{
		this.head = null;
		this.tail = null;
		this.size = 0;
	}


	List (List<T> list)
	// copy constructor
	{
		if (list.head == null)
		// creates an empty new list if the list is empty
		{
			this.head = null;
			this.tail = null;
			this.size = 0;
		}
		else
		{
			// creates head node via copy constructor
			this.head = new Node<>(list.head);
			this.tail = this.head;

			Node<T> iter = list.head.next;
			while (iter != null)
			// copies the nodes of list into the new list
			{
				this.tail.next = new Node<>(iter);
				this.tail = this.tail.next;
				iter = iter.next;
			}

			// sets the size of the new list
			this.size = list.size;
		}
	}


	/* Method(s) */


	@Override
	public Iterator<T> iterator ()
	// returns a List::Iterator<T>
	{
		return ( new Iterator<T>(this.head) );
	}


	public int size ()
	// returns the size of the list (or equivalently, the number of elements stored)
	{
		return this.size;
	}


	public T get (int pos)
	// returns a copy of the stored data at position
	{

		if (this.head == null)
		// complains if the list is empty
		{
			String errmsg = "List<T>::get(): emptyListError";
			throw new IllegalArgumentException(errmsg);
		}


		if (pos < 0 || pos >= this.size)
		// complains if the position index is invalid
		{
			String errmsg = "List<T>::get(): outOfBoundsError";
			throw new IllegalArgumentException(errmsg);
		}


		if ( pos == 0 )
		// returns a copy of the data stored in the head node
		{
			return ( this.head.get() );
		}


		if ( pos == (this.size - 1) )
		// returns a copy of the data stored in the tail node
		{
			return ( this.tail.get() );
		}


		int i = 0;
		Node<T> iter = this.head;
		while (i != pos)
		// advances iterator to the requested position
		{
			iter = iter.next;
			++i;
		}

		// returns a copy of the data stored in the requested node
		return ( iter.get() );
	}


	public void insort (Supplier<T> supplier)
	// insertion sort method
	{
		// creates a copy of the data
		Node<T> node = new Node<>(supplier);
		T data = node.get();

		if (this.head == null)
		// if the list is empty
		{
			this.create(supplier);
			return;
		}


		if (data.compareTo(this.head.data) <= 0)
		// if less than or equal to the first element in the list
		{
			this.push_front(supplier);
			return;
		}


		if (data.compareTo(this.tail.data) >= 0)
		// if greater than or equal to the last element in the list
		{
			this.push_back(supplier);
			return;
		}


		// otherwise, searches linearly for the insertion location
		this.insert(supplier);
		return;
	}


	public void insort (Supplier<T> supplier, Comparator<T> comparator)
	// overloads the insertion sort method with a comparator
	{
		// creates a copy of the data
		Node<T> node = new Node<>(supplier);
		T data = node.get();

		if (this.head == null)
		// if the list is empty
		{
			this.create(supplier);
			return;
		}


		if (comparator.compare(data, this.head.data) <= 0)
		// if less than or equal to the first element in the list
		{
			this.push_front(supplier);
			return;
		}


		if (comparator.compare(data, this.tail.data) >= 0)
		// if greater than or equal to the last element in the list
		{
			this.push_back(supplier);
			return;
		}


		//otherwise, searches linearly for the insertion location
		this.insert(supplier, comparator);
		return;
	}


	public boolean contains (T target)
	// returns true if the target is in the list, returns false otherwise
	{
		T t = target;
		if (this.search(t) < 0)
			return false;
		else
			return true;
	}


	public int search (T target)
	/*

	Synopsis:
	Returns the index of the node containing the target, otherwise
	returns an invalid index to indicate that the target is not in
	the list.

	*/
	{
		T t = target;
		return this.linearSearch(t);
	}


	public T[] toArray(Function<Integer, T[]> allocator)
	// returns an array containing a copy of the stored data
	{

		// allocates an array of suitable storage capacity
		T[] data = allocator.apply(this.size);

		Node<T> iter = this.head;
		for (int i = 0; i != this.size; ++i)
		// copies stored data into array
		{
			data[i] = iter.get();
			iter = iter.next;
		}

		return data;
	}


	public List<T> sublist (int begin, int end)
	// creates a sublist containing the elements in the range [begin, end)
	{
		// complains if the asymmetric range [b, e) is invalid
		if (begin < 0)
		{
			String err = ("List<T>::sublist(): invalid `begin' value given");
			throw new IllegalArgumentException(err);
		}

		if ( end > this.size() )
		{
			String err = ("List<T>::sublist(): invalid `end' value given");
			throw new IllegalArgumentException(err);
		}

		if (this.size == 0)
		{
			// returns an empty list for this list is empty
			return ( new List<>() );
		}

		if (begin >= end)
		{
			// returns an empty list for the range [b, e) is empty
			return ( new List<>() );
		}

		Node<T> iter = this.head;
		Node<T> next = iter.next;
		for (int i = 0; i != begin; ++i)
		// advances iterator to the begin position
		{
			iter = iter.next;
			next = next.next;
		}


		Node<T> head = new Node<>(iter);
		Node<T> tail = head;
		for (int i = begin; i != (end - 1); ++i)
		// copies elements in the range [begin, end) into the sublist
		{
			tail.next = new Node<>(next);
			tail = tail.next;
			next = next.next;
		}


		// creates the sublist
		List<T> sublist = new List<>();
		sublist.head = head;
		sublist.tail = tail;
		sublist.size = (end - begin);

		return sublist;
	}


	/*
	public List<T> bisect ()
	// bisects list into two partitions, returns the second partition
	{

		//isDivisible();	// complains if list cannot be divided

		int i = 0;			// counter
		Node<T> iter = this.head;	// iterator
		Node<T> head = this.head;	// first partition head
		Node<T> tail = this.tail;	// first partition tail
		int size = this.size;		// original list size
		int half = (size / 2);		// first partition size
		while (i != half)
		// advances iterator until the second partition is reached
		{
			tail = iter;
			iter = iter.next;
			++i;
		}

		// creates placeholder (list) for the second partition
		List<T> part = new List<>();
		part.head = iter;
		part.tail = this.tail;
		part.size = (size - half);

		// unlinks the first partition from the second partition
		tail.next = null;
		this.head = head;
		this.tail = tail;
		this.size = half;

		return part;
	}
	*/


	/* private methods (used for testing only) */


	private void append (Supplier<T> supplier)
	// appends data at the end of the list
	{
		this.push_back(supplier);
	}


	/* implementations */


	private void push_back (Supplier<T> supplier)
	// pushes coordinate object at the back of the list
	{
		if (this.tail == null)
		{
			this.create(supplier);
		}
		else
		{
			this.tail.next = new Node<>(supplier);
			this.tail = this.tail.next;
			++this.size;
		}
	}


	private void push_front (Supplier<T> supplier)
	// pushes coordinate object at the front of the list
	{
		Node<T> head = this.head;
		this.head = new Node<>(supplier);
		this.head.next = head;
		++this.size;
	}


	private void create (Supplier<T> supplier)
	// creates the first node of the list
	{
		this.head = new Node<>(supplier);
		this.tail = this.head;
		++this.size;
	}


	private void insert (Supplier<T> supplier)
	// inserts data at the location that keeps the ordered structure
	{
		// gets a copy of the data to find the insertion location
		Node<T> node = new Node<>(supplier);
		T data = node.get();

		// creates iterators
		Node<T> iter = this.head;
		Node<T> next = iter.next;
		while (next != null && data.compareTo(next.data) > 0)
		// advances iterators until the insertion location is found
		{
			iter = iter.next;
			next = (next == null)? null : next.next;
		}

		// inserts node at insertion location
		iter.next = node;
		node.next = next;
		++this.size;
	}


	private void insert (Supplier<T> supplier, Comparator<T> comparator)
	// as above but uses user-defined comparator
	{
		// gets a copy of the data to find the insertion location
		Node<T> node = new Node<>(supplier);
		T data = node.get();

		// creates iterators
		Node<T> iter = this.head;
		Node<T> next = iter.next;
		while (next != null && comparator.compare(data, next.data) > 0)
		// advances iterators until the insertion location is found
		{
			iter = iter.next;
			next = (next == null)? null : next.next;
		}

		// inserts node at insertion location
		iter.next = node;
		node.next = next;
		++this.size;
	}


	private int linearSearch (T target)
	// implements linear search
	{
		int invalidIndex = (0xFFFFFFFF);	// sets invalidIndex to -1
		if (this.head == null)
		// if the list is empty there is nothing to search
		{
			return invalidIndex;
		}

		int idx = 0;
		Node<T> iter = this.head;
		while (iter != null)
		// advances iterator, returns index if the target is found
		{
			T data = iter.data;
			if (data.compareTo(target) == 0)
				return idx;

			iter = iter.next;
			++idx;
		}

		// returns an invalid index otherwise
		return invalidIndex;
	}


	/*
	private void isDivisible ()
	// complains if the list cannot be divided into two partitions
	{
		String errmsg = (
			"list.bisect(): list of size: " + this.size +
			"cannot be divided into two partitions"
		);
		if (this.size < 2)
		{
			throw new RuntimeException(errmsg);
		}
	}
	*/


	public static void main (String [] args)
	// tests the methods of the generic linked-list
	{
		testAppendMethod();
		testSearchMethod();
		testInsortMethod();
		testSublistMethod();
		testListIterator();
	}


	private static void testAppendMethod()
	{
		int size = 16;
		Point [] points = new Point[size];
		List<Point> list = new List<>();
		for (int i = 0; i != size; ++i)
		{
			Point point = new Point(i, i);
			points[i] = point;

			/*

			uses a lambda to supply the functional interface of the
			copy constructor of the Point class so that the new node can
			construct copies of the (current) point

			*/

			list.append( () -> new Point(point) );
		}


		int diffs = 0;
		for (int i = 0; i != size; ++i)
		/*

		counts the number of differences between the original data
		and the data stored in the list (we expect none)

		*/
		{
			Point P = list.get(i);
			Point Q = points[i];

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;
		}

		System.out.printf("test-append[0]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testSearchMethod ()
	// tests the search method
	{
		int size = 0x00001000;
		List<Point> list = new List<>();
		Point [] points = new Point[size];
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// fills the list with random points
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point (x, y);
			points[i] = p;
			list.insort( () -> new Point(p) );
		}


		/*

		counts the number of times the search method fails to find
		the target in the list (we expect none for we know that the
		target must be in the list)

		*/


		int fails = 0;
		for (int i = 0; i != size; ++i)
		// counts number of search failures
		{
			Point p = points[i];
			if (list.search(p) < 0)
				fails += 1;
			else
				fails += 0;
		}


		System.out.printf("test-search[0]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		fails = 0;
		for (int i = 0; i != (size - 1); ++i)
		// checks that the list is sorted in ascending order
		{
			Point P = list.get(i);
			Point Q = list.get(i + 1);
			if (P.compareTo(Q) > 0)
				fails += 1;
			else
				fails += 0;

		}


		System.out.printf("test-search[1]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		// copies data in list into array
		points = list.toArray( (Integer sz) -> new Point[sz] );


		int diffs = 0;
		for (int i = 0; i != size; ++i)
		/*

		counts the number of differences between the copied data
		and the data stored in the list (we expect none)

		*/
		{
			Point P = list.get(i);
			Point Q = points[i];

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;
		}

		System.out.printf("test-search[2]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testInsortMethod ()
	{
		int size = 0x00001000;
		List<Point> list = new List<>();
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// fills the list with default, sorted, points
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point(x, y);
			list.insort( () -> new Point(p) );
		}

		// sorts the points according to the comparator
		Point [] points = list.toArray( (Integer sz) -> new Point[sz] );
		Arrays.sort( points, new Point.Comparator() );

		List<Point> sorted = new List<>();
		for (int i = 0; i != points.length; ++i)
		// inserts the points at the back of the list
		{
			Point p = points[i];
			sorted.insort( () -> new Point(p), new Point.Comparator() );
		}


		int n = 0;
		int fails = 0;
		for (Point P : sorted)
		// checks that insort() has preserved the original order
		{
			Point Q = points[n];

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++n;
		}

		System.out.printf("test-sort[0]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-sort[1]: ");
		if (sorted.size() != n)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-sort[2]: ");
		if (sorted.size() != points.length)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-sort[3]: ");
		if ( sorted.size() != list.size() )
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testSublistMethod ()
	{
		int size = 0x00001000;
		List<Point> list = new List<>();
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// fills the list with random points
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point (x, y);
			list.insort( () -> new Point(p) );
		}

		// partitions the list into halves via the sublist method
		List<Point> first = list.sublist(0, size/2);
		List<Point> second = list.sublist(size/2, size);


		System.out.printf("test-sublist[0]: ");
		if (first.size() != size/2)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		int count = 0;
		for (Point P : first)
		{
			++count;
		}


		System.out.printf("test-sublist[1]: ");
		if (first.size() != count)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		for (Point P : second)
		{
			++count;
		}

		System.out.printf("test-sublist[2]: ");
		/*

		checks that the partitioning has been successful - the total
		number of elements in the sublists should be equal to the
		size of the original list

		*/
		if (list.size() != count)
			System.out.println("FAIL");
		else
			System.out.println("pass");

		count = 0;
		int fails = 0;
		Iterator<Point> iter = list.iterator();
		for (Point P : first)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		System.out.printf("test-sublist[3]: ");
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		for (Point P : second)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		System.out.printf("test-sublist[4]: ");
		// checks that we have considered all the elements (again)
		if (list.size() != count)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-sublist[5]: ");
		// reports about data differences (we expect none)
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		List<Point> q1 = list.sublist(0, size/4);
		List<Point> q2 = list.sublist(size/4, 2*size/4);
		List<Point> q3 = list.sublist(2*size/4, 3*size/4);
		List<Point> q4 = list.sublist(3*size/4, size);


		count = 0;
		fails = 0;
		iter = list.iterator();
		for (Point P : q1)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		for (Point P : q2)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		for (Point P : q3)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		for (Point P : q4)
		// checks for data differences (we expect none)
		{
			Point Q = iter.next();

			if (P.compareTo(Q) != 0)
				fails += 1;
			else
				fails += 0;

			++count;
		}


		System.out.printf("test-sublist[6]: ");
		// checks that we have considered all the elements (again)
		if (list.size() != count)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-sublist[7]: ");
		// reports about data differences
		if (fails != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testListIterator ()
	// tests the iterator of the linked-list
	{
		int size = 0x00001000;
		List<Point> list = new List<>();
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// fills the list with random points
		{
			int x = r.nextInt(size), y = r.nextInt(size);
			Point p = new Point (x, y);
			list.insort( () -> new Point(p) );
		}

		int numel = 0;
		Iterator<Point> iter = list.iterator();
		while ( iter.hasNext() )
		// counts the number of elements
		{
			Point p = iter.next();
			++numel;
		}

		System.out.printf("test-iterator[0]: ");
		if (numel != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		// partitions list into a pair of lists each containing half of the data
		List<Point> first = list.sublist( 0, list.size() / 2 );
		List<Point> second = list.sublist( list.size() / 2, list.size() );

		numel = 0;
		iter = first.iterator();
		while ( iter.hasNext() )
		// counts the number of elements in the first partition
		{
			Point p = iter.next();
			++numel;
		}

		iter = second.iterator();
		while ( iter.hasNext() )
		// counts the number of elements in the second partition
		{
			Point p = iter.next();
			++numel;
		}

		System.out.printf("test-iterator[1]: ");
		// checks the total number of elements
		if (numel != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		Point [] points = list.toArray( (Integer sz) -> new Point[sz] );


		int n = 0;
		int diffs = 0;
		for (Point point : first)
		{
			Point P = point;
			Point Q = points[n];

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++n;
		}

		for (Point point : second)
		{
			Point P = point;
			Point Q = points[n];

			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;

			++n;
		}

		System.out.printf("test-iterator[2]: ");
		// checks if the stored data is the same as the original
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		System.out.printf("test-iterator[3]: ");
		// checks that we have traversed all the elements
		if (list.size() != n)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}
}


/*
 * COMMENTS:
 *
 * Class Declaration of the Generic List
 *
 * Even if the actual type T implements Comparable<T> it is necessary to
 * use the `extends' keyword to declare the class of the generic List:
 *
 * 			List<T extends Comparable<T>>
 *
 * as indicated by the stackoverflow user in the following post:
 *
 * 			https://stackoverflow.com/a/19216805
 *
 * Hence, only the types that implement the comparable interface can be
 * stored in the generic list.
 *
 *
 * List Nodes
 *
 * The node stores a copy of the data (object) and the constructor which
 * has the blueprint for creating copies of that data. The list does not
 * return a reference of the stored data to protect the data from changes
 * applied elsewhere. Returning a copy of a generic type is tricky because
 * there is no type information available at runtime for generics in Java.
 * (Thus, the constructor of the generic type T cannot be invoked directly
 * as in C++.) A possible solution is to supply a functional interface of
 * the constructor, as we have done here. I have borrowed ideas from the
 * following stackoverflow post to implement the generic linked-list:
 *
 * 			https://stackoverflow.com/a/36315051
 *
 *
 * Iterator
 *
 * Borrowed ideas from the following Geeks-for-Geeks article to implement
 * iterator for the generic linked-list:
 *
 * https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/
 *
 *
 * toArray
 * Due to the issues associated with the erasure of the type of generics in
 * Java, the toArray() method expects the user to supply the constructor to
 * to create an array of type T. There are other alternatives which could
 * be applied, as seen in this stackoverflow post on the creation of
 * generic arrays:
 *
 * stackoverflow.com/questions/529085/how-to-create-a-generic-array-in-java
 *
 */
