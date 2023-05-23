/*

source: exercise39_numCharInstances.java
author: @misael-diaz
date:   2023-02-08

Synopsis:
Solves exercise-39 of the make-it-real codecamp.
Reports to users the frequency of characters in a string.


Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] readline from Scanner: https://www.w3schools.com/java/java_user_input.asp
[1] String:charAt(): https://www.geeksforgeeks.org/java-string-charat-method-example
[2] comparing characters: https://www.delftstack.com/howto/java/java-char-equals-in-java
[3] partition sets: JJ McConnell, Analysis of Algorithms, second edition

*/

import java.util.Scanner;

public class exercise39_numCharInstances
{
  public static void main (String [] args)
  {
    // creates readline `rl' object for storing a line of input into a string
    Scanner rl = new Scanner(System.in);

    // prompts users for a message and stores it in a string
    System.out.print("input a message> ");
    String msg = rl.nextLine();

    // displays on the console the frequency of each character in the message
    frequency(msg);
  }


  // implementations:


  // displays on the console the number of character instances in the message `msg'
  private static void frequency (String msg)
  {

    // pre-processing:


    // removes whitespace from message `msg'
    String str = removeWhitespace(msg);


    // obtains the frequency of characters in the string `str'
    int [] freq = distinct(str);


    // post-processing:


    // loop-invariant:
    // we have reported on the console the number of instances of `count'
    // different characters present in the string `str'


    int count = 0;
    // displays on the console the frequency of characters in the message
    for (int i = 0; i != str.length(); ++i)
    {
      // checks the partition array `freq' for the number of instances
      // of the character at the ith position in the string
      if (freq[i] < 0)
      {
	if (count == 0)	// checks if displaying on the console for the first time
	{
	  String fstr = "%c: %d";				// format string
	  System.out.printf(fstr, str.charAt(i), -freq[i]);
	  ++count;
	}
	else		// if not the first time, insert the delimiter `, '
	{
	  String dlim = ", ";					// delimiter
	  String elem = "%c: %d";				// element
	  String fstr = dlim + elem;				// format string
	  System.out.printf(fstr, str.charAt(i), -freq[i]);
	  ++count;
	}
      }
    }

    System.out.println();
  }


  // boolean contains (String str, char target)
  //
  // Synopsis:
  // Returns true if the target character `target' is in the string `str', returns
  // false otherwise.


  private static boolean contains (String str, char target)
  {
    int pos = search(str, target);
    return ( (pos < 0)? false : true );
  }


  // int search (String str, char target)
  //
  // Synopsis:
  // Searches linearly for the target character `target' in the string `str' and
  // returns its positional index if its present, otherwise returns an invalid index.


  private static int search (String str, char target)
  {
    int pos = -1;
    for (int i = 0; i != str.length(); ++i)
    {
      if (str.charAt(i) == target)
      {
	pos = i;
	return pos;
      }
    }

    return pos;
  }


  // returns a string containing the distinct characters in `msg' string
  private static int [] distinct (String msg)
  {

    int [] part = new int[msg.length()];
    for (int i = 0; i != msg.length(); ++i)
    {
      part[i] = -1;	// initializes the partition array for finding duplicates
    }


    // uses brute force to find the distinct characters in the message
    for (int i = 0; i != (msg.length() - 1); ++i)
    {
      for (int j = (i + 1); j != msg.length(); ++j)
      {
	// if not marked as duplicate and if repeated, then:
	if ( (part[j] < 0) && ( msg.charAt(i) == msg.charAt(j) ) )
	{
	  part[j] = i;		// sets the ith char as the parent of the jth char
	  --part[i];		// increments number of ith character duplicates
	}
      }
    }

    return part;
  }


  // returns a copy of the string `msg' without whitespace ' '
  private static String removeWhitespace (String msg)
  {
    String str = new String();
    for (int i = 0; i != msg.length(); ++i)
    {
      if (msg.charAt(i) != ' ')
      {
	str += msg.charAt(i);
      }
    }

    return str;
  }
}
