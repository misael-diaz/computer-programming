/*
 * Algorithms and Programming II                               May 29, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Trail Class. From Let's Build a Game Tutorial.
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
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

// defines a basic trail
public class Trail extends GameObject
{

  // Trail Attributes:


  private Color color;			// trail color
  private float alpha = 1;		// defines alpha composite
  private float span;			// trail lifespan
  private float min_span = 0.05f;	// minimum trail lifespan
  private float max_span = 1.00f;	// maximum trail lifespan
  private boolean shape;		// circle, otherwise rectangle
  private Handler handler;		// fading handler


  // Constructors:


  public Trail (int x, int y, ID id, Color color, boolean shape,
      float span, int width, int height, Handler handler)
  {
    // creates trail object
    super(x, y, id, width, height);

    // defines its attributes
    this.span = Game.clamp(span, min_span, max_span);
    this.shape = shape;
    this.color = color;
    this.handler = handler;
  }


  // Methods:


  // we need to define this method because objects derived from the Game Object class
  // must define it.
  public void shoot ()
  {
    return;
  }


  public void tick ()	// initial tick method, fades trail until it disappears
  {
    if ( alpha > (1.0f - span) )
    {
      alpha -= min_span;
      alpha = Game.clamp(alpha, 0, 1);
    }
    else
    {
      this.garbage = true;
    }

  }

  public void render (Graphics g)	// initial render method
  {
    Graphics2D g2d = (Graphics2D) g;

    // fades the trail by tweeking the alpha composite
    g2d.setComposite( fade(alpha) );

    g.setColor(color);

    // renders trail according to its shape
    if (shape)
    {
      g.fillOval(x, y, width, height);	// circle
    }
    else
    {
      g.fillRect(x, y, width, height);	// square
    }

    // restores the alpha composite for other `solid' objects
    g2d.setComposite( fade(1) );
  }

  // We need to define this since the class extends GameObject
  // even if we are never going to use it, for the trail does not
  // collide with other objects since it is an after image effect.
  public Rectangle getBounds()
  {
    return null;
  }

  private AlphaComposite fade (float alpha)	// fading method
  {
    int type = AlphaComposite.SRC_OVER;
    return ( AlphaComposite.getInstance(type, alpha) );
  }
}

/*

COMMENTS:
The `super' keyword is used here to invoke the constructor of the
parent class, which in this case refers to the GameObject class.

Comments on fading or making transparent objets. After rendering
the trail (via the g.fillRect(x, y, width, height) call) we need to
restore the alpha composite value to `one' to render other non-trail
(or `solid') objects; otherwise we would be making transparent other
objects we do not intend to fade.

Abstract methods of a super class must be defined by derived objects.

 */
