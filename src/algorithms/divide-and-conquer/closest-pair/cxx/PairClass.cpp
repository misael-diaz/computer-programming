/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Implements the Closest Pair Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include "PairClass.h"


// (de)constructors:


Pair::Pair ()
// default constructor, constructs an infinitely separated pair of points
{
  // gets the binary floating-point representation of positive infinity
  double inf = std::numeric_limits<double>::infinity();
  this -> first = new Point();
  this -> second = new Point(0, inf);
  this -> distance = inf;

  // adds the dynamically allocated Point objects to `this' handler
  this -> handler.add(this -> first);
  this -> handler.add(this -> second);
}


Pair::Pair (Point* p, Point* q, double d) : distance(d)
// constructs from a pair of (read-only) points and their distance
{
  if ( p -> compareTo(q) < 0 )
  {
    this -> first = p;
    this -> second = q;
  }
  else
  {
    this -> first = q;
    this -> second = p;
  }

  /*

  NOTE:
  We need not to add the Point objects P and Q to this handler, for these
  objects must already be managed by a handler.

  */
}


// destructor
Pair::~Pair () {}


// getters:


double Pair::getDistance () const
// returns the separating distance of the points that comprise the pair
{
  return (this -> distance);
}


// methods:


void Pair::copy (const Pair *pair)
// implements a copy method for copying the components of pair objects
{
  this -> first = pair -> first;
  this -> second = pair -> second;
  this -> distance = pair -> distance;
}


void Pair::print () const
// prints info about the pair object
{
  // prints the coordinates of the first point on the console
  std::cout << "first:" << std::endl;
  this -> first -> print();

  // prints the coordinates of the second point on the console
  std::cout << "second:" << std::endl;
  this -> second -> print();

  // prints the separating distance of the points on the console
  std::cout << std::scientific << std::setprecision(15)
    << "distance: " << (this -> distance) << std::endl;
}


bool Pair::equidistant (const Pair* pair) const
/*

Synopsis:
Returns true if `this' pair and the other `pair' have equidistant points; that is,
the points that comprise `this' pair and the points that comprise the other `pair'
are separated by an equal distance.

Input:
pair		the other pair (read-only access)

Output:
boolean		true if the comprising points are equidistant, false otherwise

*/
{
  double d1 = this -> distance;
  double d2 = pair -> distance;

  return (d1 == d2);
}


int Pair::compareTo (const Pair* pair) const
/*

Synopsis:
Returns true if `this' pair is less that the other `pair'; that is, the separating
distance of the points that comprise `this' pair is less than the separating
distance of the points that comprise the other `pair'.

Input:
pair		the other pair (read-only access)

Output:
compare		returns 0 if `this' pair is equal to the other `pair' (equidistant),
		returns 1 if `this' pair is greater than the other `pair',
		and returns -1 if `this' pair is less than the other `pair'

*/
{
  double d1 = this -> distance;
  double d2 = pair -> distance;
  return Comparator(d1, d2);
}


bool Pair::equalTo (const Pair* pair) const
/*

Synopsis:
Defines what it means for two Pair objects to be equal to one another.
Two pair objects are equal if both the first and second points that comprise
them are equal.

Input:
pair		a pair object (referred to as that pair)

Output:
equal		true if `this pair' and `that pair' are equal, false otherwise

COMMENTS:
Since we make sure that the first point is smaller than the second point when
constructing a Pair object we need not to consider other cases.

*/
{
  // checks if the first points are equal
  bool first = (this -> first) -> equalTo(pair -> first);
  // checks if the second points are equal
  bool second = (this -> second) -> equalTo(pair -> second);

  if (first && second)
    return true;
  else
    return false;
}


// static methods contained in the pair namespace


Pair* pair::min(Pair* first, Pair* second)
// returns (reference to) the smallest of the two Pair objects
{
  if ( first -> compareTo(second) )
  {
    return first;
  }
  else
  {
    return second;
  }
}
