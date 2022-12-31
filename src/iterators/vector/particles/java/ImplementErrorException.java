/*
 * Algorithms and Complexity                            December 09, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines the Implementation Error Exception Class.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] custom-exception: www.javatpoint.com/custom-exception
 *
 */

public class ImplementErrorException extends Exception
{
	ImplementErrorException (String errmsg)
	{
		super(errmsg);
	}
}
