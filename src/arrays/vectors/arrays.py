#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Computer Programming for ME                                December 1, 2020
ME 2010 WI20
Prof. M. Diaz-Maldonado

Synopsis:
Shows how to create and use variables to perform engineering computations
with Python 3.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
[1] The Style Guide for Python Code: https://pep8.org/
"""


# imports the supporting methods from the numerical python module
from numpy import r_
from numpy import cbrt
from numpy import linspace


"""
Example 1.6-A: Using variables
Assigns the value 4.5 to the variable `x` and computes y = f(x), the
value of f(x) is stored in the variable `y':
"""
x = 4.5
y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7

# uses f-string to display the numerical result
print(f"f(x = {x}) = {y}")


"""
Example 1.6-B: Recycling variables
Reuses variable `x` to evaluate another function, y = f(x):
"""
y = (x**3 - 23) / cbrt(x**2 + 17.5)
print(f"f(x = {x}) = {y:6.2f}")


"""
Example 1.6-C: Incrementing sequences
Assigns the sequence 0, 1, 2, 3, ..., 6, 7 to the variable `x`.
Note that in Python the end point is non-inclusive so you need to
increase the last value by the increment to obtain the desired sequence,
in this example the increment is one (hence the end point is 7+1=8).
"""
x = r_[0:8]

"""
The python interpreter assumes elementwise operations by default, thus
the code looks cleaner than its MATLAB counterpart.
"""
y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7

# references (or addresses) the element at the 4th position
print(f'\nexample 1.6-C:')
print(f'references the 4th element of array x: {x[3]}')
print(f'references the 4th element of array y: {y[3]:.6f}')


"""
Example 1.6-D: Non-unit, step, incrementing, sequences
Creates a sequence that increases in steps of two:
"""
x = r_[0:16:2]  # x = begin:end:step, note: non-inclusive end-point
y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7
print(f'\nexample 1.6-D:')
print(f'references the 7th element of array x: {x[6]}')
print(f'references the 7th element of array y: {y[6]:.6f}')


"""
Example 1.6-E: Decrementing sequences
Generates a decrementing sequence:
"""
x = r_[14:0:-2]
y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7

"""
Example 1.6-F: Non-integer, step, incrementing sequences
Creates an increasing sequence having a non-integer step (step: 0.15).
via the linspace function. It's a good practice to use the linspace
function to generate sequences having non-integer steps.
"""

x = linspace(0, 1.15, 8)    # linspace(begin, end, numel):
                            # begin: beginning value
                            # end:   ending value
                            # numel: number of elements of the sequence

y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7


# Example 1.6-G: Using the linspace function to generate sequences

x = linspace(0, 7, 8)
y = 0.4 * x**4 + 3.1 * x**2 - 162.3 * x - 80.7


"""
Good Programming Practices:
[0] Comment your code to make it useful for future use.
[1] Limit the lenght of the line of code to 75 characters or less.
[2] Use the semicolon (;) to suppress the output of this line of code
    on the command window.
[3] Use the dot `.' exclusively for elementwise operations on arrays
    to make the code more readable.
[4] Use the linspace function to generate sequences having non-integer
    steps.
"""


"""
Comments:
If you use Python 2 you might obtain incorrect numeric results because of
the way the interpreter handles division. You are encouraged to consult the
following article:

        https://www.tutorialspoint.com/division-operators-in-python

"""
