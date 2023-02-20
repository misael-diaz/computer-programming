#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 13, 2023

source: Pair.py
author: @misael-diaz


Synopsis:
Defines the Closest Pair Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
[2] positive-infinity: https://numpy.org/devdocs/reference/constants.html

"""


from point import copy                  # provides method for copying numeric types
from point import compare               # provides method for comparing numbers
from Point import Point                 # provides the Point Constructor, Point(x, y)
from pair import Constructor            # provides the Pair Constructor, Pair(p, q, d)
from pair import defaultConstructor     # provides the Pair Default Constructor, Pair()
from pair import copyConstructor        # provides the Pair Copy Constructor
from numpy import number                # provides numpy's numeric type


'''
class
'''


class Pair:

    # variadic constructor:

    def __init__ (this, *args):
        '''
        Synopsis:
        Implements variadic constructor for the Pair class.
        '''

        if len(args) == 0:      # default constructor Pair::Pair()

            pair = defaultConstructor()
            this._Pair__first, this._Pair__second, this._Pair__distance = pair

        elif len(args) == 1:    # copy constructor Pair::Pair(Pair)

            p = args[0]
            if ( not isinstance(p, Pair) ):
                err = ("Pair::Pair(Pair) copy constructor expects " +
                       "a Pair as argument")
                raise RuntimeError(err)

            else:

                pair = copyConstructor(p)
                this._Pair__first, this._Pair__second, this._Pair__distance = pair

        elif len(args) == 3:    # constructor Pair::Pair(Point, Point, Number)

            p, q, d = args
            numeric = (int, float, number)

            if ( not isinstance(p, Point) ):
                err = ("Pair::Pair(Point, Point, Number) expects the "+
                       "first argument to be a Point object")
                raise RuntimeError(err)

            if ( not isinstance(q, Point) ):
                err = ("Pair::Pair(Point, Point, Number) expects the "+
                       "second argument to be a Point object")
                raise RuntimeError(err)

            if ( not isinstance(d, numeric) ):
                err = ("Pair::Pair(Point, Point, Number) expects the "+
                       "third argument to be a numeric type")
                raise RuntimeError(err)

            pair = Constructor(args)
            this._Pair__first, this._Pair__second, this._Pair__distance = pair

        return


    # getters:


    def getFirst (this):
        '''
        Synopsis:
        Returns a copy of the first point that comprises the pair.
        '''
        return Point(this._Pair__first)


    def getSecond (this):
        '''
        Synopsis:
        Returns a copy of the second point that comprises the pair.
        '''
        return Point(this._Pair__second)


    def getDistance (this):
        '''
        Synopsis:
        Returns a copy of the separating distance of the points that
        comprise the pair.
        '''
        return copy(float, this._Pair__distance)


    # methods:


    def print (this):
        '''
        Synopsis:
        Prints info about the Pair object.
        '''

        print('first:')
        this._Pair__first.print()

        print('second:')
        this._Pair__second.print()

        d = this._Pair__distance
        print(f'distance: {d:.15e}')
        return


    def compareTo (this, otherPair):
        '''
        Synopsis:
        Returns 0 if equal, 1 if `this' is greater than the `other Pair',
        and -1 if `this' is less than the `other Pair'.
        '''

        d1 = this.getDistance()
        d2 = otherPair.getDistance()

        return compare(d1, d2)
