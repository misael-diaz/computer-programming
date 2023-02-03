! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Pair Class.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (PairClass) methods
implicit none
contains


    ! Constructors:


    module function defaultConstructor () result(this)
    ! constructs a Pair object from two infinitely separated Points
        type(Pair), pointer :: this
        real(kind = real64) :: inf
        integer(kind = int32) :: mstat


        allocate(this, stat=mstat)
        if (mstat /= 0) then
            print *, 'Pair::Pair(): memory allocation of Pair object failed'
            stop
        end if


        ! stores the floating-point representation of positive infinity
        inf = ieee_value(0.0_real64, ieee_positive_inf)


        this % first => Point()                 ! creates a Point at the origin (0, 0)
        this % second => Point(inf, 0.0_real64) ! creates a Point at infinity (inf, 0)
        this % distance = inf                   ! stores the infinite distance

        return
    end function defaultConstructor


    module function Constructor (p, q, d) result(this)
    ! creates a Pair object from the Points P and Q and their squared distance
        type(Pair), pointer :: this
        class(Point), pointer, intent(in) :: p
        class(Point), pointer, intent(in) :: q
        real(kind = real64), intent(in) :: d
        integer(kind = int32) :: mstat


        allocate(this, stat=mstat)
        if (mstat /= 0) then
            print *, 'Pair::Pair(): memory allocation of Pair object failed'
            stop
        end if


        ! places the smaller Point first by design
        if (p % compareTo(q) < 0) then
            this % first => Point(p)
            this % second => Point(q)
        else
            this % first => Point(q)
            this % second => Point(p)
        end if

        this % distance = d

        return
    end function Constructor


    module function copyConstructor (pairObject) result(this)
        type(Pair), intent(in) :: pairObject
        type(Pair), pointer :: this
        integer(kind = int32) :: mstat

        allocate(this, stat=mstat)
        if (mstat /= 0) then
            print *, 'Pair::Pair(): memory allocation of Pair object failed'
            stop
        end if

        this % first => Point(pairObject % first)
        this % second => Point(pairObject % second)
        this % distance = pairObject % distance

        return
    end function copyConstructor


    ! Destructor:


    module subroutine Destructor (this)
    ! delegates the memory handling
        type(Pair), intent(inout) :: this

        call destroy(this)

        return
    end subroutine Destructor


    module subroutine destroy (this)
    ! releases Point objects from memory created by the Pair class Constructors
        type(Pair), intent(inout) :: this

        if ( associated(this % first) ) then
                deallocate(this % first)
                this % first => null()
        end if

        if ( associated(this % second) ) then
                deallocate(this % second)
                this % second => null()
        end if

        return
    end subroutine destroy


    module subroutine assignmentOperator (this, otherPair)
    ! copies the references of Point objects from other Pair object into `this' Pair
        class(Pair), intent(out) :: this
        class(Pair), intent(in) :: otherPair

        ! caters self-assignment
        if ( loc(this) /= loc(otherPair) ) then

            ! releases dynamically allocated Points by the class Constructors
            call destroy(this)

            this % first => Point(otherPair % first)
            this % second => Point(otherPair % second)
            this % distance = otherPair % distance

        end if

        return
    end subroutine assignmentOperator


    ! Getters:


    module pure function getDistance (this) result(distance)
    ! returns a copy of the squared separating distance of the points comprising the Pair
        class(Pair), intent(in) :: this
        real(kind = real64) :: distance

        distance = this % distance

        return
    end function


    ! Methods:


    module subroutine print (this)
    ! prints info about the Pair object on the console
        class(Pair), intent(in) :: this

        print *, 'first:'
        call this % first % print()

        print *, 'second:'
        call this % second % print()

        print *, 'distance:', this % distance

        return
    end subroutine


    module pure function compareTo (this, pairObject) result(comp)
        class(Pair), intent(in) :: this
        class(Pair), intent(in) :: pairObject
        real(kind = real64) :: d1, d2
        integer(kind = int32) :: comp

        d1 = this % distance
        d2 = pairObject % distance

        comp = compare(d1, d2)

        return
    end function

end submodule
