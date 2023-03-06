#ifndef GUARD__CLOSEST_PAIR_LAB__UTIL_PRNG_NAMESPACE__H
#define GUARD__CLOSEST_PAIR_LAB__UTIL_PRNG_NAMESPACE__H

typedef struct
{
    Random* (*create) ();
    Random* (*destroy) (Random*);

} util_prng_namespace;

#endif

/*
 * Closest Pair Lab						March 06, 2023
 *
 * source: prng.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Defines the Namespace for (de)allocating the Pseudo Random Number Generator.
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
