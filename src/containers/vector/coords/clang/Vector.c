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
#include "Vector.h"

// implementations:
static size_t size_method (void* vector)
// returns the vector size --- the number of elements stored
{
	// asserts that the type of the (universal) pointer is a vector
	vector_t *vec = vector;
	return ( ( (vec -> avail) - (vec -> begin) ) / sizeof(coord_t) );
}


static void clear_method (void* vector)
// clears the vector elements
{
	// asserts that the type of the (universal) pointer is a vector
	vector_t *vec = vector;
	vec -> avail = (vec -> begin);
}


static vector_t* util_alloc_vector_t ()
// allocates memory for a vector object
{
	vector_t *vec = (vector_t*) malloc ( sizeof(vector_t) );
        if (vec == NULL)
        {
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
		exit(EXIT_FAILURE);
        }

	return vec;
}


static coord_t* util_alloc_array_coord_t (size_t size)
// allocates memory for an array of coordinates of requested size
{
	coord_t *data = (coord_t*) malloc ( size * sizeof(coord_t) );
	if (data == NULL)
	{
		char errmsg [] = "memory allocation error: %s\n";
		fprintf (stderr, errmsg, strerror(errno) );
		exit(EXIT_FAILURE);
        }

	return data;
}


static coord_t* util_init_array_coord_t (size_t size, coord_t array[size])
// initializes array of coordinates with zeros
{
	for (int i = 0; i != size; ++i)
	{
		array[i].x = 0;
		array[i].y = 0;
	}

	return array;
}


static coord_t* util_copy_array_coord_t (
	size_t size, coord_t src[size], coord_t dst[size]
)
// copies from the source `src' to the destination `dst' coordinates array
{
	for (int i = 0; i != size; ++i)
	{
		dst[i].x = src[i].x;
		dst[i].y = src[i].y;
	}

	return dst;
}


static void grow (vector_t* vec)
// doubles the vector size
{
	size_t numel = size_method (vec);
	size_t limit = (2 * numel);
	// creates an array temporary to backup the vector's data buffer
	coord_t *data = util_alloc_array_coord_t (numel);

	// copies data into the backup array temporary
	data = util_copy_array_coord_t (numel, vec -> array, data);

	// destroys the vector's data buffer
	free (vec -> array);
	vec -> array = NULL;
	vec -> begin = NULL;
	vec -> avail = NULL;
	vec -> limit = NULL;

	// doubles the memory allocation for the vector's data buffer
	vec -> array = util_alloc_array_coord_t (limit);

	// initializes the vector's data buffer
	coord_t* array = (vec -> array);
	array = util_copy_array_coord_t (numel, data, array);

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


static vector_t* create (size_t size)
// constructor --- allocates memory for a vector of requested size
{
	// allocates memory for the vector
	vector_t *vec = util_alloc_vector_t ();

	// allocates memory for the data buffer
	vec -> array = util_alloc_array_coord_t (size);

	coord_t *array = vec -> array;
	// initializes the elements of the vector's data buffer
	array = util_init_array_coord_t (size, array);

	// defines the vector limits
	vec -> begin = vec -> array;
	vec -> avail = vec -> array;
	vec -> limit = ( (vec -> begin) + size * sizeof(coord_t) );
	// binds the methods
	vec -> size  = size_method;
	vec -> clear = clear_method;
	vec -> push_back = push_back_method;

	return vec;
}


static vector_t* destroy (vector_t* vec)
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


// creates namespace for the vector class constructor and destructor
vector_namespace const vector = {create, destroy};
