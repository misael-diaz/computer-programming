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

public abstract class GameObject
// players, enemies, items, etc. are instances of the Game Object Class
{

	/* game object attributes */


	protected int x, y;	// position (x, y - Cartesian coordinates)
	protected int v_x, v_y;	// velocity
	protected ID id;	// game object ID


	/* constructors */


	public GameObject (int x, int y, ID id)		// constructor
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}


	/* abstract methods */


	public abstract void tick();			// tick method
	public abstract void render (Graphics g);	// render method


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

}


/*
 * COMMENTS:
 * Protected variables can be accessed by inherited objects.
 *
 * Abstract methods must be implemented by the Class that extends the
 * Game Object Class.
 *
 */