/*
 * Algorithms and Programming II                               May 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. GameObject Class. From Let's Build a Game Tutorial.
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

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject
// players, enemies, items, etc. are instances of the Game Object Class
{

  /* game object attributes */


  // sets the default width and height of game objects
  private int default_GameObjectWidth = 16;
  private int default_GameObjectHeight = 16;


  protected int x, y;		// position vector components
  protected int v_x, v_y;		// velocity vector components
  protected ID id;		// game object ID

  protected int width, height;	// width and height of object
  protected boolean destroy;	// destroyed state
  protected boolean garbage;	// garbage state


  /* constructors */


  public GameObject (int x, int y, ID id)
  {
    this.x = x;
    this.y = y;
    this.id = id;

    if (this.id == ID.BasicEnemy)
    {
      this.width  = default_GameObjectWidth;
      this.height = default_GameObjectHeight;
    }
    else if (this.id == ID.FastEnemy)
    {
      this.width  = default_GameObjectWidth;
      this.height = default_GameObjectHeight;
    }
    else if (this.id == ID.SmartEnemy)
    {
      this.width  = default_GameObjectWidth;
      this.height = default_GameObjectHeight;
    }
    if (this.id == ID.Player)
    {
      this.width  = 2 * default_GameObjectWidth;
      this.height = 2 * default_GameObjectHeight;
    }

    this.garbage = false;
    this.destroy = false;
  }


  public GameObject (int x, int y, ID id, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.id = id;
    this.width = width;
    this.height = height;
    this.garbage = false;
    this.destroy = false;
  }


  /* abstract methods */


  public abstract void shoot();			// shoot method
  public abstract void tick();			// tick method
  public abstract void render (Graphics g);	// render method
  public abstract Rectangle getBounds();		// rectangle bounds


  /* setters */


  public void setPosX (int x)	// sets object position in x axis
  {
    this.x = x;
  }

  public void setPosY (int y)	// sets object position in y axis
  {
    this.y = y;
  }

  public void setVelX (int v_x)	// sets velocity along x axis
  {
    this.v_x = v_x;
  }

  public void setVelY (int v_y)	// sets velocity along y axis
  {
    this.v_y = v_y;
  }

  public void setID (ID id)	// sets object ID
  {
    this.id = id;
  }

  public void setGarbage ()	// sets garbage state
  {
    this.garbage = true;
  }

  public void setDestroyed ()	// sets destroyed state
  {
    this.destroy = true;
  }


  /* getters */


  public int getPosX ()		// gets object position in x axis
  {
    return x;
  }

  public int getPosY ()		// gets object position in y axis
  {
    return y;
  }

  public int getVelX ()		// gets x-axis velocity component
  {
    return v_x;
  }

  public int getVelY ()		// gets y-axis velocity component
  {
    return v_y;
  }

  public ID getID ()		// gets object ID
  {
    return id;
  }

  public int getWidth ()		// gets object width
  {
    return width;
  }

  public int getHeight ()		// gets object height
  {
    return height;
  }

  public boolean isGarbage ()	// gets garbage state
  {
    return garbage;
  }

  public boolean isDestroyed ()	// gets destroyed state
  {
    return destroy;
  }
}


/*
 * COMMENTS:
 * Protected variables can be accessed by inherited objects.
 *
 * Abstract methods must be implemented by the Class that extends the
 * Game Object Class.
 *
 */
