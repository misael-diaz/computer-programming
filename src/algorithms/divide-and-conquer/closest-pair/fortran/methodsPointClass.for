! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Point Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!
! References:
! [0] No Short-Circuit Logical Operators: fortranwiki.org/fortran/show/short-circuiting

submodule (PointClass) methods
implicit none
contains


    ! Constructors:


    module function defaultConstructor () result(this)
    ! constructs a Point at the origin (0, 0)
        class(Point), pointer :: this
        integer(kind = int32) :: mstat

        allocate(Point::this, stat=mstat)

        if (mstat /= 0) then
            print *, 'Point::Point() memory allocation of Point object failed'
            stop
        end if

        this % x = 0.0_real64
        this % y = 0.0_real64

        return
    end function


    module function PointConstructor (x, y) result(this)
    ! constructs a Point from x, y coordinates
        class(Point), pointer :: this
        real(kind = real64), intent(in) :: x
        real(kind = real64), intent(in) :: y
        integer(kind = int32) :: mstat

        allocate(Point::this, stat=mstat)

        if (mstat /= 0) then
            print *, 'Point::Point() memory allocation of Point object failed'
            stop
        end if

        this % x = x
        this % y = y

        return
    end function


    module function Point3DConstructor (x, y, z) result(this)
    ! constructs a 3D Point from x, y, z coordinates
        class(Point), pointer :: this
        real(kind = real64), intent(in) :: x
        real(kind = real64), intent(in) :: y
        real(kind = real64), intent(in) :: z
        integer(kind = int32) :: mstat

        allocate(Point3D::this, stat=mstat)

        if (mstat /= 0) then
            print *, 'Point::Point3D() memory allocation of Point object failed'
            stop
        end if

        ! asserts that the type of the returned object is Point3D
        select type (this)
            type is (Point3D)
                this % x = x
                this % y = y
                this % z = z
        end select

        return
    end function


    module function copyConstructor (pointObject) result(this)
    ! returns a copy of the passed Point object
        class(Point), intent(in) :: pointObject
        class(Point), pointer :: this
        integer(kind = int32) :: mstat

        select type (pointObject)

            type is (Point3D)

                allocate(Point3D::this, stat=mstat)

                if (mstat /= 0) then
                    print *, 'Point::Point() memory allocation of Point object failed'
                    stop
                end if

                ! asserts that the type of the returned object is Point3D
                select type (this)
                    type is (Point3D)
                        this % x = pointObject % getX()
                        this % y = pointObject % getY()
                        this % z = pointObject % getZ()
                end select

            class default

                allocate(Point::this, stat=mstat)

                if (mstat /= 0) then
                    print *, 'Point::Point() memory allocation of Point object failed'
                    stop
                end if

                this % x = pointObject % x
                this % y = pointObject % y

        end select

        return
    end function copyConstructor


    ! Operators:


    module subroutine Point_assignment (this, otherPoint)
    ! implements the assigment operation for Point objects
        class(Point), intent(out) :: this
        class(Point), intent(in) :: otherPoint

        select type (this)
            type is (Point3D)
                this % x = otherPoint % getX()
                this % y = otherPoint % getY()
                this % z = otherPoint % getZ()
            class default
                this % x = otherPoint % x
                this % y = otherPoint % y
        end select

        return
    end subroutine


    ! Getters:


    module pure function getX (this) result(x)
    ! returns a copy of the x-axis coordinate
        class(Point), intent(in) :: this
        real(kind = real64) :: x

        x = this % x

        return
    end function


    module pure function getY (this) result(y)
    ! returns a copy of the y-axis coordinate
        class(Point), intent(in) :: this
        real(kind = real64) :: y

        y = this % y

        return
    end function


    module pure function Point_getZ (this) result(z)
    ! returns zero for the Point is a Point object
        class(Point), intent(in) :: this
        real(kind = real64) :: z

        z = 0.0_real64

        return
    end function


    module pure function Point3D_getZ (this) result(z)
    ! returns a copy of the z-axis coordiante of the 3D Point object
        class(Point3D), intent(in) :: this
        real(kind = real64) :: z

        z = this % z

        return
    end function


    ! Methods:


    module subroutine Point_print (this)
    ! prints the components of the Point object
        class(Point), intent(in) :: this

        print *, 'x: ', this % x, ' y: ', this % y

        return
    end subroutine


    module subroutine Point3D_print (this)
    ! prints the components of the 3D Point object
        class(Point3D), intent(in) :: this

        print *, 'x: ', this % x, ' y: ', this % y, ' z: ', this % z

        return
    end subroutine


    module pure function Point_compareTo (this, pointObject) result(comp)
    ! compares Points primarily by their x and secondarily by their y-axis coordinates
        class(Point), intent(in) :: this
        class(Point), intent(in) :: pointObject
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        integer(kind = int32) :: comp

        x1 = this % x
        y1 = this % y

        x2 = pointObject % x
        y2 = pointObject % y

        ! Note: we can get away with the inequality test because we are representing
        ! integers as floating-point numbers, for these have exact binary representations
        if (x1 /= x2) then
            comp = compare(x1, x2)
        else
            comp = compare(y1, y2)
        end if

        return
    end function Point_compareTo


    module pure function Point3D_compareTo (this, pointObject) result(comp)
    ! compares Points firstly by their x, secondarily by their y-axis coordinates
    ! and thirdly by their z-axis coordinates
        class(Point3D), intent(in) :: this
        class(Point), intent(in) :: pointObject
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        real(kind = real64) :: z1, z2
        integer(kind = int32) :: comp

        x1 = this % getX()
        y1 = this % getY()
        z1 = this % getZ()

        x2 = pointObject % getX()
        y2 = pointObject % getY()
        z2 = pointObject % getZ()

        ! Note: we can get away with the inequality test because we are representing
        ! integers as floating-point numbers, for these have exact binary representations
        if (x1 /= x2) then
            comp = compare(x1, x2)
        else if (y1 /= y2) then
            comp = compare(y1, y2)
        else
            comp = compare(z1, z2)
        end if

        return
    end function Point3D_compareTo


    module pure function Point_distance (this, pointObject) result(dist)
    ! computes the squared distance between two Point objects
        class(Point), intent(in) :: this
        class(Point), intent(in) :: pointObject
        real(kind = real64) :: dist
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2

        x1 = this % x
        y1 = this % y

        x2 = pointObject % x
        y2 = pointObject % y

        dist = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)

        return
    end function


    module pure function Point3D_distance (this, pointObject) result(dist)
    ! computes the squared distance between two 3D Point objects
        class(Point3D), intent(in) :: this
        class(Point), intent(in) :: pointObject
        real(kind = real64) :: dist
        real(kind = real64) :: x1, x2
        real(kind = real64) :: y1, y2
        real(kind = real64) :: z1, z2

        x1 = this % getX()
        y1 = this % getY()
        z1 = this % getZ()

        x2 = pointObject % getX()
        y2 = pointObject % getY()
        z2 = pointObject % getZ()

        dist = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1)

        return
    end function


    ! Algorithms:


    module subroutine InsertionSort (N, points)
    ! implements the Insertion Sort Algorithm for an array of Points, sorts the points
    ! in ascending order
        integer(kind = int32), intent(in) :: N
        class(Point), intent(inout) :: points(:)
        class(Point), pointer :: insertionElement
        integer(kind = int32) :: mstat
        integer(kind = int32) :: pos
        integer(kind = int32) :: i

        ! allocates a temporary Point object with the type contained in the array,
        ! Note: we are assuming that the array is homogeneous either an Array<Point>
        ! or Array<Point3D>
        allocate(insertionElement, mold=points(1), stat=mstat)

        ! Do-loop invariant:
        ! The array elements in the asymmetric range [0, i) are sorted in ascending order.
        do i = 2, N
            ! gets the insertion element
            insertionElement = points(i)
            ! gets the position of the preceeding element
            pos = (i - 1)

            ! While-loop invariant:
            ! Thus far we have shifted [(i - 1) - pos] preceeding element(s) larger than
            ! the insertion element.
            do while (pos > 0)

                ! NOTE:
                ! Short-circuit logical .AND. (&&) is not part of the FORTRAN Standard,
                ! thus, the implementation differs from my implementation in Java.
                if (points(pos) % compareTo(insertionElement) > 0) then
                        points(pos + 1) = points(pos)
                        pos = (pos - 1)
                else
                    exit
                end if

            end do

            ! the current element at `pos' is less than or equal to the insertion element,
            ! hence, we have found the right position for writing the insertion element
            points(pos + 1) = insertionElement

        end do

        deallocate(insertionElement)

        return
    end subroutine InsertionSort


    ! Tests:


    module subroutine tests ()
        call testInsertionSort()
        return
    end subroutine


    module subroutine testInsertionSort ()
    ! tests the implementation of the Insertion Sort Algorithm
        class(Point), pointer :: pnt
        class(Point), allocatable :: points(:)
        real(kind = real64) :: x, y, z
        real(kind = real64) :: x_min, y_min, z_min
        real(kind = real64) :: x_max, y_max, z_max
        real(kind = real64) :: float_arraySize
        integer(kind = int32), parameter :: arraySize = 16384
        integer(kind = int32), parameter :: reps = 256
        integer(kind = int32) :: misplacements
        integer(kind = int32) :: mstat
        integer(kind = int32) :: rep, i


        ! allocates an array of 3D Points
        allocate(Point3D::points(arraySize), stat=mstat)

        if (mstat /= 0) then
            print *, 'Point::testInsertionSort() memory allocation failed'
            stop
        end if


        ! defines limits for the x, y, and z coordinates of the Points
        float_arraySize = real(arraySize, kind=real64)
        x_min = -float_arraySize * float_arraySize
        y_min = -float_arraySize * float_arraySize
        z_min = -float_arraySize * float_arraySize

        x_max = float_arraySize * float_arraySize
        y_max = float_arraySize * float_arraySize
        z_max = float_arraySize * float_arraySize


        misplacements = 0
        do rep = 1, reps

            ! fills the array of points with pseudo-random x, y, z values
            do i = 1, arraySize
                call random_number(x)
                call random_number(y)
                call random_number(z)
                x = x_min + real( (x_max - x_min) * x, kind=real64 )
                y = y_min + real( (y_max - y_min) * y, kind=real64 )
                z = z_min + real( (z_max - z_min) * z, kind=real64 )
                pnt => Point(x, y, z)
                points(i) = pnt
                deallocate(pnt)
            end do

            call InsertionSort(arraySize, points)       ! sorts the array

            ! counts the number of misplaced (or out-of-order) array elements
            do i = 1, (arraySize - 1)
                if ( points(i + 1) % compareTo( points(i) ) < 0 ) then
                    misplacements = misplacements + 1
                end if
            end do

        end do


        ! reports to the user the test outcome
        write (*, '(A)', advance='no') 'test-sort[0]:'
        if (misplacements /= 0) then
            print *, 'FAIL'
        else
            print *, 'pass'
        end if

        return
    end subroutine testInsertionSort

end submodule
