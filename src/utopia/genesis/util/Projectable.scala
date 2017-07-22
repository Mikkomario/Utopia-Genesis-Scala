package utopia.genesis.util

import utopia.genesis.util.Extensions._

/**
 * This trait is extended by shapes that can be projected over a specified axis
 * @author Mikko Hilpinen
 * @since 18.7.2017
 */
trait Projectable
{
    // ABSTRACT METHODS    --------------------
    
    /**
     * Projects this shape, creating a line parallel to the provided axis
     */
    def projectedOver(axis: Vector3D): Line
    
    
    // OTHER METHODS    -----------------------
    
    /**
    * Calculates if / how much the projections of the two shapes overlap on the specified axis
    * @param other the other projectable shape
    * @param axis the axis along which the overlap is checked
    * @return the mtv for the specified axis, if there is overlap
    */
    def projectionOverlapWith(other: Projectable, axis: Vector3D) = 
    {
        val projection = projectedOver(axis)
        val otherProjection = other.projectedOver(axis)
        
        if (projection.end <= otherProjection.start || projection.start >= otherProjection.end)
        {
            None
        }
        else 
        {
            val forwardsMtv = otherProjection.end - projection.start
            val backwardsMtv = otherProjection.start - projection.end
            
            if (forwardsMtv.length < backwardsMtv.length) Some(forwardsMtv) else Some(backwardsMtv)
        }
    }
    
    /**
     * Calculates the minimum translation vector that would get the two projectable shapes out of 
     * a collision situation
     * @param other the other projectable instance
     * @param axes the axes along which the collision is checked
     * @return The minimum translation vector that gets the two shapes out of a collision situation 
     * or none if there is no collision
     */
    def collisionMtvWith(other: Projectable, axes: Iterable[Vector3D]) = 
    {
        // If there is collision, there must be overlap on each axis
        val mtvs = axes.mapOrFail { projectionOverlapWith(other, _) }
        
        if (mtvs.isDefined && !mtvs.get.isEmpty)
        {
            // Finds the smallest possible translation vector
            Some(mtvs.get.minBy { _.length })
        }
        else
        {
            None
        }
    }
}