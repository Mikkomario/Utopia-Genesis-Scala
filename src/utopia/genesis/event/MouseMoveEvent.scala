package utopia.genesis.event

import utopia.genesis.shape.Vector3D
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.GenesisValue
import utopia.inception.util.Filter
import utopia.genesis.shape.template.Area

object MouseMoveEvent
{
    // OTHER METHODS    ------------
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse entering 
     * the specified area
     */
    def enterAreaFilter(area: Area) = new Filter[MouseMoveEvent]({ _.enteredArea(area) });
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse exiting the
     * specified area
     */
    def exitedAreaFilter(area: Area) = new Filter[MouseMoveEvent]({ _.exitedArea(area) });
    
    /**
     * Creates an event filter that only accepts events where the mouse cursor moved with enough
     * speed
     */
    def minVelocityFilter(minVelocity: Double) = new Filter[MouseMoveEvent](
            { _.velocity.length > minVelocity });
}

/**
 * These events are generated when the mouse cursor moves
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
class MouseMoveEvent(mousePosition: Vector3D, val previousMousePosition: Vector3D, 
        buttonStatus: MouseButtonStatus, val durationMillis: Double) extends MouseEvent(
        mousePosition, buttonStatus)
{
    // COMPUTED PROPERTIES    -----------
    
    /**
     * The movement vector for the mouse cursor for the duration of the event
     */
    def transition = mousePosition - previousMousePosition
    
    /**
     * The velocity vector of the mouse cursor, in pixels per millisecond
     */
    def velocity = transition / durationMillis
    
    
    // OTHER METHODS    -----------------
    
    /**
     * Checks whether the mouse position was previously over a specified area
     */
    def wasOverArea(area: Area) = area.contains2D(previousMousePosition)
    
    /**
     * Checks whether the mouse cursor just entered a specified area
     */
    def enteredArea(area: Area) = !wasOverArea(area) && isOverArea(area)
    
    /**
     * Checks whether the mouse cursor just exited a specified area
     */
    def exitedArea(area: Area) = wasOverArea(area) && !isOverArea(area)
}