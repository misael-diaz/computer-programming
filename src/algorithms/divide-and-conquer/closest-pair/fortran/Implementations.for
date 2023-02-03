! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Implements the Algorithms that support closest pair finding algorithms.
!
!
! Copyright (C) 2023 Misael Diaz-Maldonado
! This program is free software: you can redistribute it and/or modify
! it under the terms of the GNU General Public License as published by
! the Free Software Foundation, either version 3 of the License, or
! (at your option) any later version.
!

submodule (Algorithm) implement
implicit none
contains

    module pure function compare (x1, x2) result(comp)
    ! returns 0 if x1 == x2, returns 1 if x1 > x2, and returns -1 if x1 < x2
        real(kind = real64), intent(in) :: x1, x2
        integer(kind = int32) :: comp

        ! Note: we can get away with the equality test because we are representing
        ! integers as floating-point numbers, for these have exact binary representations
        if (x1 == x2) then
            comp = 0
        elseif (x1 > x2) then
            comp = 1
        else
            comp = -1
        end if

        return
    end function

end submodule
