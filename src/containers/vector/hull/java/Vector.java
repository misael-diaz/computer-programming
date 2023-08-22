public final class Vector
{
  final double x;
  final double y;

  Vector (Coord P, Coord Q)
  {
    this.x = P.getX() - Q.getX();
    this.y = P.getY() - Q.getY();
  }

  double dot (Vector vector)
  {
    final double x1 = this.x;
    final double y1 = this.y;
    final double x2 = vector.x;
    final double y2 = vector.y;
    return ( x1 * x2 + y1 * y2 );
  }
}

/*

Algorithms and Complexity					August 22, 2023

source: Vector.java
author: @misael-diaz

Synopsis:
Possible implementation of a (math) vector.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

*/
