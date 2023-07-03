#ifndef GUARD_AC_HANDLER_CLASS
#define GUARD_AC_HANDLER_CLASS

#include <cstddef>	// provides NULL
#include <list>		// provides std::list, a doubly linked-list

template <typename T> class Handler
// Generic Handler Class for handling dynamically allocated objects
{

  private:


    // component(s):


    std::list<T> objects;		// objects placeholder


  public:


    // (de)constructors:


    Handler () {}			// default constructor


    ~Handler ()			// destructor
    {
      destroy();
    }


    const std::list<T>& getObjects () const
      // returns a reference to the vector of point objects
    {
      return (this -> objects);
    }


    size_t size () const
      // returns the number of objects contained in the handler
    {
      return ( this -> objects.size() );
    }


    void add (T object)
      // adds object to the handler
    {
      this -> objects.push_back(object);
    }


    void remove (T object)
      // removes object from the handler
    {
      this -> objects.remove(object);
    }


    void erase ()
      // erases all objects from the handler
    {
      this -> destroy();
    }


    bool contains (const T& target) const
      // returns true if the target is present, returns false otherwise
    {
      const std::list<T>& objects = this -> objects;
      for (T object : objects)
      {
	if ( object -> equalTo(target) )
	{
	  return true;
	}
      }

      return false;
    }


  private:


    // implementations:


    void destroy ()
      // releases dynamically allocated objects from memory
    {
      for (T obj : objects)
      {
	delete obj;	// releases object from memory
	obj = NULL;	// nullifies pointer to object
      }

      // erases the nodes of the linked-list std::list
      objects.erase( objects.begin(), objects.end() );
    }
};

#endif

/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Defines the Generic Handler Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/
