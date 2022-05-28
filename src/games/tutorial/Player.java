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
	}

	public void tick ()
	// initial tick method, updates position by a constant velocity
	{
		x += v_x;
		y += v_y;

		/* simulates rigid boundaries */

		int min_x = 0;
		int max_x = (Game.WIDTH - 32);

		int min_y = 0;
		int max_y = (Game.HEIGHT - 64);

		x = clamp (x, min_x, max_x);
		y = clamp (y, min_y, max_y);
	}

	public void render (Graphics g)
	// initial render method
	{
		g.setColor (Color.blue);
		g.fillOval (x, y, 32, 32);
	}


	private int clamp (int pos, int min, int max)
	/*
	 * Synopsis:
	 * Possible implementation of a rigid boundary.
	 *
	 * Inputs:
	 * pos		position coordinate
	 * min		minimum allowed position
	 * max		maximum allowed position
	 *
	 * Ouput
	 * pos		the position after applying boundary condition
	 *
	 */
	{
		if (pos < min)
			return min;
		else if (pos > max)
			return max;
		else
			return pos;
	}


	private int periodic (int pos, int min, int max)
	/*
	 * Synopsis:
	 * Possible implementation of a periodic boundary.
	 *
	 * Inputs:
	 * pos		position coordinate
	 * min		minimum allowed position
	 * max		maximum allowed position
	 *
	 * Ouput
	 * pos		the position after applying boundary condition
	 *
	 */
	{
		// gets dimension lenght
		int L = (max - min);

		// applies periodic condition to the object position
		if (pos < min)
			return (pos + L);
		else if (pos > max)
			return (pos - L);
		else
			return pos;
	}
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 */
