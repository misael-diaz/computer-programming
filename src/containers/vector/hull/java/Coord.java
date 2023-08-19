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
    {
      return (this.x - coord.x);
    }
    else
    {
      return (this.y - coord.y);
    }
  }


// public static double distance(Coord coord1, Coord coord2)
//
// Synopsis:
// Returns the squared distance of a pair of particles.
//
// Inputs:
// coord1	coordinates of the first particle
// coord2	coordinates of the second particle
//
// Output:
// d		squared separation distance
//
// NOTES:
// Omits the computation of the squared root for speed. And uses double precision floating
// point numbers to represent the particle coordinates to avert (potential) overflows
// owing to the limited range of numbers that can be represented with 32-bit signed
// integers (just for the sake of being cautious).


  public static double distance (Coord coord1, Coord coord2)
  {
    final double x1 = coord1.x;
    final double y1 = coord1.y;
    final double x2 = coord2.x;
    final double y2 = coord2.y;
    return ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
  }
}


/*

Algorithms and Complexity                            October 06, 2022
IST 4310
Prof. M. Diaz-Maldonado

Synopsis:
Possible implementation of a 2D Cartesian Coordinates Class.

Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
[1] www.codejava.net/java-core/collections/sorting-arrays-examples-with-comparable-and-comparator

*/
