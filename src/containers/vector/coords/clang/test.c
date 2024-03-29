/*
 * Algorithms and Complexity                             September 03, 2022
 * IST 4310
 * Prof. M Diaz-Maldonado
 *
 * source: test.c
 *
 * Synopsis:
 * Tests the vector class.
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
#include "Vector.h"

extern vector_namespace const vector;	// imports vector namespace

int main() {

	size_t numel = 16;
	// creates a vector with requested storage capacity
	vector_t *vec = vector.create (numel);

	// pushes coordinates unto the back of the vector
	for (int i = 0; i != 32; ++i)
	{
		coord_t coord = {.x = i, .y = i};
		vec -> push_back (vec, coord);
	}

	coord_t *array = (vec -> array);
	// prints the coordinates on the console
	for (size_t i = 0; i != 32; ++i)
	{
		coord_t coord = {.x = array[i].x, .y = array[i].y};
		printf("%4d %4d \n", coord.x, coord.y);
	}

	// prints the vector size on the console
	printf("size: %lu \n", vec -> size(vec));

	// clears the data stored in vector
	vec -> clear(vec);
	printf("cleared vector\n");
	printf("size: %lu \n", vec -> size(vec));

	// pushes new coordinates unto the back of the vector
	for (int i = 0; i != 32; ++i)
	{
		coord_t coord = {.x = (32 - i), .y = (32 - i)};
		vec -> push_back (vec, coord);
	}

	// prints the new coordinates on the console
	for (size_t i = 0; i != 32; ++i)
	{
		coord_t coord = {.x = array[i].x, .y = array[i].y};
		printf("%4d %4d \n", coord.x, coord.y);
	}

	// prints the vector size on the console
	printf("size: %lu \n", vec -> size(vec));

	// frees the memory allocated for the vector
	vec = vector.destroy (vec);
	return 0;
}
