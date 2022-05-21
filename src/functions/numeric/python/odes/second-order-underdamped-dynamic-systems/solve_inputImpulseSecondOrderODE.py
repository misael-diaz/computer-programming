#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Computational Solutions of Engineering Problems                May 12, 2022
IST 4360
Prof. M Diaz-Maldonado


Synopsis:
Solves for the step response of an underdamped mechanical system described
by the second-order Ordinary Differential Equation ODE:

                m * y'' + b * y' + k * y = f * u(t),

where m is the mass, b is the damper friction, k is the spring stiffness,
f is the forcing constant, and u(t) = P is the impulse input of weight P.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] A Gilat and V Subramanian, Numerical Methods for Engineers and
    Scientists: An Introduction with Applications using MATLAB
[1] CA Kluever, Dynamic Systems: Modeling, Simulation, and Control
[2] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""


from numpy import array
from numpy import empty
from numpy import zeros
from numpy import vstack
from numpy import newaxis
from numpy import linspace
from numpy import empty_like
from numpy import exp, sqrt
from numpy import pi, cos, sin
import matplotlib as mpl
from matplotlib import pyplot as plt


def Euler(N, tspan, yi, f):
        """
        Synopsis:
        Solves a system of ordinary differential equations with Euler's
        explicit method.

        inputs:
        N       number of time-steps for numerical integration
        tspan   t[ime] span, the initial, ti, and final times, tf
        yi      1st-rank array, initial values of the state variables
        f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)

        output:
        odesol  2nd-rank numpy array, [t, y]
        """

        y = empty([yi.size, N + 1]) # preallocates array for speed
        ti, tf = tspan              # unpacks the initial and final times

        # computes the time array and the time-step
        t,  dt = (linspace(ti, tf, N+1), (tf - ti) / N)

        y[:, 0] = yi[:, 0]          # initializes y1(0) and y2(0)
        for i in range(N):
            y[:, i + 1] = y[:, i] + dt * f(t[i], y[:, i])

        t = t[newaxis, :]           # creates axis for vertical stacking
        odesol = vstack([t, y])     # packs state vars t, y1(t), and y2(t)
        return odesol               # returns numeric solution


def RungeKutta(N, tspan, yi, f):
        """
        Synopsis:
        Solves a system of ordinary differential equations with the fourth
        order Runge-Kutta method.

        inputs:
        N       number of time-steps for numerical integration
        tspan   t[ime] span, the initial, ti, and final times, tf
        yi      1st-rank array, initial values of the state variables
        f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)

        output:
        odesol  2nd-rank numpy array, [t, y]
        """

        y = empty([yi.size, N + 1])
        ti, tf = tspan

        t,  dt = (linspace(ti, tf, N+1), (tf - ti) / N)

        y[:, 0] = yi[:, 0]
        for i in range(N):

            # computes the slopes of the Runge-Kutta method
            K1 = f(t[i], y[:, i])
            K2 = f(t[i] + 0.5 * dt, y[:, i] + 0.5 * K1 * dt)
            K3 = f(t[i] + 0.5 * dt, y[:, i] + 0.5 * K2 * dt)
            K4 = f(t[i] + dt, y[:, i] + K3 * dt)

            # integrates the state-variable array
            y[:, i + 1] = y[:, i] + dt * (K1 + 2 * K2 + 2 * K3 + K4) / 6

        t = t[newaxis, :]
        odesol = vstack([t, y])
        return odesol


def impulse(prms, t):
    """
    Synopsis:
    Computes the impulse-response y(t) of an underdamped second-order
    dynamic mechanical system given the system mass, friction and
    stiffness constants, and the forcing constant and magnitude P of the
    impulse u(t).
    """

    # unpacks mechanical system params (inertia, friction, stiffness, etc.)
    m, b, k, f, P = prms
    # defines the standard coefficients of the second-order ODE
    a0, a1, b0 = (k / m, b / m, f / m)
    # determines the natural frequency and damping ratio, respectively
    omega, zeta = (  sqrt(a0),  a1 / ( 2 * sqrt(a0) )  )
    # complains if the system is not underdamped
    is_underdamped(zeta)
    # obtains the real and imaginary components of the characteristic roots
    alpha, beta = ( -zeta * omega, omega * sqrt(1 - zeta**2) )

    # computes the system response y(t) to a unit-impulse input
    y = beta * (1 + (alpha / beta)**2) * exp(alpha * t) * sin(beta * t)
    # rescales to obtain the system response to an impulse of magnitude P
    y *= b0 * P / omega**2

    return y


def underdamped(prms):
    """
    Synopsis:
    Returns the basic characteristics of an underdamped mechanical system
    given the mass/moment of inertia and friction and stiffness constants.
    """

    # unpacks mechanical system parameters (inertia, friction, stiffness)
    m, b, k, f, P = prms
    # computes the standard coefficients of the second-order ODE
    a0, a1, b0 = (k / m, b / m, f / m)
    # determines the natural frequency and damping ratio, respectively
    omega, zeta = (  sqrt(a0),  a1 / ( 2 * sqrt(a0) )  )
    # complains if not underdamped
    is_underdamped(zeta)
    # returns the standard coefficients and response parameters
    return (a0, a1, b0, omega, zeta)


def is_underdamped(zeta):
    # complains if the mechanical system is not underdamped

    errMSG = (
        f"Not an underdamped mechanical system:\n"
        f"the damping ratio (zeta = {zeta:.2f}) is not less than one\n"
    )

    tol = 1.0e-4
    if (1 - zeta) < tol:
        raise RuntimeError(errMSG)
    return


""" defines the mechanical system parameters """
# moment of inertia, friction, stiffness, forcing-constant, and pulse
prms = m, b, k, f, P = (0.2, 1.6,  65.0, 1.0, 2.5)
# defines the analytic solution as a lambda function
f = lambda t: impulse(prms, t)

# unpacks the standard coefficients of the second-order dynamic system
a0, a1, b0, _, _ = underdamped (prms)

# defines the step function u(t) of height P
u = lambda t: P


"""
Defines the right-hand side of the system of first-order ODEs that
result from expressing the model equation in its standard form.
"""
odefun = lambda t, y: array([ y[1], b0 * u(t) - a1 * y[1] - a0 * y[0] ])


""" defines the solver parameters for Euler's Method """

N = 4095                    # number of integration steps
yi = zeros([2, 1])          # initial values y(0) = 0
ti, tf = 0, 2               # initial and final times
tspan = (ti, tf)            # timespan
""" applies Euler's method """
t_Euler, y1_Euler, y2_Euler = Euler (N, tspan, yi, odefun)


# overrides the number of integration steps for the Runge-Kutta method
N = 255
# applies the fourth-order Runge-Kutta method (note the smaller step-size)
t_RK, y1_RK, y2_RK = RungeKutta (N, tspan, yi, odefun)


""" transient response visualization """
plt.close("all")
plt.ion()
fig, ax = plt.subplots()
t = t_Euler
# plots the analytic solution of the step response
ax.plot(t, f(t), color = "black", linestyle = "-", linewidth = 2,
    label="analytic")
# plots Euler's method solution
ax.plot(t_Euler[::128], y2_Euler[::128], linestyle="", marker="s",
    markersize=4,  color="blue", label="Euler's method")
# plots the numeric solution obtained from the 4th-order Runge-Kutta Method
ax.plot(t_RK[::14], y2_RK[::14], linestyle="", marker="*", markersize=8,
    color="red", label="4th-order Runge-Kutta method")
#ax.set_xlabel('t')
#ax.set_ylabel('x(t)')
title = ('Impulse Response of an Underdamped Second Order Dynamic System')
#ax.set_title(title)
ax.legend()
figure = 'plots/impulse-response-underdamped-mechanical-system.png'
fig.savefig(figure, dpi=300)


"""
COMMENTS:
Note that the Runge-Kutta method required fewer integration steps to
approximate the analytic solution, this is to be expected since the
order of the approximation is O(h^4), where h is the step-size.
In contrast, the order of approximation of Euler's method is only O(h).
It's because of this that a smaller step-size (more integration steps)
was needed to improve Euler's approximation. When dealing with systems of
ordinary differential equations it is recommended to use higher order
methods such as the fourth-order Runge-Kutta method employed here.
"""
