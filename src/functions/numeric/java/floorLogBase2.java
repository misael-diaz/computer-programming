/*
 * Algorithms and Programming II                               May 01, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Recursive computation of the floor( LogBase2(N) ) function in Java.
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
 *
 */

import java.util.Random;
import static java.lang.Math.log;
import static java.lang.Math.floor;

public class floorLogBase2
{
  public static void main (String [] args)
  {
    // selects an integer in the asymmetric range [1, 16)
    int min = 1, max = 1025;
    Random rand = new Random();
    int N = min + rand.nextInt(max - min);


    String fmt = ("recursive   floor( Log2(%d) ): %d\n");
    // computes floor( Log2(N) ) recursively
    System.out.printf(fmt, N, Log(N) );


    fmt = ("Java's Math floor( Log2(%d) ): %.1f\n");
    // computes floor( Log2(N) ) via Java's Math library
    double lg = log(N) / log(2);
    System.out.printf(fmt, N, floor(lg) );
    return;
  }


  private static int Log (int N)
  {
    // computes floor( LogBase2(N) ) recursively
    int n;				// exponent
    if (N == 1)
      return 0;				// direct solution
    else
      n = Log (N / 2);			// divides and recurses

    return (1 + n);			// combines
  }
}
