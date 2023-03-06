/*
 * Closest Pair Lab						February 22, 2023
 *
 * source: benchmark.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Benchmarks the implementations of Closest Pair finding algorithms.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] system-clock: Linux Programmer's Manual: `man clock_getres()'
 * [3] NTP: https://www.linux.com/topic/networking/keep-accurate-time-linux-ntp/
 * [4] https://stackoverflow.com/questions/68804469/subtract-two-timespec-objects-find-difference-in-time-or-duration/68804612#68804612
 *
 */

#include "../include/benchmark.h"

#define RUNS 14
#define REPS 256
#define SIZE 2

extern algorithm_namespace const algorithm;
extern util_numeric_namespace const numeric;
extern util_workspace_namespace const workspace;

double timeRecursive (Workspace*, ClosestPairInfo*);
double timeBruteForce (Workspace*, ClosestPairInfo*);
void export (const char*, double (*timer) (Workspace*, ClosestPairInfo*) );

int main ()
{

//  Note: uncomment if you wish to time the implementation of the brute force algorithm
//  char filename [] = "timeBruteForce.dat";
//  export(filename, timeBruteForce);


    // times the 1D Divide And Conquer Algorithm
    char filename [] = "timeDivideAndConquer1D.dat";
    export(filename, timeRecursive);

    return 0;
}


double getElapsedTime (const struct timespec* startTime, const struct timespec* endTime)
// gets the elapsed-time of the time difference (`endTime' - `startTime') in nanoseconds
{

    double start = ( (double) (startTime -> tv_nsec) ) +
	   1.0e9 * ( (double) (startTime -> tv_sec) );

    double end = ( (double) (endTime -> tv_nsec) ) +
	 1.0e9 * ( (double) (endTime -> tv_sec) );

    return (end - start);
}


int compare (const int size, const Dataset* dataset, int i, int j)
// Returns 0 if the points are equal, 1 if the `i'-th is greater than the `j'-th point,
// and -1 if the `i'-th is less than the `j'-the point.
{
    const double* coords = dataset -> coords;
    const double* x = coords;
    const double* y = (coords + size);

    int comp = 0;
    if (x[i] != x[j])
    {
	comp = numeric.compare(x[i], x[j]);
    }
    else
    {
	comp = numeric.compare(y[i], y[j]);
    }

    return comp;
}


void create (Workspace* wspace)
// creates dataset storing the coordinates of `size' distinct Points
{
    Dataset* dataset = wspace -> dataset;
    const int* size = dataset -> size;
    Random* random = wspace -> random;

    double* coords = dataset -> coords;
    double* x = coords;
    double* y = (coords + *size);

    // defines limits for the x, y coordinates
    double numel = ( (double) *size );
    double limit = (numel * numel);
    double x_min = -limit, x_max = limit;
    double y_min = -16, y_max = 16;

    // generates the x, y coordinates of the (distinct) Points array of length `size':

    const int sz = *size;
    for (int i = 0; i != sz; ++i)
    {
	double r = random -> next(random);
	double x_i = floor( x_min + r * (x_max - x_min) );
	double y_i = floor( y_min + r * (y_max - y_min) );

	double p [] = {x_i, y_i};
	// generates a new (x_i, y_i) coordinates set if already contained in the dataset:
	while ( algorithm.contains(sz, coords, 0, i, p) )
	{
	    r = random -> next(random);
	    x_i = floor( x_min + r * (x_max - x_min) );
	    y_i = floor( y_min + r * (y_max - y_min) );

	    p[0] = x_i;
	    p[1] = y_i;
	}

	// stores the `x' and `y' coordinates of the ith Point
	x[i] = x_i;
	y[i] = y_i;
    }

    // sorts to support the Divide and Conquer Algorithm
    algorithm.sort(sz, coords);
}


void print (const int size, const Dataset* dataset)
// prints the coordinates of the array of Points on the console
{
    const double* coords = dataset -> coords;

    const double* x = coords;
    const double* y = (coords + size);
    for (int i = 0; i != size; ++i)
    {
	printf("x: %+.15e y: %+.15e\n", x[i], y[i]);
    }
}


/*

Synopsis:
Implements the Brute Force Algorithm that finds the closest pair in the whole dataset.

Inputs:

size			the number of points represented by the dataset

dataset			first-rank array of length (2 * `size') storing the `x' and `y'
			coordinates of the points; the first `size' elements correspond to
			the `x' coordinates and the second `size' elements correspond to
			the `y' coordinates of the array of points

closestPairInfo		first-rank array of length 4 for storing the closest pair info:
			the positional index of the `first' point that comprises the
			closest pair, the positional index of the `second' point that
			comprises the closest pair, the separating distance of the
			closest pair comprising points, and the number of operations
			used to find the closest pair.

Outputs:

closestPairInfo		the positional indexes (`first' and `second') of the pair of
			points that comprise the closest pair, the separating distance of
			the closest pair (the minimum), and the number of operations spent
			on finding the closest pair.

*/
void bruteForce (const Workspace* wspace, ClosestPairInfo* closestPairInfo)
{
    const Dataset* dataset = wspace -> dataset;
    const int* size = dataset -> size;
    const double* coords = dataset -> coords;
    const double* x = coords;
    const double* y = (coords + *size);


    // considers all (distinct) pairs to find the closest pair:


    double first = -1;
    double second = -2;
    double d_min = INFINITY;
    for (int i = 0; i != (*size - 1); ++i)
    {
	for (int j = (i + 1); j != *size; ++j)
	{
	    double x1 = x[i], x2 = x[j];
	    double y1 = y[i], y2 = y[j];

	    // Note: computes the squared distance for speed
	    double d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

	    if (d < d_min)
	    // updates the closest pair info
	    {
		first = ( (double) i );
		second = ( (double) j );
		d_min = d;
	    }
	}
    }

    double N = *size;
    // determines the number of operations (or equivalently, the distance computations)
    double numOperations = ( N * (N - 1.0) ) / 2.0;

    closestPairInfo -> first = first;
    closestPairInfo -> second = second;
    closestPairInfo -> distance = d_min;
    closestPairInfo -> numOperations = numOperations;
}


void distance_smallerPartition (const int beginPosition,
				const int endPosition,
				const Workspace* wspace,
				ClosestPairInfo* closestPairInfo)
// as bruteForce() but delimited to the asymmetric range [beginPosition, endPosition)
{
    const Dataset* dataset = wspace -> dataset;
    const int* size = dataset -> size;
    const double* coords = dataset -> coords;
    const double* x = coords;
    const double* y = (coords + *size);


    // considers all (distinct) pairs in the arange [b, e) to find the closest pair:


    double first = INFINITY;
    double second = INFINITY;
    double d_min = INFINITY;
    const int b = beginPosition;
    const int e = endPosition;
    for (int i = b; i != (e - 1); ++i)
    {
	for (int j = (i + 1); j != e; ++j)
	{
	    double x1 = x[i], x2 = x[j];
	    double y1 = y[i], y2 = y[j];

	    double d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

	    if (d < d_min)
	    {
		first = ( (double) i );
		second = ( (double) j );
		d_min = d;
	    }
	}
    }

    double N = ( (double) (e - b) );
    // determines the number of operations given the arange size `N'
    double numOperations = ( N * (N - 1.0) ) / 2.0;

    closestPairInfo -> first = first;
    closestPairInfo -> second = second;
    closestPairInfo -> distance = d_min;
    closestPairInfo -> numOperations = numOperations;
}


void distance_partitionInterface (const int beginPosLeft,
				  const int endPosLeft,
				  const int beginPosRigth,
				  const int endPosRigth,
				  const Workspace* wspace,
				  ClosestPairInfo* closestPairInfo)
// as bruteForce() but updates the closest-pair by considering the partition interface
{
    const Dataset* dataset = wspace -> dataset;
    const int* size = dataset -> size;
    const double* coords = dataset -> coords;
    const double* x = coords;
    const double* y = (coords + *size);


    double first = closestPairInfo -> first;
    double second = closestPairInfo -> second;
    double d_min = closestPairInfo -> distance;
    for (int i = beginPosLeft; i != endPosLeft; ++i)
    {
	for (int j = beginPosRigth; j != endPosRigth; ++j)
	{
	    double x1 = x[i], x2 = x[j];
	    double y1 = y[i], y2 = y[j];

	    double d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

	    if (d < d_min)
	    {
		first = ( (double) i );
		second = ( (double) j );
		d_min = d;
	    }
	}
    }


    // updates the number of operations executed thus far:


    // Note:
    // Takes into account the distance computations done in combine() and the operations
    // done so far at deeper levels of recursion.
    double N1 = ( (double) (endPosLeft - beginPosLeft) );
    double N2 = ( (double) (endPosRigth - beginPosRigth) );
    double numOperations = closestPairInfo -> numOperations;
    numOperations += ( (N1 * N2) + (N1 + 1.0) + (N2 + 1.0) );


    // updates the closest pair info:


    closestPairInfo -> first = first;
    closestPairInfo -> second = second;
    closestPairInfo -> distance = d_min;
    closestPairInfo -> numOperations = numOperations;
}


/*

Synopsis:
Uses Brute Force to determine if the array of Points has duplicate closest pairs. That is,
the points that comprise the `second' closest pair are farther away than the points that
comprise the `first' closest pair.

Inputs:

size			the number of points represented by the dataset

dataset			first-rank array of length (2 * `size') storing the `x' and `y'
			coordinates of the points; the first `size' elements correspond to
			the `x' coordinates and the second `size' elements correspond to
			the `y' coordinates of the array of points

Output:
hasDuplicates		true if there are duplicate closest pairs, false otherwise

*/
bool hasDuplicateClosestPair (const int size, const Workspace* wspace)
{
    const Dataset* dataset = wspace -> dataset;
    const double* coords = dataset -> coords;

    const double* x = coords;
    const double* y = (coords + size);

    // considers all (distinct) pairs to find the first and second closest pairs:

    double d_min = INFINITY, d_2nd = INFINITY;
    for (int i = 0; i != (size - 1); ++i)
    {
	for (int j = (i + 1); j != size; ++j)
	{
	    double x1 = x[i], x2 = x[j];
	    double y1 = y[i], y2 = y[j];

	    double d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

	    if (d <= d_min)
	    {
		d_2nd = d_min;
		d_min = d;
	    }
	}
    }

    bool hasDuplicates;
    if (d_2nd == d_min)
    {
	hasDuplicates = true;
    }
    else
    {
	hasDuplicates = false;
    }

    return hasDuplicates;
}


void createDataset1D (Workspace* wspace)
// creates an array of distinct Points of length `size' without duplicate closest pairs
{
	const Dataset* dataset = wspace -> dataset;
	const int* size = dataset -> size;

	create(wspace);
	while ( hasDuplicateClosestPair(*size, wspace) )
	{
		create(wspace);
	}
}


bool isSorted (const int size, const Dataset* dataset)
// Returns true if the dataset is x-y sorted, false otherwise.
{
	int misplacements = 0;
	for (int i = 0; i != (size - 1); ++i)
	// loop-invariant: thus far we have checked the order of `i' pairs
	{
		if ( compare(size, dataset, i, i + 1) > 0 )
		{
			++misplacements;
		}
	}

	bool sorted = false;
	if (misplacements != 0)
	{
		sorted = false;
	}
	else
	{
		sorted = true;
	}

	return sorted;
}


bool hasDuplicates (const int size, const Dataset* dataset)
// returns true if there are repeated x, y coordindates (duplicate points), false if none
{
	int duplicates = 0;
	for (int i = 0; i != (size - 1); ++i)
	// loop-invariant: thus far we have checked `i' pairs for equality (duplication)
	{
		if ( compare(size, dataset, i, i + 1) == 0 )
		{
			++duplicates;
		}
	}

	if (duplicates != 0)
	{
		return true;
	}
	else
	{
		return false;
	}
}


bool isInvalid (const int size, const Dataset* dataset)
// Returns true if the dataset is not sorted primarily by the `x' coordinates and
// secondarily by the `y' coordinates (x-y sorted) or if it has repeated coordinates,
// returns false otherwise to indicate that the dataset is valid.
{
    if ( !isSorted(size, dataset) )
    {
	printf("isInvalid(): coordinates dataset must be x-y sorted\n");
	return true;
    }

    if ( hasDuplicates(size, dataset) )
    {
	printf("isInvalid(): dataset must encompass distinct coordinates\n");
	return true;
    }

    return false;
}


bool equal_closestPair (const ClosestPairInfo* closestPair1,
			const ClosestPairInfo* closestPair2)
// returns true if the closest pair are equal, false otherwise
{

    if (closestPair1 -> first != closestPair2 -> first)
    {
	return false;
    }

    if (closestPair1 -> second != closestPair2 -> second)
    {
	return false;
    }

    if (closestPair1 -> distance != closestPair2 -> distance)
    {
	return false;
    }

    return true;
}


void update_closestPairInfo (ClosestPairInfo* closestPair, const ClosestPairInfo* info)
// updates the closest pair
{
    closestPair -> first = info -> first;
    closestPair -> second = info -> second;
    closestPair -> distance = info -> distance;
}


void update_closestPair (ClosestPairInfo* closestPair,
		      const ClosestPairInfo* left_closestPair,
		      const ClosestPairInfo* rigth_closestPair)
// updates the closest pair by getting the closest pair from the left and rigth partitions
{
    const double left_minDistance = left_closestPair -> distance;
    const double rigth_minDistance = rigth_closestPair -> distance;

    if (left_minDistance < rigth_minDistance)
    {
	update_closestPairInfo(closestPair, left_closestPair);
    }
    else
    {
	update_closestPairInfo(closestPair, rigth_closestPair);
    }
}


void combine (const int beginPosition, const int endPosition,
	      const Workspace* wspace, ClosestPairInfo* closestPairInfo)
// combines the left and rigth partitions to look for the closest pair
{
    const Dataset* dataset = wspace -> dataset;
    const double* coords = dataset -> coords;
    const double* x = coords;


    // gets the (squared) separating distance of the current closest pair
    double d_min = closestPairInfo -> distance;


    // gets the total number of elements in the combined `left' and `rigth' partitions
    const int numel = (endPosition - beginPosition);


    // defines the arange [b, e) that delimits the left partition
    int beginPos = beginPosition;
    int endPos = beginPosition + (numel / 2);


    // initializes the arange that delimits the closest pair comprising points in the left
    int beginPosLeft = beginPosition + (numel / 2);
    int endPosLeft = beginPosition + (numel / 2);


    for (int i = 0; i != (endPos - beginPos); ++i)
    // traverses the left partition from back to front to look for closest pair candidates
    {
	int j = ( endPos - (i + 1) );		// index of the (current) rigthmost point

	double x1 = x[j];			// position of rightmost point in the left
	double x2 = x[endPos];			// position of leftmost point in the rigth

	double d = (x2 - x1) * (x2 - x1);	// Note: `d_min' is the squared distance

	if (d < d_min)				// adds rigthmost point to the arange
	{
	    --beginPosLeft;
	}
	else					// stops for the next one is farther apart
	{
	    break;
	}
    }


    // Assertion:
    // After the for-loop we have asserted that the (rigthmost) points in the left
    // partition delimited by the arange [beginPosLeft, endPosLeft) have the potential
    // to form a closest pair with the leftmost point in the rigth partition.


    // defines the arange [b, e) that delimits the rigth partition
    beginPos = beginPosition + (numel / 2);
    endPos = beginPosition + numel;


    // initializes the arange that delimits closest pair comprising points in the rigth
    int beginPosRigth = beginPosition + (numel / 2);
    int endPosRigth = beginPosition + (numel / 2);


    for (int i = beginPos; i != endPos; ++i)
    // traverses the rigth partition from front to back to find closest pair candidates
    {
	double x1 = x[beginPos - 1];		// position of rightmost point in the left
	double x2 = x[i];			// position of leftmost point in the rigth

	double d = (x2 - x1) * (x2 - x1);	// Note: `d_min' is the squared distance

	if (d < d_min)				// adds leftmost point to the arange
	{
	    ++endPosRigth;
	}
	else					// stops for the next one is farther apart
	{
	    break;
	}
    }


    // Assertion:
    // After the for-loop we have asserted that the (leftmost) points in the rigth
    // partition delimited by the arange [beginPosRigth, endPosRigth) have the potential
    // to form a closest pair with the rightmost point in the left partition.


    // updates the closest pair by considering the closest pair candidates
    return distance_partitionInterface(beginPosLeft, endPosLeft,
				       beginPosRigth, endPosRigth,
				       wspace, closestPairInfo);
}


void recurse (const int beginPosition, const int endPosition,
	      const Workspace* wspace, ClosestPairInfo* closestPair)
// implements the 1D Divide And Conquer Algorithm that finds the closest pair
{

    // gets the number of elements in the partition
    const int numel = (endPosition - beginPosition);

    if (numel <= 3)
    {
	distance_smallerPartition(beginPosition, endPosition, wspace, closestPair);
    }
    else
    {

	// divides the partition into (smaller) left and right partitions


	ClosestPairInfo left_closestPair = {
	    .first = INFINITY,
	    .second = INFINITY,
	    .distance = INFINITY,
	    .numOperations = INFINITY
	};


	const int beginPosLeft = beginPosition;
	const int endPosLeft = beginPosition + (numel / 2);
	// finds the closest pair in the left partition
	recurse(beginPosLeft, endPosLeft, wspace, &left_closestPair);


	ClosestPairInfo rigth_closestPair = {
	    .first = INFINITY,
	    .second = INFINITY,
	    .distance = INFINITY,
	    .numOperations = INFINITY
	};


	const int beginPosRigth = beginPosition + (numel / 2);
	const int endPosRigth = beginPosition + numel;
	// finds the closest pair in the rigth partition
	recurse(beginPosRigth, endPosRigth, wspace, &rigth_closestPair);


	// gets the closest of the closest pairs in the left adn rigth partitions
	update_closestPair(closestPair, &left_closestPair, &rigth_closestPair);


	// updates the number of operations invested thus far on finding the closest pair
	closestPair -> numOperations = (left_closestPair.numOperations +
					rigth_closestPair.numOperations);


	// combines the left and rigth partitions to find the closest pair between them
	combine(beginPosition, endPosition, wspace, closestPair);

    }

}


double timeBruteForce (Workspace* wspace, ClosestPairInfo* closestPair)
// times the implementation of the Brute Force Algorithm that finds the closest pair
{
    struct timespec* startTime = wspace -> startTime;
    struct timespec* endTime = wspace -> endTime;


    int stat = clock_gettime(CLOCK_MONOTONIC_RAW, startTime);

    if (stat != 0)
    {
	wspace = workspace.allocator.deallocate(wspace);
	printf("timeBruteForce(): unexpected system-clock error!\n");
	return (0xFFFFFFFF);
    }

    bruteForce(wspace, closestPair);	// finds the closest pair with brute force

    stat = clock_gettime(CLOCK_MONOTONIC_RAW, endTime);

    if (stat != 0)
    {
	wspace = workspace.allocator.deallocate(wspace);
	printf("timeBruteForce(): unexpected system-clock error!\n");
	return (0xFFFFFFFF);
    }

    // returns the elapsed-time (nanoseconds) invested on finding the closest pair

    return getElapsedTime(startTime, endTime);
}


double timeRecursive (Workspace* wspace, ClosestPairInfo* closestPair)
// times the implementation of the 1D Divide-and-Conquer, Closest-Pair, Finding Algorithm
{
    const Dataset* dataset = wspace -> dataset;
    const int* size = dataset -> size;
    struct timespec* startTime = wspace -> startTime;
    struct timespec* endTime = wspace -> endTime;


    ClosestPairInfo closestPairBruteForce = {
	.first = INFINITY,
	.second = INFINITY,
	.distance = INFINITY,
	.numOperations = INFINITY
    };

    // saves the closest pair found by the brute force algorithm
    bruteForce(wspace, &closestPairBruteForce);


    int stat = clock_gettime(CLOCK_MONOTONIC_RAW, startTime);

    if (stat != 0)
    {
	wspace = workspace.allocator.deallocate(wspace);
	printf("timeRecursive(): unexpected system-clock error!\n");
	return (0xFFFFFFFF);
    }


    // applies the 1D Divide And Conquer Algorithm to find the closest pair
    recurse(0, *size, wspace, closestPair);


    stat = clock_gettime(CLOCK_MONOTONIC_RAW, endTime);

    if (stat != 0)
    {
	wspace = workspace.allocator.deallocate(wspace);
	printf("timeRecursive(): unexpected system-clock error!\n");
	return (0xFFFFFFFF);
    }


    if ( !equal_closestPair(closestPair, &closestPairBruteForce) )
    // complains if the closest pairs are not equal
    {
	printf("timeRecursive(): different closest pairs detected!\n");
	return (0xFFFFFFFF);
    }


    // returns the elapsed-time (nanoseconds) invested on finding the closest pair
    return getElapsedTime(startTime, endTime);
}


void export (const char* filename, double (*timer) (Workspace*, ClosestPairInfo*) )
// exports the timings of the closest pair finding algorithm with respect to the number of
// points (or equivalently the `size') to a plain text file
{
    const int runs = RUNS;
    const int reps = REPS;
    double statistics[3 * RUNS];
    double* sizes = statistics;
    double* avgElapsedTimes = (statistics + RUNS);
    double* avgNumOperations = (statistics + 2 * RUNS);


    Workspace* wspace = NULL;
    wspace = (Workspace*) workspace.allocator.allocate(SIZE);

    if (wspace == NULL)
    {
	printf("export(): failed to allocate workspace!\n");
	return;
    }

    for (int run = 0; run != runs; ++run)
    // times the brute force algorithm as a function of the number of Points `size'
    {
	double avgElapsedTime = 0;
	double avgNumOperation = 0;
	const Dataset* dataset = wspace -> dataset;
	int* size = dataset -> size;
	for (int rep = 0; rep != reps; ++rep)
	// repeats the timing REPS times to get a meaningful elapsed-time measurement
	{
	    createDataset1D(wspace);	// creates a new Points array of length `size'

	    if ( isInvalid(*size, wspace -> dataset) )
	    // createDataset1D() yields a valid `dataset' but it is checked anyways
	    {
		wspace = workspace.allocator.deallocate(wspace);
		return;
	    }


	    ClosestPairInfo closestPairInfo = {
		.first = INFINITY,
		.second = INFINITY,
		.distance = INFINITY,
		.numOperations = INFINITY
	    };


	    // times the closest pair finding algorithm
	    double elapsedTime = timer(wspace, &closestPairInfo);


	    // Note:
	    // The selected hardware-clock is unaffected by NTP adjustments but we check
	    // for errors anyways. Note that if there is an error, it should manifest
	    // itself when the `size' of the Points array is relatively small.
	    if (elapsedTime < 0)
	    {
		printf("export(): unexpected error of the hardware-clock!\n");
		printf("invalid elpased-time (nanoseconds): %.15e\n", elapsedTime);
		wspace = workspace.allocator.deallocate(wspace);
		return;
	    }

	    avgElapsedTime += elapsedTime;
	    avgNumOperation += closestPairInfo.numOperations;
	}

	sizes[run] = *size;
	avgElapsedTimes[run] = ( avgElapsedTime / ( (double) reps ) );
	avgNumOperations[run] = ( avgNumOperation / ( (double) reps ) );

	(*size) *= 2;

	// reallocates the array of Points to the new length `size'
	wspace = workspace.allocator.reallocate(*size, wspace);

	if (wspace == NULL)
	{
	    printf("export(): reallocation failed!\n");
	    return;
	}
    }


    FILE* file = fopen(filename, "w");

    if (file == NULL)
    {
	printf("export_bruteForceTimes(): IO ERROR\n");
	wspace = workspace.allocator.deallocate(wspace);
	return;
    }


    for (int run = 0; run != runs; ++run)
    // exports the average elapsed-time and #operations as a function of the `size'
    {
	char fmt [] = ("%.15e %.15e %.15e\n");
	printf(fmt, sizes[run], avgElapsedTimes[run], avgNumOperations[run]);
	fprintf(file, fmt, sizes[run], avgElapsedTimes[run], avgNumOperations[run]);
    }

    fclose(file);
    wspace = workspace.allocator.deallocate(wspace);
    return;
}


/*
 * TODO:
 * [ ] get time-complexity results from the HPCf
 *
 */
