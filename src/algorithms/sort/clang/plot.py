#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms                                             August 05, 2023

source: plot.py
author: @misael-diaz

Synopsis:
Plots the time complexity of the implementation of the insertion sort algorithm isort().

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""

from numpy import loadtxt
from numpy import log2 as log
from matplotlib import pyplot as plt

size, time = loadtxt('complexity.txt').transpose()

plt.close('all')
plt.ion()
fig, ax = plt.subplots()
c = time[-1] / (size[-1] ** 2)
ax.loglog(size, c * size ** 2, color='black', label='theory')
c = time[0] / (size[0] * log(size[0]))
ax.loglog(size, c * size * log(size), linestyle='--', color='black', label='theory')
ax.loglog(size, time, color='red', label='numeric')
ax.set_xlabel('size')
ax.set_ylabel('time')
ax.legend()


"""
COMMENTS:
For small sizes the time complexity appears to grow at a rate of N * log(N) but as N
gets larger the time complexity grows quadratically. This is to be expected despite the
optimizations done by the compiler, for the shift() method has a linear time complexity
and it is called almost N times.
"""
