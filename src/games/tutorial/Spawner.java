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

// spawner handles the level and score system and the spawning of enemies
public class Spawner
{

  // Spawner Attributes:


  private HUD hud;
  private Random rand;
  private Handler handler;
  private boolean levelup = false;
  private boolean spawned = false;


  // Constructors:


  public Spawner (HUD hud, Handler handler)
  {
    this.hud = hud;
    this.rand = new Random();
    this.handler = handler;
  }


  public void tick ()	// initial tick method, sets score and level in HUD
  {
    int score = hud.getScore();

    if (score != 0 && (score % 200) == 0)	// levels up every 200 points
    {
      hud.setLevel(hud.getLevel() + 1);
      levelup = true;
      spawned = false;
    }
    else
      levelup = false;
  }


  public void spawn ()	// spawns enemies at random locations after leveling up
  {
    // spawns enemies if it has not already done so
    if (levelup && !spawned)
      spawner();
  }


  private void spawner ()	// spawns a new enemy every level up (until level 20)
  {
    ID id;
    ID spawns;
    int x, y;
    Color color;
    BossEnemy boss;
    FastEnemy fast;
    BasicEnemy basic;
    SmartEnemy smart;

    if ( hud.getLevel() < 10 )	// spawns a group of basic enemies and a smart enemy
    {
      for (int i = 0; i != hud.getLevel(); ++i)
      {
	x = rand.nextInt(Game.WIDTH / 2);
	y = rand.nextInt(Game.HEIGHT / 2);
	id = ID.BasicEnemy;
	color = Color.red;
	basic = new BasicEnemy(x, y, id, color, handler);
	handler.addObject(basic);
      }

      // spawns smart enemy
      x = rand.nextInt(Game.WIDTH / 2);
      y = rand.nextInt(Game.HEIGHT / 2);
      id = ID.SmartEnemy;
      color = Color.green;
      smart = new SmartEnemy(x, y, id, color, handler);
      handler.addObject(smart);
    }

    if (hud.getLevel() ==  10)	// spawns a boss at level 10
    {
      // spawns boss around the middle of the game and hides part of its body so that
      // the player cannot go behind it
      x = rand.nextInt(Game.WIDTH / 2);
      y = -16;
      id = ID.BossEnemy;
      // sets the id of the enemies that the boss spawns
      spawns = ID.BasicEnemy;
      color = Color.red;

      boss = new BossEnemy(x, y, id, Color.red, 96, 96, spawns, handler);
      handler.addObject(boss);
    }

    // Spawns groups of fast enemies and smart enemies
    // Note that the boss is indestructible and that it lasts for five levels so we do not
    // spawn enemies until it goes away.
    if ( (hud.getLevel() >= 15) && (hud.getLevel() < 20) )
    {
      // sets the group size for fast enemies
      int count = Game.clamp(hud.getLevel() - 4, 1, 31);
      for (int i = 0; i != count; ++i)
      {
	x = rand.nextInt(Game.WIDTH / 2);
	y = rand.nextInt(Game.HEIGHT / 2);
	id = ID.FastEnemy;
	color = Color.yellow;

	fast = new FastEnemy(x, y, id, color, handler);
	handler.addObject(fast);
      }

      // sets the group size of smart enemies
      count = Game.clamp(hud.getLevel() - 12, 1, 31);
      for (int i = 0; i != count; ++i)
      {
	x = rand.nextInt(Game.WIDTH / 2);
	y = rand.nextInt(Game.HEIGHT / 2);
	id = ID.SmartEnemy;
	color = Color.green;
	smart = new SmartEnemy(x, y, id, color, handler);
	handler.addObject(smart);
      }
    }

    // spawns a boss at level 20
    if (hud.getLevel() ==  20)
    {
      x = rand.nextInt(Game.WIDTH / 2);
      y = -16;
      id = ID.BossEnemy;
      spawns = ID.FastEnemy;
      color = Color.yellow;
      boss = new BossEnemy(x, y, id, color, 96, 96, spawns, handler);
      handler.addObject(boss);
    }

    // sets the spawned state so that we know that the spawner has completed its task
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
 * Boss Fights.
 * Since our player can destroy enemies we do not clear them out of the
 * game prior to spawning the boss in contrast to what the author of the
 * tutorial did. One could regard the boss as a dropship instead of a
 * larger enemy that shoots at the player.
 *
 */
