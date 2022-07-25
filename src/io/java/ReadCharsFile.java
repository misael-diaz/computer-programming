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
 * [0] Files: www.w3schools.com/java/java_files_create.asp
 * [1] PrintWriter: (docs.oracle.com/en/java/javase/11/docs/api/java.base/
 *                   java/io/PrintWriter.html)
 * [2] IOException: (docs.oracle.com/javase/7/docs/api/java/io/
 *                   IOException.html)
 * [3] FileNotFoundException: (docs.oracle.com/javase/7/docs/api/java/io/
 *                             FileNotFoundException.html)
 * [4] BufferedReader: www.javatpoint.com/java-bufferedreader-class
 * [5] FileReader: www.tutorialspoint.com/java/java_filereader_class.htm
 *
 */

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class ReadCharsFile
{
	public static void main (String [] args)
	// reads and prints the GPL on the console one character at a time
	{
		read ();
	}

	// implementation:
	private static void read ()
	// opens GPL file for reading one character at a time
	{
		String filename = ("GPL.txt");
		File f = new File (filename);
		try
		// the compiler complains if not in a try - catch structure
		{
			// we need a buffer reader to read char-by-char
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			// reads the buffer one character at a time
			read (br);
		}
		catch (FileNotFoundException e)
		{
			// prints the stack trace for troubleshooting
			e.printStackTrace();
		}

	}


	private static void read (BufferedReader br)
	/*

	Synopsis:
	Reads buffer one character at a time and reports on the console the
	number of capital letters found.

	*/
	{
		// reads the first character in the file
		int c = readBuffer (br);
		while (c != 0xFFFFFFFF)	/* (Note: 0xFFFFFFFF = -1) */
		// reads one character at a time until end-of-file EOF
		{
			// prints the read character on the console
			System.out.printf("%c", c);
			// reads the next character
			c = readBuffer (br);
		}
	}


	private static int readBuffer (BufferedReader br)
	// tries to read a single character from buffer stream
	{
		int c = 0;
		// the compiler complains if not in a try - catch structure
		try { c = br.read(); }
		catch (IOException e) { e.printStackTrace(); }
		return c;
	}
}
