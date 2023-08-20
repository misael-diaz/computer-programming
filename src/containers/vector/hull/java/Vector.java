import java.util.Arrays;
import java.util.Random;
import java.util.Comparator;

public class Vector
{
  // defines comparator for y-x sorting of coordinates
  public static Comparator<Coord> comparator = (Coord P, Coord Q) -> {
    if ( P.getY() != Q.getY() )
    {
      return ( P.getY() - Q.getY() );
    }
    else
    {
      return ( P.getX() - Q.getX() );
    }
  };


  // initializes parameters (not bound to objects of the class)
  private static final int DEFAULT_SIZE_LIMIT = (0x00000010);


  // defines private data members:


  private int begin;		// beginning of the placeholder (0)
  private int avail;		// available location for writing
  private int limit;		// size limit for storing data
  private Coord[] data;		// data placeholder


  // constructors:


  // default constructor: creates a vector of default size limit
  Vector ()
  {
    this.begin = 0;
    this.avail = 0;
    this.limit = DEFAULT_SIZE_LIMIT;
    this.data  = new Coord[limit];
  }


  // constructs a vector with requested capacity for storage
  Vector (int size)
  {
    this.begin = 0;
    this.avail = 0;
    this.limit = size;
    this.data  = new Coord[size];
  }


  // copy constructor
  Vector (Vector vector)
  {
    this.begin = 0;			// sets to default
    this.avail = vector.size();		// gets the size
    this.limit = vector.size();		// fits to size
    this.data  = vector.getData();	// gets a copy of the data
  }


  // getters:


  // returns a clone of the data contained in vector
  public Coord[] getData ()
  {
    return Arrays.copyOfRange(this.data, this.begin, this.avail);
  }


  // returns a copy of the ith element
  public Coord getData (int i)
  {
    return new Coord(this.data[i]);
  }


  // methods:


  // effectively clears the vector elements
  public void clear ()
  {
    this.avail = 0;
  }


  // returns the number of elements stored in the vector
  public int size ()
  {
    return (this.avail - this.begin);
  }


  // returns the storage capacity of the vector
  public int capacity ()
  {
    return (this.limit - this.begin);
  }


  // pushes coordinates unto the back of vector
  public void push_back (Coord x)
  {
    this.back_inserter(x);
  }


  // delegates the task of sorting to the sort method of Arrays
  public void sort ()
  {
    Arrays.sort(this.data, this.begin, this.avail);
  }


  // delegates the task of sorting to the sort method of Arrays
  public void sort (Comparator<Coord> comp)
  {
    Arrays.sort(this.data, this.begin, this.avail, comp);
  }


  // delegates the task to the Binary Search method of Arrays
  public int search (Coord key)
  {
    return Arrays.binarySearch(this.data, this.begin, this.avail, key);
  }


  // delegates the task to the Binary Search method of Arrays
  public int search (Coord key, Comparator<Coord> comp)
  {
    return Arrays.binarySearch(this.data, this.begin, this.avail, key, comp);
  }


  // prints the (x, y) coordinates on the console
  public void print ()
  {
    int size = this.size();
    for (int i = 0; i != size; ++i)
    {
      Coord c = this.data[i];
      String fmt = ("x, y = (%8d, %8d)\n");
      System.out.printf(fmt, c.getX(), c.getY());
    }
  }


  // implementations:


  // pushes data into the back of vector
  private void back_inserter (Coord x)
  {
    if (this.avail == this.limit)	// checks if there's space left
    {
      this.grow();			// doubles the vector size limit
    }

    this.data[this.avail] = x;		// writes at available location
    ++this.avail;			// increments vector size accordingly
  }


  // doubles the vector size
  private void grow ()
  {
    int lim = this.limit;		// gets current size limit
    Coord[] tmp = this.data.clone();	// copies data to temporary

    this.limit *= 2;			// doubles size limit
    this.data = new Coord[this.limit];	// doubles allocation
    for (int i = 0; i != lim; ++i)	// restores data
    {
      this.data[i] = tmp[i];
    }
  }


  // tests the methods of the vector class
  public static void main (String[] args)
  {
    testPushBackMethod();
    testClearMethod();
    testSortMethod();
    testSearchMethod();
    return;
  }


  // tests:


  // Pushes coordinates unto the back of the vector and checks its size.
  private static void testPushBackMethod ()
  {
    int size = 32;
    Vector vector = new Vector();
    for (int i = 0; i != size; ++i)
    {
      vector.push_back( new Coord(i, i) );
    }

    System.out.printf("push-back-method-test: ");
    // checks the vector size against the expected size
    if (vector.size() != size)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }
  }


  //Pushes coordinates unto the back of a vector, clears it, and checks that it is empty.
  private static void testClearMethod ()
  {
    int size = 32;
    Vector vector = new Vector();
    for (int i = 0; i != size; ++i)
    {
      vector.push_back( new Coord(i, i) );
    }


    vector.clear();	// clears vector


    System.out.printf("clear-method-test: ");
    // checks the vector size against the expected size
    if (vector.size() != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }
  }


  // Pushes random coordinates unto the back of a vector, sorts it in ascending order,
  // and checks if the sorting was successful.
  private static void testSortMethod ()
  {
    int size = 8;
    Vector vector = new Vector(size);


    // NullPointerException Test: Sorts Empty Vector. Passes if no exception is thrown.


    vector.sort();


    // Pushes data unto back vector


    Random random = new Random();
    // fills vector with random coordinates
    for (int i = 0; i != size; ++i)
    {
      int x = random.nextInt(size);
      int y = random.nextInt(size);
      Coord coord = new Coord(x, y);
      vector.push_back(coord);
    }


    vector.sort();	// sorts data contained in vector


    Coord[] data = vector.getData();
    // prints the (sorted) coordinates on the console
    for (int i = 0; i != size; ++i)
    {
      int x = data[i].getX();
      int y = data[i].getY();
      System.out.printf("x: %2d y: %2d\n", x, y);
    }


    int failures = 0;
    // counts failures (not in ascending order instances)
    for (int i = 0; i != (size - 1); ++i)
    {
      Coord thisCoord = data[i], nextCoord = data[i + 1];
      if (thisCoord.compareTo(nextCoord) > 0)
      {
	++failures;
      }
    }


    System.out.printf("sort-method-test[0]: ");
    // checks if the sorting method failed (unexpectedly)
    if (failures != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }


    // performs y - x sorting


    vector.sort(comparator);
    data = vector.getData();
    System.out.println("y-x sorting:");
    // prints the (sorted) coordinates on the console
    for (int i = 0; i != size; ++i)
    {
      int x = data[i].getX();
      int y = data[i].getY();
      System.out.printf("x: %2d y: %2d\n", x, y);
    }


    failures = 0;
    // counts failures (not in ascending order instances)
    for (int i = 0; i != (size - 1); ++i)
    {
      Coord thisCoord = data[i], nextCoord = data[i + 1];
      if (comparator.compare(thisCoord, nextCoord) > 0)
      {
	++failures;
      }
    }


    System.out.printf("sort-method-test[1]: ");
    // checks if the sorting method failed (unexpectedly)
    if (failures != 0)
    {
      System.out.println("FAIL\n");
    }
    else
    {
      System.out.println("pass\n");
    }
  }


  // Uses the search method to create a distinct set of (x, y) coordinates.
  private static void testSearchMethod ()
  {
    Vector vector = new Vector();	// creates (empty) vector
    Random random = new Random();	// creates (default) PRNG


    // creates data set from random data


    int size = (0x00000010);
    // generates the distinct set of (x, y) coordinates
    for (int i = 0; i != size; ++i)
    {
      int x = random.nextInt(size);
      int y = random.nextInt(size);
      Coord c = new Coord(x, y);
      // generates a new coordinate if already in vector
      while (vector.search(c) >= 0)
      {
	x = random.nextInt(size);
	y = random.nextInt(size);
	c = new Coord(x, y);
      }
      // pushes (distinct) coordinate unto back of vector
      vector.push_back(c);
      // sorts to use binary search on next pass
      vector.sort();
    }


    // Displays Data on the Console


    Coord[] data = vector.getData();
    // prints the (distinct set of) coordinates on the console
    for (int i = 0; i != size; ++i)
    {
      int x = data[i].getX();
      int y = data[i].getY();
      System.out.printf("x: %2d y: %2d\n", x, y);
    }


    // Duplicates Test


    int duplicates = 0;
    // counts duplicates by checking for equality
    for (int i = 0; i != (size - 1); ++i)
    {
      Coord thisCoord = data[i], nextCoord = data[i + 1];
      if (thisCoord.compareTo(nextCoord) == 0)
      {
	++duplicates;
      }
    }

    System.out.printf("search-method-test[0]: ");
    if (duplicates != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }


    // Performs y - x sorting test:
    //
    // The test consists on searching the x-y sorted data after the vector has been
    // y-x sorted. We increment the number of failures every time the method fails to
    // find an element. Note that if the comparator is not supplied to the binary
    // search method, it will fail sometimes because the data has been y-x sorted while
    // the method assumes x-y sorting. On the other hand, when the comparator is supplied
    // the search is successful.


    int failures = 0;		// initializes counter
    data = vector.getData();	// gets x - y sorted data
    vector.sort(comparator);	// does y - x sorting
    // searches the x-y sorted data in the y-x sorted vector
    for (int i = 0; i != size; ++i)
    {
      Coord c = data[i];
      if (vector.search(c, comparator) < 0)
      {
	++failures;
      }
    }


    System.out.printf("search-method-test[1]: ");

    // #failures should be zero because we are searching for actually contained data
    if (failures != 0)
    {
      System.out.println("FAIL");
    }
    else
    {
      System.out.println("pass");
    }
  }
}

/*
 * Algorithms and Complexity                            October 06, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Possible implementation of a vector of coordinates (x, y) in Java.
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
 * [1] Sorting Custom Objects Tutorial: (
 * 	www.codejava.net/java-core/collections/
 * 	sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */
