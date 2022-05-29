/*
 * Algorithms and Programming II                               May 28, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Heads-Up Display HUD Class.
 * From Let's Build a Game Tutorial.
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

public class HUD
// defines the heads-up display
{

	// defines health limits
	private static int MIN_HEALTH = 0, MAX_HEALTH = 100;
	// initializes the player health
	public static int HEALTH = MAX_HEALTH;


	public void tick ()
	// demo tick method that drains health points from player
	{
		HEALTH = Game.clamp (HEALTH, MIN_HEALTH, MAX_HEALTH);
	}

	public void render (Graphics g)
	// initial implementation of the HUD renderer method
	{
		// renders hud background
		g.setColor (Color.gray);
		g.fillRect (15, 15, 200, 32);
		// renders health bar
		g.setColor (Color.green);
		g.fillRect (15, 15, 2 * HEALTH, 32);
		// renders hud frame
		g.setColor (Color.black);
		g.drawRect (15, 15, 200, 32);
	}
}

/*
 * COMMENTS:
 * Rationale of the author of the tutorial.
 * Since we are not going to define the variable `HEALTH' elsewhere
 * in the code we are defining it here as a public static variable
 * for ease of access. It won't be necessary to instantiate a HUD
 * object in order to access it, using HUD.HEALTH will do just fine.
 *
 */
