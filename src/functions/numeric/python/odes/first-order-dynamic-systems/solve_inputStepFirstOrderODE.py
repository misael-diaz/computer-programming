#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Computational Solutions of Engineering Problems                May 09, 2022
IST 4360
Prof. M. Diaz-Maldonado


Synopsis:
Solves the first-order Ordinary Differential Equation ODE by applying
Euler's explicit method:

                        tau * y'(t) + y(t) = u(t),

where `tau' is the characteristic system time, y(t) is the output state
variable, t is the time, and u(t) is the input, unit-step, function,
u(t) = 1.


Copyright (c) 2022 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.


References:
[0] A Gilat and V Subramanian, Numerical Methods for Engineers and
    Scientists: An Introduction with Applications using MATLAB
[1] R Johansson, Numerical Python: Scientific Computing and Data
    Science Applications with NumPy, SciPy, and Matplotlib, 2nd edition
"""


from numpy import exp
from numpy import array
from numpy import empty
from numpy import linspace
import matplotlib as mpl
from matplotlib import pyplot as plt


def Euler(N, trange, yi, f):
        """
        Synopsis:
        Solves a first-order ordinary differential equation with Euler's
        explicit method.

        inputs:
        N       number of time-steps for numerical integration
        trange  t[ime] span, the initial, ti, and final times, tf
        yi      initial value of the state variable, yi = y(t = ti)
        f       f(t, y), right-hand side of the ODE: dy/dt = f(t, y)

        output:
        odesol  second-rank numpy array, [t, y]
        """

        y      = empty(N + 1)       # preallocates array for speed
        ti, tf = trange             # unpacks the initial and final times

        # computes the time array and the time-step
        t,  dt = (linspace(ti, tf, N+1), (tf - ti) / N)

        y[0] = yi
        for i in range(N):
                y[i + 1] = y[i] + dt * f(t[i], y[i])

        odesol = array([t, y])      # packs into a second-rank numpy array
        return odesol


""" Obtains the dynamic response of the first-order system to unit step """

# defines the characteristic system time and rate, respectively
tau = 0.5
k = 1 / tau
# defines the unit-step input function as a lambda
u = lambda t: 1
# defines the right-hand side RHS of the ODE: dy/dt = f(t, y) as a lambda
odefun = lambda t, y: ( k * (u(t) -  y) )


N = 255                         # number of integration time-steps
ti, tf = (0, 5 * tau)           # initial time ti and final time tf
tspan  = (ti, tf)               # time span tuple
yi     = 0                      # initial value: yi = y(t = ti) = 0


""" solves the ODE with Euler's explicit method """
t, y = Euler (N, tspan, yi, odefun) # solves ODE and unpacks into t, y(t)
f = lambda t: 1 - exp(-k * t)       # defines the exact solution


""" plots the numerical solution to compare it with the exact solution """
plt.close("all")            # closes all existing figures
plt.ion()                   # enables interactive plotting
fig, ax = plt.subplots()    # effectively plots on the same figure

# defines the unit-step input for plotting
u = lambda t: (t > 0)
ax.plot(t, u(t), color="blue", linestyle="-", linewidth=1,
        label="unit-step input")
ax.plot(t, f(t), color="black", linestyle="--", linewidth=1,
        label="analytic solution")
ax.plot(t[::16], y[::16], color="red", marker="*", markersize=12,
        linestyle="", label="Euler's Method")

ax.grid()                   # shows grid
ax.legend()                 # displays the legend
ax.set_xlabel("time, t")
ax.set_ylabel("dynamic response, y(t)")
title = ("Transient Response of a First Order Dynamic System to a Step")
ax.set_title(title)
