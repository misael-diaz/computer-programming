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
import java.util.Random;

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

	private Handler handler;// handler object utility
	private Random rand;	// pseudo random number generator PRNG
	private HUD hud;	// heads-up display

	public Game ()	// defines default constructor for the game
	{

		hud = new HUD ();		// instantiates HUD
		rand = new Random ();		// instantiates PRNG
		handler = new Handler ();	// instantiates handler

		// adds key listener to the game for capturing player input
		this.addKeyListener ( new KeyInput (handler) );


		/* Demo (this will possibly change in a future revision) */


		int W = WIDTH, H = HEIGHT;	// aliases width and height

		// spawns player at the center of the window
		Player player = new Player (W/2 - 32, H/2 - 32, ID.Player,
				            handler);
		handler.addObject (player);	// adds player to handler

		// spawns basic enemies "bouncers"
		for (int i = 0; i != 5; ++i)
		{
			BasicEnemy enemy;
			enemy = new BasicEnemy (rand.nextInt(W/2),
						rand.nextInt(H/2),
						ID.BasicEnemy,
						Color.red,
						handler);

			handler.addObject (enemy);
		}

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
		// request focus so that we don't have to click the game
		// window to be able to control the player, with this
		// option the player responds to commands right away
		this.requestFocus();
		// gets current time in nanoseconds
		long lastTime = System.nanoTime();
		// defines the number of ticks in a second
		double amountOfTicks = 60;
		// defines the ticking period in nanoseconds
		double ns = 1e9 / amountOfTicks;
		// initializes tick accumulator
		double delta = 0;

		// gets the current system time in milliseconds
		long timer = System.currentTimeMillis();
		// frames counter
		int frames = 0;

		while (running)
		{

			/* implements our game clock */


			// gets the current time with nanosecond resolution
			long now = System.nanoTime();
			// accumulates ticks in the current timespan
			delta += (now - lastTime) / ns;
			// updates last known time
			lastTime = now;


			/* updates the positions of the game objects */


			// gets (integral) number of elapsed ticks thus far
			long ticks = ( (long) Math.floor (delta) );
			// moves objects according to the number of ticks
			for (long n = 0; n != ticks; ++n)
				tick();

			// decrements tick accumulator for the next pass
			delta -= ( (double) ticks );


			/* removes garbage objects from game */


			garbageCollector ();


			/* renders game objects */


			if (running)
			// renders objects while controlling the framerate
				render();

			++frames;	// increments frame counter


			/* reports framerate */


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


	private void garbageCollector ()
	// delegates the task of removing garbage objects to the handler
	{
		handler.garbageCollector ();
	}


	private void tick ()		// tick method
	{
		handler.tick();		// updates game object positions
		hud.tick();		// updates HUD
		return;
	}


	private void render ()
	// renders game objects, note that a graphics buffer is also
	// rendered to throttle the frame rate to about 8000 FPS
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		// creates graphics buffer if it does not exist
		{
			// the tutorial author recommended using 3 or 4
			this.createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();	// renders graphic
		g.setColor(Color.black);		// black background
		g.fillRect(0, 0, WIDTH, HEIGHT);	// fills window
		handler.render(g);			// renders objects
		hud.render(g);				// renders heads-up
		g.dispose();				// frees resources
		bs.show();				// displays graphic
	}


	public static int clamp (int val, int min, int max)
	/*
	 * Synopsis:
	 * General purpose method for imposing limits on variables.
	 * Clamps values to the range [min, max].
	 *
	 * Inputs:
	 * val		value
	 * min		minimum allowed value
	 * max		maximum allowed value
	 *
	 * Ouput
	 * val		returns min, max, or the original value
	 *
	 */
	{
		if (val < min)
			return min;
		else if (val > max)
			return max;
		else
			return val;
	}


	public static float clamp (float val, float min, float max)
	/*
	 * Synopsis:
	 * General purpose method for imposing limits on variables.
	 * Clamps values to the range [min, max].
	 *
	 * Inputs:
	 * val		value
	 * min		minimum allowed value
	 * max		maximum allowed value
	 *
	 * Ouput
	 * val		returns min, max, or the original value
	 *
	 */
	{
		if (val < min)
			return min;
		else if (val > max)
			return max;
		else
			return val;
	}


	public static double clamp (double val, double min, double max)
	/*
	 * Synopsis:
	 * General purpose method for imposing limits on variables.
	 * Clamps values to the range [min, max].
	 *
	 * Inputs:
	 * val		value
	 * min		minimum allowed value
	 * max		maximum allowed value
	 *
	 * Ouput
	 * val		returns min, max, or the original value
	 *
	 */
	{
		if (val < min)
			return min;
		else if (val > max)
			return max;
		else
			return val;
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
