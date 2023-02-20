#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 19, 2023

source: TimeComplexity.py
author: @misael-diaz


Synopsis:
Defines the Time Complexity Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from point import copy              # provides method for copying numeric types
from numpy import zeros             # numpy's zeros is used for preallocating arrays
from numpy import savetxt           # for saving arrays in a plain text file ASCII
from Ensemble import Ensemble       # provides the Ensemble Constructor, Ensemble(size)


class TimeComplexity:

    def __init__ (this, runs, reps):
        '''
        Synopsis:
        Constructs object from the number of runs (or time complexity experiments).
        '''

        this._TimeComplexity__runs = copy(int, runs)
        this._TimeComplexity__reps = copy(int, reps)

        return


    def getRuns (this):
        '''
        Synopsis:
        Returns a copy of the number of runs.
        '''

        return copy(int, this._TimeComplexity__runs)


    def getReps (this):
        '''
        Synopsis:
        Returns a copy of the number of repetitions `reps'.
        '''

        return copy(int, this._TimeComplexity__reps)


    def exportTimeComplexity_DivideAndConquer1D (this):
        '''
        Synopsis:
        Exports the time complexity of the 1D Divide And Conquer Algorithm that finds
        the closest pair to a (three-column) plain text file.
        '''

        statistics = this.timeDivideAndConquer1D().transpose()
        savetxt('timeDivideAndConquer1D.dat', statistics)

        return


    def timeDivideAndConquer1D (this):
        '''
        Synopsis:
        Times the 1D Divide And Conquer (recursive) Algorithm that finds the closest pair
        as a function of the ensemble size.

        Input:
        None

        Output:
        statistics          second-rank, numpy, array that stores the ensemble sizes,
                            the average elapsed-times (nanoseconds), and the average
                            number of operations spent by the recursive algorithm to
                            find the closest pair.
        '''

        runs = this.getRuns()                       # gets the number of experiments
        reps = this.getReps()                       # gets the number of repetitions
        s = statistics = zeros([3, runs])           # allocates the `statistics' array
        sizes, avgElapsedTimes, avgNumOperations = s# 2nd-rank array -> 1st-rank arrays
        sz = 16                                     # sets the initial ensemble size
        for run in range(runs):                     # for each time complexity experiment:

            etime = 0.0                             # zeros the elapsed-time accumulator
            opers = 0.0                             # zeros the #operations accumulator
            for rep in range(reps):                 # times the algorithm `reps' times
                ens = Ensemble(sz)                  # creates a new ensemble of size `sz'
                ens.recursive1D()                   # times the recursive algorithm
                etime += ens.getElapsedTime()       # accumulates the elapsed times
                opers += ens.getNumOperations()     # accumulates the #operations

            sizes[run] = sz                         # stores the ensemble size
            avgElapsedTimes[run] = (etime / reps)   # stores the average elapsed time
            avgNumOperations[run] = (opers / reps)  # stores the average #operations

            sz *= 2                                 # doubles the size for the next run

        return statistics
