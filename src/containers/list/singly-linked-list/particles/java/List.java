/*
 * Algorithms and Complexity                            October 25, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 *
 *
 * Synopsis:
 * Defines a singly linked-list for storing 2D Cartesian coordinates.
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
 *      www.codejava.net/java-core/collections/
 *      sorting-arrays-examples-with-comparable-and-comparator
 * )
 *
 */

import java.util.Random;

public class List
{
  private class Node
  {

    /* node data members */

    private Node  next;
    private Coord data;

    /* node constructors */

    Node ()
      // default constructor
    {
      this.next = null;
      this.data = new Coord();
    }

    Node (Coord data)
      // constructs node that stores given coordinate object
    {
      this.next = null;
      this.data = ( new Coord(data) );
    }

    Node (Node node)
      // copy constructor
    {
      this.next = null;
      this.data = ( new Coord(node.data) );
    }

    /* getters */

    public Coord get ()
      // returns a copy of the coordinates object
    {
      return ( new Coord(data) );
    }
  }


  public class forwardIterator
  {

    /* data */


    private Node iter;


    /* constructors */


    forwardIterator ()
      // default constructor
    {
      this.iter = null;
    }


    forwardIterator (Node head)
      // constructs forward iterator from the list head node
    {
      this.iter = head;
    }


    forwardIterator (Node head, int pos)
      // constructs forward iterator at position
    {
      int i = 0;
      this.iter = head;
      while (i != pos)
	/*

	   NOTE:
	   It is the responsibility of the user to request a
	   valid position; if it is an out-of-bounds position
	   the iterator could point to HEAD or be NULL.

*/
      {
	iter = (iter == null)? null : iter.next;
	++i;
      }
    }


    /* getters */


    public Coord get ()
      // returns a copy of the data stored in the current node
    {
      return this.iter.get();
    }


    /* methods */


    public boolean hasNext ()
      // returns true if there is a next node, false otherwise
    {
      if (this.iter == null)
      {
	return false;
      }
      else
      {
	if (this.iter.next == null)
	  return false;
	else
	  return true;
      }
    }


    public void next ()
      // advances iterator forward
    {
      this.iter = this.iter.next;
    }
  }


  /* list data members */

  private Node head;
  private Node tail;
  private int size;


  /* list constructors */


  List ()
    // default constructor
  {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }


  List (List list)
    // copy constructor
  {
    if (list.head == null)
      // returns a new empty list if passed list is empty
    {
      this.head = null;
      this.tail = null;
      this.size = 0;
      return;
    }
    else
    {
      // otherwise, creates a copy of the head node
      this.head = new Node (list.head);
      this.tail = this.head;

      Node it = list.head.next;
      while (it != null)
	// and copies the remaining nodes into the new list
      {
	this.tail.next = new Node (it);
	this.tail = this.tail.next;
	it = it.next;
      }

      // defines the size of the new list
      this.size = list.size;
    }
  }


  /* getters */


  public Coord get (int pos)
    // returns copy of the coordinates object at position
  {
    if (this.head == null)
    {
      String errmsg = "EmptyListError";
      throw new RuntimeException(errmsg);
    }

    if (pos < 0 || pos >= this.size)
      // complains if the position index is invalid
    {
      String errmsg = "OutOfBoundsError";
      throw new RuntimeException(errmsg);
    }

    if ( pos == 0 )
      // returns data stored in head node
    {
      return ( new Coord(head.data) );
    }

    if ( pos == (this.size - 1) )
      // returns data stored in tail node
    {
      return ( new Coord(tail.data) );
    }

    int i = 0;
    Node iter = this.head;
    while (i != pos)
      // advances iterator to requested position
    {
      iter = iter.next;
      ++i;
    }

    return ( new Coord(iter.data) );
  }


  /* methods */


  public int size ()
    // returns the size of the list (number of elements stored)
  {
    return this.size;
  }


  private void append (Coord c)
    // appends coordinate objects to the list
  {
    this.push_back(c);
  }


  public void insort (Coord c)
    // insertion sort method
  {
    if (this.head == null)
      // if the list is empty
    {
      this.create(c);
      return;
    }


    if (c.compareTo(this.head.data) <= 0)
      // if less than or equal to the first element in the list
    {
      this.push_front(c);
      return;
    }


    if (c.compareTo(this.tail.data) >= 0)
      // if greater than or equal to the last element in the list
    {
      this.push_back(c);
      return;
    }


    //otherwise, searches linearly for the insertion location
    this.insert(c);
    return;
  }


  public int search (Coord target)
    /*

       Synopsis:
       Returns the index of the node containing the target, otherwise
       returns an invalid index to indicate that the target is not in
       the list.

*/
  {
    Coord t = target;
    return this.linearSearch(t);
  }


  public List bisect ()
    // bisects list into two partitions, returns the second partition
  {

    isDivisible();	// complains if list cannot be divided

    int i = 0;		// counter
    Node it   = this.head;	// iterator
    Node head = this.head;	// first partition head
    Node tail = this.tail;	// first partition tail
    int size = this.size;	// original list size
    int half = (size / 2);	// first partition size
    while (i != half)
      // advances iterator until the second partition is reached
    {
      tail = it;
      it = it.next;
      ++i;
    }

    // creates placeholder (list) for the second partition
    List part = new List();
    part.head = it;
    part.tail = this.tail;
    part.size = (size - half);

    // unlinks the first partition from the second partition
    tail.next = null;
    this.head = head;
    this.tail = tail;
    this.size = half;

    return part;
  }


  public void print ()
    // prints stored coordinates on the console
  {
    Node it = this.head;
    while (it != null)
    {
      Coord c = it.data;
      int x = c.getX(), y = c.getY();
      System.out.printf("x, y = %d, %d\n", x, y);
      it = it.next;
    }
  }


  public forwardIterator forwardIterator ()
    // returns default iterator
  {
    return ( new forwardIterator(this.head) );
  }


  public forwardIterator forwardIterator (int pos)
    // returns an iterator that points at requested position
  {
    return ( new forwardIterator(this.head, pos) );
  }


  public Coord [] toArray ()
    // returns an array of the data contained in the nodes
  {
    int size = this.size;
    Coord [] data = new Coord[size];

    int i = 0;
    Node iter = this.head;
    while (iter != null)
    {
      data[i] = iter.get();
      ++i;
      iter = iter.next;
    }

    return data;
  }


  /* implementations */


  private void create (Coord c)
    // creates the first node of the list
  {
    this.head = new Node(c);
    this.tail = this.head;
    ++this.size;
  }


  private void push_front (Coord c)
    // pushes coordinate object at the front of the list
  {
    Node head = this.head;
    this.head = new Node(c);
    this.head.next = head;
    ++this.size;
  }


  private void push_back (Coord c)
    // pushes coordinate object at the back of the list
  {
    if (this.tail == null)
    {
      this.create(c);
      return;
    }
    else
    {
      this.tail.next = new Node(c);
      this.tail = this.tail.next;
      ++this.size;
    }
  }


  private void insert (Coord c)
    // inserts data at suitable location
  {
    // creates iterators
    Node it = this.head;
    Node next = it.next;
    while (next != null && c.compareTo(next.data) > 0)
      // advances iterators until the insertion location is found
    {
      it = it.next;
      next = (next == null)? null : next.next;
    }

    // inserts node at insertion location
    Node node = new Node(c);
    it.next = node;
    node.next = next;
    ++this.size;
  }


  private int linearSearch (Coord target)
    // implements linear search
  {
    // if the list is empty there is nothing to search
    int invalidIndex = (0xFFFFFFFF);
    if (this.head == null)
      return invalidIndex;

    int idx = 0;
    Coord t = target;
    Node it = this.head;
    while (it != null)
    {

      Coord c = it.data;
      if (c.compareTo(t) == 0)
	return idx;

      it = it.next;
      ++idx;
    }

    return invalidIndex;
  }


  private void isDivisible ()
    // complains if the list cannot be divided into two partitions
  {
    String errmsg = (
	"list.bisect(): list of size: " + this.size +
	"cannot be divided into two partitions"
	);
    if (this.size < 2)
      throw new RuntimeException(errmsg);
  }


  public static void main (String [] args)
  {
    testAppendMethod();
    testInsortMethod();
    testSearchMethod();
    testCopyConstructor();
    testForwardIterator();
    return;
  }


  private static void testInsortMethod ()
    // tests the insertion sort method
  {
    int size = 0x00001000;
    List list = new List ();
    Random r = new Random ();
    for (int i = 0; i != size; ++i)
      // fills the list with random coordinates
    {
      int x = r.nextInt(size), y = r.nextInt(size);
      Coord c = new Coord (x, y);
      list.insort(c);
    }


    System.out.printf("test-insort[0]: ");
    // checks the size of the list
    if (list.size() != size)
      System.out.println("FAIL");
    else
      System.out.println("pass");


    int fails = 0;
    for (int i = 0; i != (size - 1); ++i)
      // counts number of disordered pairs
    {
      Coord P = list.get(i);
      Coord Q = list.get(i + 1);
      if (P.compareTo(Q) > 0)
	++fails;
    }


    System.out.printf("test-insort[1]: ");
    if (fails != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");
  }


  private static void testAppendMethod ()
    // tests the append method
  {
    int size = 0x00001000;
    List list = new List();

    Random r = new Random ();
    Coord [] coords = new Coord [size];
    for (int i = 0; i != size; ++i)
      // appends data to the list
    {
      int x = r.nextInt();
      int y = r.nextInt();
      Coord c = new Coord(x, y);
      coords[i] = c;
      list.append(c);
    }

    /*

       checks for differences between the stored and the
       generated data (we expect none)

*/

    int diffs = 0;
    for (int i = 0; i != size; ++i)
    {
      Coord P = coords[i];
      Coord Q = list.get(i);
      diffs += (P.compareTo(Q));
    }

    System.out.printf("test[0]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");
  }


  private static void testSearchMethod ()
    // tests the search method
  {
    int size = 0x00001000;
    List list = new List ();
    Coord [] coords = new Coord [size];
    Random r = new Random ();
    for (int i = 0; i != size; ++i)
      // fills the list with random coordinates
    {
      int x = r.nextInt(size), y = r.nextInt(size);
      Coord c = new Coord (x, y);
      coords[i] = c;
      list.insort(c);
    }


    /*

       counts the number of times the search method fails to find
       the target in the list (we expect none for we know that the
       target must be in the list)

*/


    int fails = 0;
    for (int i = 0; i != size; ++i)
      // counts number of search failures
    {
      Coord c = coords[i];
      if (list.search(c) < 0)
	++fails;
    }


    System.out.printf("test-search[0]: ");
    if (fails != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");
  }


  private static void testCopyConstructor ()
    // test the copy of constructor of the list class
  {
    int size = 0x00001000;
    List list = new List ();
    Random r = new Random ();
    for (int i = 0; i != size; ++i)
    {
      int x = r.nextInt(size), y = r.nextInt(size);
      Coord c = new Coord (x, y);
      list.insort(c);
    }

    // creates a copy fo the list
    List copy = new List(list);
    System.out.printf("test-copy-constructor[0]: ");
    if ( list.size() != copy.size() )
      System.out.println("FAIL");
    else
      System.out.println("pass");


    int diffs = 0;
    for (int i = 0; i != size; ++i)
      // counts number of differences between the lists
    {
      Coord P = list.get(i);
      Coord Q = copy.get(i);
      if (P.compareTo(Q) != 0)
	++diffs;
    }


    System.out.printf("test-copy-constructor[1]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");
  }


  private static void testForwardIterator ()
    // test the forward iterator of the list class
  {
    int size = 0x00001000;
    List list = new List ();
    Random r = new Random ();
    for (int i = 0; i != size; ++i)
    {
      int x = r.nextInt(size), y = r.nextInt(size);
      Coord c = new Coord (x, y);
      list.insort(c);
    }


    /*

test: uses the iterator to determine the list size

*/


    List copy = new List(list);	// invokes copy constructor


    int count = 0;
    forwardIterator iter = copy.forwardIterator();
    while ( iter.hasNext() )
      /*

	 while-loop invariant:
	 so far we have counted `count' nodes with a node next to it

*/
    {
      ++count;
      iter.next();
    }


    /* #nodes: the list has `count' nodes plus the tail node */


    int nodes = (count + 1);
    System.out.printf("test-forward-iterator[0]: ");
    if ( nodes != list.size() )
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /*

test:
checks that the data pointed to by the iterator of the
copied list is the same as that contained in the original
list (as it should)

*/


    int diffs = 0;
    iter = copy.forwardIterator();
    for (int i = 0; i != size; ++i)
      // counts number of differences
    {
      Coord P = list.get(i);
      Coord Q = iter.get();
      if (P.compareTo(Q) != 0)
	++diffs;

      iter.next();
    }


    System.out.printf("test-forward-iterator[1]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /*

test:
checks that the data pointed to by the iterator is the
same as that contained in the list (as it should)

*/


    diffs = 0;
    iter = list.forwardIterator();
    Coord [] data = list.toArray();
    for (int i = 0; i != size; ++i)
      // counts differences
    {
      Coord P = data[i];
      Coord Q = iter.get();
      if (P.compareTo(Q) != 0)
	++diffs;

      iter.next();
    }


    System.out.printf("test-forward-iterator[2]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /* divides the original list into two partitions */


    List first = copy;
    List second = first.bisect();


    /* test: checks the size of the first partition */


    count = 0;
    iter = first.forwardIterator();
    while ( iter.hasNext() )
      /*

	 while-loop invariant:
	 so far we have counted `count' nodes with a node next to it

*/
    {
      ++count;
      iter.next();
    }


    /* #nodes: the list has `count' nodes plus the tail node */


    nodes = (count + 1);
    System.out.printf("test-forward-iterator[3]: ");
    if ( nodes != first.size() )
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /* test: checks the size of the second partition */


    count = 0;
    iter = second.forwardIterator();
    while ( iter.hasNext() )
      /*

	 while-loop invariant:
	 so far we have counted `count' nodes with a node next to it

*/
    {
      ++count;
      iter.next();
    }


    /* #nodes: the list has `count' nodes plus the tail node */


    nodes = (count + 1);
    System.out.printf("test-forward-iterator[4]: ");
    if ( nodes != second.size() )
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /*

test:
checks that the data in the first partition corresponds
to the data stored in the first half of the original list

*/


    diffs = 0;
    data = list.toArray();
    iter = first.forwardIterator();
    for (int i = 0; i != first.size(); ++i)
      // counts number of differences
    {
      Coord P = data[i];
      Coord Q = iter.get();
      if (P.compareTo(Q) != 0)
	++diffs;

      iter.next();
    }


    System.out.printf("test-forward-iterator[5]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");


    /*

test:
checks that the data in the second partition corresponds
to the data stored in the second half of the original list

*/


    diffs = 0;
    data = list.toArray();
    iter = second.forwardIterator();
    for (int i = 0; i != second.size(); ++i)
      // counts number of differences
    {
      Coord P = data[i + first.size()];
      Coord Q = iter.get();
      if (P.compareTo(Q) != 0)
	++diffs;

      iter.next();
    }


    System.out.printf("test-forward-iterator[6]: ");
    if (diffs != 0)
      System.out.println("FAIL");
    else
      System.out.println("pass");
  }
}
