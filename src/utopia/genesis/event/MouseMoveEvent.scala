package utopia.genesis.event

import utopia.genesis.util.Vector3D
import utopia.inception.event.Event
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.GenesisValue

object MouseMoveEvent
{
    val previousPositionAttName = "position_previous"
    val mousePositionAttName = "position"
    val durationMillisAttName = "duration"
}

/**
 * These events are generated when the mouse cursor moves
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
class MouseMoveEvent(val mousePosition: Vector3D, 
        val previousMousePosition: Vector3D, val durationMillis: Double) extends Event
{
    // ATTRIBUTES    --------------------
    
    override lazy val identifiers = Model(Vector(
            (MouseMoveEvent.previousPositionAttName, GenesisValue of previousMousePosition), 
            (MouseMoveEvent.mousePositionAttName, GenesisValue of mousePosition), 
            (MouseMoveEvent.durationMillisAttName, Value of durationMillis)));
    
    
    // OTHER METHODS    -----------------
    
    /**
     * Checks whether the mouse position is currently over a specified area
     * @param containment The function that defines whether a specific point is within an area or
     * not
     */
    def isOverArea(containment: Vector3D => Boolean) = containment(mousePosition)
    
    /**
     * Checks whether the mouse position was previously over a specified area
     * @param containment The function that defines whether a specific point is within an area or
     * not
     */
    def wasOverArea(containment: Vector3D => Boolean) = containment(previousMousePosition)
    
    /**
     * Checks whether the mouse cursor just entered a specified area
     * @param containment The function that defines whether a specific point is within an area or
     * not
     */
    def enteredArea(containment: Vector3D => Boolean) = !wasOverArea(containment) && isOverArea(containment)
    
    /**
     * Checks whether the mouse cursor just exited a specified area
     * @param containment The function that defines whether a specific point is within an area or
     * not
     */
    def exitedArea(containment: Vector3D => Boolean) = wasOverArea(containment) && !isOverArea(containment)
}