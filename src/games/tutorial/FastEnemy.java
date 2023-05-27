/*
 * Algorithms and Programming II                               May 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Fast Enemy Class. From Let's Build a Game Tutorial.
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

public class FastEnemy extends GameObject
// defines Fast Enemy with attributes inherited from the Game Object Class
{

  // Fast Enemy Attributes:

  private Color color;			// enemy color
  private boolean shape = false;	// enemy shape (rectangle)
  private float trailspan = 0.30f;	// defines enemy trail span
  private Handler handler;		// enemy trail handler


  // Constructors:


  public FastEnemy (int x, int y, ID id, Color color, Handler handler)
  {
    // creates fast enemy object
    super (x, y, id);

    // defines its attributes
    this.color = color;
    this.handler = handler;
    v_x = 3;
    v_y = 7;
  }


  // Methods:


  public void shoot ()	// unimplemented intentionally
  {
    // we need to define this method because objects derived from the
    // Game Object class must define it.
    return;
  }


  public void tick ()	// initial tick method
  {

    // marks enemy as garbage if it has been destroyed,
    // otherwise updates its position and velocity
    if ( isDestroyed() )
    {
      setGarbage();	// sets garbage state to true
      return;
    }

    // updates position:

    x += v_x;
    y += v_y;

    // simulates elastic collisions with boundaries:

    if ( x <= 0 || x >= (Game.WIDTH - 16) )
    {
      v_x *= -1;
    }

    if ( y <= 0 || y >= (Game.HEIGHT - 64) )
    {
      v_y *= -1;
    }

    // simulates enemy trail:

    Trail trail = new Trail(x, y, ID.Trail, color, shape, trailspan, width, height,
			    handler);

    handler.addObject (trail);
  }


  public void render (Graphics g)	// initial render method
  {
    g.setColor (color);			// sets enemy color

    if (shape)				// renders enemy according to its shape
    {
      g.fillOval (x, y, width, height);	// circle
    }
    else
    {
      g.fillRect (x, y, width, height);	// square
    }
  }


  public Rectangle getBounds ()		// we need this for collision detection
  {
    return new Rectangle (x, y, width, height);
  }
}

// COMMENTS:
// The `super' keyword is used here to invoke the constructor of the
// parent class, which in this case refers to the GameObject class.
//
// The trail of the enemy is simulated by spawning trail objects that
// fade with time (via calls to the trail tick method).
//
// We do not remove enemies right away to avoid serious issues while
// traversing the linked-list.
