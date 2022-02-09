% Computer Programming for ME                              December 1, 2020
% ME 2010 WI20
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Shows how to create and use arrays to perform engineering computations.
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


% clears existing variables from the workspace and clears the console
clear
clc


% uses the best representation in long format (15 decimal places)
format long g


% examples:

% Example 1.6-A: Using variables
% Computes f(x) for x = 4.5 and stores its value in another variable `y':
fprintf('example 1.6-A:\n')
x = 4.5;
y = 0.4 * x^4 + 3.1 * x^2 - 162.3 * x - 80.7


% Example 1.6-B: Recycling variables
% Reuses variable `x` to compute another function, y = f(x):
fprintf('example 1.6-B:\n')
y = (x^3 - 23) / nthroot(x^2 + 17.5, 3)


% Example 1.6-C: Incrementing sequences
% Assigns the sequence 0, 1, 2, 3, ..., 6, 7 to the variable `x`
x = 0:7;	% x = begin:end
% uses the dot `.' to perform elementwise operations on arrays
y = 0.4 * x.^4 + 3.1 * x.^2 - 162.3 * x - 80.7
fprintf('example 1.6-C:\n')
fprintf('4th element of array x: %.6f\n', x(4) )
fprintf('4th element of array y: %.6f\n', y(4) )


% Example 1.6-D: Non-unit, step, incrementing sequences
% Creates a sequence that increments in steps of two
fprintf('example 1.6-D:\n')
x = 0:2:14;	% x = begin:step:end
y = 0.4 * x.^4 + 3.1 * x.^2 - 162.3 * x - 80.7
fprintf('7th element of array x: %.6f\n', x(7) )
fprintf('7th element of array y: %.6f\n', y(7) )



% Example 1.6-E: Decrementing sequences
% Generates a sequence of decreasing values (step: -2)
% x = 14:-2:2, x = begin:step:end
fprintf('example 1.6-E:\n')
x = 14:-2:2;
y = 0.4 * x.^4 + 3.1 * x.^2 - 162.3 * x - 80.7


% Example 1.6-F: Incrementing sequence having a non-integer step
fprintf('example 1.6-F:\n')
x = 0:0.15:1.05;	% x = begin:step:end (step: 0.15)
y = 0.4 * x.^4 + 3.1 * x.^2 - 162.3 * x - 80.7


% Example 1.6-G: Using the linspace function as an alternative to ranges

fprintf('example 1.6-G:\n')
x = linspace(0, 7, 8);		% linspace(xi, xf, N):
				% xi: begin value
				% xf: end value
				%  N: number of elements of the sequence

y = 0.4 * x.^4 + 3.1 * x.^2 - 162.3 * x - 80.7


% Good Programming Practices:
% [0] Comment your code to make it useful for future use.
% [1] Limit the lenght of the line of code to 75 characters or less.
% [2] Use the semicolon (;) to suppress the output of this line of code
%     on the command window.
% [3] Use the dot `.' exclusively for elementwise operations on arrays
%     to make the code more readable.
