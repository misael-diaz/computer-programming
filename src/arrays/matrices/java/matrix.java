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
 * [0] https://docs.oracle.com/javame/config/cldc/opt-pkgs/api/daapi/com/oracle/deviceaccess/InvalidOperationException.html
 * )
 *
 */


class Matrix
{

  public static void main (String[] args)
  {
    System.out.println("\nMatrix Examples\n");

    Zeroes();				// initializes a matrix with zeroes
    Identity();				// creates identity matrix
    Diagonal();				// creates diagonal matrix
    Tridiagonal();			// creates tridiagonal matrix
    Tridiagonal2();			// creates tridiagonal matrix
    Symmetric();			// creates symmetric matrix A[i][j] = A[j][i]
    uTriangular();			// creates upper triangular matrix
    lTriangular();			// creates lower triangular matrix
    Sequence();				// initializes a matrix with a sequence
    Transpose();			// transpose of a matrix
    Trace();				// Trace of a matrix
    Sum();				// Matrix Summation

    return;
  }


  public static void Zeroes() // creates a N x M matrix of zeroes.
  {

    int ROWS = 4;
    int COLS = 4;

    double A[][] = mZeros(ROWS, COLS);

    System.out.println("Matrix of zeroes:");
    mPrint(ROWS, COLS, A);

    return;
  }


  public static void Identity() // creates a N x N identity matrix.
  {

    int ROWS = 4;
    int COLS = ROWS;
    double I[][] = mIdentity(ROWS);

    System.out.println("Identity Matrix:");
    mPrint(ROWS, COLS, I);

    return;
  }


  public static void Diagonal() // creates a N x N diagonal matrix.
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


  public static void Tridiagonal() // Creates a N x N tridiagonal matrix.
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


  public static void Tridiagonal2() // Creates a N x N tridiagonal matrix.
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


  // Synopsis:
  // Creates a N x M matrix storing the asymmetric range [0, 16).
  public static void Sequence()
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


  // void Transpose()
  //
  // Synopsis:
  // Creates a N x M matrix storing the asymmetric range [0, 32), and obtains it tranpose.


  public static void Transpose()
  {
    int ROWS = 4;
    int COLS = 8;

    // generates matrix A storing the arange [0, 32)
    double A[][] = new double [ROWS][COLS];

    for (int i = 0; i != ROWS; ++i)
    {
      for (int j = 0; j != COLS; ++j)
	A[i][j] = ( (double) (j + COLS * i) );
    }

    System.out.println("Matrix storing the sequence [0, 32):");
    mPrint(ROWS, COLS, A);


    // transposes matrix A and stores it in matrix B
    double B[][] = new double [COLS][ROWS];
    for (int i = 0; i != ROWS; ++i)
    {
      for (int j = 0; j != COLS; ++j)
	B[j][i] = A[i][j];
    }

    System.out.println("Transposed Matrix:");
    mPrint(COLS, ROWS, B);

    return;
  }


  public static void Symmetric() // Creates a N x N symmetric matrix.
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


  public static void uTriangular() // Creates a N x N upper triangular matrix.
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


  public static void lTriangular() // Creates a N x N lower triangular matrix.
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


  public static void Trace() // Calculates the trace of the square matrix A.
  {
    double A[][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
    int ROWS = A.length;
    int COLS = A[0].length;

    System.out.println("Trace of Square Matrix:");
    System.out.printf("tr(A) = %f \n", mTrace(ROWS, COLS, A) );

    return;
  }


  public static void Sum() // Sums matrices A and B and stores the result in matrix C.
  {
    // initializes 3 x 3 matrices A and B
    double A[][] = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
    double B[][] = { {1, 4, 7}, {2, 5, 8}, {3, 6, 9} };

    // gets shape (rows, cols) of matrix A
    int N = A.length;		// rows of matrix A
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


  // double[][] mSum(int N, int M, double [][] A, double [][] B)
  //
  // Synopsis:
  // Possible implementation of the summation operator for matrices.
  //
  // Inputs
  // N	number of rows
  // M	number of columns
  // A	N x M matrix
  // B	N x M matrix
  //
  // Output
  // C	N x M matrix, C = A + B


  public static double[][] mSum(int N, int M, double [][] A, double [][] B)
  {
    // complains if the matrix shapes are inconsistent
    String method = "mSum(): ";
    isConsistent(method, N, M, A);
    isConsistent(method, N, M, B);

    double C[][] = new double [N][M];
    for (int i = 0; i != N; ++i)
    {
      for (int j = 0; j != M; ++j)
	C[i][j] = A[i][j] + B[i][j];
    }

    return C;
  }


  // double mTrace(int N, int M, double [][] A)
  //
  // Synopsis:
  // Possible implementation of the trace operator (square) matrices.
  //
  // Inputs
  // N	number of rows
  // M	number of columns (M must be equal to N, a square matrix)
  // A	N x M matrix
  //
  // Output
  // tr	sum of the diagonal elements of the square matrix A


  public static double mTrace(int N, int M, double [][] A)
  {
    // complains if matrix is not square
    String method = "mTrace(): ";
    isSquare(method, A);
    // complains if the matrix shape is inconsistent
    isConsistent(method, N, M, A);

    double tr = 0;
    for (int i = 0; i != N; ++i)
      tr += A[i][i];

    return tr;
  }


  public static void mPrint(int N, int M, double [][] A)
  {
    // complains if the matrix shape is inconsistent
    String method = "mPrint(): ";
    isConsistent(method, N, M, A);

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


  public static double [][] mZeros(int N, int M) // Creates a N x M matrix of zeroes.
  {

    double A[][] = new double [N][M];

    for (int i = 0; i != N; ++i)
    {
      for (int j = 0; j != M; ++j)
	A[i][j] = 0.0;
    }

    return A;
  }


  public static double [][] mIdentity(int N) // Creates a N x N identity matrix.
  {
    double I[][] = mZeros(N, N);

    for (int k = 0; k != N; ++k)
      I[k][k] = 1.0;

    return I;
  }


  // implementations:


  // void isSquare(String method, double [][] A)
  //
  // Synopsis:
  // Complains if matrix A is not square, that is, a matrix having
  // an unequal number of rows and columns.


  private static void isSquare(String method, double [][] A)
  {
    // gets rows and cols, respectively
    int N = A.length, M = A[0].length;

    if (N != M)
    {
      throw new RuntimeException(method + "expects a square matrix");
    }
  }


  // void isConsistent(String method, int Rows, int Cols, double [][] A)
  //
  // Synopsis:
  // Complains if the shape passed by the caller is inconsistent
  // with the shape of the passed matrix A.


  private static void isConsistent(String method, int Rows, int Cols, double [][] A)
  {
    if (Rows != A.length)
    {
      throw new RuntimeException(method + "inconsistent number of rows");
    }

    if (Cols != A[0].length)
    {
      throw new RuntimeException(method + "inconsistent number of columns");
    }
  }
}


/*
 * COMMENTS:
 *
 * The methods check that the matrix shapes are consistent, this
 * matters when performing matrix summation, multiplication, trace,
 * concatenation, etc. A robust implementation raises an
 * exception to inform the user what went wrong, as for example an
 * invalid operation exception due to inconsistent shapes of matrices
 * A and B.
 *
 * Even though the shape of a matrix can be easily obtained via the
 * length method, passing that data to the functions has been adopted
 * as done in legacy linear algebra codes.
 *
 */
