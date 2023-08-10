#ifndef GUARD__CLOSEST_PAIR_LAB__DS_WORKSPACE__H
#define GUARD__CLOSEST_PAIR_LAB__DS_WORKSPACE__H

#include <time.h>

typedef struct
{
    Dataset* dataset;		// placeholder for the Point coordinates
    struct timespec* startTime;	// placeholder for the start time (see `man clock_getres')
    struct timespec* endTime;	// placeholder for the end time
    Random* random;		// Pseudo Random Number Generator PRNG

} Workspace;

#endif

/*
 * Closest Pair Lab						March 04, 2023
 *
 * source: workspace.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Defines the Workspace Data Structure.
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
