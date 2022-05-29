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
import java.awt.Rectangle;

public class BasicEnemy extends GameObject
// defines Basic Enemy with attributes inherited from the Game Object Class
{

	private boolean shape = false;		// enemy shape (rectangle)
	private Color color;			// enemy color
	private float trailspan = 0.25f;	// defines enemy trail span
	private int width = 16, height = 16;	// defines enemy dimensions
	private Handler handler;		// enemy trail handler


	/* Constructors */


	public BasicEnemy (int x, int y, ID id, Color color,
			   Handler handler)
	{
		super (x, y, id);
		this.color = color;
		this.handler = handler;
		v_x = 5;
		v_y = 5;
	}


	/* Methods */


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

		/* simulates enemy trail */

		Trail trail = new Trail(x, y, ID.Trail, color, shape,
					trailspan, width, height, handler);

		handler.addObject (trail);
	}

	public void render (Graphics g)
	// initial render method
	{
		// sets enemy color
		g.setColor (color);

		// renders enemy according to its shape
		if (shape)
			g.fillOval (x, y, width, height);	// circle
		else
			g.fillRect (x, y, width, height);	// square
	}

	public Rectangle getBounds ()
	{
		return new Rectangle (x, y, width, height);
	}
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 * The trail of the enemy is simulated by spawning trail objects that
 * fade with time (via calls to the trail tick method).
 *
 */
