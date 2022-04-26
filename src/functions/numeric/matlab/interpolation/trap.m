% Applied Numerical Analysis                               January 10, 2019
% ME 2020 FA21
% Prof. M Diaz-Maldonado
% Revised: July  27, 2021
% Revised: April 26, 2022
%
%
% Synopsis:
% Possible implementation of the Trapezoid Method in MATLAB.
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
%    Scientists: An Introduction with Applications using MATLAB
% [1] A Gilat, MATLAB: An Introduction with Applications, 6th edition
%

function I = trap(a, b, N, f)
% Trapezoid Method
% Integrates the function f(x) in the interval [a, b] using N intervals

% step-size
dx = (b - a) / N;
x = linspace(a, b, N + 1);

sum = 0;
for i = 1:N
    sum = sum + f( x(i) ) + f( x(i+1) );
end

I = 0.5 * dx * sum;
return
