#!/usr/bin/make
#
# source: Makefile
# author: misael-diaz
# date:   2021/07/20
#
# Synopsis:
# Defines the Makefile for building the program with GNU make.
#
# Copyright (c) 2021 Misael Diaz-Maldonado
#
# This file is released under the GNU General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#

include make-inc

all: $(TESTS)

$(TEST_EXE): $(OBJECTS)
	$(CC) $(CCOPT) $(OBJECTS) -o $(TEST_EXE) $(LIBS)

$(TEST_OBJ): $(TEST_SRC)
	$(CC) $(CCOPT) -c $(TEST_SRC) -o $(TEST_OBJ)

clean:
	/bin/rm -rf *.o $(TESTS)
