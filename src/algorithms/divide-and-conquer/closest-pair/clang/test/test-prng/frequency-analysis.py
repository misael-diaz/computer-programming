#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                                February 23, 2023

source: frequency-analysis.py
author: @misael-diaz

Synopsis:
Plots histogram of the pseudo-random numbers generated in the arange [low, high).
The plot shows that the pseudo-random numbers are uniformly distributed. The height
of the density probability of the normalized pseudo-random numbers [0, 1) is close
to one, as shown by the post-processing computations.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.

References:
[0] R. Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""

import matplotlib as mpl
from matplotlib import pyplot as plt
from numpy import loadtxt

# gets the pseudo-random numbers `x' and their frequency `count'
x, count = data = loadtxt('data/frequency.dat').transpose()

plt.close('all')
plt.ion()

n, bins, patches = plt.hist(x, density=True)    # plots histogram in [low, high)

# post-processing:
# Estimates the area of the Probability Density Function PDF (1.0)

binWidth = 1
numBins = count.size
area = binWidth * count.sum()
normalized_binHeights = (count / area)
normalized_avgBinHeight = normalized_binHeights.mean()
area_pdf = numBins * normalized_avgBinHeight

print(f'uniformly distributed: {area_pdf}')

# Notes:
# The area of the PDF must be close to one for the Pseudo Random Number Generator PRNG
# to be uniformly distributed.
