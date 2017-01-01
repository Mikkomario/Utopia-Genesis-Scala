package utopia.genesis.util

import utopia.genesis.util.Extensions._

object Line
{
    /**
     * Creates a new line from position and vector combo
     * @param position The starting position of the line
     * @param vector The vector portion of the line
     * @return A line with the provided position and vector part
     */
    def ofVector(position: Vector3D, vector: Vector3D) = Line(position, position + vector)    
}

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
    
    
    // COMPUTED PROPERTIES    ----------
    
    /**
     * This line with inverted direction
     */
    def inverted = Line(end, start)
    
    
    // OPERATORS    --------------------
    
    /**
     * Checks if the two lines are practically (approximately) identical
     */
    def ~==(other: Line) = (start ~== other.start) && (end ~== other.end)
    
    /**
     * Finds a position on this line
     * @param t The length parameter. t = 1 will result in the end point of the line. If t is 
     * between 0 and 1, the resulting point will lie on the line segment
     * @return a point along the line
     */
    def apply(t: Double) = start + vector * t
    
    def intersection(other: Line) = 
    {
        // a (V1 x V2) = (P2 - P1) x V2
        // Where P is the start point and Vs are the vector parts
        val leftVector = vector cross other.vector
        val rightVector = (other.start - start) cross other.vector
        
        // If the left hand side vector is a zero vector, there is no collision
        // The two vectors must also be parallel
        if ((leftVector ~== Vector3D.zero) || !(leftVector isParallelWith rightVector))
        {
            None
        }
        else
        {
            // a = |right| / |left|, negative if they have opposite directions
            val a = if (leftVector.directionRads ~== rightVector.directionRads) 
                rightVector.length / leftVector.length else -rightVector.length / leftVector.length;
            
            Some(apply(a))
        }
    }
}