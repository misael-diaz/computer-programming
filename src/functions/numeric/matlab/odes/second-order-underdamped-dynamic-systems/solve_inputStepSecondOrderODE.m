% Computational Solutions of Engineering Problems              May 18, 2022
% IST 4360
% Prof. M. Diaz-Maldonado
%
%
% Synopsis:
% Solves for the step response of an underdamped mechanical system
% described by the second-order Ordinary Differential Equation ODE:
%
%                m * y'' + b * y' + k * y = f * u(t),
%
% where m is the mass, b is the damper friction, k is the spring stiffness,
% f is the forcing constant, and u(t) = H is the step-input function of
% magnitude H.
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

% defines inertia, friction, stiffness, forcing, and step magnitude
[m, b, k, f, H] = prms();

% defines the analytic solution as a lambda function
f = @(t) step(t);

% defines the right-hand side of the ODE as a lambda function
odefun = @(t, y) fodefun(t, y);


% defines the solver parameters for Euler's Method:

N = 1023;			% number of integration steps
yi = zeros(1, 2);		% initial values y(0) = 0
ti = 0; tf = 2;			% initial and final times
tspan = [ti, tf];		% timespan
% applies Euler's method
[t_Euler, y1_Euler, y2_Euler] = Euler (N, tspan, yi, odefun);

% overrides the number of integration steps for the Runge-Kutta method
N = 255;
% applies the fourth-order Runge-Kutta method (note the smaller step-size)
[t_RK, y1_RK, y2_RK] = RungeKutta (N, tspan, yi, odefun);


% transient response visualization:

% plots the analytic solution of the step response
t = t_Euler;
plot(t, f(t), '-k', 'DisplayName', 'analytic')
hold on
% plots Euler's method solution
plot(t_Euler(1:80:end), y1_Euler(1:80:end), 'linestyle', 'none', ...
	'marker', 's', 'markersize', 12, 'color', 'blue', ...
	'DisplayName', "Euler's method")

% plots the numeric solution obtained from the 4th-order Runge-Kutta Method
plot(t_RK(1:14:end), y1_RK(1:14:end), 'linestyle', 'none', ...
	'marker', 'd', 'markersize', 12, 'color', 'red', ...
	'DisplayName', "Runge-Kutta Method")
xlabel('t')
ylabel('x(t)')
title('Step Response of an Underdamped Second Order Dynamic System')
legend()



function [m, b, k, f, H] = prms()
%   Synopsis:
%   Returns the mass (or moment of inertia), friction, stiffness,
%   forcing-constant, and the step magnitude, respectively.

    m = 0.2;	b = 1.6;	k = 65.0;	f = 1.0;	H = 2.5;
end


function H = u(t)
%   Synopsis:
%   Defines the input step function u(t)
    [~, ~, ~, ~, H] = prms();
end


function f = fodefun (t, y)
%    Synopsis:
%    Defines the right-hand side of the system of first-order ODEs that
%    result from expressing the model equation in its standard form.

    % unpacks the standard coefficients of the second-order dynamic system
    [a0, a1, b0, ~, ~] = underdamped ();

    f = zeros( size(y) );
    f(1) = y(2);
    f(2) = b0 * u(t) - a1 * y(2) - a0 * y(1);
end



function is_underdamped(zeta)
%   Synopsis:
%   Complains if the damping ratio is either too close or greater than one.

    tol = 1.0e-4;
    if ( (1 - zeta) < tol )
	errID  = 'MechanicalSystem:DampingException';
	errMSG = sprintf('Not an underdamped system (zeta = %f)\n', zeta);
	except = MException(errID, errMSG);
	throw(except);
    end
end


function y = step(t)
%   Synopsis:
%   Analytic Solution.
%   Computes the step-response y(t) of an underdamped second-order dynamic
%   mechanical system given the system mass, friction and stiffness
%   constants, the forcing constant, and magnitude H of the step u(t).

    % unpacks mechanical system params (inertia, friction, stiffness, etc.)
    [m, b, k, f, H] = prms();
    % defines the standard coefficients of the second-order ODE
    a0 = (k / m);	a1 = (b / m);	b0 = (f / m);
    % determines the natural frequency and damping ratio, respectively
    omega = sqrt(a0);	zeta = a1 / ( 2 * sqrt(a0) );
    % complains if the system is not underdamped
    is_underdamped(zeta);
    % obtains the real and imaginary components of the characteristic roots
    alpha = -zeta * omega;	beta = omega * sqrt(1 - zeta^2);

    % computes the system response y(t) to a unit-step input
    y = 1 - exp(alpha * t) .* ( cos(beta * t) - alpha / beta * ...
        sin(beta * t) );
    % scales to obtain the system response to a step of magnitude H
    y = (b0 * H / omega^2) * y;

end


function [a0, a1, b0, omega, zeta] = underdamped()
%   Synopsis:
%   Returns the basic characteristics of an underdamped mechanical system
%   given the mass/moment of inertia and friction and stiffness constants.

    % unpacks mechanical system parameters (inertia, friction, stiffness)
    [m, b, k, f, H] = prms();
    % defines the standard coefficients of the second-order ODE
    a0 = (k / m);	a1 = (b / m);	b0 = (f / m);
    % determines the natural frequency and damping ratio, respectively
    omega = sqrt(a0);	zeta = a1 / ( 2 * sqrt(a0) );
    % complains if the system is not underdamped
    is_underdamped(zeta);

end



function [t, y1, y2] = Euler(N, tspan, yi, f)
%   Synopsis:
%   Solves a system of first-order ordinary differential equation with
%   Euler's explicit method.
%
%   inputs:
%   N       number of time-steps for numeric integration
%   tspan   t[ime] span, the initial, ti, and final times, tf
%   yi      initial value of the state variable, yi = y(t = ti)
%   f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)
%
%   output:
%   odesol  second-rank array, [t, y's]

    y  = zeros(N + 1, 2);		% preallocates for speed
    ti = tspan(1);	tf = tspan(2);	% unpacks initial and final times
    dt = (tf - ti) / N;			% computes the time-step
    t  = linspace(ti, tf, N + 1)';	% obtains the time array

    y(1, :) = yi;
    for i = 1:N
        y(i + 1, :) = y(i, :) + dt * f( t(i), y(i, :) );
    end

    y1 = y(:, 1);	% references the position, x(t)
    y2 = y(:, 2);	% references the velocity, x'(t)

end


function [t, y1, y2] = RungeKutta(N, tspan, yi, f)
%   Synopsis:
%   Solves a system of first-order ordinary differential equations with the
%   fourth order Runge-Kutta method.
%
%   inputs:
%   N       number of time-steps for numeric integration
%   tspan   t[ime] span, the initial, ti, and final times, tf
%   yi      initial value of the state variable, yi = y(t = ti)
%   f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)
%
%   output:
%   odesol  second-rank array, [t, y's]

    y  = zeros(N + 1, 2);
    ti = tspan(1);	tf = tspan(2);
    dt = (tf - ti) / N;
    t  = linspace(ti, tf, N + 1)';

    y(1, :) = yi;
    for i = 1:N

	% computes the slopes of the Runge-Kutta method
	K1 = f( t(i), y(i, :) );
	K2 = f( t(i) + 0.5 * dt, y(i, :) + 0.5 * dt * K1 );
	K3 = f( t(i) + 0.5 * dt, y(i, :) + 0.5 * dt * K2 );
	K4 = f( t(i) + dt, y(i, :) + dt * K3 );

	% integrates the state-variable array
        y(i + 1, :) = y(i, :) + dt * (K1 + 2 * K2 + 2 * K3 + K4) / 6;
    end

    y1 = y(:, 1);
    y2 = y(:, 2);

end



% COMMENTS:
% Note that the Runge-Kutta method required fewer integration steps to
% approximate the analytic solution, this is to be expected since the
% order of the approximation is O(h^4), where h is the step-size.
% In contrast, the order of approximation of Euler's method is only O(h).
% It's because of this that a smaller step-size (more integration steps)
% was needed to improve Euler's approximation. When dealing with systems of
% ordinary differential equations it is recommended to use higher order
% methods such as the fourth-order Runge-Kutta method employed here.
