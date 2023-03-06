#!/usr/bin/make
#
# Closest Pair Lab						February 23, 2023
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

include prng-test-inc

all: $(TEST_PRNG_BIN)

$(TEST_PRNG_BIN): $(OBJS) $(TEST_PRNG_OBJ)
	$(CC) $(CCOPT) $(OBJS) $(TEST_PRNG_OBJ) -o $(TEST_PRNG_BIN) $(LIBS)

$(TEST_PRNG_OBJ): $(TEST_PRNG_SRC)
	$(CC) $(CCOPT) -c $(TEST_PRNG_SRC) -o $(TEST_PRNG_OBJ)

clean:
	/bin/rm -rf *.o $(TEST_PRNG_BIN)
