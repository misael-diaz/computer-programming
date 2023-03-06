#ifndef GUARD__CLOSEST_PAIR_LAB__ALGORITHM_H
#define GUARD__CLOSEST_PAIR_LAB__ALGORITHM_H

#include <stdbool.h>

typedef struct
{
    bool (*contains) (const int, const double*, const int, const int, const double*);
    void (*sort) (const int, double*);

} algorithm_namespace;

#endif


/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: algorithm.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Declares the namespace for the required algorithms.
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
