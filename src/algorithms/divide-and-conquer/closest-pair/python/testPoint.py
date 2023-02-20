#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 12, 2023

source: testPoint.py
author: @misael-diaz


Synopsis:
Tests the implementation of the Point Class.


Copyrigth (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] JJ McConnell, Analysis of Algorithms, 2nd edition
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from Point import Point     # provides the variadic Point(*) constructor
from Point import Point3D   # provides the variadic Point3D(*) constructor


# tests:


def testDefaultConstructor ():

    # invokes the default constructor to create a Point at the origin (0, 0, 0)
    p = Point()

    x, y, z = p.getX(), p.getY(), p.getZ()

    if x != 0.0:
        err = 'Point::Point() default constructor failed to set x to zero'
        raise RuntimeError(err)

    if y != 0.0:
        err = 'Point::Point() default constructor failed to set y to zero'
        raise RuntimeError(err)

    if z != 0.0:
        err = 'Point::Point() default constructor failed to set z to zero'
        raise RuntimeError(err)

    print('Point::Point():\t\t\tPASS')

    return


def testConstructor ():

    xi, yi = 0.5, -0.5
    # invokes the constructor to create a Point at the origin (xi, yi)
    p = Point(xi, yi)

    x, y, z = p.getX(), p.getY(), p.getZ()

    if x != xi:
        err = 'Point::Point(x, y) constructor failed to set x'
        raise RuntimeError(err)

    if y != yi:
        err = 'Point::Point(x, y) constructor failed to set y'
        raise RuntimeError(err)

    if z != 0.0:
        err = 'Point::Point(x, y) constructor failed to set z to zero'
        raise RuntimeError(err)

    print('Point::Point(x, y):\t\tPASS')

    return


def testCopyConstructor ():

    point = Point()     # constructs a point at the origin (0, 0)
    p = Point(point)    # constructs a copy


    # checks that the Points (`point' and `p') are different objects:


    if point is p:
        err = ('Point::Point(Point) copy constructor failed to create a ' +
               'new Point object')
        raise RuntimeError(err)


    # checks that the coordinate data is consistent:


    if point.getX() != p.getX():
        err = ('Point::Point(Point) copy constructor failed to copy the ' +
               'x coordinate')
        raise RuntimeError(err)


    if point.getY() != p.getY():
        err = ('Point::Point(Point) copy constructor failed to copy the ' +
               'y coordinate')
        raise RuntimeError(err)


    if point.getZ() != p.getZ():
        err = ('Point::Point(Point) copy constructor failed to copy the ' +
               '(zero-valued) z coordinate')
        raise RuntimeError(err)


    print('Point::Point(Point):\t\tPASS')


    return


def testDefaultConstructorPoint3D ():

    # invokes the default constructor to create a Point at the origin (0, 0, 0)
    p = Point3D()

    x, y, z = p.getX(), p.getY(), p.getZ()

    if x != 0.0:
        err = 'Point3D::Point3D() default constructor failed to zero x'
        raise RuntimeError(err)

    if y != 0.0:
        err = 'Point3D::Point3D() default constructor failed to zero y'
        raise RuntimeError(err)

    if z != 0.0:
        err = 'Point3D::Point3D() default constructor failed to zero z'
        raise RuntimeError(err)

    print('Point3D::Point3D():\t\tPASS')

    return


def testCopyConstructorPoint3D ():

    point = Point3D()       # constructs a point at the origin (0, 0)
    p = Point3D(point)      # constructs a copy


    # checks that the Points (`point' and `p') are different objects:


    if point is p:
        err = ('Point3D::Point3D(Point3D) copy constructor failed to ' +
               'create a new Point object')
        raise RuntimeError(err)


    # checks that the coordinate data is consistent:


    if point.getX() != p.getX():
        err = ('Point3D::Point3D(Point3D) copy constructor failed to ' +
               'copy the x coordinate')
        raise RuntimeError(err)


    if point.getY() != p.getY():
        err = ('Point3D::Point3D(Point3D) copy constructor failed to ' +
               'copy the y coordinate')
        raise RuntimeError(err)


    if point.getZ() != p.getZ():
        err = ('Point3D::Point3D(Point3D) copy constructor failed to ' +
               'copy the z coordinate')
        raise RuntimeError(err)


    print('Point3D::Point3D(Point3D):\tPASS')


    return


def testConstructorPoint3D ():

    xi, yi, zi = 0.5, -0.5, 1.0
    # invokes the constructor to create a Point at the origin (xi, yi)
    p = Point3D(xi, yi, zi)

    x, y, z = p.getX(), p.getY(), p.getZ()

    if x != xi:
        err = 'Point3D::Point3D(x, y, z) constructor failed to set x'
        raise RuntimeError(err)

    if y != yi:
        err = 'Point3D::Point3D(x, y, z) constructor failed to set y'
        raise RuntimeError(err)

    if z != zi:
        err = 'Point3D::Point3D(x, y, z) constructor failed to set z'
        raise RuntimeError(err)

    print('Point3D::Point3D(x, y, z):\t\tPASS')

    return


def testDistance ():

    x1, y1 =  0.5,  0.5
    x2, y2 = -0.5, -0.5

    p = Point(x1, y1)
    q = Point(x2, y2)

    d = 2.0
    # checks the computed distance against the exact
    if p.distance(q) != d:
        err = 'Point::distance(): implementation error'
        raise RuntimeError(err)

    x1, y1, z1 =  0.5,  0.5,  0.5
    x2, y2, z2 = -0.5, -0.5, -0.5


    p = Point3D(x1, y1, z1)
    q = Point3D(x2, y2, z2)

    d = 3.0
    # checks the computed distance against the exact
    if p.distance(q) != d:
        err = 'Point3D.distance(Point): implementation error'
        raise RuntimeError(err)

    print('Point.distance(Point):\t\tPASS')
    print('Point3D.distance(Point3D):\tPASS')

    return


def tests ():

    testDefaultConstructor()
    testConstructor()
    testCopyConstructor()

    testDefaultConstructorPoint3D()
    testCopyConstructorPoint3D()

    testDistance()

    return


# main:


tests()
