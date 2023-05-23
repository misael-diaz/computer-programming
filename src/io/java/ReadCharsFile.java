/*
 * Algorithms and Complexity                                  July 20, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Reads the GNU General Public License GPL one character at a time.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] https://www.w3schools.com/java/java_files_create.asp
 * [1] https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/PrintWriter.html
 * [2] https://docs.oracle.com/javase/7/docs/api/java/io/IOException.html
 * [3] https://docs.oracle.com/javase/7/docs/api/java/io/FileNotFoundException.html
 * [4] https://www.javatpoint.com/java-bufferedreader-class
 * [5] https://www.tutorialspoint.com/java/java_filereader_class.htm
 *
 */

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class ReadCharsFile
{
  // reads and prints the GPL on the console one character at a time
  public static void main (String [] args)
  {
    read ();
  }


  // implementation:


  // opens GPL file for reading one character at a time
  private static void read ()
  {
    String filename = ("GPL.txt");
    File f = new File (filename);
    try	// the compiler complains if this codeblock is not in a try - catch structure
    {
      FileReader fr = new FileReader (f);		// for reading file
      BufferedReader br = new BufferedReader (fr);	// for reading char-by-char
      read (br);					// reads chars in buffer
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }

  }


  // Reads the buffer one character at a time and reports the number of capital letters
  private static void read (BufferedReader br)
  {
    int c = readBuffer (br);	// reads the first character in the file
    while (c != 0xFFFFFFFF)	// reads one character at a time until end-of-file EOF
    {
      System.out.printf("%c", c);
      c = readBuffer (br);	// reads the next character
    }
  }


  // tries to read a single character from buffer stream
  private static int readBuffer (BufferedReader br)
  {
    int c = 0;
    // the compiler complains if codeblock is not in a try - catch structure
    try
    {
      c = br.read();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return c;
  }
}
