/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Implements the Cartesian 2D and 3D Point classes.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include "PointClass.h"


/* Point Class */


/* (de)constructors */


Point::Point () : x(0), y(0) {}				// default constructor


Point::Point (double x, double y) : x(x), y(y) {}	// constructs via x, y coordinates


Point::~Point() {}					// destructor


/* getters */


double Point::getX () const				// returns the x-axis position
{
  return (this -> x);
}


double Point::getY () const				// returns the y-axis position
{
  return (this -> y);
}


// returns a zero z-axis position, for the point is a 2D point
double Point::getZ () const
{
  return 0;
}


/* methods */


// copies components from point objects
void Point::copy (const Point* point)
{
  this -> x = point -> x;
  this -> y = point -> y;
}


/*

Synopsis:
Returns the squared distance between `this' point and the other point `point'.

Input:
point		the other point object (read-only access)

Output:
distance	the squared distance

*/
double Point::distance (const Point* point) const
{
  double x1 = (this -> x), x2 = (point -> x);
  double y1 = (this -> y), y2 = (point -> y);

  return ( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) );
}


// prints the x, y coordinates of this point on the console
void Point::print () const
{
  std::cout << std::scientific << std::setprecision(15) << "x: "
	    << (this -> x) << " y: " << (this -> y) << std::endl;
}


/*

Synopsis:
Returns true if `this' point is equal to the other point `point', returns false
otherwise.

Input:
point		the other point object (read-only access)

Output:
equal		true if the points are equal, false otherwise

*/
bool Point::equalTo (const Point* point) const
{
  return ( (this -> x == point -> x) && (this -> y == point -> y) );
}


/*

Synopsis:
Returns true if `this' point is less than the other point `point', returns false
otherwise. Useful for sorting an array of Point objects primarily by their
x-axis coordinates and secondarily by their y-axis coordinates.

Input:
point		the other point object (read-only access)

Output:
compare		returns 0 if `this' point is equal to the other `point',
		returns 1 if `this' point is greater than the other `point',
		and returns -1 if `this' point is less than the other `point'

*/
int Point::compareTo (const Point* point) const
{
  if ( (this -> x) != (point -> x) )
  {
    return ( Comparator(this -> x, point -> x) );
  }
  else
  {
    return ( Comparator(this -> y, point -> y) );
  }
}


// as compareTo() but primarily considers the y-axis coordinates
int Point::yPosCompareTo (const Point* p) const
{
  double x1 = this -> getX(), x2 = p -> getX();
  double y1 = this -> getY(), y2 = p -> getY();

  if (y1 != y2)
  {
    return ( Comparator(y1, y2) );
  }
  else
  {
    return ( Comparator(x1, x2) );
  }
}


// as compareTo() but primarily considers the z-axis coordinates
int Point::zPosCompareTo (const Point* p) const
{
  double x1 = this -> getX(), x2 = p -> getX();
  double y1 = this -> getY(), y2 = p -> getY();

  if (x1 != x2)
  {
    return ( Comparator(x1, x2) );
  }
  else
  {
    return ( Comparator(y1, y2) );
  }
}


/* Point3D Class */


/* (de)constructors */


// default constructor
Point3D::Point3D () : Point(), z(0) {}


// constructs from x, y, z coordinates
Point3D::Point3D (double x, double y, double z) : Point(x, y), z(z) {}


// destructor
Point3D::~Point3D () {}


/* getters */


// Overrides Point::getZ() by returning the z-axis coordinate
double Point3D::getZ () const
{
  return (this -> z);
}


/* methods */


// copies components from point objects
void Point3D::copy (const Point* point)
{
  this -> x = point -> getX();
  this -> y = point -> getY();
  this -> z = point -> getZ();
}


// Overrides the Point::distance method by considering the z-axis positions.
double Point3D::distance (const Point* point) const
{
  double x1 = ( this -> getX() ), x2 = ( point -> getX() );
  double y1 = ( this -> getY() ), y2 = ( point -> getY() );
  double z1 = ( this -> getZ() ), z2 = ( point -> getZ() );

  return ( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1) );
}


// Overrides Point::print() by printing the x, y, z coordinates
void Point3D::print () const
{
  double x = this -> getX(), y = this -> getY(), z = this -> getZ();
  std::cout << std::scientific << std::setprecision(15) << "x: " << x
	    << " y: " << y << " z: " << z << std::endl;
}


// Overrides Point::equalTo() by considering the z-axis coordinates
bool Point3D::equalTo (const Point* point) const
{
  double x1 = this -> getX(), x2 = point -> getX();
  double y1 = this -> getY(), y2 = point -> getY();
  double z1 = this -> getZ(), z2 = point -> getZ();

  if ( (x1 == x2) && (y1 == y2) )
  {
    return (z1 == z2);
  }
  else
  {
    return false;
  }
}


// Overrides Point::compareTo() by considering the z-axis coordinates
int Point3D::compareTo (const Point* point) const
{
  double x1 = this -> getX(), x2 = point -> getX();
  double y1 = this -> getY(), y2 = point -> getY();
  double z1 = this -> getZ(), z2 = point -> getZ();

  if (x1 != x2)
  {
    return ( Comparator(x1, x2) );
  }
  else if (y1 != y2)
  {
    return ( Comparator(y1, y2) );
  }
  else
  {
    return ( Comparator(z1, z2) );
  }
}


// Overrides Point::yPosCompareTo() by considering the z-axis coordinates
int Point3D::yPosCompareTo (const Point* p) const
{
  double x1 = this -> getX(), x2 = p -> getX();
  double y1 = this -> getY(), y2 = p -> getY();
  double z1 = this -> getZ(), z2 = p -> getZ();

  if (y1 != y2)
  {
    return (y1 < y2);
  }
  else if (x1 != x2)
  {
    return (x1 < x2);
  }
  else
  {
    return (z1 < z2);
  }
}


// Overrides Point::zPosCompareTo() by considering the z-axis coordinates
int Point3D::zPosCompareTo (const Point* point) const
{
  double x1 = this -> getX(), x2 = point -> getX();
  double y1 = this -> getY(), y2 = point -> getY();
  double z1 = this -> getZ(), z2 = point -> getZ();

  if (z1 != z2)
  {
    return (z1 < z2);
  }
  else if (x1 != x2)
  {
    return (x1 < x2);
  }
  else
  {
    return (y1 < y2);
  }
}


/* static methods defined in the point namespace */


// defines predicate method compare for x-y sorting of points
bool point::compare (const Point* p, const Point* q)
{
  // returns true if point P is less than point Q, returns false otherwise
  return ( ( p -> compareTo(q) ) < 0 );
}


// returns the distance between a pair of points
double point::distance (const Point* p, const Point* q)
{
  return ( p -> distance(q) );
}
