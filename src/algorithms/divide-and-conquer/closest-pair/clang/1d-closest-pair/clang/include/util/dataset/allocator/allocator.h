#ifndef GUARD__CLOSEST_PAIR_LAB__UTIL_DATASET_ALLOCATOR_NAMESPACE__H
#define GUARD__CLOSEST_PAIR_LAB__UTIL_DATASET_ALLOCATOR_NAMESPACE__H

typedef struct
{
    Dataset* (*allocate) (const int);			// allocates dataset from size
    Dataset* (*reallocate) (const int, Dataset*);	// reallocates dataset to size
    Dataset* (*deallocate) (Dataset*);			// deallocates dataset

} util_dataset_allocator_namespace;

#endif

/*
 * Closest Pair Lab						March 05, 2023
 *
 * source: allocator.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Defines the namespace containing the names of the memory (de)allocator methods of
 * the dataset data structure.
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
