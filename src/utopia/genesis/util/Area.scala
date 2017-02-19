package utopia.genesis.util

/**
 * Areas are able to specify whether they contain a specific coordinate point
 * @author Mikko Hilpinen
 * @since 19.2.2017
 */
trait Area
{
    /**
     * Whether a set of 3D coordinates lies within this specific area
     */
    def contains(point: Vector3D): Boolean
    
    /**
     * Whether a point lies within this area when both are projected to 2D (x-y) plane
     */
    def contains2D(point: Vector3D): Boolean
}