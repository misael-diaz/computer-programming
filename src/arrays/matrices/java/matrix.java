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
		System.out.println("\nMatrix Examples\n");

		// initializes a matrix with zeroes
		Zeroes();
		// creates identity matrix
		Identity();
		// creates diagonal matrix
		Diagonal();
		// creates tridiagonal matrix
		Tridiagonal();
		Tridiagonal2();
		// creates symmetric matrix A[i][j] = A[j][i]
		Symmetric();
		// creates upper triangular matrix
		uTriangular();
		// creates lower triangular matrix
		lTriangular();
		// initializes a matrix with a sequence
		Sequence();
		// Trace of a matrix
		Trace();
		// Matrix Summation
		Sum();

		return;
	}


	public static void Zeroes()
	/*
	 * Synopsis:
	 * Creates a N x M matrix of zeroes.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = 4;

		double A[][] = mZeros(ROWS, COLS);

		System.out.println("Matrix of zeroes:");
		mPrint(ROWS, COLS, A);

		return;
	}


	public static void Identity()
	/*
	 * Synopsis:
	 * Creates a N x N identity matrix.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = ROWS;
		double I[][] = mIdentity(ROWS);

		System.out.println("Identity Matrix:");
		mPrint(ROWS, COLS, I);

		return;
	}


	public static void Diagonal()
	/*
	 * Synopsis:
	 * Creates a N x N diagonal matrix.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = ROWS;

		double D[][] = mZeros(ROWS, COLS);

		int n = 0;
		for (int i = 0; i != ROWS; ++i)
			D[i][i] = ( (double) (++n) );

		System.out.println("Diagonal Matrix:");
		mPrint(ROWS, COLS, D);

		return;
	}


	public static void Tridiagonal()
	/*
	 * Synopsis:
	 * Creates a N x N tridiagonal matrix.
	 *
	 */
	{

		int ROWS = 8;
		int COLS = ROWS;

		double T[][] = mZeros(ROWS, COLS);

		int n = 0;
		// first row (two non-zero elements)
		T[0][0] = ( (double) (++n) );
		T[0][1] = ( (double) (++n) );

		// intermediate rows (three non-zero elements)
		for (int i = 1; i != (ROWS - 1); ++i)
		{
			for (int j = (i - 1); j <= (i + 1); ++j)
				T[i][j] = ( (double) (++n) );
		}

		// last row (two non-zero elements)
		T[ROWS - 1][ROWS - 2] = ( (double) (++n) );
		T[ROWS - 1][ROWS - 1] = ( (double) (++n) );


		System.out.println("Tridiagonal Matrix:");
		mPrint(ROWS, COLS, T);

		return;
	}


	public static void Tridiagonal2()
	/*
	 * Synopsis:
	 * Creates a N x N tridiagonal matrix.
	 *
	 */
	{

		int ROWS = 8;
		int COLS = ROWS;

		double T[][] = mZeros(ROWS, COLS);

		// first row (two non-zero elements)
		T[0][0] = 2.0;
		T[0][1] = 1.0;

		// intermediate rows (three non-zero elements)
		for (int i = 1; i != (ROWS - 1); ++i)
		{
			T[i][i - 1] = 1.0;
			T[i][i]     = 2.0;
			T[i][i + 1] = 1.0;
		}

		// last row (two non-zero elements)
		T[ROWS - 1][ROWS - 2] = 1.0;
		T[ROWS - 1][ROWS - 1] = 2.0;


		System.out.println("Tridiagonal Matrix:");
		mPrint(ROWS, COLS, T);

		return;
	}


	public static void Sequence()
	/*
	 * Synopsis:
	 * Creates a N x M matrix storing the asymmetric range [0, 16).
	 *
	 */
	{

		int ROWS = 4;
		int COLS = 4;

		double A[][] = new double [ROWS][COLS];

		for (int i = 0; i != ROWS; ++i)
		{
			for (int j = 0; j != COLS; ++j)
				A[i][j] = ( (double) (j + COLS * i) );
		}

		System.out.println("Matrix storing the sequence [0, 16):");
		mPrint(ROWS, COLS, A);

		return;
	}


	public static void Symmetric()
	/*
	 * Synopsis:
	 * Creates a N x N symmetric matrix.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = ROWS;

		double A[][] = new double [ROWS][COLS];

		for (int i = 0; i != ROWS; ++i)
		{
			for (int j = 0; j != COLS; ++j)
				A[i][j] = ( (double) (j + i) );
		}

		System.out.println("Symmetric Matrix:");
		mPrint(ROWS, COLS, A);

		return;
	}


	public static void uTriangular()
	/*
	 * Synopsis:
	 * Creates a N x N upper triangular matrix.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = ROWS;

		double U[][] = mZeros(ROWS, COLS);

		int n = 0;
		for (int i = 0; i != ROWS; ++i)
		{
			for (int j = i; j != COLS; ++j)
				U[i][j] = ( (double) (++n) );
		}

		System.out.println("Upper Triangular Matrix:");
		mPrint(ROWS, COLS, U);

		return;
	}


	public static void lTriangular()
	/*
	 * Synopsis:
	 * Creates a N x N lower triangular matrix.
	 *
	 */
	{

		int ROWS = 4;
		int COLS = ROWS;

		double L[][] = mZeros(ROWS, COLS);

		int n = 0;
		for (int i = 0; i != ROWS; ++i)
		{
			for (int j = 0; j <= i; ++j)
				L[i][j] = ( (double) (++n) );
		}

		System.out.println("Lower Triangular Matrix:");
		mPrint(ROWS, COLS, L);

		return;
	}


	public static void Trace()
	/*
	 * Synopsis:
	 * Calculates the trace of the square matrix A.
	 *
	 */
	{
		double A[][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
		int ROWS = A.length;
		int COLS = A[0].length;

		System.out.println("Trace of Square Matrix:");
		System.out.printf("tr(A) = %f \n", mTrace(ROWS, COLS, A) );

		return;
	}


	public static void Sum()
	/*
	 * Synopsis:
	 * Sums matrices A and B and stores the result in matrix C.
	 *
	 */
	{
		// initializes 3 x 3 matrices A and B
		double A[][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
		double B[][] = { {1, 4, 7}, {2, 5, 8}, {3, 6, 9} };

		/* gets shape (rows, cols) of matrix A */
		int N = A.length;	// rows of matrix A
		int M = A[0].length;	// columns of matrix A

		// sums matrices A and B and places the result in matrix C
		double C[][] = mSum(N, M, A, B);


		System.out.println("\nMatrix Summation:\n");

		// prints matrix A
		System.out.println("A:");
		mPrint(N, M, A);

		// prints matrix B
		System.out.println("B:");
		mPrint(N, M, B);

		// prints matrix C = A + B
		System.out.println("C = A + B:");
		mPrint(N, M, C);

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
				C[i][j] = A[i][j] + B[i][j];
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
				System.out.printf("%8.2f", A[i][j]);
			// prints the last element of the ith row
			System.out.printf("%8.2f\n", A[i][j]);
		}
		System.out.println("");

		return;
	}


	public static double [][] mZeros(int N, int M)
	/*
	 * Synopsis:
	 * Creates a N x M matrix of zeroes.
	 *
	 */
	{

		double A[][] = new double [N][M];

		for (int i = 0; i != N; ++i)
		{
			for (int j = 0; j != M; ++j)
				A[i][j] = 0.0;
		}

		return A;
	}


	public static double [][] mIdentity(int N)
	/*
	 * Synopsis:
	 * Creates a N x N identity matrix.
	 *
	 */
	{
		double I[][] = mZeros(N, N);

		for (int k = 0; k != N; ++k)
			I[k][k] = 1.0;

		return I;
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
