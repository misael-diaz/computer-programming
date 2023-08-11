#include <stdio.h>	// uses console logging and other output stream facilities
#include <stdlib.h>	// uses uses dynamic memory (de)allocation via malloc() and free()
#include <time.h>	// uses clock_gettime() to measure the elapsed time
#include "util.h"	// uses user-defined utilities to find the closest pair

#define RUNS 16
#define REPS 256
#define iNUMEL 2

void test_sort();
void complexity_sort();

int main ()
{
  test_sort();
  complexity_sort();
  return 0;
}


// void generate (double* positions, size_t const numel)
//
// Synopsis:
// Generates (distinct) positions for `numel' particles. Stores the positions of the
// particles along the x axis in the arange [0, numel) and the positions along the
// y axis in the arange [numel, 2 * numel). The expected time complexity is quadratic.
//
// Inputs:
// positions		array of (at least) size (2 * numel)
// numel		the number of elements (or particles)
//
// Output:
// positions		array filled with (distinct) positions


void generate (double* positions, size_t const numel)
{
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
    size_t const size = (2 * numel);
    double* original = create(2 * size);
    double* positions = create(2 * size);
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      generate(original, numel);
      copy(original, positions, size);

      sort(positions, size, numel, xcompare);

      // complains if there are out-of-order elements after invoking sort():

      if ( !sorted(positions, numel, 0, numel, xcompare) )
      {
	failed = true;
	printf("test-sort(): sort() method failed\n");
	break;
      }

      // complains if sort() modified the actual positions (should only order them):

      const double* xsrc = original;
      const double* ysrc = (original + numel);
      // writes the target element at the unused location (in the temporary placeholder)
      double* xdst = (positions + size);
      double* ydst = (positions + size + numel);
      // performs a linear search for the target element
      for (size_t i = 0; i != numel; ++i)
      {
	xdst[0] = xsrc[i];
	ydst[0] = ysrc[i];
	if (search(positions, numel, xcompare) == -1)
	{
	  failed = true;
	  printf("test-sort(): the sort() method clobbered (some) particle positions\n");
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
      original = destroy(original);
      positions = destroy(positions);
      break;
    }

    original = destroy(original);
    positions = destroy(positions);
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
    size_t const size = (2 * numel);
    double* original = create(2 * size);
    if (original == NULL)
    {
      free(begin);
      free(end);
      begin = NULL;
      end = NULL;
      printf("complexity-sort(): failed to allocate memory for the particle positions\n");
      return;
    }

    double* positions = create(2 * size);
    if (positions == NULL)
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
      generate(original, numel);
      copy(original, positions, size);

      // gets the elapsed time of the sort() method

      clock_gettime(CLOCK_MONOTONIC_RAW, begin);

      sort(positions, size, numel, xcompare);

      clock_gettime(CLOCK_MONOTONIC_RAW, end);

      etime += getElapsedTime(begin, end);

      // complains if there are out-of-order elements after invoking sort():

      if ( !sorted(positions, numel, 0, numel, xcompare) )
      {
	failed = true;
	printf("complexity-sort(): sort() method failed\n");
	break;
      }

      // complains if sort() modified the actual positions (should only order them):

      const double* xsrc = original;
      const double* ysrc = (original + numel);
      double* xdst = (positions + size);
      double* ydst = (positions + size + numel);
      // performs a linear search for the target element
      for (size_t i = 0; i != numel; ++i)
      {
	xdst[0] = xsrc[i];
	ydst[0] = ysrc[i];
	if (search(positions, numel, xcompare) == -1)
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
      positions = destroy(positions);
      break;
    }

    etimes[run] = etime / ( (double) REPS );	// gets the average elapsed time (nanosec)

    original = destroy(original);
    positions = destroy(positions);
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
