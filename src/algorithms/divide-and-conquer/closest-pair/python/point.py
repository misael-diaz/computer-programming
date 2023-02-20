#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 09, 2023

source: point.py
author: @misael-diaz


Synopsis:
Implements utilities used by the base and Point 3D classes.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] numeric built-in types: https://docs.python.org/3/library/stdtypes.html
[1] isnumeric: https://stackoverflow.com/questions/500328/identifying-numeric-and-array-types-in-numpy
[2] isnumeric: https://stackoverflow.com/questions/11204789/how-to-properly-use-pythons-isinstance-to-check-if-a-variable-is-a-number
[3] JJ McConnell, Analysis of Algorithms, 2nd edition
[4] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from numpy import array     # for creating copies of primitive types
from numpy import number    # for type checking of numeric types


def isNumeric (x):
    '''
    Synopsis:
    Complains if `x' is not of a supported numeric type (excludes
    complex types).
    '''

    numeric = (int, float, number)
    if ( not isinstance(x, numeric) ):
        err = ("Point::Point() constructor expects scalar " +
               "numeric types as arguments")
        raise RuntimeError(err)

    return


def copy (numericType, num):
    '''
    Synopsis:
    Creates a copy of the number `num' in the requested the numeric type.
    '''

    # checks if `num' is a supported numeric type number
    numeric = (int, float, number)
    if ( not isinstance(num, numeric) ):
        err = "Point::copy() expects the second argument to be a number"
        raise RuntimeError(err)

    # checks if the passed type `numericType' is a supported numeric type
    if numericType == int or numericType == float or numericType == number:

        # cast the number to the requested numeric type
        ret = array([num]).astype(numericType)[0]

    else:
        # otherwise complain to the user about the error
        err = ('Point::copy(): expects the first argument to be a ' +
               'supported numeric type: `int`, `float`, or `numpy.number`')
        raise RuntimeError(err)

    return ret


def isInvalid (args):
    '''
    Synopsis:
    Complains if any of the arguments is not a supported numeric type.
    '''

    for arg in args:
        isNumeric(arg)

    return


def defaultConstructor ():
    '''
    Synopsis:
    Implements the default-constructor Point::Point(), sets the (x, y)
    coordinates at the origin.
    '''

    ret = x, y = array([0, 0]).astype(float)

    return ret


def Constructor (args):
    '''
    Synopsis:
    Implements the base Point::Point(x, y) constructor,
    which sets the (x, y) fields.
    '''

    isInvalid(args)
    ret = x, y = array(args).astype(float)

    return ret


def copyConstructor (p):
    '''
    Synopsis:
    Implements the base Point::Point(Point) copy constructor.
    '''

    ret = x, y = array([p.getX(), p.getY()]).astype(float)

    return ret


def ConstructorPoint3D (args):
    '''
    Synopsis:
    Implements the base Point3D::Point3D(x, y, z) constructor.
    '''

    isInvalid(args)
    ret = z = array(args).astype(float)[0]

    return ret


def compare (x1, x2):
    '''
    Synopsis:
    Compares numeric types.
    Returns 0 if x1 == x2, 1 if x1 > x2, and -1 if x1 < x2.
    '''

    if (x1 == x2):
        ret = 0
    elif (x1 > x2):
        ret = 1
    else:
        ret = -1

    return ret


def contains (points, target):
    '''
    Synopsis:
    Returns true if the `target' Point is in the list of points, false otherwise.
    '''

    pos = search(points, target)

    if (pos < 0):
        ret = False
    else:
        ret = True

    return ret


def search (points, target):
    '''
    Synopsis:
    Implements linear search.
    Returns the positional index `i' of the `target' Point in the list
    if present, otherwise returns the invalid index of -1.
    '''

    t = target

    for i, p in enumerate(points):

        if p.compareTo(t) == 0:
            return i

    return -1
