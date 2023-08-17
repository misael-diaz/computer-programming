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


void generate (ensemble_t* ensemble)
{
  size_t const numel = *(ensemble -> numel);
  point_t* points = ensemble -> points;

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


bool hasDuplicateClosestPairs (const ensemble_t* ensemble)
{
  const point_t* points = ensemble -> points;
  const point_t* point1 = points;

  double dmin1 = INFINITY;
  double dmin2 = INFINITY;
  size_t const numel = *(ensemble -> numel);
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


void initialize (ensemble_t* ensemble)
{
  do
  {
    generate(ensemble);
  } while ( hasDuplicateClosestPairs(ensemble) );
}


void bruteForce (const ensemble_t* ensemble, pair_t* closestPair)
{
  size_t const numel = *(ensemble -> numel);
  const point_t* points = ensemble -> points;

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
  ensemble_t* ensemble = create(numel);
  initialize(ensemble);

  const point_t* points = ensemble -> points;
  const point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> log(point);
    ++point;
  }

  bruteForce(ensemble, closestPair);
  printf("bruteForce(): ");
  closestPair -> log(closestPair);

  ensemble = destroy(ensemble);
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
