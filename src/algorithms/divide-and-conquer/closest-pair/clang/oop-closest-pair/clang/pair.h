#ifndef GUARD_AC_CLOSEST_PAIR_OOP_PAIR_TYPE_H
#define GUARD_AC_CLOSEST_PAIR_OOP_PAIR_TYPE_H

#define __PAIR_ID_TYPE size_t
#define __PAIR_DISTANCE_TYPE double

typedef struct
{
// private:
  __PAIR_ID_TYPE _first;
  __PAIR_ID_TYPE _second;
  __PAIR_DISTANCE_TYPE _dist;
// public:
  size_t (*getFirst)(const void* closestPair);
  size_t (*getSecond)(const void* closestPair);
  double (*getDistance)(const void* closestPair);
  void (*set)(void* closestPair, size_t const first, size_t const second, double const d);
  void (*min)(void* closestPair, const void* pair1, const void* pair2);
  bool (*cmp)(const void* pair1, const void* pair2);
  void (*log)(const void* closestPair);
} pair_t;

typedef struct
{
  pair_t* (*create) ();
  pair_t* (*destroy) (pair_t* closestPair);
} pair_namespace_t;

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
