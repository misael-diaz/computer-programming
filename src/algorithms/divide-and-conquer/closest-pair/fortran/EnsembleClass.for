! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines the Ensemble Class.
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

module EnsembleClass
use, intrinsic :: iso_fortran_env, only: int32, int64, real64
use :: ieee_arithmetic, only : ieee_value, ieee_positive_inf
use :: VectorClass, only: Vector
use :: TupleClass, only: Tuple, TupleConstructor
use :: PairClass, only: Pair
use :: PointClass, only: Point
use :: PointClass, only: insertionSort => Point_insertionSort
implicit none
private
save
public :: Ensemble_tests

    type, public :: Ensemble
        private
        ! time spent on finding the closest pair in an Ensemble of Points
        real(kind = real64) :: elapsedTime
        ! number of operations spent on finding the closest pair
        real(kind = real64) :: numOperations
        ! ensemble size (or equivalently, the number of distinct points)
        integer(kind = int32) :: size
        contains
            private
            ! getters
            procedure, public :: getElapsedTime
            procedure, public :: getNumOperations
            ! Brute Force Methods
            procedure, public :: bruteForce
            procedure, public :: bruteForceArrayBased
            ! Divide And Conquer Methods
            procedure, public :: recursive1D
            procedure, public :: recursive1D2           ! (version 2)
            ! setters (or timers)
            procedure :: bruteForceMethod
            procedure :: bruteForceMethodArrayBased
            procedure :: recursive1DMethod
            procedure :: recursive1DMethod2             ! (version 2)
            ! implements recursive algorithms
            procedure :: recurse
            procedure :: recurse2                       ! (version 2)
            procedure :: combine
            procedure :: combine2                       ! (version 2)
            ! implements distance computing algorithms
            procedure :: distanceOOP
            procedure :: distanceArrayBased
            procedure :: distanceArrayBased2            ! (version 2)
            procedure :: distanceSmallerPartition
            procedure :: distanceSmallerPartition2
            procedure :: distancePartitionInterface
            procedure :: distancePartitionInterface2
            ! utilities
            procedure :: toArray
            procedure :: isInvalidData
            procedure :: createDataset1D
            procedure :: hasDuplicateClosestPair
            procedure :: create
            procedure :: create2D
            procedure :: export
            ! generics
            generic :: distance => distanceOOP, distanceArrayBased, distanceArrayBased2,&
                                  &distanceSmallerPartition, distanceSmallerPartition2,&
                                  &distancePartitionInterface, distancePartitionInterface2
    end type


    ! Constructors:


    interface Ensemble
        module procedure defaultConstructor
        module procedure Constructor
    end interface


    interface

        module pure function defaultConstructor () result(this)
        ! useless default constructor
            type(Ensemble) :: this
        end function

        module pure function Constructor (size) result(this)
        ! constructs an Ensemble of points of requested size
            type(Ensemble) :: this
            integer(kind = int32), intent(in) :: size
        end function

    end interface


    ! Getters:


    interface

        module pure function getElapsedTime (this) result(elapsedTime)
        ! returns the elapsed-time spent on finding the closest pair
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: elapsedTime
        end function


        module pure function getNumOperations (this) result(numOperations)
        ! returns the number of operations spent on finding the closest pair
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: numOperations
        end function

    end interface


    ! Methods:


    interface

        module subroutine bruteForce (this)
        ! applies the Brute Force Algorithm to find the closest pair
            class(Ensemble), intent(inout) :: this
        end subroutine

        module subroutine bruteForceArrayBased (this)
        ! FORTRAN77 (or procedural) implementation of the Brute Force Algorithm
            class(Ensemble), intent(inout) :: this
        end subroutine

        module subroutine recursive1D (this)
        ! applies the 1D Divide And Algorithm to find the closest pair
            class(Ensemble), intent(inout) :: this
        end subroutine

        module subroutine recursive1D2 (this)
        ! applies the 1D Divide And Algorithm to find the closest pair (version 2)
            class(Ensemble), intent(inout) :: this
        end subroutine

    end interface


    ! Implementations:


    interface

        module subroutine bruteForceMethod (this)
        ! invokes the Brute Force Algorithm and sets (as a side-effect) the elapsed-time
        ! and the number of operations executed by the algorithm to find the closest pair
            class(Ensemble), intent(inout) :: this
        end subroutine


        module subroutine bruteForceMethodArrayBased (this)
        ! invokes the Brute Force Algorithm and sets (as a side-effect) the elapsed-time
        ! and the number of operations executed by the algorithm to find the closest pair
            class(Ensemble), intent(inout) :: this
        end subroutine


        module subroutine recursive1DMethod (this)
        ! invokes the 1D Divide And Conquer Algorithm and sets (as a side-effect) the
        ! elapsed-time and the number of operations executed by the algorithm to find
        ! the closest pair
            class(Ensemble), intent(inout) :: this
        end subroutine


        module subroutine recursive1DMethod2 (this)
        ! as recursive1DMethod() but does not (de)constructs derived-type objects (ver 2)
            class(Ensemble), intent(inout) :: this
        end subroutine


        module recursive function recurse (this, beginPosition, endPosition, Px) result(t)
        ! implements the 1D Divide And Conquer Algorithm that finds the closest pair
            class(Ensemble), intent(in) :: this
            type(Tuple), pointer :: t
            real(kind = real64), intent(in) :: Px(:, :)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
        end function


        module recursive function recurse2 (this, beginPosition, endPosition, Px)&
                                           &result(ret)
        ! implements the 1D Divide And Conquer Algorithm that finds the closest pair ver 2
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: ret(3)
            real(kind = real64), intent(in) :: Px(this % size, 2)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
        end function


        module function combine (this, beginPosition, endPosition,&
                                &Px, currentClosestPair) result(t)
        ! implements the combination step of the 1D Divide And Conquer Algorithm
            class(Ensemble), intent(in) :: this
            type(Tuple), pointer :: t
            type(Pair), intent(in) :: currentClosestPair
            real(kind = real64), intent(in) :: Px(:, :)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
        end function


        module function combine2 (this, beginPosition, endPosition,&
                                 &Px, currentClosestPair) result(ret)
        ! implements the combination step of the 1D Divide And Conquer Algorithm, ver 2
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: ret(3)
            real(kind = real64), intent(in) :: Px(this % size, 2)
            integer(kind = int32), intent(in) :: currentClosestPair(2)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
        end function


        module function distanceOOP (this, points) result(closestPair)
        ! Object-Oriented implementation of the Brute Force Algorithm that finds the
        ! closest pair
            class(Ensemble), intent(in) :: this
            class(Point), intent(in), target :: points(:)
            type(Pair), pointer :: closestPair
        end function


        module function distanceArrayBased (this, points) result(closestPair)
        ! FORTRAN77 (or procedural) implementation of the Brute Force Algorithm
            class(Ensemble), intent(in) :: this
            real(kind = real64), intent(in) :: points(:, :)
            type(Pair), pointer :: closestPair
        end function


        module function distanceArrayBased2 (this, points, closestPair)&
                                            &result(ret)
        ! FORTRAN77 (or procedural) implementation of the Brute Force Algorithm, version 2
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: ret(3)
            real(kind = real64), intent(in) :: points(this % size, 2)
            integer(kind = int32), intent(out) :: closestPair(2)
        end function


        module function distanceSmallerPartition (this, beginPosition, endPosition, Px)&
                                                 &result(t)
        ! implements the Brute Force Algorithm for smaller partitions
            class(Ensemble), intent(in) :: this
            type(Tuple), pointer :: t
            real(kind = real64), intent(in) :: Px(:, :)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
        end function


        module function distanceSmallerPartition2 (this, beginPosition, endPosition, Px,&
                                                  &closestPair) result(ret)
        ! implements the Brute Force Algorithm for smaller partitions (version 2)
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: ret(3)
            real(kind = real64), intent(in) :: Px(this % size, 2)
            integer(kind = int32), intent(in) :: beginPosition
            integer(kind = int32), intent(in) :: endPosition
            integer(kind = int32), intent(out) :: closestPair(2)
        end function


        module function distancePartitionInterface (this, beginPosLeft, endPosLeft,&
                                                   &beginPosRight, endPosRight, Px,&
                                                   &currentClosestPair) result(t)
        ! implements the Brute Force Algorithm to seek the closest pair between partitions
            class(Ensemble), intent(in) :: this
            type(Tuple), pointer :: t
            type(Pair), intent(in) :: currentClosestPair
            real(kind = real64), intent(in) :: Px(:, :)
            integer(kind = int32), intent(in) :: beginPosLeft, endPosLeft
            integer(kind = int32), intent(in) :: beginPosRight, endPosRight
        end function


        module function distancePartitionInterface2 (this, beginPosLeft, endPosLeft,&
                                                    &beginPosRight, endPosRight, Px,&
                                                    &currentClosestPair) result(ret)
        ! implements the partition interface, closest-pair, finding method (version 2)
            class(Ensemble), intent(in) :: this
            real(kind = real64) :: ret(3)
            real(kind = real64), intent(in) :: Px(this % size, 2)
            integer(kind = int32), intent(in) :: currentClosestPair(2)
            integer(kind = int32), intent(in) :: beginPosLeft, endPosLeft
            integer(kind = int32), intent(in) :: beginPosRight, endPosRight
        end function

    end interface


    ! Utilities:


    interface

        module subroutine toArray (this, points, array)
        ! copies the Points x, y coordinates into a second-rank array
            class(Ensemble), intent(in) :: this
            class(Point), intent(in) :: points(:)
            real(kind = real64), allocatable, intent(out) :: array(:, :)
        end subroutine


        module pure function isInvalidData (this, points) result(isInvalid)
            class(Ensemble), intent(in) :: this
            class(Point), intent(in) :: points(:)
            logical(kind = int32) :: isInvalid
        end function


        module subroutine createDataset1D (this, points)
        ! creates a dataset of `size' distinct points without duplicate closest pairs
            class(Ensemble), intent(in) :: this
            class(Point), allocatable, intent(out) :: points(:)
        end subroutine


        module function hasDuplicateClosestPair (this, points) result(hasDuplicate)
        ! returns true (false) if the closest pair is (not) duplicated
            class(Ensemble), intent(in) :: this
            class(Point), intent(in), target :: points(:)
            logical(kind = int32) :: hasDuplicate
        end function


        module subroutine create (this, points)
        ! creates a dataset of N distinct points, where N is the ensemble `size'
            class(Ensemble), intent(in) :: this
            class(Point), allocatable, intent(out) :: points(:)
        end subroutine


        module subroutine create2D (this, points)
        ! as create() but it bounds the Points into a squared domain
            class(Ensemble), intent(in) :: this
            class(Point), allocatable, intent(out) :: points(:)
        end subroutine


        module subroutine export (this, points)
        ! exports the coordinates that cause the recursive algorithm to fail for analysis
            class(Ensemble), intent(in) :: this
            real(kind = real64), intent(in) :: points(this % size, 2)
        end subroutine

    end interface


    ! Tests:


    interface Ensemble_tests
        module procedure tests
    end interface


    interface

        module subroutine tests ()
        end subroutine

    end interface


end module EnsembleClass
