package utopia.genesis.event

import utopia.flow.util.TimeExtensions._

import utopia.genesis.shape.Vector3D
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.GenesisValue
import utopia.inception.util.Filter
import utopia.genesis.shape.template.Area
import utopia.genesis.shape.shape2D.Point
import java.time.Duration
import utopia.genesis.shape.shape2D.Area2D

object MouseMoveEvent
{
    // OTHER METHODS    ------------
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse entering 
     * the specified area
     */
    def enterAreaFilter(area: Area2D): Filter[MouseMoveEvent] = e => e.enteredArea(area)
    
    /**
     * Creates an event filter that only accepts mouse events originating from the mouse exiting the
     * specified area
     */
    def exitedAreaFilter(area: Area2D): Filter[MouseMoveEvent] = e => e.exitedArea(area)
    
    /**
     * Creates an event filter that only accepts events where the mouse cursor moved with enough
     * speed
     */
    def minVelocityFilter(minVelocity: Double): Filter[MouseMoveEvent] = e => e.velocity.length >= minVelocity
}

/**
 * These events are generated when the mouse cursor moves
  * @param mousePosition The current mouse position
  * @param previousMousePosition The previous mouse position
  * @param buttonStatus Current mouse button status
  * @param duration The duration of the event (similar to act(...))
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
case class MouseMoveEvent(mousePosition: Point, previousMousePosition: Point, buttonStatus: MouseButtonStatus,
                          duration: Duration) extends MouseEvent
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
    
    /**
     * The duration of this event in duration format
     */
    def durationMillis = duration.toPreciseMillis
    
    
    // OTHER METHODS    -----------------
    
    /**
     * Checks whether the mouse position was previously over a specified area
     */
    def wasOverArea(area: Area2D) = area.contains(previousMousePosition)
    
    /**
     * Checks whether the mouse cursor just entered a specified area
     */
    def enteredArea(area: Area2D) = !wasOverArea(area) && isOverArea(area)
    
    /**
     * Checks whether the mouse cursor just exited a specified area
     */
    def exitedArea(area: Area2D) = wasOverArea(area) && !isOverArea(area)
}