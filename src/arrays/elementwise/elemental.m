% Algorithms and Programming II                           February 13, 2022
% IST 2089
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Conducts elementwise operations on arrays via for-loops.
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


close all
clear
clc


% uses the best representation in long format (15 decimal places)
format long g


% examples:



% Example 1.6-C: Incrementing sequences
% i 0, 1, 2, 3, ..., 6, 7
x = zeros(8, 1);
x(1) = 0;
for i = 2:8
	x(i) = x(i - 1) + 1;
end
fprintf('\nexample 1.6-C: i = 0, 1, 2, 3, ..., 6, 7\n')
x



% Example 1.6-D: Non-unit, step, incrementing sequences
% i = 0, 2, 4, ..., 12, 14
x = zeros(8, 1);

x(1) = 0;
for i = 2:8
	x(i) = x(i - 1) + 2;
end
fprintf('\nexample 1.6-D: i = 0, 2, 4, ..., 12, 14\n')
x


% Example 1.6-E: Decrementing sequences
% Generates a sequence of decreasing values (step: -2)
% x = 14:-2:2, x = begin:step:end
x = zeros(7, 1);

x(1) = 14;
for i = 2:7
	x(i) = x(i-1) - 2;
end

y = zeros(size(x));
% calculates the function y = f(x) element-by-element in a for-loop
for i = 1:7
	y(i) = 0.4 * x(i)^4 + 3.1 * x(i)^2 - 162.3 * x(i) - 80.7;
end

fprintf('\nexample 1.6-D: i = 14, 12, 10, ..., 4, 2\n')

% displays the array index, and the `x' and `y' arrays in tabulated form
i = (1:7)';
fprintf('\n\t\t\t i \t\t\t   x \t\t\t y\n')
fprintf('----------------------------------------------------------------')
fprintf('------------------\n')
disp([i, x, y])



% Example: Sum of Squares

n = zeros(8, 1);	% preallocates the series index array
n(1) = 1;
for i = 2:8
	n(i) = n(i-1) + 1;
end

s = zeros(8, 1);	% preallocates the sequence array

for i = 1:8
	s(i) = n(i) .^ 2;
end


cumsum = zeros(8, 1);	% preallocates the cumulative sum array

cumsum(1) = s(1);
for i = 2:8
	cumsum(i) = cumsum(i-1) + s(i);
end


fprintf('\nSum of Squares:\n')

% tabulates results (array index, series index, sequence, cumulative sum)
i = (1:8)';
fprintf('\n     i \t   n    s(n)  sum')
fprintf('\n----------------------------\n')
disp([i, n, s, cumsum])
