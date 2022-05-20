#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Complexity                                      May 20, 2022
IST 3498
Prof. M Diaz-Maldonado

Synopsis:
Plots the time complexity of the implementation of the Divide and Conquer
Algorithm that solves the Closest Pair problem.


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


from numpy import loadtxt
from numpy import log2 as log
from matplotlib import pyplot as plt


filename = 'results/time.txt'
N, t = loadtxt (filename).T


plt.close('all')
plt.ion()
fig, ax = plt.subplots()
# approximates the constant proportion between the measured and theoretical
# time-complexity for ease of visualization
c = (t / ( N * log(N) ) ).mean()
ax.loglog(N, c * ( N * log(N) ), color='black')
# plots measured time-complexity
ax.loglog(N, t, color='red', linestyle='', marker='*', markersize=12)
ax.set_xlabel('size, N')
ax.set_ylabel('time, t')
ax.set_title('Time Complexity of the Divide and Conquer Algorithm')
fig.savefig('plots/time-complexity.png', dpi=300)
