#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>
#include <math.h>
#include "util.h"


double urand (double const size)
{
  double const lim = (size * size);
  double const min = -lim;
  double const max = +lim;
  double const urand = ( (double) rand() ) / ( (double) RAND_MAX );
  double const r = min + urand * (max - min);
  return round(r);
}


static int compare (double const x1, double const x2)
{
  if (x1 == x2)
  {
    return 0;
  }
  else if (x1 > x2)
  {
    return 1;
  }
  else
  {
    return -1;
  }
}


int xcompare (const point_t* point1, const point_t* point2)
{
  double const x1 = point1 -> x;
  double const y1 = point1 -> y;
  double const x2 = point2 -> x;
  double const y2 = point2 -> y;
  return ( (x1 != x2)? compare(x1, x2) : compare(y1, y2) );
}


int ycompare (const point_t* point1, const point_t* point2)
{
  double const x1 = point1 -> x;
  double const y1 = point1 -> y;
  double const x2 = point2 -> x;
  double const y2 = point2 -> y;
  return ( (y1 != y2)? compare(y1, y2) : compare(x1, x2) );
}


static void setter (void* vpoint, double const x, double const y, size_t const id)
{
  point_t* point = vpoint;
  point -> x = x;
  point -> y = y;
  point -> id = id;
}


static void logger (const void* vpoint)
{
  const point_t* point = vpoint;
  double const x = point -> x;
  double const y = point -> y;
  size_t const id = point -> id;
  printf("x: %+e y: %+e id: %lu\n", x, y, id);
}


bool isEqual (const point_t* point1, const point_t* point2)
{
  return ( (xcompare(point1, point2) == 0) && (point1 -> id == point2 -> id) );
}


int search (const point_t* points, size_t const b, size_t const e, const point_t* target)
{
  const point_t* point = points;
  for (size_t i = b; i != e; ++i)
  {
    if (xcompare(point, target) == 0)
    {
      return i;
    }
    ++point;
  }
  return -1;
}


static double distance (const void* vpoint1, const void* vpoint2)
{
  const point_t* point1 = vpoint1;
  const point_t* point2 = vpoint2;
  double const x1 = point1 -> x;
  double const y1 = point1 -> y;
  double const x2 = point2 -> x;
  double const y2 = point2 -> y;
  return ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
}


void init (point_t* points, size_t const numel)
{
  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    size_t const id = i;
    double const x = INFINITY;
    double const y = INFINITY;
    point -> set = setter;
    point -> log = logger;
    point -> dist = distance;
    point -> set(point, x, y, id);
    ++point;
  }
}


static void setClosestPair (void* vclosestPair,
			    size_t const first,
			    size_t const second,
			    double const dist)
{
  pair_t* closestPair = vclosestPair;
  if (first < second)
  {
    closestPair -> first = first;
    closestPair -> second = second;
    closestPair -> dist = dist;
  }
  else
  {
    closestPair -> first = second;
    closestPair -> second = first;
    closestPair -> dist = dist;
  }
}


static void logClosestPair (const void* vclosestPair)
{
  const pair_t* closestPair = vclosestPair;
  double const first = closestPair -> first;
  double const second = closestPair -> second;
  double const distance = closestPair -> dist;
  printf("first: %.0f second: %.0f distance: %e\n", first, second, distance);
}


pair_t* construct ()
{
  pair_t* closestPair = malloc( sizeof(pair_t) );
  if (closestPair == NULL)
  {
    printf("construct(): insufficient memory to allocate the closest pair\n");
    return NULL;
  }

  closestPair -> first = 0xffffffffffffffff;
  closestPair -> second = 0xffffffffffffffff;
  closestPair -> dist = INFINITY;
  closestPair -> set = setClosestPair;
  closestPair -> log = logClosestPair;
  return closestPair;
}


pair_t* deconstruct (pair_t* closestPair)
{
  if (closestPair == NULL)
  {
    return closestPair;
  }

  free(closestPair);
  closestPair = NULL;
  return closestPair;
}


ensemble_t* create (size_t const numel)
{
  if (numel == 0)
  {
    printf("create(): expects the number of particles to be finite\n");
    return NULL;
  }

  if (numel % 2)
  {
    printf("create(): expects the number of particles to be even\n");
    return NULL;
  }

  if (numel >= 0x7fffffff)
  {
    printf("create(): reserved value\n");
    return NULL;
  }

  union { double data; uint64_t bin; } mantissa = { .data = numel };
  if ( (mantissa.bin & 0x000fffffffffffff) != 0 )
  {
    printf("create(): expects the number of particles to be a power of two\n");
    return NULL;
  }

  size_t const size_points = numel * sizeof(point_t);
  size_t const size_temp = numel * sizeof(point_t);
  size_t const size_numel = 1 * sizeof(size_t);
  size_t const size = size_points +
		      size_temp +
		      size_numel;

  void* data = malloc(size);
  if (data == NULL)
  {
    printf("create(): failed to allocate data placeholder\n");
    return NULL;
  }

  ensemble_t* ensemble = malloc( sizeof(ensemble_t) );
  if (ensemble == NULL)
  {
    free(data);
    data = NULL;
    return NULL;
  }

  ensemble -> points = ( (point_t*) data );
  ensemble -> temp = ensemble -> points + numel;
  ensemble -> numel = ( (size_t*) (ensemble -> temp + numel) );
  ensemble -> data = data;
  data = NULL;

  point_t* points = ensemble -> points;
  point_t* temp = ensemble -> temp;

  init(points, numel);
  init(temp, numel);

  *(ensemble -> numel) = numel;

  return ensemble;
}


ensemble_t* destroy (ensemble_t* ensemble)
{
  if (ensemble == NULL)
  {
    return ensemble;
  }

  free(ensemble -> data);
  ensemble -> data = NULL;
  ensemble -> points = NULL;
  ensemble -> temp = NULL;
  ensemble -> numel = NULL;

  free(ensemble);
  ensemble = NULL;
  return ensemble;
}


/*

Algorithms and Complexity					August 16, 2023

source: util.c
author: @misael-diaz

Synopsis:
Defines utilities to solve the closest pair problem.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
