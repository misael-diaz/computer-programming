#ifndef GUARD_AC_PAIR_CLASS
#define GUARD_AC_PAIR_CLASS


/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Closest Pair Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include <iostream>		// provides std::cout and std::endl
#include <iomanip>		// provides std::scientific and std::setprecision()
#include <limits>		// provides std::numeric_limits<double>::infinity()

#include "PointClass.h"
#include "ComparatorTemplateFunction.h"
#include "HandlerTemplateClass.h"

class Pair	// closest pair class
{

	private:


	/* components */


	// it needs a handler to manage the memory of dynamically allocated points
	Handler<Point*> handler;

	Point* first;		// first point that comprises the pair
	Point* second;		// second point that comprises the pair
	double distance;	// separating distance of the points


	public:


	/* (de)constructors */


	// default constructor
	Pair ();


	// constructs from a pair of points and their distance
	Pair (Point* p, Point* q, double d);


	// destructor
	~Pair ();


	/* getters */


	// returns the separating distance of the points that comprise the pair
	double getDistance () const;


	/* methods */


	// copies components from another pair
	void copy (const Pair* pair);


	// prints info about the closest pair
	void print () const;


	// returns true if the pairs have equidistant points
	bool equidistant (const Pair* pair) const;


	// returns true if `this' pair is less than the other `pair'
	int compareTo (const Pair* pair) const;


	// returns true if `this' pair is equal to the other `pair'
	bool equalTo (const Pair* pair) const;
};


namespace pair
{
	// returns reference to the smallest of the two pairs
	Pair* min(Pair* first, Pair* second);
}

#endif
