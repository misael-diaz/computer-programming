% Computer Programming                                    November 17, 2020
% ME 2010 WI20
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Code shows how to perform some arithmetic operations.
%
%
% Copyright (c) 2021 Misael Diaz-Maldonado
% This file is released under the GNU General Public License as published
% by the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.
%
%
% References:
% [0] A Gilat, MATLAB: An Introduction with Applications, 6th edition.


% clears variables, figures, and the command window
clear
close all
clc


% examples:

% example 1.1-A
(5 - 19/7 + 2.5^3) ^ 2

% example 1.1-B
7 * 3.1 + sqrt(120)/5 - 15^(5/3)

% example 1.2-A
nthroot(8 + 80/2.6, 3) + exp(3.5)

% example 1.3-A
(23 + nthroot(45, 3) ) / (16 * 0.7) + log10(589006)

% example 1.4-D
1.0 / sqrt(5.0e-2) + 2.0 * log10( 4.5e-3 / 3.7 + ...
    2.51 / (1.8e6 * sqrt(2.5e-1) ) )

% example 1.5-A
sin(0.2 * pi) / cos(pi / 6) + tand(72)


% Good Programming Practice:
% Use the ellipsis (...) to break down long formulas (example 1.4-D).
