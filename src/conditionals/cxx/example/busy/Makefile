#!/usr/bin/make
#
# Conditionals					February 10, 2024
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

all: $(BUSY_BIN)

$(BUSY_BIN): $(BUSY_OBJ)
	$(CXX) $(CXXOPT) $(BUSY_OBJ) -o $(BUSY_BIN)

$(BUSY_OBJ): $(HEADERS) $(BUSY_CXX)
	$(CXX) $(INC) $(CXXOPT) -c $(BUSY_CXX) -o $(BUSY_OBJ)

clean:
	/bin/rm -f *.obj *.bin
