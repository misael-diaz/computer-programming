#ifndef GUARD_AC_CLOSEST_PAIR_2D_PARTICLE_TYPE_H
#define GUARD_AC_CLOSEST_PAIR_2D_PARTICLE_TYPE_H

typedef struct
{
  // pointers (x and y (temporary) coordinates and the number of elements (or particles)):
  double* id;
  double* x;
  double* y;
  double* xtmp;
  double* ytmp;
  double* itmp;
  double* numel;
  // actual placeholder (allocated on the heap) for storing the particle coordinates:
  double* data;
} particle_t;

#endif


/*

Algorithms and Complexity					August 11, 2023

source: particle.h
author: @misael-diaz

Synopsis:
Defines the particle type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
