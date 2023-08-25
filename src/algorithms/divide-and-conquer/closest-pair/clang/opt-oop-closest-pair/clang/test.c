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

void divide(const ensemble_t* Ex, const ensemble_t* Ey, pair_t* closestPair);
void recurse(const ensemble_t* Ex, const ensemble_t* Ey, pair_t* closestPair);

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


void direct (const ensemble_t* ens, pair_t* closestPair)
{
  const point_t* points = ens -> points;
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


void xcombine (const ensemble_t* leftEx, const ensemble_t* rightEx, pair_t* closestPair)
{
  size_t const numel = *(leftEx -> numel);
  size_t const beginLeft = 0;
  size_t const endLeft = numel;
  size_t const beginRight = 0;
  size_t const endRight = numel;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  double const dist = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const offset = ( numel - (i + 1) );
    const point_t* pivot = rightEx -> points;
    const point_t* left = leftEx -> points + offset;
    double const d = left -> dist_x(left, pivot);
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
  for (size_t i = 0; i != (endRight - beginRight); ++i)
  {
    size_t const offset = i;
    size_t const last = (numel - 1);
    const point_t* pivot = leftEx -> points + last;
    const point_t* right = rightEx -> points + offset;
    double const d = right -> dist_x(right, pivot);
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

  size_t first = closestPair -> getFirst(closestPair);
  size_t second = closestPair -> getSecond(closestPair);
  double dmin = closestPair -> getDistance(closestPair);
  const point_t* left = leftEx -> points + bLeft;
  for (size_t i = 0; i != (eLeft - bLeft); ++i)
  {
    const point_t* right = rightEx -> points + bRight;
    for (size_t j = 0; j != (eRight - bRight); ++j)
    {
      double const d =left -> dist(left, right);
      if (d < dmin)
      {
	first = left -> getID(left);
	second = right -> getID(right);
	dmin = d;
      }
      ++right;
    }
    ++left;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


// as xcombine() but uses the y-axis distances to determine too far elements
void ycombine (const ensemble_t* leftEy, const ensemble_t* rightEy, pair_t* closestPair)
{
  size_t const numel = *(leftEy -> numel);
  size_t const beginLeft = 0;
  size_t const endLeft = numel;
  size_t const beginRight = 0;
  size_t const endRight = numel;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  double const dist = closestPair -> getDistance(closestPair);
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const offset = ( numel - (i + 1) );
    const point_t* pivot = rightEy -> points;
    const point_t* left = leftEy -> points + offset;
    double const d = left -> dist_y(left, pivot);
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
  for (size_t i = 0; i != (endRight - beginRight); ++i)
  {
    size_t const offset = i;
    size_t const last = (numel - 1);
    const point_t* pivot = leftEy -> points + last;
    const point_t* right = rightEy -> points + offset;
    double const d = right -> dist_y(right, pivot);
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

  size_t first = closestPair -> getFirst(closestPair);
  size_t second = closestPair -> getSecond(closestPair);
  double dmin = closestPair -> getDistance(closestPair);
  const point_t* left = leftEy -> points + bLeft;
  for (size_t i = 0; i != (eLeft - bLeft); ++i)
  {
    const point_t* right = rightEy -> points + bRight;
    for (size_t j = 0; j != (eRight - bRight); ++j)
    {
      double const d =left -> dist(left, right);
      if (d < dmin)
      {
	first = left -> getID(left);
	second = right -> getID(right);
	dmin = d;
      }
      ++right;
    }
    ++left;
  }
  closestPair -> set(closestPair, first, second, dmin);
}


// partitions the ensemble in the x dimension into left and right partitions
void xpart (const ensemble_t* Ex,
	    const ensemble_t* Ey,
	    ensemble_t* leftEx,
	    ensemble_t* rightEx,
	    ensemble_t* leftEy,
	    ensemble_t* rightEy)
{
  size_t const numel = *(Ex -> numel);

  const point_t* src = Ex -> points;
  point_t* dst = leftEx -> points;
  copy(dst, src, numel / 2);

  src += (numel / 2);
  dst = rightEx -> points;
  copy(dst, src, numel / 2);

  src = Ey -> points;
  point_t* left = leftEy -> points;
  point_t* right = rightEy -> points;
  point_t* pivot = rightEx -> points;
  for (size_t i = 0; i != numel; ++i)
  {
    if (point.xcomparator(src, pivot) == -1)
    {
      left -> clone(left, src);
      ++left;
    }
    else
    {
      right -> clone(right, src);
      ++right;
    }
    ++src;
  }
}


// partitions the ensemble in the y dimension into left and right partitions
void ypart (const ensemble_t* Ex,
	    const ensemble_t* Ey,
	    ensemble_t* leftEx,
	    ensemble_t* rightEx,
	    ensemble_t* leftEy,
	    ensemble_t* rightEy)
{
  size_t const numel = *(Ey -> numel);

  const point_t* src = Ey -> points;
  point_t* dst = leftEy -> points;
  copy(dst, src, numel / 2);

  src += (numel / 2);
  dst = rightEy -> points;
  copy(dst, src, numel / 2);

  src = Ex -> points;
  point_t* left = leftEx -> points;
  point_t* right = rightEx -> points;
  point_t* pivot = rightEy -> points;
  for (size_t i = 0; i != numel; ++i)
  {
    if (point.ycomparator(src, pivot) == -1)
    {
      left -> clone(left, src);
      ++left;
    }
    else
    {
      right -> clone(right, src);
      ++right;
    }
    ++src;
  }
}


// as recurse() but partitions the system in the y dimension
void divide (const ensemble_t* Ex, const ensemble_t* Ey, pair_t* closestPair)
{
  size_t const numel = *(Ey -> numel);
  if (numel == 2)
  {
    direct(Ey, closestPair);
  }
  else
  {
    ensemble_t* leftEx = ensemble.create(numel / 2);
    ensemble_t* rightEx = ensemble.create(numel / 2);
    ensemble_t* leftEy = ensemble.create(numel / 2);
    ensemble_t* rightEy = ensemble.create(numel / 2);

    ypart(Ex, Ey, leftEx, rightEx, leftEy, rightEy);

    pair_t* closestPairLeft = pair.create();
    recurse(leftEx, leftEy, closestPairLeft);

    pair_t* closestPairRight = pair.create();
    recurse(rightEx, rightEy, closestPairRight);

    closestPair -> min(closestPair, closestPairLeft, closestPairRight);

    ycombine(leftEy, rightEy, closestPair);

    leftEx = ensemble.destroy(leftEx);
    rightEx = ensemble.destroy(rightEx);
    leftEy = ensemble.destroy(leftEy);
    rightEy = ensemble.destroy(rightEy);
    closestPairLeft = pair.destroy(closestPairLeft);
    closestPairRight = pair.destroy(closestPairRight);
  }
}


void recurse (const ensemble_t* Ex, const ensemble_t* Ey, pair_t* closestPair)
{
  size_t const numel = *(Ex -> numel);
  if (numel == 2)
  {
    direct(Ex, closestPair);
  }
  else
  {
    ensemble_t* leftEx = ensemble.create(numel / 2);
    ensemble_t* rightEx = ensemble.create(numel / 2);
    ensemble_t* leftEy = ensemble.create(numel / 2);
    ensemble_t* rightEy = ensemble.create(numel / 2);

    xpart(Ex, Ey, leftEx, rightEx, leftEy, rightEy);

    pair_t* closestPairLeft = pair.create();
    divide(leftEx, leftEy, closestPairLeft);

    pair_t* closestPairRight = pair.create();
    divide(rightEx, rightEy, closestPairRight);

    closestPair -> min(closestPair, closestPairLeft, closestPairRight);

    xcombine(leftEx, rightEx, closestPair);

    leftEx = ensemble.destroy(leftEx);
    rightEx = ensemble.destroy(rightEx);
    leftEy = ensemble.destroy(leftEy);
    rightEy = ensemble.destroy(rightEy);
    closestPairLeft = pair.destroy(closestPairLeft);
    closestPairRight = pair.destroy(closestPairRight);
  }
}


// defines an interface to the Divide-And-Conquer algorithm that finds the closest pair
void divideAndConquer (const ensemble_t* ens, pair_t* closestPair)
{
  size_t const numel = *(ens -> numel);

  ensemble_t* Ex = ensemble.create(numel);
  Ex -> clone(Ex, ens);
  sort(Ex, 0, numel, point.xcomparator);

  ensemble_t* Ey = ensemble.create(numel);
  Ey -> clone(Ey, ens);
  sort(Ey, 0, numel, point.ycomparator);

  recurse(Ex, Ey, closestPair);

  Ex = ensemble.destroy(Ex);
  Ey = ensemble.destroy(Ey);
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
  divideAndConquer(ens, closestPairRecurse);

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
  divideAndConquer(ens, closestPairRecurse);

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
  divideAndConquer(ens, closestPairRecurse);

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
      divideAndConquer(ens, closestPairRecurse);

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
      divideAndConquer(ens, closestPairRecurse);

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

Algorithms and Complexity					August 24, 2023

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
