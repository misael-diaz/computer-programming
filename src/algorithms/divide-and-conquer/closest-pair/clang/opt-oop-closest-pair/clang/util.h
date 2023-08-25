#ifndef GUARD_AC_CLOSEST_PAIR_OOP_UTIL_H
#define GUARD_AC_CLOSEST_PAIR_OOP_UTIL_H

#include <time.h>
#include "ensemble.h"
#include "pair.h"

double getElapsedTime (const struct timespec* b, const struct timespec* e);
int search(const point_t* points, size_t const b, size_t const e, const point_t* target);
double urand(double const size);
void copy (point_t* dst, const point_t* src, size_t const numel);
bool sorted(const ensemble_t* ensemble, size_t const b, size_t const e,
	    int (*comp) (const point_t* p, const point_t* q));
void sort(ensemble_t* ensemble, size_t const b, size_t const e,
	  int (*comp) (const point_t* p, const point_t* q));

#endif
/*

Algorithms and Complexity					August 24, 2023

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
