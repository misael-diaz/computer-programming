#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Solvers                                                   February 28, 2022
Prof. M Diaz Maldonado

Synopsis:
Plots the function f(x) = 8 – 4.5 (x – sin x) in a range where f(x)
undergoes a sign change.

Sinopsis:
Grafica la funcion f(x) = 8 – 4.5 (x – sin x) en un rango donde f(x)
muestra un cambio de signo.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""


from numpy import sin
from numpy import array
from numpy import linspace
from numpy import zeros_like
import matplotlib as mpl
from matplotlib import pyplot as plt


# defines x as an array with values in the range [0, 5] for plotting
x = linspace(0, 5, 100)
# defines the nonlinear function f(x) as a lambda (or anonymous) function
f = lambda x: 8 - 4.5 * ( x - sin(x) )
# evaluates the function
y = f(x)


# defines possible bracketing interval (lower and upper bounds)
lb, ub = (2.4, 2.5)
# stores the bracketing interval in an array for plotting
xi = array([lb, ub])
yi = f(xi)


plt.close('all')
plt.ion()
fig, ax = plt.subplots()    # enables multiple plots in the same figure
# plots the function f(x)
ax.plot(x, y, label='f(x)')
# plots the bracketing interval
ax.plot(xi, yi, linestyle='', color='red', marker='o', label='bracketing')
# plots a zero line (optional)
ax.plot(x, zeros_like(x), linestyle='--', color='black')
# sets limits for the axes
ax.set_xlim([ 2, 3])
ax.set_ylim([-3, 3])
# sets labels for the axes
ax.set_xlabel('x')
ax.set_ylabel('f(x)')
# shows the grid
ax.grid()
# shows the legend
ax.legend()

# exports graph in PNG format with 300 DPI
fig.savefig('graph.png', dpi=300)
