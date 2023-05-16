/*
 * Algorithms and Programming II                          April 06, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of (some) sorting algorithms in Java.
 *
 *
 * Copyright (c) 2022 Misael Diaz-Maldonado
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] JJ McConnell, Analysis of Algorithms, 2nd edition
 * [1] lambdas: www.geeksforgeeks.org/java-lambda-expressions-parameters
 *
 */

import java.util.Random;

interface Comparator
{
  public boolean f(int x, int y);	// defines abstract comparison function f(x, y)
}


public class SortingAlgorithms
{

  // implements comparison (lambda) functions
  static Comparator isGreater = (int x, int y) -> { return (x > y); };
  static Comparator isSmaller = (int x, int y) -> { return (x < y); };


  public static void main (String[] args)
  {
    sort();					// sorts a list with duplicates
    ascend();					// sorts in ascending order
    descend();					// sorts in descending order
    testInsertionSort();			// tests the insertion sort implementation
    test();
  }


  // examples:
  public static void sort()			// sorts a list that contains duplicates
  {
    int list [] = {6, 2, 4, 7, 1, 3, 8, 5, 1};

    System.out.printf("\nInsertion Sort:\n");
    System.out.printf("\nOriginal List:\n");
    print(list);				// prints the original list

    boolean iters = true;
    InsertionSort(list, iters);			// prints the list on each iteration

    System.out.printf("\nSorted List:\n");
    print(list);				// prints the sorted list
  }


  public static void ascend()			// sorts in ascending order example
  {
    int list [] = {6, 2, 4, 7, 1, 3, 8, 5};

    System.out.printf("\n\nSorts in Ascending Order:\n\n");
    System.out.printf("\n\nOriginal List:\n\n");
    print(list);

    InsertionSort(list, isGreater);

    System.out.printf("\n\nSorted List:\n\n");
    print(list);
  }


  public static void descend()			// sorts in descending order example
  {
    int list [] = {6, 2, 4, 7, 1, 3, 8, 5};

    System.out.printf("\n\nSorts in Descending Order:\n\n");
    System.out.printf("\n\nOriginal List:\n\n");
    print(list);

    InsertionSort(list, isSmaller);

    System.out.printf("\n\nSorted List:\n\n");
    print(list);
  }




  private static int [] ulist ()	// Returns a list of unsorted integers.
  {
    int list[] = {16,  7, 10,  1,  5, 11,  3,  8, 14,  4,  2, 12,  6, 13,  9, 15};
    return list;
  }


  private static int [] arange ()	// Returns an asymmetric range of integers.
  {
    int size = 16;
    int x[] = new int [size];
    for (int i = 0; i != size; ++i)
      x[i] = (i + 1);

    return x;
  }


  // interfaces:


  public static void InsertionSort (int [] list)
  {
    defaultInsertionSort(list);
  }


  // overloads Insertion Sort with the verbose implementation
  public static void InsertionSort (int [] list, boolean verbose)
  {
    if (verbose)
      verboseInsertionSort(list);
    else
      defaultInsertionSort(list);
  }


  // implementations:


  // void defaultInsertionSort (int [] list)
  //
  // Synopsis:
  // Possible implementation of the Insertion Sort Algorithm.
  //
  // Inputs
  // list		array of (presumed) unsorted integers
  //
  // Output
  // list		the sorted list


  private static void defaultInsertionSort (int [] list)
  {
    int ins, loc;
    for (int i = 1; i != list.length; ++i)
    {
      ins = list[i];		// insertion element
      loc = (i - 1);		// sorted range [0, loc]
      shift (list, ins, loc);	// inserts in right place
    }
  }


  // void verboseInsertionSort (int [] list)
  //
  // Synopsis:
  // Possible implementation of the Insertion Sort Algorithm.
  // Verbose version. Prints the elements of the list on each pass.
  //
  // Inputs
  // list		array of (presumed) unsorted integers
  //
  // Output
  // list		the sorted list


  private static void verboseInsertionSort (int [] list)
  {
    int i, ins, loc;
    System.out.printf("\n\nPasses:\n\n");
    for (i = 1; i != list.length; ++i)
    {
      System.out.printf("%4d:\t", i - 1);
      print (list);		// prints the list
      ins = list[i];		// insertion element
      loc = (i - 1);		// sorted range [0, loc]
      shift (list, ins, loc);	// inserts in right place
    }

    System.out.printf("%4d:\t", i - 1);
    print(list);
  }


  // void InsertionSort (int [] list, Comparator comp)
  //
  // Synopsis:
  // Possible implementation of the Insertion Sort Algorithm.
  //
  // Inputs
  // list		array of (presumed) unsorted integers
  // comp		user-defined comparator of list elements
  //
  // Output
  // list		the sorted list


  public static void InsertionSort (int [] list, Comparator comp)
  {
    int ins, loc;
    for (int i = 1; i != list.length; ++i)
    {
      ins = list[i];
      loc = (i - 1);
      shift (list, comp, ins, loc);
    }
  }


  // void shift (int [] list, int ins, int loc)
  //
  // Synopsis:
  // Shifts elements in the list whose value are greater than the
  // insertion element. Returns the list having the new element in
  // its rigthful location so that the ordered structure of the list
  // is preserved.
  //
  // Inputs
  // list		the list with sorted elements in the range [0, loc]
  // ins		insertion (or new) element
  // loc		index indicating the sorted portion of the list
  //
  // Ouput
  // list		the ordered list containing the new element


  private static void shift (int [] list, int ins, int loc)
  {
    while ( (loc >= 0) && (list[loc] > ins) )	// shifts larger elements to the right:
    {
      list[loc + 1] = list[loc];
      --loc;
    }

    list[loc + 1] = ins;			// inserts element in the correct location
  }


  // void shift (int [] list, Comparator comp, int ins, int loc)
  //
  // Synopsis:
  // Shifts elements in the list whose value are greater than the
  // insertion element. Returns the list having the new element in
  // its rigthful location so that the ordered structure of the list
  // is preserved. Uses the supplied comparator to compare the list
  // elements.
  //
  // Inputs
  // list		the list with sorted elements in the range [0, loc]
  // comp		user-defined comparator of list elements
  // ins		insertion (or new) element
  // loc		index indicating the sorted portion of the list
  //
  // Ouput
  // list		the ordered list containing the new element


  private static void shift (int [] list, Comparator comp, int ins, int loc)
  {
    while ( (loc >= 0) && comp.f(list[loc], ins) )
    {
      list[loc + 1] = list[loc];
      --loc;
    }

    list[loc + 1] = ins;
  }


  private static void consistency (int [] x, int [] y)
  {
    if (x.length != y.length)	// complains if the lists have different lengths
    {
      throw new RuntimeException("lists must have the same length");
    }
  }


  // Returns true if the lists are identical, false otherwise.
  private static boolean equality (int [] x, int [] y)
  {
    consistency (x, y);			// complains if the list differ in length

    int diff = 0;
    for (int i = 0; i != x.length; ++i)
      diff += (x[i] - y[i]);		// computes differences elementwise

    return ( (diff == 0)? true : false );
  }


  private static int [] copy (int [] list)	// Creates a shallow copy of a list.
  {
    int ls [] = new int [list.length];
    for (int i = 0; i != list.length; ++i)
      ls[i] = list[i];

    return ls;
  }


  private static void print (int [] list)
  {
    // prints the elements of the list to the console
    for (int i = 0; i != list.length; ++i)
      System.out.printf("%4d", list[i]);
    System.out.println("\n");
  }


  // tests:


  // void testInsertionSort ()
  //
  // Synopsis:
  // Tests the implementation of InsertionSort().
  // Compares the sorted list against the expected one.
  // The implementation passes the test if the lists are identical.


  private static void testInsertionSort ()
  {
    int list [] = ulist ();				// unsorted list
    int incr [] = arange ();				// expected list

    InsertionSort(list);				// sorts list in ascending order

    System.out.printf("test::InsertionSort(): ");
    if ( equality(list, incr) )
      System.out.printf("pass\n");
    else
      System.out.printf("FAIL\n");
  }


  private static void test ()	// tests if the implementation of insertion sort fails
  {
    int fails = 0;					// failures counter
    int reps = 256;					// repetitions
    for (int size = 2; size != 0x00001000; size *= 2)
    {
      int [] list = new int [size];
      for (int rep = 0; rep != reps; ++rep)
      {
	generate(list);
	InsertionSort(list);
	if ( !sorted(list) )
	{
	  ++fails;
	}
      }
    }

    System.out.printf("test::InsertionSort(): ");
    if (fails != 0)
    {
      System.out.printf("FAIL\n");
    }
    else
    {
      System.out.printf("pass\n");
    }

  }


  private static void generate (int [] list)	// fills list with random numbers
  {
    int min = 0;
    int max = list.length;
    Random r = new Random();
    for (int i = 0; i != list.length; ++i)
    {
      list[i] = min + r.nextInt(max - min);
    }
  }


  private static boolean sorted (int [] list)
  {
    for (int i = 0; i != (list.length - 1); ++i)
    {
      if (list[i] > list[i + 1])
      {
	return false;
      }
    }

    return true;
  }
}
