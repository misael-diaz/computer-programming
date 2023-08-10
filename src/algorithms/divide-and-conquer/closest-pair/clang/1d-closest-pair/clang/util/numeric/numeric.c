/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: numeric.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Provides utility methods for numeric types.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */

#include "../../include/util/numeric/numeric.h"

static int compare (const double x1, const double x2)
/*

Synopsis:
Implements a method for comparing floating-point numbers of double precision.
Returns 0 if x1 == x2, 1 if x1 > x2, and -1 if x1 < x2

*/
{
	if (x1 == x2)
	{
		return 0;
	}
	else if (x1 > x2)
	{
		return 1;
	}
	else
	{
		return -1;
	}
}

// instantiates numeric namespace
util_numeric_namespace const numeric = {
	compare
};
