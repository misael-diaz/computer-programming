/*
 * Algorithms and Complexity                                August 22, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Performs rutinary Input/Output IO operations in Java.
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
 * [4] https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
 * [5] https://www.javatpoint.com/throw-keyword
 *
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

class BasicIOExample
{
  public static void main (String[] args)
  {
    create();						// creates a file
    write();						// writes data to the file
    read();						// reads data in the file
    store();						// stores data in an array
  }


  // implementations:


  private static void create ()				// creates a file
  {
    try
    {
      String fname = ("data.txt");			// defines the filename
      File f = new File (fname);			// creates a new File object

      String msg = "creating file `" + fname + "' ... ";
      System.out.println();
      System.out.printf("%s", msg);
      f.createNewFile();				// creates the new file
      System.out.println("done");
    }
    catch (IOException err)				// catches Input/Output Error
    {
      err.printStackTrace();
    }
  }


  private static void write ()				// writes data to a file
  {
    try
    {
      String filename = ("data.txt");			// defines the filename
      PrintWriter out = new PrintWriter (filename);	// creates PrintWriter for writing

      int numel = 256;
      String msg = "writing %d numbers ... ";
      System.out.printf(msg, numel);
      for (int i = 0; i != numel; ++i)
	out.printf("%d\n", i);				// writes integers in [0, 256)

      System.out.println("done");

      System.out.printf("closing file ... ");
      out.close();					// closes the output stream
      System.out.println("done");
    }
    catch (FileNotFoundException err)			// catches if file does not exist
    {
      err.printStackTrace();
    }
  }


  private static void read ()				// reads file
  {
    String filename = ("data.txt");			// defines the filename
    File f = new File (filename);			// creates a File object
    try
    {
      Scanner in = new Scanner (f);			// creates a Scanner object

      System.out.printf("\nnumbers in file:\n");

      int x;
      int count = 0;
      // reads integers in file until End-Of-File EOF:
      while ( in.hasNextInt() )
      {
	x = in.nextInt();				// reads
	System.out.printf("%4d\n", x);			// prints
	++count;
      }

      String msg = ("%d numbers have been read\n");
      System.out.printf(msg, count);

      in.close();					// closes the input stream
    }
    catch (FileNotFoundException err)
    {
      err.printStackTrace();
    }
  }


  // stores the file contents into an array and prints the array
  private static void store ()
  {
    String filename = "data.txt";
    File f = new File (filename);

    try
    {
      Scanner in = new Scanner (f);

      int size = lines (filename);
      int [] list = new int [size];	// allocates list for storing the numbers in file

      int count = 0;
      while ( in.hasNextInt() )
      {
	list[count] = in.nextInt();	// reads numbers into array
	++count;
      }

      System.out.printf("\nnumbers in array:\n");
      for (int i = 0; i != size; ++i)
	System.out.printf("%4d\n", list[i]);

      String msg = ("array stores %d numbers\n");
      System.out.printf(msg, count);

      in.close();			// closes the input stream

    }
    catch (FileNotFoundException err)
    {
      err.printStackTrace();
    }
  }


  // counts number of lines (or records) in a file
  private static int lines (String filename)
  {
    int count = 0;
    File f = new File (filename);
    try
    {
      Scanner in = new Scanner (f);

      while ( in.hasNextInt() )		// reads integers in file until EOF
      {
	in.nextInt();
	++count;
      }

      in.close();			// closes the input stream
    }
    catch (FileNotFoundException err)
    {
      err.printStackTrace();
    }

    return count;
  }
}


// COMMENTS:
//
// Reading Data:
// Reads integers until the scanner object finds something that it is not
// an integer, such as a String or an End-Of-File EOF for instance.
//
// Static methods:
// Static methods are not bound to any object of the class and these can
// be conveniently invoked directly from the main program (as done here).
//
// Variables:
// Note that local variables are destroyed (freed from memory) after the
// method executes. Do not use global variables unless you really have to.
