#!/usr/bin/make
#
# Loops					February 10, 2024
#
# source: src/busy/Makefile
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

all: $(MULT_BIN)

$(MULT_BIN): $(MULT_OBJ)
	$(CXX) $(CXXOPT) $(MULT_OBJ) -o $(MULT_BIN)

$(MULT_OBJ): $(HEADERS) $(MULT_CXX)
	$(CXX) $(INC) $(CXXOPT) -c $(MULT_CXX) -o $(MULT_OBJ)

clean:
	/bin/rm -f *.obj *.bin
