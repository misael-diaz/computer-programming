/*
 * Algorithms and Programming II                               May 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. From Let's Build a Game Tutorial.
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
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable
{

	// creates user-defined serial version UID for (de)serialization
	private static final long serialVersionUID = 1550691097823471818L;

	// defines window dimensions
	public static final int WIDTH = 640, HEIGHT = 3 * WIDTH / 4;
	// creates thread instance for the (serial) game
	private Thread thread;
	// initializes game running state to false
	private boolean running = false;

	public Game ()	// defines default constructor for the game
	{
		// creates a window of specified dimensions and title
		new Window (WIDTH, HEIGHT, "Let's Build a Game!", this);
	}


	public synchronized void start ()
	// starts instance of our game class
	{
		thread = new Thread (this);
		thread.start();
		running = true;
	}


	public synchronized void stop ()
	// stops instance of our game class
	{
		try
		{
			// (presumably) stops the thread
			thread.join ();
			running = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void run ()
	/* implements game loop, borrowed code */
	{
		// gets current time in nanoseconds
		long lastTime = System.nanoTime();
		// defines the number of ticks in a second
		double amountOfTicks = 60;
		// defines the ticking period in nanoseconds
		double ns = 1e9 / amountOfTicks;
		// initializes tick counter
		double delta = 0;

		// gets current time in milliseconds
		long timer = System.currentTimeMillis();
		// frames counter
		int frames = 0;

		while (running)
		{
			// gets the current time with nanosecond resolution
			long now = System.nanoTime();
			// increments number of ticks on each pass
			delta += (now - lastTime) / ns;
			// updates last known time
			lastTime = now;
			while (delta >= 1)
			// possibly creates a delay
			{
				tick();		// unimplemented
				--delta;
			}
			// NOTE: after the while-loop, delta ~ 0


			if (running)
			// renders buffer to control the framerate
				render();

			++frames;	// increments frame counter

			if (System.currentTimeMillis() - timer > 1000)
			// prints frames per second FPS every second
			{
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}

		stop();			// stops thread
	}


	private void tick ()		// unimplemented
	{
		return;
	}


	private void render ()
	// renders graphics buffer to throttle the frame rate to ~4000 FPS
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		// creates graphics buffer if it does not exist
		{
			// the tutorial author recommended using 3 or 4
			this.createBufferStrategy(4);
			return;
		}

		Graphics g = bs.getDrawGraphics();	// renders graphic
		g.setColor(Color.black);		// black background
		g.fillRect(0, 0, WIDTH, HEIGHT);	// fills window
		g.dispose();				// frees resources
		bs.show();				// displays graphic
	}

	public static void main (String [] args)
	// creates instance of our game
	{
		new Game ();
	}
}


/*
 * COMMENTS:
 * The author of the tutorial did not provide any explanation about the
 * SerialVersionUID other than it's needed. Digging around should lead to
 * some answers but for the time being simply go along with the flow, it
 * might be explained later.
 *
 */
