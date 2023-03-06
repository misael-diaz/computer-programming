#!/usr/bin/make
#
# Closest Pair Lab						February 22, 2023
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

all: numerics prngs dataset_namespace workspace_namespace

numerics: numeric
	@$(MAKE) -C numeric

prngs: prng
	@$(MAKE) -C prng

dataset_namespace: dataset
	@$(MAKE) -C dataset

workspace_namespace: prngs dataset_namespace workspace
	@$(MAKE) -C workspace

clean:
	@$(MAKE) -C numeric clean
	@$(MAKE) -C prng clean
	@$(MAKE) -C dataset clean
	@$(MAKE) -C workspace clean
