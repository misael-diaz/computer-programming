/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Implements the Duplicate Closest Pair Exception Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include "DuplicateClosestPairExceptionClass.h"


/* (de)constructors */


DuplicateClosestPairException::DuplicateClosestPairException ()
// default constructor
{
	this -> msg = std::string("DuplicateClosestPairException");
}


DuplicateClosestPairException::DuplicateClosestPairException (const char* msg)
// constructs from a C-string
{
	this -> msg = std::string(msg);
}


DuplicateClosestPairException::DuplicateClosestPairException (const std::string& msg)
// constructs from a string
{
	this -> msg = msg;
}


DuplicateClosestPairException::~DuplicateClosestPairException () noexcept {}


/* method(s) */


const char* DuplicateClosestPairException::what () const noexcept
// returns the error message contained in a C-string
{
		return msg.c_str();	// creates C-string from std::string
}
