/*
 * Algorithms and Programming II                             April 25, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Performs rutinary input output operations in Java.
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
 * [5] Random: docs.oracle.com/javase/8/docs/api/java/util/Random.html
 * [6] www.javatpoint.com/throw-keyword
 * [7] www.javatpoint.com/post/java-random
 *
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Files
{
	public static void main (String[] args)
	{
		create();	// creates a file
		write ();	// writes random numbers to the file
		read  ();	// reads random numbers in the file
		readIntoArray();// reads random numbers into array
		return;
	}

	private static void create ()
	// creates a file with given name `filename'
	{
		try
		{
			// defines the filename
			String fname = "random.txt";
			// creates a new File object
			File f = new File (fname);

			// creates the new file
			String msg = "creating file `" + fname + "' ... ";
			System.out.println();
			System.out.printf("%s", msg);
			f.createNewFile();
			System.out.println("done");

		}
		catch (IOException err)
		{
			// complains if there is an Input/Output Error
			System.out.println("IO Error:");
			err.printStackTrace();
		}

		return;
	}


	private static void write ()
	// writes to the file
	{
		try
		{
			// defines the filename
			String filename = "random.txt";
			// creates new PrintWriter object for writing file
			PrintWriter out = new PrintWriter (filename);

			int numel = 16;
			Random rand = new Random (numel);
			String msg = "writing %d random numbers ... ";
			System.out.printf(msg, numel);
			// writes random integers in the range [0, 256)
			for (int i = 0; i != numel; ++i)
				out.printf("%d\n", rand.nextInt(256) );

			System.out.println("done");

			System.out.printf("closing file ... ");
			out.close();	// closes the file
			System.out.println("done");
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			System.out.println("IO Error:");
			err.printStackTrace();
		}

		return;
	}


	private static void read ()
	// reads the file contents and prints them to the console
	{
		// defines the filename
		String filename = "random.txt";
		// creates a File object
		File f = new File (filename);
		try
		{
			// attempts to create a Scanner object
			Scanner in = new Scanner (f);

			System.out.printf("\nrandom numbers in file:\n");

			int r;
			int count = 0;
			// reads integers in file until EOF
			while ( in.hasNextInt() )
			{
				// reads random number and prints it
				r = in.nextInt();
				System.out.printf("%2d %4d\n", count, r);
				++count;
			}

			String msg = "%d random numbers have been read\n";
			System.out.printf(msg, count);

		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			System.out.println("IO Error:");
			err.printStackTrace();
		}


		return;
	}


	private static void readIntoArray ()
	// reads the file contents into an array and prints the array
	{
		String filename = "random.txt";
		File f = new File (filename);

		try
		{
			Scanner in = new Scanner (f);

			// allocates for storing the random numbers in file
			int size = lines (filename);
			int [] rand = new int [size];

			int count = 0;
			// reads random numbers into array
			while ( in.hasNextInt() )
			{
				rand[count] = in.nextInt();
				++count;
			}

			System.out.printf("\nrandom numbers in array:\n");
			for (int i = 0; i != size; ++i)
				System.out.printf("%2d %4d\n", i, rand[i]);

			String msg = "array stores %d random numbers\n";
			System.out.printf(msg, count);

		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			System.out.println("IO Error:");
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
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			System.out.println("IO Error:");
			err.printStackTrace();
		}

		return count;
	}
}

/*
 * COMMENTS:
 * Reading:
 * Reads integers until the scanner object finds something that it is not
 * an integer, such as a String or an End-Of-File EOF for instance.
 *
 */
