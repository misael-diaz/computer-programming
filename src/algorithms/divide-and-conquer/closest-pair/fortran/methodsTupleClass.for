! Algorithms and Complexity                                January 27, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Tuple Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (TupleClass) methods
implicit none
contains


    ! Constructors:


    module function defaultConstructor () result(this)
    ! useless default constructor
        type(Tuple), pointer :: this
        integer(kind = int32) :: mstat

        allocate(this, stat=mstat)
        if (mstat /= 0) then
            print *, 'Tuple::Tuple(): memory allocation of Tuple object failed'
            stop
        end if

        this % closestPair => Pair()
        this % numOperations = 0.0_real64

        return
    end function


    module function Constructor (closestPair, numOperations) result(this)
    ! constructs from the (current) closest pair and the number of operations done so far
        type(Tuple), pointer :: this
        type(Pair), intent(in) :: closestPair
        real(kind = real64), intent(in) :: numOperations
        integer(kind = int32) :: mstat

        allocate(this, stat=mstat)
        if (mstat /= 0) then
            print *, 'Tuple::Tuple(): memory allocation of Tuple object failed'
            stop
        end if

        this % closestPair => Pair(closestPair)
        this % numOperations = numOperations

        return
    end function Constructor


    ! Destructor:


    module subroutine Destructor (this)
    ! releases from memory the current closest pair object
        type(Tuple), intent(inout) :: this

        if ( associated(this % closestPair) ) then
            deallocate(this % closestPair)
            this % closestPair => null()
        end if

        return
    end subroutine


    ! Operators:


    module subroutine assignmentOperator (this, tupleObject)
    ! disables the assignment operator for tuples, warns the user at runtime
        class(Tuple), intent(inout) :: this
        type(Tuple), intent(in) :: tupleObject

        ! note: we could generate a compile time error if this were C++ code
        print *, 'Tuple::assignment(=): Assignment operation has been ignored'

        return
    end subroutine


    ! Getters:


    module pure function getNumOperations (this) result(numOperations)
    ! returns the number of operations done (thus far) to find the closest pair
        class(Tuple), intent(in) :: this
        real(kind = real64) :: numOperations

        numOperations = this % numOperations

        return
    end function


    module function getClosestPair (this) result(closestPair)
    ! returns a copy of the reference to the closest pair object (if any) otherwise NULL
    ! NOTE:
    ! We do not check for NULL elsewhere because we don't have to since we only create a
    ! Tuple object whenever a possible closest pair has been found. We are just being
    ! thorough in the implementation of this getter.
        class(Tuple), intent(in) :: this
        type(Pair), pointer :: closestPair

        if ( associated(this % closestPair) ) then
            closestPair => Pair(this % closestPair)
        else
            closestPair => null()
        end if

        return
    end function


    ! Methods: None


end submodule
