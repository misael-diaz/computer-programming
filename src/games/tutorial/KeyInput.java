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

public class KeyInput extends KeyAdapter
// defines the key input class for processing input from the player
{

	// as for other objects this class needs a handler
	private Handler handler;


	/* Constructors */


	public KeyInput (Handler handler)
	{
		this.handler = handler;
	}


	/* Methods */


	@Override
	public void keyPressed (KeyEvent e)
	// moves player according to the pressed key
	{
		int key = e.getKeyCode();	// gets code of pressed key

		for (int i = 0; i != handler.objects.size(); ++i)
		// searches sequentially for the player object
		{
			GameObject object = handler.objects.get(i);

			if (object.getID() == ID.Player)
			// handles key events for player
			{
				switch (key)
				// maps pressed keys to player movements
				// sets velocity according to pressed key
				{
					case KeyEvent.VK_UP:
						object.setVelY (-5);
						break;
					case KeyEvent.VK_DOWN:
						object.setVelY (+5);
						break;
					case KeyEvent.VK_LEFT:
						object.setVelX (-5);
						break;
					case (KeyEvent.VK_RIGHT):
						object.setVelX (+5);
						break;
				}
			}
		}
	}


	@Override
	public void keyReleased (KeyEvent e)
	// restores player defaults according to the released key
	{
		int key = e.getKeyCode();	// gets released key code

		for (int i = 0; i != handler.objects.size(); ++i)
		// searches sequentially for the player object
		{
			GameObject object = handler.objects.get(i);

			if (object.getID() == ID.Player)
			// handles key events for player
			{
				switch (key)
				// resets velocity to default (stationary)
				{
					case KeyEvent.VK_UP:
						object.setVelY (0);
						break;
					case KeyEvent.VK_DOWN:
						object.setVelY (0);
						break;
					case KeyEvent.VK_LEFT:
						object.setVelX (0);
						break;
					case (KeyEvent.VK_RIGHT):
						object.setVelX (0);
						break;
				}
			}
		}
	}
}


/*
 * COMMENTS:
 * The class that extends KeyAdapter only has to override the methods
 * it needs. The Override keyword expresses that we are modifying the
 * implementation of the method that follows it.
 *
 */
