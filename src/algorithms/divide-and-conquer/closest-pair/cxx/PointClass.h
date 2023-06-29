#ifndef GUARD_AC_POINT_CLASS
#define GUARD_AC_POINT_CLASS

#include <iostream>	// provides std::cout and std::endl
#include <iomanip>	// provides std::scientific and std::setprecision()

#include "ComparatorTemplateFunction.h"

class Point		// 2D Cartesian Point Class
{

  protected:


    // components:


    double x;	// x-axis coordinate
    double y;	// y-axis coordinate


  public:


    // (de)constructors:


    // default constructor
    Point ();


    // constructs a point object from x, y coordinates
    Point (double x, double y);


    // we need a virtual destructor to destroy derived instances
    virtual ~Point ();


    // getters:


    double getX () const;		// returns the x-axis position
    double getY () const;		// returns the y-axis position
    virtual double getZ () const;	// returns a zero z-axis position


    // methods:


    // copy method
    virtual void copy (const Point* point);


    // returns the squared distance between a pair of points
    virtual double distance (const Point* point) const;


    // prints the x, y coordinates
    virtual void print () const;


    // returns true if the points are equal, returns false otherwise
    virtual bool equalTo (const Point* point) const;


    // returns true if this point is less than the other point
    virtual int compareTo (const Point* point) const;


    // compares points primarily by their y-axis values
    virtual int yPosCompareTo (const Point* p) const;


    // compares points primarily by their z-axis values
    virtual int zPosCompareTo (const Point* p) const;
};


class Point3D : public Point	// 3D Cartesian Point Class
{

	private:


	// component(s):


	double z;	// z-axis coordinate


	public:


	// (de)constructors:


	// default constructor
	Point3D ();


	// constructs 3d point from x, y, z coordinates
	Point3D (double x, double y, double z);


	// destructor
	virtual ~Point3D ();


	// getters:


	virtual double getZ () const;	// returns the z-axis position


	// methods:


	// copy method
	virtual void copy (const Point* point);


	// returns the squared distance between a pair of 3d points
	virtual double distance (const Point* point) const;


	// prints the x, y, z coordinates
	virtual void print () const;


	// returns true if the points are equal, returns false otherwise
	virtual bool equalTo (const Point* point) const;


	// compares points primarily by the x-axis values (default)
	virtual int compareTo (const Point* point) const;


	// compares points primarily by the y-axis values
	virtual int yPosCompareTo (const Point* p) const;


	// compares points primarily by the z-axis values
	virtual int zPosCompareTo (const Point* point) const;
};


namespace point
{
	// defines predicate method compare() for x-y sorting of points
	bool compare (const Point* p, const Point* q);

	// returns the distance between a pair of points
	double distance (const Point* p, const Point* q);
}

#endif

/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines Cartesian 2D and 3D Point classes.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/
