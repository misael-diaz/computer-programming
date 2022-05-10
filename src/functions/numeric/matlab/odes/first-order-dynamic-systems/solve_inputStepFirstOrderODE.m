% Computational Solutions of Engineering Problems              May 09, 2022
% IST 4360
% Prof. M. Diaz-Maldonado
%
%
% Synopsis:
% Solves a first-order Ordinary Differential Equation ODE numerically by
% applying Euler's explicit method:
%
%                             tau * y'(t) + y(t) = u(t),
%
% where `tau' is the characteristic system time, y(t) is the output state
% variable, t is the time, and u(t) is the input, unit-step, function,
% u(t) = 1.
%
%
% Copyright (c) 2022 Misael Diaz-Maldonado
% This file is released under the GNU General Public License as published
% by the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.
%
%
% References:
% [0] A Gilat and V Subramanian, Numerical Methods for Engineers and
%     Scientists: An Introduction with Applications using MATLAB
% [1] A Gilat, MATLAB: An Introduction with Applications, 6th edition

clear
close all
clc

% defines the characteristic system time
tau = 0.5;
% defines the characteristic system rate
k = 1 / tau;

% defines the input function u(t) as a unit-step function
u = @(t) 1;

% defines the right-hand side RHS of the ODE: dy/dt = f(t, y) as a lambda
odefun = @(t, y) ( k * (u(t) - y) );


N = 255;             		% number of integration time-steps
ti = 0.0;	tf = 5.0 * tau;	% initial time `ti' and final time `tf'
tspan  = [ti, tf];   		% timespan array (integration limits)
yi     = 0;        		% initial value: yi = y(t = ti) = 0


% solves the ODE with Euler's method
[t, y] = Euler (N, tspan, yi, odefun);
% exact solution
f = @(t) 1 - exp(-t / tau);


% plots the numeric solution to comapare against the exact solution
figure(1)
% define step input function for plotting
u = @(t) (t > 0);
plot(t, u(t), "-b", "displayname", "unit-step input")
hold on
plot(t, f(t), "--k", "displayname", "exact solution")
plot(t(1:16:end), y(1:16:end), "color", "red", "marker", "s", ...
	"markersize", 12, "linestyle", "none", "displayname", ...
	"Euler's method")

grid on                  	% shows grid
legend('location', 'SE')	% displays the legend at given location
xlabel("time, t")
ylabel("dynamic response, y(t)")
ylim([-0.05, 1.05])
title("Transient Response of a First Order Dynamic System to a Step")



function [t, y] = Euler(N, tspan, yi, f)
%   Synopsis:
%   Solves a first-order ordinary differential equation with Euler's
%   explicit method.
%
%   inputs:
%   N       number of time-steps for numeric integration
%   tspan   t[ime] span, the initial, ti, and final times, tf
%   yi      initial value of the state variable, yi = y(t = ti)
%   f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)
%
%   output:
%   odesol  second-rank array, [t, y]

    y  = zeros(N + 1, 1);		% preallocates for speed
    ti = tspan(1);	tf = tspan(2);	% unpacks initial and final times
    dt = (tf - ti) / N;			% computes the time-step
    t  = linspace(ti, tf, N + 1)';	% obtains the time array

    y(1) = yi;
    for i = 1:N
        y(i + 1) = y(i) + dt * f( t(i), y(i) );
    end

end
