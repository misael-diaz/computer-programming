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

import numpy as np
import matplotlib as mpl
from matplotlib import pyplot as plt

x = np.linspace(0, 5, 100)
y = 8 - 4.5 * ( x - np.sin(x) )

plt.close('all')
plt.ion()
fig, ax = plt.subplots()    # enables multiple plots in the same figure
ax.plot(x, y)
ax.plot(x, np.zeros_like(x), linestyle='--', color='black')
ax.set_xlim([ 2, 3])
ax.set_ylim([-3, 3])
ax.set_xlabel('x')
ax.set_ylabel('f(x)')
