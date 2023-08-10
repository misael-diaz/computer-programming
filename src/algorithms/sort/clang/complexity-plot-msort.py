#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms                                             August 08, 2023

source: complexity-plot-msort.py
author: @misael-diaz

Synopsis:
Plots the time complexity of the implementation of the merge sort algorithm msort().

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

size, time = loadtxt('complexity-msort.txt').transpose()

plt.close('all')
plt.ion()
fig, ax = plt.subplots()
c = time.mean() / ( size * log(size) ).mean()
ax.loglog(size, c * size * log(size), linestyle='--', color='black', label='theory')
ax.loglog(size, time, color='red', label='numeric')
ax.set_xlabel('size')
ax.set_ylabel('time')
ax.legend()
