/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Obtains the average time complexity as a function of the ensemble size of the
implemented closest pair finding algorithms.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
[1] JJ McConnell, Analysis of Algorithms, 2nd edition
[2] compiling projects that use templates (or generics):

stackoverflow.com/questions/4955159/is-is-a-good-practice-to-put-the-definition-of-c-classes-into-the-header-file

*/

#include "TimeComplexityClass.h"

int main ()
{
	// considers eight ensemble sizes in the asymmetric range [2^4, 2^12)
	TimeComplexity t(8);
	// exports the time complexity data of the 1D Divide And Conquer Algorithm
	// as a function of the ensemble size
	t.exportTimeComplexity_DivideAndConquer1D();
	return 0;
}
