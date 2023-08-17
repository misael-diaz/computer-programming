#ifndef GUARD_AC_CLOSEST_PAIR_OOP_POINT_TYPE_H
#define GUARD_AC_CLOSEST_PAIR_OOP_POINT_TYPE_H

typedef struct
{
  double x;
  double y;
  size_t id;
  void (*set) (void* point, double const x, double const y, size_t const id);
  void (*log) (const void* point);
  void (*clone) (void* dst, const void* src);
  double (*dist) (const void* point1, const void* point2);
} point_t;

#endif
/*

Algorithms and Complexity					August 16, 2023

source: point.h
author: @misael-diaz

Synopsis:
Defines the 2D Point type.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
