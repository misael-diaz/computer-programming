#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include "util.h"

#define iNUMEL 8

void test_bruteForce();

int main ()
{
  test_bruteForce();
  return 0;
}


void generate (point_t* points, size_t const numel)
{
  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    do
    {
      size_t const id = i;
      double x = urand(numel);
      double y = urand(numel);
      point -> set(point, x, y, id);
    }
    while (search(points, 0, i, point) != -1);
    ++point;
  }
}


bool hasDuplicateClosestPairs (const point_t* points, size_t const numel)
{
  double dmin1 = INFINITY;
  double dmin2 = INFINITY;
  const point_t* point1 = points;
  for (size_t i = 0; i != (numel - 1); ++i)
  {
    const point_t* point2 = (point1 + 1);
    for (size_t j = (i + 1); j != numel; ++j)
    {
      double const d = point1 -> dist(point1, point2);
      if (d <= dmin1)
      {
	dmin2 = dmin1;
	dmin1 = d;
      }
      ++point2;
    }
    ++point1;
  }
  bool const hasDuplicateClosestPairs = ( (dmin1 == dmin2)? true : false );
  return hasDuplicateClosestPairs;
}


void initialize (point_t* points, size_t const numel)
{
  do
  {
    generate(points, numel);
  } while ( hasDuplicateClosestPairs(points, numel) );
}


void bruteForce (const point_t* points, pair_t* closestPair, size_t const numel)
{
  size_t first = numel;
  size_t second = numel;
  double dmin = INFINITY;
  const point_t* point1 = points;
  for (size_t i = 0; i != (numel - 1); ++i)
  {
    const point_t* point2 = (point1 + 1);
    for (size_t j = (i + 1); j != numel; ++j)
    {
      double const d = point1 -> dist(point1, point2);
      if (d < dmin)
      {
	first = i;
	second = j;
	dmin = d;
      }
      ++point2;
    }
    ++point1;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


void test_bruteForce ()
{
  pair_t* closestPair = construct();
  if (closestPair == NULL)
  {
    return;
  }

  size_t const numel = iNUMEL;
  point_t* points = create(numel);
  initialize(points, numel);

  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> log(point);
    ++point;
  }

  bruteForce(points, closestPair, numel);
  printf("bruteForce(): ");
  closestPair -> log(closestPair);

  points = destroy(points);
  closestPair = deconstruct(closestPair);
}


/*

Algorithms and Complexity					August 16, 2023

source: test.c
author: @misael-diaz

Synopsis:
Solves the 2D closest pair problem.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
