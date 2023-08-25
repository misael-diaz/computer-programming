#ifndef GUARD_AC_CLOSEST_PAIR_OOP_ENSEMBLE_TYPE_H
#define GUARD_AC_CLOSEST_PAIR_OOP_ENSEMBLE_TYPE_H

#include "point.h"

typedef struct
{
  point_t* points;
  point_t* temp;
  size_t* numel;
  void* data;
} ensemble_t;

typedef struct
{
  ensemble_t* (*create) (size_t const numel);
  ensemble_t* (*destroy) (ensemble_t* ensemble);
} ensemble_namespace_t;

#endif
/*

Algorithms and Complexity					August 16, 2023

source: ensemble.h
author: @misael-diaz

Synopsis:
Defines the Ensemble (of points) type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
