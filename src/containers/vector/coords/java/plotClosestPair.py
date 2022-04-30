#!/usr/bin/env python3
# -*- coding: utf-8 -*-
syn="""
Algorithms and Programming II                                April 30, 2022
IST 2089
Prof. M Diaz-Maldonado

Synopsis:
Plots the coordinates of the closest pair.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition, 2007.
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""


from numpy import loadtxt
from matplotlib import pyplot as plt


""" imports datasets """
coords = loadtxt('coordinates.txt')
closestPair = loadtxt('closest-pair.txt')


""" plots the coordinates for visualization """

print(syn)
plt.close('all')
plt.ion()
fig, ax = plt.subplots()
# unpacks all coordinates
x, y = coords.T
# marks all the points in black symbols
ax.plot(x, y, marker='*', color='black', linestyle='')
# unpacks coordinates of the closest pair
x, y = closestPair.T
# marks the closest pair with a red symbol
ax.plot(x, y, marker='*', color='red', linestyle='')
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_title('Closest Pair')
