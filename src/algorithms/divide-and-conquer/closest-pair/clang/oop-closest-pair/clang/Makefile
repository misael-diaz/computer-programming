#!/usr/bin/make
#
# Algorithms and Complexity				August 10, 2023
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

$(POINT_OBJ): $(POINT_SRC)
	$(CC) $(CCOPT) -c $(POINT_SRC) -o $(POINT_OBJ)

$(PAIR_OBJ): $(POINT_OBJ) $(PAIR_SRC)
	$(CC) $(CCOPT) -c $(PAIR_SRC) -o $(PAIR_OBJ)

$(ENSEMBLE_OBJ): $(POINT_OBJ) $(ENSEMBLE_SRC)
	$(CC) $(CCOPT) -c $(ENSEMBLE_SRC) -o $(ENSEMBLE_OBJ)

$(UTIL_OBJ): $(POINT_OBJ) $(PAIR_OBJ) $(ENSEMBLE_OBJ) $(UTIL_SRC)
	$(CC) $(CCOPT) -c $(UTIL_SRC) -o $(UTIL_OBJ)

$(TEST_OBJ): $(UTIL_OBJ) $(TEST_SRC)
	$(CC) $(CCOPT) -c $(TEST_SRC) -o $(TEST_OBJ)

clean:
	/bin/rm -rf *.o *.bin
