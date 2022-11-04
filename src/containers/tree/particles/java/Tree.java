/*
 * Algorithms and Complexity                            October 31, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a tree of 2D Cartesian coordinates in Java.
 * The Cartesian coordinates are stored in a sorted array for speed.
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
 * [1] Sorting Custom Objects Tutorial: (
 * 	www.codejava.net/java-core/collections/
 * 	sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */

import java.util.Arrays;
import java.util.Random;

public class Tree
{

	/* initializes parameters (not bound to objects of the class) */


	private static final int DEFAULT_SIZE_LIMIT = (0x00000010);


	/* defines private data members */


	private int begin;	// beginning of the placeholder (0)
	private int avail;	// available location for writing
	private int limit;	// size limit for storing data
	private Coord [] data;	// data placeholder


	/* constructors */


	Tree ()
	// default constructor: creates a tree of default size limit
	{
		this.begin = 0;
		this.avail = 0;
		this.limit = DEFAULT_SIZE_LIMIT;
		this.data  = new Coord [limit];
	}


	Tree (int size)
	// constructs a tree with requested capacity for storage
	{
		this.begin = 0;
		this.avail = 0;
		this.limit = size;
		this.data  = new Coord [size];
	}


	/* copy constructor */


	Tree (Tree tree)
	// constructs a tree that stores a copy of the data of another tree
	{
		this.begin = 0;			// sets to default
		this.avail = tree.size();	// gets the size
		this.limit = tree.size();	// fits to size
		this.data  = tree.get();	// gets a copy of the data
	}


	/* getters */


	public Coord [] get ()
	// returns a copy of the data contained in the tree
	{
		return Arrays.copyOfRange(data, begin, avail);
	}


	public Coord get (int i)
	// returns a copy of the ith element
	{
		return ( new Coord(this.data[i]) );
	}


	/* methods */


	public void clear ()
	// effectively clears the tree elements
	{
		this.avail = 0;
	}


	public int size ()
	// returns the number of elements stored in the tree
	{
		return (this.avail - this.begin);
	}


	public int capacity ()
	// returns the storage capacity of the tree
	{
		return (this.limit - this.begin);
	}


	public void insert (Coord x)
	// inserts data while keeping the ordered structure of the tree
	{
		this.inserter(x);
	}


	public Tree bisect ()
	// bisects tree, keeps first partition and returns second partition
	{
		/*

		Computes the sizes of the first and second partitions
		(or halves); note that these differ in size when the
		(original) tree contains an odd number of elements.

		*/

		int size = this.size();			// original size
		int partSize1 = (size / 2);		// first-half size
		int partSize2 = (size - partSize1);	// second-half size

		// creates the (fitted-to-size) returned tree
		Tree t = new Tree(partSize2);
		// copies data in second half into the returned tree
		t.data = Arrays.copyOfRange(this.data, partSize1, size);

		// effectively deletes second half from (original) tree
		this.avail = partSize1;

		// sets the size of the returned tree
		t.avail = partSize2;
		// returns the tree that contains the second half
		return t;
	}


	public int search (Coord key)
	// delegates the task to the Binary Search method of Arrays
	{
		return Arrays.binarySearch (data, begin, avail, key);
	}


	public void print ()
	// prints the (x, y) coordinates on the console
	{
		int size = this.size();
		for (int i = 0; i != size; ++i)
		{
			Coord c = this.data[i];
			String fmt = ("x, y = (%d, %d)\n");
			System.out.printf(fmt, c.getX(), c.getY());
		}
	}


	/* implementations */


	private void inserter (Coord x)
	// inserts data at the location that keeps the ordered structure
	{


		/* gets insertion location via Arrays.binarySearch() */


		int loc = this.search(x);
		if (loc < 0)
		/*

		inserts if not already present (note: ignores duplicates)

		*/
		{
			// grows the tree if there's no more space left
			if (this.avail == this.limit)
				this.grow();

			// inserts at the insertion location
			this.insort(x, loc);
		}
	}


	private void insort (Coord x, int loc)
	// inserts at the position that preserves the ordered structure
	{
		// gets the insertion position from Arrays.binarySearch()
		int pos = -(1 + loc);
		// computes the size of the data slice to be shifted
		int size = (this.avail - pos);
		for (int i = 0; i != size; ++i)
		// shifts data slice to free the insertion position
		{
			int j = this.avail - (i + 1);
			this.data[j + 1] = this.data[j];
		}

		// inserts data at the (freed) insertion position
		this.data[pos] = x;

		// increments tree size
		++(this.avail);
	}


	private void grow ()
	// doubles the tree size
	{
		// copies stored data into an array temporary
		Coord [] data = this.get();

		/* doubles the allocation and restores the stored data */

		this.limit *= 2;
		this.data = new Coord[this.limit];
		// copies the object references for speed
		for (int i = 0; i != this.avail; ++i)
			this.data[i] = data[i];
	}



	public static void main (String[] args)
	// tests the methods of the tree class
	{
		testInsertionSortMethod();
		testBisectMethod();
		return;
	}


	/* tests */


	private static void testInsertionSortMethod ()
	/*

	Synopsis:
	Inserts coordinates (data) in the tree, checks its size, and
	verifies that the coordinates are sorted in ascending order.

	*/
	{
		int size = 1024;
		Tree tree = new Tree ();
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// stores `size' random coordinates in the tree
		{
			int x = r.nextInt(size);
			int y = r.nextInt(size);
			Coord c = new Coord(x, y);
			while (tree.search(c) >= 0)
			{
				x = r.nextInt(size);
				y = r.nextInt(size);
				c = new Coord(x, y);
			}
			tree.insert(c);
		}


		// checks the tree size against the expected size
		System.out.printf("insert-method-test[0]: ");
		if (tree.size() != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		/* tree.print(); */


		int failures = 0;
		Coord [] data = tree.get();
		for (int i = 0; i != (size - 1); ++i)
		// counts failures (not in ascending order instances)
		{
			Coord thisCoord = data[i], nextCoord = data[i + 1];
			if (thisCoord.compareTo(nextCoord) > 0)
				++failures;
		}


		System.out.printf("insert-method-test[1]: ");
		// checks if the sorting method failed (unexpectedly)
		if (failures != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testBisectMethod ()
	/*

	Synopsis:
	Tests the bisect method of the tree class.
	Stores random coordinates in a tree, bisects the tree in two,
	(each "subtree" (or partition) contains the first and second halves
	of the data stored in the original one), and verifies that the data
	present in the "subtrees" is consistent with that in the original
	tree.

	*/
	{
		int size = 1024;
		Tree tree = new Tree ();
		Random r = new Random();
		for (int i = 0; i != size; ++i)
		// stores `size' random coordinates in the tree
		{
			int x = r.nextInt(size);
			int y = r.nextInt(size);
			Coord c = new Coord(x, y);
			while (tree.search(c) >= 0)
			{
				x = r.nextInt(size);
				y = r.nextInt(size);
				c = new Coord(x, y);
			}
			tree.insert(c);
		}


		/* computes size differences */


		Tree first = new Tree (tree);
		Tree second = first.bisect();
		int numel = ( first.size() + second.size() );
		System.out.printf("bisect-method-test[0]: ");
		if (size != numel)
			System.out.println("FAIL");
		else
			System.out.println("pass");


		/* computes differences in the stored data */


		int diffs = 0;
		for (int i = 0; i != first.size(); ++i)
		{
			Coord P = tree.get(i), Q = first.get(i);
			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;
		}

		for (int i = 0; i != second.size(); ++i)
		{
			Coord Q = second.get(i);
			Coord P = tree.get(first.size() + i);
			if (P.compareTo(Q) != 0)
				diffs += 1;
			else
				diffs += 0;
		}

		System.out.printf("bisect-method-test[1]: ");
		if (diffs != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}
}
