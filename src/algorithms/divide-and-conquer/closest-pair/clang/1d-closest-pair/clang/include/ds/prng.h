#ifndef GUARD__CLOSEST_PAIR_LAB__DS_RANDOM__H
#define GUARD__CLOSEST_PAIR_LAB__DS_RANDOM__H


#include <stdlib.h>
#include <stdint.h>


typedef struct
{
	size_t* count;
	uint32_t* seed;
} State;


typedef struct
{
	State* state;
	double (*next) (void*);
} Random;


#endif


/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: prng.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Header for the Pseudo Random Number Generator PRNG Data Structure.
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
