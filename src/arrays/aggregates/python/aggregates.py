#!/usr/bin/env python3
# -*- coding: utf-8 -*-
syn="""
Algorithms and Programming II                             February 10, 2022
IST 2048
Prof. M. Diaz-Maldonado

Synopsis:
Code shows how to obtain the total, minimum and maximum value, and
the average of the values of an array in Python.

Sinopsis:
Muestra como obtener el total, el minimo y maximo, y el promedio de los
valores de un arreglo en Python.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition.
[1] The Style Guide for Python Code: https://pep8.org/
"""

from numpy import arange

"""
Sum of Squares in the asymmetric range [0, 256)
"""
n = arange(256)
sn = n**2

# uses f-string to display answer on the console
ans = (
    f"\n\n"
    f"Sum of squares in [0, 256):\n"
    f"min  : {sn.min()}\n"
    f"max  : {sn.max()}\n"
    f"mean : {sn.mean()}\n"
    f"total: {sn.sum()}\n"
    f"\n\n"
)
print(syn)
print(ans)
