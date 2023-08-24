#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Complexity                               August 23, 2022

source: points.py
author: @misael-diaz

Synopsis:
Plots the 2D points.

Copyright (c) 2023 Misael Diaz-Maldonado
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

from numpy import array
from matplotlib import pyplot as plt

points = [
  [-8, -8, -5, -2, +0, +0, +7, +7],
  [-7, -1, +5, +0, -2, +7, -7, +7]
]

x, y = array(points)

plt.close('all')
plt.ion()
fig, ax = plt.subplots()
ax.plot(x, y, marker = '*', markersize = 12, linestyle='')
