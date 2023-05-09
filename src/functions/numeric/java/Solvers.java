/*
 * Algorithms and Programming II                               May 01, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Recursive implementation of (some) nonlinear equation solvers.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition.
 * [1] A Gilat and V Subramanian, Numerical Methods for Engineers and
 *     Scientists, 3rd edition.
 * [2] BR Munson et al., Fundamentals of Fluid Mechanics, 8th edition.
 *
 */

import static java.lang.Math.sqrt;
import static java.lang.Math.log10;

public class Solvers
{

  interface Objf		// Objetive Function Interface
  {
    public double f (double x);	// nonlinear function, f(x)
  }


  // defines the nonlinear objective function f(x) as a lambda
  // Note: the Objf has been made static so that it can be accessed by the static methods
  static Objf objf = (double x) -> {
    return (1.0/sqrt(x) + 2.0 * log10(0.024490 / 3.7 + 2.51 / (7812525.1 * sqrt(x) ) ) );
  };


  // solves for the root of a nonlinear function f(x):
  public static void main (String [] args)
  {
    Bisection ();	// applies recursive Bisection Method
    RegulaFalsi ();	// applies recursive Regula Falsi Method
    return;
  }


  // double bisect (double l, double u, Objf objf)
  //
  // Synopsis:
  // Recursive implementation of the Bisection Method.
  //
  // Inputs
  // l		lower bracketing limit
  // u 		upper bracketing limit
  // objf	nonlinear function f(x)
  //
  // Output
  // x		returns root of the nonlinear function f(x)


  public static double bisect (double l, double u, Objf objf)
  {
    hasRoot(l, u, objf);	// complains if the there is no root enclosed in [l, u]

    double tol = 1.0e-12;	// tolerance
    double m = (l + u) / 2;	// middle value
    double f;			// f(x)

    f = (objf.f(m) < 0)? -objf.f(m) : objf.f(m); // gets the absolute value of f(x)

    if (f < tol)		// if it meets the convergence criterion:
    {
      return m;			// uses direct solution
    }
    else			// divides:
    {
      if (objf.f(m) * objf.f(u) < 0.0)
      {
	l = m;			// divides by removing first half
      }
      else
      {
	u = m;			// divides by removing second half
      }
    }

    return bisect(l, u, objf);	// recurses
  }


  // double regfal (double l, double u, Objf objf)
  //
  // Synopsis:
  // Recursive implementation of the Regula Falsi (or False Position) Method.
  //
  // Inputs
  // l		lower bracketing limit
  // u 		upper bracketing limit
  // objf	nonlinear function f(x)
  //
  // Output
  // x		returns root of the nonlinear function f(x)


  public static double regfal (double l, double u, Objf objf)
  {
    hasRoot(l, u, objf); 	// complains if the there is no root enclosed in [l, u]

    double tol = 1.0e-12;	// tolerance

    double fl = objf.f(l), fu = objf.f(u);	// gets f(x_l) and f(x_u)
    double m = (l * fu - u * fl) / (fu - fl); 	// interpolates to get intermediate value 
    double f;			// f(x)

    f = (objf.f(m) < 0)? -objf.f(m) : objf.f(m);// gets the absolute value of f(x)

    if (f < tol)		// if f(x_m) meets the convergence criterion:
    {
      return m;			// uses direct solution
    }
    else			// divides:
    {
      if (objf.f(m) * objf.f(u) < 0.0)
      {
	l = m;			// divides by removing first part
      }
      else
      {
	u = m;			// divides by removing second part
      }
    }

    return regfal (l, u, objf);	// recurses
  }


  private static void hasRoot (double l, double u, Objf objf)
  {
    // complains if the bracketing interval [l, u] does not bracket a root
    if ( objf.f(l) * objf.f(u) > 0.0 )
    {
      String e = "solver(): expects a root in the interval [l, u]";
      throw new IllegalArgumentException(e);
    }
  }


  private static void Bisection ()
  {
    // solves for the root of f(x) with the Bisection Method

    System.out.println("\nBisection Method:\n");
    // defines the bracketing interval lower and upper limits
    double l = 2.0e-2, u = 6.0e-2;
    // solves for the root of f(x) recursively
    double r = bisect (l, u, objf);

    // reports the location of the root on the console
    System.out.printf("root: %.12f\n", r);
    System.out.printf("f(x): %.12e\n", objf.f(r) );

    return;
  }


  private static void RegulaFalsi ()
  {
    // solves for the root of f(x) with the Regula Falsi Method

    System.out.println("\nRegula Falsi Method:\n");
    // defines the bracketing interval lower and upper limits
    double l = 2.0e-2, u = 6.0e-2;
    // solves for the root of f(x) recursively
    double r = regfal (l, u, objf);

    // reports the location of the root on the console
    System.out.printf("root: %.12f\n", r);
    System.out.printf("f(x): %.12e\n", objf.f(r) );

    return;
  }
}
