! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines the Closest Pair Class.
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

module PairClass
use, intrinsic :: iso_fortran_env, only: int32, real64
use :: ieee_arithmetic, only: ieee_value, ieee_positive_inf
use :: Algorithm, only: compare
use :: PointClass, only: Point
implicit none
private
save

    type, public :: Pair
        private
        ! the (smaller) first point that comprises the Pair of points
        class(Point), pointer :: first => null()
        ! the (larger) second point that comprises the Pair of points
        class(Point), pointer :: second => null()
        ! the (squared) distance between the points
        real(kind = real64) :: distance = 0.0_real64
        contains
            private
            procedure :: assignmentOperator
            procedure, public :: getDistance
            procedure, public :: print
            procedure, public :: compareTo
            generic, public :: assignment(=) => assignmentOperator
            final :: destructor
    end type


    ! Constructors:


    interface Pair
        module procedure defaultConstructor
        module procedure Constructor
        module procedure copyConstructor
    end interface


    interface

        module function defaultConstructor () result(this)
        ! constructs a Pair object from two infinitely separated points
            type(Pair), pointer :: this
        end function

        module function Constructor (p, q, d) result(this)
        ! constructs from a pair of Point objects and their separating distance
            type(Pair), pointer :: this
            class(Point), pointer, intent(in) :: p
            class(Point), pointer, intent(in) :: q
            real(kind = real64), intent(in) :: d
        end function

        module function copyConstructor (pairObject) result(this)
        ! constructs a Pair from another Pair object
            type(Pair), intent(in) :: pairObject
            type(Pair), pointer :: this
        end function

    end interface


    ! Destructor:


    interface

        module subroutine destructor (this)
        ! delegates the memory handling
            type(Pair), intent(inout) :: this
        end subroutine

        module subroutine destroy (this)
        ! releases dynamically allocated Point objects from memory
            type(Pair), intent(inout) :: this
        end subroutine

    end interface


    ! Operators:


    interface

        module subroutine assignmentOperator (this, otherPair)
        ! copies the references of the first and second Point objects from another Pair
            class(Pair), intent(out) :: this
            class(Pair), intent(in) :: otherPair
        end subroutine

    end interface


    ! Getters:


    interface

        module pure function getDistance (this) result(distance)
        ! returns a copy of the separating (squared) distance
            class(Pair), intent(in) :: this
            real(kind = real64) :: distance
        end function

    end interface


    ! Methods:


    interface

        module subroutine print (this)
        ! prints info of the Pair object on the console
            class(Pair), intent(in) :: this
        end subroutine


        module pure function compareTo (this, pairObject) result(comp)
        ! returns 0 if equal, returns 1 if `this' is greater than the other `Pair',
        ! and returns -1 if `this' is less than the other `Pair'
            class(Pair), intent(in) :: this
            class(Pair), intent(in) :: pairObject
            integer(kind = int32) :: comp
        end function

    end interface

end module PairClass
