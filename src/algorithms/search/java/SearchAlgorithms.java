/*
 * Algorithms and Programming II                          April 01, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of the sequential search algorithm in Java.
 *
 *
 * Sinopsis:
 * Posible implementacion del algoritmo de busqueda secuencial en Java.
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
 *
 */


public class SearchAlgorithms
{

  public static void main (String[] args)
  {
    succSequentialSearch();	// performs a successful search
    failSequentialSearch();	// performs a failed search
  }


  // interfaces:


  // int SequentialSearch (int [] list, int target)
  //
  // Synopsis:
  // Possible implementation of the Sequential Search Algorithm.
  // Searches sequentially until a match is found or until the list
  // is traversed completely without a match.
  //
  // Inputs
  // list		list of (presumed) unsorted integers
  // target		target value
  //
  // Output
  // pos		positional index [1, NUMEL], otherwise zero


  public static int SequentialSearch (int [] list, int target)
  {
    int pos = 0;
    for (int i = 0; i != list.length; ++i)
    {
      if (list[i] == target)
      {
	pos = (i + 1);
	break;
      }
    }

    return pos;
  }


  // implementations:


  private static int [] ulist ()	// returns a list of unsorted integers.
  {
    int list [] = {16,  7, 10,  1, 5, 11,  3,  8, 14,  4,  2, 12, 6, 13,  9, 15};
    return list;
  }


  private static void search (int [] list, int target)
  {
    // searches for the target and displays its position in the list
    int pos = SequentialSearch (list, target);

    System.out.printf("\nSequential Search:\n");
    if (pos != 0)
      System.out.printf("target found in position %d\n\n", pos);
    else
      System.out.printf("target is not in the list\n\n");
  }


  // tests:


  // performs a successful search, looks for a value which is actually in the list.
  private static void succSequentialSearch ()
  {
    int target = 11;		// target is in the list
    int list [] = ulist();	// creates unsorted list
    search(list, target);	// searches for the target
  }


  // performs an unsuccessful search, looks for a value which is not in the list.
  private static void failSequentialSearch ()
  {
    int target = 21;		// target is *not* in the list
    int list [] = ulist();	// creates unsorted list
    search(list, target);	// searches for target
  }
}
