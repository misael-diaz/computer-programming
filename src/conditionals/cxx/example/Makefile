#!/usr/bin/make
#
# Conditionals				February 10, 2024
#
# source: src/Makefile
# author: @misael-diaz
#
# Synopsis:
# Defines the Makefile for building the program with GNU make.
#
# Copyright (c) 2024 Misael Díaz-Maldonado
# This file is released under the GNU General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#

all: busys dows

busys:
	@$(MAKE) -C busy
dows:
	@$(MAKE) -C dow
clean:
	@$(MAKE) -C busy clean
	@$(MAKE) -C dow clean
