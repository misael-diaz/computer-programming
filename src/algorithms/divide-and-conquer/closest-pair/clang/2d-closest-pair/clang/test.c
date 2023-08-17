#include <stdio.h>	// uses console logging and other output stream facilities
#include <stdlib.h>	// uses uses dynamic memory (de)allocation via malloc() and free()
#include <time.h>	// uses clock_gettime() to measure the elapsed time
#include <math.h>	// uses sqrt() and the floating-point representation of INFINITY
#include "util.h"	// uses user-defined utilities to find the closest pair

#define RUNS 16
#define REPS 1024
#define iNUMEL 2

void test_sort();
void complexity_sort();
void complexity_recurse();
void test_bruteForce();
void test_recurse();
void test_recurse1();
void test_recurse2();
void test_recurse3();
void divide(particle_t* particles, size_t const b, size_t const e, pair_t* closestPair);
void recurse(particle_t* particles, size_t const b, size_t const e, pair_t* closestPair);

int main ()
{
  test_recurse();
  test_recurse1();
  test_recurse2();
  test_recurse3();
  test_bruteForce();
  test_sort();
  complexity_sort();
  complexity_recurse();
  return 0;
}


// void generate (particle_t* particles)
//
// Synopsis:
// Generates (distinct) positions for `numel' particles. Stores the positions of the
// particles along the x axis in the arange [0, numel) and the positions along the
// y axis in the arange [numel, 2 * numel). The expected time complexity is quadratic.
//
// Inputs:
// particles	container of the x, y coordinates of the particles
//
// Output:
// particles	container with distinct x, y coordinates for the particles


void generate (particle_t* particles)
{
  double* positions = particles -> x;
  size_t const numel = ( (size_t) *(particles -> numel) );
  // the total number of x's and y's is 2 * numel
  for (size_t i = 0; i != (2 * numel); ++i)
  {
    double r = urand(numel);
    while ( contains(positions, 0, i, r) )
    {
      r = urand(numel);
    }
    positions[i] = r;
  }
}


// returns true if the system has duplicate closest pairs, false otherwise
bool hasDuplicateClosestPairs (const particle_t* particles)
{
  double dmin1 = INFINITY;
  double dmin2 = INFINITY;
  const double* x = particles -> x;
  const double* y = particles -> y;
  size_t const numel = ( (size_t) *(particles -> numel) );
  for (size_t i = 0; i != (numel - 1); ++i)
  {
    for (size_t j = (i + 1); j != numel; ++j)
    {
      double const d = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
      if (d <= dmin1)
      {
	dmin2 = dmin1;
	dmin1 = d;
      }
    }
  }
  bool const hasDuplicateClosestPairs = ( (dmin1 == dmin2)? true : false );
  return hasDuplicateClosestPairs;
}


// initializes the system so that there's only one closest pair
void initialize (particle_t* particles)
{
  do
  {
    generate(particles);
  } while ( hasDuplicateClosestPairs(particles) );
  iota(particles -> id, *particles -> numel);
}


// applies the implementation of the brute force algorithm to find the closest pair
void bruteForce (const particle_t* particles, pair_t* closestPair)
{
  double dmin = INFINITY;
  const double* x = particles -> x;
  const double* y = particles -> y;
  size_t first = 0xffffffffffffffff;
  size_t second = 0xffffffffffffffff;
  size_t const numel = ( (size_t) *(particles -> numel) );
  for (size_t i = 0; i != (numel - 1); ++i)
  {
    for (size_t j = (i + 1); j != numel; ++j)
    {
      double const d = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
      if (d < dmin)
      {
	first = i;
	second = j;
	dmin = d;
      }
    }
  }
  setClosestPair(closestPair, first, second, dmin);
}


// direct solution, updates the closest pair by considering a pair of particles
void direct (const particle_t* particles,
	     size_t const beg,
	     size_t const end,
	     pair_t* closestPair)
{
  size_t const offset = beg;
  const double* x = (particles -> x + offset);
  const double* y = (particles -> y + offset);
  const double* id = (particles -> id + offset);
  double const dist = (x[0] - x[1]) * (x[0] - x[1]) + (y[0] - y[1]) * (y[0] - y[1]);
  double const dmin = closestPair -> dist;
  if (dist < dmin)
  {
    size_t const first = id[0];
    size_t const second = id[1];
    setClosestPair(closestPair, first, second, dist);
  }
}



// prunes elements from the combined partition prior to using brute force to obtain the
// closest pair
void xcombine (particle_t* particles,
	       size_t const beg,
	       size_t const end,
	       pair_t* closestPair)
{
  size_t const numel = (end - beg);
  size_t const beginLeft = beg;
  size_t const endLeft = beg + (numel / 2);
  size_t const beginRight = beg + (numel / 2);
  size_t const endRight = beg + numel;

  const double* x = particles -> x;
  const double* y = particles -> y;
  const double* id = particles -> id;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  double const dist = closestPair -> dist;
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const j = ( endLeft - (i + 1) );
    double const d = (x[j] - x[beginRight]) * (x[j] - x[beginRight]);
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
    size_t const lastLeft = (endLeft - 1);
    double const d = (x[i] - x[lastLeft]) * (x[i] - x[lastLeft]);
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

  double dmin = closestPair -> dist;
  size_t first = closestPair -> first;
  size_t second = closestPair -> second;
  for (size_t i = bLeft; i != eLeft; ++i)
  {
    for (size_t j = bRight; j != eRight; ++j)
    {
      double const d = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
      if (d < dmin)
      {
	first = ( (size_t) id[i] );
	second = ( (size_t) id[j] );
	dmin = d;
      }
    }
  }
  setClosestPair(closestPair, first, second, dmin);
}


// as xcombine() but uses the y-component of the position vector to compute the distance
void ycombine (particle_t* particles,
	       size_t const beg,
	       size_t const end,
	       pair_t* closestPair)
{
  size_t const numel = (end - beg);
  size_t const beginLeft = beg;
  size_t const endLeft = beg + (numel / 2);
  size_t const beginRight = beg + (numel / 2);
  size_t const endRight = beg + numel;

  const double* x = particles -> x;
  const double* y = particles -> y;
  const double* id = particles -> id;

  // prunes elements (too far to comprise the closest pair) from the left partition:

  size_t bLeft = endLeft;
  size_t const eLeft = endLeft;
  double const dist = closestPair -> dist;
  for (size_t i = 0; i != (endLeft - beginLeft); ++i)
  {
    size_t const j = ( endLeft - (i + 1) );
    double const d = (y[j] - y[beginRight]) * (y[j] - y[beginRight]);
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
    size_t const lastLeft = (endLeft - 1);
    double const d = (y[i] - y[lastLeft]) * (y[i] - y[lastLeft]);
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

  double dmin = closestPair -> dist;
  size_t first = closestPair -> first;
  size_t second = closestPair -> second;
  for (size_t i = bLeft; i != eLeft; ++i)
  {
    for (size_t j = bRight; j != eRight; ++j)
    {
      double const d = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
      if (d < dmin)
      {
	first = ( (size_t) id[i] );
	second = ( (size_t) id[j] );
	dmin = d;
      }
    }
  }
  setClosestPair(closestPair, first, second, dmin);
}


// as recurse() but partitions the system in the y dimension
void divide (particle_t* particles,
	     size_t const beg,
	     size_t const end,
	     pair_t* closestPair)
{
  size_t const numel = (end - beg);
  if (numel == 2)
  {
    direct(particles, beg, end, closestPair);
  }
  else
  {
    sort(particles, beg, end, ycompare);

    size_t const beginLeft = beg;
    size_t const endLeft = beg + (numel / 2);
    size_t const size = ( (size_t) *(particles -> numel) );
    pair_t closestPairLeft = { .first = size, .second = size, .dist = INFINITY };
    recurse(particles, beginLeft, endLeft, &closestPairLeft);

    size_t const beginRight = beg + (numel / 2);
    size_t const endRight = beg + numel;
    pair_t closestPairRight = { .first = size, .second = size, .dist = INFINITY };
    recurse(particles, beginRight, endRight, &closestPairRight);

    minClosestPair(closestPair, &closestPairLeft, &closestPairRight);

    ycombine(particles, beg, end, closestPair);

    // NOTE: we need to restore the x - y sorting because the xcombine() method at the
    // level of the caller method recurse() expects it (as if we didn't call sort() here)
    sort(particles, beg, end, xcompare);
  }
}


// finds the closest pair recursively
void recurse (particle_t* particles,
	      size_t const beg,
	      size_t const end,
	      pair_t* closestPair)
{
  size_t const numel = (end - beg);
  if (numel == 2)
  {
    direct(particles, beg, end, closestPair);
  }
  else
  {
    sort(particles, beg, end, xcompare);

    size_t const beginLeft = beg;
    size_t const endLeft = beg + (numel / 2);
    size_t const size = ( (size_t) *(particles -> numel) );
    pair_t closestPairLeft = { .first = size, .second = size, .dist = INFINITY };
    divide(particles, beginLeft, endLeft, &closestPairLeft);

    size_t const beginRight = beg + (numel / 2);
    size_t const endRight = beg + numel;
    pair_t closestPairRight = { .first = size, .second = size, .dist = INFINITY };
    divide(particles, beginRight, endRight, &closestPairRight);

    minClosestPair(closestPair, &closestPairLeft, &closestPairRight);

    xcombine(particles, beg, end, closestPair);

    // NOTE: we need to restore the y - x sorting because the ycombine() method at the
    // level of the caller method divide() expects it (as if we didn't call sort() here)
    sort(particles, beg, end, ycompare);
  }
}


// tests the implementation of the brute force algorithm that finds the closest pair
void test_bruteForce ()
{
  pair_t* closestPair = malloc( sizeof(pair_t) );
  if (closestPair == NULL)
  {
    printf("test-bruteForce(): failed to allocate memory for the closest pair\n");
    return;
  }

  size_t const numel = 256;
  particle_t* particles = create(numel);
  if (particles == NULL)
  {
      free(closestPair);
      closestPair = NULL;
      printf("test-bruteForce(): failed to allocate memory for the particle positions\n");
      return;
  }

  initialize(particles);
  bruteForce(particles, closestPair);

  double const d = sqrt(closestPair -> dist);
  size_t const first = closestPair -> first;
  size_t const second = closestPair -> second;
  printf("bruteForce(): first: %lu second: %lu min-distance: %e\n", first, second, d);

  free(closestPair);
  closestPair = NULL;
  particles = destroy(particles);
}


// tests the implementation with problem 2.3.1-1 of reference [1]
void test_recurse ()
{
  // note that we have added four more elements to meet create()'s requirements
  double x[] = {2,  4, 5, 10, 13, 15, 17, 19, 22, 25, 29, 30, 64, -64, -64,  64};
  double y[] = {7, 13, 7,  5,  9,  5,  7, 10,  7, 10, 14,  2, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );
  particle_t* particles = create(numel);
  if (particles == NULL)
  {
    return;
  }

  double* xdst = particles -> x;
  double* ydst = particles -> y;
  const double *xsrc = x;
  const double *ysrc = y;
  copy(xsrc, xdst, numel);
  copy(ysrc, ydst, numel);

  pair_t closestPairBruteForce = {.first = numel, .second = numel, .dist = -INFINITY};
  bruteForce(particles, &closestPairBruteForce);

  pair_t closestPairRecurse = {.first = numel, .second = numel, .dist = +INFINITY};
  recurse(particles, 0, numel, &closestPairRecurse);

  bool failed = true;
  if ( !isEqualClosestPair(&closestPairBruteForce, &closestPairRecurse) )
  {
    failed = true;
  }
  else
  {
    failed = false;
  }

  printf("test-recurse[0]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  particles = destroy(particles);
}


// tests the implementation with problem 2.3.1-2 of reference [1]
void test_recurse1 ()
{
  // note that we have added four more elements to meet create()'s requirements
  double x[] = {1,  1, 7, 9, 12, 13, 20, 22, 23, 25, 26, 31, 64, -64, -64,  64};
  double y[] = {2, 11, 8, 9, 13,  4,  8,  3, 12, 14,  7, 10, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );
  particle_t* particles = create(numel);
  if (particles == NULL)
  {
    return;
  }

  double* xdst = particles -> x;
  double* ydst = particles -> y;
  const double *xsrc = x;
  const double *ysrc = y;
  copy(xsrc, xdst, numel);
  copy(ysrc, ydst, numel);

  pair_t closestPairBruteForce = {.first = numel, .second = numel, .dist = -INFINITY};
  bruteForce(particles, &closestPairBruteForce);

  pair_t closestPairRecurse = {.first = numel, .second = numel, .dist = +INFINITY};
  recurse(particles, 0, numel, &closestPairRecurse);

  bool failed = true;
  if ( !isEqualClosestPair(&closestPairBruteForce, &closestPairRecurse) )
  {
    failed = true;
  }
  else
  {
    failed = false;
  }

  printf("test-recurse[1]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  particles = destroy(particles);
}


// tests the implementation with problem 2.3.1-3 of reference [1]
void test_recurse2 ()
{
  // note that we have added four more elements to meet create()'s requirements
  double x[] = {2,  2, 5,  9, 11, 15, 17, 18, 22, 25, 28, 30, 64, -64, -64,  64};
  double y[] = {2, 12, 4, 11,  4, 14, 13,  7,  4,  7, 14,  2, 64,  64, -64, -64};
  size_t const numel = ( sizeof(x) / sizeof(double) );
  particle_t* particles = create(numel);
  if (particles == NULL)
  {
    return;
  }

  double* xdst = particles -> x;
  double* ydst = particles -> y;
  const double *xsrc = x;
  const double *ysrc = y;
  copy(xsrc, xdst, numel);
  copy(ysrc, ydst, numel);

  pair_t closestPairBruteForce = {.first = numel, .second = numel, .dist = -INFINITY};
  bruteForce(particles, &closestPairBruteForce);

  pair_t closestPairRecurse = {.first = numel, .second = numel, .dist = +INFINITY};
  recurse(particles, 0, numel, &closestPairRecurse);

  bool failed = true;
  if ( !isEqualClosestPair(&closestPairBruteForce, &closestPairRecurse) )
  {
    failed = true;
  }
  else
  {
    failed = false;
  }

  printf("test-recurse[2]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  particles = destroy(particles);
}


void test_recurse3 ()
{
  bool failed = false;
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    particle_t* particles = create(numel);
    if (particles == NULL)
    {
      printf("test-recurse3(): failed to allocate memory for the particle positions\n");
      return;
    }

    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(particles);
      pair_t closestPairBruteForce = {.first = numel, .second = numel, .dist = -INFINITY};
      bruteForce(particles, &closestPairBruteForce);

      pair_t closestPairRecurse = {.first = numel, .second = numel, .dist = +INFINITY};
      recurse(particles, 0, numel, &closestPairRecurse);

      if ( !isEqualClosestPair(&closestPairBruteForce, &closestPairRecurse) )
      {
	logClosestPair(&closestPairBruteForce);
	logClosestPair(&closestPairRecurse);
	failed = true;
	break;
      }
    }

    if (failed)
    {
      particles = destroy(particles);
      break;
    }

    particles = destroy(particles);
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
}


// void test_sort()
//
// Synopsis:
// Checks that sort() orders the particle positions according to the supplied comparator
// function without altering the (original) positions (as it should be for the sorting
// method should only re-arrange their storage location).


void test_sort ()
{
  bool failed = false;
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    particle_t* original = create(numel);
    if (original == NULL)
    {
      printf("test-sort(): failed to allocate memory for the particle positions\n");
      return;
    }

    particle_t* particles = create(numel);
    if (particles == NULL)
    {
      free(original);
      original = NULL;
      printf("test-sort(): failed to allocate memory for the particle positions\n");
      return;
    }

    for (size_t rep = 0; rep != REPS; ++rep)
    {
      generate(original);
      double* xdst = particles -> x;
      double* ydst = particles -> y;
      const double* xsrc = original -> x;
      const double* ysrc = original -> y;
      copy(xsrc, xdst, numel);
      copy(ysrc, ydst, numel);

      // note that on the previous iteration sort() moved the IDs so we need to set them
      iota(particles -> id, numel);
      sort(particles, 0, numel, xcompare);

      // complains if there are out-of-order elements after invoking sort():

      if ( !sorted(particles, 0, numel, xcompare) )
      {
	failed = true;
	printf("test-sort(): sort() method failed\n");
	break;
      }

      // complains if sort() modified the actual positions (should only order them):

      xdst = particles -> xtmp;
      ydst = particles -> ytmp;
      // performs a linear search for the target element
      for (size_t i = 0; i != numel; ++i)
      {
	xdst[0] = xsrc[i];
	ydst[0] = ysrc[i];
	if (search(particles, xcompare) == -1)
	{
	  failed = true;
	  printf("test-sort(): the sort() method clobbered (some) particle positions\n");
	  break;
	}
      }

      // checks that we can sort the original position arrays with the IDs:

      double diffs = 0;
      const double* id = particles -> id;
      for (size_t i = 0; i != numel; ++i)
      {
	double const x1 = particles -> x[i];
	double const x2 = (original -> x[( (size_t) id[i] )]);
	diffs += (x1 - x2) * (x1 - x2);
      }

      if (diffs != 0)
      {
	failed = true;
      }

      for (size_t i = 0; i != numel; ++i)
      {
	double const y1 = particles -> y[i];
	double const y2 = (original -> y[( (size_t) id[i] )]);
	diffs += (y1 - y2) * (y1 - y2);
      }

      if (diffs != 0)
      {
	failed = true;
      }

      if (failed)
      {
	break;
      }
    }

    if (failed)
    {
      original = destroy(original);
      particles = destroy(particles);
      break;
    }

    original = destroy(original);
    particles = destroy(particles);
    numel *= 2;
  }

  printf("sort-test[0]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }
}


// void complexity_sort()
//
// Synopsis:
// As test_sort() but exports the average runtime of the sorting algorithm sort() to a
// plain text file with respect to the input size N (the number of particles).


void complexity_sort ()
{
  struct timespec* begin = malloc( sizeof(struct timespec) );
  if (begin == NULL)
  {
    printf("complexity-sort(): failed to allocate memory for the timespec struct\n");
    return;
  }

  struct timespec* end = malloc( sizeof(struct timespec) );
  if (end == NULL)
  {
    free(begin);
    begin = NULL;
    printf("complexity-sort(): failed to allocate memory for the timespec struct\n");
    return;
  }

  bool failed = false;
  double etimes[RUNS];
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    particle_t* original = create(numel);
    if (original == NULL)
    {
      free(begin);
      free(end);
      begin = NULL;
      end = NULL;
      printf("complexity-sort(): failed to allocate memory for the particle positions\n");
      return;
    }

    particle_t* particles = create(numel);
    if (particles == NULL)
    {
      free(end);
      free(begin);
      free(original);
      original = NULL;
      begin = NULL;
      end = NULL;
      printf("complexity-sort(): failed to allocate memory for the particle positions\n");
      return;
    }

    double etime = 0;
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      generate(original);
      double* xdst = particles -> x;
      double* ydst = particles -> y;
      const double* xsrc = original -> x;
      const double* ysrc = original -> y;
      copy(xsrc, xdst, numel);
      copy(ysrc, ydst, numel);

      // gets the elapsed time of the sort() method

      clock_gettime(CLOCK_MONOTONIC_RAW, begin);

      sort(particles, 0, numel, xcompare);

      clock_gettime(CLOCK_MONOTONIC_RAW, end);

      etime += getElapsedTime(begin, end);

      // complains if there are out-of-order elements after invoking sort():

      if ( !sorted(particles, 0, numel, xcompare) )
      {
	failed = true;
	printf("complexity-sort(): sort() method failed\n");
	break;
      }

      // complains if sort() modified the actual positions (should only order them):

      xdst = particles -> xtmp;
      ydst = particles -> ytmp;
      // performs a linear search for the target element
      for (size_t i = 0; i != numel; ++i)
      {
	xdst[0] = xsrc[i];
	ydst[0] = ysrc[i];
	if (search(particles, xcompare) == -1)
	{
	  failed = true;
	  printf("complexity-sort(): the sort() method clobbered particle positions\n");
	  break;
	}
      }

      if (failed)
      {
	break;
      }
    }

    if (failed)
    {
      free(begin);
      free(end);
      begin = NULL;
      end = NULL;
      original = destroy(original);
      particles = destroy(particles);
      break;
    }

    etimes[run] = etime / ( (double) REPS );	// gets the average elapsed time (nanosec)

    original = destroy(original);
    particles = destroy(particles);
    numel *= 2;
  }

  printf("sort-test[1]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  const char fname[] = "complexity-sort.txt";
  FILE* file = fopen(fname, "w");
  if (file == NULL)
  {
    free(begin);
    free(end);
    begin = NULL;
    end = NULL;
    printf("complexity-sort(): IO ERROR with file %s\n", fname);
    return;
  }

  numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    fprintf(file, "%lu %.16e\n", numel, etimes[run]);
    numel *= 2;
  }

  free(begin);
  free(end);
  begin = NULL;
  end = NULL;
  fclose(file);
  printf("complexity-sort(): successful export of time complexity data to %s\n", fname);
}


void complexity_recurse ()
{
  double etimes[RUNS];
  bool failed = false;
  size_t numel = iNUMEL;
  for (size_t run = 0; run != RUNS; ++run)
  {
    particle_t* particles = create(numel);
    if (particles == NULL)
    {
      printf("complexity-recurse(): failed to allocate the particle positions\n");
      return;
    }

    double etime = 0;
    struct timespec end;
    struct timespec begin;
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      initialize(particles);
      pair_t closestPairBruteForce = {.first = numel, .second = numel, .dist = -INFINITY};
      bruteForce(particles, &closestPairBruteForce);

      pair_t closestPairRecurse = {.first = numel, .second = numel, .dist = +INFINITY};

      clock_gettime(CLOCK_MONOTONIC_RAW, &begin);

      recurse(particles, 0, numel, &closestPairRecurse);

      clock_gettime(CLOCK_MONOTONIC_RAW, &end);

      etime += getElapsedTime(&begin, &end);

      if ( !isEqualClosestPair(&closestPairBruteForce, &closestPairRecurse) )
      {
	logClosestPair(&closestPairBruteForce);
	logClosestPair(&closestPairRecurse);
	failed = true;
	break;
      }
    }

    etimes[run] = etime / ( (double) REPS );	// gets the average elapsed time (nanosec)

    if (failed)
    {
      particles = destroy(particles);
      break;
    }

    particles = destroy(particles);
    numel *= 2;
  }

  printf("test-complexity-recurse[0]: ");
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

  const char fname[] = "complexity-recurse.txt";
  FILE* file = fopen(fname, "w");
  if (file == NULL)
  {
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
  printf("complexity-recurse(): successful export of time complexity data to %s\n", fname);
}


/*

Algorithms and Complexity					August 10, 2023

source: test.c
author: @misael-diaz

Synopsis:
Solves the 2D closest pair problem (to add).

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/


// TODO:
// [x] fix the particle ID issue by tasking sort() with the manipulation of the IDs array
// [x] add test that checks that the IDs of the closest pair found by the brute force
//     and recursive algorithms match
