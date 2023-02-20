#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 14, 2023

source: testPair.py
author: @misael-diaz


Synopsis:
Tests the implementation of the Pair Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from numpy import Inf       # provides IEEE binary representation of positive infinity
from Pair import Pair       # provides the variadic Pair(*) constructor
from Point import Point     # provides the variadic Point(*) constructor


# tests:


def testDefaultConstructor ():

    # invokes the default constructor to create a Point at the origin (0, 0, 0)
    pair = Pair()
    p, q = pair.getFirst(), pair.getSecond()
    d = pair.getDistance()

    if p is q or p.compareTo(q) == 0:
        err = 'Pair::Pair() default constructor failed to generate distinct points'
        raise RuntimeError(err)

    if d != Inf:
        err = 'Point::Point() default constructor failed to set the distance'
        raise RuntimeError(err)

    print('Pair::Pair():\t\t\t\tPASS')

    return


def testConstructor ():

    x, y = 0.5, 0.5
    p = Point( x,  y)
    q = Point(-x, -y)
    d = p.distance(q)

    pair = Pair(p, q, d)

    if q.compareTo( pair.getFirst() ) != 0:
        err = ('Pair::Pair(Point, Point, Number) constructor failed to ' +
               'set the smallest Point as the `first` Point')
        raise RuntimeError(err)

    if p.compareTo( pair.getSecond() ) != 0:
        err = ('Pair::Pair(Point, Point, Number) constructor failed to ' +
               'set the largest Point as the `second` Point')
        raise RuntimeError(err)

    if d != pair.getDistance():
        err = ('Pair::Pair(Point, Point, Number) constructor failed to ' +
               'the distance')
        raise RuntimeError(err)

    print('Pair::Pair(Point, Point, Number):\tPASS')

    return


def testCopyConstructor ():

    pair = Pair()       # constructs the default pair object
    p = Pair(pair)      # constructs a copy


    # checks that the Pairs (`pair' and `p') are different objects:


    if pair is p:
        err = ('Pair::Pair(Pair) copy constructor failed to create a ' +
               'new Pair object')
        raise RuntimeError(err)


    # checks that the data is consistent:


    if pair.getFirst().compareTo( p.getFirst() ) != 0:
        err = ('Pair::Pair(Pair) copy constructor failed to copy the ' +
               'data of the first point that comprises the pair')
        raise RuntimeError(err)


    if pair.getSecond().compareTo( p.getSecond() ) != 0:
        err = ('Pair::Pair(Pair) copy constructor failed to copy the ' +
               'data of the second point that comprises the pair')
        raise RuntimeError(err)


    if pair.getDistance() != p.getDistance():
        err = ('Pair::Pair(Pair) copy constructor failed to copy the ' +
               'separating distance of the pair')
        raise RuntimeError(err)


    # checks for aliasing:


    if pair.getFirst() is p.getFirst():
        err = ('Pair::Pair(Pair) copy constructor failed to construct ' +
               'a copy of the first point that comprises the pair')
        raise RuntimeError(err)


    if pair.getSecond() is p.getSecond():
        err = ('Pair::Pair(Pair) copy constructor failed to construct ' +
               'a copy of the second point that comprises the pair')
        raise RuntimeError(err)


    print('Pair::Pair(Pair):\t\t\tPASS')


    return


def testCompareTo ():

    # creates a pair comprised by points separated by a unit distance
    p = Point()
    q = Point(1, 0)
    d = p.distance(q)
    p = Pair(p, q, d)

    # creates a pair comprised by infinitely separated points
    pair = Pair()

    if pair.compareTo(p) <= 0:
        err = ('Pair::compareTo(): implementation error')
        raise RuntimeError(err)

    print('Pair.compareTo(Pair):\t\t\tPASS')

    return


def tests ():

    testDefaultConstructor()
    testConstructor()
    testCopyConstructor()

    testCompareTo()

    return


# main:


tests()
