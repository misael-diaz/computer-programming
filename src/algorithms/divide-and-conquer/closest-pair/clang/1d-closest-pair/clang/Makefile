#!/usr/bin/make
#
# Closest Pair Lab						February 20, 2023
#
# source: Makefile
# author: @misael-diaz
#
# Synopsis:
# Defines the Makefile for building the program with GNU make.
#
# Copyright (c) 2023 Misael Diaz-Maldonado
# This file is released under the GNU General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#

include make-inc

export CC
export CCOPT
export LIBS

all: utils algorithms benchmarks tests

utils: util
	@$(MAKE) -C util

algorithms: utils algorithm
	@$(MAKE) -C algorithm

benchmarks: utils algorithms benchmark
	@$(MAKE) -C benchmark

tests: utils algorithms test
	@$(MAKE) -C test

clean:
	@$(MAKE) -C util clean
	@$(MAKE) -C algorithm clean
	@$(MAKE) -C benchmark clean
	@$(MAKE) -C test clean
