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


static void cloner (void* vdst, const void* vsrc)
{
  point_t* dst = vdst;
  const point_t* src = vsrc;
  dst -> x = src -> x;
  dst -> y = src -> y;
  dst -> id = src -> id;
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


void copy (point_t* dst, const point_t* src, size_t const numel)
{
  point_t* idst = dst;
  const point_t* isrc = src;
  for (size_t i = 0; i != numel; ++i)
  {
    idst -> clone(idst, isrc);
    ++idst;
    ++isrc;
  }
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
    point -> clone = cloner;
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


bool sorted (const ensemble_t* ensemble, size_t const beg, size_t const end,
	    int (*comp) (const point_t* point1, const point_t* point2))
{
  bool sorted = true;
  const point_t* points = ensemble -> points;
  const point_t* point = points;
  for (size_t i = beg; i != (end - 1); ++i)
  {
    const point_t* pnt1 = point;
    const point_t* pnt2 = (point + 1);
    if (comp(pnt2, pnt1) == -1)
    {
      sorted = false;
      return sorted;
    }
    ++point;
  }
  return sorted;
}


void direct(ensemble_t* ensemble, size_t const beg, size_t const end,
	    int (*comp) (const point_t* point1, const point_t* point2))
{
  size_t const offset = beg;
  point_t* point1 = ensemble -> points + offset;
  point_t* point2 = ensemble -> points + (offset + 1);

  point_t* smallerPoint = ensemble -> temp;
  if (comp(point2, point1) == -1)
  {
    smallerPoint -> clone(smallerPoint, point2);
    point2 -> clone(point2, point1);
    point1 -> clone(point1, smallerPoint);
  }
}


void combine (ensemble_t* ensemble, size_t const beg, size_t const end,
	      int (*comp) (const point_t* point1, const point_t* point2))
{
  size_t const beginLeft = beg;
  size_t const endLeft = beg + ( (end - beg) / 2 );
  size_t const beginRight = beg + ( (end - beg) / 2 );
  size_t const endRight = end;

  size_t iterLeft = beginLeft;
  size_t iterRight = beginRight;
  point_t* points = ensemble -> points;
  point_t* dst = ensemble -> temp;
  while ( (iterLeft != endLeft) && (iterRight != endRight) )
  {
    point_t* pointLeft = points + iterLeft;
    point_t* pointRight = points + iterRight;
    if (comp(pointLeft, pointRight) == -1)
    {
      point_t* smallerPoint = pointLeft;
      dst -> clone(dst, smallerPoint);
      ++iterLeft;
    }
    else
    {
      point_t* smallerPoint = pointRight;
      dst -> clone(dst, smallerPoint);
      ++iterRight;
    }
    ++dst;
  }

  size_t b = iterLeft;
  size_t e = endLeft;
  const point_t* src = (points + b);
  copy(dst, src, e - b);

  dst += (e - b);

  b = iterRight;
  e = endRight;
  src = (points + b);
  copy(dst, src, e - b);

  dst = (points + beg);
  src = ensemble -> temp;
  copy(dst, src, end - beg);
}


void sort(ensemble_t* ensemble, size_t const beg, size_t const end,
	  int (*comp) (const point_t* point1, const point_t* point2))
{
  size_t const numel = (end - beg);
  for (size_t i = 0; i != numel; i += 2)
  {
    size_t const offset = beg;
    size_t const b = (i + offset);
    size_t const e = ( (i + 1) + offset);
    direct(ensemble, b, e, comp);
  }

  if (numel == 2)
  {
    return;
  }

  for (size_t stride = 4; stride != numel; stride *= 2)
  {
    for (size_t i = 0; i != numel; i += stride)
    {
      size_t const offset = beg;
      size_t const b = (i + offset);
      size_t const e = ( (i + stride) + offset );
      combine(ensemble, b, e, comp);
    }
  }

  combine(ensemble, beg, end, comp);
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
