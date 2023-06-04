/*
 * Algorithms and Programming II                              June 06, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Game Development. Game Menu. From Let's Build a Game Tutorial.
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

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class Menu extends MouseAdapter
// defines the game menu and help screens
{
  private class Button
    // defines menu buttons
  {
    // Button Attributes:

    private String label;		// label
    private Font font;		// label font
    private int x, y;		// position
    private int width, height;	// button dimensions
    private int h_offset;		// label horizontal offset
    private int v_offset;		// label vertical offset

    // Constructors:

    public Button  (Font font, String label, int x, int y,
	int width, int height, int h_offset,
	int v_offset)
    {
      this.x = x;
      this.y = y;
      this.font = font;
      this.label = label;
      this.width = width;
      this.height = height;
      this.h_offset = h_offset;
      this.v_offset = v_offset;
    }

    // Methods:

    public void render (Graphics g)
    {
      g.setFont (font);
      g.setColor (Color.white);
      g.drawRect (x, y, width, height);
      int x = this.x + this.h_offset;
      int y = this.y + this.v_offset;
      g.drawString (label, x, y);
    }

    // Setters:

    public void setPosX (int x)
    {
      this.x = x;
    }

    public void setPosY (int y)
    {
      this.y = y;
    }

    public void setWidth (int width)
    {
      this.width = width;
    }

    public void setHeight (int height)
    {
      this.height = height;
    }

    // Getters:

    public int getPosX ()
    {
      return x;
    }

    public int getPosY ()
    {
      return y;
    }

    public int getWidth ()
    {
      return width;
    }

    public int getHeight ()
    {
      return height;
    }

    public boolean isHovering (int x, int y)
      // returns true if x, y are both inside the button area
    {
      if (x > this.x && x < this.x + this.width)
      {
	if (y > this.y && y < this.y + this.height)
	  return true;
	else
	  return false;
      }
      else
      {
	return false;
      }
    }
  }


  // Menu Components:


  private Game game;		// for setting and getting states
  private Font font;		// we need this for rendering text
  private Button play;		// starts game
  private Button help;		// switches to the help screen
  private Button quit;		// quits game
  private Button back;		// returns to menu from help screen
  private Handler handler;	// for showing swarming enemies


  // defines the font name, style, and size
  private String fontname = ("Arial");
  private int fontstyle = 1;
  private int fontsize = 48;


  // defines button dimensions
  private int width = 256, height = 64;
  // defines the reference position (window center) of the buttons
  int x = (Game.WIDTH - width) / 2;
  int y = (Game.HEIGHT - height) / 2;
  // defines button, center-to-center, vertical spacing
  private int vspace = height + (height / 2);
  // defines the horizontal spacing of the button label
  private int hspace = (width / 4);
  // defines the vertical offset of the button label
  private int offset = (vspace / 2);


  // Constructors:


  public Menu (Game game, Handler handler)
  {
    // creates Font for graphics display

    this.font = new Font (fontname, fontstyle, fontsize);

    // creates play, help, quit, and back buttons

    this.play = new Button (font, "Play", x, y - vspace,
	width, height, hspace, offset);

    this.help = new Button (font, "Help", x, y,
	width, height, hspace, offset);

    this.quit = new Button (font, "Quit", x, y + vspace,
	width, height, hspace, offset);

    this.back = new Button (font, "Back", x, y + vspace,
	width, height, hspace, offset);

    this.game = game;
    this.handler = handler;
  }


  // Methods:


  public void mousePressed (MouseEvent e)
    // handles mouse events depending on the state: menu or help screen
  {
    int x = e.getX();
    int y = e.getY();

    switch (game.gameState)
    {
      case Menu:
	if ( play.isHovering(x, y) )
	  game.setGameState (State.Init);
	else if ( help.isHovering(x, y) )
	  game.setGameState (State.Help);
	else if ( quit.isHovering(x, y) )
	  game.quit ();
	break;

      case Help:
	if ( back.isHovering(x, y) )
	  game.setGameState (State.Menu);
    }
  }


  public void mouseReleased (MouseEvent e)
  {
    return;
  }


  public void tick ()
    // we might use this in the near future to display swarming enemies
  {
    return;
  }


  public void render (Graphics g)
    // renders the menu or help screen depending on the game state
  {
    switch (game.gameState)
    {
      case Menu:
	// renders game title
	g.setFont (font);
	g.setColor (Color.red);
	g.drawString  (	"Swarm", x + hspace / 2,
	    y - 3 * vspace / 2 );

	// renders menu buttons
	play.render (g);
	help.render (g);
	quit.render (g);
	break;

      case Help:
	// renders help title
	g.setFont (font);
	g.setColor (Color.white);
	g.drawString  (	"Help", x + hspace,
	    y - 3 * vspace / 2 );

	// renders help buttons
	back.render (g);
	break;
    }
  }
}


/*
 * COMMENTS:
 *
 *
 * Handler:
 * We need the handler because we might show swarming enemies in the menu
 * screen in the future.
 *
 *
 * Rendering Text:
 * Point to Pixel Proportion: 		48 points == 64 pixels
 *
 * Even though the font size has been chosen to be of the same height as
 * the button it the text does not exactly fit the button height as
 * expected but it looks good. It would be necessary to know more about
 * how graphics are produced later to fine tune this. For the time being
 * the current sizes are good enough.
 *
 */
