#ifndef GUARD_AC_DUPLICATE_CP_EXCEPT_CLASS
#define GUARD_AC_DUPLICATE_CP_EXCEPT_CLASS


/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Duplicate Closest Pair Exception Class as an extension of std::exception.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/

#include <string>	// provides std::string

class DuplicateClosestPairException : public std::exception
{

	private:


	/* component(s) */


	std::string msg;		// error message


	public:


	/* (de)constructors */


	// default constructor
	DuplicateClosestPairException ();

	// constructs from error message contained in C-string
	DuplicateClosestPairException (const char *msg);

	// constructs from error message contained in std::string
	DuplicateClosestPairException (const std::string& msg);

	// destructor
	~DuplicateClosestPairException () noexcept;


	/* method(s) */


	const char* what () const noexcept;

};

#endif
