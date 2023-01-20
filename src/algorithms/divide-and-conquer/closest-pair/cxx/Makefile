#!/usr/bin/make
#
# Algorithms and Complexity			           January 16, 2023
# IST 4310
#
# Synopsis:
# Defines the Makefile for building the project source files with GNU make.
#
# Copyright (c) 2023 Misael Diaz-Maldonado
# This file is released under the GNU General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#

include make-inc

all: $(TESTS)

$(MAIN_EXE): $(OBJECTS)
	$(CXX) $(CXXOPT) $(OBJECTS) -o $(MAIN_EXE)

$(DUPLICATE_CP_EXCEPT_CLASS_OBJ): $(DUPLICATE_CP_EXCEPT_CLASS_H)\
		$(DUPLICATE_CP_EXCEPT_CLASS_CXX)
	$(CXX) $(CXXOPT) -c $(DUPLICATE_CP_EXCEPT_CLASS_CXX) -o\
		$(DUPLICATE_CP_EXCEPT_CLASS_OBJ)

$(POINT_CLASS_OBJ): $(COMPARATOR_TEMPLATE_FUN_H) $(POINT_CLASS_H) $(POINT_CLASS_CXX)
	$(CXX) $(CXXOPT) -c $(POINT_CLASS_CXX) -o $(POINT_CLASS_OBJ)

$(PAIR_CLASS_OBJ): $(COMPARATOR_TEMPLATE_FUN_H) $(POINT_CLASS_H)\
		$(HANDLER_TEMPLATE_CLASS_H) $(PAIR_CLASS_H) $(PAIR_CLASS_CXX)
	$(CXX) $(CXXOPT) -c $(PAIR_CLASS_CXX) -o $(PAIR_CLASS_OBJ)

$(ENSEMBLE_CLASS_OBJ): $(DUPLICATE_CP_EXCEPT_CLASS_H) $(POINT_CLASS_H) $(PAIR_CLASS_H)\
		$(HANDLER_TEMPLATE_CLASS_H) $(ENSEMBLE_CLASS_H) $(ENSEMBLE_CLASS_CXX)
	$(CXX) $(CXXOPT) -c $(ENSEMBLE_CLASS_CXX) -o $(ENSEMBLE_CLASS_OBJ)

$(TIME_COMPLEXITY_CLASS_OBJ): $(ENSEMBLE_CLASS_H) $(HANDLER_TEMPLATE_CLASS_H)\
		$(TIME_COMPLEXITY_CLASS_H) $(TIME_COMPLEXITY_CLASS_CXX)
	$(CXX) $(CXXOPT) -c $(TIME_COMPLEXITY_CLASS_CXX) -o $(TIME_COMPLEXITY_CLASS_OBJ)

$(MAIN_OBJ): $(TIME_COMPLEXITY_CLASS_H) $(MAIN_CXX)
	$(CXX) $(CXXOPT) -c $(MAIN_CXX) -o $(MAIN_OBJ)

clean:
	/bin/rm -rf $(OBJECTS) $(TESTS)
