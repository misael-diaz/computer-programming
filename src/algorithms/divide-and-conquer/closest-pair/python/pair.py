#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 14, 2023

source: pair.py
author: @misael-diaz


Synopsis:
Implements utilities for the Closest Pair Class.


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


from numpy import Inf
from point import copy
from Point import Point


def defaultConstructor ():
    '''
    Synopsis:
    Constructs a Pair comprising infinitely separated points.
    '''

    first = Point()
    second = Point(Inf, 0)
    distance = Inf

    return (first, second, distance)


def Constructor (args):
    '''
    Synopsis:
    Constructs a Pair from the Points `P' and `Q' and their separating distance `d'.
    By design the smallest Point is stored first.
    '''

    p, q, d = args

    if p.compareTo(q) < 0:

        first = Point(p)
        second = Point(q)

    else:

        first = Point(q)
        second = Point(p)

    distance = copy(float, d)

    return (first, second, distance)


def copyConstructor (pair):
    '''
    Synopsis:
    Constructs a copy of the Pair object.
    '''

    first = pair.getFirst()
    second = pair.getSecond()
    distance = pair.getDistance()

    return (first, second, distance)


def min (first, second):
    '''
    Synopsis:
    Returns a reference to the smallest of the passed Pair objects.
    '''

    if first.compareTo(second) < 0:
        ret = first
    else:
        ret = second

    return ret
