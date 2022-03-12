#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Solvers                                                      March 12, 2022
Prof. M Diaz Maldonado

Synopsis:
Plots the function f(x) = 8 – 4.5 (x – sin x) and its first derivative
f'(x) in a range where f(x) undergoes a sign change.

Sinopsis:
Grafica la funcion f(x) = 8 – 4.5 (x – sin x) y su primera derivada f'(x)
en un rango donde f(x) muestra un cambio de signo.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
[1] The Style Guide for Python Code: https://pep8.org/
[2] f-string: realpython.com/python-f-strings/
[3] symbolic math: www.sympy.org/en/index.html
"""


from sympy import sin
from sympy import Symbol
from sympy import diff as D
from sympy import lambdify
from numpy import linspace
from numpy import zeros_like
import matplotlib as mpl
from matplotlib import pyplot as plt


""" creates symbols """
x = Symbol('x')                     # defines `x' as a symbol
y = 8 - 4.5 * ( x - sin(x) )        # y = f(x), also a symbol


""" defines lambdas for f(x) and f'(x) """
f = lambdify(x, y, 'numpy')         # y = f(x)
g = lambdify(x, D(y, x), 'numpy')   # g = f'(x) (Note: D(y, x) = dy/dx)


""" plotting """
xs = linspace(0, 5, 100)            # sets the range of values for plotting

plt.close('all')
plt.ion()
fig, ax = plt.subplots()                                  # multi-line plot
ax.plot(xs, f(xs), label = f"f(x) = {y}")                 # plots f(x)
ax.plot(xs, g(xs), label = f"f'(x) = {D(y, x)}")          # plots f'(x)
ax.plot(xs, zeros_like(xs), linestyle='--', color='black')# horizontal line
# sets x and y axes labels
ax.set_xlabel('x')
ax.set_ylabel('y')
# sets the title
ax.set_title("The function f(x) and its first derivative f'(x)")
# shows the grid lines
ax.grid()
# displays the legend
ax.legend()

"""
COMMENTS:
[0] The math function sin(x) is imported from sympy instead of
    math or numpy modules.
[1] Sympy's lambdify enable us to create functions that can be evaluated
    numerically just like any other functions we have defined thus far.
[2] Note the use of the f-strings to define the plot labels.
"""
