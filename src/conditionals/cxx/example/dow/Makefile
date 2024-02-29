#!/usr/bin/make
#
# Conditionals					February 10, 2024
#
# source: src/dow/Makefile
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

include make-inc

all: $(DOW_BIN)

$(DOW_BIN): $(DOW_OBJ)
	$(CXX) $(CXXOPT) $(DOW_OBJ) -o $(DOW_BIN)

$(DOW_OBJ): $(HEADERS) $(DOW_CXX)
	$(CXX) $(INC) $(CXXOPT) -c $(DOW_CXX) -o $(DOW_OBJ)

clean:
	/bin/rm -f *.obj *.bin
