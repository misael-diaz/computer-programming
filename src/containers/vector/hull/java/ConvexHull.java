import java.util.Random;

public class ConvexHull
{
  private double elapsedTime;		// elapsed-time (nanoseconds)
  private final Stack data;		// points dataset


  ConvexHull (Stack data)
  {
    this.data = new Stack(data);
    this.elapsedTime = Double.NEGATIVE_INFINITY;
  }


  public double getElapsedTime ()
  {
    return this.elapsedTime;
  }


  public static void main (String [] args) throws RejectedHullException
  {
    test();
    testBruteForceMethod();
    testDivideAndConquerMethod();
    return;
  }


  // implementations:


  // generates a data set of 2D Points
  private static Stack genDataSet ()
  {
    // initializes the dataset of problem 2.4.1-1
    // int [][] data = {
    //   { 1,  6}, { 4, 15}, { 7,  7}, {10, 13}, {11,  6}, {11, 18}, {11, 21},
    //   {12, 10}, {15, 18}, {16,  6}, {18,  3}, {18, 12}, {19, 15}, {22, 19}
    // };


    // initializes the dataset of problem 2.4.1-2
    // int [][] data = {
    //   { 2, 20}, { 3, 12}, { 5,  5}, { 6, 8}, { 6, 17}, { 8,  9}, {10,  2}, {10, 15},
    //   {11,  7}, {11, 11}, {14, 14}, {16, 7}, {18, 15}, {19,  6}, {19, 10}, {22, 16}
    // };


    // initializes the dataset of problem 2.4.1-3
    int [][] data = {
      { 2, 14}, { 4,  3}, { 5,  8}, { 5, 17}, { 6,  5},
      { 6, 21}, { 8, 11}, { 9, 17}, {10,  7}, {10, 19},
      {12,  5}, {12, 14}, {16,  9}, {18, 12}, {18, 17},
      {19,  4}, {19,  6}, {21, 15}, {22, 20}, {23,  9}
    };


    // this dataset contains a convex hull with some vertical edges
    // int [][]  data = {
    //   {-8, -7}, {-8, -1}, {-5, +5}, {-2, +0}, {+0, -2}, {+0, +7}, {+7, -7}, {+7, +7}
    // };


    // yields a Bad Convex Hull (complains as it should)
    // int [][] data = { {0, 0}, {2, 0}, {2, 2}, {1, 1} };


    // a line we expect this dataset to be rejected (and code does that OK)
    // int [][] data = { {0, 0}, {1, 1}, {2, 2}, {3, 3} };


    // triangle (code does fine, finds the vertices and complains, OK)
    // int [][] data = { {0, 0}, {0, 1}, {1, 1} };


    // square (OK)
    // int [][] data = { {0, 0}, {0, 1}, {1, 0}, {1, 1} };


    // gets the shape of the datset
    int rows = data.length, cols = data[0].length;
    // gets the number of points from the max dimension
    int size = (rows > cols)? rows : cols;

    Stack points = new Stack(size);
    // stacks the dataset of points on the stack
    for (int i = 0; i != size; ++i)
    {
      int x = data[i][0];
      int y = data[i][1];
      Point p = new Point(x, y);
      points.push_back(p);
    }

    return points;
  }


  // returns true if the line PQ is an edge of the convex hull
  private boolean isEdge (Point P, Point Q) throws RejectedHullException
  {
    final Stack points = this.data;
    final int size = this.data.size();	// gets the data size
    Line ln = new Line(P, Q);		// creates a line object


    // traverses the stack until we can initialize sign with a non-zero value


    int k = 0;
    int isign = 0;
    while (isign == 0 && k != size)
    {
      Point R = points.get(k);
      isign = ln.sign(R);
      ++k;
    }


    if (isign == 0)
    {
      this.RejectHull("there's no convex hull because there are no sign changes");
    }


    // there's an initial sign so we can check for sign changes on the rest of the stack.


    boolean isEdge = true;
    for (int l = k; l != size; ++l)
    {
      Point R = points.get(l);
      int sgn = ln.sign(R);
      if (sgn * isign < 0)
      {
	isEdge = false;
      }
    }

    return isEdge;
  }


  // returns true if all the vertices have been found
  private boolean isClosed (Stack vertices, Point P, Point Q)
  {
    int pos1 = vertices.search(P), pos2 = vertices.search(Q);
    if (pos1 >= 0 && pos2 >= 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  // private boolean addVertex(Stack vertices, Point P, Point Q)
  //
  // Synopsis:
  // Pushes unique vertices unto the back of the stack of vertices. As a side effect, it
  // sets the closed state of the convex hull. This happens when both points P and Q are
  // already vertices of the convex hull. Generally this means that the algorithm has
  // found all the vertices. The other possibility is that we have an invalid convex hull
  // with at least one internal angle equal to 180 degrees; this is the main reason for
  // setting the closed state of the convex hull.
  //
  // Inputs:
  // vertices		placeholder for storing the vertices found thus far
  // P			point P is a vertex since we have already asserted this
  // Q			point Q is also vertex for the same reasons
  //
  // Output:
  // vertices		the updated placeholder (could remain unchanged if we are done)
  // closed		true if we have found all the vertices, false otherwise


  private boolean addVertex(Stack vertices, Point P, Point Q)
  {
    // checks if all the vertices have been found
    boolean closed = this.isClosed(vertices, P, Q);
    // pushes new vertices unto the back of the stack
    if (vertices.search(P) < 0)
    {
      vertices.push_back(P);
    }

    if (vertices.search(Q) < 0)
    {
      vertices.push_back(Q);
    }
    // sorts stack to support underlying binary search method
    vertices.sort();
    // returns the closed state for the next pass
    return closed;
  }


  // complains if the data set does not contain a (valid) convex hull
  private void RejectHull (String errmsg) throws RejectedHullException
  {
    throw new RejectedHullException(errmsg);
  }


  // private void isRejectableHull (boolean isClosed) throws RejectedHullException
  //
  // Synopsis:
  // Complains if both vertices P and Q are already in the stack of vertices. This means
  // that there is at least a 180 degrees vertex. Note that our acceptable range for the
  // interior angle of a vertex is [90, 180) degrees.
  //
  // Input:
  // isClosed	true if all vertices have been found yet here again, false otherwise
  //
  // Output:
  // None


  private void isRejectableHull (boolean isClosed) throws RejectedHullException
  {
    if (isClosed)
    {
      this.RejectHull("180AngleError");
    }
  }


  private void checkAngle (Vector U, Vector V) throws RejectedHullException
  {
    if (U.dot(V) > 0)
    {
      this.RejectHull("detected hull with interior angle less than 90 degrees");
    }
  }


  // checks the interior angle of first vertex
  private void first (Stack vertices) throws RejectedHullException
  {
    final int last = (vertices.size() - 1);
    final Point A = vertices.get(last);
    final Point B = vertices.get(0);
    final Point C = vertices.get(1);
    final Vector BA = new Vector(B, A);
    final Vector BC = new Vector(B, C);
    this.checkAngle(BA, BC);
  }


  // checks the interior angles of the intermediate vertices
  private void middle (Stack vertices) throws RejectedHullException
  {
    for (int i = 1; i != (vertices.size() - 1); ++i)
    {
      final Point A = vertices.get(i - 1);
      final Point B = vertices.get(i);
      final Point C = vertices.get(i + 1);
      final Vector BA = new Vector(B, A);
      final Vector BC = new Vector(B, C);
      this.checkAngle(BA, BC);
    }
  }


  // checks the interior angle of the last vertex
  private void last (Stack vertices) throws RejectedHullException
  {
    final int last = (vertices.size() - 1);
    final Point A = vertices.get(last - 1);
    final Point B = vertices.get(last);
    final Point C = vertices.get(0);
    final Vector BA = new Vector(B, A);
    final Vector BC = new Vector(B, C);
    this.checkAngle(BA, BC);
  }


  // checks the interior angles of the hull, complains if there's an acute angle
  private void isRejectableHull (Stack vertices) throws RejectedHullException
  {
    this.first(vertices);
    this.middle(vertices);
    this.last(vertices);
  }


  // returns the vertices of the convex hull in clockwise order
  private Stack clockwise (Stack vertices)
  {
    vertices.sort();
    final int size = vertices.size();
    final int last = (size - 1);
    final Point P = vertices.get(0);
    final Point Q = vertices.get(last);
    final Line line = new Line(P, Q);

    Stack left = new Stack(size);
    Stack right = new Stack(size);
    // divides vertices into left and right partitions
    for (int i = 0; i != size; ++i)
    {
      final Point vertex = vertices.get(i);
      if (line.sign(vertex) < 0)
      {
	left.push_back(vertex);
      }
      else
      {
	right.push_back(vertex);
      }
    }

    Stack clockwise = new Stack(size);
    // stores the vertices in the right partition in order
    for (int i = 0; i != right.size(); ++i)
    {
      final Point vertex = right.get(i);
      clockwise.push_back(vertex);
    }

    // stores the vertices in the left partition in reverse order
    for (int i = 0; i != left.size(); ++i)
    {
      final int j = ( left.size() - (i + 1) );
      final Point vertex = left.get(j);
      clockwise.push_back(vertex);
    }

    return clockwise;
  }


  // private Stack bruteforce (Stack points) throws RejectedHullException
  //
  // Synopsis:
  // Uses brute force to obtain the convex hull from the dataset of points.
  // Complains if it finds an invalid convex hull whose interior angles do not meet the
  // convex angle criterion, not in the range range [90, 180).
  //
  // Input:
  // points	dataset of points
  //
  // Output:
  // vertices	the vertices of the convex hull (in clockwise order)


  private Stack bruteforce () throws RejectedHullException
  {
    // creates the placeholder for the vertices of the hull
    Stack vertices = new Stack();

    boolean closed = false;
    final Stack points = this.data;
    final int size = this.data.size();
    // considers all the possible edges, size * (size - 1) / 2
    for (int i = 0; i != (size - 1); ++i)
    {
      final Point P = points.get(i);
      // stacks points P and Q if they form a hull edge
      for (int j = (i + 1); j != size; ++j)
      {
	final Point Q = points.get(j);

	if ( this.isEdge(P, Q) )
	{
	  this.isRejectableHull(closed);
	  closed = this.addVertex(vertices, P, Q);
	}
      }
    }

    vertices = this.clockwise(vertices);

    this.isRejectableHull(vertices);

    return vertices;
  }


  // static methods:


  // public static Stack genDataSet (int size)
  //
  // Synopsis:
  // Generates a distinct dataset of points by sampling values from a uniform pseudo
  // random number generator PRNG.
  //
  // Inputs:
  // size		size of the dataset (or number of points)
  //
  // Output:
  // stack		the placeholder that stores the dataset


  public static Stack genDataSet (int size)
  {
    // creates a stack for storing the points
    Stack stack = new Stack();
    // creates a pseudo-random number generator PRNG
    //long seed = System.nanoTime();
    Random random = new Random();
    // defines limits for the point coordinates
    int x_min = -size, x_max = size;
    int y_min = -size, y_max = size;
    // generates the distinct set of 2D points
    for (int i = 0; i != size; ++i)
    {
      int x = x_min + random.nextInt(x_max - x_min);
      int y = y_min + random.nextInt(y_max - y_min);
      Point p = new Point(x, y);
      // generates a new point if already in stack
      while (stack.search(p) >= 0)
      {
	x = random.nextInt(size);
	y = random.nextInt(size);
	p = new Point(x, y);
      }
      // pushes (distinct) point unto back of stack
      stack.push_back(p);
      // sorts to use binary search on next pass
      stack.sort();
    }

    return stack;
  }


  // uses brute force to obtain the convex hull
  public Stack bruteForce () throws RejectedHullException
  {
    // times the implementation that finds the convex hull
    double start = System.nanoTime();
    Stack vertices = this.bruteforce();
    double end = System.nanoTime();

    // computes the elapsed time in nanoseconds
    this.elapsedTime = (end - start);

    return vertices;
  }


  // returns a sorted stack of convex hull vertices
  private Stack direct (final Stack points)
  {
    // we have asserted that there are only two points in the stack
    Stack vertices = new Stack();
    final Point P = points.get(0);
    final Point Q = points.get(1);
    if (P.compareTo(Q) < 0)
    {
      vertices.push_back(P);
      vertices.push_back(Q);
    }
    else
    {
      vertices.push_back(Q);
      vertices.push_back(P);
    }

    return vertices;
  }


  private Stack combine (final Stack left, final Stack right)
  {
    final int sz = left.size() + right.size();

    int beginLeft = 0;
    int endLeft = left.size();
    int beginRight = 0;
    int endRight = right.size();

    Stack vertices = new Stack(sz);
    // combines the left and right in order:
    while ( (beginLeft != endLeft) && (beginRight != endRight) )
    {
      final Point P = left.get(beginLeft);
      final Point Q = right.get(beginRight);
      if (P.compareTo(Q) == 0)
      {
	vertices.push_back(P);
	++beginLeft;
	++beginRight;
      }
      else if (P.compareTo(Q) < 0)
      {
	vertices.push_back(P);
	++beginLeft;
      }
      else
      {
	vertices.push_back(Q);
	++beginRight;
      }
    }

    // stacks whatever that remains from the left and right:

    for (int i = beginLeft; i != endLeft; ++i)
    {
      final Point vertex = left.get(i);
      vertices.push_back(vertex);
    }

    for (int i = beginRight; i != endRight; ++i)
    {
      final Point vertex = right.get(i);
      vertices.push_back(vertex);
    }

    return vertices;
  }


  // returns the pivot point farthest from the baseline PQ
  private Point pivot (final Stack points)
  {
    final int last = (points.size() - 1);
    final Point P = points.get(0);
    final Point Q = points.get(last);
    final Vector PQ = new Vector(P, Q);

    int id = -1;
    double dmax = Double.NEGATIVE_INFINITY;
    for (int i = 1; i != last; ++i)
    {
      final Point R = points.get(i);
      final Vector PR = new Vector(P, R);
      final double d = Vector.trans(PQ, PR);
      if (d > dmax)
      {
	dmax = d;
	id = i;
      }
    }

    final Point pivot = points.get(id);
    return pivot;
  }


  // divides into "left" and "right" partitions at the pivotal point
  private void divide (final Stack points, Stack left, Stack right)
  {
    final int size = points.size();
    final int last = (size - 1);
    final Point P = points.get(0);
    final Point Q = points.get(last);
    final Point pivot = this.pivot(points);

    left.push_back(P);
    // pushes points in the same direction as the pivot (above or below the baseline)
    for (int i = 1; i != last; ++i)
    {
      final Line baseline = new Line(P, Q);
      final int sign = baseline.sign(pivot);
      final Point point = points.get(i);
      final Line line = new Line(P, pivot);
      if ( (line.getdX() != 0) && (line.sign(point) == sign) )
      {
	left.push_back(point);
      }
    }
    left.push_back(pivot);

    right.push_back(Q);
    for (int i = 1; i != last; ++i)
    {
      final Line baseline = new Line(P, Q);
      final int sign = baseline.sign(pivot);
      final Point point = points.get(i);
      final Line line = new Line(Q, pivot);
      if ( (line.getdX() != 0) && (line.sign(point) == sign) )
      {
	right.push_back(point);
      }
    }
    right.push_back(pivot);
  }


  // looks recursively for the vertices of the convex hull
  private Stack recurse (final Stack points)
  {
    final int size = points.size();
    if (size == 2)
    {
      return this.direct(points);
    }
    else
    {
      Stack left = new Stack(size);
      Stack right = new Stack(size);
      this.divide(points, left, right);
      Stack leftVertices = this.recurse(left);
      Stack rightVertices = this.recurse(right);
      return this.combine(leftVertices, rightVertices);
    }
  }


  // implements the divide and conquer method that finds the vertices of the convex hull
  public Stack convexHull ()
  {
    // sorts the points to support the divide and conquer algorithm:

    Stack points = new Stack(this.data);
    points.sort();

    // constructs the baseline for the first division:

    final int size = points.size();
    final int last = (size - 1);
    final Point P = points.get(0);
    final Point Q = points.get(last);
    final Line baseline = new Line(P, Q);

    // divides into left and right partitions:

    Stack left = new Stack(size);
    // stacks points below the baseline including the vertices P and Q
    for (int i = 0; i != size; ++i)
    {
      final Point point = points.get(i);
      final int sign = baseline.sign(point);
      if (sign <= 0)
      {
	left.push_back(point);
      }
    }

    Stack right = new Stack(size);
    // stacks points above the baseline including the vertices P and Q
    for (int i = 0; i != size; ++i)
    {
      final Point point = points.get(i);
      final int sign = baseline.sign(point);
      if (sign >= 0)
      {
	right.push_back(point);
      }
    }

    // applies the divide and conquer scheme on each partition:

    Stack verticesLeft = this.recurse(left);
    Stack verticesRight = this.recurse(right);

    // combines and yields the vertices of the convex hull in clockwise order
    return this.clockwise( this.combine(verticesLeft, verticesRight) );
  }


  // tests:


  // tests if `Line.sign()' detects sign changes properly
  private static void testLineLocMethod ()
  {
    int x1 = 0, y1 = 0;
    int x2 = 4, y2 = 4;
    int xi = -1, yi = -1;
    Point P = new Point(x1, y1);
    Point Q = new Point(x2, y2);
    Point R = new Point(xi, yi);

    Line line = new Line(P, Q);

    System.out.printf("on the line (0): %d\n", line.sign(R));

    xi = -1;
    yi = -2;
    Point S = new Point(xi, yi);
    System.out.printf("below line (-1): %d\n", line.sign(S));

    xi = -2;
    yi = -1;
    Point T = new Point(xi, yi);
    System.out.printf("above line (1): %d\n", line.sign(T));
  }


  private static void testBruteForceMethod () throws RejectedHullException
  {
    // creates the data set of 2D Points
    Stack points = genDataSet();
    ConvexHull hull = new ConvexHull(points);
    // finds the vertices of the convex hull with brute force
    Stack vertices = hull.bruteforce();
    // displays the coordinates of the vertices on the console
    System.out.println("BruteForce:");
    vertices.print();
  }


  private static void testDivideAndConquerMethod () throws RejectedHullException
  {
    Stack points = genDataSet();
    ConvexHull hull = new ConvexHull(points);
    Stack verticesBruteForce = hull.bruteforce();
    System.out.println("BruteForce:");
    verticesBruteForce.print();

    Stack vertices = hull.convexHull();
    System.out.println("Divide-And-Conquer:");
    vertices.print();
  }


  private static void test ()
  {
    int size = 8;
    final int runs = 8;
    final int reps = 256;
    boolean failed = false;
    for (int run = 0; run != runs; ++run)
    {
      for (int rep = 0; rep != reps; ++rep)
      {
	int sw = 1;
	Stack points = genDataSet(size);
	ConvexHull hull = new ConvexHull(points);
	Stack verticesBruteForce = new Stack(size);
	while (sw == 1)
	{
	  try
	  {
	    verticesBruteForce = hull.bruteforce();
	    sw = 0;
	  }
	  catch (RejectedHullException e)
	  {
	    points = genDataSet(size);
	    hull = new ConvexHull(points);
	  }
	}

	Stack vertices = hull.convexHull();

	failed = !Stack.isEqual(verticesBruteForce, vertices);
	if (failed)
	{
	  System.out.println("BruteForce:");
	  verticesBruteForce.print();
	  System.out.println("Divide-And-Conquer:");
	  vertices.print();
	  System.out.println("points:");
	  points.print();
	  break;
	}
      }

      if (failed)
      {
	break;
      }

      size *= 2;
    }

    System.out.print("test: ");
    if (failed)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("PASS");
    }
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
