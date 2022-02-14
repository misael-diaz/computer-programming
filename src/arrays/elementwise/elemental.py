#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Programming II                             February 13, 2022
IST 2089
Prof. M. Diaz-Maldonado

Synopsis:
Conducts elementwise operations on arrays via for-loops.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
[1] The Style Guide for Python Code: https://pep8.org/
"""


from numpy import r_
from numpy import zeros, zeros_like


"""
Example 1.6-C: Incrementing sequences
i = 0, 1, 2, 3, ..., 6, 7
"""
x = zeros(8)    # preallocates array

x[0] = 0
for i in range(1, 8):
    x[i] = x[i - 1] + 1

x = x.astype(int)
print('\nExample 1.6-C: i = [0, 8)')
print(x)    # equivalent to print(r_[0:8])


"""
Example 1.6-D: Non-unit, step, incrementing, sequences
i = 0, 2, 4, ..., 12, 14
"""
x = zeros(8)

x[0] = 0
for i in range(1, 8):
    x[i] = x[i-1] + 2

x = x.astype(int)
print('\nExample 1.6-D: i = 0, 2, 4, ..., 12, 14')
print(x)    # equivalent to print(r_[0:16:2])


"""
Example 1.6-E: Decrementing sequences
i = 14, 12, 10, ..., 4, 2
"""

x = zeros(7)
x[0] = 14
for i in range(1, 7):
    x[i] = x[i-1] - 2

print('\nExample 1.6-E: i = 14, 12, 10, ..., 4, 2')
print(x)    # equivalent to print(r_[14:0:-2])

# calculates the function y = f(x) element-by-element in a for-loop
y = zeros_like(x)
for i in range(x.size):
    y[i] = 0.4 * x[i]**4 + 3.1 * x[i]**2 - 162.3 * x[i] - 80.7

print(y)
