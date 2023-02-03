! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines the Vector<Point> Class.
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

module VectorClass
use, intrinsic :: iso_fortran_env, only: int32, real64
use :: PointClass, only: Point
implicit none
private
save

    integer(kind = int32), parameter :: DEFAULT_SIZE = 16

    type, public :: Vector
        private                                 ! Components:
        integer(kind = int32) :: begin          ! beginning of the data array
        integer(kind = int32) :: avail          ! available for storage
        integer(kind = int32) :: limit          ! limiting size
        class(Point), allocatable :: data(:)    ! data array, placeholder of Points
        contains
            private                             ! Methods:
            procedure :: grow
            procedure :: fitToSize
            procedure :: assignOperator
            procedure, public :: size           ! returns the size of the vector
            procedure, public :: print          ! returns the size of the vector
            procedure, public :: push_back      ! inserts data at the back of the vector
            procedure, public :: search         ! returns the target's positional index
            procedure, public :: contains       ! returns true if the target is present
            procedure, public :: toArray        ! returns a copy of the data array
            generic, public :: assignment(=) => assignOperator
            final :: destructor                 ! frees allocated resources
    end type


    ! Constructor(s):


    interface Vector
        module procedure defaultConstructor
        module procedure Constructor
    end interface


    interface

        module function defaultConstructor () result(this)
        ! constructs an empty Vector of default storage capacity
            type(Vector) :: this
        end function

        module function Constructor (size) result(this)
        ! constructs a Vector with the requested storage capacity
            type(Vector) :: this
            integer(kind = int32), intent(in) :: size
        end function

    end interface


    ! Destructor:


    interface

        module subroutine destructor (this)
        ! frees the data placeholder array from memory
            type(Vector), intent(inout) :: this
        end subroutine

    end interface


    ! Operator(s):


    interface

        module subroutine assignOperator (this, vec)
            class(Vector), intent(inout) :: this
            class(Vector), intent(in) :: vec
        end subroutine

    end interface


    ! Getter(s):


    interface

        module subroutine toArray (this, array)
        ! returns a copy of the contained data array
            class(Vector), intent(in) :: this
            class(Point), allocatable, intent(out) :: array(:)
        end subroutine

    end interface


    ! Methods:


    interface

        module function size (this) result(sz)
        ! returns the number of elements contained in the vector
            class(Vector), intent(in) :: this
            integer(kind = int32) :: sz
        end function


        module subroutine print (this)
        ! prints the vector elements on the console
            class(Vector), intent(in) :: this
        end subroutine


        module subroutine push_back (this, pointObject)
        ! pushes data at the back of the vector
            class(Vector), intent(inout) :: this
            class(Point), intent(in) :: pointObject
        end subroutine


        module function search (this, pointObject) result(pos)
        ! returns the positional index of the target `pointObject'
            class(Vector), intent(in) :: this
            class(Point), intent(in) :: pointObject
            integer(kind = int32) :: pos
        end function


        module function contains (this, pointObject) result(cont)
        ! returns true if the target `pointObject' is present, returns false otherwise
            class(Vector), intent(in) :: this
            class(Point), intent(in) :: pointObject
            logical(kind = int32) :: cont
        end function

    end interface


    ! Utilities:


    interface

        module subroutine grow (this)
        ! doubles the vector storage capacity
            class(Vector), intent(inout) :: this
        end subroutine


        module subroutine fitToSize (this)
            class(Vector), intent(inout) :: this
        end subroutine

    end interface

end module
