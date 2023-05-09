/*
 * Algorithms and Programming II                          February 20, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of some integrators as functions in Java.
 *
 *
 * Sinopsis:
 * Posible implementacion de algunos integradores como functions en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 */


class Integrators
{
  public static void main (String[] args)
  {
    System.out.println("\nFunction Example:\n");

    double xl = 0;	// lower integration limit
    double xu = 1;	// upper integration limit
    int N = 255;	// number of integration intervals

    // applies the integrators to obtain estimates of the area under the curve
    System.out.println("Integral of f(x) = exp(x) in [0, 1]:");
    System.out.printf ("via left  Riemann sum: %.12f\n", lsum(xl, xu, N));
    System.out.printf ("via right Riemann sum: %.12f\n", rsum(xl, xu, N));
    System.out.println("");
  }


  public static double objf (double x)
  {
    // defines the objective function, f(x) = exp(x), invoked by the integrators
    return Math.exp(x);
  }


  // double lsum (double xl, double xu, int N)
  //
  // Synopsis:
  // Implements the left Riemann sum as a function.
  //
  // Inputs:
  // xl		lower integration limit
  // xu		upper integration limit
  // N		number of integration intervals
  //
  // Output:
  // integral	integral of the objective function f(x)


  public static double lsum (double xl, double xu, int N)
  {
    double x;
    double integral = 0;
    double dx = (xu - xl) / ( (double) N );

    for (int i = 0; i != N; ++i)
    {
      x = xl + ( (double) i ) * dx;
      integral += objf(x);
    }

    return (dx * integral);
  }


  // double rsum (double xl, double xu, int N)
  //
  // Synopsis:
  // Implements the right Riemann sum as a function.
  //
  // Inputs:
  // xl		lower integration limit
  // xu		upper integration limit
  // N		number of integration intervals
  //
  // Output:
  // integral	integral of the objective function f(x)


  public static double rsum (double xl, double xu, int N)
  {
    double x;
    double integral = 0;
    double dx = (xu - xl) / ( (double) N );

    for (int i = 1; i != (N + 1); ++i)
    {
      x = xl + ( (double) i ) * dx;
      integral += objf(x);
    }

    return (dx * integral);
  }
}
