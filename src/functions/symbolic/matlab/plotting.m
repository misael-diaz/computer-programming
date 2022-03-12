% Solvers                                                    March 12, 2022
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

clear
close all
clc

% creates symbols
syms x
y = 8 - 4.5 * ( x - sin(x) );
% defines f(x) and f'(x) as MATLAB functions
f = matlabFunction(y);
g = matlabFunction( diff(y) );

% plots
x = linspace(0, 5, 100);
plot(x, f(x), 'DisplayName', 'f(x) = ' + string(y)); hold on
plot(x, g(x), 'DisplayName', "f'(x) = " + string(diff(y)))
plot(x, zeros( size(x) ), '--r', 'DisplayName', 'zeros')
% sets x and y axes labels
xlabel('x')
ylabel('y')
% sets the figure title
title("Function f(x) and its first-derivative f'(x)")
% displays grid
grid on
legend()
