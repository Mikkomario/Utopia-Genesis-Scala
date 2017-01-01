package utopia.genesis.util

/**
 * Circles are shapes that are formed by an origin and a radius
 * @author Mikko Hilpinen
 * @since 1.1.2017
 */
case class Circle(val origin: Vector3D, radius: Double)
{
    /**
     * Checks whether the circle contains the provided point
     */
    def contains(point: Vector3D) = (point - origin).length <= radius
    
    /**
     * Checks whether the two circles intersect with each other
     */
    def intersectsWith(other: Circle) = (origin - other.origin).length <= radius + other.radius
}