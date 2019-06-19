package utopia.genesis.shape

/**
  * Paths form a sequence of points. They have a specified start and end point
  * @author Mikko Hilpinen
  * @since 19.6.2019, v2.1+
  */
trait Path[+P]
{
	/**
	  * @return The starting point of this path
	  */
	def start: P
	/**
	  * @return The end point of this path
	  */
	def end: P
	/**
	  * Calculates a point in this path
	  * @param t The progress along this path [0, 1] where 0 is at the start of this path and 1 is at the end
	  * @return A point within this path (provided 0 <= t <= 1)
	  */
	def apply(t: Double): P
}
