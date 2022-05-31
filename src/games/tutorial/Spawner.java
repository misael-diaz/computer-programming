/*
 * Algorithms and Programming II                               May 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Spawner. From Let's Build a Game Tutorial.
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
import java.util.Random;

public class Spawner
// spawner handles the level and score system and the spawning of enemies
{

	/* Spawner Attributes */


	private HUD hud;
	private Random rand;
	private Handler handler;
	private boolean levelup = false;
	private boolean spawned = false;


	/* Constructors */


	public Spawner (HUD hud, Handler handler)
	{
		this.hud = hud;
		this.rand = new Random();
		this.handler = handler;
	}


	public void tick ()
	// initial tick method, sets score and level in HUD
	{
		int score = hud.getScore();

		if (score != 0 && (score % 200) == 0)
		// levels up every 200 points
		{
			hud.setLevel(hud.getLevel() + 1);
			levelup = true;
			spawned = false;
		}
		else
			levelup = false;
	}


	public void spawn ()
	// spawns enemies at random locations after leveling up
	{
		// spawns enemies if it has not already done so
		if (levelup && !spawned)
			spawner ();
	}


	private void spawner ()
	// spawns a new enemy every level up
	{
		ID id;
		int x, y;
		Color color;
		FastEnemy fast;
		BasicEnemy basic;
		if ( hud.getLevel() >= 5 )
		// spawns a group of fast enemies and a basic enemy
		{
			int count = Game.clamp(hud.getLevel() - 4, 1, 31);
			for (int i = 0; i != count; ++i)
			{
				x = rand.nextInt(Game.WIDTH / 2);
				y = rand.nextInt(Game.HEIGHT / 2);
				id = ID.FastEnemy;
				color = Color.yellow;

				fast = new FastEnemy (x, y, id, color,
						      handler);
				handler.addObject (fast);
			}

			x = rand.nextInt(Game.WIDTH / 2);
			y = rand.nextInt(Game.HEIGHT / 2);
			id = ID.BasicEnemy;
			color = Color.red;
			basic = new BasicEnemy (x, y, id, color,
						handler);
			handler.addObject (basic);
		}
		else
		// spawns a basic enemy
		{
			x = rand.nextInt(Game.WIDTH / 2);
			y = rand.nextInt(Game.HEIGHT / 2);
			id = ID.BasicEnemy;
			color = Color.red;
			basic = new BasicEnemy (x, y, id, color,
						handler);
			handler.addObject (basic);
		}

		// sets the spawned state so that we know that the spawner
		// has completed its task
		spawned = true;
	}
}


/*
 * COMMENTS:
 * If the player does not destroy any enemies the game might eventually
 * stall because the exceedingly large number enemies that the application
 * needs to render. I could have imposed limits on the total number of
 * enemies at any given time but I did not wanted to invest much on a
 * piece of code that is likely to change in the near future.
 *
 */
