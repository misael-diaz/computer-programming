#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Computer Programming                                      November 17, 2020
ME 2010 WI20
Prof. M. Diaz-Maldonado


Synopsis:
Code shows how to perform some arithmetic operations in Python 3.


Copyright (c) 2021 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
[1] The Style Guide for Python Code: https://pep8.org/
"""


from numpy import exp, sqrt, cbrt, log10
from numpy import pi, cos, sin, tan, deg2rad


# defines MATLAB-like tand() function
tand = lambda x: tan( deg2rad(x) )


""" examples: """


# example 1.1-A
x11a = (5 - 19/7 + 2.5**3) ** 2

# example 1.1-B
x11b = 7 * 3.1 + sqrt(120)/5 - 15**(5/3)

# example 1.2-A
x12a = cbrt(8 + 80/2.6) + exp(3.5)

# example 1.3-A
x13a =(23 + cbrt(45) ) / (16 * 0.7) + log10(589006)

# example 1.4-D
x14d = (
    1 / sqrt(5.0e-2) + 2 * log10(4.5e-3 / 3.7 + 2.51 /
        ( 1.8e6 * sqrt(2.5e-1) ))
)

# example 1.5-A
x15a = sin(0.2 * pi) / cos(pi / 6) + tand(72)


# displays answers
ans = (
    f"\n\n"
    f"example 1.1-A: {x11a:10.4f}\n"
    f"example 1.1-B: {x11b:10.4f}\n"
    f"example 1.2-A: {x12a:10.4f}\n"
    f"example 1.3-A: {x13a:10.4f}\n"
    f"example 1.4-D: {x14d:10.4f}\n"
    f"example 1.5-A: {x15a:10.4f}\n"
    f"\n\n"
)

print(ans)


"""
Good Programming Practices:

Use `from module import names' to import module-names into the namespace.
This tells future users and developers what facilities, functions, etc.
does your code use from the modules it imports from. You may want to
import the module (as for example import numpy) if it's otherwise
impractical to do so.

Use the format string f-string for writing output into the console.

Use parenthesis to break down long formulas as in example 1.4-D.
"""
