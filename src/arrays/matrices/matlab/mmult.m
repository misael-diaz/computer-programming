% Algorithms and Programming II                              April 19, 2022
% IST 2089
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Code shows how to perform some matrix algebra operations in MATLAB.
%
%
% Copyright (c) 2022 Misael Diaz-Maldonado
% This file is released under the GNU General Public License as published
% by the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.
%
%
% References:
% [0] A Gilat, MATLAB: An Introduction with Applications, 6th edition.

clear
close all
clc

% initializes the 5 x 5 matrices A, B, and C
A = [52, 41, 48, 24, 50
     54, 29, 40, 13, 11
     25, 55, 43,  5, 51
     12, 49, 53, 31,  9
     56,  8, 35, 57, 19];

B = [45,  1, 48, 49, 61
     40, 55, 33, 34,  6
      4, 13, 27, 46, 31
     26, 56, 16, 62, 42
     51, 19, 24, 23, 50];

C = [45, 51,  1, 42, 21
     47, 28,  6,  5, 15
     20, 37, 40, 36, 24
     53, 44,  4, 17, 11
     52, 33, 34,  9, 58];

% obtains the trace of the resulting matrix
tr = trace(B * A * C');
fprintf("trace: %d\n", tr);
