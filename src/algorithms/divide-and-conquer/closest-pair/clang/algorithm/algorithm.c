/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: algorithm.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Implements required algorithms.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] contains(): https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/util/ArrayList.html
 *
 */


#include "../include/util/numeric/numeric.h"
#include "../include/algorithm.h"


extern util_numeric_namespace const numeric;	// imports numeric namespace


// implementations:


static int compare (const double* p, const double* q)
/*

Synopsis:
Returns 0 if the `p' and `q' Points are equal, returns 1 if the `p' is greater than
the `q' Point, and returns -1 if the `p' is less than the `q' Point.

*/
{
	// unpacks the `x' and `y' coordinates of the `p' and `q' Points
	double x1 = p[0], y1 = p[1];
	double x2 = q[0], y2 = q[1];

	if (x1 != x2)
	{
		return numeric.compare(x1, x2);
	}
	else
	{
		return numeric.compare(y1, y2);
	}
}


// methods:


/*

Synopsis:
Implements the linear search algorithm.

Searches for the target Point `t' in the placeholder `dataset' within the limits defined
by the asymmetric range [`b', `e'), where `b' and `e' are the begin and end indexes,
respectively.

Returns the positional index of the target Point `t' if present, -1 otherwise.

Inputs:
size		the number of Points effectively stored in the placeholder `dataset'

dataset		first-rank array of length (2 * `size') storing the `x' and `y'
		coordinates of the Points; the first `size' elements correspond to the
		`x' coordinates and the second `size' elements correspond to the `y'
		coordinates of the Points

b		beginning index that delimits the lower bounds of the `x' and `y' arrays

e		ending index that delimits the upper bounds of the `x' and `y' arrays

t		first-rank array of length 2 storing the `x' and `y' coordinates of the
		target Point `t'

Output:
pos		positional index of the target Point `t' if present, -1 otherwise

*/
static int linearSearch (
	const int size, const double* dataset, const int b, const int e, const double* t
)
{

	const double* x = dataset;			// binds to the `x' coordinates
	const double* y = (dataset + size);		// binds to the `y' coordinates

	int pos = -1;					// initializes with invalid index
	for (int i = b; i != e; ++i)			// looks for the target in order
	{
		const double  p [] = {x[i], y[i]};	// constructs Point P
		const double* q = t;			// gets reference to Point Q

		if (compare(p, q) == 0)			// checks for equality (P == Q)
		{
			pos = i;			// gets the positional index
			break;				// stops (target has been found)
		}
	}

	return pos;					// returns the positional index
}


/*

Synopsis:
Implements the Java ArrayList-like contains() method.

Returns `true' if the target Point `t' is present, `false' otherwise.

Inputs:
size		the number of Points effectively stored in the placeholder `dataset'

dataset		first-rank array of length (2 * `size') storing the `x' and `y'
		coordinates of the Points; the first `size' elements correspond to the
		`x' coordinates and the second `size' elements correspond to the `y'
		coordinates of the Points

b		beginning index that delimits the lower bounds of the `x' and `y' arrays

e		ending index that delimits the upper bounds of the `x' and `y' arrays

t		first-rank array of length 2 storing the `x' and `y' coordinates of the
		target Point `t'

Output:
ret 		`true' if the target Point `t' is present, `false' otherwise.

*/
static bool contains (
	const int size, const double* dataset, const int b, const int e, const double* t
)
{
	return ( (linearSearch(size, dataset, b, e, t) < 0)? false : true );
}


static void insertionSort (const int size, double* dataset)
// implements the Insertion Sort algorithm
{
	double* x = dataset;
	double* y = (dataset + size);

	// for-loop invariant: the elements in the asymmetric range [0, `i') are ordered
	for (int i = 1; i != size; ++i)
	{
		int pos = (i - 1);		// gets the preceeding element position
		double x_i = x[i], y_i = y[i];	// saves the `i'-th element coordinates
		double q [] = {x[i], y[i]};	// constructs the `i'-th element `q'
		double p [] = {x[pos], y[pos]};	// constructs the preceeding element `p'

		// while-loop invariant:
		// So far we have shifted ( `i' - (`pos' + 1) ) elements greater than `q'
		while ( pos > 0 && compare(p, q) > 0 )
		{
			// shifts the greater element `p' to the rigth
			x[pos + 1] = x[pos];
			y[pos + 1] = y[pos];
			--pos;

			// updates the element `p' for the next pass
			p[0] = x[pos];
			p[1] = y[pos];
		}

		if ( pos == 0 && compare(p, q) > 0 )
		// shifts the greater first element
		{
			x[pos + 1] = x[pos];
			y[pos + 1] = y[pos];
			--pos;
		}

		// Note: after the while-loop executes the preceding element at `pos'
		// (if any) is less than or equal to the original `i'-th element `q'.

		// writes `q' at the position that makes the for-loop invariant true
		x[pos + 1] = x_i;
		y[pos + 1] = y_i;
	}
}


// instantiates algorithm namespace
algorithm_namespace const algorithm = {
	.contains = contains,
	.sort = insertionSort
};
