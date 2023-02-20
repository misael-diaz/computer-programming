#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 19, 2023

source: testClosestPair.py
author: @misael-diaz


Synopsis:
Exports the Time Complexity of Closest Pair finding algorithms.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""

from TimeComplexity import TimeComplexity as time

# Note:
# Contrary to the definition of the Time Complexity Constructor in Java, C++, etc.,
# the Python version expects two parameters: the number of runs and the number of
# repetitions. It takes too long to repeat each experiment 256 times, at least not
# a reasonable time to conduct the analysis in my PC.
t = time(4, 64)
t.exportTimeComplexity_DivideAndConquer1D()

'''
TODO:
[ ] get the experimental data from the HPCf
'''
