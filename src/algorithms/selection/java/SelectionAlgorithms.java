/*
 * Algorithms and Programming II                          April 05, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of selection algorithms in Java.
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

import java.util.Random;

public class SelectionAlgorithms
{

  public static void main (String[] args)
  {
    FindsKthLargest();		// uses the implementation
    testFindKthLargest();	// validates the implementation
    test();
  }


  // examples:


  public static void FindsKthLargest()
  {
    int list [] = ulist();			// unsorted list
    int sorted [] = new int [list.length];	// sorted list

    // sorts original list
    for (int k = 0; k != list.length; ++k)
      sorted[k] = FindKthLargest(k + 1, list);


    // prints the original list
    System.out.printf("\n\nOriginal List:\n\n");
    for (int i = 0; i != list.length; ++i)
      System.out.printf("%4d", list[i]);
    System.out.printf("\n\n");


    // prints the sorted list
    System.out.printf("\n\nSorted List:\n\n");
    for (int i = 0; i != sorted.length; ++i)
      System.out.printf("%4d", sorted[i]);
    System.out.printf("\n\n");

  }


  private static int [] ulist () // returns a list of unsorted integers.
  {
    int x[] = {16,  7, 10,  1, 5, 11,  3,  8, 14,  4,  2, 12, 6, 13,  9, 15};
    return x;
  }


  private static int [] descending() // returns a descending list of integers.
  {
    int x[] = new int [16];
    for (int i = 0; i != 16; ++i)
      x[i] = (16 - i);

    return x;
  }


  // interfaces:


  // int FindKthLargest(int K, int [] list)
  //
  // Synopsis:
  // Possible implementation of the FindKthLargest Method.
  //
  // Inputs
  // K			integer indicating 1st, 2nd, 3rd, or kth largest
  // list		array of (presumed) unsorted integers
  //
  // Output
  // KthLargest	the kth largest element in the list


  public static int FindKthLargest(int K, int [] list)
  {
    int max = list[0];			// caters init warning
    int N = list.length;		// gets the list size
    int ls [] = copy (list);		// creates a shallow copy
    for (int i = 0; i != K; ++i)
    {
      max = swap(N, ls, argmax(N, ls));	// swaps (current) max with last (current) element
      --N;				// decrements size to exclude max on the next pass
    }

    return max; 			// returns the kth largest: ls[list.length - K]
  }


  // implementations:


  private static int [] copy (int [] list) // Creates a shallow copy of a list.
  {
    int ls [] = new int [list.length];
    for (int i = 0; i != list.length; ++i)
      ls[i] = list[i];

    return ls;
  }


  // int argmax (int N, int [] list)
  //
  // Synopsis:
  // Possible implementation of argmax function in Java.
  // Returns the index of the element of the list having the maximum
  // value.
  //
  // Input
  // N		number of elements in list
  // list		array of integers
  //
  // Output
  // idx		index of the element having the maximum value


  private static int argmax (int N, int [] list)
  {
    int idx = 0;
    int max = list[idx];
    for (int i = 1; i != N; ++i)
    {
      if (list[i] > max)
      {
	idx = i;
	max = list[idx];
      }
    }

    return idx;
  }


  // int swap (int N, int [] ls, int i)
  //
  // Synopsis:
  // Swaps (or interchanges) the ith element with the last one.
  //
  // Input
  // N			list size
  // ls			list, array of integers
  // i			the index of the ith element of the list
  //
  // Output
  // list		the list after the interchange of elements


  private static int swap (int N, int [] ls, int i)
  {
    int e = N - 1;		// gets the index of the end value
    int end = ls[e];		// creates placeholder for the end value
    int ith = ls[i];		// creates placeholder for the ith value
    ls[e] = ls[i];		// writes the ith value at the end of list
    ls[i] = end;		// writes the end value on the ith position
    return ith;
  }


  private static void consistency (int [] x, int [] y)
  {
    if (x.length != y.length)
    {
      throw new RuntimeException("lists must have the same length");
    }
  }


  // Returns true if the lists are equal, false otherwise.
  private static boolean equality (int [] x, int [] y)
  {
    consistency(x, y);			// complains if the lists have different lengths

    int diff = 0;
    for (int i = 0; i != x.length; ++i)	// checks for differences elementwise
      diff += (x[i] - y[i]);

    return ( (diff == 0)? true : false );
  }


  // tests:


  // void testFindKthLargest()
  //
  // Synopsis:
  // Tests the implementation of FindKthLargest().
  // The test uses FindKthLargest() to sort a list of integers in
  // descending order and compares it against the expected list.
  // The implementation passes the test if the lists are identical.


  private static void testFindKthLargest()
  {
    int list []   = ulist();				// unsorted list
    int sorted [] = new int [list.length];		// sorted list
    int arange [] = descending ();			// expected list

    for (int k = 0; k != list.length; ++k)		// sorts list in descending order
      sorted[k] = FindKthLargest(k + 1, list);

    System.out.printf("\ntest::FindKthLargest(): ");
    if ( equality(sorted, arange) )			// tests the implementation
      System.out.println("pass");
    else
      System.out.println("FAIL");
  }


  private static int search (int b, int e, int [] list, int t)	// linear search
  {
    int pos = 0x11111111;
    for (int i = b; i != e; ++i)
    {
      if (list[i] == t)
      {
	pos = i;
	return pos;
      }
    }
    return pos;
  }


  private static void generate (int [] list)	// generates distinct random numbers
  {
    Random r = new Random();
    int min = 0, max = 4 * list.length;
    for (int i = 0; i != list.length; ++i)
    {
      int elem = min + r.nextInt(max - min);
      while (search(0, i, list, elem) != 0x11111111)
      {
	elem = min + r.nextInt(max - min);
      }
      list[i] = elem;
    }
  }


  private static int [] sort (int [] list)
  {
    int [] sorted = new int [list.length];
    for (int k = 0; k != list.length; ++k)
    {
      sorted[k] = FindKthLargest(k + 1, list);
    }
    return sorted;
  }


  private static boolean isDescending(int [] list)
  {
    for (int k = 0; k != (list.length - 1); ++k)
    {
      if (list[k] < list[k + 1])
      {
	return false;
      }
    }
    return true;
  }


  private static void test ()
  {
    int size = 0x00000100;
    int [] list = new int [size];
    generate(list);
    int [] sorted = sort(list);
    
    System.out.printf("test::FindKthLargest(): ");
    if ( isDescending(sorted) )
    {
      System.out.println("pass");
    }
    else
    {
      System.out.println("FAIL");
    }
  }
}


/*
 * COMMENTS:
 * The FindKthLargest() method creates a shallow copy of the list because
 * the method should not change the original ordering of the list since
 * its only objective is to return the kth largest value.
 *
 * A shallow copy means that we create another list (or object) having the
 * same numeric values. What a method does on the copy does not affect the
 * original.
 *
 */
