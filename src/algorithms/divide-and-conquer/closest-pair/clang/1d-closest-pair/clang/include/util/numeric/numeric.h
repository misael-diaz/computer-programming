#ifndef GUARD__CLOSEST_PAIR_LAB__UTIL_NUMERIC_NAMESPACE__H
#define GUARD__CLOSEST_PAIR_LAB__UTIL_NUMERIC_NAMESPACE__H

typedef struct
{
	int (*compare) (const double, const double);

} util_numeric_namespace;

#endif


/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: numeric.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Declares the namespace for utility methods that operate on numeric types.
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
