#ifndef GUARD_AC_COMPARATOR_FUN
#define GUARD_AC_COMPARATOR_FUN

// returns 0 if x1 == x2, returns 1 if x1 > x2, and returns -1 if x1 < x2
template <typename T> int Comparator (T x1, T x2)
{
  if (x1 == x2)
  {
    return 0;
  }
  else if (x1 > x2)
  {
    return 1;
  }
  else
  {
    return -1;
  }
}

#endif

/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Generic Comparator Function.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/
