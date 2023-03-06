#ifndef GUARD__CLOSEST_PAIR_LAB__BENCHMARK_H
#define GUARD__CLOSEST_PAIR_LAB__BENCHMARK_H


#include <math.h>		// for INFINITY MACRO and floor() method
#include <time.h>		// for the POSIX clock_getres() and clock_gettime()
#include <stdio.h>		// for printf() and fprintf()
#include <stdlib.h>		// for malloc(), free(), and NULL


#include "util.h"		// for the closest pair lab utilities
#include "algorithm.h"		// for the sort() and contains() algorithms


typedef struct
{
    double first;		// index of first, closest-pair, comprising, point
    double second;		// index of second, closest-pair, comprising, point
    double distance;		// separating distance of the `first' and `second' points 
    double numOperations;	// number of operations spent to find the closest pair

} ClosestPairInfo;


#endif


/*
 * Closest Pair Lab						March 06, 2023
 *
 * source: benchmark.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Includes utility headers.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] Linux Programmer's Manual: `man clock_getres()'
 *
 */


