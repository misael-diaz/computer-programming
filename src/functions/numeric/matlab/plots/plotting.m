% Solvers                                                 February 28, 2022
% Prof. M Diaz Maldonado
%
% Synopsis:
% Plots the function f(x) = 8 – 4.5 (x – sin x) in a range where f(x)
% undergoes a sign change.
%
% Sinopsis:
% Grafica la funcion f(x) = 8 – 4.5 (x – sin x) en un rango donde f(x)
% muestra un cambio de signo.
%
%
% Copyright (c) 2022 Misael Diaz-Maldonado
% This file is released under the GNU General Public License as published
% by the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.


% clears variables, closes all figures, and clears the command window
clear
close all
clc

% defines x in the range [0, 5] for plotting
x = linspace(0, 5, 100);
% defines the nonlinear equation as a lambda (or anonymous) function
f = @(x) 8 - 4.5 * ( x - sin(x) );
% evaluates the function
y = f(x);

% defines possible bracketing interval
lb = 2.4;	% lower limit
ub = 2.5;	% upper limit

% stores bracketing interval in an array for plotting
xi = [lb, ub];
yi = f(xi);

plot(x, y); hold on	% hold on enables multiple plots in the same figure
plot(xi, yi, 'o')	% plots possible bracketing interval
plot(x, zeros( size(x) ), '--r')	% horizontal line (optional)
ylim([-3, 3])
xlabel('x')
ylabel('f(x)')
grid on

% exports figure in PNG format with 300 DPI
print('graph.png', '-dpng', '-r300')
