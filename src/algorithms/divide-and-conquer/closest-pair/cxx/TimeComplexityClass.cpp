/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Implements the Time Complexity Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include "TimeComplexityClass.h"


/* (de)constructors */


// useless default constructor
TimeComplexity::TimeComplexity () : runs(0) {}

// constructs object from the number of runs (or experiments)
TimeComplexity::TimeComplexity (int runs) : runs(runs) {}

// destructor
TimeComplexity::~TimeComplexity () {}


/* methods */


void TimeComplexity::exportTimeComplexity_DivideAndConquer1D () const
/*

Synopsis:
Exports the time complexity results of the 1D Divide and Conquer Algorithm that
solves the closest pair problem to a plain text data file.

Inputs:
None

Outputs:
None

*/
{
	if ( (this -> runs) <= 0 )
	// does nothing if the number of runs is nonsensical
	{
		return;
	}

	// creates containers to store the time complexity data
	int runs = this -> runs;
	std::vector<double> sizes;
	std::vector<double> avgElapsedTimes;
	std::vector<double> avgNumOperations;
	// preallocates containers for speed
	sizes.reserve(runs);
	avgElapsedTimes.reserve(runs);
	avgNumOperations.reserve(runs);

	// gets the time complexity data of the 1D Divide and Conquer Algorithm
	this -> timeDivideAndConquer1D(sizes, avgElapsedTimes, avgNumOperations);

	// creates the output file stream
	std::fstream out;
	// opens the output file stream
	out.open("timeDivideAndConquer1D.dat", std::ios::out);
	// writes time complexity results to the formatted output file stream
	for (int i = 0; i != (this -> runs); ++i)
	{
		out << std::scientific << std::setprecision(15)
			<< sizes[i] << " " << avgElapsedTimes[i] << " "
			<< avgNumOperations[i] << std::endl;
	}

	out.close();	// closes the output file stream
}


/* implementations */


void TimeComplexity::timeDivideAndConquer1D (std::vector<double>& sizes,
					     std::vector<double>& avgElapsedTimes,
					     std::vector<double>& avgNumOperations) const
/*

Synopsis:
Times the 1D Divide and Conquer Algorithm that finds the closest
pair in an ensmble of particles (or just points).

Inputs:
sizes			a preallocated vector
avgElapsedTimes		a preallocated vector
avgNumOperations	a preallocated vector

Outputs:
sizes			the ensemble sizes considered
avgElapsedTimes		contains the average elapsed time needed to find
			the closest pair for each ensemble size
avgNumOperations	contains the average number of operations needed
			to find the closest pair for each ensemble size

*/
{
	int size = 16;	// defines the initial ensemble size
	int reps = 512;	// defines the number of repetitions
	for (int i = 0; i != (this -> runs); ++i)
	{
		double etime = 0;
		double opers = 0;
		for (int j = 0; j != reps; ++j)
		// accumulates the elapsed-time and the number of operations
		{
			// creates a new ensemble of requested size
			Ensemble ens(size);
			// times the 1D Divide And Conquer Algorithm
			ens.recursive1D();

			// gets the elapsed-time and the number of operations
			etime += ens.getElapsedTime();
			opers += ens.getNumOperations();
		}

		// computes the average elapsed-time and number of operations
		double avgElapsedTime = (etime / reps);
		double avgOperationCount = (opers / reps);

		// inserts the ensemble size, elapsed-time and #operations
		sizes.push_back(size);
		avgElapsedTimes.push_back(avgElapsedTime);
		avgNumOperations.push_back(avgOperationCount);

		// doubles the ensemble size for the next run
		size *= 2;
	}
}
