package utopia.genesis.event

import utopia.genesis.util.Vector3D
import utopia.inception.event.Event
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.GenesisValue
import utopia.inception.event.ConditionEventFilter
import utopia.inception.event.EventFilter

object MouseMoveEvent
{
    // ATTRIBUTES    ----------------
    
    val previousPositionAttName = "position_previous"
    val mousePositionAttName = "position"
    val durationMillisAttName = "duration"
    
    
    // OTHER METHODS    ------------
    
    /**
     * Creates an event filter that only accepts mouse events originating from the specified area
     * @param containment a function that determines whether a position resides within a specific
     * area
     */
    def overAreaFilter(containment: Vector3D => Boolean) = new EventFilter[MouseMoveEvent](
            { _.isOverArea(containment) });
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse entering 
     * the specified area
     * @param containment a function that determines whether a position resides within a specific
     * area
     */
    def enterAreaFilter(containment: Vector3D => Boolean) = new EventFilter[MouseMoveEvent](
            { _.enteredArea(containment)});
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse exiting the
     * specified area
     * @param containment a function that determines whether a position resides within a specific
     * area
     */
    def exitedAreaFilter(containment: Vector3D => Boolean) = new EventFilter[MouseMoveEvent](
            { _.exitedArea(containment) });
    
    /**
     * Creates an event filter that only accepts events where the velocity the mouse cursor moved 
     * was large enough
     * @param minVelocity The minimum required velocity for the mouse cursor, pixels per
     * millisecond.
     */
    def minVelocityFilter(minVelocity: Double) = new EventFilter[MouseMoveEvent](
            { _.velocity.length > minVelocity });
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
    
    
    // COMPUTED PROPERTIES    -----------
    
    /**
     * The movement vector for the mouse cursor for the duration of the event
     */
    def transition = mousePosition - previousMousePosition
    
    /**
     * The velocity vector for the mouse cursor in pixels per millisecond
     */
    def velocity = transition / durationMillis
    
    
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