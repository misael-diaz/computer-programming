#
# Algorithms                                               August 04, 2023
#
# source: make-inc
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
	$(CC) $(CCOPT) $(OBJECTS) -o $(TEST_BIN)

$(TEST_OBJ): $(TEST_SRC)
	$(CC) $(CCOPT) -c $(TEST_SRC) -o $(TEST_OBJ)

clean:
	/bin/rm -rf *.o *.bin
