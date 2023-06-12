/*
 * Algorithms and Programming II                               May 28, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Key Input. From Let's Build a Game Tutorial.
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
 * [3] docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html
 *
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// defines the key input class for processing input from the player
public class KeyInput extends KeyAdapter
{

  // we use this boolean array to avert sticky player movements
  private boolean [] keyDown = {false, false, false, false};
  // as for other objects this class needs a handler
  private Handler handler;


  // Constructors:


  public KeyInput (Handler handler)
  {
    this.handler = handler;
  }


  // Methods:


  @Override
  public void keyPressed (KeyEvent e)	// moves player according to the pressed key
  {
    // gets code of pressed key
    int key = e.getKeyCode();


    // gets the player
    GameObject object = handler.objects.get(0);


    // complains if the player is not at the front of the linked-list
    String errMSG = ("KeyInput(): expects the player at the front of the linked-list");
    if (object.getID() != ID.Player)
      throw new RuntimeException(errMSG);


    // handles key events for player:


    // maps pressed keys to player movements
    // sets velocity according to pressed key
    switch (key)
    {
      case KeyEvent.VK_UP:
	keyDown[0] = true;
	object.setVelY (-5);
	break;
      case KeyEvent.VK_DOWN:
	keyDown[1] = true;
	object.setVelY (+5);
	break;
      case KeyEvent.VK_LEFT:
	keyDown[2] = true;
	object.setVelX (-5);
	break;
      case (KeyEvent.VK_RIGHT):
	keyDown[3] = true;
	object.setVelX (+5);
	break;
      case (KeyEvent.VK_SPACE):
	object.shoot ();
	break;
      case (KeyEvent.VK_R):
	HUD.HEALTH = 100;
	break;
    }
  }


  @Override
  public void keyReleased (KeyEvent e)	// restores defaults according to the released key
  {
    // gets released key code
    int key = e.getKeyCode();


    // gets the player
    GameObject object = handler.objects.get(0);


    // complains if the player is not at the front of the linked-list
    String errMSG = ("KeyInput(): expects the player at the front of the linked-list");
    if (object.getID() != ID.Player)
      throw new RuntimeException(errMSG);


    // handles key events for player:


    // resets velocity to default (stationary)
    switch (key)
    {
      case KeyEvent.VK_UP:
	keyDown[0] = false;
	break;
      case KeyEvent.VK_DOWN:
	keyDown[1] = false;
	break;
      case KeyEvent.VK_LEFT:
	keyDown[2] = false;
	break;
      case (KeyEvent.VK_RIGHT):
	keyDown[3] = false;
	break;
      case (KeyEvent.VK_SPACE):
	break;
      case (KeyEvent.VK_R):
	break;
    }

    // zeroes vertical movement
    if (!keyDown[0] && !keyDown[1])
      object.setVelY (0);

    // zeroes horizontal movement
    if (!keyDown[2] && !keyDown[3])
      object.setVelX (0);
  }
}


// COMMENTS:
// The class that extends KeyAdapter only has to override the methods
// it needs. The Override keyword expresses that we are modifying the
// implementation of the method that follows it.
//
// By design we expect the player to be at the front of the linked-list
// (that is, to be the first object in the linked-list).
//
// We set the player velocity to zero only if the user is not pressing
// opposing arrow keys consecutively to avoid the sticky effect bug.
// Now the user can quickly press down opposing keys for tight maneuvers.
