#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include "pair.h"


static size_t get_first (const void* vclosestPair)
{
  const pair_t* closestPair = vclosestPair;
  return closestPair -> _first;
}


static size_t get_second (const void* vclosestPair)
{
  const pair_t* closestPair = vclosestPair;
  return closestPair -> _second;
}


static double get_distance (const void* vclosestPair)
{
  const pair_t* closestPair = vclosestPair;
  return closestPair -> _dist;
}


static void setter (void* vclosestPair,
		    size_t const first,
		    size_t const second,
		    double const dist)
{
  pair_t* closestPair = vclosestPair;
  if (first < second)
  {
    closestPair -> _first = first;
    closestPair -> _second = second;
    closestPair -> _dist = dist;
  }
  else
  {
    closestPair -> _first = second;
    closestPair -> _second = first;
    closestPair -> _dist = dist;
  }
}


static void minimum (void* vclosestPair,
		     const void* vclosestPairLeft,
		     const void* vclosestPairRight)
{
  pair_t* closestPair = vclosestPair;
  const pair_t* closestPairLeft = vclosestPairLeft;
  const pair_t* closestPairRight = vclosestPairRight;

  double dmin = INFINITY;
  size_t first = 0xffffffffffffffff;
  size_t second = 0xffffffffffffffff;
  double const minDistLeft = closestPairLeft -> _dist;
  double const minDistRight = closestPairRight -> _dist;
  if (minDistLeft < minDistRight)
  {
    dmin = minDistLeft;
    first = closestPairLeft -> _first;
    second = closestPairLeft -> _second;
  }
  else
  {
    dmin = minDistRight;
    first = closestPairRight -> _first;
    second = closestPairRight -> _second;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


static void logger (const void* vclosestPair)
{
  const pair_t* closestPair = vclosestPair;
  size_t const first = closestPair -> _first;
  size_t const second = closestPair -> _second;
  double const distance = closestPair -> _dist;
  printf("first: %lu second: %lu distance: %e\n", first, second, distance);
}


static bool comparator (const void* vclosestPair1, const void* vclosestPair2)
{
  const pair_t* closestPair1 = vclosestPair1;
  const pair_t* closestPair2 = vclosestPair2;

  size_t const i = closestPair1 -> _first;
  size_t const j = closestPair1 -> _second;
  double const d1 = closestPair1 -> _dist;

  size_t const n = closestPair2 -> _first;
  size_t const m = closestPair2 -> _second;
  double const d2 = closestPair2 -> _dist;

  return ( ( (i == n) && (j == m) && (d1 == d2) )? true : false );
}


static pair_t* construct ()
{
  pair_t* closestPair = malloc( sizeof(pair_t) );
  if (closestPair == NULL)
  {
    printf("construct(): insufficient memory to allocate the closest pair\n");
    return NULL;
  }

  closestPair -> _first = 0xffffffffffffffff;
  closestPair -> _second = 0xffffffffffffffff;
  closestPair -> _dist = INFINITY;
  closestPair -> set = setter;
  closestPair -> min = minimum;
  closestPair -> cmp = comparator;
  closestPair -> log = logger;
  closestPair -> getFirst = get_first;
  closestPair -> getSecond = get_second;
  closestPair -> getDistance = get_distance;
  return closestPair;
}


static pair_t* deconstruct (pair_t* closestPair)
{
  if (closestPair == NULL)
  {
    return closestPair;
  }

  free(closestPair);
  closestPair = NULL;
  return closestPair;
}


pair_namespace_t const pair = {
  .create = construct,
  .destroy = deconstruct
};


/*

Algorithms and Complexity					August 24, 2023

source: pair.c
author: @misael-diaz

Synopsis:
Defines methods for the pair type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
