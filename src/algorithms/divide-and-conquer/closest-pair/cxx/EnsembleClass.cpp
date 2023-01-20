/*

Algorithms and Complexity				   January 12, 2023
IST 4310
Prof M Diaz-Maldonado

Synopsis:
Implements the Ensemble Class.

Copyright (c) 2023 Misael Diaz-Maldonado
This file is released under the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

References:
[0] A Koenig and B Moo, Accelerated C++ Practical Programming by Example

*/


#include "EnsembleClass.h"


/* (de)constructors */


// useless default constructor
Ensemble::Ensemble () : elapsedTime(0), numOperations(0), size(0) {}


// constructs an ensemble of requested size
Ensemble::Ensemble (int size) : elapsedTime(0), numOperations(0), size(size) {}


// destructor
Ensemble::~Ensemble () {}


/* getters */


double Ensemble::getElapsedTime () const
// returns the elapsed-time spent on finding the closest pair
{
	return (this -> elapsedTime);
}


double Ensemble::getNumOperations () const
// returns the number of operations spent on finding the closest pair
{
	return (this -> numOperations);
}


double Ensemble::getSize () const
// returns a copy of the ensemble size in a double precision floating-point number
{
	return (this -> size);
}


/* methods */


void Ensemble::bruteForce ()
/*

Synopsis:
Applies the Brute Force Algorithm to obtain the closest pair.

Input:
None

Output:
None

*/
{
	if ( (this -> size) < 2 )
	// caters invalid ensemble sizes
	{
		return;
	}

	// we need a local handler to release the closest pair from memory
	Handler<Pair*> handler;

	// creates a vector with a storage capacity to store the ensemble
	std::vector<Point*> points;
	points.reserve(this -> size);

	// creates a new dataset of distinct points
	this -> createDataset1D(points);

	// uses the brute force algorithm to find the closest pair
	Pair *closestPair = this -> bruteForceMethod(points);
	handler.add(closestPair);

	// displays info about the closest pair on the console
	std::cout << "Brute Force Algorithm:" << std::endl;
	closestPair -> print();
}


void Ensemble::recursive1D ()
/*

Synopsis:
Finds the closest pair via the 1D Divide And Conquer Algorithm.

Input:
None

Output:
None

*/
{
	if ( (this -> size) < 2 )
	// caters invalid ensemble sizes
	{
		return;
	}


	// we need a local handler to release the closest pair from memory
	Handler<Pair*> handler;


	// creates a vector with a storage capacity to store the ensemble
	std::vector<Point*> points;
	points.reserve(this -> size);


	// creates a new dataset of distinct points
	this -> createDataset1D(points);


	// saves the closest pair found by the Brute Force Algorithm
	Pair *closestPairBruteForce = this -> bruteForceMethod(points);
	handler.add(closestPairBruteForce);


	// uses the 1D Divide And Conquer Algorithm to find the closest pair
	Pair *closestPair = this -> recursive1DMethod(points);
	handler.add(closestPair);


	// reports to the user if the closest pairs do not match
	if ( closestPair -> equalTo(closestPairBruteForce) )
	{
		return;
	}
	else
	{
		std::cout << "Brute Force Algorithm:" << std::endl;
		closestPairBruteForce -> print();

		std::cout << "1D Divide And Conquer Algorithm:" << std::endl;
		closestPair -> print();

		std::cout << "FAIL" << std::endl;
		return;
	}
}


/* implementations */


Pair* Ensemble::bruteForceMethod (std::vector<Point*>& points)
/*

Synopsis:
Applies the Brute Force Algorithm to obtain the closest pair.
Sets the elapsed-time (nanoseconds) invested in determining the
closest pair. It also sets the number of operations (or the number
of distance computations) executed by the Brute Force algorithm to
find the closest pair.

Input:
points		dataset of distinct points

Output:
closestPair	the closest pair

*/
{

	try
	{
		// complains if invalid
		this -> isInvalidData(points);
	}
	catch (std::invalid_argument& e)
	/*

	Displays the exception and returns a dummy closest pair. If we halt the
	execution of the code here we will incur in a memory leak. This is why
	we let the code complete its execution so that it ends gracefully.

	*/
	{
		// reports what went wrong on the console
		std::cout << e.what() << std::endl;
		// the handler of the caller method releases the pair object from memory
		return ( new Pair() );
	}


	Pair *closestPair;
	double numOperations;
	// times the Brute Force Algorithm
	const auto tStart = std::chrono::system_clock::now();
	std::tie(closestPair, numOperations) = this -> distance(points);
	const auto tEnd = std::chrono::system_clock::now();

	// stores the elapsed time (nanoseconds) in a double
	std::chrono::duration<double, std::nano> duration = (tEnd - tStart);
	double elapsedTime = duration.count();

	// sets the elapsed time
	this -> elapsedTime = elapsedTime;
	// sets the number of operations
	this -> numOperations = numOperations;

	return closestPair;
}


Pair* Ensemble::recursive1DMethod (std::vector<Point*>& points)
/*

Synopsis:
Applies the 1D Divide and Conquer Algorithm to find the closest pair.
Sets the elapsed-time (nanoseconds) and the number of operations
invested in finding the closest pair. It also sets the total number of
operations (or equivalently, the total number of distance computations)
executed by the Divide And Conquer algorithm to find the closest pair.

Input:
points		dataset of distinct points

Output:
closestPair	the closest pair

*/
{
	try
	{
		// complains if invalid
		this -> isInvalidData(points);
	}
	catch (std::invalid_argument& e)
	/*

	Displays the exception and returns a dummy closest pair. If we halt the
	execution of the code here we will incur in a memory leak. This is why
	we let the code complete its execution so that it ends gracefully.

	*/
	{
		// reports what went wrong on the console
		std::cout << e.what() << std::endl;
		// the handler of the caller method releases the pair object from memory
		return ( new Pair() );
	}


	Pair *closestPair;
	double numOperations;
	// times the Divide And Conquer Algorithm
	const auto tStart = std::chrono::system_clock::now();
	std::tie(closestPair, numOperations) = this -> recurse(points);
	const auto tEnd = std::chrono::system_clock::now();

	// stores the elapsed time (nanoseconds) in a double
	std::chrono::duration<double, std::nano> duration = (tEnd - tStart);
	double elapsedTime = duration.count();

	// sets the elapsed time
	this -> elapsedTime = elapsedTime;
	// sets the number of operations
	this -> numOperations = numOperations;

	return closestPair;
}


std::tuple<Pair*, double> Ensemble::recurse (const std::vector<Point*>& Px) const
/*

Synopsis:
Applies the 1D Divide and Conquer Algorithm to find the closest pair.
If the partition P is small enough, the method uses Brute Force to find
the closest pair. Otherwise, the method divides the partition P into
left and right partitions to look for the closest pair in each. Note
that the division step continues until the partitions are small enough
to use Brute Force (or the direct method). Then, the method combines
the solutions (from the left and right partitions) by stripping pairs
too distant to be considered closest pair candidates. The pairs that
remain are considered by the overloaded Brute Force Algorithm which
considers pairs between the left and right partitions.

The method returns a tuple containing the closest pair and the
number of operations (distance computations) invested to find the
closest pair.

Input:
Px		x-y sorted coordinates of the particles

Output:
tuple		the closest pair and the number of operations

*/
{

	// we need a handler to handle the memory of closest pair candidates
	Handler<Pair*> handler;

	if (Px.size() < 3)
	{
		// uses brute force on the smaller partition
		return ( this -> distance(Px) );
	}
	else
	{
		const auto b = Px.begin();			// begin
		const auto m = Px.begin() + (Px.size() / 2);	// middle
		const auto e = Px.end();			// end
		// divides dataset into left and right partitions
		std::vector<Point*> Lx(b, m), Rx(m, e);

		Pair* closestPairLeft;
		double numOperationsLeft;
		// finds the closest pair in the left partition
		std::tuple<Pair*, double> dataLeft = this -> recurse(Lx);
		std::tie(closestPairLeft, numOperationsLeft) = dataLeft;


		Pair* closestPairRight;
		double numOperationsRight;
		// finds the closest pair in the right partition
		std::tuple<Pair*, double> dataRight = this -> recurse(Rx);
		std::tie(closestPairRight, numOperationsRight) = dataRight;


		Pair *closestPair;
		// selects the closest pair from the two partitions
		if ( closestPairLeft -> compareTo(closestPairRight) < 0 )
		{
			// adds larger to the handler
			handler.add(closestPairRight);
			closestPair = closestPairLeft;
		}
		else
		{
			// adds larger to the handler
			handler.add(closestPairLeft);
			closestPair = closestPairRight;
		}

		// erases the unneeded larger pair object from memory
		handler.erase();


		// combines the left and right partitions
		std::tuple<Pair*, double> data = this -> combine(Lx, Rx, closestPair);

		double numOperations;
		// gets the closest pair and the number of operations
		std::tie(closestPair, numOperations) = data;

		// updates the number of operations
		numOperations += (numOperationsLeft + numOperationsRight);

		return std::make_tuple(closestPair, numOperations);
	}
}


std::tuple<Pair*, double> Ensemble::distance (const std::vector<Point*>& points) const
/*

Synopsis:
Applies the Brute Force Algorithm to find the closest pair in a
partition. Note that the partition could be the whole dataset.

Input:
part		partition (or whole data set of points)

Outputs:
tuple		the closest pair and the number of operations

*/
{
	// initializes the closest pair
	Pair *closestPair = new Pair();
	// considers all the distinct pairs to find the closest pair
	for (std::vector<Point*>::size_type i = 0; i != (points.size() - 1); ++i)
	{
		for (std::vector<Point*>::size_type j = (i + 1); j != points.size(); ++j)
		{
			Point *p = points[i], *q = points[j];
			double d = point::distance(p, q);
			Pair pair(p, q, d);

			if ( pair.compareTo(closestPair) < 0 )
			// updates the closest pair
			{
				closestPair -> copy(&pair);
			}
		}
	}

	double N = points.size();
	double numOperations = (N * (N - 1) / 2);

	return std::make_tuple(closestPair, numOperations);
}


std::tuple<Pair*, double> Ensemble::distance (
	const std::vector<Point*>& L, const std::vector<Point*>& R, Pair* closestPair
) const
/*

Synopsis:
Applies Brute Force Algorithm on the middle partition M. Note
that the middle partition is comprised by closest pair candidates
from the left and right partitions; it is not constructed explicitly.

Inputs:
L		closest pair candidates in left partition
R		closest pair candidates right partition
closestPair	current closest pair

Outputs:
tuple		the closest pair and the number of operations

*/
{
	for (Point* p : L)
	{
		for (Point* q : R)
		{
			double d = point::distance(p, q);
			Pair pair(p, q, d);

			if ( pair.compareTo(closestPair) < 0 )
			// updates the closest pair
			{
				closestPair -> copy(&pair);
			}
		}
	}

	// note: includes the distance computations in combine()
	double N1 = L.size(), N2 = R.size();
	double numOperations = ( (N1 * N2) + (N1 + 1) + (N2 + 1) );

	return std::make_tuple(closestPair, numOperations);
}


std::tuple<Pair*, double> Ensemble::combine (
	const std::vector<Point*>& L, const std::vector<Point*>& R, Pair* closestPair
) const
/*

Synopsis:
Looks for the closest pair at the interface of the left and right partitions
(dubbed as the middle partition M).

Inputs:
L			left partition
R			right partition
closestPair		current closest pair

Output:
tuple			the closest pair and the number of operations


COMMENTS:
Even though are refering in the comments of this method to the distance along the x axis,
we are really computing the squared (x2 - x1)^2 because we are comparing that quantity
against the squared distance of the current closest pair

			d^2 = (x2 - x1)^2 + (y2 - y1)^2

to determine if the considered pair of points could be closer.

*/
{
	double d_min = closestPair -> getDistance();
	// assumes no points in the right partition could comprise a closest pair
	size_t b2 = 0, e2 = 0;
	for (Point* Q : R)
	// includes points in the right partition comprising closest pair candidates
	{
		size_t last = (L.size() - 1);
		// gets the rightmost point in the left partition
		Point* P = L[last];

		// computes the x-axis distance of the closest pair candidate
		double x1 = P -> getX(), x2 = Q -> getX();
		double d = (x2 - x1) * (x2 - x1);

		if (d < d_min)
		// includes the point because it could be the closest pair
		{
			++e2;
		}
		else
		// halts the search because the next points are even farther apart
		{
			break;
		}
	}


	// assumes no points in the left partition could comprise a closest pair
	size_t b1 = L.size(), e1 = L.size();
	for (std::vector<Point*>::size_type i = 0; i != L.size(); ++i)
	// includes points in the left partition comprising closest pair candidates
	// Note: traverses the container from back to front owing to the x-y sorting
	{
		// gets index of the (current) rightmost point in the left partition
		int j = L.size() - (i + 1);

		Point* P = L[j];	// gets rightmost point in the left partition
		Point* Q = R[0];	// gets leftmost point in the right partition

		// computes the x-axis distance of the closest pair candidate
		double x1 = P -> getX(), x2 = Q -> getX();
		double d = (x2 - x1) * (x2 - x1);

		if (d < d_min)
		// includes the point because it could be the closest pair
		{
			--b1;
		}
		else
		// halts the search because the next points are even farther apart
		{
			break;
		}
	}

	// strips (implicitly) points in the left too distant to comprise the closest pair
	const auto beginIteratorLeft = L.end() - (e1 - b1);
	const auto endIteratorLeft = L.end();
	// creates the smaller left partition stripped from too distant points
	std::vector<Point*> smallerL(beginIteratorLeft, endIteratorLeft);

	// strips points in the right partition too distant to comprise the closest pair
	const auto beginIteratorRight = R.begin();
	const auto endIteratorRight = R.begin() + (e2 - b2);
	// creates the smaller right partition stripped from too distant points
	std::vector<Point*> smallerR(beginIteratorRight, endIteratorRight);

	return ( this -> distance(smallerL, smallerR, closestPair) );
}


	/* utilities */


void Ensemble::createDataset1D (std::vector<Point*>& dataset)
/*

Synopsis:
Creates a dataset of points that has no duplicate closest pairs;
that is, the second closest pair is farther away than the first
closest pair. This version invokes the method that creates points
uniformly distributed in a rectangular domain; that is, the
range of possible y-axis coordinates is fixed.

Input:
None

Output:
dataset		dataset of points with a unique closest pair

*/
{
	// creates a trial dataset of points
	this -> create(dataset);

	bool hasDuplicateClosestPair = true;
	while (hasDuplicateClosestPair)
	// creates a new dataset until there are no duplicates
	{
		try
		{
			// checks for duplicated closest pairs
			this -> hasDuplicateClosestPair(dataset);
			hasDuplicateClosestPair = false;
		}
		catch (DuplicateClosestPairException& e)
		{
			// creates a new dataset
			this -> create(dataset);
		}
	}
}


void Ensemble::hasDuplicateClosestPair (const std::vector<Point*>& points) const
/*

Synopsis:
Uses the Brute Force Algorithm to find the first and the second
closest pairs. Throws an exception if their distances are equal.

Input:
points		dataset of distinct points

Output:
None

*/
{
	// initializes the first closest pair
	Pair firstClosestPair;
	// initializes the second closest pair
	Pair secondClosestPair;
	// uses Brute Force to find the first and second closest pairs
	for (std::vector<Point*>::size_type i = 0; i != (points.size() - 1); ++i)
	{
		for (std::vector<Point*>::size_type j = (i + 1); j != points.size(); ++j)
		{
			Point *p = points[i], *q = points[j];
			double d = point::distance(p, q);
			Pair pair(p, q, d);

			if ( pair.compareTo(&firstClosestPair) <= 0 )
			// updates the first and second closest pairs
			{
				secondClosestPair.copy(&firstClosestPair);
				firstClosestPair.copy(&pair);
			}
		}
	}

	double d_2nd = secondClosestPair.getDistance();
	double d_min = firstClosestPair.getDistance();
	if (d_2nd == d_min)
	{
		// complains if the closest pairs have equal distances
		std::string err = ("duplicateClosestPairException");
		throw DuplicateClosestPairException(err);
	}
}


void Ensemble::isInvalidData (const std::vector<Point*>& data) const
/*

Synopsis:
Complains if the dataset of points is not x-y sorted or if it has duplicates.

Input:
data		dataset of points

Ouput:
None

*/
{

	if ( !( this -> isSorted(data) ) )
	// complains if the points are not x-y sorted
	{
		std::string err = ("points must be x-y sorted");
		throw std::invalid_argument(err);
	}


	if ( this -> hasDuplicates(data) )
	// complains if there are duplicated points
	{
		std::string err = ("points must be distinct");
		throw std::invalid_argument(err);
	}
}


bool Ensemble::isSorted (const std::vector<Point*>& points) const
/*

Synopsis:
Returns true if the vector contains duplicate points.

Input:
points		dataset of points

Output:
isSorted	true if x-y sorted, false otherwise

*/
{

	int misplacements = 0;
	for (std::vector<Point*>::size_type i = 0; i != (points.size() - 1); ++i)
	// gets the number of misplaced (or out-of-order) points
	{
		Point *p = points[i], *q = points[i + 1];

		if ( p -> compareTo(q) > 0 )
			misplacements += 1;
		else
			misplacements += 0;
	}

	if (misplacements != 0)
		return false;
	else
		return true;
}


bool Ensemble::hasDuplicates (const std::vector<Point*>& dataset) const
/*

Synopsis:
Returns true if the vector contains duplicate points.

Input:
dataset		dataset of points

Output:
hasDuplicates	true if there are duplicates, false otherwise

*/
{

	// contructs a copy of the dataset to keep it unchanged
	std::vector<Point*> points(dataset);

	// sorts to check for duplicates in pairs
	std::sort(points.begin(), points.end(), point::compare);

	int duplicates = 0;
	for (std::vector<Point*>::size_type i = 0; i != (points.size() - 1); ++i)
	// obtains the total number of duplicates
	{
		Point *p = points[i], *q = points[i + 1];

		if ( p -> equalTo(q) )
			duplicates += 1;
		else
			duplicates += 0;
	}

	if (duplicates != 0)
		return true;
	else
		return false;
}


void Ensemble::create (std::vector<Point*>& points)
/*

Synopsis:
Generates a distinct dataset of Cartesian points by sampling values from the
uniform Pseudo-Random Number Generator PRNG utilities.

Inputs:
None

Output:
points		a vector that stores the dataset of points

*/
{

	/*

	clean up:
	This method may be called a couple of times until the dataset
	of points has no duplicate closest pairs. Thus, it is necessary
	to clean up the containers and delete from memory existing
	point objects. This is done in the following lines of code.

	*/


	// erases existing point objects from the handler
	this -> handler.erase();


	// clears references to existing point objects in the container
	points.clear();


	// implements method:


	// gets the ensemble size
	size_t size = (this -> size);
	// defines limiting values for the x, y coordinates
	double xlim = (size * size);
	double ylim = 8;


	// creates a Pseudo Random Number Generator PRNG for seeding other PRNGs
	std::random_device randev;
	// creates a random-engine seeded from the random device
	std::default_random_engine engine( randev() );
	// creates Pseudo Random Number Generators PRNGs for the x, y coordinates
	std::uniform_real_distribution<double> xRand(-xlim, xlim);
	std::uniform_real_distribution<double> yRand(-ylim, ylim);
	for (std::vector<Point*>::size_type i = 0; i != size; ++i)
	// adds distinct point objects to the handler
	{
		/*

		Note:
		Creates a point from the integral parts of x, y so that we can
		represent exactly in binary the squared distance of the points

		*/

		double x = floor( xRand(engine) );
		double y = floor( yRand(engine) );
		Point point(x, y);

		while ( this -> handler.contains(&point) )
		// discards duplicates until a distinct point is found,
		// uses a linear search to determine if the point is
		// not present in the handler
		{
			// erases the duplicate point object `point'
			handler.erase();

			// creates a new point object
			double x = floor( xRand(engine) );
			double y = floor( yRand(engine) );
			Point p(x, y);
			point.copy(&p);
		}

		// creates a copy of the distinct point object via the copy constructor
		Point *object = new Point(point);
		// adds the distinct point object to class handler
		this -> handler.add(object);
	}

	// creates the dataset of distinct points by copying the references
	// to the point objects contained in the class handler into a vector
	const std::list<Point*>& objects = this -> handler.getObjects();
	std::copy(objects.begin(), objects.end(), std::back_inserter(points) );

	// sorts the dataset to support the 1D Divide And Conquer Algorithm
	std::sort(points.begin(), points.end(), point::compare);
}
