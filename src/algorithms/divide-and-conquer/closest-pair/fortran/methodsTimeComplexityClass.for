! Algorithms and Complexity                                January 21, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Time Complexity Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (TimeComplexityClass) methods
implicit none
contains


    ! Constructors:


    module pure function defaultConstructor () result(this)
    ! useless default constructor
        type(TimeComplexity) :: this

        this % runs = 0

        return
    end function


    module pure function Constructor (runs) result(this)
    ! constructs a Time Complexity Object from the number of runs (or experiments)
        type(TimeComplexity) :: this
        integer(kind = int32), intent(in) :: runs

        this % runs = runs

        return
    end function


    ! Methods:


    module subroutine exportTimeComplexity_BruteForce (this)
!
!   Synopsis:
!   Exports the time complexity results of the Object-Oriented implementation of the
!   Brute Force Algorithm that solves the closest pair problem to a plain text data file.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   None
!
        class(TimeComplexity), intent(in) :: this
        real(kind = real64), allocatable :: sizes(:)
        real(kind = real64), allocatable :: avgElapsedTimes(:)
        real(kind = real64), allocatable :: avgNumOperations(:)
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        character(*), parameter :: fileBruteForce = 'timeBruteForce.dat'

        if (this % runs <= 0) then
            return
        endif

        call this % timeBruteForce(sizes, avgElapsedTimes, avgNumOperations)

        open(newunit=fhandle, file=fileBruteForce, action='write',&
           & position='rewind', iostat=iostate)

        if (iostate /= 0) then
            print *, 'TimeComplexity::exportTimeComplexity_BruteForce(): IO ERROR'
            stop
        endif

        do i = 1, this % runs
            write (fhandle, '(3E25.15)') sizes(i), avgElapsedTimes(i), avgNumOperations(i)
        end do

        close(fhandle)

        return
    end subroutine exportTimeComplexity_BruteForce


    module subroutine exportTimeComplexity_BruteForceArrayBased (this)
!
!   Synopsis:
!   Exports the time complexity results of the FORTRAN77 (or procedural) implementation
!   of the Brute Force Algorithm that solves the closest pair problem to a plain text
!   data file.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   None
!
        class(TimeComplexity), intent(in) :: this
        real(kind = real64), allocatable :: sizes(:)
        real(kind = real64), allocatable :: avgElapsedTimes(:)
        real(kind = real64), allocatable :: avgNumOperations(:)
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        character(*), parameter :: fileBruteForce = 'timeBruteForceArrayBased.dat'

        if (this % runs <= 0) then
            return
        endif

        call this % timeBruteForceArrayBased(sizes, avgElapsedTimes, avgNumOperations)

        open(newunit=fhandle, file=fileBruteForce, action='write',&
           & position='rewind', iostat=iostate)

        if (iostate /= 0) then
            print *, 'TimeComplexity::exportTimeComplexity_BruteForce(): IO ERROR'
            stop
        endif

        do i = 1, this % runs
            write (fhandle, '(3E25.15)') sizes(i), avgElapsedTimes(i), avgNumOperations(i)
        end do

        close(fhandle)

        return
    end subroutine exportTimeComplexity_BruteForceArrayBased


    module subroutine exportTimeComplexity_DivideAndConquer1D (this)
!
!   Synopsis:
!   Exports the time complexity results of the 1D Divide and Conquer
!   Algorithm that solves the closest pair problem to a plain text
!   data file.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   None
!
        class(TimeComplexity), intent(in) :: this
        real(kind = real64), allocatable :: sizes(:)
        real(kind = real64), allocatable :: avgElapsedTimes(:)
        real(kind = real64), allocatable :: avgNumOperations(:)
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        character(*), parameter :: fileDivideAndConquer = 'timeDivideAndConquer1D.dat'

        if (this % runs <= 0) then
            return
        endif

        call this % timeDivideAndConquer1D(sizes, avgElapsedTimes, avgNumOperations)

        open(newunit=fhandle, file=fileDivideAndConquer, action='write',&
           & position='rewind', iostat=iostate)

        if (iostate /= 0) then
            print *, 'TimeComplexity::exportTimeComplexity_BruteForce(): IO ERROR'
            stop
        endif

        do i = 1, this % runs
            write (fhandle, '(3E25.15)') sizes(i), avgElapsedTimes(i), avgNumOperations(i)
        end do

        close(fhandle)

        return
    end subroutine exportTimeComplexity_DivideAndConquer1D


    module subroutine exportTimeComplexity_DivideAndConquer1D2 (this) ! (version 2)
!
!   Synopsis:
!   Exports the time complexity results of the 1D Divide and Conquer
!   Algorithm that solves the closest pair problem to a plain text
!   data file.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   None
!
        class(TimeComplexity), intent(in) :: this
        real(kind = real64), allocatable :: sizes(:)
        real(kind = real64), allocatable :: avgElapsedTimes(:)
        real(kind = real64), allocatable :: avgNumOperations(:)
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        ! exports the time complexity results in a different data file (version 2)
        character(*), parameter :: fileDivideAndConquer = 'timeDivideAndConquer1D2.dat'

        if (this % runs <= 0) then
            return
        endif

        ! invokes the timer method (version 2)
        call this % timeDivideAndConquer1D2(sizes, avgElapsedTimes, avgNumOperations)

        open(newunit=fhandle, file=fileDivideAndConquer, action='write',&
           & position='rewind', iostat=iostate)

        if (iostate /= 0) then
            print *, 'TimeComplexity::exportTimeComplexity_BruteForce(): IO ERROR'
            stop
        endif

        do i = 1, this % runs
            write (fhandle, '(3E25.15)') sizes(i), avgElapsedTimes(i), avgNumOperations(i)
        end do

        close(fhandle)

        return
    end subroutine exportTimeComplexity_DivideAndConquer1D2


    module subroutine timeBruteForce (this, sizes, avgElapsedTimes, avgNumOperations)
!
!   Synopsis:
!   Times the Object-Oriented implementation of the Brute Force Algorithm that solves
!   the closest pair problem. The method returns the ensemble sizes considered, and the
!   average elapsed-time and number of operations executed by the implementation to
!   solve the closest pair problem.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   sizes               first-rank array of ensemble sizes
!   avgElapsedTimes     first-rank array storing the average elapsed-times
!   avgNumOperations    first-rank array storing the average number of operations
!
        class(TimeComplexity), intent(in) :: this
        type(Ensemble) :: ens
        real(kind = real64), allocatable, intent(out) :: sizes(:)
        real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
        real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        integer(kind = int32) :: ensembleSize
        integer(kind = int32) :: runs, reps
        integer(kind = int32) :: run, rep
        integer(kind = int32) :: mstat

        runs = this % runs
        allocate(sizes(runs), avgElapsedTimes(runs), avgNumOperations(runs), stat=mstat)

        if (mstat /= 0) then
            print *, 'TimeComplexity::timeBruteForce(): failed to allocate arrays'
            stop
        end if

        reps = 256
        ensembleSize = 16
        do run = 1, runs
            elapsedTime = 0.0_real64
            numOperations = 0.0_real64
            do rep = 1, reps
                ens = Ensemble(ensembleSize)
                call ens % bruteForce()
                elapsedTime = elapsedTime + ens % getElapsedTime()
                numOperations = numOperations + ens % getNumOperations()
            end do
            sizes(run) = real(ensembleSize, kind=real64)
            avgElapsedTimes(run) = elapsedTime / real(reps, kind=real64)
            avgNumOperations(run) = numOperations / real(reps, kind=real64)

            ensembleSize = 2 * ensembleSize
        end do

        return
    end subroutine timeBruteForce


    module subroutine timeBruteForceArrayBased (this, sizes, avgElapsedTimes,&
                                               &avgNumOperations)
!
!   Synopsis:
!   Times the FORTRAN77 (or procedural) implementation of the Brute Force Algorithm that
!   solves the closest pair problem. The method returns the ensemble sizes considered,
!   and the average elapsed-time and number of operations executed by the implementation
!   to solve the closest pair problem.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   sizes               first-rank array of ensemble sizes
!   avgElapsedTimes     first-rank array storing the average elapsed-times
!   avgNumOperations    first-rank array storing the average number of operations
!
        class(TimeComplexity), intent(in) :: this
        type(Ensemble) :: ens
        real(kind = real64), allocatable, intent(out) :: sizes(:)
        real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
        real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        integer(kind = int32) :: ensembleSize
        integer(kind = int32) :: runs, reps
        integer(kind = int32) :: run, rep
        integer(kind = int32) :: mstat

        runs = this % runs
        allocate(sizes(runs), avgElapsedTimes(runs), avgNumOperations(runs), stat=mstat)

        if (mstat /= 0) then
            print *, 'TimeComplexity::timeBruteForceF77(): failed to allocate arrays'
            stop
        end if

        reps = 256
        ensembleSize = 16
        do run = 1, runs
            elapsedTime = 0.0_real64
            numOperations = 0.0_real64
            do rep = 1, reps
                ens = Ensemble(ensembleSize)
                call ens % bruteForceArrayBased()
                elapsedTime = elapsedTime + ens % getElapsedTime()
                numOperations = numOperations + ens % getNumOperations()
            end do
            sizes(run) = real(ensembleSize, kind=real64)
            avgElapsedTimes(run) = elapsedTime / real(reps, kind=real64)
            avgNumOperations(run) = numOperations / real(reps, kind=real64)

            ensembleSize = 2 * ensembleSize
        end do

        return
    end subroutine timeBruteForceArrayBased


    module subroutine timeDivideAndConquer1D (this, sizes, avgElapsedTimes,&
                                             &avgNumOperations)
!
!   Synopsis:
!   Times the Object-Oriented implementation of the 1D Divide And Conquer Algorithm that
!   solves the closest pair problem. The method returns the ensemble sizes considered,
!   and the average elapsed-time and number of operations executed by the implementation
!   to solve the closest pair problem.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   sizes               first-rank array of ensemble sizes
!   avgElapsedTimes     first-rank array storing the average elapsed-times
!   avgNumOperations    first-rank array storing the average number of operations
!
        class(TimeComplexity), intent(in) :: this
        type(Ensemble) :: ens
        real(kind = real64), allocatable, intent(out) :: sizes(:)
        real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
        real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        integer(kind = int32) :: ensembleSize
        integer(kind = int32) :: runs, reps
        integer(kind = int32) :: run, rep
        integer(kind = int32) :: mstat

        runs = this % runs
        allocate(sizes(runs), avgElapsedTimes(runs), avgNumOperations(runs), stat=mstat)

        if (mstat /= 0) then
            print *, 'TimeComplexity::timeDivideAndConquer1D(): failed to allocate arrays'
            stop
        end if

        reps = 256
        ensembleSize = 16
        do run = 1, runs
            elapsedTime = 0.0_real64
            numOperations = 0.0_real64
            do rep = 1, reps
                ens = Ensemble(ensembleSize)
                call ens % recursive1D()
                elapsedTime = elapsedTime + ens % getElapsedTime()
                numOperations = numOperations + ens % getNumOperations()
            end do
            sizes(run) = real(ensembleSize, kind=real64)
            avgElapsedTimes(run) = elapsedTime / real(reps, kind=real64)
            avgNumOperations(run) = numOperations / real(reps, kind=real64)

            ensembleSize = 2 * ensembleSize
        end do

        return
    end subroutine timeDivideAndConquer1D


    module subroutine timeDivideAndConquer1D2 (this, sizes, avgElapsedTimes,& ! (ver 2)
                                              &avgNumOperations)
!
!   Synopsis:
!   Times the Object-Oriented implementation of the 1D Divide And Conquer Algorithm that
!   solves the closest pair problem. The method returns the ensemble sizes considered,
!   and the average elapsed-time and number of operations executed by the implementation
!   to solve the closest pair problem.
!
!   Inputs:
!   this                the time complexity object
!
!   Outputs:
!   sizes               first-rank array of ensemble sizes
!   avgElapsedTimes     first-rank array storing the average elapsed-times
!   avgNumOperations    first-rank array storing the average number of operations
!
        class(TimeComplexity), intent(in) :: this
        type(Ensemble) :: ens
        real(kind = real64), allocatable, intent(out) :: sizes(:)
        real(kind = real64), allocatable, intent(out) :: avgElapsedTimes(:)
        real(kind = real64), allocatable, intent(out) :: avgNumOperations(:)
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        integer(kind = int32) :: ensembleSize
        integer(kind = int32) :: runs, reps
        integer(kind = int32) :: run, rep
        integer(kind = int32) :: mstat

        runs = this % runs
        allocate(sizes(runs), avgElapsedTimes(runs), avgNumOperations(runs), stat=mstat)

        if (mstat /= 0) then
            print *, 'TimeComplexity::timeDivideAndConquer1D(): failed to allocate arrays'
            stop
        end if

        reps = 256
        ensembleSize = 16
        do run = 1, runs
            elapsedTime = 0.0_real64
            numOperations = 0.0_real64
            do rep = 1, reps
                ens = Ensemble(ensembleSize)
                ! invokes the recursive1D() method (version 2)
                call ens % recursive1D2()
                elapsedTime = elapsedTime + ens % getElapsedTime()
                numOperations = numOperations + ens % getNumOperations()
            end do
            sizes(run) = real(ensembleSize, kind=real64)
            avgElapsedTimes(run) = elapsedTime / real(reps, kind=real64)
            avgNumOperations(run) = numOperations / real(reps, kind=real64)

            ensembleSize = 2 * ensembleSize
        end do

        return
    end subroutine timeDivideAndConquer1D2

end submodule
