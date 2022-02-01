% Computer Programming                                    November 17, 2020
% ME 2010 WI20
% Prof. M. Diaz-Maldonado
%
%
% Synopsis:
% Code shows how to use variables to perform some arithmetic operations
% and how to present numeric results in decimal notation up to six
% decimal places.
%
%
% Sinopsis:
% El codigo muestra como usar variables para realizar algunas operaciones
% aritmeticas y tambien como imprimir los resultados numericos en notacion
% decimal hasta seis lugares decimales.
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
x = (5 - 19/7 + 2.5^3) ^ 2;	% use the semicolon `;' to suppress output
fprintf("example 1.1-A: %.6f\n", x)

% example 1.1-B
x = 7 * 3.1 + sqrt(120)/5 - 15^(5/3);
fprintf("example 1.1-B: %.6f\n", x)

% example 1.2-A
x = nthroot(8 + 80/2.6, 3) + exp(3.5);
fprintf("example 1.2-A: %.6f\n", x)

% example 1.3-A
x = (23 + nthroot(45, 3) ) / (16 * 0.7) + log10(589006);
fprintf("example 1.3-A: %.6f\n", x)

% example 1.4-D
x = 1.0 / sqrt(5.0e-2) + 2.0 * log10( 4.5e-3 / 3.7 + ...
    2.51 / (1.8e6 * sqrt(2.5e-1) ) );

fprintf("example 1.4-D: %.6f\n", x)

% example 1.5-A
x = sin(0.2 * pi) / cos(pi / 6) + tand(72);
fprintf("example 1.5-A: %.6f\n", x)


% Good Programming Practices:
% [0] Use the ellipsis (...) to break down long formulas (example 1.4-D).
% [1] Reuse variables whenever it is practical to do so.
% [2] Use the semicolon `;' to suppress (unwanted) output.
