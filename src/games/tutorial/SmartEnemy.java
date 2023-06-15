/*
 * Algorithms and Programming II                              June 02, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Smart Enemy Class. From Let's Build a Game Tutorial.
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

// defines Smart Enemy with attributes inherited from the Game Object Class
public class SmartEnemy extends GameObject
{

  // Smart Enemy Attributes:

  private Color color;			// enemy color
  private boolean shape = false;	// enemy shape (rectangle)
  private float trailspan = 0.25f;	// defines enemy trail span
  private int speed = 4;		// defines enemy speed
  private GameObject player;		// references player
  private Handler handler;		// enemy trail handler


  // Constructors:


  public SmartEnemy (int x, int y, ID id, Color color, Handler handler)
  {
    // creates smart enemy object
    super(x, y, id);

    // defines attributes
    this.color = color;
    this.handler = handler;
    // sets initial velocity equal to the bias
    this.v_x = (speed / 2);
    this.v_y = (speed / 2);
    // binds to the player object
    this.player = handler.objects.get(0);
  }


  // Methods:


  // we need to define this method because objects derived from the
  // Game Object class must define the abstract methods of the class
  public void shoot ()
  {
    return;
  }


  public void tick ()	// initial tick method
  {

    // sets as garbage if it has been destroyed, updates position and velocity otherwise
    if ( isDestroyed() )
      setGarbage();	// sets garbage state to true
    else
    {
      // sets the velocity for chasing the player:

      chase();

      // updates position:

      x += v_x;
      y += v_y;

      // simulates elastic collisions with boundaries:

      if ( x <= 0 || x >= (Game.WIDTH - 16) )
	v_x *= -1;

      if ( y <= 0 || y >= (Game.HEIGHT - 64) )
	v_y *= -1;

      // simulates enemy trail:

      Trail trail = new Trail(x, y, ID.Trail, color, shape, trailspan, width, height,
			      handler);

      handler.addObject(trail);
    }

  }


  public void render (Graphics g)	// initial render method
  {
    // sets enemy color
    g.setColor(color);

    // renders enemy according to its shape
    if (shape)
      g.fillOval(x, y, width, height);	// circle
    else
      g.fillRect(x, y, width, height);	// square
  }


  public Rectangle getBounds ()		// we need this for collision detection
  {
    return new Rectangle(x, y, width, height);
  }


  private void chase ()	// sets the velocity of the enemy so that it chases the player
  {
    // gets player coordinates
    int playerPosX = this.player.getPosX();
    int playerPosY = this.player.getPosY();
    // gets player dimensions
    int playerWidth  = this.player.getWidth();
    int playerHeight = this.player.getHeight();
    // corrects position vector to the player's center
    playerPosX += (playerWidth / 2);
    playerPosY += (playerHeight / 2);

    // gets enemy coordinates
    int enemyPosX = this.getPosX();
    int enemyPosY = this.getPosY();
    // corrects position vector to the enemy's center
    enemyPosX += (this.width / 2);
    enemyPosY += (this.height / 2);


    // computes the distance between the player and enemy
    double diffPosX = (playerPosX - enemyPosX);
    double diffPosY = (playerPosY - enemyPosY);
    double distance = Math.sqrt(diffPosX * diffPosX + diffPosY * diffPosY);


    // sets the velocity along the relative position vector if the objects are not too
    // close to each other to avoid division by zero
    if (distance > 1)
    {
      // finds the unit, relative, position vector
      double ratio = diffPosX / distance;
      int u_x = ( (int) Math.floor(ratio) );

      ratio = diffPosY / distance;
      int u_y = ( (int) Math.floor(ratio) );

      // sets the velocity along the relative position vector to chase the player
      v_x = u_x * speed;
      v_y = u_y * speed;

      // adds a bias to improve the chase effect since we are using integers instead of
      // floats for the velocity components
      v_x += (speed / 2);
      v_y += (speed / 2);
    }
    else
    {
      // sets the velocity equal to the bias otherwise (too close)
      v_x = (speed / 2);
      v_y = (speed / 2);
    }
  }
}


// COMMENTS:
// The `super' keyword is used here to invoke the constructor of the
// parent class, which in this case refers to the GameObject class.
//
// The trail of the enemy is simulated by spawning trail objects that
// fade with time (via calls to the trail tick method).
//
// Note that the garbage collector will subsequently remove enemies
// marked as garbage. We do not remove enemies right away to avert
// serious issues while traversing the linked-list.
