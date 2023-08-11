#include <stdlib.h>	// uses dynamic memory (de)allocation via malloc() and free()
#include <stdio.h>	// does logging to the console
#include <math.h>	// uses the rand() Pseudo Random Number Generator PRNG

#include "util.h"

// defines (internal) MACROS for succinctly declaring the comparator function

#define COMP int(*comp)(const double* x,\
			size_t const numel,\
			size_t const first,\
			size_t const second)


// double urand (double size)
//
// Synopsis:
// Implements a Pseudo Random Number Generator PRNG that yields uniformly distributed
// pseudo-random numbers. Returns uniformly distributed pseudo-random numbers in the
// range [-lim, +lim], where `lim' is determined from the `size' parameter.


double urand (double const size)
{
  double const lim = (size * size);
  double const min = -lim;
  double const max = +lim;
  double const urand = ( (double) rand() ) / ( (double) RAND_MAX );
  double const r = min + urand * (max - min);
  return round(r);
}


// double getElapsedTime (const struct timespec* b, const struct timespec* e)
//
// Synopsis:
// Gets the elapsed-time from the time difference `end' - `begin' in nanoseconds.
//
// Inputs:
// b			beginning time, set by POSIX clock_gettime()
// e			ending time, set by POSIX clock_gettime()
//
// Output:
// elpased-time		the elapsed time in nanoseconds between those two measurements


double getElapsedTime (const struct timespec* b, const struct timespec* e)
{
  double begin = ( (double) (b -> tv_nsec) ) + 1.0e9 * ( (double) (b -> tv_sec) );
  double end   = ( (double) (e -> tv_nsec) ) + 1.0e9 * ( (double) (e -> tv_sec) );
  return (end - begin);
}


// void zeros (double* x, size_t const size)
//
// Synopsis:
// Numerical Python (numpy) like zeros method.
// Fills the array `x' of size `size' with zeros.


static void zeros (double* x, size_t const size)
{
  for (size_t i = 0; i != size; ++i)
  {
    x[i] = 0.0;
  }
}


// int compare (double x1, double x2)
//
// Synopsis:
// Implements a comparator for floating-point numbers.
// Returns 0 if x1 == x2, 1 if x1 > x2, and -1 otherwise (when x1 < x2).
//
// Inputs:
// x1		first 64-bit floating-point number
// x2		second 64-bit floating-point number
//
// Output:
// compare	0 if x1 == x2, 1 if x1 > x2, and -1 otherwise (when x1 < x2).


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


// int xcompare (double* positions, size_t numel, size_t first, size_t second)
//
// Synopsis:
// Compares the `first' and `second' particles primarily with respect to their
// `x' positions and secondarily with respect to their `y' positions. In the
// Object Oriented Programming OOP sense we are defining what it means to compare
// a pair of particle objects.
//
// Inputs:
// positions	array of particle positions of (at least) size 2 * numel
// numel	the number of elements (or particles)
// first	id of the first particle, where id is in the asymmetric range [0, numel)
// second	id of the second particle, where id is in the arange [0, numel)
//
// Output:
// xcompare	0 if first == second, 1 if first > second, and -1 otherwise (in essence)


int xcompare (const double* positions,
	      size_t const numel,
	      size_t const first,
	      size_t const second)
{
  const double* x = positions;
  const double* y = (positions + numel);
  double const x1 = x[first];
  double const x2 = x[second];
  double const y1 = y[first];
  double const y2 = y[second];
  int const xcompare = (x1 != x2)? compare(x1, x2) : compare(y1, y2);
  return xcompare;
}


// int ycompare (double* positions, size_t numel, size_t first, size_t second)
//
// Synopsis:
// As xcompare() but compares the `first' and `second' particles primarily with respect
// to their `y' positions and secondarily with respect to their `x' positions.
//
// Inputs:
// positions	array of particle positions of (at least) size 2 * numel
// numel	the number of elements (or particles)
// first	id of the first particle, where id is in the asymmetric range [0, numel)
// second	id of the second particle, where id is in the arange [0, numel)
//
// Output:
// ycompare	0 if first == second, 1 if first > second, and -1 otherwise (in essence)


int ycompare (const double* positions,
	      size_t const numel,
	      size_t const first,
	      size_t const second)
{
  const double* x = positions;
  const double* y = (positions + numel);
  double const x1 = x[first];
  double const x2 = x[second];
  double const y1 = y[first];
  double const y2 = y[second];
  int const ycompare = (y1 != y2)? compare(y1, y2) : compare(x1, x2);
  return ycompare;
}


// bool sorted(double* positions, size_t numel, size_t b, size_t e, int (*comp) (...))
//
// Synopsis:
// Returns true if the positions array slice delimited by the asymmetric range [b, e) is
// sorted (in ascending order), false otherwise. The user must supply a comparator method
// for comparing the particles by their positions.
//
// Inputs:
// positions	array of particle positions of (at least) size 2 * numel
// numel	the number of elements (or particles)
// b		beginning of the array slice
// e		end (non-inclusive) of the array slice
// comp		comparator function, either xcompare() or ycompare()
//
// Output:
// sorted	true if sorted, false otherwise


bool sorted(const double* positions,
	    size_t const numel,
	    size_t const b,
	    size_t const e,
	    COMP)
{
  bool sorted = true;
  size_t const size = (e - b);
  if ( (size == 0) || (size == 1) )
  {
    return sorted;
  }

  for (size_t i = b; i != (e - 1); ++i)
  {
    if (comp(positions, numel, i + 1, i) == -1)
    {
      sorted = false;
      return sorted;
    }
  }

  return sorted;
}


// int64_t linsearch(double* x, int64_t b, int64_t e, double t)
//
// Synopsis:
// Implements linear search, does a look up for the target in the array slice [b, e) of x.
//
// Inputs:
// x		array of 64-bit floating-point numbers
// b		beginning of the array slice
// e		ending (non-inclusive) of the array slice
// t		target element
//
// Output:
// pos		the positional index of the target element if present, -1 otherwise


static int64_t linsearch (const double* x,
			  int64_t const b,
			  int64_t const e,
			  double const t)
{
  double const tgt = t;
  for (int64_t i = b; i != e; ++i)
  {
    if (x[i] == tgt)
    {
      return i;
    }
  }
  return -1;
}


// bool contains (double* x, int64_t b, int64_t e, double target)
//
// Synopsis:
// Implements linear search, does a look up for the target in the array slice [b, e) of x.
//
// Inputs:
// x		array of 64-bit floating-point numbers
// b		beginning of the array slice
// e		ending (non-inclusive) of the array slice
// target	target element
//
// Output:
// contains	returns true if the target element is present, false otherwise


bool contains (const double* x, int64_t const b, int64_t const e, double const target)
{
  bool const contains = ( (linsearch(x, b, e, target) == -1)? false : true );
  return contains;
}


// int64_t search (double* positions, size_t numel, int (*comp) (...))
//
// Synopsis:
// Performs a linear search for the target element. This method expects the target element
// to be located at the position i = 2 * numel by design.
//
// Inputs:
// positions	array of particle positions of (at least) size 2 * numel
// numel	the number of elements (or particles)
// comp		comparator function, either xcompare() or ycompare()
//
// Output:
// pos		the positional index of the target element if present, -1 otherwise


int64_t search (const double* positions, size_t const numel, COMP)
{
  for (size_t i = 0; i != numel; ++i)
  {
    if (comp(positions, numel, i, 2 * numel) == 0)
    {
      return i;
    }
  }
  return -1;
}


// void copy(double* src, double* dst, size_t numel)
//
// Synopsis:
// Copies numel elements from source `src' into destination `dst' array.
//
// Inputs:
// src		source array `src'
// numel	number of elements (of both the source and destination arrays)
//
// Output:
// dst		destination array `dst'


void copy (const double* restrict src, double* restrict dst, size_t const numel)
{
  for (size_t i = 0; i != numel; ++i)
  {
    dst[i] = src[i];
  }
}


// void direct(double* positions, size_t numel, size_t first, size_t second, int (*comp))
//
// Synopsis:
// Implements sort()'s direct solution, which consists of interchanging out-of-order
// neighboring elements.
//
// Inputs:
// positions	array of particle positions of (at least) size 2 * numel
// numel	the number of elements (or particles)
// first	id of the first particle, where id is in the asymmetric range [0, numel)
// second	id of the second particle, where id is in the arange [0, numel)
// comp		comparator function, either xcompare() or ycompare()
//
// Output:
// positions	the position array with the ordered pair comprised by `first' and `second'


static void direct (double* positions,
		    size_t const numel,
		    size_t const first,
		    size_t const second,
		    COMP)
{
  double* x = positions;
  double* y = (positions + numel);
  if (comp(positions, numel, second, first) == -1)
  {
    double const xmin = x[second];
    x[second] = x[first];
    x[first] = xmin;

    double const ymin = y[second];
    y[second] = y[first];
    y[first] = ymin;
  }
}


// void combine(double* positions, size_t size, size_t numel, size_t b, size_t e, (*comp))
//
// Synopsis:
// Combines the left and right partitions in O(N) operations, where `N' is the number
// of elements in the partition.
//
// Inputs:
// positions	positions array to be sorted (in ascending order)
// numel	the number of elements (or particles) same value as that given to sort()
// size		is twice the number of elements
// b		beginning of the partition
// e		end (non-inclusive) of the partition
// comp		comparator function, either xcompare() or ycompare()
//
// Output:
// positions	the sorted positions partition [b, e)


static void combine(double* positions,
		    size_t const size,
		    size_t const numel,
		    size_t const b,
		    size_t const e,
		    COMP)
{
  size_t const beginLeft = b;
  size_t const endLeft = b + ( (e - b) / 2 );
  size_t const beginRight = b + ( (e - b) / 2 );
  size_t const endRight = e;

  size_t n = 0;
  double* x = positions;
  double* y = (x + numel);
  double* xdst = (positions + size);
  double* ydst = (xdst + numel);
  size_t iterLeft = beginLeft;
  size_t iterRight = beginRight;
  // copies elements in order into the temporary until either partition is depleted
  while (iterLeft != endLeft && iterRight != endRight)
  {
    if (comp(positions, numel, iterLeft, iterRight) == -1)
    {
      xdst[n] = x[iterLeft];
      ydst[n] = y[iterLeft];
      ++iterLeft;
    }
    else
    {
      xdst[n] = x[iterRight];
      ydst[n] = y[iterRight];
      ++iterRight;
    }
    ++n;
  }

  // updates the number of elements written to the temporary

  xdst += n;
  ydst += n;

  // copies whatever that remains from the left partition (could be empty)

  size_t beg = iterLeft;
  size_t end = endLeft;
  const double* xsrc = (x + beg);
  copy(xsrc, xdst, end - beg);

  const double* ysrc = (y + beg);
  copy(ysrc, ydst, end - beg);

  // updates the number of elements written to the temporary

  xdst += (end - beg);
  ydst += (end - beg);

  // copies whatever that remains from the right partition (could be empty)

  beg = iterRight;
  end = endRight;
  x = positions;
  xsrc = (x + beg);
  copy(xsrc, xdst, end - beg);

  beg = iterRight;
  end = endRight;
  y = (positions + numel);
  ysrc = (y + beg);
  copy(ysrc, ydst, end - beg);

  // commits the combined partition into the partition [b, e)

  x = positions;
  xdst = (x + b);
  xsrc = (positions + size);
  copy(xsrc, xdst, e - b);

  y = (positions + numel);
  ydst = (y + b);
  ysrc = (positions + size + numel);
  copy(ysrc, ydst, e - b);
}


// void sort (double* positions, size_t size, size_t numel, int (*comp) (...))
//
// Synopsis:
// Iterative implementation of the merge sort algorithm.
//
// Inputs:
// positions	positions array to be sorted (in ascending order)
// numel	the number of elements (or particles)
// size		is twice the number of elements
// comp		comparator function, either xcompare() or ycompare()
//
// Output:
// positions	the sorted positions array


void sort (double* positions, size_t const size, size_t const numel, COMP)
{
  // orders particles in pairs (that is, in partitions of size 2):

  for (size_t i = 0; i != numel; i += 2)
  {
    size_t const first = i;
    size_t const second = (i + 1);
    direct(positions, numel, first, second, comp);
  }

  if (numel == 2)
  {
    return;
  }

  // combines the smaller partitions delimited by the asymmetric ranges [b, e):

  for (size_t stride = 4; stride != numel; stride *= 2)
  {
    for (size_t i = 0; i != numel; i += stride)
    {
      size_t const b = i;
      size_t const e = (i + stride);
      combine(positions, size, numel, b, e, comp);
    }
  }

  combine(positions, size, numel, 0, numel, comp);
}


// allocates memory and initializes the array of positions
double* create (size_t const size)
{
  // performs sane checks:

  if (size % 2)
  {
    printf("create(): expects the array size to be even\n");
    return NULL;
  }

  if (size >= 0x7fffffffffffffff)
  {
    printf("create(): reserved values\n");
    return NULL;
  }

  // allocates memory for the positions array:

  size_t const bytes = size * sizeof(double);
  double* positions = malloc(bytes);
  if (positions == NULL)
  {
    return NULL;
  }

  // initializes the positions array with zeros:

  zeros(positions, size);

  return positions;
}


// destroys the positions array (frees it from memory)
double* destroy (double* positions)
{
  if (positions == NULL)
  {
    return positions;
  }

  free(positions);
  positions = NULL;
  return positions;
}


/*

Algorithms and Complexity					August 10, 2023

source: util.c
author: @misael-diaz

Synopsis:
Implements (some) utilities that support the solution of the closest pair problem.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
