
public class Line
{

  // data members:

  private int x1, y1;		// coordinates of start point
  private int x2, y2;		// coordinates of end point
  private int dx, dy;		// differences dx = (x2 - x1)

  // constructors:

  Line ()			// default constructor
  {
    this.x1 = 0;
    this.y1 = 0;
    this.x2 = 1;
    this.y2 = 1;
    this.dx = 1;
    this.dy = 1;
  }

  Line (Coord P, Coord Q)	// constructs a line from a pair of points
  {
    // complains if P == Q, for P != Q to create a line
    isInvalidInput(P, Q);

    int x1 = P.getX(), y1 = P.getY();
    int x2 = Q.getX(), y2 = Q.getY();
    if (x2 >= x1)
    {
      this.x1 = x1;
      this.x2 = x2;

      this.y1 = y1;
      this.y2 = y2;
    }
    else
    {
      this.x1 = x2;
      this.x2 = x1;

      this.y1 = y2;
      this.y2 = y1;
    }
    this.dx = (this.x2 - this.x1);
    this.dy = (this.y2 - this.y1);
  }

  Line (Line ln)		// copy constructor
  {
    this.x1 = ln.x1;
    this.y1 = ln.y1;
    this.x2 = ln.x2;
    this.y2 = ln.y2;
    this.dx = ln.dx;
    this.dy = ln.dy;
  }

  // getters:

  public int getdX()
  {
    return this.dx;
  }

  public int getdY()
  {
    return this.dy;
  }

  // methods:

  // returns 0 if point is on the line, 1 if above, and -1 if below
  public int loc (Coord c)
  {
    int x = c.getX(), y = c.getY();
    int diff = dx * (y - y1) + dy * (x1 - x);
    return Integer.signum(diff);
  }

  // implementations:

  // complains if P == Q, cannot create line from a point
  private static void isInvalidInput (Coord P, Coord Q)
  {
    if (P.compareTo(Q) == 0)
    {
      String errmsg = ("Line(P, Q) constructor cannot create line from a point (P == Q)");
      throw new IllegalArgumentException(errmsg);
    }
  }
}
/*
 * Algorithms and Complexity                            October 19, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines the Line Class used to identify edges of the Convex Hull.
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
