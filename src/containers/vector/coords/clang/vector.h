#ifndef GUARD_VECTOR_H
#define GUARD_VECTOR_H
/*
 * Algorithms and Complexity                             September 03, 2022
 * IST 4310
 * Prof. M Diaz-Maldonado
 *
 * source: vector.h
 *
 * Synopsis:
 * Header file for the vector class.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 *
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by
 *     Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */

#include "coord.h"

typedef struct {
coord_t* begin;
coord_t* avail;
coord_t* limit;
coord_t* array;
} vector_t;
#endif
