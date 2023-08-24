public final class Vector
{
  final double x;
  final double y;

  Vector ()
  {
    this.x = 0.0;
    this.y = 0.0;
  }

  Vector (final double x, final double y)
  {
    this.x = x;
    this.y = y;
  }

  Vector (final Point P, final Point Q)
  {
    this.x = P.getX() - Q.getX();
    this.y = P.getY() - Q.getY();
  }

  public double dot (final Vector vector)
  {
    final double x1 = this.x;
    final double y1 = this.y;
    final double x2 = vector.x;
    final double y2 = vector.y;
    return ( x1 * x2 + y1 * y2 );
  }

  public static Vector prod (final Vector U, final double c)
  {
    final double x = c * U.x;
    final double y = c * U.y;
    return new Vector(x, y);
  }

  public static Vector sub (final Vector U, final Vector V)
  {
    final double x = U.x - V.x;
    final double y = U.y - V.y;
    return new Vector(x, y);
  }

  // yields ||U|^2 * Vt|^2, where Vt is the transversal component of V with respect to U
  public static double trans (final Vector U, final Vector V)
  {
    Vector VI = prod(V, U.dot(U));	// V .dot. I, where I is the Identity tensor
    Vector VUU = prod(U, V.dot(U));	// V .dot. UU dyad
    Vector T = sub(VI, VUU);		// transversal component of V with respect to U
    return T.dot(T);			// squared magnitude of the transversal vector
  }


  public static void main (String [] args)
  {
    test();
    test1();
    test2();
  }


  private static void test ()
  {
    Vector V = new Vector(1, 1);
    System.out.print("test[0]: ");
    if (trans(V, V) != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("PASS");
    }
  }


  private static void test1 ()
  {
    Vector U = new Vector(0, 1);
    Vector V = new Vector(1, 0);
    System.out.print("test[1]: ");
    if (trans(U, V) != 1)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("PASS");
    }
  }


  private static void test2 ()
  {
    Vector U = new Vector(+1, +1);
    Vector V = new Vector(+1, -1);
    // V and U are orthogonal so the expected result is the following
    final double res = ( U.dot(U) * U.dot(U) ) * V.dot(V);
    System.out.print("test[2]: ");
    if (trans(U, V) != res)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("PASS");
    }
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
