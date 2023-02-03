! Algorithms and Complexity                                January 27, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines an auxiliary Tuple Class to encapsulate the closest pair and the
! number of operations (or equivalently, the number of distance computations)
! done to find the closest pair.
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

module TupleClass
use, intrinsic :: iso_fortran_env, only: int32, real64
use :: PairClass, only: Pair
implicit none
private
save
public :: TupleConstructor

    type, public :: Tuple
        private
        type(Pair), pointer :: closestPair => null()
        real(kind = real64) :: numOperations = 0.0_real64
        contains
            private
            procedure :: assignmentOperator
            procedure, public :: getClosestPair
            procedure, public :: getNumOperations
            generic, public :: assignment(=) => assignmentOperator
            final :: destructor
    end type


    ! Constructors:


    interface TupleConstructor
        module procedure defaultConstructor
        module procedure Constructor
    end interface


    interface

        module function defaultConstructor () result(this)
        ! useless default constructor
            type(Tuple), pointer :: this
        end function

        module function Constructor (closestPair, numOperations) result(this)
        ! contructs from the (current) closest pair and the number of operations
            type(Tuple), pointer :: this
            type(Pair), intent(in) :: closestPair
            real(kind = real64), intent(in) :: numOperations
        end function

    end interface


    ! Destructor:


    interface

        module subroutine destructor (this)
            type(Tuple), intent(inout) :: this
        end subroutine

    end interface


    ! Operators:


    interface

        module subroutine assignmentOperator (this, tupleObject)
        ! disables the assignment operator, warns the user at runtime
            class(Tuple), intent(inout) :: this
            type(Tuple), intent(in) :: tupleObject
        end subroutine

    end interface


    ! Getters:


    interface

        module pure function getNumOperations (this) result(numOperations)
        ! returns the number of operations spent on finding the closest pair
            class(Tuple), intent(in) :: this
            real(kind = real64) :: numOperations
        end function

        module function getClosestPair (this) result(closestPair)
        ! returns (a copy of the reference of) the current closest pair
            class(Tuple), intent(in) :: this
            type(Pair), pointer :: closestPair
        end function

    end interface


    ! Methods: None


end module TupleClass
