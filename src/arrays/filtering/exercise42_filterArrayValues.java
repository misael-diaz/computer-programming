/*

From Make-it-Real Codecamp				       February 09, 2023

source: exercise42_filterArrayValues.java
author: @misael-diaz

Synopsis:
Solves exercise-42 of the make-it-real codecamp to illustrate different array filtering
techniques in Java.


Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
at your option) any later version.


References:
[0] filter: https://stackoverflow.com/questions/24112715/java-8-filter-array-using-lambda

*/

import java.util.Random;	// for generating pseudo-random numbers
import java.util.ArrayList;	// for storing numbers dynamically
import java.util.Arrays;	// for filtering numbers

public class exercise42_filterArrayValues
{
  // filters out odd (or prunes) odd numbers from an array of pseudo-random numbers
  public static void main (String[] args)
  {
    int n = 16;			// sets the size of the array `numbers'
    int [] numbers = create(n);	// gets `n' random numbers into array
    example1(numbers);		// uses core language features
    example2(numbers);		// uses the ArrayList utility
    example3(numbers);		// uses the Arrays::stream::filter utility
  }


  // possible implementation via core language features
  private static void example1 (int [] numbers)
  {
    // finds the number of even values contained in the array `numbers'
    int numEvenValues = numEvenValues(numbers);

    // caters odd-valued arrays (to avert memory allocation errors)
    if (numEvenValues != 0)
    {
      int count = 0;
      // Note: it is invalid to allocate an array of zero elements
      int [] evenValues = new int [numEvenValues];
      // copies the even values into the array temporary `evenValues'
      for (int i = 0; i != numbers.length; ++i)
      {
	int number = numbers[i];
	if (number % 2 == 0)
	{
	  evenValues[count] = number;
	  ++count;
	}
      }


      // post-processing:


      System.out.print("even: ");
      // displays on the console the even values
      for (int i = 0; i != count; ++i)
      {
	int evenValue = evenValues[i];
	if (i == 0)
	{
	  System.out.printf("%d", evenValue);
	}
	else
	{
	  System.out.printf(", %d", evenValue);
	}
      }
      System.out.println();
    }
    else
    {
      // otherwise, informs the user that the passed array has no even values
      System.out.println("even: none");
    }
  }


  // uses an ArrayList to insert dynamically the even values in `numbers'
  private static void example2 (int [] numbers)
  {

    // creates an empty ArrayList for storing the even values in `numbers'
    ArrayList<Integer> evenValues = new ArrayList<>();

    // inserts dynamically the even values into the ArrayList `evenValues'
    for (int i = 0; i != numbers.length; ++i)
    {
      int number = numbers[i];
      if (number % 2 == 0)
      {
	evenValues.add(number);
      }
    }


    // post-processing:


    // gets the number of even values from the size of the ArrayList
    int count = evenValues.size();
    if (count != 0)
    {
      System.out.print("even: ");
      // displays on the console the even values
      for (int i = 0; i != count; ++i)
      {
	// gets the ith element in the ArrayList `evenValues'
	int evenValue = evenValues.get(i);
	if (i == 0)
	{
	  System.out.printf("%d", evenValue);
	}
	else
	{
	  System.out.printf(", %d", evenValue);
	}
      }
      System.out.println();
    }
    else
    {
      // otherwise, informs the user that the passed array has no even values
      System.out.println("even: none");
    }
  }


  // prunes the odd values from the array `nums' via Arrays::stream::filter()
  private static void example3 (int [] nums)
  {

    // filters the stream according to the predicate lambda function (ref [0])
    int [] even = Arrays.stream(nums).filter( e -> (e % 2 == 0) ).toArray();


    // post-processing:


    // gets the number of even values from the length of the array `even'
    int count = even.length;
    if (count != 0)
    {
      System.out.print("even: ");
      // displays on the console the even values
      for (int i = 0; i != count; ++i)
      {
	int e = even[i];
	if (i == 0)
	{
	  System.out.printf("%d", e);
	}
	else
	{
	  System.out.printf(", %d", e);
	}
      }
      System.out.println();
    }
    else
    {
      // otherwise, informs the user that the passed array has no even values
      System.out.println("even: none");
    }
  }


  // counts the number of even values in the array
  private static int numEvenValues (int [] numbers)
  {
    int count = 0;
    // invariant: we have counted `count' even elements in the array
    for (int i = 0; i != numbers.length; ++i)
    {
      int number = numbers[i];
      if (number % 2 == 0)
	count += 1;
      else
	count += 0;
    }

    return count;
  }


  private static int [] create (int numel)
  {
    // returns an array of pseudo-random numbers of `numel' elements with values
    // in the asymmetric range [0, numel)
    Random r = new Random();
    int [] numbers = new int[numel];
    for (int i = 0; i != numel; ++i)
    {
      numbers[i] = r.nextInt(numel);
    }

    return numbers;
  }
}
