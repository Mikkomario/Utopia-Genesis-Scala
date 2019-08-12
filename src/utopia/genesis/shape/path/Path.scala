package utopia.genesis.shape.path

import utopia.genesis.animation.Animation

/**
  * Paths form a sequence of points. They have a specified start and end point
  * @author Mikko Hilpinen
  * @since 19.6.2019, v2.1+
  */
trait Path[+P] extends Animation[P]
{
	// ABSTRACT	----------------
	
	/**
	  * @return The starting point of this path
	  */
	def start: P
	/**
	  * @return The end point of this path
	  */
	def end: P
	/**
	  * @return The (approximate) length of this path
	  */
	def length: Double
	
	
	// IMPLEMENTED	--------------
	
	/**
	  * Calculates a point in this path
	  * @param t The progress along this path [0, 1] where 0 is at the start of this path and 1 is at the end
	  * @return A point within this path (provided 0 <= t <= 1)
	  */
	override def apply(t: Double): P
	
	
	// OPERATORS	-------------
	
	/**
	  * Continues this path with another
	  * @param another Another path
	  * @tparam B The type of resulting path
	  * @return A path that starts with this path and continues with the another
	  */
	def +[B >: P](another: Path[B]) = CompoundPath(Vector(this, another))
	
	
	// OTHER	-----------------
	
	/**
	  * Maps the values of this path
	  * @param f A mapping function
	  * @tparam A Type of map function input
	  * @tparam B Type of map function output
	  * @return A mapped version of this path
	  */
	def map[A >: P, B](f: A => B) = MappedPath[A, B](this, f)
}
