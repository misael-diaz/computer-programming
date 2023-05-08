/*
 * Algorithms and Programming II                             April 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementations of the Fibonacci sequence in Java:
 * 		F(N) = F(N - 1) + F(N - 2), 	F(0) = 0, F(1) = 1
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition, 2007.
 * [1] A Gilat, MATLAB: An Introduction with Applications, 6th edition
 *
 */

import java.util.Random;

class Fibonacci
{
  static long recursions;

  public static void main (String [] args)
  {
    // selects an integer in the asymmetric range [2, 21)
    int min = 2, max = 21;
    Random rand = new Random();
    int N = min + rand.nextInt(max - min);

    // defines format string
    String fmt = ("Fibonacci(%d): %d\n");
    // obtains the factorial iteratively and recursively
    System.out.printf(fmt, N, iterativeFibonacci(N) );
    System.out.printf(fmt, N, recursiveFibonacci(N) );
    return;
  }


  private static int iterativeFibonacci (int N)
  {
    // Synopsis:
    // Iterative computation of the Fibonacci sequence:
    // 	F(N) = F(N - 1) + F(N - 2), 	F(0) = 0, F(1) = 1

    int F = 0;		// F(N)
    int F0 = 0, F1 = 1;	// F(0) and F(1)
    for (int i = 2; i <= N; ++i)
    {
      // F(N) = F(N - 1) + F(N - 2)
      F  = (F1 + F0);
      // advances ``iterators''
      F0 = F1;
      F1 = F;
    }

    return F;
  }


  private static int recursiveFibonacci (int N)
  {
    // Synopsis:
    // Recursive computation of the Fibonacci sequence:
    // 	F(N) = F(N - 1) + F(N - 2), 	F(0) = 0, F(1) = 1

    int F;
    // uses direct solution if N == 0 OR 1, otherwise recurses
    if (N == 0)
    {
      return 0;
    }
    else if (N == 1)
    {
      return 1;
    }
    else
    {
      F = recursiveFibonacci(N - 1) + recursiveFibonacci(N - 2);
    }

    ++recursions;
    return F;
  }


  public static double iterative (int N)
  {
    // returns the elapsed time spent on the iterative computation
    double start = System.nanoTime();
    iterativeFibonacci(N);
    double end = System.nanoTime();
    return (end - start);
  }


  public static double recursive (int N)
  {
    // returns the elapsed time spent on the recursive computation
    recursions = 0;
    double start = System.nanoTime();
    recursiveFibonacci(N);
    double end = System.nanoTime();
    return (end - start);
  }
}
