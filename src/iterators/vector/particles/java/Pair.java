/*
 * Algorithms and Complexity                            December 01, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines the Pair (of Points) Class.
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
 *
 */

public final class Pair implements Comparable<Pair>
{

  // component(s):


  private final Point first;			// (smaller) first point
  private final Point second;			// second point
  private final double distance;		// separating distance


  // constructor(s):


  Pair ()					// default constructor
  {
    double inf = Double.POSITIVE_INFINITY;
    this.first = new Point(0, 0);
    this.second = new Point(inf, 0);
    this.distance = inf;
  }


  // Synopsis: constructs from a pair of points and their distance
  Pair (Point P, Point Q, double distance)
  {
    // places the smaller Point first by design
    if (P.compareTo(Q) < 0)
    {
      this.first = P;
      this.second = Q;
      this.distance = distance;
    }
    else
    {
      this.first = Q;
      this.second = P;
      this.distance = distance;
    }
  }


  Pair (Pair pair)				// copy constructor
  {
    this.first = new Point(pair.first);
    this.second = new Point(pair.second);
    this.distance = pair.distance;
  }


  // getters:


  public Point getFirst ()			// returns the first point
  {
    return this.first;
  }


  public Point getSecond ()			// returns the second point
  {
    return this.second;
  }


  public double getDistance ()			// returns the separating distance
  {
    return this.distance;
  }


  // methods:


  //  int compareTo (Pair pair)
  //
  //  Synopsis:
  //  Implements the comparable interface.
  //  Compares Pair objects by their separating distance.
  //
  //  Input:
  //  pair	a pair object (referred to as that pair)
  //
  //  Output:
  //  comp	0 if `this pair' is equal to `that pair',
  //   		1 if `this pair' is greater than `that pair',
  //   		and -1 if `this pair' is less than `that pair'
  //
  //  COMMENTS:
  //  Because we assign integral values to the coordinates of the points
  //  and these have exact binary floating-point representations, we can
  //  compare doubles as done here with confidence.
  //


  @Override
  public int compareTo (Pair pair)
  {
    // computes the difference in their distances
    double diff = (this.distance - pair.distance);

    if (diff == 0.0)
    {
      return 0;
    }
    else if (diff < 0.0)
    {
      return -1;
    }
    else
    {
      return 1;
    }
  }


  //  boolean equalTo (Pair pair)
  //
  //  Synopsis:
  //  Defines what it means for two Pair objects to be equal to one another.
  //  Two pair objects are equal if both the first and second points that comprise them
  //  are equal.
  //
  //  Input:
  //  pair		a pair object (referred to as `that pair')
  //
  //  Output:
  //  equal		true if `this pair' and `that pair' are equal,
  //   			false otherwise
  //
  //  COMMENTS:
  //  Since we make sure that the first point is smaller than the second
  //  point when constructing a Pair we need not to consider other cases.


  public boolean equalTo (Pair pair)
  {
    // checks if the first points are equal
    boolean first = this.first.compareTo(pair.first) == 0;
    // checks if the second points are equal
    boolean second = this.second.compareTo(pair.second) == 0;

    if (first && second)
      return true;
    else
      return false;
  }


  //  Pair min (Pair first, Pair second)
  //
  //  Synopsis:
  //  Returns the smallest of the given Pairs.
  //
  //  Inputs:
  //  first		first pair
  //  second 		second pair
  //
  //  Output:
  //  min		the smallest of the two pairs


  public static Pair min (Pair first, Pair second)
  {
    if (first.compareTo(second) < 0)
      return first;
    else
      return second;
  }
}
