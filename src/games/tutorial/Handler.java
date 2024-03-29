/*
 * Algorithms and Programming II                               May 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Handler. From Let's Build a Game Tutorial.
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
import java.util.LinkedList;

// maintains and renders all of our objects in our game
// loops through all our objects in our game and updates them individually
public class Handler
{

  // creates a list for containing all of our game objects
  LinkedList<GameObject> objects = new LinkedList<GameObject>();


  // Methods:


  public void addObject (GameObject object)	// adds object to list of game objects
  {
    this.objects.add(object);
  }


  public void removeObject (GameObject object)	// removes object from list
  {
    this.objects.remove(object);
  }


  public void tick ()				// invokes the game object tick method
  {
    int i = 0;
    while ( i != objects.size() )
    {
      GameObject object = objects.get(i);
      object.tick();
      ++i;
    }
  }


  public void render (Graphics g)		// invokes the game object render method
  {
    for (int i = 0; i != objects.size(); ++i)
    {
      GameObject object = objects.get(i);
      object.render(g);
    }
  }


  public void garbageCollector ()		// removes garbage objects from game
  {
    this.objects.removeIf ( object -> object.isGarbage() );
  }
}


// COMMENTS:
// A while-loop is used to invoke the tick() method because some of the
// game objects create other game objects such as the player and enemies.
// Enemies create after images of themselves to simulate a trail. The
// player can shoot at enemies by means of projectiles and these are also
// implemented as game objects. Note that it is safe to do this because
// new objects are inserted at the end of the linked-list. The while-loop
// is necessary to properly account for the change in size of the
// linked-list.
