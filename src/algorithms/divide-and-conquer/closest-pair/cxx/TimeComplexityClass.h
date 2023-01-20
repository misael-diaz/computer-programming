#ifndef GUARD_AC_TIME_COMPLEXITY_CLASS
#define GUARD_AC_TIME_COMPLEXITY_CLASS

/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Time Complexity Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/

#include <vector>	// provides std::vector
#include <fstream>	// provides std::fstream, sequential file stream
#include <iomanip>	// provides std::ios::out, std::scientific and std::setprecision()

#include "EnsembleClass.h"

class TimeComplexity		// Time Complexity Class
{

	private:


	/* component(s) */


	int runs;		// number of time complexity experiments


	public:


	/* (de)constructors */


	// useless default constructor
	TimeComplexity ();

	// constructs object from the number of runs (or experiments)
	TimeComplexity (int runs);

	// destructor
	~TimeComplexity ();


	/* methods */


	// exports the time complexity results of the 1D Divide And Conquer Algorithm
	void exportTimeComplexity_DivideAndConquer1D () const;


	private:


	/* implementations */


	// times (repeatedly) the 1D Divide And Conquer Algorithm
	void timeDivideAndConquer1D (std::vector<double>& sizes,
				     std::vector<double>& avgElapsedTimes,
				     std::vector<double>& avgNumOperations) const;
};

#endif
