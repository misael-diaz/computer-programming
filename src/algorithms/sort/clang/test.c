#include <stdbool.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>

#define NUMEL 256
#define SIZE ( NUMEL * sizeof(uint64_t) )
#define EXEC_SANE_CHECKS true
#define WARNINGS false

void test_search();
void test_isearch();

int main ()
{
  test_search();
  test_isearch();
  return 0;
}


// returns true if the array is sorted (in ascending order), false otherwise
bool sorted (const uint64_t* x)
{
  bool sorted = true;
  for (size_t i = 0; i != (NUMEL - 1); ++i)
  {
    if (x[i] > x[i + 1])
    {
      sorted = false;
      return sorted;
    }
  }
  return sorted;
}


// int64_t search(const uint64_t* x, int64_t b, int64_t e, uint64_t const target)
//
// Synopsis:
// Recursive implementation of the binary search algorithm.
// Returns the position of the target if it is present in the asymmetric range [b, e),
// otherwise returns the insertion position -(pos + 1).
//
// Inputs:
// x		the array
// b		beginning of the array
// e		end of the array (non-inclusive)
// target	the sought value
//
// Output:
// pos		if found the position of the target, otherwise the insertion position


int64_t search (const uint64_t* x, int64_t b, int64_t e, uint64_t const target)
{
  if (EXEC_SANE_CHECKS)
  {
    if ( !sorted(x) )
    {
      if (WARNINGS)
      {
	printf("search(): WARNING unsorted array\n");
      }
      return ( (int64_t) 0x8000000000000000);
    }
  }

  uint64_t const tgt = target;
  while (b < e)
  {
    int64_t const m = b + (e - b) / 2;
    if (x[m] == tgt)
    {
      int64_t const pos = m;
      return pos;
    }
    else if (x[m] < tgt)
    {
      b = (m + 1);
      return search(x, b, e, tgt);
    }
    else
    {
      e = m;
      return search(x, b, e, tgt);
    }
  }
  int64_t const pos = -(b + 1);
  return pos;
}


// int64_t isearch(const uint64_t* x, int64_t b, int64_t e, uint64_t const target)
//
// Synopsis:
// Iterative implementation of the binary search algorithm.
// Returns the position of the target if it is present in the asymmetric range [b, e),
// otherwise returns the insertion position -(pos + 1).
//
// Inputs:
// x		the array
// b		beginning of the array
// e		end of the array (non-inclusive)
// target	the sought value
//
// Output:
// pos		if found the position of the target, otherwise the insertion position


int64_t isearch (const uint64_t* x, int64_t b, int64_t e, uint64_t const target)
{
  if (EXEC_SANE_CHECKS)
  {
    if ( !sorted(x) )
    {
      if (WARNINGS)
      {
	printf("search(): WARNING unsorted array\n");
      }
      return ( (int64_t) 0x8000000000000000);
    }
  }

  uint64_t const tgt = target;
  while (b < e)
  {
    int64_t const m = b + (e - b) / 2;
    if (x[m] == tgt)
    {
      int64_t const pos = m;
      return pos;
    }
    else if (x[m] < tgt)
    {
      b = (m + 1);
    }
    else
    {
      e = m;
    }
  }
  int64_t const pos = -(b + 1);
  return pos;
}


void iota (uint64_t* x, uint64_t const start, uint64_t const stride)
{
  for (size_t i = 0; i != NUMEL; ++i)
  {
    x[i] = start + stride * i;
  }
}


uint64_t* create ()
{
  if (NUMEL == 0x7fffffffffffffff)
  {
    printf("create(): reserved value\n");
    return NULL;
  }

  uint64_t* x = malloc(SIZE);
  if (x == NULL)
  {
    return NULL;
  }

  uint64_t const start = 2;
  uint64_t const stride = 2;
  iota(x, start, stride);

  return x;
}


uint64_t* destroy (uint64_t* x)
{
  if (x == NULL)
  {
    return x;
  }

  free(x);
  x = NULL;
  return x;
}


void test_search ()
{
  uint64_t* x = create();
  if (x == NULL)
  {
    return;
  }

  size_t fails = 0;
  for (int64_t i = 0; i != NUMEL; ++i)
  {
    uint64_t const tgt = x[i];
    int64_t const pos = search(x, 0, NUMEL, tgt);
    if (pos != i)
    {
      ++fails;
    }
  }

  printf("test-search[0]: ");
  if (fails != 0)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  for (int64_t i = 0; i != NUMEL; ++i)
  {
    uint64_t const tgt = (2 * i + 1);
    int64_t const pos = search(x, 0, NUMEL, tgt);
    if ( -(pos + 1) != i )
    {
      ++fails;
    }
  }
 
  printf("test-search[1]: ");
  if (fails != 0)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  if (EXEC_SANE_CHECKS)
  {
    iota(x, -1, -2);

    size_t fails = 0;
    for (int64_t i = 0; i != NUMEL; ++i)
    {
      uint64_t const tgt = i;
      int64_t const pos = isearch(x, 0, NUMEL, tgt);
      if (pos != ( (int64_t) 0x8000000000000000 ) )
      {
	++fails;
      }
    }

    printf("test-search[2]: ");
    if (fails != 0)
    {
      printf("FAIL\n");
    }
    else
    {
      printf("PASS\n");
    }
  }

  x = destroy(x);
}


void test_isearch ()
{
  uint64_t* x = create();
  if (x == NULL)
  {
    return;
  }

  size_t fails = 0;
  for (int64_t i = 0; i != NUMEL; ++i)
  {
    uint64_t const tgt = x[i];
    int64_t const pos = isearch(x, 0, NUMEL, tgt);
    if (pos != i)
    {
      ++fails;
    }
  }

  printf("test-isearch[0]: ");
  if (fails != 0)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  for (int64_t i = 0; i != NUMEL; ++i)
  {
    uint64_t const tgt = (2 * i + 1);
    int64_t const pos = isearch(x, 0, NUMEL, tgt);
    if ( -(pos + 1) != i )
    {
      ++fails;
    }
  }
 
  printf("test-isearch[1]: ");
  if (fails != 0)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  if (EXEC_SANE_CHECKS)
  {
    iota(x, -1, -2);

    size_t fails = 0;
    for (int64_t i = 0; i != NUMEL; ++i)
    {
      uint64_t const tgt = i;
      int64_t const pos = isearch(x, 0, NUMEL, tgt);
      if (pos != ( (int64_t) 0x8000000000000000 ) )
      {
	++fails;
      }
    }

    printf("test-isearch[2]: ");
    if (fails != 0)
    {
      printf("FAIL\n");
    }
    else
    {
      printf("PASS\n");
    }
  }

  x = destroy(x);
}


/*

Algorithms							August 04, 2023

source: test.c
author: @misael-diaz

Synopsis:
Implements (some) classic algorithms.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
[1] JJ McConnell, Analysis of Algorithms, second edition.

*/
