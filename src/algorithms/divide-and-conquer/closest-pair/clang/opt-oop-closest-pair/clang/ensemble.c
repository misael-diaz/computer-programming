#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include "ensemble.h"

extern point_namespace_t const point;

static void cloner (void* vdestination, const void* vsource)
{
  const ensemble_t* source = vsource;
  ensemble_t* destination = vdestination;
  const point_t* src = source -> points;
  point_t* dst = destination -> points;
  size_t const numel = *(source -> numel);
  for (size_t i = 0; i != numel; ++i)
  {
    dst -> clone(dst, src);
    ++src;
    ++dst;
  }
}

static ensemble_t* construct (size_t const numel)
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

  point.initializer(points, numel);
  point.initializer(temp, numel);

  *(ensemble -> numel) = numel;

  ensemble -> clone = cloner;

  return ensemble;
}

static ensemble_t* deconstruct (ensemble_t* ensemble)
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

ensemble_namespace_t const ensemble = {
  .create = construct,
  .destroy = deconstruct
};

/*

Algorithms and Complexity					August 24, 2023

source: ensemble.c
author: @misael-diaz

Synopsis:
Defines the (de)constructor of the ensemble type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
