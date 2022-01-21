% Computer Programming for ME                            December 10, 2020
% ME 2010 WI20
% Prof. M. Diaz-Maldonado
%
% Synopsis:
% Shows how to use arrays to compute (truncated) series in MATLAB.
% Series examples: arithmetic series, sum of squares, alternating series,
%                  geometric series, and Taylor series.

clear
clc

format long g



fprintf("example 1: arithmetic sequence")
% example 1:  arithmetic sequence
%         for j = 1:100 sum(j) to obtain the sum of the
% natural numbers from one to a hundred.

% defines the index of the sequence
n = 1:100;
% computes and stores the elements of the sequence in an array
sn = n;
% answer, sums the elements of the series
sum(sn)



fprintf("example 2: sum of squares")
% example 2: sum of squares
%         for n = 1:100 sum(n^2)
% to obtain the sum of the squares of the natural numbers from
% one to a hundred.

% defines the index of the sequence
n = 1:100;
% computes and stores the elements of the sequence in an array
sn = n.^2;
% answer, sums the elements of the series
sum(sn)



fprintf("example 3: alternating series")
% example 3: alternating series
%         for n = 0:99, sum( (-1)^(n) / (n+1) )

% defines the index of the sequence
n = 0:99;
% computes and stores the elements of the sequence in an array
sn = (-1).^(n) ./ (n+1);
% answer, sums the elements of the series
sum(sn)



fprintf("example 4: geometric series")
% example 4: geometric series
%         for n = 0:99 sum(r^n), where r = 0.2
% defines the geometric constant
r = 0.2;
% defines the index of the sequence
n = 0:99;
% computes and stores the elements of the sequence in an array
sn = r.^n;
% answer, sums the elements of the series
sum(sn)



fprintf("example 5: Taylor series")
% example 5: Taylor series,
% compute the first 16 elements of the series
%         for n = 0:15 sum(2^n / n!),
% where n! is the factorial of n

% note that the index n = 0:15 has 16 elements,
% you may want to check that numel(n) returns 16 elements
% when invoked on the command window

% defines the index of the series
n = 0:15;
% computes and stores the elements of the sequence in an array
sn = 2.^n ./factorial(n);
% answer, sums the elements of the series
sum(sn)



fprintf("example 6: Taylor series")
% example 6: Taylor series, compute the first 11 elements of the series
%          for n = 0:10 sum( (-1)^n * x^(2n+1) / (2n+1)! )

% defines the independent variable
x = pi/3;
% defines the index of the series
n = 0:11;
% computes and stores the elements of the sequence in an array
sn = (-1).^n .* x.^(2*n+1) ./factorial(2*n+1);
% answer, sums the elements of the series
sum(sn)

