! Algorithms and Complexity                                January 21, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines the Time Complexity Class.
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

module TimeComplexityClass
use, intrinsic :: iso_fortran_env, only: int32, real64
use :: EnsembleClass, only: Ensemble
implicit none
private
save


    type, public :: TimeComplexity
        private
        integer(kind = int32) :: runs   ! number of runs (or experiments)
        contains
            private
            procedure :: timeBruteForce
            procedure :: timeBruteForceArrayBased
            procedure :: timeDivideAndConquer1D
            procedure, public :: exportTimeComplexity_BruteForce
            procedure, public :: exportTimeComplexity_BruteForceArrayBased
            procedure, public :: exportTimeComplexity_DivideAndConquer1D
    end type


    ! Constructors:


    interface TimeComplexity
        module procedure defaultConstructor
        module procedure Constructor
    end interface


    interface

        module pure function defaultConstructor () result(this)
        ! useless default constructor
            type(TimeComplexity) :: this
        end function

        module pure function Constructor (runs) result(this)
        ! constructs from the number of runs (or experiments)
            type(TimeComplexity) :: this
            integer(kind = int32), intent(in) :: runs
        end function

    end interface


    ! Methods:


    interface

        module subroutine exportTimeComplexity_BruteForce (this)
        ! exports the time complexity results of the Object-Oriented implementation of
        ! the Brute Force Algorithm
            class(TimeComplexity), intent(in) :: this
        end subroutine

        module subroutine exportTimeComplexity_BruteForceArrayBased (this)
        ! exports the time complexity results of the FORTRAN77 (or procedural)
        ! implementation of the Brute Force Algorithm
            class(TimeComplexity), intent(in) :: this
        end subroutine

        module subroutine exportTimeComplexity_DivideAndConquer1D (this)
        ! exports the time complexity results of the implementation of the 1D Divide And
        ! Conquer Algorithm
            class(TimeComplexity), intent(in) :: this
        end subroutine

    end interface


    ! Implementations:


    interface

        module subroutine timeBruteForce (this, sizes, avgElapsedTimes, avgNumOperations)
        ! times the Object-Oriented implementation of the Brute Force Algorithm
            class(TimeComplexity), intent(in) :: this
            real(kind = real64), allocatable, intent(out) :: sizes(:)
            real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
            real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        end subroutine


        module subroutine timeBruteForceArrayBased (this, sizes, avgElapsedTimes,&
                                                   &avgNumOperations)
        ! times the FORTRAN77 (or procedural) implementation of the Brute Force Algorithm
            class(TimeComplexity), intent(in) :: this
            real(kind = real64), allocatable, intent(out) :: sizes(:)
            real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
            real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        end subroutine


        module subroutine timeDivideAndConquer1D (this, sizes, avgElapsedTimes,&
                                                 &avgNumOperations)
        ! times the implementation of the 1D Divide And Conquer Algorithm
            class(TimeComplexity), intent(in) :: this
            real(kind = real64), allocatable, intent(out) :: sizes(:)
            real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
            real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        end subroutine

    end interface

end module TimeComplexityClass
