/*
 * Algorithms and Programming II                          February 01, 2022
 * IST 2048
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Code shows possible ways to compute (truncated) series in Java.
 *
 *
 * Sinopsis:
 * Muestra posibles maneras de realizar computos de series en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */


class Series
{

  public static void main (String[] args)
  {

    System.out.println("Series Examples:\n\n");



    // example 1: Arithmetic Series
    int size = 101;			// defines the array size
    int s[] = new int [size];		// creates integers array


    // initializes the sequence
    for (int n = 0; n != size; ++n)
      s[n] = n;


    int iSum = 0;
    // obtains the sum of the sequence
    for (int n = 0; n != size; ++n)
      iSum += s[n];


    System.out.printf("example 1: Arithmetic Series\n");
    System.out.printf("for n = [0, 101), Sum(n) = %d\n\n\n", iSum);



    // example 2: Sum of Squares
    size = 101;
    for (int n = 0; n != size; ++n)
      s[n] = n * n;


    iSum = 0;
    // obtains the sum of the sequence
    for (int n = 0; n != size; ++n)
      iSum += s[n];


    System.out.printf("example 2: Sum of Squares\n");
    System.out.printf("for n = [0, 101), Sum(n^2) = %d\n\n\n", iSum);



    // example 3: Alternating Series
    size = 100;
    double ds [] = new double [size];	// array of doubles


    for (int n = 0; n != size; ++n)
      ds[n] = Math.pow(-1.0, n) / (1.0 + n);


    double sum = 0.0;
    for (int n = 0; n != size; ++n)
      sum += ds[n];


    System.out.printf("example 3: Alternating Series\n");
    System.out.printf("for n = [0, 100), Sum( (-1)^n / (n + 1) ) = %.6f\n\n\n", sum);



    // example 4: Geometric Series
    size = 100;
    double r = 0.2;


    for (int n = 0; n != size; ++n)
      ds[n] = Math.pow(r, n);


    sum = 0.0;
    for (int n = 0; n != size; ++n)
      sum += ds[n];


    System.out.printf("example 4: Geometric Series\n");
    System.out.printf ("for n = [0, 100), Sum(r^n) = %.6f\n\n\n", sum);



    // example 5: Taylor Series
    long fact = 1;		// factorial
    size = 16;			// redefines the array size
    ds = new double [size];	// resizes the array


    // stores the elements of the sequence in an array
    for (int n = 0; n != size; ++n)
    {
      fact = 1;
      // calculates the factorial of `n'
      for (long m = n; m != 0; --m)
	fact *= m;

      ds[n] = Math.pow(2.0, n) / fact;
    }


    sum = 0.0;
    for (int n = 0; n != size; ++n)
      sum += ds[n];


    System.out.printf("example 5: Taylor Series\n");
    System.out.printf("for n = [0, 16), Sum(2^n / n!) = %.6f\n\n\n", sum);



    // example 6: Taylor Series
    size = 11;			// redefines the array size
    ds = new double [size];	// resizes the array
    double x = Math.PI / 3;

    // stores the elements of the sequence in an array
    for (int n = 0; n != size; ++n)
    {
      fact = 1;
      // calculates the factorial of `2n + 1'
      for (long m = (2*n + 1); m != 0; --m)
	fact *= m;

      ds[n] = Math.pow(x, 2*n + 1) / fact;
      ds[n] = (n % 2 == 0)? ds[n]: -ds[n];
    }

    sum = 0.0;
    for (int n = 0; n != size; ++n)
      sum += ds[n];

    System.out.printf("example 6: Taylor Series\n");
    System.out.printf(
	"for n = [0, 11), " +
	"Sum( (-1)^n * x^(2n+1) / (2n+1)! ) = %.6f\n", sum
    );
  }
}


/*
 * COMMENTS:
 * [0] The factorial is not implemented in the core Math library of Java,
 *     this is why it is implemented in the nested for-loops.
 * [1] There is a lot of code repetition, those blocks of code are perfect
 *     candidates for partitioning them into functions (or subroutines) as
 *     we shall see later on in the course. In the end the code will become
 *     more readable and easier to maintain.
 */
