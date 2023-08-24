#include <stdio.h>
#include <math.h>
#include "point.h"


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


static int xcompare (const point_t* point1, const point_t* point2)
{
  double const x1 = point1 -> _x;
  double const y1 = point1 -> _y;
  double const x2 = point2 -> _x;
  double const y2 = point2 -> _y;
  return ( (x1 != x2)? compare(x1, x2) : compare(y1, y2) );
}


static int ycompare (const point_t* point1, const point_t* point2)
{
  double const x1 = point1 -> _x;
  double const y1 = point1 -> _y;
  double const x2 = point2 -> _x;
  double const y2 = point2 -> _y;
  return ( (y1 != y2)? compare(y1, y2) : compare(x1, x2) );
}


static void setter (void* vpoint, double const x, double const y, size_t const id)
{
  point_t* point = vpoint;
  point -> _x = x;
  point -> _y = y;
  point -> _id = id;
}


static size_t id_getter (const void* vpoint)
{
  const point_t* point = vpoint;
  return (point -> _id);
}


static void cloner (void* vdst, const void* vsrc)
{
  point_t* dst = vdst;
  const point_t* src = vsrc;
  dst -> _x = src -> _x;
  dst -> _y = src -> _y;
  dst -> _id = src -> _id;
}


static void logger (const void* vpoint)
{
  const point_t* point = vpoint;
  double const x = point -> _x;
  double const y = point -> _y;
  size_t const id = point -> _id;
  printf("x: %+e y: %+e id: %lu\n", x, y, id);
}


static double distance (const void* vpoint1, const void* vpoint2)
{
  const point_t* point1 = vpoint1;
  const point_t* point2 = vpoint2;
  double const x1 = point1 -> _x;
  double const y1 = point1 -> _y;
  double const x2 = point2 -> _x;
  double const y2 = point2 -> _y;
  return ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
}


static double distance_x (const void* vpoint1, const void* vpoint2)
{
  const point_t* point1 = vpoint1;
  const point_t* point2 = vpoint2;
  double const x1 = point1 -> _x;
  double const x2 = point2 -> _x;
  return ( (x1 - x2) * (x1 - x2) );
}


static double distance_y (const void* vpoint1, const void* vpoint2)
{
  const point_t* point1 = vpoint1;
  const point_t* point2 = vpoint2;
  double const y1 = point1 -> _y;
  double const y2 = point2 -> _y;
  return ( (y1 - y2) * (y1 - y2) );
}


static void init (point_t* points, size_t const numel)
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
    point -> dist_x = distance_x;
    point -> dist_y = distance_y;
    point -> getID = id_getter;
    point -> set(point, x, y, id);
    ++point;
  }
}


point_namespace_t const point = {
  .initializer = init,
  .xcomparator = xcompare,
  .ycomparator = ycompare
};


/*

Algorithms and Complexity					August 24, 2023

source: point.c
author: @misael-diaz

Synopsis:
Defines methods for points.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
