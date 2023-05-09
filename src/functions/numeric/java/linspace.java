/*
 * Algorithms and Programming II                          February 15, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of the linspace function in Java.
 *
 *
 * Sinopsis:
 * Posible implementacion de la function linspace en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] www.javatpoint.com/how-to-return-an-array-in-java
 * [1] www.javatpoint.com/throw-keyword
 */


class Numerics
{
  public static void main (String[] args)
  {
    System.out.println("\nFunction Example:\n");

    double xi = 0;				// initial array value
    double xf = 7;				// final array value
    int numel = 8;				// number of elements in the array
    double x[] = linspace (xi, xf, numel);

    // displays the elements of the array on the console
    System.out.println("prints array x = 0, 1, 2, ..., 6, 7:");
    for (int i = 0; i != numel; ++i)
      System.out.printf("%.6f\n", x[i]);
  }


  // double[] linspace (double xi, double xf, int numel)
  //
  // Synopsis:
  // Implements the linspace function.
  //
  // Inputs:
  // xi		initial value of the array
  // xf		final value of the array
  // numel	number of elements of the array
  //
  // Output:
  // x		array [xi, xf] with equally spaced elements


  public static double[] linspace (double xi, double xf, int numel)
  {

    if (numel <= 1)	// caters invalid number of elements
    {
      throw new RuntimeException("number of elements must be greater than one");
    }

    double dx  = (xf - xi) / ( (double) (numel - 1) );
    double x[] = new double [numel];

    x[0] = xi;
    for (int i = 1; i != numel; ++i)
      x[i] = x[i-1] + dx;

    return x;
  }
}


/*
 * COMMENTS:
 * [0] It's a good practice to validate the user input by throwing
 *     exceptions that inform the user about what the problem is.
 *
 */
