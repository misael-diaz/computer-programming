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
 * [1] https://www.codejava.net/java-core/collections/sorting-arrays-examples-with-comparable-and-comparator
 *
 */

public final class Coord implements Comparable<Coord> // 2D Cartesian Coordinates Class
{

  // data members:

  private final int x;	// x-axis coordinate
  private final int y;	// y-axis coordinate

  // constructors:

  Coord ()		// default constructor
  {
    this.x = 0;
    this.y = 0;
  }

  Coord (int x, int y)	// constructs from (x, y) coordinates
  {
    this.x = x;
    this.y = y;
  }

  Coord (Coord c)	// copy constructor
  {
    this.x = c.x;
    this.y = c.y;
  }

  // getters:

  public int getX ()	// returns copy of the x-axis coordinate
  {
    return this.x;
  }

  public int getY ()	// returns copy of the y-axis coordinate
  {
    return this.y;
  }

  // methods:

  @Override
  public int compareTo (Coord coord)	// implements comparable interface
  {
    return ( (this.x != coord.x)? (this.x - coord.x) : (this.y - coord.y) );
  }

  // double distance (Coord p, Coord q)
  //
  // Synopsis:
  // Returns the squared distance of a pair of particles (p, q).
  //
  // Inputs:
  // p		coordinates of the pth particle
  // q		coordinates of the qth particle
  //
  // Output:
  // d		squared distance of the pth and qth particles
  //
  // NOTES:
  // Omits the computation of the squared root for speed. And uses
  // doubles to represent the coordinates to avert overflows owing to
  // the limited range of numbers that can be represented with the
  // signed integer type.

  public static double distance (Coord p, Coord q)
  {
    double x_p = p.x, y_p = p.y;// gets the (x, y) coordinates of the pth particle
    double x_q = q.x, y_q = q.y;// gets the (x, y) coordinates of the qth particle
    return ( (x_p - x_q) * (x_p - x_q) + (y_p - y_q) * (y_p - y_q) );
  }
}
