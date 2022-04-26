/*
 * Algorithms and Programming II                             April 26, 2022
 * IST 2089
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a map (or associative array) in Java.
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

import java.util.Random;

public class Map
{
	int begin = 0;				// beginning
	int avail = 0;				// available for writing
	int limit = 16;				// default size limit
	Item [] array = new Item [limit];	// placeholder

	static class Item
	{
		int key;
		int value;

		Item (int k)
		// constructor, creates item object from key
		{
			key = k;
			value = 0;
		}
	}

	// interfaces:
	public static int size (Map map)
	// returns the number of elements stored in the map
	{
		return (map.avail - map.begin);
	}


	public static int insert (Map map, int key)
	/*
	 * Synopsis:
	 * Inserts key unto the back of the map if it does not exist.
	 * And returns the index where the key is located in the map.
	 *
	 */
	{
		int avail = map.avail;
		int limit = map.limit;

		if (avail == limit)
			grow (map);

		int i;
		// searches for key in the map
		int pos = search (map, key);
		if (pos == 0)
		{
			// inserts new key and get its index
			i = map.avail;
			map.array[map.avail++] = new Item(key);
		}
		else
		{
			// gets index of existing key
			i = (pos - 1);
		}

		return i;
	}


	public static void keys (Map map)
	// prints the keys on the console
	{
		System.out.println("keys:");
		for (int i = 0; i != size(map); ++i)
			System.out.printf("%d\n", map.array[i].key);

		return;
	}


	public static void values (Map map)
	// prints the values on the console
	{
		System.out.println("values:");
		for (int i = 0; i != size(map); ++i)
			System.out.printf("%d\n", map.array[i].value);

		return;
	}


	public static int search (Map map, int key)
	// returns the positional index of the item with the key in the map
	{
		int numel = size (map);
		// delegates the task to the Sequential Search Algorithm
		return SequentialSearch (numel, map.array, key);
	}


	// implementations:
	private static void grow (Map map)
	// doubles the map size limit
	{
		int limit = 2 * map.limit;
		int numel = map.avail;
		Item [] items = new Item [numel];

		// copies existing data into temporary placeholder
		for (int i = 0; i != numel; ++i)
			items[i] = map.array[i];

		// doubles the map size limit and restores the data
		map.array = null;
		map.array = new Item [limit];
		map.limit = limit;
		for (int i = 0; i != numel; ++i)
			map.array[i] = items[i];

		return;
	}


	private static int SequentialSearch (int N, Item [] items, int key)
	/*
	 * Synopsis:
	 * Possible implementation of the Sequential Search Algorithm.
	 * Searches sequentially until a match is found or until the array
	 * is traversed completely.
	 *
	 * Inputs
	 * N		number of elements in the array of items
	 * items	array of (presumed) unsorted items
	 * key		key, target
	 *
	 * Output
	 * idx		positional index [1, NUMEL], otherwise zero
	 *
	 */
	{
		int idx = 0;
		for (int i = 0; i != N; ++i)
		{
			if (items[i].key == key)
			{
				idx = (i + 1);
				break;
			}
		}

		return idx;
	}


	public static void main (String[] args)
	{
		// counts number of duplicates in a list of random numbers
		duplicates();
		return;
	}


	// examples:
	private static void duplicates ()
	// counts the number of duplicates in a list of random numbers
	{
		System.out.println();
		System.out.println("Duplicates:");

		int numel = 32;
		Random random = new Random();
		int [] rand = new int [numel];

		System.out.println();
		System.out.println("list with duplicates:");
		for (int i = 0; i != numel; ++i)
		{
			rand[i] = random.nextInt(8);
			System.out.println(rand[i]);
		}

		int idx;
		int key = 0;
		Map map = new Map();
		for (int i = 0; i != numel; ++i)
		{
			key = rand[i];
			idx = map.insert (map, key);
			++map.array[idx].value;
		}


		int value;
		System.out.println();
		System.out.printf("random number \t repetitions:\n");
		System.out.printf("=============================\n");
		for (int i = 0; i != size(map); ++i)
		{
			key = map.array[i].key;
			value = map.array[i].value;
			System.out.printf("%6d \t\t %6d\n", key, value);
		}

		System.out.println();
		/* uncomment if you wish to see the keys and values */
		//keys (map);
		//values (map);

		return;
	}
}
