! Algorithms and Complexity                                January 20, 2023
! IST 4310
! Prof M Diaz-Maldonado
!
! Synopsis:
! Tests the implementation of the algorithms.
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
! [1] JJ McConnell, Analysis of Algorithms, second edition
! [2] OOP FORTRAN: https://gist.github.com/n-s-k/522f2669979ed6d0582b8e80cf6c95fd

#define RUNS 8

program main
!   use, intrinsic :: iso_fortran_env, only: int32, real64
    use :: TimeComplexityClass, only: TimeComplexity
    use :: EnsembleClass, only: Ensemble
    use :: EnsembleClass, only: tests => Ensemble_tests
    implicit none
    type(TimeComplexity) :: t

    t = TimeComplexity(RUNS)
    ! exports the time complexity results of the 1D Divide And Conquer Algorithm
!   call t % exportTimeComplexity_DivideAndConquer1D()
    call t % exportTimeComplexity_DivideAndConquer1D2() ! (version 2)

end program
