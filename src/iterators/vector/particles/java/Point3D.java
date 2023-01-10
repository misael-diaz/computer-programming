/*
 * Algorithms and Complexity                               January 09, 2023
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines an immutable 3D Point Class.
 *
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
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

public final class Point3D extends Point
// 3D Cartesian Point Class
{

	/* components */


	private final double z;		// z-axis position


	/* constructors */


	Point3D ()
	// default constructor
	{
		super();
		this.z = 0.0;
	}


	Point3D (double x, double y, double z)
	// constructs a 3D Point from x, y, and z coordinates
	{
		super(x, y);
		this.z = z;
	}


	Point3D (Point3D point)
	// copy constructor
	{
		super(point);
		this.z = point.z;
	}


	/* getters */


	public double getZ ()
	// returns the z-axis position
	{
		return this.z;
	}


	/* methods */


	@Override
	public void print ()
	// prints the x, y, z coordinates on the console
	{
		String fmt = ("x: %+e y: %+e z: %+e\n");
		System.out.printf( fmt, this.getX(), this.getY(), this.getZ() );
	}


	@Override
	public double distance (Point point)
	// returns the squared distance between a pair of 3D Point objects
	{
		// complains at runtime if given a base Point object
		Point3D p = (Point3D) point;

		double x1 = this.getX(), x2 = p.getX();
		double y1 = this.getY(), y2 = p.getY();
		double z1 = this.getZ(), z2 = p.getZ();

		// computes the squared distance between the points
		double sqDist = (x2 - x1) * (x2 - x1) +
				(y2 - y1) * (y2 - y1) +
				(z2 - z1) * (z2 - z1);

		return sqDist;
	}


	@Override
	public int compareTo (Point point)
	// overrides the comparable interface of the base Point class
	{
		// complains when comparing a base point with a 3D point
		Point3D p = (Point3D) point;

		if ( this.getX() != p.getX() )
		{
			return compare( this.getX(), p.getX() );
		}
		else if ( this.getY() != point.getY() )
		{
			return compare( this.getY(), p.getY() );
		}
		else
		{
			return compare ( this.getZ(), p.getZ() );
		}
	}


	public static class yPosComparator implements java.util.Comparator<Point>
	/*

	Synopsis:
	Defines auxiliary class for yxz sorting.

	Note:
	Implements Comparator<Point> because we use a user-defined Vector<Point>
	to store the 3D Point objects.

	*/
	{
		@Override
		public int compare (Point p, Point q)
		// implements the compare method
		{
			// complains if points belong to the base Point class
			Point3D P = (Point3D) p;
			Point3D Q = (Point3D) q;

			if ( P.getY() != Q.getY() )
			{
				return Point.compare( P.getY(), Q.getY() );
			}
			else if ( P.getX() != Q.getX() )
			{
				return Point.compare( P.getX(), Q.getX() );
			}
			else
			{
				return Point.compare( P.getZ(), Q.getZ() );
			}
		}
	}


	public static class zPosComparator implements java.util.Comparator<Point>
	// defines auxiliary class for zxy sorting
	{
		@Override
		public int compare (Point p, Point q)
		// implements the compare method
		{
			Point3D P = (Point3D) p;
			Point3D Q = (Point3D) q;

			if ( P.getZ() != Q.getZ() )
			{
				return Point.compare( P.getZ(), Q.getZ() );
			}
			else if ( P.getX() != Q.getX() )
			{
				return Point.compare( P.getX(), Q.getX() );
			}
			else
			{
				return Point.compare( P.getY(), Q.getY() );
			}
		}
	}
}
