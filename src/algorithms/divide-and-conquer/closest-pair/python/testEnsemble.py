#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 15, 2023

source: testEnsemble.py
author: @misael-diaz


Synopsis:
Tests the Ensemble Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from Ensemble import Ensemble       # provides the Ensemble constructor, Ensemble(size)


runs = 4
reps = 16

size = 16
# Note:
# The code complains if there is an implementation error, meaning that the
# Closest Pair found by the Brute Force and Divide and Conquer Algorithms differ.
for run in range(runs):
    for rep in range(reps):
        ens = Ensemble(size)
        closestPair = ens.recursive1D()
    size *= 2

elapsedTime, numOperations = ens.getElapsedTime(), ens.getNumOperations()
print('Recursive:')
closestPair.print()
out = (
        f'elapsed-time: {elapsedTime}\n'
        f'#operations:  {numOperations}\n'
)
print(out)
