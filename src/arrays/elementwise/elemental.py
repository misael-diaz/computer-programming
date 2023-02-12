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
from numpy import concatenate
from pandas import DataFrame


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

print(f'references the 4th element of x: {x[3]:+.15e}')

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

print(f'references the 8th element of x: {x[7]:+.15e}')

"""
Example 1.6-E: Decrementing sequences
i = 14, 12, 10, ..., 4, 2
"""

x = zeros([7, 1])   # preallocates as a column vector of 7 rows
x[0] = 14
for i in range(1, 7):
    x[i] = x[i-1] - 2

print('\nExample 1.6-E: i = 14, 12, 10, ..., 4, 2\n')

# calculates the function y = f(x) element-by-element in a for-loop
y = zeros_like(x)
for i in range(x.size):
    y[i] = 0.4 * x[i]**4 + 3.1 * x[i]**2 - 162.3 * x[i] - 80.7

# joins x and y into a 2D array or matrix for printing
data = concatenate([x, y], axis=1)
df = DataFrame(data)    # creates a data frame from numpy array
print(df)               # displays the data frame to the console


"""
Sum of Squares Example
"""
n = zeros([8, 1])       # preallocates series index as a column vector

n[0] = 1
for i in range(1, n.size):
    n[i] = n[i - 1] + 1


s = zeros_like(n)       # preallocates sequence as a column vector

for i in range(8):
    s[i] = n[i]**2

cumsum = zeros_like(n)  # preallocates `cumulative sum' as a column vector

cumsum[0] = s[0]
for i in range(1, n.size):
    cumsum[i] = cumsum[i - 1] + s[i]

print('\nSum of Squares:\n')
# prints the series index, sequence, and cumulative sum arrays
data = concatenate([n, s, cumsum], axis=1).astype(int)
df = DataFrame(data)
print(df)
