/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: prng.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Implements Marsaglia's 32-bit xorshift Pseudo Random Number Generator PRNG.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] xorshift: http://simul.iro.umontreal.ca/testu01/tu01.html
 *
 */


#include <stdio.h>
#include <stdlib.h>
#include <limits.h>


#include "../../include/ds/prng.h"
#include "../../include/util/prng/prng.h"


static uint32_t xorshift (uint32_t x)
// implements Marsaglia's xorshift 32-bit PRNG
{
    x ^= (x << 13);
    x ^= (x >> 17);
    x ^= (x <<  5);
    return x;
}


static double next (void* prng)
// returns Pseudo Random Floating-Point Numbers in the asymmetric range [0, 1)
{
    Random* rand = prng;
    State* state = rand -> state;
    size_t* count = state -> count;
    uint32_t* x = state -> seed;
    // defines the theoretical period of the 32-bit PRNG
    const double period = ( ( (double) UINT_MAX ) + 1.0);

    if ( (*count) > period )
    {
	printf("Random.next(): WARNING.Depleted.PRNG\n");
    }

    *x = xorshift(*x);
    ++(*count);

    const double eps = (1.0 / period);
    double num = ( (*x) * eps );
    return num;
}


static State* seed ()
// seeds the PRNG
{
    State* state = NULL;

    size_t* c = (size_t*) malloc( sizeof(size_t) );

    if (c == NULL)
    // returns NULL if the allocation of the count `c' failed
    {
	return state;
    }

    uint32_t* s = (uint32_t*) malloc( sizeof(uint32_t) );

    if (s == NULL)
    // returns NULL if the allocation of the seed `s' failed
    {
	free(c);
	c = NULL;
	return state;
    }

    state = (State*) malloc( sizeof(State) );

    if (state == NULL)
    // returns NULL if the allocation of the state fails
    {
	free(c);
	free(s);
	c = NULL;
	s = NULL;
	return state;
    }

    *c = 0;			// zeros count
    *s = UINT_MAX;		// inits seed with max unsigned 32-bit integer
    state -> count = c;		// binds `state' to `count'
    state -> seed = s;		// binds `state' to `seed'

    return state;
}


static Random* constructor ()
// constructs PRNG
{
    Random* rand = NULL;


    State* state = seed();

    if (state == NULL)
    {
	return rand;
    }


    rand = (Random*) malloc( sizeof(Random) );

    if (rand == NULL)
    {
	size_t* c = state -> count;
	uint32_t* s = state -> seed;
	free(c);
	free(s);
	c = NULL;
	s = NULL;
	state -> count = NULL;
	state -> seed = NULL;
	free(state);
	state = NULL;
	return rand;
    }

    rand -> state = state;
    rand -> next = next;

    return rand;
}


static Random* destructor (Random* rand)
// frees the memory allocated for the PRNG
{
    if (rand != NULL)
    {
	State* state = rand -> state;
	size_t* c = state -> count;
	uint32_t* s = state -> seed;

	free(c);
	free(s);
	c = NULL;
	s = NULL;
	state -> count = NULL;
	state -> seed = NULL;
	free(state);
	state = NULL;

	rand -> state = NULL;
	rand -> next = NULL;
	free(rand);
	rand = NULL;
    }

    return rand;
}


// defines the PRNG namespace
util_prng_namespace const prng = {
    .create = constructor,
    .destroy = destructor
};
