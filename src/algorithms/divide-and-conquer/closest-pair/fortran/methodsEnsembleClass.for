! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Ensemble Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (EnsembleClass) methods
implicit none
contains


    ! Constructors:


    module pure function defaultConstructor () result(this)
    ! useless default constructor
        type(Ensemble) :: this

        this % size = 0
        this % numOperations = 0.0_real64
        this % elapsedTime = 0.0_real64

        return
    end function


    module pure function Constructor (size) result(this)
    ! constructs an Ensemble (of distinct Points) of requested size
        type(Ensemble) :: this
        integer(kind = int32), intent(in) :: size

        this % size = size
        this % numOperations = 0.0_real64
        this % elapsedTime = 0.0_real64

        return
    end function


    ! Getters:


    module pure function getElapsedTime (this) result(elapsedTime)
    ! returns the elapsed time spent on finding the closest pair
        class(Ensemble), intent(in) :: this
        real(kind = real64) :: elapsedTime

        elapsedTime = this % elapsedTime

        return
    end function


    module pure function getNumOperations (this) result(numOperations)
    ! returns the number of operations spent on finding the closest pair
        class(Ensemble), intent(in) :: this
        real(kind = real64) :: numOperations

        numOperations = this % numOperations

        return
    end function


    ! Methods:


    module subroutine bruteForce (this)
    ! applies the Object Oriented Implementation of the Brute Force Algorithm
        class(Ensemble), intent(inout) :: this

        ! caters invalid ensemble sizes
        if (this % size >= 2) then
            call this % bruteForceMethod()
        endif

        return
    end subroutine


    module subroutine bruteForceArrayBased (this)
    ! applies the FORTRAN77 (procedural) implementation of the Brute Force Algorithm
        class(Ensemble), intent(inout) :: this

        ! caters invalid ensemble sizes
        if (this % size >= 2) then
            call this % bruteForceMethodArrayBased()
        endif

        return
    end subroutine


    module subroutine recursive1D (this)
    ! applies the 1D Divide And Conquer Algorithm to find the closest pair
        class(Ensemble), intent(inout) :: this

        ! caters invalid ensemble sizes
        if (this % size >= 2) then
            call this % recursive1DMethod()
        endif

        return
    end subroutine


    module subroutine bruteForceMethod (this)
!
!   Synopsis:
!   Times the Object Oriented implementation of the Brute Force Algorithm.
!
!   Applies the Brute Force Algorithm to obtain the closest pair. Sets the
!   elapsed-time (nanoseconds) invested in determining the closest pair. It
!   also sets the number of operations (or the number of distance computations)
!   executed by the Brute Force algorithm to find the closest pair.
!
!   Inputs:
!   this                the ensemble
!
!   Output:
!   None
!
        class(Ensemble), intent(inout) :: this
        class(Point), allocatable :: points(:)
        type(Pair), pointer :: closestPair
        integer(kind = int64) :: startCount, endCount
        integer(kind = int64) :: systemClockCountRate
        real(kind = real64) :: systemClockPeriod
        real(kind = real64) :: float_startCount
        real(kind = real64) :: float_endCount
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        real(kind = real64) :: N


        ! creates the dataset of N distinct points
        call this % createDataset1D(points)


        call system_clock(startCount)
        ! times the Brute Force Algorithm
        closestPair => this % distance(points)
        call system_clock(endCount)


        ! sets the elapsed-time (nanoseconds) invested on finding the closest pair
        call system_clock(count_rate = systemClockCountRate)
        systemClockPeriod = 1.0_real64 / real(systemClockCountRate, kind=real64)
        float_startCount = real(startCount, kind=real64)
        float_endCount = real(endCount, kind=real64)
        elapsedTime = systemClockPeriod * (float_endCount - float_startCount)
        elapsedTime = 1.0e9_real64 * elapsedTime
        this % elapsedTime = elapsedTime


        ! sets the number of operations executed by the Brute Force Algorithm
        ! Note: the Brute Force Algorithm always computes (N * (N - 1) / 2) distances
        N = real(this % size, kind=real64)
        numOperations = ( N * (N - 1.0_real64) ) / 2.0_real64
        this % numOperations = numOperations


        ! (optionally) displays info about the closest pair
        ! call closestPair % print()

        deallocate(closestPair, points)
        closestPair => null()

        return
    end subroutine bruteForceMethod


    module subroutine recursive1DMethod (this)
!
!   Synopsis:
!   Times the implementation of the 1D Divide And Conquer Algorithm.
!
!   Applies the 1D Divide And Conquer Algorithm to obtain the closest pair. Sets the
!   elapsed-time (nanoseconds) invested in determining the closest pair. It also sets
!   the number of operations (or the number of distance computations) executed by the
!   algorithm to find the closest pair.
!
!   Inputs:
!   this                the ensemble
!
!   Output:
!   None
!
        class(Ensemble), intent(inout) :: this
        class(Point), allocatable :: points(:)
        type(Pair), pointer :: closestPair
        type(Pair), pointer :: closestPairBruteForce
        type(Tuple), pointer :: t
        type(Tuple), pointer :: dataBruteForce
        real(kind = real64), allocatable :: Px(:, :)
        integer(kind = int64) :: startCount, endCount
        integer(kind = int64) :: systemClockCountRate
        real(kind = real64) :: systemClockPeriod
        real(kind = real64) :: float_startCount
        real(kind = real64) :: float_endCount
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        real(kind = real64) :: N


        ! creates the dataset of N distinct points and stores it in a vector
        call this % createDataset1D(points)

        if ( this % isInvalidData(points) ) then
            print *, 'Ensemble::recursive1D(): buggy sorting algorithm has been detected'
            deallocate(points)
            return
        end if

        ! copies the vector elements into an array (of N distinct) Points
        call this % toArray(points, Px)


        ! times the 1D Divide And Conquer Algorithm
        call system_clock(startCount)
        ! NOTE:
        ! The implementation of the recursive algorithm expects a zero-starting,
        ! asymmetric range [0, N) the first time it is invoked, where `N' is the
        ! ensemble size (or equivalently, the number of points). This makes the
        ! implementation in FORTRAN analogous to their Java and C++ counterparts.
        t => this % recurse(0, this % size, Px)
        call system_clock(endCount)


        ! sets the elapsed-time (nanoseconds) invested on finding the closest pair
        call system_clock(count_rate = systemClockCountRate)
        systemClockPeriod = 1.0_real64 / real(systemClockCountRate, kind=real64)
        float_startCount = real(startCount, kind=real64)
        float_endCount = real(endCount, kind=real64)
        elapsedTime = systemClockPeriod * (float_endCount - float_startCount)
        elapsedTime = 1.0e9_real64 * elapsedTime
        this % elapsedTime = elapsedTime


        ! sets the number of operations executed by the recursive algorithm
        N = real(this % size, kind=real64)
        numOperations = t % getNumOperations()
        this % numOperations = numOperations


        ! (optionally) displays info about the closest pair
        closestPair => t % getClosestPair()
        ! call closestPair % print()

        ! saves the closest pair obtained found by the brute force method
        dataBruteForce => this % distance(0, this % size, Px)
        closestPairBruteForce => dataBruteForce % getClosestPair()

        ! reports to user if the closest pairs are different
        if (closestPair % compareTo(closestPairBruteForce) /= 0) then
            print *, 'different closest pairs found:'
            print *, 'Divide And Conquer Algorithm:'
            call closestPair % print()
            print *, 'Brute Force Algorithm:'
            call closestPairBruteForce % print()
            ! writes the coordinates of the points to a file for analysis
            ! call this % export(Px)
        end if

        deallocate(t, dataBruteForce, closestPair, closestPairBruteForce, points, Px)
        closestPairBruteForce => null()
        closestPair => null()
        dataBruteForce => null()
        t => null()

        return
    end subroutine recursive1DMethod


    module subroutine bruteForceMethodArrayBased (this)
    ! times the FORTRAN77 (procedural) implementation of the Brute Force Algorithm
        class(Ensemble), intent(inout) :: this
        class(Point), allocatable :: dataset(:)
        type(Pair), pointer :: closestPair
        real(kind = real64), allocatable :: points(:, :)
        integer(kind = int64) :: startCount, endCount
        integer(kind = int64) :: systemClockCountRate
        real(kind = real64) :: systemClockPeriod
        real(kind = real64) :: float_startCount
        real(kind = real64) :: float_endCount
        real(kind = real64) :: elapsedTime
        real(kind = real64) :: numOperations
        real(kind = real64) :: N


        ! creates the dataset of N distinct points and stores it in a vector
        call this % createDataset1D(dataset)
        ! copies the vector elements into an array (of N distinct) Points
        call this % toArray(dataset, points)


        call system_clock(startCount)
        ! times the FORTRAN77 (procedural) implementation of the Brute Force Algorithm
        closestPair => this % distance(points)
        call system_clock(endCount)


        ! sets the elapsed-time (nanoseconds) invested on finding the closest pair
        call system_clock(count_rate = systemClockCountRate)
        systemClockPeriod = 1.0_real64 / real(systemClockCountRate, kind=real64)
        float_startCount = real(startCount, kind=real64)
        float_endCount = real(endCount, kind=real64)
        elapsedTime = systemClockPeriod * (float_endCount - float_startCount)
        elapsedTime = 1.0e9_real64 * elapsedTime
        this % elapsedTime = elapsedTime


        ! sets the number of operations executed by the Brute Force Algorithm
        ! Note: the Brute Force Algorithm always computes (N * (N - 1) / 2) distances
        N = real(this % size, kind=real64)
        numOperations = ( N * (N - 1.0_real64) ) / 2.0_real64
        this % numOperations = numOperations


        ! (optionally) displays info about the closest pair
        ! call closestPair % print()

        deallocate(closestPair, points)
        closestPair => null()

        return
    end subroutine bruteForceMethodArrayBased


    module recursive function recurse (this, beginPosition, endPosition, Px) result(t)
!
!   Synopsis:
!   Implements the 1D Divide and Conquer Algorithm that finds the closest pair.
!
!   Applies the 1D Divide and Conquer Algorithm to find the closest pair. If the
!   partition P is small enough, the method uses Brute Force to find the closest pair.
!   Otherwise, the method divides the partition P into left and right partitions to look
!   for the closest pair in each. Note that the division step continues until the
!   partitions are small enough to use Brute Force (or the direct method). Then, the
!   method combines the solutions by selecting the smallest of the closest pair
!   candidates and by looking for the closest pair between partitions.
!
!   The method returns a tuple containing the closest pair and the number of operations
!   (or equivalently, the distance computations) invested to find the closest pair.
!
!   Inputs:
!   this                the ensemble
!
!   beginPosition       beginning position of the partition
!   endPosition         ending position of the partition
!
!   Px                  second-rank, dimension(N, 2), array storing the x, y coordinates
!                       of the `N' distinct Points that comprise the Ensemble
!
!   Output:
!   tuple               the closest pair and the number of operations done to find it
!
!   COMMENTS:
!   The partition is delimited by the asymmetric range [beginPosition, endPosition).
!   The method expects the asymmetric range [0, N) the first time it is invoked,
!   where `N' is the number of distinct points.
!
!   The implementation in FORTRAN is closer in spirit to its Java counterpart in the
!   sense that the algorithm does its job without copying the partitions. On the other
!   hand the implementation in C++ copies the smaller partitions into a std::vector via
!   the constructor that takes (begin and end) iterators as input.
!
        class(Ensemble), intent(in) :: this
        type(Tuple), pointer :: t
        type(Tuple), pointer :: data
        type(Tuple), pointer :: dataLeft
        type(Tuple), pointer :: dataRight
        type(Pair), pointer :: closestPair
        type(Pair), pointer :: currentClosestPair
        type(Pair), pointer :: leftClosestPair
        type(Pair), pointer :: rightClosestPair
        real(kind = real64), intent(in) :: Px(:, :)
        real(kind = real64) :: numOperations
        real(kind = real64) :: numOperationsLeft
        real(kind = real64) :: numOperationsRight
        integer(kind = int32), intent(in) :: beginPosition
        integer(kind = int32), intent(in) :: endPosition
        integer(kind = int32) :: beginPos
        integer(kind = int32) :: endPos
        integer(kind = int32) :: numel


        !print *, 'b: ', beginPosition, ' e: ', endPosition


        ! gets the number of elements in this partition (NOTE: endPosition is exclusive)
        numel = (endPosition - beginPosition)
        if (numel <= 3) then

            ! applies the Brute Force Algorithm on the smaller partition
            t => this % distance(beginPosition, endPosition, Px)

        else


            ! divides into left and right partitions to find the closest pair:


            ! defines the asymmetric range [begin, end) that delimits the left partition
            ! NOTE:
            ! Contains the first half of the Points (N / 2) in the current partition
            beginPos = beginPosition
            endPos = beginPosition + (numel / 2)
            ! searches (recursively) for the closest pair in the left partition
            dataLeft => this % recurse(beginPos, endPos, Px)
            leftClosestPair => dataLeft % getClosestPair()


            ! defines the asymmetric range [begin, end) that delimits the right partition
            ! NOTE:
            ! Contains the remaining Points (N - N / 2) in the current partition, the
            ! computation takes into account odd N values.
            beginPos = beginPosition + (numel / 2)
            endPos = beginPosition + numel
            ! searches (recursively) for the closest pair in the right partition
            dataRight => this % recurse(beginPos, endPos, Px)
            rightClosestPair => dataRight % getClosestPair()


            ! selects the smallest closest pair from the left and right partitions
            if ( leftClosestPair % compareTo(rightClosestPair) < 0 ) then
                currentClosestPair => Pair(leftClosestPair)
            else
                currentClosestPair => Pair(rightClosestPair)
            end if


            ! combines the left and right partitions
            data => this % combine(beginPosition, endPosition, Px, currentClosestPair)
            closestPair => data % getClosestPair()


            ! updates the number of operations
            numOperations = data % getNumOperations()
            numOperationsLeft = dataLeft % getNumOperations()
            numOperationsRight = dataRight % getNumOperations()
            numOperations = numOperations + numOperationsLeft + numOperationsRight


            ! packs the closest pair and the number of operations into a Tuple object
            t => TupleConstructor(closestPair, numOperations)


            ! releases temporary placeholders from memory
            deallocate(closestPair)
            deallocate(currentClosestPair)
            deallocate(rightClosestPair)
            deallocate(leftClosestPair)
            deallocate(dataRight)
            deallocate(dataLeft)
            deallocate(data)


            ! nullifies pointers
            closestPair => null()
            currentClosestPair => null()
            rightClosestPair => null()
            leftClosestPair => null()
            dataRight => null()
            dataLeft => null()
            data => null()

        end if

        return
    end function recurse


    module function combine (this, beginPosition, endPosition, Px, currentClosestPair)&
                            &result(t)
!
!   Synopsis:
!   Looks for the closest pair at the interface of the left and right partitions (dubbed
!   as the middle partition M).
!
!   Inputs:
!   this                        the ensemble
!
!   beginPosition               beginning position of the partition
!   endPosition                 ending position of the partition
!
!   Px                          second-rank, dimension(N, 2), array storing the x, y
!                               coordinates of the `N' distinct Points that comprise the
!                               Ensemble
!
!   currentClosestPair          the closest pair thus far
!
!   Output:
!   tuple                       the closest pair and the number of operations
!
!
!   COMMENTS:
!   Even though are refering in the comments of this method to the distance along the
!   x-axis, we are really computing the squared (x2 - x1)^2 because we are comparing that
!   quantity against the squared distance of the current closest pair
!
!                        d^2 = (x2 - x1)^2 + (y2 - y1)^2
!
!   to determine if the considered pair of points could be closer.
!
        class(Ensemble), intent(in) :: this
        type(Tuple), pointer :: t
        type(Pair), intent(in) :: currentClosestPair
        real(kind = real64) :: x1, x2
        real(kind = real64), intent(in) :: Px(:, :)
        integer(kind = int32), intent(in) :: beginPosition
        integer(kind = int32), intent(in) :: endPosition
        integer(kind = int32) :: beginPos
        integer(kind = int32) :: endPos
        integer(kind = int32) :: beginPosLeft, endPosLeft
        integer(kind = int32) :: beginPosRight, endPosRight
        real(kind = real64) :: d, d_min
        integer(kind = int32) :: numel
        integer(kind = int32) :: i, j

        ! gets the (squared) distance of the current closest pair
        d_min = currentClosestPair % getDistance()

        ! gets the total number of Points present in the partition
        numel = (endPosition - beginPosition)

        ! defines the asymmetric range [begin, end) that encompasses the left partition
        beginPos = beginPosition
        endPos = beginPosition + (numel / 2)

        ! assumes no points in the left partition could comprise a closest pair
        ! NOTE: the Points enclosed in the asymmetric range [beginPos1, endPos1) are
        ! points in the Left partition that could form a closest pair with another point
        ! in the Right partition.
        beginPosLeft = endPos
        endPosLeft = endPos

        i = beginPos
        ! includes points in the left partition comprising closest pair candidates
        do while (i /= endPos)
        ! Note:
        ! traverses the partition array Px from back to front owing to the x-y sorting
        ! and also note that the partition array is a non-zero index starting array

            ! gets index of the current rightmost point in the left partition
            ! Note: an offset is needed to position the index on the expected data
            j = ( endPos - (i + 1) ) + beginPos

            ! gets the `x' position of the current rightmost Point in the left partition
            x1 = Px(j + 1, 1)
            ! gets the `x' position of the leftmost Point in the right partition
            x2 = Px(endPos + 1, 1)

            ! computes the (squared) x-axis distance of the closest pair candidate
            d = (x2 - x1) * (x2 - x1)

            if (d < d_min) then
                ! includes the point because it can be a closest pair comprising point
                beginPosLeft = beginPosLeft - 1
            else
                ! halts the search because the next points are even farther apart
                exit
            endif

            i = i + 1
        end do


        ! defines the asymmetric range [begin, end) that encompasses the right partition
        beginPos = beginPosition + (numel / 2)
        endPos = beginPosition + numel

        ! assumes no points in the right partition could comprise a closest pair
        beginPosRight = beginPosition + (numel / 2)
        endPosRight = beginPosition + (numel / 2)

        i = beginPos
        ! includes points in the right partition comprising closest pair candidates
        do while (i /= endPos)

            ! gets the `x' position of the rightmost point in the left partition
            x1 = Px( (beginPos - 1) + 1, 1)
            ! gets the `x' position of the current leftmost point in the right partition
            x2 = Px(i + 1, 1)

            ! computes the (squared) x-axis distance of the closest pair candidate
            d = (x2 - x1) * (x2 - x1)

            if (d < d_min) then
                ! includes the point because it can be a closest pair comprising point
                endPosRight = endPosRight + 1
            else
                ! halts the search because the next points are even farther apart
                exit
            endif

            i = i + 1
        end do


        t => this % distance(beginPosLeft, endPosLeft, beginPosRight, endPosRight, Px,&
                            &currentClosestPair)

        return
    end function combine


    module function distanceOOP (this, points) result(closestPair)
!
!   Synopsis:
!   Object Oriented Implementation of the Brute Force Algorithm
!
!   Applies the Brute Force Algorithm to find the closest pair in a partition.
!   Note that the partition could be the whole dataset.
!
!   Inputs:
!   this                the ensemble
!   points              partition
!
!   Outputs:
!   closestPair         the closest pair
!
        class(Ensemble), intent(in) :: this
        class(Point), intent(in), target :: points(:)
        type(Pair), pointer :: closestPair
        type(Pair), pointer :: pairObject
        class(Point), pointer :: p, q
        real(kind = real64) :: d
        integer(kind = int32) :: i, j

        ! instantiates the closest pair
        closestPair => Pair()

        i = 0
        ! considers all distinct pairs in search for the closest pair
        do while ( i /= (this % size - 1) )

            j = (i + 1)
            do while (j /= this % size)

                ! gets the points P and Q by reference
                p => points(i + 1)
                q => points(j + 1)
                d = p % distance(q)
                ! creates a new Pair object from the points P and Q
                pairObject => Pair(p, q, d)

                ! updates the closest pair if the current pair is closer
                if ( pairObject % compareTo(closestPair) < 0 ) then
                    closestPair = pairObject
                end if

                ! frees the temporary pair object from memory to avert memory leaks
                deallocate(pairObject)
                pairObject => null()

                j = j + 1
            end do

            i = i + 1
        end do

        return
    end function distanceOOP


    module function distanceArrayBased (this, points) result(closestPair)
!
!   Synopsis:
!   FORTRAN77 (or procedural) implementation of the Brute Force Algorithm.
!
!   Applies the Brute Force Algorithm to find the closest pair in a partition.
!   Note that the partition could be the whole dataset.
!
!   Inputs:
!   this                the ensemble
!   points              second-rank array, dimension(N, 2) partition, where `N' is the
!                       number of Points
!
!   Outputs:
!   closestPair         the closest pair
!
!   COMMENTS:
!   In constrast to the Object Oriented implementation in FORTRAN, no objects are created
!   in the nested for-loops. And the Points are represented by a second-rank array storing
!   the x, y coordinates.
!
        class(Ensemble), intent(in) :: this
        real(kind = real64), intent(in) :: points(:, :)
        type(Pair), pointer :: closestPair
        class(Point), pointer :: p, q
        real(kind = real64) :: d, d_min
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        real(kind = real64) :: inf
        integer(kind = int32) :: first, second
        integer(kind = int32) :: i, j

        ! stores the floating-point representation of positive infinity
        inf = ieee_value(0.0_real64, ieee_positive_inf)

        first = -1
        second = -1
        d_min = inf
        i = 0
        ! considers all distinct pairs in search for the closest pair
        do while ( i /= (this % size - 1) )

            j = (i + 1)
            do while (j /= this % size)

                x1 = points(i + 1, 1)
                y1 = points(i + 1, 2)

                x2 = points(j + 1, 1)
                y2 = points(j + 1, 2)

                d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)

                if (d < d_min) then
                    first = (i + 1)
                    second = (j + 1)
                    d_min = d
                end if

                j = j + 1
            end do

            i = i + 1
        end do

        ! gets the x, y coordinates of the first point that comprises the closest pair
        x1 = points(first, 1)
        y1 = points(first, 2)

        ! gets the x, y coordinates of the second point that comprises the closest pair
        x2 = points(second, 1)
        y2 = points(second, 2)

        p => Point(x1, y1)
        q => Point(x2, y2)
        ! creates the closest pair object
        closestPair => Pair(p, q, d_min)

        deallocate(p, q)

        return
    end function distanceArrayBased


    module function distanceSmallerPartition (this, beginPosition, endPosition, Px)&
                                             &result(t)
!
!   Synopsis:
!   Applies the Brute Force Algorithm to find the closest pair in a partition delimited
!   by the asymmetric range [beginPosition, endPosition).
!
!   Inputs:
!   this                the ensemble
!
!   beginPosition       beginning position of the partition
!   endPosition         ending position of the partition
!
!   Px                  second-rank, dimension(N, 2), zero-starting index, array storing
!                       the x, y coordinates of the `N' distinct Points that comprise the
!                       Ensemble
!
!   Outputs:
!   t                   a tuple storing the closest pair and the number of operations
!
        class(Ensemble), intent(in) :: this
        type(Tuple), pointer :: t
        type(Pair), pointer :: closestPair
        class(Point), pointer :: p, q
        real(kind = real64), intent(in) :: Px(:, :)
        real(kind = real64) :: d, d_min
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        real(kind = real64) :: inf
        real(kind = real64) :: N
        real(kind = real64) :: numOperations
        integer(kind = int32), intent(in) :: beginPosition
        integer(kind = int32), intent(in) :: endPosition
        integer(kind = int32) :: first, second
        integer(kind = int32) :: i, j

        ! stores the floating-point representation of positive infinity
        inf = ieee_value(0.0_real64, ieee_positive_inf)

        first = -1
        second = -1
        d_min = inf
        i = beginPosition
        do while ( i /= (endPosition - 1) )
        ! considers all distinct pairs (in partition) in search for the closest pair

            j = (i + 1)
            do while (j /= endPosition)

                x1 = Px(i + 1, 1)
                y1 = Px(i + 1, 2)

                x2 = Px(j + 1, 1)
                y2 = Px(j + 1, 2)

                d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)

                if (d < d_min) then
                    first = (i + 1)
                    second = (j + 1)
                    d_min = d
                end if

                j = j + 1
            end do

            i = i + 1
        end do

        ! gets the x, y coordinates of the first Point
        x1 = Px(first, 1)
        y1 = Px(first, 2)

        ! gets the x, y coordinates of the second Point
        x2 = Px(second, 1)
        y2 = Px(second, 2)

        ! constructs the Points P and Q that comprise the closest pair
        p => Point(x1, y1)
        q => Point(x2, y2)
        ! constructs the closest pair from the points P and Q and their distance
        closestPair => Pair(p, q, d_min)


        ! stores the number of operations (N * (N - 1) / 2) in a floating-point number
        N = real(endPosition - beginPosition, kind=real64)
        numOperations = ( N * (N - 1.0_real64) ) / 2.0_real64


        ! packs the closest pair and the number of operations into a Tuple object
        t => TupleConstructor(closestPair, numOperations)


        ! deallocates the temporary placeholders
        deallocate(p, q, closestPair)
        closestPair => null()
        p => null()
        q => null()

        return
    end function distanceSmallerPartition


    module function distancePartitionInterface (this, beginPosLeft, endPosLeft,&
                                               &beginPosRight, endPosRight, Px,&
                                               &currentClosestPair) result(t)
!
!   Synopsis:
!   Applies the Brute Force Algorithm to seek the closest pair at the interface of the
!   Left and Right partitions.
!
!   Inputs:
!   this                the ensemble
!
!   beginPosLeft        beginning position of the Left partition
!   endPosLeft          ending position of the Left partition
!
!   beginPosRight       beginning position of the Right partition
!   endPosRight         ending position of the Right partition
!
!   Px                  second-rank, dimension(N, 2), array storing the x, y coordinates
!                       of the `N' distinct Points that comprise the Ensemble
!
!   currentClosestPair  the closest pair thus far
!
!   Outputs:
!   t                   a tuple storing the closest pair and the number of operations
!
        class(Ensemble), intent(in) :: this
        type(Tuple), pointer :: t
        type(Pair), intent(in) :: currentClosestPair
        type(Pair), pointer :: closestPair
        class(Point), pointer :: p, q
        real(kind = real64), intent(in) :: Px(:, :)
        real(kind = real64) :: d, d_min
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        real(kind = real64) :: N1, N2
        real(kind = real64) :: numOperations
        integer(kind = int32), intent(in) :: beginPosLeft, endPosLeft
        integer(kind = int32), intent(in) :: beginPosRight, endPosRight
        integer(kind = int32) :: first, second
        integer(kind = int32) :: i, j


        ! gets a copy of the (squared) distance of the current closest pair
        d_min = currentClosestPair % getDistance()

        first = -1
        second = -1
        i = beginPosLeft
        ! considers pairs formed by a point in the left and another in the right partition
        do while (i /= endPosLeft)

            j = beginPosRight
            do while (j /= endPosRight)

                ! NOTE:
                ! Both i and j are zero-starting indexes by design, an offset is applied
                ! to avoid incurring in out-of-bouds errors because the Points array Px is
                ! a non-zero index starting array.
                x1 = Px(i + 1, 1)
                y1 = Px(i + 1, 2)

                x2 = Px(j + 1, 1)
                y2 = Px(j + 1, 2)

                d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)

                if (d < d_min) then
                    first = (i + 1)
                    second = (j + 1)
                    d_min = d
                end if

                j = j + 1
            end do

            i = i + 1
        end do


        if (first /= -1 .or. second /= -1) then ! creates a new closest pair object:

            ! gets the x, y coordinates of the first Point comprising the closest pair
            x1 = Px(first, 1)
            y1 = Px(first, 2)

            ! gets the x, y coordinates of the second Point comprising the closest pair
            x2 = Px(second, 1)
            y2 = Px(second, 2)

            ! constructs the Points P and Q that comprise the closest pair
            p => Point(x1, y1)
            q => Point(x2, y2)
            ! constructs the closest pair from the points P and Q and their distance
            closestPair => Pair(p, q, d_min)

            deallocate(p, q)

        else

           ! contructs a copy of the current closest pair object, for there are no
           ! pairs closer than the current closest pair
           closestPair => Pair(currentClosestPair)

        end if


        ! determines the number of distance computations including those done in the
        ! combine() step (it already takes into account if the nested for-loops execute)
        N1 = real(endPosLeft - beginPosLeft, kind=real64)
        N2 = real(endPosRight - beginPosRight, kind=real64)
        numOperations = ( (N1 * N2) + (N1 + 1.0_real64) + (N2 + 1.0_real64) )


        t => TupleConstructor(closestPair, numOperations)


        deallocate(closestPair)
        closestPair => null()
        p => null()
        q => null()

        return
    end function distancePartitionInterface


    ! Utilities:


    module subroutine toArray (this, points, array)
    ! copies the Points x, y coordinates into a second-rank array
        class(Ensemble), intent(in) :: this
        class(Point), intent(in) :: points(:)
        real(kind = real64), allocatable, intent(out) :: array(:, :)
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i

        allocate(array(this % size, 2), stat=mstat)

        if (mstat /= 0) then
            print *, 'Ensemble::toArray(): insufficient memory to allocate array'
            stop
        end if


        do i = 1, this % size
            array(i, 1) = points(i) % getX()
            array(i, 2) = points(i) % getY()
        end do

        return
    end subroutine toArray


    module pure function isInvalidData (this, points) result(isInvalid)
        class(Ensemble), intent(in) :: this
        class(Point), intent(in) :: points(:)
        logical(kind = int32) :: isInvalid
        integer(kind = int32) :: instances
        integer(kind = int32) :: i


        instances = 0
        ! increments the number of invalid instances whenever there is misplaced or a
        ! duplicate element (Point)
        do i = 1, (this % size - 1)
            if ( points(i + 1) % compareTo( points(i) ) <= 0 ) then
                instances = instances + 1
            end if
        end do


        if (instances /= 0) then
            isInvalid = .true.
        else
            isInvalid = .false.
        end if


        return
    end function isInvalidData


    module subroutine createDataset1D (this, points)
    ! creates a dataset of distinct points without duplicate closest pairs
        class(Ensemble), intent(in) :: this
        class(Point), allocatable, intent(out) :: points(:)

        call this % create(points)
        do while ( this % hasDuplicateClosestPair(points) )
                call this % create(points)
        end do

        return
    end subroutine


    module function hasDuplicateClosestPair (this, points) result(hasDuplicate)
    ! Uses Brute Force to find the first and second closest pairs. Returns true
    ! if the points that comprise the pairs are equidistant, returns false otherwise
    ! to signal the caller method that the Ensemble of Points has no duplicate closest
    ! pairs. Meaning that the Points that comprise the second closest pair are farther
    ! apart than those that comprise the first closest pair.
        class(Ensemble), intent(in) :: this
        class(Point), target, intent(in) :: points(:)
        class(Point), pointer :: p, q
        type(Pair), pointer :: firstclosestPair => null()
        type(Pair), pointer :: secondclosestPair => null()
        type(Pair), pointer :: pairObject => null()
        real(kind = real64) :: d
        real(kind = real64) :: d_min
        real(kind = real64) :: d_2nd
        integer(kind = int32) :: i, j
        logical(kind = int32) :: hasDuplicate


        ! instantiates the first and second closest pairs
        firstClosestPair => Pair()
        secondClosestPair => Pair()
        ! considers all distinct pairs in search for the closest pair
        i = 0
        do while ( i /= (this % size - 1) )

            j = (i + 1)
            do while (j /= this % size)

                p => points(i + 1)
                q => points(j + 1)
                d = p % distance(q)
                pairObject => Pair(p, q, d)

                if ( pairObject % compareTo(firstClosestPair) <= 0 ) then
                    secondClosestPair = firstClosestPair
                    firstClosestPair = pairObject
                endif

                deallocate(pairObject)
                pairObject => null()

                j = j + 1
            end do

            i = i + 1
        end do

        d_min = firstClosestPair % getDistance()
        d_2nd = secondClosestPair % getDistance()

        ! Note: we can get away with the inequality test because we are representing
        ! integers as floating-point numbers, for these have exact binary representations
        if (d_2nd /= d_min) then
            hasDuplicate = .false.
        else
            hasDuplicate = .true.
        endif

        deallocate(secondClosestPair)
        deallocate(firstClosestPair)
        secondClosestPair => null()
        firstClosestPair => null()

        return
    end function hasDuplicateClosestPair


    module subroutine create (this, points)
    ! returns an array of N distinct points, where N is the ensemble size
        class(Ensemble), intent(in) :: this
        class(Point), allocatable, intent(out) :: points(:)
        type(Vector), allocatable :: vec
        class(Point), pointer :: pnt => null()
        real(kind = real64) :: x, y
        real(kind = real64) :: x_min, x_max
        real(kind = real64) :: y_min, y_max
        real(kind = real64) :: size
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i

        ! allocates the supporting data structure (vector) of this method
        allocate(vec, stat=mstat)

        if (mstat /= 0) then
            print *, 'Ensemble::create(): insufficient memory to allocate dataset'
            stop
        end if


        ! defines limiting values to the x, y coordinates of the Points
        size = real(this % size, kind=real64)
        x_min = -size * size
        x_max = size * size
        y_min = -16
        y_max = 16


        ! instantiates the vector object via the size constructor Vector::Vector(size)
        vec = Vector(this % size)


        ! creates dataset of distinct points
        do i = 1, this % size

            ! generates pseudo-random numbers in the range [0, 1)
            call random_number(x)
            call random_number(y)
            ! maps the pseudo-random numbers to the range [min, max)
            x = x_min + real(floor( (x_max - x_min) * x, kind=int64), kind=real64)
            y = y_min + real(floor( (y_max - y_min) * y, kind=int64), kind=real64)
            ! creates a new Point object from pseudo-random x, y coordinates
            pnt => Point(x, y)

            ! discard duplicates until a distinct point is found
            do while ( vec % contains(pnt) )

                ! releases the duplicate Point object from memory
                deallocate(pnt)
                pnt => null()

                call random_number(x)
                call random_number(y)
                x = x_min + real(floor( (x_max - x_min) * x, kind=int64), kind=real64)
                y = y_min + real(floor( (y_max - y_min) * y, kind=int64), kind=real64)
                ! creates a new Point object from pseudo-random x, y coordinates
                pnt => Point(x, y)

            end do

            ! pushes (a copy of the) distinct point at the back of the vector
            call vec % push_back(pnt)
            ! frees the current distinct Point object from memory for the next iteration
            deallocate(pnt)
            pnt => null()

        end do

        ! copies the Point objects into an array of Points
        call vec % toArray(points)


        ! sorts the Points to support the Divide And Conquer Algorithm
        call insertionSort(this % size, points)


        ! releases the vector from memory for we don't need it anymore
        deallocate(vec)

        return
    end subroutine create


    module subroutine create2D (this, points)
    ! as the create() method but limits the point coordinates to a squared domain
        class(Ensemble), intent(in) :: this
        class(Point), allocatable, intent(out) :: points(:)
        type(Vector), allocatable :: vec
        class(Point), pointer :: pnt => null()
        real(kind = real64) :: x, y
        real(kind = real64) :: x_min, x_max
        real(kind = real64) :: y_min, y_max
        real(kind = real64) :: size
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i

        ! allocates the supporting data structure (vector) of this method
        allocate(vec, stat=mstat)

        if (mstat /= 0) then
            print *, 'Ensemble::create(): insufficient memory to allocate dataset'
            stop
        end if


        ! Defines limiting values for the x, y coordinates of the Points, contrary to
        ! the create() method both the x and y limiting values grow with the ensemble
        ! size. As a consquence of this, the Points are bounded to a squared domain.
        size = real(this % size, kind=real64)
        x_min = -size * size
        x_max = size * size
        y_min = -size * size
        y_max = size * size


        ! instantiates the vector object via the size constructor Vector::Vector(size)
        vec = Vector(this % size)


        ! creates dataset of distinct points
        do i = 1, this % size

            ! generates pseudo-random numbers in the range [0, 1)
            call random_number(x)
            call random_number(y)
            ! maps the pseudo-random numbers to the range [min, max)
            x = x_min + real(floor( (x_max - x_min) * x, kind=int64), kind=real64)
            y = y_min + real(floor( (y_max - y_min) * y, kind=int64), kind=real64)
            ! creates a new Point object from pseudo-random x, y coordinates
            pnt => Point(x, y)

            ! discard duplicates until a distinct point is found
            do while ( vec % contains(pnt) )

                ! releases the duplicate Point object from memory
                deallocate(pnt)
                pnt => null()

                call random_number(x)
                call random_number(y)
                x = x_min + real(floor( (x_max - x_min) * x, kind=int64), kind=real64)
                y = y_min + real(floor( (y_max - y_min) * y, kind=int64), kind=real64)
                ! creates a new Point object from pseudo-random x, y coordinates
                pnt => Point(x, y)

            end do

            ! pushes (a copy of the) distinct point at the back of the vector
            call vec % push_back(pnt)
            ! frees the current distinct Point object from memory for the next iteration
            deallocate(pnt)
            pnt => null()

        end do

        ! copies the Point objects into an array of Points
        call vec % toArray(points)


        ! sorts the Points to support the Divide And Conquer Algorithm
        call insertionSort(this % size, points)


        ! releases the vector from memory for we don't need it anymore
        deallocate(vec)

        return
    end subroutine create2D


    subroutine export (this, points)
    ! exports the coordinates that cause the recursive algorithm to fail for analysis
        class(Ensemble), intent(in) :: this
        real(kind = real64), intent(in) :: points(this % size, 2)
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        character(*), parameter :: coordinates = 'debug-positions.dat'

        open(newunit=fhandle, file=coordinates, action='write', position='rewind',&
            &iostat=iostate)

        if (iostate /= 0) then
            print *, 'UNEXPECTED IO ERROR'
            return
        end if

        do i = 1, this % size
            write(fhandle, *) points(i, 1), points(i, 2)
        end do

        close(fhandle)

        return
    end subroutine export


    ! Tests:


    module subroutine tests ()
        call test1()
        call test2()
        call test3()
        call test4()
        call test5()
        call test6()
        call test7()
        return
    end subroutine tests


    module subroutine test1 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! problem 2.3.1-1 (see reference [1] in main.for): OK
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (15, 5) and (17, 7).
    ! FORTRAN code yields the same answer as the implementation in Python:
    ! ~/Documents/UN/courses/complexity/pools/recursion/closest-pair/validation
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(12)
        real(kind = real64) :: coordinates(12, 2) = reshape([&
                &2,  4, 5, 10, 13, 15, 17, 19, 22, 25, 29, 30,& ! x coordinates
                &7, 13, 7,  5,  9,  5,  7, 10,  7, 10, 14,  2],&! y coordinates
                &shape=[12, 2])
        real(kind = real64) :: Px(12, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(12)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()

        print *, 'Problem 2.3.1-1:'
        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test1


    module subroutine test2 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! problem 2.3.1-2 (see reference [1] in main.for): OK
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (7, 8) and (9, 9).
    ! FORTRAN code yields the same answer as the implementation in Python:
    ! ~/Documents/UN/courses/complexity/pools/recursion/closest-pair/validation
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(12)
        real(kind = real64) :: coordinates(12, 2) = reshape([&
                &1,  1, 7, 9, 12, 13, 20, 22, 23, 25, 26, 31,& ! x coordinates
                &2, 11, 8, 9, 13,  4,  8,  3, 12, 14,  7, 10],&! y coordinates
                &shape=[12, 2])
        real(kind = real64) :: Px(12, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(12)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Problem 2.3.1-2:'
        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test2


    module subroutine test3 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! problem 2.3.1-3 (see reference [1] in main.for): OK
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (17, 13) and (15, 14).
    ! FORTRAN code yields the same answer as the implementation in Python:
    ! ~/Documents/UN/courses/complexity/pools/recursion/closest-pair/validation
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(12)
        real(kind = real64) :: coordinates(12, 2) = reshape([&
                &2,  2, 5,  9, 11, 15, 17, 18, 22, 25, 28, 30,& ! x coordinates
                &2, 12, 4, 11,  4, 14, 13,  7,  4,  7, 14,  2],&! y coordinates
                &shape=[12, 2])
        real(kind = real64) :: Px(12, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(12)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Problem 2.3.1-3:'
        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test3


    module subroutine test4 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! a custom problem: OK
    ! COMMENTS: TO UPDATE
    ! The closest pair is comprised by the 2D Points (0, 0) and (1, 1).
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(4)
        real(kind = real64) :: coordinates(4, 2) = reshape([&
                &0, 1, 4,  8, & ! x coordinates
                &0, 1, 5, -1],& ! y coordinates
                &shape=[4, 2])
        real(kind = real64) :: Px(4, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(4)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test4


    module subroutine test5 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! a custom problem: OK
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (7, -1) and (8, -1).
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(4)
        real(kind = real64) :: coordinates(4, 2) = reshape([&
                &-5, 1, 7,  8, & ! x coordinates
                &-1, 1,-1, -1],& ! y coordinates
                &shape=[4, 2])
        real(kind = real64) :: Px(4, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(4)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test5


    module subroutine test6 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! a custom problem: OK
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (1, -1) and (2, 1).
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        type(Point) :: points(4)
        real(kind = real64) :: coordinates(4, 2) = reshape([&
                &-15, 1, 2, 18, & ! x coordinates
                &-16,-1, 1, 14],& ! y coordinates
                &shape=[4, 2])
        real(kind = real64) :: Px(4, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: i


        ens = Ensemble(4)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, coordinates)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! pre-processing for the Divide And Conquer Algorithm:


        do i = 1, ens % size
        ! creates an array of points from the x, y coordinates
            x = coordinates(i, 1)
            y = coordinates(i, 2)
            p => Point(x, y)
            points(i) = p
            deallocate(p)
            p => null()
        end do


        call insertionSort(ens % size, points)  ! sorts the array of Points


        do i = 1, ens % size
        ! copies the sorted array of points into an array of x, y coordinates
            Px(i, 1) = points(i) % getX()
            Px(i, 2) = points(i) % getY()
        end do


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test6


    module subroutine test7 ()
    ! Checks the implementation of the closest pair finding algorithms by solving
    ! a problem that has caused the recursive algorithm to fail.
    ! COMMENTS:
    ! The closest pair is comprised by the 2D Points (-100, 10) and (-99, 13).
        type(Ensemble) :: ens
        type(Tuple), pointer :: t
        type(Tuple), pointer :: tupleBruteForce
        type(Pair), pointer :: closestPairBruteForce
        type(Pair), pointer :: closestPair
        type(Point), pointer :: p
        real(kind = real64) :: Px(16, 2)
        real(kind = real64) :: x, y
        integer(kind = int32) :: fhandle
        integer(kind = int32) :: iostate
        integer(kind = int32) :: i
        character(*), parameter :: debug = 'debug-positions.dat'


        ! imports the data that caused the recursive algorithm to fail
        open(newunit=fhandle, file=debug, action='read', position='rewind',&
            &iostat=iostate)

        if (iostate /= 0) then
            print *, 'Ensemble::test7(): unexpected io error'
            stop
        end if

        do i = 1, 16
            read(fhandle, '(2E25.15)') Px(i, 1), Px(i, 2)
        end do

        close(fhandle)


        ens = Ensemble(16)
        ! uses Brute Force to find the closest pair
        tupleBruteForce => ens % distance(0, ens % size, Px)
        closestPairBruteForce => tupleBruteForce % getClosestPair()


        print *, 'Brute Force:'
        call closestPairBruteForce % print()


        ! applies the Divide And Conquer Algorithm
        t => ens % recurse(0, ens % size, Px)
        closestPair => t % getClosestPair()


        print *, 'Divide And Conquer:'
        call closestPair % print()


        ! frees dynamically allocated variables from memory
        deallocate(t, tupleBruteForce, closestPair, closestPairBruteForce)
        closestPair => null()
        closestPairBruteForce => null()
        tupleBruteForce => null()
        t => null()

        return
    end subroutine test7

end submodule
