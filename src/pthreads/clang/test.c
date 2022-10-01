/*
 * Computational Methods                             September 30, 2022
 * ICI 70320
 * Prof. M Diaz-Maldonado
 *
 * source: test.c
 *
 * Synopsis:
 * Uses POSIX threads to perform basic matrix operations concurrently.
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by
 *     Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] POSIX Threads tutorial https://hpc-tutorials.llnl.gov/posix
 * [3] code is based on https://stackoverflow.com/a/15606507
 *
 */


#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>


#define SIZE 0x00001000
#define NUM_THREADS 4


// defines a type for the parallel workspace
typedef struct
{
	size_t tid;	// thread ID
	size_t b;	// begin
	size_t e;	// end (exclusive)
	double *x;	// array x
	double *y;	// array y
	double *z;	// array z
} data_t;


// prototypes
void pgreet   ();	// hello world
void pvectorSum ();	// vector sum
void pmatrixMult ();	// matrix multiplication


int main ()
{
	pgreet ();	// each thread greets on the console
	pvectorSum ();	// each thread sums a slice of the vectors
	pmatrixMult ();	// each thread operates on contiguous rows
	return 0;
}


void* greet (void *v)
// defines greeting from thread
{
	// asserts the type of the argument
	data_t *p = v;
	// gets the thread ID
	size_t tid = (p -> tid);
	// prints greeting on the console
	printf("hello world from thread %lu\n", tid);
	// returns NULL to comply with the pthreads prototype
	return NULL;
}


void* vectorSum (void *v)
// vector sum example, each thread operates on a contiguous vector slice
{
	// asserts the type of the argument
	data_t *p = v;
	// gets the thread ID
	size_t tid = (p -> tid);
	// gets the assigned (asymmetric) data range
	size_t b = (p -> b), e = (p -> e);
	// gets iterators for the vectors
	double *x = (p -> x), *y = (p -> y), *z = (p -> z);

	// reports the assigned slice
	printf("thread: %4lu begin: %8lu end: %8lu\n", tid, b, e);

	// operates the vector sum on the assigned slice
	for (size_t i = b; i != e; ++i)
		z[i] = (x[i] + y[i]);

	return NULL;
}


void* matrixMult (void *v)
// matrix multiplication example, each thread operates on contiguous rows
{
	// asserts the type of the argument
	data_t *p = v;
	// gets the thread ID
	size_t tid = (p -> tid);
	// gets the assigned (asymmetric) data range
	size_t b = (p -> b), e = (p -> e);
	// gets iterators for the vectors
	double *x = (p -> x), *y = (p -> y), *z = (p -> z);

	// reports the assigned slice
	printf("thread: %4lu begin: %8lu end: %8lu\n", tid, b, e);

	// performs the matrix multiplication of the symmetric matrices
	for (size_t i = b; i != e; ++i)
	{
		size_t const N = SIZE;
		for (size_t j = 0; j != N; ++j)
		{
			z[i*N + j] = 0.0;
			/* uses y[j][k] in lieu of y[k][j] for speed */
			for (size_t k = 0; k != N; ++k)
				z[i*N + j] += (x[i*N + k] * y[j*N + k]);
		}
	}

	return NULL;
}


void pgreet()
// greeting example, each thread greets on the console
{
	// allocates the threads array
	pthread_t threads[NUM_THREADS];
	// allocates data for the parallel environment
	data_t data[NUM_THREADS];
	// creates thread iterator
	pthread_t *thread = threads;
	// creates data iterator
	data_t *p = data;

	/* starts multi-threaded execution */

	for (size_t i = 0; i != NUM_THREADS; ++i)
	{
		// defines the thread ID
		p -> tid = i;
		// creates universal pointer for the data of current thread
		void *v = p;
		// creates thread with default options and puts it to work
		pthread_create (thread, NULL, greet, v);
		// gets next thread
		++thread;
		// gets the data of the next thread
		++p;
	}

	/* ends multi-threaded execution */

	// waits for each tread to complete its task
	for (size_t i = 0; i != NUM_THREADS; ++i)
		pthread_join (threads[i], 0);
}


void pvectorSum()
// uses multiple threads to sum a pair of vectors
{

	/* initialization */


	// allocates vectors on the stack
	double x[SIZE], y[SIZE], z[SIZE];

	// initializes vectors
	for (size_t i = 0; i != SIZE; ++i)
		x[i] = 1.0;

	for (size_t i = 0; i != SIZE; ++i)
		y[i] = 1.0;

	for (size_t i = 0; i != SIZE; ++i)
		z[i] = 0.0;


	// allocates the threads array
	pthread_t threads[NUM_THREADS];
	// allocates data for the parallel environment
	data_t data[NUM_THREADS];
	// creates thread iterator
	pthread_t *thread = threads;
	// creates data iterator
	data_t *p = data;


	/* multi-threaded execution */


	for (size_t i = 0; i != NUM_THREADS; ++i)
	{
		/* distributes work evenly among threads */

		size_t chunk = (SIZE / NUM_THREADS);	// sets chunk size
		size_t b = i * chunk;			// sets begin
		size_t e = (i + 1) * chunk;		// sets end

		// defines the thread ID
		p -> tid = i;
		// defines the asymmetric range [b, e) assigned to thread
		p -> b = b;
		p -> e = e;
		// references the (shared) vectors
		p -> x = x;
		p -> y = y;
		p -> z = z;
		// creates universal pointer for the data of current thread
		void *v = p;
		// creates thread with default options and puts it to work
		pthread_create (thread, NULL, vectorSum, v);
		// gets next thread
		++thread;
		// gets the data of the next thread
		++p;
	}


	/* ends multi-threaded execution */


	// waits for each tread to complete its task
	for (size_t i = 0; i != NUM_THREADS; ++i)
		pthread_join (threads[i], 0);


	/* serial */


	double diff = 0;
	// computes differences
	for (size_t i = 0; i != SIZE; ++i)
		diff += (z[i] - 2.0);


	// reports test outcome on the console
	printf("vector-sum-test[0]: ");
	if (diff != 0.0)
		printf("FAIL\n");
	else
		printf("pass\n");
}


void pmatrixMult()
// uses multiple threads to multiply a pair of symmetric square matrices
{

	/* initialization */


	// defines the number of rows (columns)
	size_t const size = SIZE;
	// defines the total number of elements of each matrix
	size_t const N = (size * size);
	// allocates contiguous space on the heap for each matrix
	double *x = (double*) malloc ( N * sizeof(double) );
	if (x == NULL)
	{
		char msg [] = "failed to allocate matrix x";
		printf("%s\n", msg);
		return;
	}

	double *y = (double*) malloc ( N * sizeof(double) );
	if (y == NULL)
	{
		char msg [] = "failed to allocate matrix y";
		printf("%s\n", msg);
		// free allocated matrix to avoid memory leaks
		free (x);
		x = NULL;
		return;
	}

	double *z = (double*) malloc ( N * sizeof(double) );
	if (z == NULL)
	{
		char msg [] = "failed to allocate matrix z";
		printf("%s\n", msg);
		// free allocated matrices to avoid memory leaks
		free (x);
		free (y);
		x = NULL;
		y = NULL;
		return;
	}


	// initializes matrices
	for (size_t i = 0; i != N; ++i)
		x[i] = 1.0;

	for (size_t i = 0; i != N; ++i)
		y[i] = 1.0;

	for (size_t i = 0; i != N; ++i)
		z[i] = 0.0;


	// allocates the threads array
	pthread_t threads[NUM_THREADS];
	// allocates data for the parallel environment
	data_t data[NUM_THREADS];
	// creates thread iterator
	pthread_t *thread = threads;
	// creates data iterator
	data_t *p = data;


	/* multi-threaded execution */


	for (size_t i = 0; i != NUM_THREADS; ++i)
	{
		/* distributes work evenly among threads */

		size_t chunk = (SIZE / NUM_THREADS);	// sets chunk size
		size_t b = i * chunk;			// sets begin
		size_t e = (i + 1) * chunk;		// sets end

		// defines the thread ID
		p -> tid = i;
		// defines the asymmetric range [b, e) assigned to thread
		p -> b = b;
		p -> e = e;
		// references the (shared) matrices
		p -> x = x;
		p -> y = y;
		p -> z = z;
		// creates universal pointer for the data of current thread
		void *v = p;
		// creates thread with default options and puts it to work
		pthread_create (thread, NULL, matrixMult, v);
		// gets next thread
		++thread;
		// gets the data of the next thread
		++p;
	}


	/* ends multi-threaded execution */


	// waits for each tread to complete its task
	for (size_t i = 0; i != NUM_THREADS; ++i)
		pthread_join (threads[i], 0);


	/* serial */


	double diff = 0;
	double prod = ( (double) SIZE );
	// computes differences
	for (size_t i = 0; i != N; ++i)
		diff += (z[i] - prod);


	// reports test outcome on the console
	printf("matrix-mult-test[0]: ");
	if (diff != 0.0)
		printf("FAIL\n");
	else
		printf("pass\n");


	// frees allocated resources from memory
	free (x);
	free (y);
	free (z);
	x = NULL;
	y = NULL;
	z = NULL;
}
