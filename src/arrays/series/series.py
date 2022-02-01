#!/usr/bin/env python3
# -*- coding: utf-8 -*-
syn="""
Computer Programming for ME                               December 10, 2020
ME 2010 WI20
Prof. M. Diaz-Maldonado

Synopsis:
Shows how to use arrays to compute (truncated) series in Python 3.

Examples:
[1] arithmetic series   [2] sum of squares  [3] alternating series
[4] geometric series    [5] Taylor series   [6] Taylor series


Copyright (c) 2021 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
[1] The Style Guide for Python Code: https://pep8.org/
"""


# imports needed facilities
from numpy import r_
from numpy import pi
from numpy import arange
from scipy.special import factorial

print(syn)  # displays the synopsis on the console
print("\nSeries:\n")


"""
Example 1:  Arithmetic Sequence
Sum all the natural numbers from one to a hundred.
For n = [1, 100], obtain the sum of `n'.
"""

# defines the index of the sequence as an array
n = r_[1:101]       # note that in Python the end-point is non-inclusive
n = arange(1, 101)  # or equivalently use the `asymmetric range' function
# computes the elements of the sequence and stores them in an array
sn = n
# answer, sums all the elements of the series
sn.sum()

# uses f-string to display answer on the console
ans = (
    f"\n\n"
    f"Example 1: Arithmetic Sequence\n"
    f"for n = [1, 100], Sum(n) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)



"""
Example 2: Sum of Squares
Obtain the sum of the squares of the natural numbers from one to a hundred.
For n = [1, 100] obtain the sum of n^2.
"""

n = arange(1, 101)
sn = n**2   # note that ** is the exponentiation operator in Python

ans = (
    f"\n\n"
    f"Example 2: Sum of Squares\n"
    f"for n = 1:100, Sum(n^2) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)



"""
Example 3: Alternating Series
For n = [0, 100) compute the sum of (-1)^(n) / (n + 1).
"""

n = arange(100)         # defines the index of the sequence
sn = (-1)**(n) / (n+1)  # computes the elements of the sequence

ans = (
    f"\n\n"
    f"Example 3: Alternating Series\n"
    f"for n = [0, 100), Sum( (-1)^n / (n + 1) ) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)



"""
Example 4: Geometric Series
For n = [0, 100) obtain the sum of r^n, where r = 0.2.
"""
r = 0.2         # defines the geometric constant `r'
n = arange(100) # defines the index of the sequence
sn = r**n       # computes the elements of the sequence

ans = (
    f"\n\n"
    f"Example 4: Geometric Series\n"
    f"for n = [0, 100), Sum(r^n) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)


"""
Example 5: Taylor series
Sum the first 16 elements of the series:


    for n = [0, 16), obtain the sum of 2^n / n!,


where`n!' is the factorial of `n'.
"""

n = arange(16)
sn = 2**n / factorial(n)

ans = (
    f"\n\n"
    f"Example 5: Taylor Series\n"
    f"for n = [0, 16), Sum( 2^n / n! ) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)


"""
Example 6: Taylor series
Obtain the sum of the first 11 elements of the series:


        for n = [0, 11), sum( (-1)^n * x^(2n+1) / (2n+1)! )


where x = pi / 3.
"""

x = pi/3
n = arange(11)
sn = (-1)**n * x**(2*n+1) / factorial(2*n+1)

ans = (
    f"\n\n"
    f"Example 6: Taylor Series\n"
    f"for n = [0, 11), Sum( (-1)^n * x^(2n+1) / (2n+1)! ) = {sn.sum()}\n"
    f"\n\n"
)
print(ans)
