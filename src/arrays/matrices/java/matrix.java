/*
 * Algorithms and Programming II                          March 16, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Implements some matrix algebra operations in Java.
 *
 *
 * Sinopsis:
 * Implementa algunas operaciones de algebra de matrices en Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] Invalid Operation Exception: (
 * 	docs.oracle.com/javame/config/cldc/opt-pkgs/api/daapi/com/oracle/
 * 	deviceaccess/InvalidOperationException.html
 * )
 *
 */


class Matrix
{

	public static void main (String[] args)
	{


		// initializes 3 x 3 matrices A and B
		double A[][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
		double B[][] = { {1, 4, 7}, {2, 5, 8}, {3, 6, 9} };


		/* gets shape (rows, cols) of matrix A */
		int N = A.length;	// rows of matrix A
		int M = A[0].length;	// columns of matrix A


		// sums matrices A and B and places the result in matrix C
		double C[][] = mSum(N, M, A, B);

		System.out.println("\nMatrix Examples\n");
		System.out.println("Matrix Summation:");

		// prints matrix A
		System.out.println("A:");
		mPrint(N, M, A);

		// prints matrix B
		System.out.println("B:");
		mPrint(N, M, B);

		// prints matrix C = A + B
		System.out.println("C = A + B:");
		mPrint(N, M, C);


		// obtains the trace of matrices A, B, and C
		System.out.println("Trace of Square Matrix:");
		System.out.printf("tr(A) = %f \n",   mTrace(N, M, A) );
		System.out.printf("tr(B) = %f \n",   mTrace(N, M, B) );
		System.out.printf("tr(C) = %f \n\n", mTrace(N, M, C) );


		// initializes a matrix with zeroes
		mZeroes();

		return;
	}


	public static void mZeroes()
	/*
	 * Synopsis:
	 * Creates a N x M matrix of zeroes.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = 4;

		double A[][] = new double [ROWS][COLS];

		for (int i = 0; i != ROWS; ++i)
		{
			for (int j = 0; j != COLS; ++j)
				A[i][j] = 0.0;
		}

		System.out.println("Matrix of zeroes:");
		mPrint(ROWS, COLS, A);

		return;
	}


	public static double[][] mSum(int N, int M, double [][] A,
                                      double [][] B)
	/*
	 * Synopsis:
	 * Possible implementation of the summation operator for matrices.
	 *
	 * Inputs
	 * N	number of rows
	 * M	number of columns
	 * A	N x M matrix
	 * B	N x M matrix
	 *
	 * Output
	 * C	N x M matrix, C = A + B
	 *
	 */
	{
		double C[][] = new double [N][M];
		for (int i = 0; i != N; ++i)
		{
			for (int j = 0; j != M; ++j)
			{
				C[i][j] = A[i][j] + B[i][j];
			}
		}

		return C;
	}


	public static double mTrace(int N, int M, double [][] A)
	/*
	 * Synopsis:
	 * Possible implementation of the trace operator (square) matrices.
	 *
	 * Inputs
	 * N	number of rows
	 * M	number of columns (M must be equal to N, a square matrix)
	 * A	N x M matrix
	 *
	 * Output
	 * tr	sum of the diagonal elements of the square matrix A
	 *
	 */
	{
		double tr = 0;
		for (int i = 0; i != N; ++i)
			tr += A[i][i];

		return tr;
	}


	public static void mPrint(int N, int M, double [][] A)
	/*
	 * Synopsis:
	 * Prints the elements of matrix A to the console.
	 *
	 * Inputs
	 * N	number of rows
	 * M	number of columns
	 * A	N x M matrix
	 *
	 * Output
	 * None
	 *
	 */
	{
		int j = 0;
		System.out.println("");
		for (int i = 0; i != N; ++i)
		{
			// prints all but the last element of the ith row
			for (j = 0; j != M - 1; ++j)
			{
				System.out.printf("%8.2f", A[i][j]);
			}
			// prints the last element of the ith row
			System.out.printf("%8.2f\n", A[i][j]);
		}
		System.out.println("");

		return;
	}
}


/*
 * COMMENTS:
 *
 * The methods do not check that the matrix shapes are consistent, this
 * matters when performing matrix summation, multiplication, trace,
 * concatenation, etc. A more robust implementation should raise an
 * exception to inform the user what went wrong, as for example an
 * invalid operation exception due to inconsistent shapes of matrices
 * A and B.
 *
 * Even though the shape of a matrix can be easily obtained via the
 * length method, passing that data to the functions has been adopted
 * as done in legacy linear algebra codes.
 *
 */
