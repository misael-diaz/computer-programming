! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines the Point Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!
!
! References:
! [0] SJ Chapman, FORTRAN for Scientists and Engineers, fourth edition

module PointClass
use, intrinsic :: iso_fortran_env, only: int32, real64
use :: Algorithm, only: compare
implicit none
private
save
public :: Point_tests
public :: Point_insertionSort


    type, public :: Point               ! Cartesian 2D Point Class
        private                         ! Components:
        real(kind = real64) :: x        ! x-axis coordinate
        real(kind = real64) :: y        ! y-axis coordinate
        contains
            private
            procedure :: Point_getZ
            procedure :: Point_print
            procedure :: Point_compareTo
            procedure :: Point_distance
            procedure :: Point_assignment
            procedure, public :: getX
            procedure, public :: getY
            procedure, public :: getZ => Point_getZ
            procedure, public :: print => Point_print
            procedure, public :: compareTo => Point_compareTo
            procedure, public :: distance => Point_distance
            generic, public :: assignment(=) => Point_assignment
    end type Point


    type, extends(Point), public :: Point3D
        private
        real(kind = real64) :: z        ! z-axis coordinate
        contains
            private
            procedure :: Point3D_print
            procedure :: Point3D_getZ
            procedure :: Point3D_compareTo
            procedure :: Point3D_distance
            procedure, public :: getZ => Point3D_getZ
            procedure, public :: print => Point3D_print
            procedure, public :: compareTo => Point3D_compareTo
            procedure, public :: distance => Point3D_distance
    end type Point3D


    ! Procedure Interfaces:


    interface Point_tests
        module procedure tests
    end interface


    interface Point_insertionSort
        module procedure InsertionSort
    end interface


    ! Constructors:


    interface Point
        module procedure defaultConstructor
        module procedure PointConstructor
        module procedure Point3DConstructor
        module procedure copyConstructor
    end interface


    interface

        module function defaultConstructor () result(this)
        ! constructs a Point located at the origin (0, 0)
            class(Point), pointer :: this
        end function

        module function PointConstructor (x, y) result(this)
        ! constructs a Point from x, y coordinates
            class(Point), pointer :: this
            real(kind = real64), intent(in) :: x
            real(kind = real64), intent(in) :: y
        end function

        module function Point3DConstructor (x, y, z) result(this)
        ! constructs a 3D Point from x, y, z coordinates
            class(Point), pointer :: this
            real(kind = real64), intent(in) :: x
            real(kind = real64), intent(in) :: y
            real(kind = real64), intent(in) :: z
        end function

        module function copyConstructor (pointObject) result(this)
        ! constructs a copy of a Point object
            class(Point), intent(in) :: pointObject
            class(Point), pointer :: this
        end function

    end interface


    ! Operators:


    interface

        module subroutine Point_assignment (this, otherPoint)
        ! implements the assignment operation for  Point objects
            class(Point), intent(out) :: this
            class(Point), intent(in) :: otherPoint
        end subroutine

    end interface



    ! Getters:


    interface

        module pure function getX (this) result(x)
        ! returns a copy of the x-axis coordinate
            class(Point), intent(in) :: this
            real(kind = real64) :: x
        end function


        module pure function getY (this) result(y)
        ! returns a copy of the y-axis coordinate
            class(Point), intent(in) :: this
            real(kind = real64) :: y
        end function


        module pure function Point_getZ (this) result(z)
        ! returns a copy of the z-axis coordinate (returns zero for 2D Point objects)
            class(Point), intent(in) :: this
            real(kind = real64) :: z
        end function


        module pure function Point3D_getZ (this) result(z)
        ! returns a copy of the z-axis coordinate
            class(Point3D), intent(in) :: this
            real(kind = real64) :: z
        end function

    end interface


    ! Methods:


    interface

        module subroutine Point_print (this)
        ! prints the components of the Point object on the console
            class(Point), intent(in) :: this
        end subroutine

        module subroutine Point3D_print (this)
        ! prints the components of the 3D Point object on the console
            class(Point3D), intent(in) :: this
        end subroutine


        module pure function Point_compareTo (this, pointObject) result(comp)
        ! defines what it means to compare  Point objects
            class(Point), intent(in) :: this
            class(Point), intent(in) :: pointObject
            integer(kind = int32) :: comp
        end function


        module pure function Point3D_compareTo (this, pointObject) result(comp)
        ! defines what it means to compare 3D Point objects
            class(Point3D), intent(in) :: this
            class(Point), intent(in) :: pointObject
            integer(kind = int32) :: comp
        end function


        module pure function Point_distance (this, pointObject) result(dist)
        ! computes the squared distance between two Point objects
            class(Point), intent(in) :: this
            class(Point), intent(in) :: pointObject
            real(kind = real64) :: dist
        end function


        module pure function Point3D_distance (this, pointObject) result(dist)
        ! computes the squared distance between two 3D Point objects
            class(Point3D), intent(in) :: this
            class(Point), intent(in) :: pointObject
            real(kind = real64) :: dist
        end function

    end interface


    ! Utils


    interface

        module subroutine InsertionSort (N, points)
            class(Point), intent(inout) :: points(:)
            integer(kind = int32), intent(in) :: N
        end subroutine

    end interface


    ! Tests:

    interface

        module subroutine tests ()
        end subroutine

        module subroutine testInsertionSort ()
        end subroutine

    end interface

end module PointClass
