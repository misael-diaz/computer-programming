/*
 * Algorithms and Programming II                               May 01, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Recursive implementation of the Binary Search Algorithm.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition
 *
 */


public class BinarySearchAlgorithm
{

  public static void main (String [] args)
  {
    int target = 16;			// sought element or target
    int [] list = sorted();		// sorted list
    int begin = 1, end = list.length;	// begin and end positional indexes


    // obtains the positional index of the target (do not confuse with the array index)
    int pos = BinarySearch(begin, end, list, target);


    System.out.printf("\nBinary Search:\n");
    // reports the position of the target on the console
    if (pos != 0)
      System.out.printf("target found in position %d\n\n", pos);
    else
      System.out.printf("target is not in the list\n\n");

    test();
  }


  // interfaces:


  // int BinarySearch (int b, int e, int [] list, int t)
  //
  // Synopsis:
  // Recursive implementation of the Binary Search Algorithm.
  // Searches until a match is found or until there are no more
  // elements left to match (a list of zero elements).
  //
  // Inputs
  // b		beginning positional index of the list
  // e		end positional index of the list
  // list	list of (presumed) sorted integers
  // t		target
  //
  // Output
  // pos	positional index [1, NUMEL], otherwise zero


  public static int BinarySearch (int b, int e, int [] list, int t)
  {
    is_sorted(list);				// complains if the list is not sorted
    check_bounds(b, e, list);			// complains if [b, e) are invalid


    int m = (b + e) / 2;			// gets middle position of the list
    int mid = list[m - 1];			// addresses the middle element


    if (b > e)					// handles empty list (target not found)
      return 0;


    if (t == mid)				// uses direct solution if found
    {
      return m;
    }
    else					// divides:
    {
      if (t > mid)				// discards first half
      {
	b = (m + 1);
      }
      else					// discards second half
      {
	e = (m - 1);
      }
    }

    return BinarySearch(b, e, list, t);		// recurses
  }


  // implementations:


  private static int [] sorted () // returns a sorted list of numbers in the range [1, 16]
  {
    int [] list = new int [16];
    for (int i = 0; i != 16; ++i)
      list[i] = (i+1);

    return list;
  }


  private static void is_sorted (int [] list)	// complains if the list is not sorted
  {
    int sgn;					// sign
    int sgn0 = (list[1] - list[0]);		// initial sign
    for (int i = 2; i != list.length; ++i)	// complains if there is a sign change
    {
      sgn = (list[i] - list[i - 1]);

      if (sgn0 * sgn < 0)
      {
	throw new IllegalArgumentException("BinarySearch(): expects a sorted list");
      }
    }
  }


  // complains if positional indexes are invalid, expects [1, NUMEL]
  private static void check_bounds (int b, int e, int [] list)
  {
    if (b <= 0)
    {
      throw new IllegalArgumentException("BinarySearch(): expects a valid `begin` index");
    }

    if (e > list.length)
    {
      throw new IllegalArgumentException("BinarySearch(): expects a valid `end` index");
    }
  }


  private static void test ()
  {
    int fails = 0;
    int size = 0x00001000;
    int [] list = new int [size];
    generate(list);
    for (int i = 0; i != list.length; ++i)
    {
      int pos = BinarySearch(1, list.length, list, list[i]) - 1;
      if (i != pos)
      {
	++fails;
      }
    }

    System.out.print("test::BinarySearch(): ");
    if (fails != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }
  }

  private static void generate (int [] list)
  {
    for (int i = 0; i != list.length; ++i)
    {
      list[i] = (2 * i + 1);
    }
  }
}
