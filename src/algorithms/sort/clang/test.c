#include <stdbool.h>	// uses bool(ean) type
#include <stdlib.h>	// uses dynamic memory (de)allocation via malloc() and free()
#include <stdint.h>	// uses unsigned integers of 64-bits
#include <stdio.h>	// does logging to the console
#include <math.h>	// uses the rand() Pseudo Random Number Generator PRNG
#include <time.h>	// uses time() to seed the PRNG

#define RUNS 13
#define REPS 4096
#define iSIZE 16
#define SIZE 0x0000000000010000
#define EXEC_SANE_CHECKS false
#define WARNINGS false

void test_search();
void test_isearch();
void test_isort();
void complexity_isort();
void complexity_msort();

int main ()
{
  test_search();
  test_isearch();
  test_isort();
  complexity_isort();
  complexity_msort();
  return 0;
}


// returns true if the array is sorted (in ascending order), false otherwise
bool sorted (const uint64_t* x, int64_t const b, int64_t const e)
{
  bool sorted = true;
  int64_t const numel = (e - b);
  if ( (numel == 0) || (numel == 1) )
  {
    return sorted;
  }

  for (int64_t i = b; i != (e - 1); ++i)
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
    if ( !sorted(x, b, e) )
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
    if ( !sorted(x, b, e) )
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


// shifts `numel' elements to the right from `inloc' position
void shift (uint64_t* x, int64_t const numel, int64_t const inloc)
{
  for (int64_t j = 0; j != numel; ++j)
  {
    int64_t const k = inloc + ( numel - (j + 1) );
    x[k + 1] = x[k];
  }
}


// implements insertion sort (optimizations: gcc can vectorize the innermost for-loop)
void isort (uint64_t* x, size_t const size)
{
  // loop-invariant: elements in the asymmetric range [0, i) are sorted
  for (size_t i = 1; i != size; ++i)
  {
    int64_t const idx = i;				// casts index explicitly
    uint64_t const inelem = x[i];			// gets the insertion element
    int64_t const l = isearch(x, 0, idx, inelem);	// gets the target location
    int64_t const inloc = (l < 0)? -(l + 1) : (l + 1);	// sets the insertion location
    int64_t const numel = (idx - inloc);		// gets the #elements to shift
    shift(x, numel, inloc);				// shifts to make inloc available
    x[inloc] = inelem;					// inserts at designated location
  }
}


// copies numel elements from source `src' into destination `dst' array
void copy (const uint64_t* restrict src, uint64_t* restrict dst, size_t const numel)
{
  for (size_t i = 0; i != numel; ++i)
  {
    dst[i] = src[i];
  }
}


// implements the direct solution, interchanges out-of-order neighboring elements
void direct (uint64_t* x)
{
  if (x[1] < x[0])
  {
    uint64_t const min = x[1];
    x[1] = x[0];
    x[0] = min;
  }
}


// gets the insertion location of the element `elem' in the left partition
int64_t ginloc (const uint64_t* x, int64_t const b, int64_t const e, uint64_t const elem)
{
  int64_t const loc = isearch(x, b, e, elem);
  return ( (loc < 0)? -(loc + 1) : (loc + 1) );
}


// int64_t traverse(uint64_t* x, int64_t b, int64_t e, int64_t parent)
//
// Synopsis:
// Finds the array slice delimited by the asymmetric range [`parent', `last' + 1) in the
// right partition so that when merged with the left partition it is in order. The arange
// [b, e) denotes the extent of the whole partition, which includes both the left and
// right partitions.
//
// Inputs:
// x		the array of elements to be sorted
// b		beginning of the partition
// e		end of the partition
// parent	beginning of the (sorted) array slice [`parent', `last' + 1)
//
// Output:
// (last + 1)	the (non-inclusive) end of the array slice
//
// Notes:
// At this point the left and right partitions are sorted by themselves but their
// combination might not be. For example, if we are lucky just joining the left and
// right partitions results in an ordered combined partition. Or perhaps the elements
// in the right partition are all less than those in the left so that simply exchanging
// their positions results in an ordered combine partition. And more generally, there
// might be array slices in the right partition that need to be inserted in the left
// to obtain a combined sorted partition. This method addresses the more general case.
//
// By calling this method is in succession one can find all the slices (in the right
// partition) that need to be merged into the left partition. However, the caller must
// make sure that the element `parent' is less than some intermediate element in the
// left partition, as done by combine1().
//
// The logic used to implement this method is straightforward: all the elements in the
// array slice [`parent', `last' + 1) have the same insertion location. Note that we
// are not inserting any elements while this method executes and so the statement holds
// true.
//
// Nomenclature:
// We use the terms `tree', `parent', and `leaf' because we regard the sorted partitions
// as binary trees. I believe that the use of these terms make it easier to understand
// how this method does its work.


int64_t traverse (const uint64_t* x, int64_t const b, int64_t const e, int64_t parent)
{
  int64_t const size = (e - b);
  int64_t const beginLeft = b;
  int64_t const endLeft = b + (size / 2);
  int64_t const beginRight = b + (size / 2);
  int64_t const endRight = e;

  int64_t beg = beginRight;
  int64_t end = endRight;
  int64_t numel = (end - beg) / 2;
  int64_t last = 0x7fffffffffffffff;
  int64_t const inloc = ginloc(x, beginLeft, endLeft, x[parent]);
  // implements a binary-search like algorithm for traversing the tree
  while (numel != 0)
  {
    bool const swappable = ( ginloc(x, beginLeft, endLeft, x[parent]) == inloc );
    // if true, we consider the larger child in the next step, otherwise the smaller child
    if (swappable)
    {
      beg = parent;
      last = parent;
    }
    else
    {
      end = parent;
    }
    numel = (end - beg) / 2;
    parent = beg + numel;
  }

  // considers the leaf node

  bool const swappable = ( ginloc(x, beginLeft, endLeft, x[parent]) == inloc );
  if (swappable)
  {
    last = parent;
  }

  // if all else fails (last + 1) would be equal to the reserved value 0x8000000000000000

  return (last + 1);
}


// void combine(uint64_t* x, size_t size, size_t b, size_t e)
//
// Synopsis:
// Optimal implementation of combine().
// Combines the left and right partitions in O(N) operations, where `N' is the number
// of elements in the partition.
//
// Inputs:
// x		array to be sorted (in ascending order)
// size		total number of elements in `x' to be sorted (same value given to msort())
// b		beginning of the partition
// e		end of the partition
//
// Output:
// x		the sorted partition [b, e)


void combine (uint64_t* x, size_t const size, size_t const b, size_t const e)
{
  size_t const numel = (e - b);
  size_t const beginLeft = b;
  size_t const endLeft = b + (numel / 2);
  size_t const beginRight = b + (numel / 2);
  size_t const endRight = e;

  size_t n = 0;
  uint64_t* dst = (x + size);
  size_t iterLeft = beginLeft;
  size_t iterRight = beginRight;
  // copies elements in order into the temporary until either partition is depleted
  while (iterLeft != endLeft && iterRight != endRight)
  {
    if (x[iterLeft] < x[iterRight])
    {
      dst[n] = x[iterLeft];
      ++iterLeft;
    }
    else
    {
      dst[n] = x[iterRight];
      ++iterRight;
    }
    ++n;
  }

  // updates the number of elements written to the temporary

  dst += n;

  // copies whatever that remains from the left partition (could be empty)

  size_t beg = iterLeft;
  size_t end = endLeft;
  const uint64_t* src = (x + beg);
  for (size_t i = 0; i != (end - beg); ++i)
  {
    dst[i] = src[i];
  }

  // updates the number of elements written to the temporary

  dst += (end - beg);

  // copies whatever that remains from the right partition (could be empty)

  beg = iterRight;
  end = endRight;
  src = (x + beg);
  copy(src, dst, end - beg);

  // commits the combined partition into the partition [b, e] in the array `x'

  dst = (x + b);
  src = (x + size);
  copy(src, dst, e - b);
}


// void combine1(uint64_t* x, size_t size, size_t b, size_t e)
//
// Synopsis:
// Sub-optimal implementation of combine().
// Combines the left and right partitions into a combined sorted partition in [b, e).
//
// Notes:
// This method is sub-optimal because we can combine the partitions in O(N) operations,
// as done by the optimal combine() method. Even though the method is sub-optimal, it is
// committed to the repository because it is of academic importance.
//
// Inputs:
// x		array to be sorted (in ascending order)
// size		total number of elements in `x' to be sorted (same value given to msort())
// b		beginning of the partition
// e		end of the partition
//
// Output:
// x		the sorted partition [b, e)


void combine1 (uint64_t* x, size_t const size, size_t const b, size_t const e)
{
  // we use this as a placeholder for storing intermediate values
  uint64_t* tmp = (x + size);

  // gets the asymmetric ranges that delimit the left and right partitions:

  int64_t const beginLeft = b;
  int64_t const endLeft = b + ( (e - b) / 2 );
  int64_t const beginRight = b + ( (e - b) / 2 );
  int64_t const endRight = e;

  // merges the left and right partitions into the array temporary tmp:

  int64_t numel = 0;
  int64_t beginSliceLeft = beginLeft;
  int64_t endSliceLeft = beginSliceLeft;
  int64_t beginSliceRight = beginRight;
  int64_t endSliceRight = beginSliceRight;
  // combines the left and right partitions into the array temporary
  while ( (beginSliceRight != endRight) && (x[beginSliceRight] < x[endLeft - 1]) )
  {
    // gets the array slice [b, e) in the left partition for copying into the temporary

    endSliceLeft = ginloc(x, beginLeft, endLeft, x[beginSliceRight]);

    // copies the sorted slice [b, e) in the left partiton into the array temporary

    uint64_t* dst = (tmp + numel);
    const uint64_t* src = (x + beginSliceLeft);
    copy(src, dst, endSliceLeft - beginSliceLeft);

    // updates the number of (ordered) elements written to the array temporary

    numel += (endSliceLeft - beginSliceLeft);

    // finds the array slice [b, e) that needs to be merged into the left partition

    endSliceRight = traverse(x, b, e, beginSliceRight);

    // copies the array slice into the temporary placeholder

    dst = (tmp + numel);
    src = (x + beginSliceRight);
    copy(src, dst, endSliceRight - beginSliceRight);

    // updates the number of (ordered) elements written to the array temporary

    numel += (endSliceRight - beginSliceRight);

    // updates the beginning of the array slices in the left and right partitions

    beginSliceLeft = endSliceLeft;
    beginSliceRight = endSliceRight;
  }

  // note that at this point either partition (left or right) could have no more elements

  // copies into the temporary whatever remains from the left partition

  endSliceLeft = endLeft;
  uint64_t* dst = (tmp + numel);
  const uint64_t* src = (x + beginSliceLeft);
  copy(src, dst, endSliceLeft - beginSliceLeft);

  // updates the number of (ordered) elements written to the array temporary

  numel += (endSliceLeft - beginSliceLeft);

  // copies into the temporary whatever remains from the right partition

  dst = (tmp + numel);
  endSliceRight = endRight;
  src = (x + beginSliceRight);
  copy(src, dst, endSliceRight - beginSliceRight);

  // commits the combined sorted partition into the partition [b, e) in `x'

  src = tmp;
  dst = (x + b);
  copy(src, dst, e - b);
}


// void msort (uint64_t* x, size_t size, size_t b, size_t e)
//
// Synopsis:
// Recursive implementation of the merge sort algorithm. The implementation does it work
// on the partition delimited by the asymmetric range [b, e). The users, in general, would
// supply [0, size) so that msort() sorts all the elements in the array `x'; but, they may
// as well use msort() to sort a slice of the array.
//
// Note:
// The implementation expects an array of twice the number of elements to be sorted.
// The elements in [0, size) contain the values to be sorted and the rest [size, 2 * size)
// is used as a temporary placeholder by combine().
//
// Inputs:
// x		array to be sorted (in ascending order)
// size		the slice of the array that contains the values to be sorted
// b		beginning of the asymmetric range [b, e)
// e		end of the asymmetric range range [b, e)
//
// Ouput:
// x		the sorted array


void msort (uint64_t* x, size_t const size, size_t const b, size_t const e)
{
  size_t const numel = (e - b);
  if (numel == 2)
  {
    // applies the direct solution (just swaps out-of-order pairs):
    size_t const offset = b;
    uint64_t* y = (x + offset);
    direct(y);
  }
  else
  {
    // divides into left and right partitions:
    size_t const beginLeft = b;
    size_t const endLeft = b + (numel / 2);
    size_t const beginRight = b + (numel / 2);
    size_t const endRight = e;

    // recurses until the direct solution becomes available:
    msort(x, size, beginLeft, endLeft);
    msort(x, size, beginRight, endRight);

    // combines the left and right partitions to obtain a combined ordered partition:
    combine(x, size, b, e);
  }
}


// iterative implementation of the merge sort algorithm.
void imsort (uint64_t* x, size_t const size)
{
  // orders pairs (this is what the recursive algorithm does last)
  for (size_t i = 0; i != size; i += 2)
  {
    size_t const offset = i;
    uint64_t* y = (x + offset);
    direct(y);
  }

  // combines partitions from the smallest to the largest (this is what the recursive
  // implementation does from the deepest to the lowest level of recursion)
  for (size_t stride = 4; stride != size; stride *= 2)
  {
    for (size_t i = 0; i != size; i += stride)
    {
      size_t const b = i;
      size_t const e = (i + stride);
      combine(x, size, b, e);
    }
  }

  // combines the final partition [b, e) to obtain a sorted array `x'
  combine(x, size, 0, size);
}


void iota (uint64_t* x, size_t const size, uint64_t const start, uint64_t const stride)
{
  for (size_t i = 0; i != size; ++i)
  {
    x[i] = start + stride * i;
  }
}


// fills the array with pseudo-random numbers
void prns (uint64_t* x, size_t const size)
{
  for (size_t i = 0; i != size; ++i)
  {
    x[i] = rand();
  }
}


// initializes the array `x'
uint64_t* create (size_t const size)
{
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

  size_t const bytes = size * sizeof(uint64_t);
  uint64_t* x = malloc(bytes);
  if (x == NULL)
  {
    return NULL;
  }

  uint64_t const start = 2;
  uint64_t const stride = 2;
  iota(x, size, start, stride);

  return x;
}


// destroys the array `x' (frees it from memory)
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


// gets the elapsed-time from the time difference in nanoseconds
double getElapsedTime (const struct timespec* b, const struct timespec* e)
{
  double begin = ( (double) (b -> tv_nsec) ) + 1.0e9 * ( (double) (b -> tv_sec) );
  double end   = ( (double) (e -> tv_nsec) ) + 1.0e9 * ( (double) (e -> tv_sec) );
  return (end - begin);
}


// exports the average runtime of isort() as a function of the input size
void complexity_isort ()
{
  struct timespec* begin = malloc( sizeof(struct timespec) );
  if (begin == NULL)
  {
    printf("complexity(): failed to allocate memory for the timespec struct\n");
    return;
  }

  struct timespec* end = malloc( sizeof(struct timespec) );
  if (end == NULL)
  {
    free(begin);
    begin = NULL;
    printf("complexity(): failed to allocate memory for the timespec struct\n");
    return;
  }

  bool failed = false;
  srand( time(NULL) );
  size_t size = iSIZE;
  double etimes[REPS];
  for (size_t run = 0; run != RUNS; ++run)
  {
    uint64_t* x = create(size);
    if (x == NULL)
    {
      free(begin);
      free(end);
      begin = NULL;
      end = NULL;
      printf("complexity(): failed to allocate memory for the array `x'\n");
      return;
    }

    double etime = 0;
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      prns(x, size);

      clock_gettime(CLOCK_MONOTONIC_RAW, begin);
      isort(x, size);
      clock_gettime(CLOCK_MONOTONIC_RAW, end);

      if ( !sorted(x, 0, size) )
      {
	failed = true;
	printf("complexity(): isort() implementation failed\n");
	break;
      }

      etime += getElapsedTime(begin, end);
    }

    if (failed)
    {
      x = destroy(x);
      break;
    }

    etimes[run] = etime / ( (double) REPS );

    size *= 2;
    x = destroy(x);
  }

  printf("test-isort[2]: ");
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
    free(begin);
    free(end);
    begin = NULL;
    end = NULL;
    return;
  }

  const char fname[] = "complexity.txt";
  FILE* file = fopen(fname, "w");
  if (file == NULL)
  {
    free(begin);
    free(end);
    begin = NULL;
    end = NULL;
    printf("complexity(): IO ERROR with file %s\n", fname);
    return;
  }

  size = iSIZE;
  for (size_t run = 0; run != RUNS; ++run)
  {
    fprintf(file, "%16lu %.16e\n", size, etimes[run]);
    size *= 2;
  }

  free(begin);
  free(end);
  begin = NULL;
  end = NULL;
  fclose(file);
}


// exports the average runtime of imsort() as a function of the input size
void complexity_msort ()
{
  struct timespec* begin = malloc( sizeof(struct timespec) );
  if (begin == NULL)
  {
    printf("complexity-msort(): failed to allocate memory for the timespec struct\n");
    return;
  }

  struct timespec* end = malloc( sizeof(struct timespec) );
  if (end == NULL)
  {
    free(begin);
    begin = NULL;
    printf("complexity-msort(): failed to allocate memory for the timespec struct\n");
    return;
  }

  srand( time(NULL) );
  size_t size = iSIZE;
  double etimes[REPS];
  for (size_t run = 0; run != RUNS; ++run)
  {
    size_t const xsize = (2 * size);
    uint64_t* x = create(xsize);
    if (x == NULL)
    {
      free(begin);
      free(end);
      begin = NULL;
      end = NULL;
      printf("complexity(): failed to allocate memory for the array `x'\n");
      return;
    }

    double etime = 0;
    for (size_t rep = 0; rep != REPS; ++rep)
    {
      prns(x, size);

      clock_gettime(CLOCK_MONOTONIC_RAW, begin);
      imsort(x, size);
      clock_gettime(CLOCK_MONOTONIC_RAW, end);

      if ( !sorted(x, 0, size) )
      {
	printf("complexity-msort(): msort() implementation failed\n");
	x = destroy(x);
	return;
      }

      etime += getElapsedTime(begin, end);
    }

    etimes[run] = etime / ( (double) REPS );

    size *= 2;
    x = destroy(x);
  }

  const char fname[] = "complexity-msort.txt";
  FILE* file = fopen(fname, "w");
  if (file == NULL)
  {
    free(begin);
    free(end);
    begin = NULL;
    end = NULL;
    printf("complexity-msort(): IO ERROR with file %s\n", fname);
    return;
  }

  size = iSIZE;
  for (size_t run = 0; run != RUNS; ++run)
  {
    fprintf(file, "%16lu %.16e\n", size, etimes[run]);
    size *= 2;
  }

  free(begin);
  free(end);
  begin = NULL;
  end = NULL;
  fclose(file);
}


void test_search ()
{
  size_t const size = 256;
  uint64_t* x = create(size);
  if (x == NULL)
  {
    return;
  }

  size_t fails = 0;
  int64_t const numel = size;
  for (int64_t i = 0; i != numel; ++i)
  {
    uint64_t const tgt = x[i];
    int64_t const pos = search(x, 0, numel, tgt);
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

  for (int64_t i = 0; i != numel; ++i)
  {
    uint64_t const tgt = (2 * i + 1);
    int64_t const pos = search(x, 0, numel, tgt);
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
    iota(x, size, -1, -2);

    size_t fails = 0;
    for (int64_t i = 0; i != numel; ++i)
    {
      uint64_t const tgt = i;
      int64_t const pos = isearch(x, 0, numel, tgt);
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
  size_t const size = 256;
  uint64_t* x = create(size);
  if (x == NULL)
  {
    return;
  }

  size_t fails = 0;
  int64_t const numel = size;
  for (int64_t i = 0; i != numel; ++i)
  {
    uint64_t const tgt = x[i];
    int64_t const pos = isearch(x, 0, numel, tgt);
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

  for (int64_t i = 0; i != numel; ++i)
  {
    uint64_t const tgt = (2 * i + 1);
    int64_t const pos = isearch(x, 0, numel, tgt);
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
    iota(x, size, -1, -2);

    size_t fails = 0;
    int64_t const numel = size;
    for (int64_t i = 0; i != numel; ++i)
    {
      uint64_t const tgt = i;
      int64_t const pos = isearch(x, 0, numel, tgt);
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


void test_isort ()
{
  bool failed = false;
  for (size_t size = 16; size != SIZE; size *= 2)
  {
    uint64_t* x = create(size);
    if (x == NULL)
    {
      return;
    }
    isort(x, size);
    if ( !sorted(x, 0, size) )
    {
      failed = true;
      x = destroy(x);
      break;
    }
    x = destroy(x);
  }

  printf("test-isort[0]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }

  failed = false;
  for (size_t size = 16; size != SIZE; size *= 2)
  {
    uint64_t* x = create(size);
    if (x == NULL)
    {
      return;
    }
    srand( time(NULL) );
    prns(x, size);
    isort(x, size);
    if ( !sorted(x, 0, size) )
    {
      failed = true;
      x = destroy(x);
      break;
    }
    x = destroy(x);
  }

  printf("test-isort[1]: ");
  if (failed)
  {
    printf("FAIL\n");
  }
  else
  {
    printf("PASS\n");
  }
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
