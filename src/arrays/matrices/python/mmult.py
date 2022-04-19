#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Programming II                                April 19, 2022
IST 2089
Prof. M Diaz-Maldondo

Synopsis:
Solves the following matrix multiplication problem:

                            D = B' * A * C

and outputs the requested element on the console.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
"""


from numpy import array
from numpy import matmul


A = array([
             9, 45, 39, 16, 33, 58,
            60, 54, 11,  3, 24, 47,
            56, 23, 63,  7, 18, 53,
            22, 17, 12, 38, 30, 21,
            57, 20, 35, 36, 19, 10,
            14, 32,  6, 28,  8, 51
]).reshape([6, 6])


B = array([
            21, 25, 17, 33, 54,  3,
            48, 40,  8, 63,  9, 53,
            13, 11, 24,  5, 60, 52,
             1, 10, 56,  7, 55, 44,
            26, 22, 28, 62, 45, 41,
            39, 18, 31, 29,  4, 50
]).reshape([6, 6])


C = array([
            30, 60, 36, 14, 61, 34,
            57, 18, 43, 13, 11, 53,
            20,  5, 23, 26, 27, 49,
            37, 28, 62, 10,  1,  7,
            46, 21, 22, 58, 52,  9,
            16, 33, 25, 45, 12, 40
]).reshape([6, 6])


D = matmul(matmul(B.T, A), C)


"""
Answer:
Input D(1, 2) (integer) in the answer field."
"""
i, j = 1, 2
ans = f"D({i}, {j}) = {D[i-1][j-1]}"
print(ans)
