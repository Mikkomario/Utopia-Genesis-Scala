package utopia.genesis.util

/**
 * A line consists of a start and an end point. Basically it is a vector with a certain position.
 * @author Mikko Hilpinen
 * @since 13.12.2016
 */
case class Line(val start: Vector3D, val end: Vector3D)
{
    // ATTRIBUTES    -------------------
    
    /**
     * The vector portion of this line (position information not included)
     */
    lazy val vector = end - start
    
    
    // OPERATORS    --------------------
    
    /**
     * Checks if the two lines are practically (approximately) identical
     */
    def ~==(other: Line) = (start ~== other.start) && (end ~== other.end)
    
    /**
     * Finds a position on this line <i>t</i> length from the line's starting point
     */
    def apply(t: Double) = start + vector.withLength(t)
}