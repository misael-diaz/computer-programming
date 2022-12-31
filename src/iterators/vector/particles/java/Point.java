/*
 * Algorithms and Complexity                            October 06, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines an immutable Point Class of two dimensions.
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

public final class Point implements Comparable<Point>
// 2D Cartesian Point Class
{

	/* components */


	private final double x;		// x-axis coordinate
	private final double y;		// y-axis coordinate


	/* constructors */


	Point ()
	// default constructor
	{
		this.x = 0.0;
		this.y = 0.0;
	}


	Point (double x, double y)
	// constructs a point from x, y coordinates
	{
		this.x = x;
		this.y = y;
	}


	Point (Point p)
	// copy constructor
	{
		this.x = p.x;
		this.y = p.y;
	}


	/* getters */


	public double getX ()
	// returns a copy of the x-axis coordinate
	{
		return this.x;
	}


	public double getY ()
	// returns a copy of the y-axis coordinate
	{
		return this.y;
	}


	/* methods */


	public void print ()
	// prints the (x, y) coordinates on the console
	{
		System.out.printf("x: %f, y: %f\n", this.x, this.y);
	}


	@Override
	public int compareTo (Point point)
	/*

	Synopsis:
	Implements the comparable interface for sorting an array of points
	primarily with respect to the x-axis coordinate and secondarily
	with respect to the y-axis coordinate. For brevity, we refer to
	this sorting as x-y sorting.

	Input:
	point	a point object (referred to as that point)

	Output:
	comp	0 if `this point' is equal to `that point',
		1 if `this point' is greater than `that point',
		and -1 if `this point' is less than `that point'

	*/
	{
		if (this.x != point.x)
		// compares points by their x-axis coordinates
		{
			return compare(this.x, point.x);
		}
		else
		// otherwise compares by their y-axis coordinates
		{
			return compare(this.y, point.y);
		}
	}


	public static class Comparator implements java.util.Comparator<Point>
	/*

	Defines an auxiliary class for y-x sorting of an array of points.
	This means that an array of points is sorted primarily with respect
	to the y-axis coordinate and secondarily with respect to the x-axis
	coordinate. For brevity, we refer to this as y-x sorting.

	*/
	{
		@Override
		public int compare (Point P, Point Q)
		// implements the comparator interface for y-x sorting
		{
			if (P.y != Q.y)
				return Point.compare(P.y, Q.y);
			else
				return Point.compare(P.x, Q.x);
		}
	}


	public static double distance (Point P, Point Q)
	/*

	Synopsis:
	Returns the squared distance of a pair of points (P, Q).

	Inputs:
	P		the Pth point
	Q		the Qth point

	Output:
	d		squared distance of the points

	COMMENTS:
	Omits the computation of the squared root for speed. And uses
	doubles to represent the coordinates to avert overflows owing to
	the limited range of numbers that can be represented with the
	signed integer type.

	*/
	{
		return (
			(P.x - Q.x)*(P.x - Q.x) + (P.y - Q.y)*(P.y - Q.y)
		);
	}


	/* implementation(s) */


	private static int compare (double x1, double x2)
	/*

	Synopsis:
	Defines what it means to compare the x (or y) axis coordinates
	of a pair of points.

	Inputs:
	x1	the x (or y) axis coordinate of the first point
	x2	the x (or y) axis coordinate of the second point

	Output:
	comp	0 if x1 == x2, -1 if x1 < x2, and 1 if x1 > x2

	*/
	{
		if (x1 == x2)
		{
			return 0;
		}
		else if (x1 < x2)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
