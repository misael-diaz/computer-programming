/*
 * Algorithms and Programming II                               May 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Player Class. From Let's Build a Game Tutorial.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example
 * [1] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [2] www.gamedesigning.org/learn/java/
 *
 */

import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject
// defines the Player with attributes inherited from the Game Object Class
{
	public Player (int x, int y, ID id)
	// constructor
	{
		super (x, y, id);
		v_x = 1;
		v_y = 0;
	}

	public void tick ()
	// initial tick method, updates position by a constant velocity
	{
		x += v_x;
		y += v_y;
	}

	public void render (Graphics g)
	// initial render method
	{
		g.setColor (Color.blue);
		g.fillOval (x, y, 32, 32);
	}
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 */
