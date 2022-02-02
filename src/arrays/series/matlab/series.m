% Computer Programming for ME                            December 10, 2020
% ME 2010 WI20
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Shows how to use arrays to compute (truncated) series in MATLAB.
%
% Sinopsis:
% Demuestra como usar arreglos para realizar computos de series en MATLAB.
%
% Examples:
% [1] arithmetic series	[2] sum of squares	[3] alternating series
% [4] geometric series 	[5] Taylor series	[6] Taylor series
%
%
% Copyright (c) 2020 Misael Diaz-Maldonado
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


fprintf('Example 1: Arithmetic Sequence\n')
% Example 1:  Arithmetic Sequence
% Obtains the sum of the natural numbers from one to a hundred:
%
%			for n = [1, 100], Sum(n)
%

% defines the index of the sequence
n = 1:100;
% computes and stores the elements of the sequence in an array
sn = n;
% answer, sums the elements of the series
fprintf('for n = [1, 100], Sum(n) = %d\n\n\n', sum(sn) )



fprintf('Example 2: Sum of Squares\n')
% example 2: Sum of Squares
% Obtains the sum of the squares of the natural numbers from
% one to a hundred:
%
%			for n = [1, 100],  Sum(n^2)
%

n = 1:100;
sn = n.^2;	% NOTE: the dot `.' indicates elementwise operations
fprintf('for n = [1, 100], Sum(n^2) = %d\n\n\n', sum(sn) )



fprintf('Example 3: Alternating Series\n')
% example 3: Alternating Series
%
%         for n = [0, 100), Sum( (-1)^(n) / (n+1) )
%

n = 0:99;	% NOTE: in MATLAB the end of the range is inclusive
sn = (-1).^(n) ./ (n+1);
fprintf('for n = [0, 100), Sum( (-1)^n / (n + 1) ) = %.6f\n\n\n', sum(sn) )



fprintf('Example 4: Geometric Series\n')
% example 4: Geometric Series
%
% 	given r = 0.2, 	for n = [0, 100), Sum(r^n)
%

r = 0.2;	% defines the geometric constant `r'
n = 0:99;
sn = r.^n;
fprintf('for n = [0, 100), Sum(r^n) = %.6f\n\n\n', sum(sn) )



fprintf('Example 5: Taylor Series\n')
% Example 5: Taylor series
% Computes the first 16 elements of the following Taylor series:
%
%         	for n = [0, 16), Sum(2^n / n!),
%
% where `n!' is the factorial of n.
%

n = 0:15;
sn = 2.^n ./ factorial(n);
fprintf('for n = [0, 16), Sum(2^n / n!) = %.6f\n\n\n', sum(sn) )



fprintf('Example 6: Taylor series\n')
% Example 6: Taylor series
% Computes the first 11 elements of the Taylor series:
%
%		for n = [0, 11), Sum( (-1)^n * x^(2n+1) / (2n+1)! )
%

x = pi/3;
n = 0:11;
sn = (-1).^n .* x.^(2*n+1) ./ factorial(2*n+1);
fprintf(['for n = [0, 11), ', ...	% NOTE: uses string concatenation
	 'Sum( (-1)^n * x^(2n+1) / (2n+1)! ) = %.6f\n\n\n'], sum(sn) )
