#ifndef GUARD__CLOSEST_PAIR_LAB__UTIL_WORKSPACE_ALLOCATOR_NAMESPACE__H
#define GUARD__CLOSEST_PAIR_LAB__UTIL_WORKSPACE_ALLOCATOR_NAMESPACE__H

typedef struct
{
    Workspace* (*allocate) (const int);			// allocates workspace from size
    Workspace* (*reallocate) (const int, Workspace*);	// reallocates workspace
    Workspace* (*deallocate) (Workspace*);		// deallocates workspace

} util_workspace_allocator_namespace;

#endif

/*
 * Closest Pair Lab						March 04, 2023
 *
 * source: allocator.h
 * author: @misael-diaz
 *
 * Synopsis:
 * Defines the namespace containing the names of the memory (de)allocation methods of the
 * workspace data structure.
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
