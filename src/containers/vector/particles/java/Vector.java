/*
 * Algorithms and Complexity                            October 06, 2022
 * IST 4310
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
 * [1] Sorting Custom Objects Tutorial: (
 * 	www.codejava.net/java-core/collections/
 * 	sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */

import java.util.Arrays;
import java.util.Random;

public class Vector
{

	/* initializes parameters (not bound to objects of the class) */


	private static final int DEFAULT_SIZE_LIMIT = (0x00000010);


	/* defines private data members */


	private int begin;	// beginning of the placeholder (0)
	private int avail;	// available location for writing
	private int limit;	// size limit for storing data
	private Coord [] data;	// data placeholder


	/* constructors */


	Vector ()
	// default constructor: creates a vector of default size limit
	{
		this.begin = 0;
		this.avail = 0;
		this.limit = DEFAULT_SIZE_LIMIT;
		this.data  = new Coord [limit];
	}


	Vector (int size)
	// constructs a vector with requested capacity for storage
	{
		this.begin = 0;
		this.avail = 0;
		this.limit = size;
		this.data  = new Coord [size];
	}


	/* getters */


	public Coord [] getData ()
	// returns a clone of the data contained in vector
	{
		return this.data.clone();
	}


	/* methods */


	public void clear ()
	// effectively clears the vector elements
	{
		this.avail = 0;
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


	public void push_back (Coord x)
	// pushes coordinates unto the back of vector
	{
		back_inserter (this, x);
	}


	public void sort ()
	// delegates the task of sorting to the sort method of Arrays
	{
		Arrays.sort (this.data);
	}


	/* implementations */


	private void back_inserter (Vector v, Coord x)
	// pushes data into the back of vector
	{
		int avail = v.avail;	// gets vector size
		int limit = v.limit;	// gets vector size limit

		if (avail == limit)	// checks if there's space left
			grow (v);	// doubles the vector size limit

		v.data[avail] = x;	// writes at available location
		++v.avail;		// increments vector size
	}


	private void grow (Vector v)
	// doubles the vector size
	{
		int limit = v.limit;		// gets current size limit
		Coord [] tmp = v.data.clone();	// copies data to temporary

		v.limit *= 2;			// doubles size limit
		v.data = new Coord[v.limit];	// doubles allocation
		for (int i = 0; i != limit; ++i)// restores data
			v.data[i] = tmp[i];
	}



	public static void main (String[] args)
	// tests the methods of the vector class
	{
		testPushBackMethod();
		testClearMethod();
		testSortMethod();
		return;
	}


	/* tests */


	private static void testPushBackMethod ()
	/*

	Synopsis:
	Pushes coordinates unto the back of the vector and checks its size.

	*/
	{
		int size = 32;
		Vector vector = new Vector ();
		for (int i = 0; i != size; ++i)
			vector.push_back ( new Coord(i, i) );

		System.out.printf("push-back-method-test: ");
		// checks the vector size against the expected size
		if (vector.size() != size)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testClearMethod ()
	/*

	Synopsis:
	Pushes coordinates unto the back of the vector, clears it, and
	checks that it is empty.

	*/
	{
		int size = 32;
		Vector vector = new Vector ();
		for (int i = 0; i != size; ++i)
			vector.push_back ( new Coord(i, i) );


		vector.clear ();	// clears vector


		System.out.printf("clear-method-test: ");
		// checks the vector size against the expected size
		if (vector.size() != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}


	private static void testSortMethod ()
	/*

	Synopsis:
	Pushes random coordinates unto the back of the vector, sorts it
	in ascending order, and checks if the sorting was successful.

	*/
	{
		int size = 8;
		Vector vector = new Vector (size);
		Random random = new Random ();
		for (int i = 0; i != size; ++i)
		// fills vector with random coordinates
		{
			int x = random.nextInt (size);
			int y = random.nextInt (size);
			Coord coord = new Coord (x, y);
			vector.push_back (coord);
		}

		vector.sort();	// sorts data contained in vector


		Coord [] data = vector.getData();
		for (int i = 0; i != size; ++i)
		// prints the (sorted) coordinates on the console
		{
			int x = data[i].getX();
			int y = data[i].getY();
			System.out.printf("x: %2d y: %2d\n", x, y);
		}


		int failures = 0;
		for (int i = 0; i != (size - 1); ++i)
		// counts failures (not in ascending order instances)
		{
			Coord thisCoord = data[i], nextCoord = data[i + 1];
			if (thisCoord.compareTo(nextCoord) > 0)
				++failures;
		}


		System.out.printf("sort-method-test: ");
		// checks if the sorting method failed (unexpectedly)
		if (failures != 0)
			System.out.println("FAIL");
		else
			System.out.println("pass");
	}
}
