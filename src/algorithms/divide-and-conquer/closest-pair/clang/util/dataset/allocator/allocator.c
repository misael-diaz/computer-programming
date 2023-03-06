/*
 * Closest Pair Lab						March 05, 2023
 *
 * source: allocator.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Implements a memory allocator for the dataset data structure.
 *
 * Copyright (c) 2023 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */


#include <stdio.h>
#include <stdlib.h>
#include "../../../include/ds/dataset.h"
#include "../../../include/util/dataset/dataset.h"


/*

Synopsis:
Allocates memory for the placeholder `dataset' used to store the `x' and `y' coordinates
of an array of Points of length equal to `size'.

Notes:
The first `size' elements store the `x' coordinates and the second `size' elements store
the `y' coordinates. This design has been chosen to use the CPU cache efficiently.

*/


static double* deallocateCoords (double* coords)
// deallocates the point coordinates from memory
{
    if (coords == NULL)
	return coords;

    free(coords);
    coords = NULL;
    return coords;
}


static double* allocateCoords (const int size)
{
    double* coords = NULL;
    coords = (double*) malloc( 2 * size * sizeof(double) );
    return coords;
}


static double* reallocateCoords (const int size, double* coords)
{
    coords = deallocateCoords(coords);
    coords = (double*) malloc( 2 * size * sizeof(double) );
    return coords;
}


static Dataset* deallocate (Dataset* dataset)
{
    if (dataset == NULL)
	return dataset;

    if (dataset -> size != NULL) free(dataset -> size);
    dataset -> coords = deallocateCoords(dataset -> coords);

    dataset -> size = NULL;
    dataset -> coords = NULL;

    free(dataset);
    dataset = NULL;
    return dataset;
}


static Dataset* allocate (const int size)
{
    Dataset* dataset = NULL;
    dataset = (Dataset*) malloc( sizeof(Dataset) );

    if (dataset == NULL)
    {
	printf("dataset.allocator.allocate(): memory allocation failed!\n");
	return dataset;
    }

    dataset -> size = NULL;
    dataset -> coords = NULL;


    dataset -> size = (int*) malloc( sizeof(int) );

    if (dataset -> size == NULL)
    {
	printf("dataset.allocator.allocate(): memory allocation failed!\n");
	return deallocate(dataset);
    }


    int* sz = dataset -> size;
    *sz = size;


    dataset -> coords = allocateCoords(size);

    if (dataset -> coords == NULL)
    {
	printf("dataset.allocator.allocate(): memory allocation failed!\n");
	return deallocate(dataset);
    }


    double* coords = dataset -> coords;


    double* x = coords;
    for (int i = 0; i != size; ++i)
	    x[i] = 0.0;


    double* y = (coords + size);
    for (int i = 0; i != size; ++i)
	    y[i] = 0.0;


    return dataset;
}


static Dataset* reallocate (const int size, Dataset* dataset)
{
    if (dataset == NULL)
	return allocate(size);

    int* sz = dataset -> size;
    *sz = size;


    dataset -> coords = reallocateCoords(size, dataset -> coords);

    if (dataset -> coords == NULL)
    {
	printf("dataset.allocator.reallocate(): reallocation failed!\n");
	return deallocate(dataset);
    }


    double* coords = dataset -> coords;


    double* x = coords;
    for (int i = 0; i != size; ++i)
	    x[i] = 0.0;


    double* y = (coords + size);
    for (int i = 0; i != size; ++i)
	    y[i] = 0.0;


    return dataset;
}


static util_dataset_allocator_namespace const allocator = {
    .allocate = allocate,
    .reallocate = reallocate,
    .deallocate = deallocate
};


util_dataset_namespace const dataset = {
	.allocator = allocator
};
