! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Defines Algorithms that support the closest pair finding algorithms.
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

module Algorithm
use, intrinsic :: iso_fortran_env, only: int32, real64
implicit none
private
public :: compare
save

    interface

        module pure function compare (x1, x2) result(comp)
        ! defines the compare method for double precision floating-point numbers
            real(kind = real64), intent(in) :: x1
            real(kind = real64), intent(in) :: x2
            integer(kind = int32) :: comp
        end function

    end interface

end module
