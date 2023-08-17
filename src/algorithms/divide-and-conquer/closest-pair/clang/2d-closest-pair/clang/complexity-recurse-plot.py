#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Complexity                                       August 17, 2023

source: complexity-recurse-plot.py
author: @misael-diaz

Synopsis:
Plots the time complexity of the implementation of the recursive closest pair finding
algorithm.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
[1] JJ McConnell, Analysis of Algorithms, second edition.
"""

from numpy import loadtxt
from numpy import log2 as log
from matplotlib import pyplot as plt

size, time = loadtxt('complexity-recurse.txt').transpose()

plt.close('all')
plt.ion()
fig, ax = plt.subplots()
c = time[-1] / (size[-1] * log(size[-1])**2)
ax.loglog(size, c * size * log(size)**2, linestyle='--', color='black', label='theory')
ax.loglog(size, time, color='red', label='numeric')
ax.set_xlabel('size')
ax.set_ylabel('time')
ax.legend()
