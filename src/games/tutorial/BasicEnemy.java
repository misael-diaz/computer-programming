/*
 * Algorithms and Programming II                               May 28, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Basic Enemy Class. From Let's Build a Game Tutorial.
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

public class BasicEnemy extends GameObject
// defines Basic Enemy with attributes inherited from the Game Object Class
{
	public BasicEnemy (int x, int y, ID id)
	// constructor
	{
		super (x, y, id);
		v_x = 5;
		v_y = 5;
	}

	public void tick ()
	// initial tick method, constant velocity
	{
		x += v_x;
		y += v_y;

		/* simulates elastic collisions with the boundaries */

		if ( x <= 0 || x >= (Game.WIDTH - 16) )
			v_x *= -1;

		if ( y <= 0 || y >= (Game.HEIGHT - 64) )
			v_y *= -1;
	}

	public void render (Graphics g)
	// initial render method
	{
		g.setColor (Color.red);
		g.fillRect (x, y, 16, 16);
	}
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 */
