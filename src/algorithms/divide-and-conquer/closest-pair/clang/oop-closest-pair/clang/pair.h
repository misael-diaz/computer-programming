#ifndef GUARD_AC_CLOSEST_PAIR_OOP_PAIR_TYPE_H
#define GUARD_AC_CLOSEST_PAIR_OOP_PAIR_TYPE_H

typedef struct
{
  size_t first;
  size_t second;
  double dist;
  void (*set)(void* closestPair, size_t const first, size_t const second, double const d);
  void (*log)(const void* closestPair);
} pair_t;

#endif


/*

Algorithms and Complexity					August 16, 2023

source: pair.h
author: @misael-diaz

Synopsis:
Defines the pair type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
