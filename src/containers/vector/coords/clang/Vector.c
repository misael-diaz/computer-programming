/*
 * Algorithms and Complexity                             September 03, 2022
 * IST 4310
 * Prof. M Diaz-Maldonado
 *
 * source: Vector.c
 *
 * Synopsis:
 * Defines methods of the vector class.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 *
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by
 *     Example.
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <errno.h>
#include "vector.h"

// implementations:
static size_t size_method (void* vector)
// returns the vector size --- the number of elements stored
{
	// asserts that the type of the (universal) pointer is a vector
	vector_t *vec = vector;
	return ( ( (vec -> avail) - (vec -> begin) ) / sizeof(coord_t) );
}


static void grow (vector_t* vec)
// doubles the vector size
{
	size_t numel = size_method (vec);
	size_t limit = (2 * numel);
	// creates an array temporary to backup the vector's data buffer
	coord_t *data = (coord_t*) malloc ( numel * sizeof(coord_t) );
	if (data == NULL)
	{
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
                exit(EXIT_FAILURE);
        }

	// copies data into the backup array temporary
	for (size_t i = 0; i != numel; ++i)
	{
		data[i].x = (vec -> array)[i].x;
		data[i].y = (vec -> array)[i].y;
	}

	// destroys the vector's data buffer
	free (vec -> array);
	vec -> array = NULL;
	vec -> begin = NULL;
	vec -> avail = NULL;
	vec -> limit = NULL;

	// doubles the memory allocation for the vector's data buffer
	vec -> array = (coord_t*) malloc ( limit * sizeof(coord_t) );
	if (vec -> array == NULL)
	{
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
                exit(EXIT_FAILURE);
        }

	// initializes the vector's data buffer
	coord_t* array = (vec -> array);
	for (size_t i = 0; i != limit; ++i)
	{
		array[i].x = 0;
		array[i].y = 0;
	}

	// restores the data
	for (size_t i = 0; i != numel; ++i)
	{
		array[i].x = data[i].x;
		array[i].y = data[i].y;
	}

	// updates the vector limits
	vec -> begin = (vec -> array);
	vec -> avail = ( (vec -> begin) + numel * sizeof(coord_t) );
	vec -> limit = ( (vec -> begin) + limit * sizeof(coord_t) );

	// frees the array temporary
	free (data);
	data = NULL;
}


static void push_back_method (void* vector, coord_t coord)
// pushes elements unto the back of vector, grows the vector as needed
{
	// asserts that the type of the (universal) pointer is a vector
	vector_t *vec = vector;
	// doubles the vector size if the limit has been reached
	if ( (vec -> avail) == (vec -> limit) )
		grow (vec);

	// gets the available location for storing new data
	size_t avail   = size_method (vec);
	// copies the input into the data buffer
	coord_t *array = (vec -> array);
	array[avail].x = coord.x;
	array[avail].y = coord.y;
	// updates the vector size
	vec -> avail += sizeof(coord_t);
}


vector_t* create (size_t size)
// constructor --- allocates memory for a vector of requested size
{
	// allocates memory for the vector
	vector_t *vec = (vector_t*) malloc ( sizeof(vector_t) );
        if (vec == NULL)
        {
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
                exit(EXIT_FAILURE);
        }

	// allocates memory for the data buffer
	vec -> array = (coord_t*) malloc ( sizeof(coord_t) * size );
	if (vec -> array == NULL)
	{
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
                exit(EXIT_FAILURE);
        }

	coord_t *array = vec -> array;
	// initializes the elements of the vector's data buffer
	for (size_t i = 0; i != size; ++i)
	{
		array[i].x = 0;
		array[i].y = 0;
	}

	// defines the vector limits
	vec -> begin = vec -> array;
	vec -> avail = vec -> array;
	vec -> limit = ( (vec -> begin) + size * sizeof(coord_t) );
	// binds the methods
	vec -> size  = size_method;
	vec -> push_back = push_back_method;

	return vec;
}


vector_t* destroy (vector_t* vec)
// destructor --- frees the memory allocated for the vector
{
	if (vec -> array)
	{
		free (vec -> array);
		vec -> array = NULL;
	}

	if (vec -> begin)
		vec -> begin = NULL;

	if (vec -> avail)
		vec -> avail = NULL;

	if (vec -> limit)
		vec -> limit = NULL;

	free (vec);
	vec = NULL;
	return vec;
}
