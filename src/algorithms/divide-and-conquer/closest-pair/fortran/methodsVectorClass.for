! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Vector<Point> Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (VectorClass) methods
implicit none
contains


    ! Constructors:


    module function defaultConstructor () result(this)
        type(Vector) :: this
        integer(kind = int32) :: mstat

        this % begin = 0
        this % avail = 0
        this % limit = DEFAULT_SIZE

        allocate(this % data( this % begin : (DEFAULT_SIZE - 1) ), stat=mstat)

        if (mstat /= 0) then
            print *, 'Vector::Vector(): insufficient memory to allocate vector'
            stop
        end if

        return
    end function


    module function Constructor (size) result(this)
        type(Vector) :: this
        integer(kind = int32), intent(in) :: size
        integer(kind = int32) :: mstat

        this % begin = 0
        this % avail = 0
        this % limit = max(DEFAULT_SIZE, size) ! caters invalid sizes

        allocate(this % data( this % begin : (this % limit - 1) ), stat=mstat)

        if (mstat /= 0) then
            print *, 'Vector::Vector(size): insufficient memory to allocate vector'
            stop
        end if

        return
    end function


    ! Destructor:


    module subroutine destructor (this)
        type(Vector), intent(inout) :: this

        if ( allocated(this % data) ) then
            deallocate(this % data)
        end if

        return
    end subroutine


    ! Operator(s):


    module subroutine assignOperator (this, vec)
        class(Vector), intent(inout) :: this
        class(Vector), intent(in) :: vec
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i

        ! caters self-assignment by checking the memory addresses of the source vector
        ! `vec' and the destination vector `this'
        ! NOTE:
        ! We use intent(inout) for `this' vector because the method would erase any
        ! contained data upon self-assignment if the intent is `intent(out)'.
        if ( loc(this) /= loc(vec) ) then

            if ( allocated(vec % data) ) then

                this % begin = vec % begin
                this % avail = vec % avail
                this % limit = vec % limit

                if ( .not. allocated(this % data) ) then

                    allocate(this % data( 0 : (this % limit - 1) ), stat=mstat)

                    if (mstat /= 0) then
                        print *, 'Vector::assignment(=): allocation failed'
                        stop
                    end if

                else

                    deallocate(this % data)
                    ! reallocates the placeholder of `this' vector to match the storage
                    ! capacity of the source vector `vec'
                    allocate(this % data( 0 : (this % limit - 1) ), stat=mstat)

                    if (mstat /= 0) then
                        print *, 'Vector::assignment(=): allocation failed'
                        stop
                    end if

                end if

                i = 0
                ! copies data from the source vector `vec' into the destination `this'
                do while ( i /= vec % size() )
                    ! Note: the while-loop construction caters for empty vectors
                    this % data(i) = vec % data(i)
                    i = i + 1
                end do

            else
                print *, 'Vector::assignment(=): source vector is not instantiated'
                stop
            end if

        end if

        return
    end subroutine



    ! Methods:


    module function size (this) result(sz)
    ! returns the number of elements contained in the vector
        class(Vector), intent(in) :: this
        integer(kind = int32) :: sz

        sz = (this % avail - this % begin)

        return
    end function


    module subroutine print (this)
    ! invokes the print method of the Point objects
        class(Vector), intent(in) :: this
        integer(kind = int32) :: n

        n = 0
        do while ( n /= this % size() )
            call this % data(n) % print()
            n = n + 1
        end do

        return
    end subroutine


    module subroutine push_back (this, pointObject)
    ! pushes Point object at the back of the vector
        class(Vector), intent(inout) :: this
        class(Point), intent(in) :: pointObject


        if ( .not. allocated(this % data) ) then
            print *, 'Vector::push_back(): Vector must be instantiated'
            stop
        end if

        ! grows the vector if there is no more space for storage
        if (this % avail == this % limit) then
            call this % grow()
        end if

        associate(data => this % data, avail => this % avail)
            data(avail) = pointObject   ! writes at the available position
            avail = avail + 1           ! increments for the next element
        end associate

        return
    end subroutine


    module function search (this, pointObject) result(pos)
    ! returns the positional index of the Point object in the vector
        class(Vector), intent(in) :: this
        class(Point), intent(in) :: pointObject
        integer(kind = int32) :: pos
        integer(kind = int32) :: n

        n = 0
        pos = 0 ! initializes the positional index with an invalid index
        do while ( n /= this % size() )
            if (this % data(n) % compareTo(pointObject) == 0) then
                pos = n + 1
                exit
            end if
            n = n + 1
        end do

        return
    end function


    module function contains (this, pointObject) result(cont)
    ! returns true if the target `pointObject' is present, returns false otherwise
        class(Vector), intent(in) :: this
        class(Point), intent(in) :: pointObject
        logical(kind = int32) :: cont

        if ( this % search(pointObject) /= 0 ) then
            cont = .true.
        else
            cont = .false.
        end if

        return
    end function


    module subroutine toArray (this, array)
    ! copies the contained data into an array
        class(Vector), intent(in) :: this
        class(Point), allocatable, intent(out) :: array(:)
        integer(kind = int32) :: mstat
        integer(kind = int32) :: numel
        integer(kind = int32) :: i


        if ( .not. allocated(this % data) ) then
            print *, 'Vector::toArray(): Vector must be instantiated'
            stop
        end if


        if ( allocated(array) ) then
            deallocate(array)
        endif


        numel = this % size()
        ! creates a zero starting-index array
        allocate (array(numel), stat=mstat)

        if (mstat /= 0) then
            print *, 'Vector::toArray(): insufficient memory to allocate array'
            stop
        end if


        i = 0
        ! copies the contained data into the array
        do while ( i /= this % size() )
            array(i + 1) = this % data(i)
            i = i + 1
        end do

        return
    end subroutine toArray


    ! Utilities:


    module subroutine grow (this)
    ! doubles the storage capacity of the vector
        class(Vector), intent(inout) :: this
        class(Point), allocatable :: tmp(:)
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i, n


        ! allocates array temporary for storing the existing data
        allocate(tmp( 1 : this % size() ), stat=mstat)
        if (mstat /= 0) then
            print *, 'Vector::grow(): failed to allocate array temporary'
            stop
        end if


        n = 0
        i = 1
        ! copies the contained data into the array temporary to create a backup
        do while ( n /= this % size() )
            tmp(i) = this % data(n)
            i = i + 1
            n = n + 1
        end do


        ! doubles the storage capacity of the vector
        this % limit = this % begin + 2 * (this % limit - this % begin)


        deallocate(this % data)
        ! reallocates data placeholder array of the vector accordingly
        allocate(this % data( this % begin : (this % limit - 1) ), stat=mstat)
        if (mstat /= 0) then
            print *, 'Vector::grow(): failed to increase the vector storage capacity'
            stop
        end if


        n = 0
        i = 1
        ! copies the elements of the array temporary into the vector to restore the data
        do while ( n /= this % size() )
            this % data(n) = tmp(i)
            i = i + 1
            n = n + 1
        end do

        ! releases the array temprary from memory
        deallocate(tmp)

        return
    end subroutine grow


    module subroutine fitToSize (this)
        class(Vector), intent(inout) :: this
        class(Point), allocatable :: tmp(:)
        integer(kind = int32) :: numel
        integer(kind = int32) :: mstat
        integer(kind = int32) :: i


        numel = this % size()
        allocate ( tmp( 0 : (numel - 1) ), stat=mstat )

        if (mstat /= 0) then
            print *, 'Vector::fitToSize(): failed to allocate array temporary'
            stop
        end if


        i = 0
        ! copies vector elements into array temporary
        do while ( i /= this % size() )
            tmp(i) = this % data(i)
            i = i + 1
        end do


        deallocate(this % data)
        ! fits container memory allocation to size
        allocate(this % data( 0 : (numel - 1) ), stat=mstat)

        if (mstat /= 0) then
            print *, 'Vector::fitToSize(): failed to reallocate vector data array'
            stop
        end if


        i = 0
        ! copies data back to the vector
        do while ( i /= this % size() )
            this % data(i) = tmp(i)
            i = i + 1
        end do

        deallocate(tmp)

        return
    end subroutine fitToSize

end submodule
