#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Closest Pair Lab                                          February 09, 2023

source: Point.py
author: @misael-diaz


Synopsis:
Defines the base and the Point 3D Classes.


Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] numeric built-in types: https://docs.python.org/3/library/stdtypes.html
[1] isnumeric: https://stackoverflow.com/questions/500328/identifying-numeric-and-array-types-in-numpy
[2] isnumeric: https://stackoverflow.com/questions/11204789/how-to-properly-use-pythons-isinstance-to-check-if-a-variable-is-a-number
[3] classes: https://docs.python.org/3/tutorial/classes.html
[4] classes: https://www.w3schools.com/python/python_classes.asp
[5] overloading constructors: https://www.delftstack.com/howto/python/overload-constructors-in-python/
[6] JJ McConnell, Analysis of Algorithms, 2nd edition
[7] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition

"""


from point import copy                  # provides method for copying numeric types
from point import compare               # provides method for comparing numbers
from point import Constructor           # provides Point Constructor, Point(x, y)
from point import copyConstructor       # provides Point Copy Constructor, Point(Point)
from point import defaultConstructor    # provides Point Default Constructor, Point()
from point import ConstructorPoint3D    # provides Point3D Constructor, Point3D(x, y, z)
from point import contains              # provides linear search method for Point lists


'''
classes
'''


class Point:

    # variadic constructor:

    def __init__ (this, *args):
        '''
        Synopsis:
        Implements variadic constructor for the base Point class.
        '''

        if len(args) == 0:      # default constructor Point::Point()

            this._Point__x, this._Point__y = defaultConstructor()

        elif len(args) == 1:    # copy constructor Point::Point(Point)

            p = args[0]
            if not isinstance(p, Point):
                err = ("Point::Point(Point) copy constructor expects " +
                       "a Point as argument")
                raise RuntimeError(err)

            else:

                this._Point__x, this._Point__y = copyConstructor(p)

        elif len(args) == 2:    # constructor Point::Point(Number, Number)

            this._Point__x, this._Point__y = Constructor(args)

        else:                   # undefined constructor

            err = ("Point::Point() constructor expects none, one, or two " +
                   "arguments")
            raise RuntimeError(err)

        return


    # getters:


    def getX (this):
        '''
        Synopsis:
        Returns a copy of the x coordinate.
        '''
        return copy(float, this._Point__x)


    def getY (this):
        '''
        Synopsis:
        Returns a copy of the y coordinate.
        '''
        return copy(float, this._Point__y)


    def getZ (this):
        '''
        Synopsis:
        Returns the z coordinate (zero).
        '''
        return 0.0


    # methods:


    def print (this):
        '''
        Synopsis:
        Prints the (x, y) coordinates of point on the console.
        '''
        x, y = this.getX(), this.getY()
        print(f'{x:+.15e}, {y:+.15e}')
        return


    def compareTo (this, otherPoint):
        '''
        Synopsis:
        Returns 0 if equal, 1 if `this' is greater than the `other Point',
        and -1 if `this' is less than the `other Point'.
        '''

        x1, y1 = this.getX(), this.getY()
        x2, y2 = otherPoint.getX(), otherPoint.getY()

        if (x1 != x2):
            ret = compare(x1, x2)
        else:
            ret = compare(y1, y2)

        return ret


    def distance (this, otherPoint):
        '''
        Synopsis:
        Computes the separating distance of `this' and the other Point.
        '''

        x1, y1 = this.getX(), this.getY()
        x2, y2 = otherPoint.getX(), otherPoint.getY()

        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)



class Point3D(Point):

    # variadic constructor:

    def __init__ (this, *args):


        if len(args) == 0:      # default constructor Point3D::Point3D()

            this._Point__x, this._Point__y = defaultConstructor()
            this._Point3D__z = ConstructorPoint3D([0])

        elif len(args) == 1:    # copy constructor Point3D::Point3D(Point3D)

            p = args[0]
            if not isinstance(p, Point3D):
                err = ("copy constructor Point3D::Point3D(Point3D) " +
                       "expects a 3D Point as argument")
                raise RuntimeError(err)

            else:

                # constructs the base Point components
                this._Point__x, this._Point__y = copyConstructor(p)
                # constructs the 3D Point component
                this._Point3D__z = p.getZ()

        elif len(args) == 3:    # constructor Point3D::Point3D(float, float, float)

            x, y, z = args
            # constructs the base Point components
            this._Point__x, this._Point__y = Constructor([x, y])
            # constructs the 3D Point component
            this._Point3D__z = ConstructorPoint3D([z])

        else:                   # undefined constructor

            err = ("Point3D::Point3D() constructor expects " +
                   "none, one, or three arguments");
            raise RuntimeError(err)

        return


    # getters:


    #@Override
    def getZ (this):
        '''
        Synopsis:
        Returns a copy of the z coordinate.
        '''
        return copy(float, this._Point3D__z)


    # methods:


    #@Override
    def print (this):
        '''
        Synopsis:
        Prints the (x, y, z) coordinates of the 3D Point on the console.
        '''
        x, y, z = this.getX(), this.getY(), this.getZ()
        print(f'{x:+.15e}, {y:+.15e}, {z:+.15e}')
        return


    #@Override
    def compareTo (this, otherPoint):
        '''
        Synopsis:
        Returns 0 if equal, 1 if `this' is greater than the `other Point',
        and -1 if `this' is less than the `other Point'.
        '''

        x1, y1, z1 = this.getX(), this.getY(), this.getZ()
        x2, y2, z2 = otherPoint.getX(), otherPoint.getY(), otherPoint.getZ()

        if (x1 != x2):

            ret = compare(x1, x2)

        else:

            if (y1 != y2):
                ret = compare(y1, y2)
            else:
                ret = compare(z1, z2)

        return ret


    #@Override
    def distance (this, otherPoint):
        '''
        Synopsis:
        Computes the separating distance of `this' and the other Point.
        '''

        x1, y1, z1 = this.getX(), this.getY(), this.getZ()
        x2, y2, z2 = otherPoint.getX(), otherPoint.getY(), otherPoint.getZ()

        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1)
