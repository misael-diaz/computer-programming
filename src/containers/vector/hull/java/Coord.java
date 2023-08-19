// 2D Cartesian Coordinates Class
public class Coord implements Comparable<Coord>
{

  // data members:

  private int x;			// x-axis coordinate
  private int y;			// y-axis coordinate

  // constructors:

  Coord ()				// default constructor
  {
    this.x = 0;
    this.y = 0;
  }

  Coord (int x, int y)			// constructs from (x, y) coordinates
  {
    this.x = x;
    this.y = y;
  }

  Coord (Coord c)			// copy constructor
  {
    this.x = c.x;
    this.y = c.y;
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
  public int compareTo (Coord coord)	// implements comparable interface
  {
    if (this.x != coord.x)
      return (this.x - coord.x);
    else
      return (this.y - coord.y);
  }


// public static double distance(Coord i, Coord j)
//
// Synopsis:
// Returns the squared distance of a pair of particles (i, j).
//
// Inputs:
// i		coordinates of the ith particle
// j		coordinates of the jth particle
//
// Output:
// d		squared distance of the ith and jth particles
//
// NOTES:
// Omits the computation of the squared root for speed. And uses
// doubles to represent the coordinates to avert overflows owing to
// the limited range of numbers that can be represented with the
// signed integer type.


  public static double distance (Coord i, Coord j)
  {
    // gets the (x, y) coordinates of the ith particle
    double xi = i.x, yi = i.y;
    // gets the (x, y) coordinates of the jth particle
    double xj = j.x, yj = j.y;
    // computes the squared of the distance of the particles
    return ( (xi - xj) * (xi - xj) + (yi - yj) * (yi - yj) );
  }
}


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
