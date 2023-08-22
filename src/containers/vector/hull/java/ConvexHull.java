
import java.util.Random;

public class ConvexHull
{

  // elapsed-times (nanoseconds):


  public static double etimeBruteForce;
  public static double etimeRecursive;


  public static void main (String [] args)
  {
    testBruteForceMethod();
    return;
  }


  // implementations:


  // generates a data set of 2D Cartesian coordinates
  private static Vector genDataSet ()
  {
    /*

    // initializes the dataset of problem 2.4.1-1
    int [][] data = {
    { 1,  6}, { 4, 15}, { 7,  7}, {10, 13}, {11,  6},
    {11, 18}, {11, 21}, {12, 10}, {15, 18}, {16,  6},
    {18,  3}, {18, 12}, {19, 15}, {22, 19}
    };

*/


    /*

    // initializes the dataset of problem 2.4.1-2
    int [][] data = {
    { 2, 20}, {3, 12}, { 5,  5}, { 6,  8}, {6, 17},
    { 8,  9}, {10, 2}, {10, 15}, {11,  7}, {11, 11},
    {14, 14}, {16, 7}, {18, 15}, {19,  6}, {19, 10},
    {22, 16}
    };

*/


    // initializes the dataset of problem 2.4.1-3
    int [][] data = {
      { 2, 14}, { 4,  3}, { 5,  8}, { 5, 17}, { 6,  5},
      { 6, 21}, { 8, 11}, { 9, 17}, {10,  7}, {10, 19},
      {12,  5}, {12, 14}, {16,  9}, {18, 12}, {18, 17},
      {19,  4}, {19,  6}, {21, 15}, {22, 20}, {23,  9}
    };


    /*

    // yields a Bad Convex Hull (complains as it should)
    int [][] data = { {0, 0}, {2, 0}, {2, 2}, {1, 1} };

*/


    // gets the shape of the datset
    int rows = data.length, cols = data[0].length;
    // gets the number of coordinates from the max dimension
    int size = (rows > cols)? rows : cols;

    Vector points = new Vector(size);
    // stacks the dataset of coordinates on the vector
    for (int i = 0; i != size; ++i)
    {
      int x = data[i][0];
      int y = data[i][1];
      Coord c = new Coord(x, y);
      points.push_back(c);
    }

    return points;
  }


  // returns true if the line PQ is an edge of the convex hull
  private static boolean isEdge (Vector points, Coord P, Coord Q)
  {
    int size = points.size();	// gets the data size
    Line ln = new Line(P, Q);	// creates a line object


    // traverses the vector until we can initialize sign with a non-zero value


    int k = 0;
    int isign = 0;
    while (isign == 0 && k != size)
    {
      Coord R = points.getData(k);
      int sgn = ln.loc(R);
      if (sgn != 0)
	isign = sgn;
      ++k;
    }


    // there's an initial sign so we can check for sign changes on the rest of the vector.


    boolean isEdge = true;
    for (int l = k; l != size; ++l)
    {
      Coord R = points.getData(l);
      int sgn = ln.loc(R);
      if (sgn * isign < 0)
	isEdge = false;
    }

    return isEdge;
  }


  // returns true if all the vertices have been found
  private static boolean isClosed (Vector vertices, Coord P, Coord Q)
  {
    int pos1 = vertices.search(P), pos2 = vertices.search(Q);
    if (pos1 >= 0 && pos2 >= 0)
      return true;
    else
      return false;
  }


  // Synopsis:
  // Pushes unique vertices unto the back of the vector of vertices. As
  // a side effect, it sets the closed state of the convex hull. This
  // happens when both points P and Q are already vertices of the convex
  // hull. Generally this means that the algorithm has found all the
  // vertices. The other possibility is that we have a bad convex hull
  // with at least one internal angle equal to 180 degrees; this is the
  // main reason for setting the closed state of the convex hull.


  private static boolean addVertex(Vector vertices, Coord P, Coord Q)
  {
    // checks if all the vertices have been found
    boolean closed = isClosed(vertices, P, Q);
    // pushes new vertices unto the back of the vector
    if (vertices.search(P) < 0) vertices.push_back(P);
    if (vertices.search(Q) < 0) vertices.push_back(Q);
    // sorts vector to support underlying binary search method
    vertices.sort();
    // returns the closed state for the next pass
    return closed;
  }


  // complains if the data set does not contain a convex hull
  private static void BadHull () throws RejectedHullException
  {
    String errmsg = "BadHullError";
    throw new RejectedHullException(errmsg);
  }


  // complains if the data set does not contain a convex hull
  private static void BadHull (String errmsg) throws RejectedHullException
  {
    throw new RejectedHullException(errmsg);
  }


  // Synopsis:
  // Complains if both vertices P and Q are already in the stack of
  // vertices. This means that there is at least a 180 degrees vertex.
  // Note that our acceptable range for the internal angle of a vertex
  // is [90, 180) degrees.


  private static void isRejectableHull (boolean isClosed) throws RejectedHullException
  {
    if (isClosed)
      BadHull("180AngleError");
  }


  private static void isRejectableHull (Vector points) throws RejectedHullException
    /*

       Synopsis:
       Uses brute force to determine if the data set of coordinates
       does not contain a convex hull whose interior angles are in the
       range [90, 180) --- a bad convex hull.

*/
  {
    // assumes that all the vertices have not been found
    boolean closed = false;

    // creates a placeholder for the vertices of the hull
    Vector vertices = new Vector ();

    int size = points.size();
    // considers all the possible edges, size * (size - 1) / 2
    for (int i = 0; i != (size - 1); ++i)
    {
      Coord P = points.getData(i);
      for (int j = (i + 1); j != size; ++j)
      {

	/*

	   creates the line PQ and checks if it is an
	   edge of the convex hull

*/

	Coord Q = points.getData(j);
	Line ln = new Line(P, Q);

	/*

	   traverses the whole vector until we can
	   initialize the sign with a non-zero value

*/

	int k = 0;
	int isign = 0;
	while (isign == 0 && k != size)
	{
	  Coord R = points.getData(k);
	  int sgn = ln.loc(R);
	  if (sgn != 0)
	    isign = sgn;
	  ++k;
	}


	// complains if it is a bad hull (line)


	// complains if all the points form a line
	if (isign == 0)
	  BadHull();


	/*

	   Now that we have an initial sign we can
	   check for sign changes on the remainder
	   of the vector. Note: the points P and Q
	   form an edge if there are no sign changes.

*/


	boolean isEdge = true;
	for (int l = k; l != size; ++l)
	{
	  Coord R = points.getData(l);
	  int sgn = ln.loc(R);
	  if (sgn * isign < 0)
	    isEdge = false;
	}


	// adds vertices if line PQ is a hull edge
	if (isEdge)
	{
	  isRejectableHull(closed);
	  closed = addVertex(vertices, P, Q);
	}
      }
    }
  }


  // returns the vertices of the convex hull in a vector
  private static Vector bruteforce (Vector points)
  {
    // creates the placeholder for the vertices of the hull
    Vector vertices = new Vector ();

    int size = points.size();
    // considers all the possible edges, size * (size - 1) / 2
    for (int i = 0; i != (size - 1); ++i)
    {
      Coord P = points.getData(i);
      // stacks points P and Q if they form a hull edge
      for (int j = (i + 1); j != size; ++j)
      {
	Coord Q = points.getData(j);

	if ( isEdge(points, P, Q) )
	  addVertex(vertices, P, Q);
      }
    }

    return vertices;
  }


  // static methods:


  public static Vector genDataSet (int size)
    /*

       Synopsis:
       Generates a distinct data set of coordinates by sampling values
       from a uniform pseudo-random number generator PRNG.

       Inputs:
       size		size of the data set (number of particles)

       Output:
       vector		a vector that stores the data set of coordinates

*/
  {
    // creates a vector for storing the coordinates
    Vector vector = new Vector();
    // creates a pseudo-random number generator PRNG
    //long seed = System.nanoTime();
    Random random = new Random();
    // defines limits for the particle coordinates
    int x_min = -size, x_max = size;
    int y_min = -size, y_max = size;
    // generates the distinct set of (x, y) coordinates
    for (int i = 0; i != size; ++i)
    {
      int x = x_min + random.nextInt(x_max - x_min);
      int y = y_min + random.nextInt(y_max - y_min);
      Coord c = new Coord (x, y);
      // generates a new coordinate if already in vector
      while (vector.search(c) >= 0)
      {
	x = random.nextInt(size);
	y = random.nextInt(size);
	c = new Coord (x, y);
      }
      // pushes (distinct) coordinate unto back of vector
      vector.push_back(c);
      // sorts to use binary search on next pass
      vector.sort();
    }

    return vector;
  }


  // uses brute force to obtain the convex hull
  public static Vector BruteForce (Vector points) throws RejectedHullException
  {
    // complains if there is no convex hull
    isRejectableHull(points);

    // times the implementation that finds the convex hull

    double start = System.nanoTime();
    Vector vertices = bruteforce(points);
    double end = System.nanoTime();

    // computes the elapsed time in nanoseconds
    etimeBruteForce = (end - start);

    return vertices;
  }


  // tests:


  // tests if `Line.loc()' detects sign changes properly
  private static void testLineLocMethod ()
  {
    int x1 = 0, y1 = 0;
    int x2 = 4, y2 = 4;
    int xi = -1, yi = -1;
    Coord P = new Coord(x1, y1);
    Coord Q = new Coord(x2, y2);
    Coord R = new Coord(xi, yi);

    Line line = new Line(P, Q);

    System.out.printf("on the line (0): %d\n", line.loc(R));

    xi = -1;
    yi = -2;
    Coord S = new Coord(xi, yi);
    System.out.printf("below line (-1): %d\n", line.loc(S));

    xi = -2;
    yi = -1;
    Coord T = new Coord(xi, yi);
    System.out.printf("above line (1): %d\n", line.loc(T));
  }


  private static void testBruteForceMethod ()
  {
    // creates the data set of Cartesian coordinates
    Vector points = genDataSet();
    // finds the vertices of the convex hull with brute force
    Vector vertices = bruteforce (points);
    // displays the coordinates of the vertices on the console
    vertices.print();
  }
}


/*

Algorithms and Complexity                            October 19, 2022
IST 4310
Prof. M. Diaz-Maldonado

Synopsis:
Finds the Convex Hull from a set of 2D Cartesian coordinates.

Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition, 2007
[1] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


/*

COMMENTS:
The code solves the convex hull problems of section 2.4.1 (Ref [0]) and
detects if the convex hull has an interior angle of 180 degrees (bad).
The Brute Force algorithm exhibits the expected cubic time complexity.

*/
