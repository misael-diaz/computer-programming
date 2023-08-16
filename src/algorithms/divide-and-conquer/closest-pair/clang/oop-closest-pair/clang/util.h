#ifndef GUARD_AC_CLOSEST_PAIR_OOP_UTIL_H
#define GUARD_AC_CLOSEST_PAIR_OOP_UTIL_H

#include "point.h"
#include "pair.h"

int search(const point_t* points, size_t const b, size_t const e, const point_t* target);
double urand(double const size);
point_t* create(size_t const numel);
point_t* destroy(point_t* points);
pair_t* deconstruct(pair_t* closestPair);
pair_t* construct();

#endif
/*

Algorithms and Complexity					August 16, 2023

source: util.h
author: @misael-diaz

Synopsis:
Header for the closest pair utilities.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
