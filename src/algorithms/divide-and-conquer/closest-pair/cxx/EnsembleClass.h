#ifndef GUARD_AC_ENSEMBLE_CLASS
#define GUARD_AC_ENSEMBLE_CLASS


/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Ensemble Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include <stdexcept>	// provides std::invalid_argument exception
#include <algorithm>	// provides std::sort and std::copy algorithms
#include <iostream>	// provides std::cout and std::endl
#include <random>	// provides std::random_device and other PRNGs
#include <string>	// provides std::string
#include <vector>	// provides std::vector, a dynamic array
#include <chrono>	// provides std::chrono::system_clock()
#include <tuple>	// provides std::tuple, std::make_tuple, and std::tie
#include <list>		// provides std::list, a doubly-linked list

#include "DuplicateClosestPairExceptionClass.h"
#include "PointClass.h"
#include "PairClass.h"
#include "HandlerTemplateClass.h"


class Ensemble	// Ensemble of Points Class
{

	private:


	/* components */


	Handler<Point*> handler;	// point objects handler
	double elapsedTime;		// elapsed time (nanoseconds)
	double numOperations;		// operations (computed distances) counter
	int size;			// ensemble size


	public:


	/* (de)constructors */


	// useless default constructor
	Ensemble ();

	// constructs an ensemble of requested size
	explicit Ensemble (int size);

	// destructor
	~Ensemble ();


	/* getters */


	double getElapsedTime () const;
	double getNumOperations () const;
	double getSize () const;


	/* methods */


	// invokes the implementation of the Brute Force Algorithm
	void bruteForce ();

	// invokes the implementation of the 1D Divide And Conquer Algorithm
	void recursive1D ();


	private:


	/* implementations */


	// times the Brute Force Algorithm
	Pair* bruteForceMethod (std::vector<Point*>& points);

	// times the 1D Divide And Conquer Algorithm
	Pair* recursive1DMethod (std::vector<Point*>& points);

	// implements the 1D Divide And Conquer Algorithm
	std::tuple<Pair*, double> recurse (const std::vector<Point*>& Px) const;

	// implements the Brute Force Algorithm for a partition
	std::tuple<Pair*, double> distance (const std::vector<Point*>& points) const;

	// overloads the Brute Force Algorithm for neighboring partitions
	std::tuple<Pair*, double> distance (const std::vector<Point*>& L,
					    const std::vector<Point*>& R,
					    Pair* closestPair) const;

	// strips pairs comprised by distant points in neighboring partitions
	std::tuple<Pair*, double> combine (const std::vector<Point*>& L,
					   const std::vector<Point*>& R,
					   Pair* closestPair) const;


	/* utilities */


	// creates a dataset of distinct points without duplicate closest pairs
	void createDataset1D (std::vector<Point*>& dataset);

	// checks if the dataset has duplicate closest pairs
	void hasDuplicateClosestPair (const std::vector<Point*>& points) const;

	// complains if the dataset has duplicates or if it is not x-y sorted
	void isInvalidData (const std::vector<Point*>& data) const;

	// returns true if the dataset is x-y sorted, returns false otherwise
	bool isSorted (const std::vector<Point*>& points) const;

	// returns true if the dataset has duplicate points, returns false otherwise
	bool hasDuplicates (const std::vector<Point*>& dataset) const;

	// creates a dataset of distinct points
	void create (std::vector<Point*>& points);
};

#endif
