// 2D Point Class
public class Point implements Comparable<Point>
{
  // data members:

  private int x;			// x-axis coordinate
  private int y;			// y-axis coordinate

  // constructors:

  Point ()				// default constructor
  {
    this.x = 0;
    this.y = 0;
  }

  Point (int x, int y)			// constructs from (x, y) coordinates
  {
    this.x = x;
    this.y = y;
  }

  Point (Point p)			// copy constructor
  {
    this.x = p.x;
    this.y = p.y;
  }

  // getters:

  public int getX ()			// returns copy of the x-axis coordinate
  {
    return this.x;
  }

  public int getY ()			// returns copy of the y-axis coordinate
  {
    return this.y;
  }

  // methods:

  @Override
  public int compareTo (Point point)	// implements comparable interface
  {
    if (this.x != point.x)
    {
      return (this.x - point.x);
    }
    else
    {
      return (this.y - point.y);
    }
  }

  public double distance (Point point)	// returns squared separation distance
  {
    final double x1 = this.x;
    final double y1 = this.y;
    final double x2 = point.x;
    final double y2 = point.y;
    return ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
  }

// public static double distance(Point point1, Point point2)
//
// Synopsis:
// Returns the squared distance of a pair of points.
//
// Inputs:
// point1	first particle
// point2	second particle
//
// Output:
// d		squared separation distance
//
// NOTES:
// Omits the computation of the squared root for speed. And uses double precision floating
// point numbers to represent the point coordinates to avert (potential) overflows
// owing to the limited range of numbers that can be represented with 32-bit signed
// integers (just for the sake of being cautious).

  public static double distance (Point point1, Point point2)
  {
    final double x1 = point1.x;
    final double y1 = point1.y;
    final double x2 = point2.x;
    final double y2 = point2.y;
    return ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
  }
}


/*

Algorithms and Complexity                            October 06, 2022
IST 4310
Prof. M. Diaz-Maldonado

Synopsis:
Possible implementation of a 2D Point Class.

Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
[1] www.codejava.net/java-core/collections/sorting-arrays-examples-with-comparable-and-comparator

*/
