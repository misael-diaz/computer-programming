/*
 * Algorithms and Complexity                               October 08, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Implements the Brute Force and Divide and Conquer Algorithms that solve
 * the Closest Pair problem.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] Sorting Custom Objects Tutorial: (
 *      www.codejava.net/java-core/collections/
 *      sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */

import java.util.function.Function;

public class Ensemble	// Particle Ensemble Class
{

  interface Distance	// interface of distance computing methods
  {
    public double getDistance(Point P, Point Q);
  }


  // class Tuple
  //
  // Defines an auxiliary Tuple Class to encapsulate the closest pair and the number of
  // operations (or equivalently, the number of distance computations) done to find the
  // closest pair.


  private final class Tuple
  {
    final private Pair closestPair;			// closest pair
    final private double numOperations;			// number of operations

    Tuple (Pair closestPair, double numOperations)
    {
      this.closestPair = closestPair;
      this.numOperations = numOperations;
    }

    // getters:

    public Pair getClosestPair ()
    {
      return this.closestPair;
    }

    public double getNumOperations ()
    {
      return this.numOperations;
    }
  }


  // components:


  private double elapsedTime;		// elapsed time (nanoseconds)
  private double numOperations;		// operations (computed distances) counter
  private int size;			// ensemble size (or number of points)


  // constructors:


  Ensemble (int size)	// creates an ensemble of distinct points of size `size'.
  {
    this.isInvalidEnsembleSize(size);	// complains if invalid
    this.elapsedTime = 0.0;		// zeroes the elapsed time
    this.numOperations = 0.0;		// zeroes the number of operations
    this.size = size;			// sets the ensemble size
  }


  // getters:


  // gets the number of operations spent on finding the closest pair
  public double getOperations ()
  {
    return this.numOperations;
  }


  // gets the elapsed-time invested on finding the closest pair
  public double getElapsedTime ()
  {
    return this.elapsedTime;
  }


  // gets the ensemble size (or equivalently, the number of points)
  public int size ()
  {
    return this.size;
  }


  // Pair bruteForce () throws ImplementErrorException
  //
  // Synopsis:
  // Applies the Brute Force Algorithm to obtain the closest pair.
  //
  // Inputs:
  // None
  //
  // Output:
  // closestPair	the closest pair


  public Pair bruteForce () throws ImplementErrorException
  {
    // creates a new dataset of distinct points
    Vector<Point> points = this.createDataset1D();

    // saves the closest pair found by the recursive algorithm
    Pair closestPairRecursive = this.timeRecursive1D(points);

    // uses the brute force algorithm to find the closest pair
    Pair closestPair = this.timeBruteForce(points);

    // complains if the closest pairs are different
    if ( !closestPair.equalTo(closestPairRecursive) )
    {
      String errmsg = ("different closest pairs found");
      throw new ImplementErrorException(errmsg);
    }

    return closestPair;
  }


  // Pair recursive1D () throws ImplementErrorException
  //
  // Synopsis:
  // Finds the closest pair via the 1D Divide and Conquer Algorithm.
  //
  // Inputs:
  // None
  //
  // Output:
  // closestPair	the closest pair


  public Pair recursive1D () throws ImplementErrorException
  {
    // creates a new dataset of distinct points
    Vector<Point> points = this.createDataset1D();

    // saves the closest pair found by the brute force method
    Pair closestPairBruteForce = this.timeBruteForce(points);

    // finds the closest pair via divide and conquer algorithm
    Pair closestPair = this.timeRecursive1D(points);

    // complains if the closest pairs are different
    if ( !closestPair.equalTo(closestPairBruteForce) )
    {
      String errmsg = ("different closest pairs found");
      throw new ImplementErrorException(errmsg);
    }

    return closestPair;
  }


  // Pair recursive2D () throws ImplementErrorException
  //
  // Synopsis:
  // Finds the closest pair via the 2D Divide and Conquer Algorithm.
  //
  // Inputs:
  // None
  //
  // Output:
  // closestPair	the closest pair


  public Pair recursive2D () throws ImplementErrorException
  {
    // creates a new x-y sorted dataset of distinct points
    Vector<Point> Px = this.createDataset2D();

    // creates another vector with the y-x sorted points
    Vector<Point> Py = new Vector<>(Px);
    Py.sort( new Point.Comparator() );

    // saves the closest pair found by the brute force method
    Pair closestPairBruteForce = this.timeBruteForce(Px);

    // finds the closest pair via divide and conquer algorithm
    Pair closestPair = this.timeRecursive2D(Px, Py);

    // complains if the closest pairs are different
    if ( !closestPair.equalTo(closestPairBruteForce) )
    {
      String errmsg = ("different closest pairs found");
      throw new ImplementErrorException(errmsg);
    }

    return closestPair;
  }


  // Pair recursive3D () throws ImplementErrorException
  //
  // Synopsis:
  // Finds the closest pair via the 3D Divide and Conquer Algorithm.
  //
  // Inputs:
  // None
  //
  // Output:
  // closestPair	the closest pair


  public Pair recursive3D () throws ImplementErrorException
  {
    // creates a new xyz sorted dataset of distinct points
    Vector<Point> Px = this.createDataset3D();

    // creates yxz and zxy sorted counterparts
    Vector<Point> Py = new Vector<>(Px);
    Py.sort( new Point3D.yPosComparator() );

    Vector<Point> Pz = new Vector<>(Px);
    Pz.sort( new Point3D.zPosComparator() );

    Point [] points = Px.get();
    // saves the closest pair found by the brute force method
    Tuple dataBruteForce = this.distance(points);
    Pair closestPairBruteForce = dataBruteForce.getClosestPair();

    // finds the closest pair via divide and conquer algorithm
    Pair closestPair = this.timeRecursive3D(Px, Py, Pz);

    // complains if the closest pairs are different
    if ( !closestPair.equalTo(closestPairBruteForce) )
    {
      String errmsg = ("different closest pairs found");
      throw new ImplementErrorException(errmsg);
    }

    return closestPair;
  }


  public class Random	// defines the Pseudo Random Number Generator PRNG Utility
  {
    // components:

    private java.util.Random rand;		// PRNG

    // constructor(s):

    Random ()					// constructs a PRNG
    {
      this.rand = new java.util.Random();
    }

    // method(s):

    // Synopsis: returns a double in the asymmetric range [min, max)
    double nextDouble (double minValue, double maxValue)
    {
      double min = Math.floor(minValue);
      double max = Math.floor(maxValue);
      double r = min + rand.nextDouble() * (max - min);
      return Math.floor(r);
    }
  }


  // void main (String [] args) throws ImplementErrorException
  //
  // Synopsis:
  // Executes the test codes to check for implementation errors. An
  // implementation error happens if the Brute Force and Divide and
  // Conquer Algorithms find a different closest pair. We are certain
  // of that because we have made sure that the dataset has a unique
  // closest pair; that is, there are no other pairs having the same
  // separating distance.


  public static void main (String [] args) throws ImplementErrorException
  {
    // tests the 1D, 2D, and 3D Divide and Conquer Algorithms
    test1D();
    test2D();
    test3D();

    //test();
  }


  // implementations:


  // Pair timeBruteForce (Vector<Point> points)
  //
  // Synopsis:
  // Applies the Brute Force Algorithm to obtain the closest pair.
  // Sets the elapsed-time (nanoseconds) invested in determining the
  // closest pair. It also sets the number of operations (or the number
  // of distance computations) executed by the Brute Force algorithm to
  // find the closest pair.
  //
  // Inputs:
  // points		dataset of distinct points
  //
  // Output:
  // closestPair	the closest pair


  private Pair timeBruteForce (Vector<Point> points)
  {
    // complains if invalid
    this.isInvalidData(points);

    double startTime = System.nanoTime();
    // times the Brute Force Algorithm
    Tuple data = this.distance(points);
    double endTime = System.nanoTime();

    // sets the elapsed time (nanoseconds)
    this.elapsedTime = (endTime - startTime);
    // sets the number of operations
    this.numOperations = data.getNumOperations();

    Pair closestPair = data.getClosestPair();
    return closestPair;
  }


  // Pair timeRecursive1D (Vector<Point> points)
  //
  // Synopsis:
  // Applies the 1D Divide and Conquer Algorithm to find the closest pair.
  // Sets the elapsed-time (nanoseconds) invested in determining the
  // closest pair. It also sets the number of operations (or the number
  // of distance computations) executed by the algorithm to find the closest
  // pair.
  //
  // Inputs:
  // points		dataset of distinct points
  //
  // Output:
  // closestPair	the closest pair


  private Pair timeRecursive1D (Vector<Point> points)
  {
    // complains if invalid
    this.isInvalidData(points);

    double startTime = System.nanoTime();
    // times the 1D Divide and Conquer Algorithm
    Tuple data = this.recurse(points);
    double endTime = System.nanoTime();

    // sets the elapsed time
    this.elapsedTime = (endTime - startTime);
    // sets the number of operations
    this.numOperations = data.getNumOperations();

    Pair closestPair = data.getClosestPair();
    return closestPair;
  }


  // Pair timeRecursive2D (Vector<Point> points)
  //
  // Synopsis:
  // Applies the 2D Divide and Conquer Algorithm to find the closest pair.
  // Sets the elapsed-time (nanoseconds) invested in determining the
  // closest pair. It also sets the number of operations (or the number
  // of distance computations) executed by the algorithm to find the closest
  // pair.
  //
  // Inputs:
  // points		dataset of distinct points
  //
  // Output:
  // closestPair	the closest pair


  private Pair timeRecursive2D (Vector<Point> Px, Vector<Point> Py)
  {
    // complains if invalid
    this.isInvalidData(Px);

    double startTime = System.nanoTime();
    // times the 2D Divide and Conquer Algorithm
    Tuple data = this.recurse(Px, Py);
    double endTime = System.nanoTime();

    // sets the elapsed time
    this.elapsedTime = (endTime - startTime);
    // sets the number of operations
    this.numOperations = data.getNumOperations();

    Pair closestPair = data.getClosestPair();
    return closestPair;
  }


  // Pair timeRecursive3D (Vector<Point> points)
  //
  // Synopsis:
  // Applies the 3D Divide and Conquer Algorithm to find the closest pair.
  // Sets the elapsed-time (nanoseconds) invested in determining the
  // closest pair. It also sets the number of operations (or the number
  // of distance computations) executed by the algorithm to find the closest
  // pair.
  //
  // Inputs:
  // Px		xyz sorted dataset of distinct points
  // Py		same dataset of points but yxz sorted
  // Pz		same dataset of points but zxy sorted
  //
  // Output:
  // closestPair	the closest pair

  private Pair timeRecursive3D (Vector<Point> Px, Vector<Point> Py, Vector<Point> Pz)
  {
    // complains if invalid
    this.isInvalidData(Px);

    double startTime = System.nanoTime();
    // times the 3D Divide and Conquer Algorithm
    Tuple data = this.recurse(Px, Py, Pz);
    double endTime = System.nanoTime();

    // sets the elapsed time
    this.elapsedTime = (endTime - startTime);
    // sets the number of operations
    this.numOperations = data.getNumOperations();

    Pair closestPair = data.getClosestPair();
    return closestPair;
  }


  // Tuple recurse (Vector<Point> Px)
  //
  // Synopsis:
  // Applies the 1D Divide and Conquer Algorithm to find the closest pair.
  // If the partition P is small enough, the method uses Brute Force to find
  // the closest pair. Otherwise, the method divides the partition P into
  // left and right partitions to look for the closest pair in each. Note
  // that the division step continues until the partitions are small enough
  // to use Brute Force (or the direct method). Then, the method combines
  // the solutions by selecting the smallest of the closest pair candidates
  // and by looking for the closest pair between partitions.
  //
  // The method returns a tuple containing the closest pair and the
  // number of operations (distance computations) invested to find the
  // closest pair.
  //
  // Input:
  // Px		x-y sorted coordinates of the particles
  //
  // Output:
  // tuple		the closest pair and the number of operations


  private Tuple recurse (Vector<Point> Px)
  {
    if (Px.size() <= 3)	// uses brute force on the smaller partition
    {
      return this.distance(Px);
    }
    else
    {
      // allocates the left and right partitions
      Vector<Point> Lx = new Vector<>();
      Vector<Point> Rx = new Vector<>();

      // divides dataset into left and right partitions
      this.divide(Px, Lx, Rx);

      // finds the closest pair in the left partition
      Tuple dataLeft = this.recurse(Lx);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.recurse(Rx);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // combines the left and right partitions
      Tuple data = this.combine(Lx, Rx, closestPair);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }


  // Tuple distance (Point[] part)
  //
  // Synopsis:
  // Applies the Brute Force Algorithm to find the closest pair in a
  // partition. Note that the partition could be the whole dataset.
  //
  // Inputs:
  // part		partition (or whole data set of points)
  //
  // Outputs:
  // tuple		the closest pair and the number of operations
  //
  // COMMENTS:
  // Brute Force method for the skeptical who believes that the values
  // of the z coordinates are not taken into account by the 3D Divide
  // And Conquer Algorithm.


  private Tuple distance (Point[] part)
  {
    // gets the partition size
    int sz = part.length;
    // initializes the closest pair
    Pair closestPair = new Pair();
    // considers all the distinct pairs to find the closest pair
    for (int i = 0; i != (sz - 1); ++i)
    {
      for (int j = (i + 1); j != sz; ++j)
      {
	Point p = part[i];
	Point q = part[j];
	// uses (x, y, z) coordinates to compute the distance
	double d = this.distance(p, q);
	Pair pair = new Pair(p, q, d);
	// updates the closest pair
	closestPair = Pair.min(pair, closestPair);
      }
    }

    double N = part.length;
    double numOperations = ( ( (N * (N - 1) ) / 2 ) );

    return ( new Tuple(closestPair, numOperations) );
  }


  // Tuple distance (Vector<Point> part)
  //
  // Synopsis:
  // Applies the Brute Force Algorithm to find the closest pair in a
  // partition. Note that the partition could be the whole dataset.
  //
  // Inputs:
  // part		partition (or whole data set of points)
  //
  // Outputs:
  // tuple		the closest pair and the number of operations


  private Tuple distance (Vector<Point> part)
  {
    // gets the partition size
    int sz = part.size();
    // initializes the closest pair
    Pair closestPair = new Pair();
    // considers all the distinct pairs to find the closest pair
    for (int i = 0; i != (sz - 1); ++i)
    {
      for (int j = (i + 1); j != sz; ++j)
      {
	Point p = part.get(i);
	Point q = part.get(j);
	double d = p.distance(q);
	Pair pair = new Pair(p, q, d);
	// updates the closest pair
	closestPair = Pair.min(pair, closestPair);
      }
    }

    double N = part.size();
    double numOperations = ( ( (N * (N - 1) ) / 2 ) );

    return ( new Tuple(closestPair, numOperations) );
  }


  // double distance (Point P, Point Q)
  //
  // Synopsis:
  // Returns the squared distance of a pair of 3D Points.
  //
  // Inputs:
  //
  // Output:
  // distance		squared distance between the pair of points


  private double distance (Point P, Point Q)
  {
    // complains of the points belong to the base Point class
    Point3D p = (Point3D) P, q = (Point3D) Q;

    double x1 = p.getX(), x2 = q.getX();
    double y1 = p.getY(), y2 = q.getY();
    double z1 = p.getZ(), z2 = q.getZ();

    return ( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1) );
  }


  // double distance (Vector<Point> part, int i, int j)
  //
  // Synopsis:
  // Returns the squared distance of a pair of particles (i, j) located in
  // the same partition.
  //
  // Inputs:
  // part	partition (or whole data set of points)
  // i		index of the ith particle in the left partition
  // j		index of the jth particle in the right partition
  //
  // Output:
  // d		squared distance of the ith and jth particles


  private double distance (Vector<Point> part, int i, int j)
  {
    // gets the points P and Q
    Point p = part.get(i), q = part.get(j);
    // delegates the computation of the squared distance
    return p.distance(q);
  }


  // Tuple distance (Vector<Point> L, Vector<Point> R, Pair closestPair)
  //
  // Synopsis:
  // Applies Brute Force Algorithm on the middle partition M. Note
  // that the middle partition is comprised by closest pair candidates
  // from the left and right partitions; it is not constructed explicitly.
  //
  // Inputs:
  // L			closest pair candidates in left partition
  // R			closest pair candidates right partition
  // closestPair	current closest pair
  //
  // Outputs:
  // tuple		the closest pair and the number of operations


  private Tuple distance (Vector<Point> L, Vector<Point> R, Pair closestPair)
  {
    for (Point p : L)
    {
      for (Point q : R)
      {
	double d = p.distance(q);
	Pair pair = new Pair(p, q, d);
	closestPair = Pair.min(pair, closestPair); // updates the closest pair accordingly
      }
    }

    // NOTE: includes the distance computations in combine()
    double N1 = L.size(), N2 = R.size();
    double numOperations = ( (N1 * N2) + (N1 + 1) + (N2 + 1) );

    return ( new Tuple(closestPair, numOperations) );
  }


  // void divide (Vector<Point> Px, Vector<Point> Lx, Vector<Point> Rx)
  //
  // Synopsis:
  // Divides dataset of points into left and right partitions.
  //
  // Inputs:
  // Px		original x-y sorted particle coordinates
  // Lx		an unset view
  // Rx		an unset view
  //
  // Outputs:
  // Lx		view of the left partition
  // Rx		view of the right partition


  private void divide (Vector<Point> Px, Vector<Point> Lx, Vector<Point> Rx)
  {
    int size = Px.size();		// gets the size of the given dataset
    int half = (Px.size() / 2);		// gets the half of the dataset size

    // creates views of the left and right parititions:

    Lx.view(Px, 0, half);		// view of the left partition
    Rx.view(Px, half, size);		// view of the right partition
  }


  // Tuple combine (Vector<Point> L, Vector<Point> R, Pair closestPair)
  //
  // Synopsis:
  // Looks for the closest pair at the interface of the left and right
  // partitions (dubbed as the middle partition M).
  //
  // Inputs:
  // L		left partition
  // R		right partition
  // closestPair	current closest pair
  //
  // Output:
  // tuple		the closest pair and the number of operations
  //
  // COMMENTS:
  // See comments at the end of the source code for more information about this method.


  private Tuple combine (Vector<Point> L, Vector<Point> R, Pair closestPair)
  {
    double d_min = closestPair.getDistance();

    // initializes arange [b, e) to empty [0, 0)
    int b2 = 0, e2 = 0;
    for (Point Q : R)				// Rigth Partition Traversal
    {
      // gets index of the rigthmost point in the left partition
      int last = (L.size() - 1);
      Point P = L.get(last);

      // computes the x-axis distance of the closest pair candidate
      double x1 = P.getX(), x2 = Q.getX();
      double d = (x2 - x1) * (x2 - x1);

      if (d < d_min)
	++e2;
      else
	break;
    }

    // initializes arange [b, e) to empty [size, size)
    int b1 = L.size(), e1 = L.size();
    for (int i = 0; i != L.size(); ++i)		// Left Partition Traversal
    {
      // gets index of (current) rightmost point in the left partition
      int j = L.size() - (i + 1);

      Point P = L.get(j);
      Point Q = R.get(0);

      // computes the x-axis distance of the closest pair candidate
      double x1 = P.getX(), x2 = Q.getX();
      double d = (x2 - x1) * (x2 - x1);

      if (d < d_min)
	--b1;
      else
	break;
    }

    // creates views containing the closest pair candidates
    Vector<Point> smallerL = new Vector<>(L, b1, e1);
    Vector<Point> smallerR = new Vector<>(R, b2, e2);
    // looks for the closest pair at the interface of the left and rigth partitions
    return this.distance(smallerL, smallerR, closestPair);
  }


  // Tuple combine (Vector<Point> L, Vector<Point> R, Pair closestPair, Distance fun)
  //
  // Synopsis:
  // Looks for the closest pair at the interface of the left and right
  // partitions (dubbed as the middle partition M).
  //
  // Inputs:
  // L			left partition
  // R			right partition
  // closestPair	current closest pair
  // fun		lambda, user-defined distance computing method along an axis
  //
  // Output:
  // tuple		the closest pair and the number of operations


  private Tuple combine (Vector<Point> L, Vector<Point> R, Pair closestPair, Distance fun)
  {
    double d_min = closestPair.getDistance();

    // initializes arange [b, e) to empty [0, 0)
    int b2 = 0, e2 = 0;
    for (Point Q : R)
    {
      // gets the x-axis position of the rigthmost point in the left
      int last = (L.size() - 1);
      Point P = L.get(last);

      // computes the distance (along the axis)
      double d = fun.getDistance(P, Q);

      if (d < d_min)
	++e2;
      else
	break;
    }

    // initializes arange [b, e) to empty [size, size)
    int b1 = L.size(), e1 = L.size();
    for (int i = 0; i != L.size(); ++i)
    {
      // gets index of (current) rigthmost point in the left partition
      int j = L.size() - (i + 1);

      Point P = L.get(j);
      Point Q = R.get(0);

      // computes the distance (along the axis)
      double d = fun.getDistance(P, Q);

      if (d < d_min)
	--b1;
      else
	break;
    }

    // creates views containing the closest pair candidates
    Vector<Point> smallerL = new Vector<>(L, b1, e1);
    Vector<Point> smallerR = new Vector<>(R, b2, e2);
    // looks for the closest pair at the interface of the left and rigth partitions
    return this.distance(smallerL, smallerR, closestPair);
  }


  // Tuple recurse (Vector<Point> Px, Vector<Point> Py)
  //
  // Synopsis:
  // Applies the 2D Divide and Conquer Algorithm to find the closest pair.
  // If the partition P is small enough, the method uses Brute Force to find
  // the closest pair. Otherwise, the method divides the partition P into
  // left and right partitions to look for the closest pair in each. Note
  // that the division step continues until the partitions are small enough
  // to use Brute Force (or the direct method). Then, the method combines
  // the solutions by selecting the smallest of the closest pair candidates
  // and by looking for the closest pair between partitions.
  //
  // The method returns a tuple containing the closest pair and the
  // number of operations (distance computations) invested to find the
  // closest pair.
  //
  // Input:
  // Px		x-y sorted partition
  // Py		y-x sorted partition
  //
  // Output:
  // tuple		the closest pair and the number of operations
  //
  // COMMENTS:
  // This 2D version of the Divide and Conquer Algorithm divides along
  // the y dimension as well whenever it makes sense to do so. The task
  // of dividing along the y dimension is carried out by the overloaded
  // divide() method.


  private Tuple recurse (Vector<Point> Px, Vector<Point> Py)
  {
    if (Px.size() <= 3)
    {
      // uses brute force on the smaller partition
      return this.distance(Px);
    }
    else
    {
      // allocs the x-y sorted left and right partitions
      Vector<Point> Lx = new Vector<>();
      Vector<Point> Rx = new Vector<>();

      // divides dataset into left and right partitions
      this.divide(Px, Lx, Rx);

      // allocs the y-x sorted left and right partitions
      Vector<Point> Ly = new Vector<>(Lx.size(), (Integer sz) -> new Point[sz]);
      Vector<Point> Ry = new Vector<>(Rx.size(), (Integer sz) -> new Point[sz]);

      // builds the y-x sorted left and right partitions
      this.sort(Py, Lx, Ly, Ry);

      // finds the closest pair in the left partition
      Tuple dataLeft = this.divide(Lx, Ly);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.divide(Rx, Ry);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // combines the left and right partitions
      Tuple data = this.combine(Lx, Rx, closestPair);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }


  // Tuple recurse (Vector<Point> Px, Vector<Point> Py, Vector<Point> Pz)
  //
  // Synopsis:
  // Applies the 3D Divide and Conquer Algorithm to find the closest pair.
  // If the partition P is small enough, the method uses Brute Force to find
  // the closest pair. Otherwise, the method divides the partition P into
  // left and right partitions to look for the closest pair in each. Note
  // that the division step continues until the partitions are small enough
  // to use Brute Force (or the direct method). Then, the method combines
  // the solutions by selecting the smallest of the closest pair candidates
  // and by looking for the closest pair between partitions.
  //
  // The method returns a tuple containing the closest pair and the
  // number of operations (distance computations) invested to find the
  // closest pair.
  //
  // Input:
  // Px		xyz sorted partition
  // Py		yxz sorted partition
  // Pz		zxy sorted partition
  //
  // Output:
  // tuple		the closest pair and the number of operations
  //
  // COMMENTS:
  // This 3D version of the Divide and Conquer Algorithm divides along
  // the y and z dimension as well whenever it makes sense to do so. The task
  // of dividing along the y dimension is carried out by the overloaded
  // divide3D() method.


  private Tuple recurse (Vector<Point> Px, Vector<Point> Py, Vector<Point> Pz)
  {
    if (Px.size() <= 3)
    {
      // uses brute force on the smaller partition
      return this.distance(Pz);
    }
    else
    {
      // allocs the xyz sorted left and right partitions
      Vector<Point> Lx = new Vector<>();
      Vector<Point> Rx = new Vector<>();

      // divides dataset into left and right partitions
      this.divide(Px, Lx, Rx);

      // builds the yxz sorted left and right partitions
      Vector<Point> Ly = new Vector<>(Lx);
      Ly.sort( new Point3D.yPosComparator() );

      Vector<Point> Ry = new Vector<>(Rx);
      Ry.sort( new Point3D.yPosComparator() );

      // finds the closest pair in the left partition
      Tuple dataLeft = this.divide3D(Lx, Ly);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.divide3D(Rx, Ry);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // combines the left and right partitions
      Tuple data = this.combine(Lx, Rx, closestPair);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }


  // Tuple divide (Vector<Point> Px, Vector<Point> Py)
  //
  // Synopsis:
  // If the partition is small enough, the method applies the Brute
  // Force Method to find the closest pair; otherwise it divides the
  // partition into two of four possible quadrants to find the closest pair.
  // If the partition P corresponds to the left partition, the method
  // divides it into the second and third quadrants; otherwise, the
  // method divides it into the first and fourth quadrants.
  //
  // Inputs:
  // Px		the x-y sorted left (right) partition
  // Py		the y-x sorted left (right) partition
  //
  // Output:
  // tuple		the closest pair and the number of operations


  private Tuple divide (Vector<Point> Px, Vector<Point> Py)
  {
    if (Px.size() <= 3)
    {
      return this.distance(Px);
    }
    else
    {
      // allocs the y-x sorted left and right partitions
      Vector<Point> Ly = new Vector<>();
      Vector<Point> Ry = new Vector<>();

      // divides into left and right partitions
      this.divide(Py, Ly, Ry);

      // constructs x-y sorted left and right partitions
      Vector<Point> Lx = new Vector<>(Ly);
      Vector<Point> Rx = new Vector<>(Ry);
      Lx.sort();
      Rx.sort();

      // finds the closest pair in the left partition
      Tuple dataLeft = this.recurse(Lx, Ly);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.recurse(Rx, Ry);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // defines the (squared) distance computation along the y axis
      Distance dist = (Point P, Point Q) -> {
	double y1 = P.getY(), y2 = Q.getY();
	double d = (y2 - y1) * (y2 - y1);
	return d;
      };

      // combines the left and right partitions
      Tuple data = this.combine(Ly, Ry, closestPair, dist);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }


  // Tuple divide3D (Vector<Point> Px, Vector<Point> Py)
  //
  // Synopsis:
  // If the partition is small enough, the method applies the Brute
  // Force Method to find the closest pair; otherwise it divides the
  // partition into two of four possible quadrants to find the closest pair.
  // If the partition P corresponds to the left partition, the method
  // divides it into the second and third quadrants; otherwise, the
  // method divides it into the first and fourth quadrants.
  //
  // Inputs:
  // Px		the xyz sorted left (right) partition
  // Py		the yxz sorted left (right) partition
  //
  // Output:
  // tuple		the closest pair and the number of operations


  private Tuple divide3D (Vector<Point> Px, Vector<Point> Py)
  {
    if (Px.size() <= 3)
    {
      return this.distance(Px);
    }
    else
    {
      // allocs the yxz sorted left and right partitions
      Vector<Point> Ly = new Vector<>();
      Vector<Point> Ry = new Vector<>();

      // divides into left and right partitions
      this.divide(Py, Ly, Ry);

      // constructs xyz sorted left and right partitions
      Vector<Point> Lx = new Vector<>(Ly);
      Lx.sort();

      Vector<Point> Rx = new Vector<>(Ry);
      Rx.sort();

      // constructs zxy sorted left and right partitions
      Vector<Point> Lz = new Vector<>(Ly);
      Lz.sort( new Point3D.zPosComparator() );

      Vector<Point> Rz = new Vector<>(Ry);
      Rz.sort( new Point3D.zPosComparator() );

      // finds the closest pair in the left partition
      Tuple dataLeft = this.divide3D(Lx, Ly, Lz);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.divide3D(Rx, Ry, Rz);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // defines the (squared) distance computation along the y axis
      Distance dist = (Point P, Point Q) -> {
	double y1 = P.getY(), y2 = Q.getY();
	double d = (y2 - y1) * (y2 - y1);
	return d;
      };

      // combines the left and right partitions
      Tuple data = this.combine(Ly, Ry, closestPair, dist);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }


  // Tuple divide3D (Vector<Point> Px, Vector<Point> Py, Vector<Point> Pz)
  //
  // Synopsis:
  // If the partition is small enough, the method applies the Brute
  // Force Method to find the closest pair; otherwise it divides into
  // two partitions along the z dimension.
  //
  // Inputs:
  // Px		the xyz sorted left (right) partition
  // Py		the yxz sorted left (right) partition
  // Pz		the zxy sorted left (right) partition
  //
  // Output:
  // tuple		the closest pair and the number of operations


  private Tuple divide3D (Vector<Point> Px, Vector<Point> Py, Vector<Point> Pz)
  {
    if (Px.size() <= 3)
    {
      return this.distance(Px);
    }
    else
    {
      // allocs the zxy sorted left and right partitions
      Vector<Point> Lz = new Vector<>();
      Vector<Point> Rz = new Vector<>();

      // divides into left and right partitions
      this.divide(Pz, Lz, Rz);

      // constructs xyz sorted left and right partitions
      Vector<Point> Lx = new Vector<>(Lz);
      Lx.sort();

      Vector<Point> Rx = new Vector<>(Rz);
      Rx.sort();

      // constructs yxz sorted left and right partitions
      Vector<Point> Ly = new Vector<>(Lz);
      Ly.sort( new Point3D.yPosComparator() );

      Vector<Point> Ry = new Vector<>(Rz);
      Ry.sort( new Point3D.yPosComparator() );

      // finds the closest pair in the left partition
      Tuple dataLeft = this.recurse(Lx, Ly, Lz);
      Pair closestPairLeft = dataLeft.getClosestPair();

      // finds the closest pair in the right partition
      Tuple dataRight = this.recurse(Rx, Ry, Rz);
      Pair closestPairRight = dataRight.getClosestPair();

      // selects the closest from the two partitions
      Pair closestPair = Pair.min(closestPairLeft, closestPairRight);

      // defines the (squared) distance computation along the z axis
      Distance dist = (Point p, Point q) -> {
	// complains if points belong to the base class
	Point3D P = (Point3D) p;
	Point3D Q = (Point3D) q;

	double z1 = P.getZ(), z2 = Q.getZ();
	double d = (z2 - z1) * (z2 - z1);
	return d;
      };

      // combines the left and right partitions
      Tuple data = this.combine(Lz, Rz, closestPair, dist);

      // updates the number of operations
      double numOperations = (dataLeft.getNumOperations() +
			      dataRight.getNumOperations() +
			      data.getNumOperations());

      closestPair = data.getClosestPair();
      return ( new Tuple(closestPair, numOperations) );
    }
  }

  // void sort (Vector<Point> Py, Vector<Point> Lx, Vector<Point> Ly, Vector<Point> Ry)
  //
  // Synopsis:
  // Sorts Py into left Ly and right Ry partitions. It does it work by
  // searching in Py for elements in the left partition Lx. Elements
  // found to be contained in the left partition Lx are copied into Ly.
  // And those that are not contained in the left must be in the right
  // partition; thus, the method copies them into Ry.
  //
  // Inputs:
  // Py			y-x sorted vector of points
  // Lx			x-y sorted left partition
  // Ly			preallocated vector of size Lx.size()
  // Ry			preallocated vector of size Rx.size()
  //
  // Outputs:
  // Ly			y-x sorted left partition
  // Ry			y-x sorted right partition

  private void sort(Vector<Point> Py, Vector<Point> Lx,
		    Vector<Point> Ly, Vector<Point> Ry)
  {
    for (Point p : Py)
    {
      if ( Lx.contains(p) )
      {
	Ly.push_back( () -> new Point(p) );
      }
      else
      {
	Ry.push_back( () -> new Point(p) );
      }
    }
  }


  // Vector<Point> createDataset1D ()
  //
  // Synopsis:
  // Creates a dataset of points that has no duplicate closest pairs;
  // that is, the second closest pair is farther away than the first
  // closest pair. This version invokes the method that creates points
  // uniformly distributed in a rectangular domain; that is, the
  // range of possible y-axis coordinates is fixed.
  //
  // Input:
  // None
  //
  // Output:
  // dataset		dataset of points with a unique closest pair


  private Vector<Point> createDataset1D ()
  {
    // creates a trial dataset of points
    Vector<Point> dataset = this.create();

    boolean hasDuplicateClosestPair = true;
    // creates a new dataset until there are no duplicates
    while (hasDuplicateClosestPair)
    {
      try
      {
	// checks for duplicated closest pairs
	this.hasDuplicateClosestPair(dataset);
	hasDuplicateClosestPair = false;
      }
      catch (DuplicatedClosestPairException e)
      {
	// creates a new dataset
	dataset = this.create();
      }
    }

    return dataset;
  }


  // Vector<Point> createDataset2D ()
  //
  // Synopsis:
  // Creates a dataset of points that has no duplicate closest pairs;
  // that is, the second closest pair is farther away than the first
  // closest pair. This version invokes the method that creates points
  // uniformly distributed in a rectangular domain. The possible range of
  // x, y coordinates grow with the ensemble size to keep the point
  // density constant regardless of the ensemble size.
  //
  // Input:
  // None
  //
  // Output:
  // dataset		dataset of points with a unique closest pair


  private Vector<Point> createDataset2D ()
  {
    // creates a trial dataset of points
    Vector<Point> dataset = this.create2D();

    boolean hasDuplicateClosestPair = true;
    // creates a new dataset until there are no duplicates
    while (hasDuplicateClosestPair)
    {
      try
      {
	// checks for duplicated closest pairs
	this.hasDuplicateClosestPair(dataset);
	hasDuplicateClosestPair = false;
      }
      catch (DuplicatedClosestPairException e)
      {
	// creates a new dataset
	dataset = this.create2D();
      }
    }

    return dataset;
  }


  // Vector<Point> createDataset3D ()
  //
  // Synopsis:
  // Creates a dataset of points that has no duplicate closest pairs;
  // that is, the second closest pair is farther away than the first
  // closest pair. This version invokes the method that creates points
  // uniformly distributed in a rectangular domain. The possible range of
  // x, y, and z coordinates grow with the ensemble size to keep the point
  // density constant regardless of the ensemble size.
  //
  // Input:
  // None
  //
  // Output:
  // dataset		dataset of points with a unique closest pair


  private Vector<Point> createDataset3D ()
  {
    // creates a trial dataset of 3d points
    Vector<Point> dataset = this.create3D();

    boolean hasDuplicateClosestPair = true;
    // creates a new dataset until there are no duplicates
    while (hasDuplicateClosestPair)
    {
      try
      {
	// checks for duplicated closest pairs
	this.hasDuplicateClosestPair(dataset);
	hasDuplicateClosestPair = false;
      }
      catch (DuplicatedClosestPairException e)
      {
	// creates a new dataset
	dataset = this.create3D();
      }
    }

    return dataset;
  }


  // Vector<Point> create ()
  //
  // Synopsis:
  // Generates a distinct dataset of Cartesian points by sampling
  // values from the uniform Pseudo-Random Number Generator PRNG
  // utility.
  //
  // Inputs:
  // None
  //
  // Output:
  // points		a vector that stores the dataset of points


  private Vector<Point> create ()
  {
    // creates a vector for storing the points
    Vector<Point> points = new Vector<>( (Integer sz) -> new Point[sz] );


    // creates a new Pseudo-Random Number Generator PRNG
    Ensemble.Random r = new Ensemble.Random();


    // defines limits for the point coordinates along the axes
    double size = this.size;
    double limit = (size * size);
    double x_min = -limit, x_max = limit;
    double y_min = -4, y_max = 4;


    // creates the set of distinct points
    for (int i = 0; i != this.size; ++i)
    {

      double x = r.nextDouble(x_min, x_max);
      double y = r.nextDouble(y_min, y_max);

      Point p = new Point(x, y);
      // creates a new point if already present in vector
      while ( points.contains(p) )
      {
	x = r.nextDouble(x_min, x_max);
	y = r.nextDouble(y_min, y_max);
	p = new Point(x, y);
      }

      // pushes (new) point unto the back of the vector
      Point point = new Point(p);
      points.push_back( () -> new Point(point) );
    }

    // sorts to support the divide and conquer algorithm
    points.sort();

    return points;
  }


  // Vector<Point> create2D ()
  //
  // Synopsis:
  // Generates a distinct dataset of Cartesian points by sampling
  // values from the uniform Pseudo-Random Number Generator PRNG
  // utility. This version spawns the points in a squared domain.
  //
  // Inputs:
  // None
  //
  // Output:
  // Px		a vector containing the x-y sorted points


  private Vector<Point> create2D ()
  {
    // creates a vector for storing exactly the ensemble
    Vector<Point> Px = new Vector<>(this.size, (Integer sz) -> new Point[sz]);

    // creates a new Pseudo-Random Number Generator PRNG
    Ensemble.Random r = new Ensemble.Random();


    // defines limits for the point coordinates along the axes
    double size = this.size;
    double limit = (size * size);
    double x_min = -limit, x_max = limit;
    double y_min = -limit, y_max = limit;


    // creates the set of distinct points
    for (int i = 0; i != this.size; ++i)
    {
      double x = r.nextDouble(x_min, x_max);
      double y = r.nextDouble(y_min, y_max);

      Point p = new Point(x, y);
      // creates a new point if already present in vector
      while ( Px.contains(p) )
      {
	x = r.nextDouble(x_min, x_max);
	y = r.nextDouble(y_min, y_max);
	p = new Point(x, y);
      }

      // pushes (new) point unto the back of the vector
      Point point = new Point(p);
      Px.push_back( () -> new Point(point) );
    }

    // sorts to support the divide and conquer algorithm
    Px.sort();

    return Px;
  }


  // Vector<Point> create3D ()
  //
  // Synopsis:
  // Generates a distinct dataset of Cartesian points by sampling
  // values from the uniform Pseudo-Random Number Generator PRNG
  // utility. This version spawns the points in a cubic domain.
  //
  // Inputs:
  // None
  //
  // Output:
  // Px		a vector containing the xyz sorted points


  private Vector<Point> create3D ()
  {
    // creates a vector for storing exactly the ensemble
    Vector<Point> Px = new Vector<>(this.size, (Integer sz) -> new Point3D[sz]);

    // creates a new Pseudo-Random Number Generator PRNG
    Ensemble.Random r = new Ensemble.Random();


    // defines limits for the point coordinates along the axes
    double size = this.size;
    double limit = (size * size);
    double x_min = -limit, x_max = limit;
    double y_min = -limit, y_max = limit;
    double z_min = -limit, z_max = limit;


    // creates the set of distinct points
    for (int i = 0; i != this.size; ++i)
    {
      double x = r.nextDouble(x_min, x_max);
      double y = r.nextDouble(y_min, y_max);
      double z = r.nextDouble(z_min, z_max);

      Point3D p = new Point3D(x, y, z);
      // creates a new point if already present in vector
      while ( Px.contains(p) )
      {
	x = r.nextDouble(x_min, x_max);
	y = r.nextDouble(y_min, y_max);
	z = r.nextDouble(z_min, z_max);
	p = new Point3D(x, y, z);
      }

      // pushes (new) point unto the back of the vector
      Point3D point = new Point3D(p);
      Px.push_back( () -> new Point3D(point) );
    }

    // sorts to support the divide and conquer algorithm
    Px.sort();

    return Px;
  }


  // input validation:


  // void hasDuplicateClosestPair (Vector<Point> points)
  //
  // Synopsis:
  // Uses the Brute Force Algorithm to find the first and the second
  // closest pairs. Throws an exception if their distances are equal.
  //
  // Input:
  // points		dataset of distinct points
  //
  // Output:
  // None


  private void hasDuplicateClosestPair (Vector<Point> points)
    throws DuplicatedClosestPairException
    {
      // gets the total number of points
      int sz = points.size();
      // initializes the first closest pair
      Pair firstClosestPair = new Pair();
      // initializes the the second closest pair
      Pair secondClosestPair = new Pair();
      // uses Brute Force to find the first and second closest pairs
      for (int i = 0; i != (sz - 1); ++i)
      {
	for (int j = (i + 1); j != sz; ++j)
	{
	  Point p = points.get(i);
	  Point q = points.get(j);
	  double d = p.distance(q);
	  Pair pair = new Pair(p, q, d);

	  // updates the first and second closest pairs
	  if (pair.compareTo(firstClosestPair) <= 0)
	  {
	    secondClosestPair = firstClosestPair;
	    firstClosestPair = pair;
	  }
	}
      }

      double d_2nd = secondClosestPair.getDistance();
      double d_min = firstClosestPair.getDistance();

      // complains if the closest pairs have equal distances
      if (d_2nd == d_min)
      {
	throw new DuplicatedClosestPairException();
      }
    }


  // void isInvalidEnsembleSize (int size)
  //
  // Synopsis:
  // Complains if the requested ensemble size is invalid.
  //
  // Input:
  // size	requested ensemble size
  //
  // Output:
  // None


  private void isInvalidEnsembleSize (int size)
  {
    String err = ("the ensemble size must be greater or equal to two");
    if (size < 2)
    {
      throw new IllegalArgumentException(err);
    }
  }


  // void isInvalidData (Vector<Point> data) throws IllegalArgumentException
  //
  // Synopsis:
  // Complains if the dataset of points is not x-y sorted or if it has duplicates.
  //
  // Input:
  // data		dataset of points
  //
  // Ouput:
  // None


  private void isInvalidData (Vector<Point> data) throws IllegalArgumentException
  {
    // complains if the points are not x-y sorted
    if ( !this.isSorted(data) )
    {
      String err = "points must be sorted with respect to the x-axis in ascending order";
      throw new IllegalArgumentException(err);
    }

    // complains if there are duplicated points
    if ( this.hasDuplicates(data) )
    {
      String err = "points must be distinct";
      throw new IllegalArgumentException(err);
    }
  }


  // boolean hasDuplicates (Vector<Point> points)
  //
  // Synopsis:
  // Returns true if there are duplicated points, returns false otherwise.
  //
  // Input:
  // points		dataset of points
  //
  // Output:
  // hasDuplicates	true if there are duplicates, false otherwise


  private boolean hasDuplicates (Vector<Point> points)
  {
    // sorts to check for duplicates in pairs
    points.sort();

    int duplicates = 0;
    // obtains the total number of duplicates
    for (int i = 0; i != (points.size() - 1); ++i)
    {
      Point P = points.get(i);
      Point Q = points.get(i + 1);

      if (P.compareTo(Q) == 0)
	duplicates += 1;
      else
	duplicates += 0;
    }

    if (duplicates != 0)
      return true;
    else
      return false;
  }


  // boolean isSorted (Vector<Point> points)
  //
  // Synopsis:
  // Returns true if the points are sorted in ascending order with
  // respect to their x-axis coordinates, returns false otherwise.
  //
  // Input:
  // points		dataset of points
  //
  // Output:
  // isSorted	true if sorted, false otherwise


  private boolean isSorted (Vector<Point> points)
  {

    int misplacements = 0;
    // gets the number of misplaced (or out-of-order) points
    for (int i = 0; i != (points.size() - 1); ++i)
    {
      Point P = points.get(i);
      Point Q = points.get(i + 1);

      if (P.compareTo(Q) > 0)
	misplacements += 1;
      else
	misplacements += 0;
    }

    if (misplacements != 0)
      return false;
    else
      return true;
  }


  // tests:


  // void test () throws ImplementErrorException
  //
  // Synopsis:
  // Checks for implementation errors. If the closest pair found by the
  // Brute Force algorithm is not the same as that found by the Divide
  // And Conquer algorithm an Implementation Error Exception is thrown.
  // This version uses the 2D Divide And Conquer Algorithm that divides
  // along the x and y axes, for the points span a squared domain in
  // space.
  //
  // The ensemble has a predetermined size.
  //
  // Inputs:
  // None
  //
  // Outputs:
  // None


  private static void test () throws ImplementErrorException
  {
    int size = 2;
    Ensemble ens = new Ensemble(size);
    Pair closestPair = ens.recursive2D();
  }


  // void test1D () throws ImplementErrorException
  //
  // Synopsis:
  // Checks for implementation errors. If the closest pair found by the
  // Brute Force algorithm is not the same as that found by the Divide
  // And Conquer algorithm an Implementation Error Exception is thrown.
  // This version uses the 1D Divide And Conquer Algorithm that divides
  // along the largest dimension (the x-axis by design).
  //
  // The test is performed for ensembles having sizes in the asymmetric
  // range [2, 4096). A new ensemble is created each time to change the
  // location of the closest pair. Note that the closest pair may be
  // present in or between (the smaller) partitions (or subdomains).
  //
  // Inputs:
  // None
  //
  // Outputs:
  // None


  private static void test1D () throws ImplementErrorException
  {
    for (int size = 2; size != (0x00001000); size *= 2)
    {
      for (int i = 0; i != 256; ++i)
      {
	Ensemble ens = new Ensemble(size);
	Pair closestPair = ens.recursive1D();
      }
    }
  }


  // void test2D () throws ImplementErrorException
  //
  // Synopsis:
  // Checks for implementation errors. If the closest pair found by the
  // Brute Force algorithm is not the same as that found by the Divide
  // And Conquer algorithm an Implementation Error Exception is thrown.
  // This version uses the 2D Divide And Conquer Algorithm that divides
  // along the x and y axes, for the points span a rectangular domain.
  //
  // The test is performed for ensembles having sizes in the asymmetric
  // range [2, 4096). A new ensemble is created each time to change the
  // location of the closest pair. Note that the closest pair may be
  // present in or between (the smaller) partitions (or subdomains).
  //
  // Inputs:
  // None
  //
  // Outputs:
  // None


  private static void test2D () throws ImplementErrorException
  {
    for (int size = 2; size != (0x00001000); size *= 2)
    {
      for (int i = 0; i != 256; ++i)
      {
	Ensemble ens = new Ensemble(size);
	Pair closestPair = ens.recursive2D();
      }
    }
  }


  // void test3D () throws ImplementErrorException
  //
  // Synopsis:
  // Checks for implementation errors. If the closest pair found by the
  // Brute Force algorithm is not the same as that found by the Divide
  // And Conquer algorithm an Implementation Error Exception is thrown.
  // This version uses the 3D Divide And Conquer Algorithm that divides
  // along the x, y, and z axes, for the points span a cubic domain.
  //
  // The test is performed for ensembles having sizes in the asymmetric
  // range [2, 4096). A new ensemble is created each time to change the
  // location of the closest pair. Note that the closest pair may be
  // present in or between (the smaller) partitions (or subdomains).
  //
  // Inputs:
  // None
  //
  // Outputs:
  // None

  private static void test3D () throws ImplementErrorException
  {
    for (int size = 2; size != (0x00001000); size *= 2)
    {
      for (int i = 0; i != 256; ++i)
      {
	Ensemble ens = new Ensemble(size);
	Pair closestPair = ens.recursive3D();
      }
    }
  }
}


// COMMENTS:
//
// combine()
//
//
// Comments on the distance computations:
//
//
// Even though we  are refering in the comments of the combine() method to the distance
// along the x axis, we are really computing the squared (x2 - x1)^2 because we are
// comparing that quantity against the squared distance of the current closest pair:
//
//       		d^2 = (x2 - x1)^2 + (y2 - y1)^2
//
// in order to determine if the considered pair of points could be closer.
//
//
// Comments on the Right Partition Traversal:
//
//
// Assumes that there are no particles on the right partition
// that could form a pair with any of the particles on the
// left partition which could be closer than the (current)
// closest pair. (Recall that at this point we have already
// considered all possible pairs within the partitions, thus
// we need not to do that again.)
//
// To keep track of the number of potential candidates we use
// the asymmetric range (arange for short) [b, e), where `b'
// is the beginning limit and `e' is the end limit. The number
// of candidates in a particular partition is computed via the
// difference (e - b).
//
// By initializing the range to zero (or empty) we are saying
// that there are no potential closest pair candidates between
// the left and right partitions in accordance with our
// initial assumption.
//
// The algorithm that follows (for-loop) determines how many
// particles in the right partition could potentially form
// a pair closer than the (current) closest pair.
//
// Candidate particles on the right partition are selected
// by incremening the end of the arange [b, e). Note that the
// first particle on the right partition is the closest to
// the particles on the left partition and so the arange must
// start with b = 0; also, this is why we only adjust the end
// limit. We update the end limit by traversing the particles
// in the right partition in sequential order. We can afford
// to do that because of the inherent ordering which stems
// from the imposed x-axis sorting.
//
// If it turns out that there are no candidates, the arange
// remains empty [b, e) = [0, 0), as it should be.
//
//
// Comments on the for-loop where the traversal of R takes place - for (Point Q : R):
//
//
// Computes the distance between the rightmost particle P in
// the left partition and the particle Q on the right
// partition.
//
// If the distance of the pair PQ (along the x-axis) is
// smaller than the (current) distance of the closest pair,
// the particle Q is included in the list of (potential)
// candidates by incrementing the end limit of the asymmetric
// range.
//
// We stop the traversal as soon as there is a pair farther
// than the closest pair, for it is obvious that the next one
// is going to be even farther away owing to the sorting.
//
//
// Comments on the Left Partition Traversal:
//
//
// As above but the traversal is reversed. Again, recall that
// the rightmost particle in the left partition is the closest
// to the right partition and so the end limit is fixed and
// the begin limit of the arange [b, e) gets decremented to
// add particles into the list of closest pair candidates.
//
//
// Comments on the closest pair finding scheme at the interface of the left and rigth
// partitions, also known as the middle partition:
//
//
// Applies the brute force algorithm to find the closest pair
// in the middle partition. The middle partition consists of
// the possible closest pairs formed by particles in the left
// and right partitions. The number of candidates in the left
// partition is given by (e1 - b1) and those in the right
// partition is given by (e2 - b2). If it turns out that there
// are no candidates the (current) closest pair is returned.
