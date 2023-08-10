/*
 * Closest Pair Lab					March 04, 2023
 *
 * source: allocator.c
 * author: @misael-diaz
 *
 * Synopsis:
 * Implements workspace (de)allocators.
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


#include "../../../include/ds/prng.h"
#include "../../../include/ds/dataset.h"
#include "../../../include/ds/workspace.h"
#include "../../../include/util/prng/prng.h"
#include "../../../include/util/dataset/dataset.h"
#include "../../../include/util/workspace/workspace.h"


extern util_prng_namespace const prng;
extern util_dataset_namespace const dataset;


static Workspace* deallocate (Workspace* workspace)
{
    if (workspace == NULL)
	return workspace;

    workspace -> random = prng.destroy(workspace -> random);
    workspace -> dataset = dataset.allocator.deallocate(workspace -> dataset);

    // checks allocation status so that we can invoke deallocate() from anywhere

    if (workspace -> startTime != NULL) free(workspace -> startTime);
    if (workspace -> endTime != NULL) free(workspace -> endTime);


    workspace -> dataset = NULL;
    workspace -> startTime = NULL;
    workspace -> endTime = NULL;
    workspace -> random = NULL;

    free(workspace);
    workspace = NULL;

    return workspace;
}

static Workspace* allocate (const int ensembleSize)
// allocates memory for the workspace
{
    Workspace* workspace = NULL;
    workspace = (Workspace*) malloc( sizeof(Workspace) );

    if (workspace == NULL)
    {
	printf("workspace.allocator.allocate(): failed to allocate workspace!\n");
	return workspace;
    }


    workspace -> dataset = NULL;
    workspace -> startTime = NULL;
    workspace -> endTime = NULL;
    workspace -> random = NULL;


    workspace -> dataset = dataset.allocator.allocate(ensembleSize);

    if (workspace -> dataset == NULL)
    {
	printf("workspace.allocator.allocate(): failed to allocate dataset!\n");
	return deallocate(workspace);
    }


    workspace -> startTime = (struct timespec*) malloc( sizeof(struct timespec) );

    struct timespec* startTime = workspace -> startTime;

    if (startTime == NULL)
    {
	printf("workspace.allocator.allocate(): failed to allocate startTime!\n");
	return deallocate(workspace);
    }


    // checks for unexpected system clock error


    if (clock_getres(CLOCK_MONOTONIC_RAW, startTime) != 0)
    {
	printf("workspace.allocator.allocate():");
	printf("clock_getres(): returned non-zero status (startTime)\n");
	return deallocate(workspace);
    }


    workspace -> endTime = (struct timespec*) malloc( sizeof(struct timespec) );

    struct timespec* endTime = workspace -> endTime;

    if (endTime == NULL)
    {
	printf("workspace.allocator.allocate(): failed to allocate endTime!\n");
	return deallocate(workspace);
    }


    // checks for unexpected system clock error


    if (clock_getres(CLOCK_MONOTONIC_RAW, endTime) != 0)
    {
	printf("workspace.allocator.allocate():");
	printf("clock_getres(): returned non-zero status (endTime)\n");
	return deallocate(workspace);
    }


    workspace -> random = prng.create();

    if (workspace -> random == NULL)
    {
	printf("workspace.allocator.allocate(): ");
	printf("failed to allocate the Pseudo Random Number Generator PRNG!\n");
	return deallocate(workspace);
    }

    return workspace;
}


static Workspace* reallocate (const int size, Workspace* workspace)
{
    if (workspace == NULL)
	return allocate(size);

    workspace -> dataset = dataset.allocator.reallocate(size, workspace -> dataset);

    if (workspace -> dataset == NULL)
    {
	printf("workspace.allocator.reallocate(): reallocation failed!\n");
	return deallocate(workspace);
    }

    return workspace;
}


static util_workspace_allocator_namespace const allocator = {
    .allocate = allocate,
    .reallocate = reallocate,
    .deallocate = deallocate
};


util_workspace_namespace const workspace = {
    .allocator = allocator
};
