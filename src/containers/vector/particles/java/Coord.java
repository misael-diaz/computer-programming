/*
 * Algorithms and Complexity                            October 06, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a 2D Cartesian Coordinates Class.
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
 *      www.codejava.net/java-core/collections/
 *      sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */

public class Coord implements Comparable<Coord>
// 2D Cartesian Coordinates Class
{

	/* data members */

	private int x;		// x-axis coordinate
	private int y;		// y-axis coordinate

	/* constructors */

	Coord ()		// default constructor
	{
		this.x = 0;
		this.y = 0;
	}

	Coord (int xi, int yi)	// constructs from (xi, yi) coordinates
	{
		this.x = xi;
		this.y = yi;
	}

	/* getters */

	public int getX ()	// returns copy of the x-axis coordinate
	{
		return this.x;
	}

	public int getY ()	// returns copy of the y-axis coordinate
	{
		return this.y;
	}

	/* methods */

	@Override
	public int compareTo (Coord coord)
	// implements comparable interface
	{
		if (this.x != coord.x)
			return (this.x - coord.x);
		else
			return (this.y - coord.y);
	}
}
