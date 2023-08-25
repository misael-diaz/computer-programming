#!/usr/bin/make
#
# Algorithms and Complexity				August 24, 2023
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

all: $(TESTS)

$(TEST_BIN): $(OBJECTS)
	$(CC) $(CCOPT) $(OBJECTS) -o $(TEST_BIN) $(LIBS)

$(POINT_O): $(POINT_H) $(POINT_C)
	$(CC) $(CCOPT) -c $(POINT_C) -o $(POINT_O)

$(PAIR_O): $(POINT_O) $(PAIR_H) $(PAIR_C)
	$(CC) $(CCOPT) -c $(PAIR_C) -o $(PAIR_O)

$(ENSEMBLE_O): $(POINT_O) $(ENSEMBLE_H) $(ENSEMBLE_C)
	$(CC) $(CCOPT) -c $(ENSEMBLE_C) -o $(ENSEMBLE_O)

$(UTIL_O): $(POINT_O) $(PAIR_O) $(ENSEMBLE_O) $(UTIL_H) $(UTIL_C)
	$(CC) $(CCOPT) -c $(UTIL_C) -o $(UTIL_O)

$(TEST_O): $(UTIL_O) $(TEST_C)
	$(CC) $(CCOPT) -c $(TEST_C) -o $(TEST_O)

clean:
	/bin/rm -rf *.o *.bin
