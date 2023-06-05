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
import java.awt.Rectangle;

public class Player extends GameObject
// defines the Player with attributes inherited from the Game Object Class
{

  // Player Attributes:


  // locks player weapons
  private boolean attack = false;
  // defines the shape of our player (circle or square)
  private boolean shape = true;
  // defines the color of our player
  private Color color = Color.blue;
  // defines the trailspan of our player
  private float trailspan = 0.15f;
  // defines limits for the coordinates of our player
  private int min_x = 0, max_x = (Game.WIDTH - 32);
  private int min_y = 0, max_y = (Game.HEIGHT - 64);
  // collision handler
  private Handler handler;


  // Constructors:


  public Player (int x, int y, ID id, Handler handler)
  {
    super (x, y, id);
    this.handler = handler;
  }


  // Methods:


  public void shoot ()
    // toggles attack mode
  {
    if (attack)
      attack = false;
    else
      attack = true;
  }


  private void fire ()
    // fires projectiles in the vertical direction (bottom to top)
  {
    if (attack && (HUD.HEALTH != 0) )
    {
      // left cannon
      Projectile bullet;
      bullet = new Projectile(x - 4, y, ID.Projectile,
	  Color.cyan, false, 8, 8,
	  handler);

      handler.addObject (bullet);


      // center cannon
      bullet = new Projectile(x + (width / 2) - 4, y,
	  ID.Projectile,
	  Color.magenta, false, 8, 8,
	  handler);

      handler.addObject (bullet);


      // right cannon
      bullet = new Projectile(x + width - 4, y,
	  ID.Projectile, Color.cyan,
	  false, 8, 8, handler);

      handler.addObject (bullet);
    }
  }


  public void tick ()
    // initial tick method, updates position by a constant velocity
    // confines the player to the game boundaries, handles damage from
    // enemies, and simulates the player trail (after image).
  {
    // updates player position:

    if (HUD.HEALTH != 0)
    {
      x += v_x;
      y += v_y;
    }
    else
    {
      x += 0;
      y += 0;
    }

    // simulates rigid boundaries:

    x = clamp (x, min_x, max_x);
    y = clamp (y, min_y, max_y);

    // simulates player trail:

    Trail trail = new Trail(x, y, ID.Trail, color, shape,
	trailspan, width, height, handler);

    handler.addObject (trail);

    // fires at enemies:

    fire();

    // drains player health upon collisions with enemies:

    collision();
  }


  public void render (Graphics g)
    // initial render method
  {
    // sets player color
    g.setColor (color);

    // renders player according to its shape
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


  // int clamp (int pos, int min, int max)
  //
  // Synopsis:
  // Possible implementation of a rigid boundary.
  //
  // Inputs:
  // pos		position coordinate
  // min		minimum allowed position
  // max		maximum allowed position
  //
  // Ouput
  // pos		the position after applying boundary condition


  private int clamp (int pos, int min, int max)
  {
    if (pos < min)
      return min;
    else if (pos > max)
      return max;
    else
      return pos;
  }


  // int periodic (int pos, int min, int max)
  //
  // Synopsis:
  // Possible implementation of a periodic boundary.
  //
  // Inputs:
  // pos		position coordinate
  // min		minimum allowed position
  // max		maximum allowed position
  //
  // Ouput
  // pos		the position after applying boundary condition


  private int periodic (int pos, int min, int max)
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


  private void collision ()	// decrements player health upon collisions with enemies
  {
    for (int i = 0; i != handler.objects.size(); ++i)
    {
      GameObject obj = handler.objects.get(i);

      if (obj.getID() == ID.BasicEnemy)
      {
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	  --HUD.HEALTH;
      }
      else if (obj.getID() == ID.SmartEnemy)
      {
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	  --HUD.HEALTH;
      }
      else if (obj.getID() == ID.FastEnemy)
      {
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	  HUD.HEALTH -= 4;
      }
      else if (obj.getID() == ID.BossEnemy)
      {
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	  HUD.HEALTH -= 100;
      }
    }
  }
}

/*
 * COMMENTS:
 * The `super' keyword is used here to invoke the constructor of the
 * parent class, which in this case refers to the GameObject class.
 *
 * Note: fast enemies hit harder than basic enemies.
 *
 */
