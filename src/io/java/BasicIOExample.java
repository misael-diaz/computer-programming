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
 * [0] Files: www.w3schools.com/java/java_files_create.asp
 * [1] PrintWriter: (docs.oracle.com/en/java/javase/11/docs/api/java.base/
 *                   java/io/PrintWriter.html)
 * [2] IOException: (docs.oracle.com/javase/7/docs/api/java/io/
 *                   IOException.html)
 * [3] FileNotFoundException: (docs.oracle.com/javase/7/docs/api/java/io/
 *                             FileNotFoundException.html)
 * [4] Scanner: docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
 * [5] www.javatpoint.com/throw-keyword
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
		create();	// creates a file
		write();	// writes data to the file
		read();		// reads data in the file
		store();	// stores data in an array
		return;
	}

	// implementations:
	private static void create ()
	// creates a file
	{
		try
		{
			// defines the filename
			String fname = ("data.txt");
			// creates a new File object
			File f = new File (fname);

			String msg = "creating file `" + fname + "' ... ";
			System.out.println();
			System.out.printf("%s", msg);
			// creates the new file
			f.createNewFile();
			System.out.println("done");

		}
		catch (IOException err)
		{
			// complains if there is an Input/Output Error
			err.printStackTrace();
		}

		return;
	}


	private static void write ()
	// writes data to a file
	{
		try
		{
			// defines the filename
			String filename = ("data.txt");
			// creates new PrintWriter object for writing file
			PrintWriter out = new PrintWriter (filename);

			int numel = 256;
			String msg = "writing %d numbers ... ";
			System.out.printf(msg, numel);
			// writes the integers in the range [0, 256)
			for (int i = 0; i != numel; ++i)
				out.printf("%d\n", i);

			System.out.println("done");

			System.out.printf("closing file ... ");
			out.close();	// closes the output stream
			System.out.println("done");
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}

		return;
	}


	private static void read ()
	// reads the file contents and prints them to the console
	{
		// defines the filename
		String filename = ("data.txt");
		// creates a File object
		File f = new File (filename);
		try
		{
			// attempts to create a Scanner object
			Scanner in = new Scanner (f);

			System.out.printf("\nnumbers in file:\n");

			int x;
			int count = 0;
			// reads integers in file until EOF
			while ( in.hasNextInt() )
			{
				// reads  number and prints it
				x = in.nextInt();
				System.out.printf("%4d\n", x);
				++count;
			}

			String msg = ("%d numbers have been read\n");
			System.out.printf(msg, count);

			in.close();	// closes the input stream

		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}


		return;
	}


	private static void store ()
	// stores the file contents into an array and prints the array
	{
		String filename = "data.txt";
		File f = new File (filename);

		try
		{
			Scanner in = new Scanner (f);

			// allocates list for storing the numbers in file
			int size = lines (filename);
			int [] list = new int [size];

			int count = 0;
			// reads numbers into array
			while ( in.hasNextInt() )
			{
				list[count] = in.nextInt();
				++count;
			}

			System.out.printf("\nnumbers in array:\n");
			for (int i = 0; i != size; ++i)
				System.out.printf("%4d\n", list[i]);

			String msg = ("array stores %d numbers\n");
			System.out.printf(msg, count);

			in.close();	// closes the input stream

		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}


		return;
	}


	private static int lines (String filename)
	// counts number of lines (or records) in a file
	{

		int count = 0;
		// creates a File object
		File f = new File (filename);
		try
		{
			// attempts to create a Scanner object
			Scanner in = new Scanner (f);

			// reads integers in file until EOF for counting
			while ( in.hasNextInt() )
			{
				in.nextInt();
				++count;
			}

			in.close();	// closes the input stream
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}

		return count;
	}
}

/*
 * COMMENTS:
 *
 * Reading Data:
 * Reads integers until the scanner object finds something that it is not
 * an integer, such as a String or an End-Of-File EOF for instance.
 *
 * Static methods:
 * Static methods are not bound to any object of the class and these can
 * be conveniently invoked directly from the main program (as done here).
 *
 * Variables:
 * Note that local variables are destroyed (freed from memory) after the
 * method executes. Do not use global variables unless you really have to.
 *
 */
