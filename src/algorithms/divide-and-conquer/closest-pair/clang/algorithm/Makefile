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

include algorithm-inc

all: $(ALGORITHM_OBJ)

$(ALGORITHM_OBJ): $(ALGORITHM_SRC)
	$(CC) $(CCOPT) -c $(ALGORITHM_SRC) -o $(ALGORITHM_OBJ)

clean:
	/bin/rm -rf *.o
