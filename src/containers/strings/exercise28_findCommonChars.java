/*

source: exercise28_findCommonChars.java
author: @misael-diaz
date:   2023-02-08

Synopsis:
Solves exercise-28 of the make-it-real codecamp.
Reports to users the number of common characters in their input phrases.


Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] readline from Scanner: https://www.w3schools.com/java/java_user_input.asp
[1] String:charAt(): https://www.geeksforgeeks.org/java-string-charat-method-example
[2] comparing characters: https://www.delftstack.com/howto/java/java-char-equals-in-java
[3] Arrays: docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/ArrayList.html
[4] partition sets: JJ McConnell, Analysis of Algorithms, second edition

*/

import java.util.Scanner;
import java.util.ArrayList;

public class exercise28_findCommonChars
{
  public static void main (String [] args)
  {
    // creates readline `rl' object for storing a line of input into a string
    Scanner rl = new Scanner(System.in);

    // prompts users for the first phrase and stores it in a string
    System.out.print("input a phrase> ");
    String msg1 = rl.nextLine();

    // prompts users for the second phrase and stores it in another string
    System.out.print("input another phrase> ");
    String msg2 = rl.nextLine();

    // displays on the console the common characters
    findCommonChars(msg1, msg2);
  }


  // implementations:


  // displays on the console the common characters in strings `msg1' and `msg2'
  private static void findCommonChars (String msg1, String msg2)
  {

    // pre-processing:


    // removes whitespace and duplicate characters from the messages
    String str1 = distinct( removeWhitespace(msg1) );
    String str2 = distinct( removeWhitespace(msg2) );


    ArrayList<Character> commonChars = new ArrayList<>();
    // appends common characters into the list of common characters
    for (int i = 0; i != str2.length(); ++i)
    {
      if ( contains( str1, str2.charAt(i) ) )
      {
	commonChars.add( str2.charAt(i) );
      }
    }


    int count = 0;
    System.out.print("common: ");
    // displays the common characters on the console, otherwise displays none:
    for (char c : commonChars)
    {
      if (count == 0)	// accounts for a single common character scenario
      {
	System.out.printf("%s", c);
      }
      else
      {
	System.out.printf("%s", ", " + c);
      }

      ++count;
    }


    if (commonChars.size() != 0)
    {
      System.out.println();
    }
    else
    {
      System.out.print("none\n");
    }
  }


  // boolean contains (String str, char target)
  //
  // Synopsis:
  // Returns true if the target character `target' is in the string `str', returns
  // false otherwise.


  private static boolean contains (String str, char target)
  {
    int pos = search(str, target);
    if (pos < 0)
      return false;
    else
      return true;
  }


  // int search (String str, char target)
  //
  // Synopsis:
  // Searches linearly for the target character `target' in the string `str' and
  // returns its positional index if it is present, otherwise returns an invalid index.


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
  private static String distinct (String msg)
  {
    int [] part = new int[msg.length()];
    for (int i = 0; i != msg.length(); ++i)
    {
      part[i] = -1;	// initializes the partition array to find duplicates
    }


    // uses brute force to find the distinct characters in the message
    for (int i = 0; i != (msg.length() - 1); ++i)
    {
      for (int j = (i + 1); j != msg.length(); ++j)
      {
	// if not marked as duplicate and if repeated, then:
	if ( (part[j] < 0) && ( msg.charAt(i) == msg.charAt(j) ) )
	{
	  part[j] = i;	// sets the ith char as the parent of the jth char
	  --part[i];	// increments number of ith character duplicates
	}
      }
    }

    String str = new String();
    // concatenates distinct characters in message `msg' into new string `str'
    for (int i = 0; i != msg.length(); ++i)
    {
      if (part[i] < 0)
      {
	str += msg.charAt(i);
      }
    }

    return str;
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
