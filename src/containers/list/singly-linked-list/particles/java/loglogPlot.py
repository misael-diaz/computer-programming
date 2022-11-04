#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Complexity                                  October 28, 2022
IST4310
Prof. M Diaz-Maldonado

Synopsis:
Plots the average number of operations and the average elapsed time
as a function of the input size.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] TH Cormen, CE Leiserson, RL Rivest, C Stein, Introduction to
    Algorithms, 4th edition
[2] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""

# imports the needed methods from numerical python and matplotlib library
from numpy import loadtxt
from numpy import log2 as log
from matplotlib import pyplot as plt

""" imports the time data of the recursive algorithm """

# unpacks the input size, the number of comparisons, and the elapsed time
size, etime, opers = loadtxt("time.dat").T

""" visualization """

# closes all files and enables interactive plotting
plt.close('all')
plt.ion()
# creates a figure and a set of axes
fig, ax = plt.subplots()

# computes scaling constants to improve presentation
c1 = (size**2).mean() / etime.mean()
c2 = size.mean() / opers.mean()
# plots the expected (theoretical) time as a function of size
ax.loglog(size, size,    color='black', linestyle='-',
          label='linear')
ax.loglog(size, size**2,    color='black', linestyle='--',
          label='quadratic')
# plots the elapsed time as a function of size
ax.loglog(size, c1 * etime, color='black',
    linestyle='', marker='o', markersize=12, label='elapsed-time')
# plots the number of operations as a function of size
ax.loglog(size, c2 * opers, color='red',   linestyle='', marker='*',
          markersize=12, label='distance-computations')
ax.set_xlabel('input size, N')
ax.set_ylabel('time, t')
ax.set_title('Recursive Algorithm')
ax.legend()

# exports the figure in PNG with a resolution of 600 DPI
fig.savefig("time-complexity-recursive.png", dpi=600)


""" brute force algorithm """


size, etime, opers = loadtxt("timeBruteForce.dat").T
# creates a figure and a set of axes
fig, ax = plt.subplots()

# computes scaling constants to improve presentation
c1 = (size**3).mean() / etime.mean()
c2 = (size**2).mean() / opers.mean()
# plots the expected (theoretical) time as a function of size
ax.loglog(size, size**2,    color='black', linestyle='-',
          label='quadratic')
ax.loglog(size, size**3,    color='black', linestyle='--',
          label='cubic')
# plots the elapsed time as a function of size
ax.loglog(size, c1 * etime, color='black',
    linestyle='', marker='o', markersize=12, label='elapsed-time')
# plots the number of operations as a function of size
ax.loglog(size, c2 * opers, color='red',   linestyle='', marker='*',
          markersize=12, label='distance-computations')
ax.set_xlabel('input size, N')
ax.set_ylabel('time, t')
ax.set_title('Brute Force Algorithm')
ax.legend()

# exports the figure in PNG with a resolution of 600 DPI
fig.savefig("time-complexity-bruteforce.png", dpi=600)


""" improved brute force algorithm """


size, etime, opers = loadtxt("time-improvedBruteForce.dat").T
# creates a figure and a set of axes
fig, ax = plt.subplots()

# computes scaling constants to improve presentation
c1 = ( log(size) * ( size * log(size) )**2 ).mean() / etime.mean()
c2 = (size**2).mean() / opers.mean()
# plots the expected (theoretical) time as a function of size
ax.loglog(size, log(size) * ( size * log(size) )**2, color='black',
    linestyle='--', label='super-quadratic')
ax.loglog(size, size**2,    color='black', linestyle='-',
    label='quadratic')
# plots the elapsed time as a function of size
ax.loglog(size, c1 * etime, color='black',
    linestyle='', marker='o', markersize=12, label='elapsed-time')
# plots the number of operations as a function of size
ax.loglog(size, c2 * opers, color='red',   linestyle='', marker='*',
    markersize=12, label='distance-computations')
ax.set_xlabel('input size, N')
ax.set_ylabel('time, t')
ax.set_title('improved Brute Force Algorithm')
ax.legend()

# exports the figure in PNG with a resolution of 600 DPI
fig.savefig("time-complexity-improved-bruteforce.png", dpi=600)
