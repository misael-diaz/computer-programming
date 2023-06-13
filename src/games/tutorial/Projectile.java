/*
 * Algorithms and Programming II                               May 30, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Projectile Class. From Let's Build a Game Tutorial.
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

public class Projectile extends GameObject
// defines a basic projectile object
{

  // Projectile Attributes:


  private boolean shape;		// circle, otherwise rectangle
  private Color color;		// projectile color
  private Handler handler;	// collisions handler

  // defines the game boundaries for the projectiles
  private int min_y = 0, max_y = (Game.HEIGHT - 64);


  // Constructors:


  public Projectile (int x, int y, ID id, Color color, boolean shape,
      int width, int height, Handler handler)
  {
    // creates projectile object
    super (x, y, id, width, height);

    // defines its attributes
    this.shape = shape;
    this.color = color;
    this.handler = handler;

    // sets projectile velocity
    v_x = this.handler.objects.get(0).getVelX();
    v_y = -10;
  }


  // Methods:


  public void shoot ()
    // note that we do not have to implement this method unless
    // we have projectiles that can shoot at enemies ;)
  {
    return;
  }


  // void tick()
  //
  // Synopsis:
  // Marks projectiles that have hit enemies for removal by
  // setting the garbage state to true; otherwise, updates the
  // projectile coordinates and checks for collisions with enemies.
  // Marks projectiles that have hit enemies or that have reached
  // the game boundaries for destruction. Note that destroyed
  // projectiles end up marked as garbage the next time the tick()
  // method is invoked. Also note that enemies hit by the projectiles
  // are marked for destruction as well.


  public void tick ()
  {
    // marks projectile for removal if it has hit an enemy:

    if ( isDestroyed() )
      setGarbage();
    else
    {
      // updates projectile coordinates:

      y += v_y;
      y = Game.clamp (y, min_y, max_y);

      // destroys enemies:

      collision();

      // destroys projectiles at game boundaries:

      if (y == min_y || y == max_y)
	setDestroyed();
    }

  }


  public void render (Graphics g)
    // initial render method
  {
    g.setColor (color);

    // renders projectile according to its shape
    if (shape)
      g.fillOval (x, y, width, height);	// circle
    else
      g.fillRect (x, y, width, height);	// square
  }


  public Rectangle getBounds ()
    // we need this method for collision detection
  {
    return new Rectangle(x, y, width, height);
  }


  private void collision ()
    // marks hit enemies and the projectiles themselves for removal
  {
    for (int i = 0; i != handler.objects.size(); ++i)
    {
      GameObject obj = handler.objects.get(i);

      if (obj.getID() == ID.BasicEnemy)
      {
	// detects collision
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	{
	  // destroys enemy and projectile
	  if ( !obj.isDestroyed() )
	  {
	    obj.setDestroyed();
	    this.setDestroyed();
	  }
	}
      }
      else if (obj.getID() == ID.SmartEnemy)
      {
	// detects collision
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	{
	  // destroys enemy and projectile
	  if ( !obj.isDestroyed() )
	  {
	    obj.setDestroyed();
	    this.setDestroyed();
	  }
	}
      }
      else if (obj.getID() == ID.FastEnemy)
      {
	// detects collision
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	{
	  // destroys enemy and projectile
	  if ( !obj.isDestroyed() )
	  {
	    obj.setDestroyed();
	    this.setDestroyed();
	  }
	}
      }
      else if (obj.getID() == ID.BossEnemy)
      {
	// detects collision
	Rectangle mask = obj.getBounds();
	if ( getBounds().intersects(mask) )
	{
	  // destroys projectile
	  this.setDestroyed();
	}
      }
    }
  }
}


// COMMENTS:
// The `super' keyword is used here to invoke the constructor of the
// parent class, which in this case refers to the GameObject class.
//
// Collisions. Note that collision detection is done in the same way we
// handle collisions between other game objects. This is to be expected
// since the projectiles themselves are also game objects.
//
// During collision detection we check if the enemy has been hit by another
// projectile by checking its destroyed state, for we want to be fair with
// the player. An enemy that has been marked as destroyed should not end up
// destroying other projectiles that could potentially hit other enemies.
//
// Destruction. We do not want to remove game objects while we are invoking
// the tick method because doing so can cause serious runtime problems if
// we are not careful. Note that removing an object from a linked-list
// invalidates the index we are using to traverse the linked-list because
// its size has changed due to the removal. It would only be safe to remove
// the last object in the linked-list but there's no guarantee that the
// object that we want to remove is actually at the end, most likely it
// will be around the middle of the linked-list. It is be possible to
// circumvent this issue some other way but we would have to change our
// algorithm. I prefer to keep things simple without delving further into
// the world of data structures at this level. An advantage of using this
// approach is that we can easily add a destruction sequence latter on.
//
// Creation, Spawning, or Insertion of Objects.
// We can safely add objects to the game while ticking because these are
// inserted at the end of the linked-list. It is easy to account for the
// size change with a while-loop.
