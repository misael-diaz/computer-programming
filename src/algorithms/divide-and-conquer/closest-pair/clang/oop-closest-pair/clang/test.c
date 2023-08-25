#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include "util.h"

#define RUNS 16
#define REPS 1024
#define iNUMEL 2

extern point_namespace_t const point;
extern ensemble_namespace_t const ensemble;
extern pair_namespace_t const pair;

void test_sort();
void test_bruteForce();
void test_recurse();
void test_recurse1();
void test_recurse2();
void test_recurse3();
void complexity_sort();
void complexity_recurse();

void divide(ensemble_t* ens, size_t const beg, size_t const end, pair_t* closestPair);
void recurse(ensemble_t* ens, size_t const beg, size_t const end, pair_t* closestPair);

int main ()
{
  test_sort();
  test_recurse();
  test_recurse1();
  test_recurse2();
  test_recurse3();
  test_bruteForce();
  complexity_sort();
  complexity_recurse();
  return 0;
}


void generate (ensemble_t* ens)
{
  size_t const numel = *(ens -> numel);
  point_t* points = ens -> points;

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


bool hasDuplicateClosestPairs (const ensemble_t* ens)
{
  const point_t* points = ens -> points;
  const point_t* point1 = points;

  double dmin1 = INFINITY;
  double dmin2 = INFINITY;
  size_t const numel = *(ens -> numel);
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


void initialize (ensemble_t* ens)
{
  do
  {
    generate(ens);
  } while ( hasDuplicateClosestPairs(ens) );
}


void bruteForce (const ensemble_t* ens, pair_t* closestPair)
{
  size_t const numel = *(ens -> numel);
  const point_t* points = ens -> points;

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


void direct (const ensemble_t* ens,
             size_t const beg,
             size_t const end,
             pair_t* closestPair)
{
  size_t const offset = beg;
  const point_t* points = ens -> points + offset;
  const point_t* point1 = points;
  const point_t* point2 = points + 1;
  double const dist = point1 -> dist(point1, point2);
  double const dmin = closestPair -> getDistance(closestPair);
  if (dist < dmin)
  {
    size_t const first = point1 -> getID(point1);
    size_t const second = point2 -> getID(point2);
    closestPair -> set(closestPair, first, second, dist);
  }
}


void xcombine (ensemble_t* ens,
	       size_t const beg,
	       size_t const end,
	       pair_t* closestPair)
{
  size_t const numel = (end - beg);
  size_t const beginLeft = beg;
  size_t const endLeft = beg + (numel / 2);
  size_t const beginRight = beg + (numel / 2);
  size_t const endRight = beg + numel;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  const point_t* points = ens -> points;
  double const dist = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const offset = ( endLeft - (i + 1) );
    const point_t* pointLeft = points + offset;
    const point_t* pointRight = points + beginRight;
    double const d = pointLeft -> dist_x(pointLeft, pointRight);
    if (d < dist)
    {
      --bLeft;
    }
    else
    {
      break;
    }
  }

  // prunes elements (too far to comprise the closest pair) from the right partition:

  size_t const bRight = beginRight;
  size_t eRight = beginRight;
  for (size_t i = beginRight; i != endRight; ++i)
  {
    size_t const offset = i;
    size_t const lastLeft = (endLeft - 1);
    const point_t* pointLeft = points + lastLeft;
    const point_t* pointRight = points + offset;
    double const d = pointRight -> dist_x(pointRight, pointLeft);
    if (d < dist)
    {
      ++eRight;
    }
    else
    {
      break;
    }
  }

  // uses brute force on the (pruned) combined partition:

  const point_t* pointLeft = points + bLeft;
  size_t first = closestPair -> getFirst(closestPair);
  size_t second = closestPair -> getSecond(closestPair);
  double dmin = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (eLeft - bLeft); ++i)
  {
    const point_t* pointRight = points + bRight;
    for (size_t j = 0; j != (eRight - bRight); ++j)
    {
      double const d = pointLeft -> dist(pointLeft, pointRight);
      if (d < dmin)
      {
	first = pointLeft -> getID(pointLeft);
	second = pointRight -> getID(pointRight);
	dmin = d;
      }
      ++pointRight;
    }
    ++pointLeft;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


// as xcombine() but uses the y-axis distances to determine too far elements
void ycombine (ensemble_t* ens,
	       size_t const beg,
	       size_t const end,
	       pair_t* closestPair)
{
  size_t const numel = (end - beg);
  size_t const beginLeft = beg;
  size_t const endLeft = beg + (numel / 2);
  size_t const beginRight = beg + (numel / 2);
  size_t const endRight = beg + numel;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  const point_t* points = ens -> points;
  double const dist = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const offset = ( endLeft - (i + 1) );
    const point_t* pointLeft = points + offset;
    const point_t* pointRight = points + beginRight;
    double const d = pointLeft -> dist_y(pointLeft, pointRight);
    if (d < dist)
    {
      --bLeft;
    }
    else
    {
      break;
    }
  }

  // prunes elements (too far to comprise the closest pair) from the right partition:

  size_t const bRight = beginRight;
  size_t eRight = beginRight;
  for (size_t i = beginRight; i != endRight; ++i)
  {
    size_t const offset = i;
    size_t const lastLeft = (endLeft - 1);
    const point_t* pointLeft = points + lastLeft;
    const point_t* pointRight = points + offset;
    double const d = pointRight -> dist_y(pointRight, pointLeft);
    if (d < dist)
    {
      ++eRight;
    }
    else
    {
      break;
    }
  }

  // uses brute force on the (pruned) combined partition:

  const point_t* pointLeft = points + bLeft;
  size_t first = closestPair -> getFirst(closestPair);
  size_t second = closestPair -> getSecond(closestPair);
  double dmin = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (eLeft - bLeft); ++i)
  {
    const point_t* pointRight = points + bRight;
    for (size_t j = 0; j != (eRight - bRight); ++j)
    {
      double const d = pointLeft -> dist(pointLeft, pointRight);
      if (d < dmin)
      {
	first = pointLeft -> getID(pointLeft);
	second = pointRight -> getID(pointRight);
	dmin = d;
      }
      ++pointRight;
    }
    ++pointLeft;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


// as recurse() but partitions the system in the y dimension
void divide (ensemble_t* ens,
	     size_t const beg,
	     size_t const end,
	     pair_t* closestPair)
{
  size_t const numel = (end - beg);
  if (numel == 2)
  {
    direct(ens, beg, end, closestPair);
  }
  else
  {
    sort(ens, beg, end, point.ycomparator);

    size_t const beginLeft = beg;
    size_t const endLeft = beg + (numel / 2);
    pair_t* closestPairLeft = pair.create();
    recurse(ens, beginLeft, endLeft, closestPairLeft);

    size_t const beginRight = beg + (numel / 2);
    size_t const endRight = beg + numel;
    pair_t* closestPairRight = pair.create();
    recurse(ens, beginRight, endRight, closestPairRight);

    closestPair -> min(closestPair, closestPairLeft, closestPairRight);

    ycombine(ens, beg, end, closestPair);

    // NOTE: we need to restore the x - y sorting because the xcombine() method at the
    // level of the caller method recurse() expects it (as if we didn't call sort() here)
    sort(ens, beg, end, point.xcomparator);

    closestPairLeft = pair.destroy(closestPairLeft);
    closestPairRight = pair.destroy(closestPairRight);
  }
}


void recurse (ensemble_t* ens,
	      size_t const beg,
	      size_t const end,
	      pair_t* closestPair)
{
  size_t const numel = (end - beg);
  if (numel == 2)
  {
    direct(ens, beg, end, closestPair);
  }
  else
  {
    sort(ens, beg, end, point.xcomparator);

    size_t const beginLeft = beg;
    size_t const endLeft = beg + (numel / 2);
    pair_t* closestPairLeft = pair.create();
    divide(ens, beginLeft, endLeft, closestPairLeft);

    size_t const beginRight = beg + (numel / 2);
    size_t const endRight = beg + numel;
    pair_t* closestPairRight = pair.create();
    divide(ens, beginRight, endRight, closestPairRight);

    closestPair -> min(closestPair, closestPairLeft, closestPairRight);

    xcombine(ens, beg, end, closestPair);

    // NOTE: we need to restore the y - x sorting because the ycombine() method at the
    // level of the caller method recurse() expects it (as if we didn't call sort() here)
    sort(ens, beg, end, point.ycomparator);

    closestPairLeft = pair.destroy(closestPairLeft);
    closestPairRight = pair.destroy(closestPairRight);
  }
}


void test_bruteForce ()
{
  pair_t* closestPair = pair.create();
  if (closestPair == NULL)
  {
    return;
  }

  size_t const numel = iNUMEL;
  ensemble_t* ens = ensemble.create(numel);
  initialize(ens);

  const point_t* points = ens -> points;
  const point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> log(point);
    ++point;
  }

  bruteForce(ens, closestPair);
  printf("bruteForce(): ");
  closestPair -> log(closestPair);

  ens = ensemble.destroy(ens);
  closestPair = pair.destroy(closestPair);
}


// tests the implementation with problem 2.3.1-1 of reference [1]
void test_recurse ()
{
  // note that we have added four more elements to meet create()'s requirements
  double x[] = {2,  4, 5, 10, 13, 15, 17, 19, 22, 25, 29, 30, 64, -64, -64,  64};
  double y[] = {7, 13, 7,  5,  9,  5,  7, 10,  7, 10, 14,  2, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );

  pair_t* closestPairBruteForce = pair.create();
  if (closestPairBruteForce == NULL)
  {
    return;
  }

  pair_t* closestPairRecurse = pair.create();
  if (closestPairRecurse == NULL)
  {
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    return;
  }

  ensemble_t* ens = ensemble.create(numel);
  point_t* points = ens -> points;
  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> set(point, x[i], y[i], i);
    ++point;
  }

  closestPairBruteForce -> set(closestPairBruteForce, numel, numel, INFINITY);
  bruteForce(ens, closestPairBruteForce);

  closestPairRecurse -> set(closestPairRecurse, numel, numel, INFINITY);
  recurse(ens, 0, numel, closestPairRecurse);

  bool failed = !closestPairRecurse -> cmp(closestPairRecurse, closestPairBruteForce);
  printf("test-recurse[0]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  ens = ensemble.destroy(ens);
  closestPairBruteForce = pair.destroy(closestPairBruteForce);
  closestPairRecurse = pair.destroy(closestPairRecurse);
}


// tests the implementation with problem 2.3.1-2 of reference [1]
void test_recurse1 ()
{
  double x[] = {1,  1, 7, 9, 12, 13, 20, 22, 23, 25, 26, 31, 64, -64, -64,  64};
  double y[] = {2, 11, 8, 9, 13,  4,  8,  3, 12, 14,  7, 10, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );

  pair_t* closestPairBruteForce = pair.create();
  if (closestPairBruteForce == NULL)
  {
    return;
  }

  pair_t* closestPairRecurse = pair.create();
  if (closestPairRecurse == NULL)
  {
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    return;
  }

  ensemble_t* ens = ensemble.create(numel);
  point_t* points = ens -> points;
  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> set(point, x[i], y[i], i);
    ++point;
  }

  closestPairBruteForce -> set(closestPairBruteForce, numel, numel, INFINITY);
  bruteForce(ens, closestPairBruteForce);

  closestPairRecurse -> set(closestPairRecurse, numel, numel, INFINITY);
  recurse(ens, 0, numel, closestPairRecurse);

  bool failed = !closestPairRecurse -> cmp(closestPairRecurse, closestPairBruteForce);
  printf("test-recurse[1]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  ens = ensemble.destroy(ens);
  closestPairBruteForce = pair.destroy(closestPairBruteForce);
  closestPairRecurse = pair.destroy(closestPairRecurse);
}


// tests the implementation with problem 2.3.1-3 of reference [1]
void test_recurse2 ()
{
  double x[] = {2,  2, 5,  9, 11, 15, 17, 18, 22, 25, 28, 30, 64, -64, -64,  64};
  double y[] = {2, 12, 4, 11,  4, 14, 13,  7,  4,  7, 14,  2, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );

  pair_t* closestPairBruteForce = pair.create();
  if (closestPairBruteForce == NULL)
  {
    return;
  }

  pair_t* closestPairRecurse = pair.create();
  if (closestPairRecurse == NULL)
  {
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    return;
  }

  ensemble_t* ens = ensemble.create(numel);
  point_t* points = ens -> points;
  point_t* point = points;
  for (size_t i = 0; i != numel; ++i)
  {
    point -> set(point, x[i], y[i], i);
    ++point;
  }

  closestPairBruteForce -> set(closestPairBruteForce, numel, numel, INFINITY);
  bruteForce(ens, closestPairBruteForce);

  closestPairRecurse -> set(closestPairRecurse, numel, numel, INFINITY);
  recurse(ens, 0, numel, closestPairRecurse);

  bool failed = !closestPairRecurse -> cmp(closestPairRecurse, closestPairBruteForce);
  printf("test-recurse[2]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  ens = ensemble.destroy(ens);
  closestPairBruteForce = pair.destroy(closestPairBruteForce);
  closestPairRecurse = pair.destroy(closestPairRecurse);
}


void test_recurse3 ()
{
  pair_t* closestPairBruteForce = pair.create();
  if (closestPairBruteForce == NULL)
  {
    return;
  }

  pair_t* closestPairRecurse = pair.create();
  if (closestPairRecurse == NULL)
  {
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    return;
  }

  bool failed = false;
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    ensemble_t* ens = ensemble.create(numel);
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(ens);

      closestPairBruteForce -> set(closestPairBruteForce, numel, numel, INFINITY);
      bruteForce(ens, closestPairBruteForce);

      closestPairRecurse -> set(closestPairRecurse, numel, numel, INFINITY);
      recurse(ens, 0, numel, closestPairRecurse);

      failed = !closestPairRecurse -> cmp(closestPairRecurse, closestPairBruteForce);
      if (failed)
      {
	printf("bruteForce(): ");
	closestPairBruteForce -> log(closestPairBruteForce);
	printf("recurse(): ");
	closestPairRecurse -> log(closestPairRecurse);
	break;
      }
    }

    if (failed)
    {
      ens = ensemble.destroy(ens);
      break;
    }

    ens = ensemble.destroy(ens);
    numel *= 2;
  }

  printf("test-recurse[3]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  closestPairBruteForce = pair.destroy(closestPairBruteForce);
  closestPairRecurse = pair.destroy(closestPairRecurse);
}


void test_sort ()
{
  bool failed = false;
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    ensemble_t* ens = ensemble.create(numel);

    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(ens);

      sort(ens, 0, numel, point.xcomparator);

      failed = !sorted(ens, 0, numel, point.xcomparator);
      if (failed)
      {
	break;
      }
    }

    if (failed)
    {
      ens = ensemble.destroy(ens);
      break;
    }

    ens = ensemble.destroy(ens);
    numel *= 2;
  }

  printf("test-sort[0]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }
}


void complexity_sort ()
{
  bool failed = false;
  double etimes[RUNS];
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    ensemble_t* ens = ensemble.create(numel);

    double etime = 0;
    struct timespec end;
    struct timespec begin;
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(ens);

      clock_gettime(CLOCK_MONOTONIC_RAW, &begin);

      sort(ens, 0, numel, point.xcomparator);

      clock_gettime(CLOCK_MONOTONIC_RAW, &end);

      etime += getElapsedTime(&begin, &end);

      failed = !sorted(ens, 0, numel, point.xcomparator);
      if (failed)
      {
	break;
      }
    }

    etimes[run] = etime / ( (double) REPS );

    if (failed)
    {
      ens = ensemble.destroy(ens);
      break;
    }

    ens = ensemble.destroy(ens);
    numel *= 2;
  }

  printf("test-sort[1]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  if (failed)
  {
    return;
  }

  const char fname[] = "complexity-sort.txt";
  FILE* file = fopen(fname, "w");

  if (file == NULL)
  {
    printf("complexity-sort(): IO ERROR with file %s\n", fname);
    return;
  }

  numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    fprintf(file, "%lu %.16e\n", numel, etimes[run]);
    numel *= 2;
  }

  fclose(file);
  printf("complexity-sort(): time complexity results have been writen to %s\n", fname);
}


void complexity_recurse ()
{
  pair_t* closestPairBruteForce = pair.create();
  if (closestPairBruteForce == NULL)
  {
    return;
  }

  pair_t* closestPairRecurse = pair.create();
  if (closestPairRecurse == NULL)
  {
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    return;
  }

  bool failed = false;
  double etimes[RUNS];
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    double etime = 0;
    struct timespec end;
    struct timespec begin;
    ensemble_t* ens = ensemble.create(numel);
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(ens);

      closestPairBruteForce -> set(closestPairBruteForce, numel, numel, INFINITY);
      bruteForce(ens, closestPairBruteForce);

      clock_gettime(CLOCK_MONOTONIC_RAW, &begin);

      closestPairRecurse -> set(closestPairRecurse, numel, numel, INFINITY);
      recurse(ens, 0, numel, closestPairRecurse);

      clock_gettime(CLOCK_MONOTONIC_RAW, &end);

      etime += getElapsedTime(&begin, &end);

      failed = !closestPairRecurse -> cmp(closestPairRecurse, closestPairBruteForce);
      if (failed)
      {
	printf("bruteForce(): ");
	closestPairBruteForce -> log(closestPairBruteForce);
	printf("recurse(): ");
	closestPairRecurse -> log(closestPairRecurse);
	break;
      }
    }

    etimes[run] = etime / ( (double) REPS );

    if (failed)
    {
      ens = ensemble.destroy(ens);
      break;
    }

    ens = ensemble.destroy(ens);
    numel *= 2;
  }

  printf("test-recurse[4]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  const char fname[] = "complexity-recurse.txt";
  FILE* file = fopen(fname, "w");
  if (file == NULL)
  {
    closestPairRecurse = pair.destroy(closestPairRecurse);
    closestPairBruteForce = pair.destroy(closestPairBruteForce);
    printf("complexity-recurse(): IO ERROR with file %s\n", fname);
    return;
  }

  numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    fprintf(file, "%lu %.16e\n", numel, etimes[run]);
    numel *= 2;
  }

  fclose(file);
  closestPairRecurse = pair.destroy(closestPairRecurse);
  closestPairBruteForce = pair.destroy(closestPairBruteForce);
  printf("complexity-recurse(): time complexity results have been writen to %s\n", fname);
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
