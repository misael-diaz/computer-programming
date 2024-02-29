#include <cstdlib>
#include <iostream>

void menu(void);
void prompt(void);
void pause(void);

int main ()
{
	menu();
	prompt();
	pause();
	return 0;
}

void menu (void)
{
	int i = 0;
	while (i != 9) {
		std::cout << "mult table of " << (i + 1) << std::endl;
		++i;
	}
}

void prompt (void)
{
	int table = 0;
	std::cout << "please select a multiplication table:";
	std::cin >> table;

	if (table < 1 || table > 9) {
		std::cout << "there's no such table" << std::endl;
		std::cout << "please try again later" << std::endl;
		exit(EXIT_FAILURE);
	}

	for (int i = 0; i != 9; ++i) {
		int const res = (i + 1) * table;
		std::cout << (i + 1) << " x " << table << " = " << res << std::endl;
	}
}

#if defined(_WIN32) || defined(_WIN64)
void pause (void)
{
	system("pause");
}
#else
void pause (void)
{
	return;
}
#endif

/*

Loops					February 11, 2024

source: mult.cpp
author: @misael-diaz

Copyright (C) 2024 Misael Díaz-Maldonado

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

References:

[0] https://en.cppreference.com/w/cpp
[1] https://www.man7.org/linux/man-pages/man3/system.3.html

*/
