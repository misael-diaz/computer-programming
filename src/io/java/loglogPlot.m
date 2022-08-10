% Algorithms and Complexity                               August 08, 2022
% IST4310
% Prof. M Diaz-Maldonado
%
% Synopsis:
% Plots the average number of comparisons and the average elapsed time
% as a function of the input size.
%
%
% Copyright (c) 2022 Misael Diaz-Maldonado
% This file is released under the GNU General Public License as published
% by the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.
%
%
% References:
% [0] JJ McConnell, Analysis of Algorithms, 2nd edition
% [1] TH Cormen, CE Leiserson, RL Rivest, C Stein, Introduction to
%     Algorithms, 4th edition
% [2] A Gilat, MATLAB: An Introduction with Applications, 6th edition

clear
close all
clc

% """ imports the experimental data """

% unpacks the input size, the number of comparisons, and the elapsed time
data = load("times.txt");
size  = data(:, 1);
comps = data(:, 2);
etime = data(:, 3);

% """ visualization """

% creates a figure
figure(1)
% computes scaling constants to improve presentation
c1 = mean(size.^2) / mean(comps);
c2 = mean(size.^2) / mean(etime);
% plots the expected (theoretical) time as a function of size
loglog(size, size.^2,    'color', 'black', 'linestyle', '--', ...
       'DisplayName', 'quadratic')
hold on
% plots the elapsed time (nanoseconds) as a function of size
loglog(size, c2 * etime, 'color', 'black',  'linestyle', 'none', ...
       'marker', 'o', 'DisplayName', 'elapsed-time')
% plots the number of comparisons as a function of size
loglog(size, c1 * comps, 'color', 'red', 'linestyle', 'none', ...
       'marker', '*', 'DisplayName', 'comparisons')
xlabel('input size, N')
ylabel('time, t')
title('average case')
legend('location', 'NW')

% exports the figure in PNG with 600 DPI
print('average.png', '-dpng', '-r600')
