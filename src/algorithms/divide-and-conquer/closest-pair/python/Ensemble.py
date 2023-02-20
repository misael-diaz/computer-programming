#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 11, 2023

source: Ensemble.py
author: @misael-diaz


Synopsis:
Defines the Point Ensemble Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


import time                                     # for time_ns() (nanoseconds timer)
import PairUtil                                 # provides min() method for Pair objects
from Pair import Pair                           # provides Pair() constructor
from point import copy                          # provides copy() utility
from Point import Point                         # provides the Point(*) constructor
from Point import contains                      # to find Points by their coordinate data
from numpy.random import randint as random      # to generate integral random numbers
from numpy import array                         # for fast iteration of array slices


from ImplementErrorException import ImplementErrorException


class Ensemble:


    def __init__ (this, size):
        '''
        Synopsis:
        Constructs an ensemble of points of requested size.
        '''

        this._Ensemble__size = copy(int, size)  # sets the number of points
        this._Ensemble__numOperations = 0.0     # zeros the number of operations
        this._Ensemble__elapsedTime = 0.0       # zeros the elapsed-time

        return


    def getSize (this):
        '''
        Synopsis:
        Returns a copy of the ensemble size.
        '''
        return copy(float, this._Ensemble__size)


    def getElapsedTime (this):
        '''
        Synopsis:
        Returns a copy of the elapsed time invested on finding the closest pair.
        '''
        return copy(float, this._Ensemble__elapsedTime)


    def getNumOperations (this):
        '''
        Synopsis:
        Returns a copy of the number of operations invested on finding the closest pair.
        '''
        return copy(float, this._Ensemble__numOperations)


    # methods:


    def bruteForce (this):
        '''
        Synopsis:
        Applies the Brute Force Algorithm to find the closest pair.
        Returns the closest pair.
        '''

        points = this.createDataset1D()
        closestPair = this.timeRecursive1D(points)
        closestPairBruteForce = this.timeBruteForce(points)

        # complains if the closest pairs differ
        if closestPair.compareTo(closestPairBruteForce) != 0:

            print('Brute Force:')
            closestPairBruteForce.print()
            print('Divide And Conquer:')
            closestPair.print()

            err = 'Ensemble.bruteForce(): different closest pairs detected'
            raise ImplementErrorException(err)

        return closestPair


    def recursive1D (this):
        '''
        Synopsis:
        Applies the Divide And Conquer Algorithm to find the closest pair.
        Returns the closest pair.
        '''

        points = this.createDataset1D()
        closestPairBruteForce = this.timeBruteForce(points)
        closestPair = this.timeRecursive1D(points)

        # complains if the closest pairs differ
        if closestPair.compareTo(closestPairBruteForce) != 0:

            print('Brute Force:')
            closestPairBruteForce.print()
            print('Divide And Conquer:')
            closestPair.print()

            err = 'Ensemble.recusive1D(): different closest pairs detected'
            raise ImplementErrorException(err)

        return closestPair


    def timeBruteForce (this, points):
        '''
        Synopsis:
        Times the implementation of the Brute Force Algorithm.
        Sets the elapsed time (nanoseconds) and the number of operations.
        Returns the closest pair.
        '''

        this.isInvalidData(points)

        startTime = time.time_ns()
        closestPair, numOperations = this.distance(points)
        endTime = time.time_ns()

        # sets the elapsed time (nanoseconds):
        elapsedTime = (endTime - startTime)
        this._Ensemble__elapsedTime = elapsedTime

        # sets the number of operations:
        this._Ensemble__numOperations = numOperations

        return closestPair


    def timeRecursive1D (this, points):
        '''
        Synopsis:
        Times the implementation of the 1D Divide And Conquer Algorithm.
        Sets the elapsed time (nanoseconds) and the number of operations.
        Returns the closest pair.
        '''

        this.isInvalidData(points)

        startTime = time.time_ns()
        closestPair, numOperations = this.recurse(0, points.size, points)
        endTime = time.time_ns()

        # sets the elapsed time (nanoseconds):
        elapsedTime = (endTime - startTime)
        this._Ensemble__elapsedTime = elapsedTime

        # sets the number of operations:
        this._Ensemble__numOperations = numOperations

        return closestPair


    def recurse (this, beginPosition, endPosition, Px):
        '''
        Synopsis:
        Implements the 1D Divide And Conquer Algorithm that finds the closest pair.

        Inputs:
        beginPosition       beginning positional index of the virtual partition
        endPosition         ending positional index of the virtual partition

        Px                  first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 returns a tuple containing the closest pair and the number of
                            operations spent on finding the closest pair
        '''

        numel = (endPosition - beginPosition)   # gets the number of elements in partition

        if numel <= 3:
            # applies the Brute Force Algorithm on the small enough partition
            return this.distanceSmallerPartition(beginPosition, endPosition, Px)

        else:

            # divides the virtual partition into smaller left and rigth partitions:

            beginPos = beginPosition
            endPos = beginPosition + (numel // 2)
            # finds the closest pair in the left partition
            dataLeft = this.recurse(beginPos, endPos, Px)
            closestPairLeft, numOperationsLeft = dataLeft

            beginPos = beginPosition + (numel // 2)
            endPos = beginPosition + numel
            # finds the closest pair in the rigth partition
            dataRigth = this.recurse(beginPos, endPos, Px)
            closestPairRigth, numOperationsRigth = dataRigth

            # selects the closest pair from the left and rigth partitions
            closestPair = PairUtil.min(closestPairLeft, closestPairRigth)

            # finds the closest pair at the interface of the left and rigth partitions
            data = this.combine(beginPosition, endPosition, Px, closestPair)
            closestPair, numOperations = data

            # updates the number of operations executed thus far
            numOperations += numOperationsLeft + numOperationsRigth

        return (closestPair, numOperations)


    def combine (this, beginPosition, endPosition, Px, closestPair):
        '''
        Synopsis:
        Finds the closest pair at the interface of the left and rigth partitions.

        Inputs:
        beginPosition       beginning positional index of the virtual partition
        endPosition         ending positional index of the virtual partition

        Px                  first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 returns a tuple containing the closest pair and the number of
                            operations spent on finding the closest pair
        '''


        # gets the squared distance of the current closest pair
        d_min = closestPair.getDistance()


        # defines the asymmetric range that encompasses the left partition
        numel = (endPosition - beginPosition)
        beginPos = beginPosition
        endPos = beginPosition + (numel // 2)

        # assumes there are no closest pair conforming points in the left partition
        beginPosLeft = beginPosition + (numel // 2)
        endPosLeft = beginPosition + (numel // 2)

        # includes points in the left partition comprising closest pair candidates:
        # Note: traverses the left partition from back to front via negative indexing
        for p in Px[ -1 - (Px.size - endPos) : -1 - (Px.size - beginPos) : -1 ]:

            # gets the `x' position of the leftmost point in the rigth partition
            q = Px[endPos]
            x2 = q.getX()

            # gets the `x' position of the (current) rigthmost point in the left partition
            x1 = p.getX()

            # computes the squared x-axis distance of the closest pair candidate
            d = (x2 - x1) * (x2 - x1)

            if d < d_min:
                # includes the point because it can be a closest pair comprising point
                beginPosLeft -= 1
            else:
                # halts the search because the next points are farther away
                break


        # defines the asymmetric range that encompasses the rigth partition
        beginPos = beginPosition + (numel // 2)
        endPos = beginPosition + numel

        # assumes there are no closest pair conforming points in the rigth partition
        beginPosRigth = beginPosition + (numel // 2)
        endPosRigth = beginPosition + (numel // 2)

        # includes points in the rigth partition comprising closest pair candidates:
        for q in Px[ beginPos : endPos ]:

            # gets the `x' position of the rigthmost point in the left partition
            p = Px[beginPos - 1]
            x1 = p.getX()

            # gets the `x' position of the (current) leftmost point in the rigth partition
            x2 = q.getX()

            # computes the squared x-axis distance of the closest pair candidate
            d = (x2 - x1) * (x2 - x1)

            if d < d_min:
                # includes the point because it can be a closest pair comprising point
                endPosRigth += 1
            else:
                # halts the search because the next points are farther away
                break


        # finds the closest pair at the interface of the left and rigth partitions
        data = this.distancePartitionInterface(
            beginPosLeft, endPosLeft, beginPosRigth, endPosRigth, Px, closestPair
        )

        return data


    def distance (this, points):
        '''
        Synopsis:
        Implements the Brute Force Algorithm that finds the closest Pair.
        The theoretical time complexity is O(N**2), where `N' is the size (or length)
        of the array of points.

        Input:
        points              a first-rank, x-y sorted, numpy, array of Points of size equa
                            to the ensemble size

        Output:
        ret                 returns a tuple containing the closest pair and the number of
                            operations spent on finding the closest pair
        '''

        N = points.size
        closestPair = Pair()    # instantiates a Pair of infinitely separated points
        for i, p in enumerate(points[0 : N - 1]):
            for q in points[i + 1 : N]:

                d = p.distance(q)
                pair = Pair(p, q, d)
                closestPair = PairUtil.min(closestPair, pair)

        sz = float(N)
        numOperations = ( sz * (sz - 1) ) / 2

        return (closestPair, numOperations)


    def distanceSmallerPartition (this, beginPosition, endPosition, Px):
        '''
        Synopsis:
        Implements the Brute Force Algorithm that finds the closest Pair in a (smaller)
        partition delimited by the asymmetric range [beginPosition, endPosition).

        Inputs:
        beginPosition       beginning positional index of the smaller partition
        endPosition         ending positional index of the smaller partition

        Px                  first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 returns a tuple containing the closest pair and the number of
                            operations spent on finding the closest pair
        '''

        closestPair = Pair()    # instantiates a Pair of infinitely separated points
        for i, p in enumerate(Px[ beginPosition : endPosition - 1 ]):
            for q in Px[ beginPosition + (i + 1) : endPosition]:

                d = p.distance(q)
                pair = Pair(p, q, d)
                closestPair = PairUtil.min(closestPair, pair)

        numel = float(endPosition - beginPosition)
        numOperations = ( numel * (numel - 1) ) / 2

        return (closestPair, numOperations)


    def distancePartitionInterface (this, beginPosLeft, endPosLeft,
                                    beginPosRigth, endPosRigth, Px,
                                    closestPair):
        '''
        Synopsis:
        Applies the Brute Force Algorithm to find the closest pair at the interface of
        the left and rigth partitions.

        Inputs:
        beginPosLeft        beginning positional index of the smaller left partition
        endPosLeft          ending positional index of the smaller left partition

        beginPosRigth       beginning positional index of the smaller rigth partition
        endPosRigth         ending positional index of the smaller rigth partition

        Px                  first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 returns a tuple containing the closest pair and the number of
                            operations spent on finding the closest pair
        '''

        for p in Px[ beginPosLeft : endPosLeft ]:
            for q in Px[ beginPosRigth : endPosRigth ]:

                d = p.distance(q)
                pair = Pair(p, q, d)
                closestPair = PairUtil.min(closestPair, pair)

        # determines the number of distance computations including those in combine()
        N1 = float(endPosLeft - beginPosLeft)
        N2 = float(endPosRigth - beginPosRigth)
        numOperations = ( (N1 * N2) + (N1 + 1) + (N2 + 1) )

        return (closestPair, numOperations)


    # utilities:


    def createDataset1D (this):
        '''
        Synopsis:
        Creates a dataset of points that has no duplicate closest pairs; that is,
        the points that comprise the second closest pair are farther away than the
        points that comprise the first closest pair.

        Inputs:
        None

        Output:
        dataset             a first-rank, x-y sorted, numpy array of Points of size equal
                            to the ensemble size
        '''

        dataset = this.create()

        while ( this.hasDuplicateClosestPair(dataset) ):
            dataset = this.create()

        return dataset


    def hasDuplicateClosestPair (this, points):
        '''
        Synopsis:
        Returns true if the points that comprise the first and second closest pairs are
        equidistant (duplicate closest pairs); returns false otherwise.

        Input:
        points              a first-rank, x-y sorted, numpy array of Points of size equal
                            to the ensemble size

        Output:
        ret                 true if there is a duplicate closest pair, false otherwise
        '''

        # considers distinct pairs to find the first and second closest pairs:

        N = points.size
        firstClosestPair = Pair()
        secondClosestPair = Pair()
        for i, p in enumerate(points[ 0 : (N - 1) ]):
            for q in points[ (i + 1) : N ]:

                d = p.distance(q)
                pair = Pair(p, q, d)

                if pair.compareTo(firstClosestPair) <= 0:
                    secondClosestPair = firstClosestPair
                    firstClosestPair = pair

        # checks if the closest pairs have equal distances (duplicates):

        d_min = firstClosestPair.getDistance()
        d_2nd = secondClosestPair.getDistance()

        if d_min != d_2nd:
            ret = False
        else:
            ret = True

        return ret


    def isSorted (this, points):
        '''
        Synopsis:
        Returns true if the array of points is x-y sorted, false otherwise.

        Input:
        points              a first-rank, x-y sorted, numpy, array of Points of size equa
                            to the ensemble size

        Output:
        ret                 true if the array is sorted, false otherwise
        '''

        misplacements = 0
        for i in range(points.size - 1):
            # invariant: so far we checked `i' pairs for misplacement (out-of-order)

            p, q = points[i], points[i + 1]

            if p.compareTo(q) > 0:
                misplacements += 1
            else:
                misplacements += 0


        if misplacements != 0:
            return False
        else:
            return True


        return ret


    def isInvalidData (this, points):
        '''
        Synopsis:
        Complains if the array of points is not sorted or if it has duplicates.

        Input:
        points              a first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 true if the array of points is invalid, false otherwise
        '''

        if not this.isSorted(points):
            err = ('Ensemble::isInvalidData(): points must be x-y sorted')
            raise RuntimeError(err)

        if this.hasDuplicates(points):
            err = ('Ensemble::isInvalidData(): points must be distinct')
            raise RuntimeError(err)

        return


    def hasDuplicates (this, dataset):
        '''
        Synopsis:
        Returns true if the array of points has duplicates, false otherwise.

        Input:
        dataset             a first-rank, x-y sorted, numpy, array of Points of size equal
                            to the ensemble size

        Output:
        ret                 true if the array of points has duplicates, false otherwise
        '''

        points = dataset.tolist()
        # sorts (the list just in case it is not sorted) to check for duplicates in pairs
        points.sort(key = lambda p: ( p.getX(), p.getY() ), reverse=False)

        duplicates = 0
        for i in range(len(points) - 1):
            # invariant: so far we checked `i' pairs for equality (duplicate)

            p, q = points[i], points[i + 1]

            if p.compareTo(q) == 0:
                duplicates += 1
            else:
                duplicates += 0


        if duplicates != 0:
            ret = True
        else:
            ret = False


        return ret


    def create (this):
        '''
        Synopsis:
        Creates an ensemble of `N' distinct points, where `N' is the
        ensemble size, by sampling coordinates from a uniformly
        distributed Pseudo-Random Number Generator PRNG.

        Inputs:
        None

        Output:
        points              a first-rank, x-y sorted, numpy array of Points of size equal
                            to the ensemble size
        '''

        sz = copy(int , this._Ensemble__size)
        float_size = copy(float, this._Ensemble__size)
        limit = float_size
        x_min, x_max = [-limit, limit]
        y_min, y_max = [-16, 16]

        # generates an Ensemble of `size' distinct Points:

        points = []
        for i in range(sz):
            x = random(low=x_min, high=x_max, size=None, dtype=int)
            y = random(low=y_min, high=y_max, size=None, dtype=int)
            p = Point(x, y)

            while contains(points, p):
                x = random(low=x_min, high=x_max, size=None, dtype=int)
                y = random(low=y_min, high=y_max, size=None, dtype=int)
                p = Point(x, y)

            points.append(p)

        # sorts the points primarily by their x and secondarily by their y
        # coordinates to support the Divide And Conquer Algorithm

        points.sort(key = lambda p: ( p.getX(), p.getY() ), reverse=False)

        return array(points)
