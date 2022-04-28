/*
 * Algorithms and Programming II                             April 27, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Uses the vector class to develop a grading program that reports the
 * median grade for each student. A vector is used to store the homework
 * grades efficiently. Note that each student has completed a different
 * number of homeworks (see that the roster file has a non-uniform number
 * of columns).
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
 *
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Grade
{
	public static void main (String [] args)
	{
		roster();// creates roster file
		grades();// writes random grades to roster file
		median();// reports the median grade for each student
		return;
	}


	private static void roster ()
	// creates a plain text file of homework grades for a course
	{
		try
		{
			String name = "roster.txt";
			File f = new File(name);
			f.createNewFile();
		}
		catch (IOException err)
		{
			System.out.println("IO Error");
			err.printStackTrace();
		}
	}


	private static void grades ()
	// writes a random number of homework grades for each student
	{
		try
		{

			String file = "roster.txt";
			PrintWriter out = new PrintWriter(file);

			int students = 40;
			String student = "student";
			String name;


			int g;		// grade
			int min = 4;	// min number of homeworks done
			int max = 8;	// max number of homeworks done
			int hws;	// number of homeworks done
			int homework;	// homework score
			Random homeworks = new Random();
			Random grade = new Random();


			// writes homework grades for each student
			for (int i = 0; i != students; ++i)
			{
				name = String.format("%s%02d", student, i);
				out.printf("%s", name);

				hws = min + homeworks.nextInt(max - min);
				for (int j = 0; j != hws; ++j)
				{
					g = 50 + grade.nextInt(45);
					out.printf("%4d", g);
				}
				out.println();
			}

			out.close();

		}
		catch (FileNotFoundException err)
		{
			System.out.println("IO Error");
			err.printStackTrace();
		}
	}


	private static void median()
	// reports the median of the homeworks for each student
	{
		try
		{
			String file = "roster.txt";
			File f = new File(file);
			Scanner in = new Scanner (f);
			Vector homeworks = new Vector();

			String student;
			int grade;
			int missed;

			// reads until the end-of-file EOF
			while ( in.hasNext() )
			{
				// reads the student name
				student = in.next();
				// clears vector for next student
				homeworks.clear (homeworks);
				// reads integers on the line one by one
				while ( in.hasNextInt() )
				{
					// inserts homework grades
					homeworks.push_back(homeworks,
							    in.nextInt());
				}

				// inserts score for unsubmitted homework
				missed = 7 - homeworks.size(homeworks);
				for (int i = 0; i != missed; ++i)
					homeworks.push_back(homeworks, 0);

				// obtains the median homework grade
				grade = homeworks.median(homeworks);
				// prints student name and grade
				System.out.println(student + "  " + grade);
			}

		}
		catch (FileNotFoundException err)
		{
			System.out.println("IO Error");
			err.printStackTrace();
		}
	}
}
