/*
 * Algorithms and Programming II                              June 04, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Boss Enemy Class. From Let's Build a Game Tutorial.
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
import java.util.Random;

public class BossEnemy extends GameObject
// defines Boss Enemy with attributes inherited from the Game Object Class
{

	/* Boss Enemy Attributes */

	private int ticks = 0;			// times boss fight
	private ID spawns;			// enemy ID that it spawns
	private Color color;			// enemy color
	private boolean shape = false;		// enemy shape (rectangle)
	private Handler handler;		// spawning handler


	/* Constructors */


	public BossEnemy (int x, int y, ID id, Color color, int width,
			  int height, ID spawns, Handler handler)
	{
		// creates boss enemy object
		super (x, y, id, width, height);

		// defines attributes
		this.color = color;
		this.spawns = spawns;
		this.handler = handler;
		v_x = 1;
		v_y = 0;
	}


	/* Methods */


	public void shoot ()	/* unimplemented */
	// we need to define this method because objects derived from the
	// Game Object class must define the abstract methods of the class
	{
		return;
	}


	public void tick ()
	// initial tick method
	{

		// marks enemy as garbage if it has been destroyed,
		// otherwise updates its position and velocity
		if ( isDestroyed() )
			setGarbage();	// sets garbage state to true
		else
		{
			/* updates position */

			x += v_x;
			y += v_y;

			/* simulates elastic collisions with boundaries */

			if ( x <= 0 || x >= (Game.WIDTH - width) )
				v_x *= -1;

			if ( y <= 0 || y >= (Game.HEIGHT - 64) )
				v_y *= -1;

			/* fires random projectiles at player */
			if (ticks != 0 && ticks % 50 == 0)
				fire ();


			/* destroys boss to remove it from the game */
			if (ticks != 0 && ticks % 1000 == 0)
				setDestroyed();

			++ticks;
		}

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
	// we need this for collision detection
	{
		return new Rectangle (x, y, width, height);
	}


	private void fire ()
	// fires enemies at the player
	{

		/* defines the attributes of the fired enemies */

		Random r = new Random ();	// PRNG for enemy speed
		Color color = this.color;	// color
		int posX = x + (width / 2);	// x-axis coordinate
		int posY = y + (height / 2);	// y-axis coordinate
		int minVel = 5, maxVel = 10;	// min and max velocity
		int vel;			// velocity

		switch (spawns)
		// spawns enemies acording to the type that the boss spawns
		{

			case BasicEnemy:
			// spawns basic enemy
			BasicEnemy basic = new BasicEnemy (posX, posY,
							   spawns, color,
							   handler);

			// sets basic enemy speed
			vel = minVel + r.nextInt(maxVel - minVel);
			basic.setVelX (vel);

			vel = minVel + r.nextInt(maxVel - minVel);
			basic.setVelY (vel);

			handler.addObject (basic);
			break;


			case FastEnemy:
			// spawns fast enemy
			FastEnemy fast = new FastEnemy (posX, posY, spawns,
							color, handler);

			minVel = 10;
			maxVel = 15;
			// sets fast enemy speed
			vel = minVel + r.nextInt(maxVel - minVel);
			fast.setVelX (vel);

			vel = minVel + r.nextInt(maxVel - minVel);
			fast.setVelY (vel);

			handler.addObject (fast);
			break;
		}
	}
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 * Note that the garbage collector will subsequently remove enemies
 * marked as garbage. We do not remove enemies right away to avert
 * serious issues while traversing the linked-list.
 *
 * Note that for selecting the enemy type that the boss spawns we used
 * the unqualified names BasicEnemy and FastEnemy instead of ID.BasicEnemy
 * and ID.FastEnemy, for this is what the Java compiler expects for enum
 * (data) types when used in switch case structures.
 *
 */
