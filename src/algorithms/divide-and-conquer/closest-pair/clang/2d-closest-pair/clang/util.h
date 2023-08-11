#ifndef GUARD_AC_CLOSEST_PAIR_2D_H
#define GUARD_AC_CLOSEST_PAIR_2D_H

#include <stdint.h>
#include <stdbool.h>
#include <time.h>

double urand(double const size);
double getElapsedTime(const struct timespec* b, const struct timespec* e);
void copy(const double* restrict src, double* restrict dst, size_t const numel);
double* create(size_t const size);
double* destroy(double* x);

int xcompare (const double* positions,
              size_t const numel,
              size_t const first,
              size_t const second);

int ycompare (const double* positions,
              size_t const numel,
              size_t const first,
              size_t const second);

bool sorted(const double* positions,
            size_t const numel,
            size_t const b,
            size_t const e,
	    int (*comp) (const double*, size_t const, size_t const, size_t const));

bool contains(const double* x, int64_t const b, int64_t const e, double const tgt);

int64_t search (const double* positions,
		size_t const numel,
		int (*comp) (const double*, size_t const, size_t const, size_t const));

void sort(double* positions,
	  size_t const size,
	  size_t const numel,
	  int (*comp) (const double*, size_t const, size_t const, size_t const));
#endif


/*

Algorithms and Complexity					August 10, 2023

source: util.h
author: @misael-diaz

Synopsis:
Defines headers for the closest pair utility methods.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
