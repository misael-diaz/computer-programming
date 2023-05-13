/*
 * Algorithms and Programming II                               May 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Window Backend. From Let's Build a Game Tutorial.
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

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends Canvas
// purpose of the class is to create the window for us
{

	// creates user-defined serial version UID for (de)serialization
	private static final long serialVersionUID = 240840600533728354L;
	private JFrame frame;

	public Window (int width, int height, String title, Game game)
	// creates a window of specified dimensions for our game
	{
		// creates the frame (or window) for our game
		frame = new JFrame (title);

		// fixes the window dimensions
		frame.setPreferredSize ( new Dimension(width, height) );
		frame.setMaximumSize   ( new Dimension(width, height) );
		frame.setMinimumSize   ( new Dimension(width, height) );

		// enables closing the window to terminate game gracefully
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// we don't want a resizable window to avoid future issues
		frame.setResizable (false);
		// spawns window at the middle of the screen
		frame.setLocationRelativeTo (null);
		// adds the game to the window
		frame.add (game);

		// makes the window visible
		frame.setVisible (true);
		// starts the game instance
		game.start ();
	}

	public void dispose ()
	// disposes of the jframe to terminate the application gracefully
	{
		System.out.println("disposing of window ...");
		frame.setVisible (false);
		frame.dispose ();
		System.exit (0);
	}
}

/*
 * COMMENTS:
 * The dispose() method was based on the following stack-overflow answer:
 *
 * 		https://stackoverflow.com/a/10873683
 *
 * Because we do not need to do other closing actions we do not add a
 * listener, for the thread that runs our game loop will be halted on exit.
 * And that's exactly what we want.
 *
 */
