package utopia.genesis.event

import utopia.genesis.event.MouseButton._
import utopia.inception.util.Filter
import utopia.genesis.shape.shape2D.Point

object MouseButtonStateEvent
{
    /**
     * This filter only accepts button events where the mouse button was just pressed down
     */
    val wasPressedFilter: Filter[MouseButtonStateEvent] = e => e.wasPressed
    
    /**
     * This filter only accepts button events where the mouse button was just released from down
     * state
     */
    val wasReleasedFilter: Filter[MouseButtonStateEvent] = e => e.wasReleased
    
    /**
      * This filter only accepts events that haven't been marked as consumed
      */
    val notConsumedFilter: Filter[MouseButtonStateEvent] = e => !e.isConsumed
    
    /**
     * This filter only accepts button events for the specific button index
     */
    def buttonFilter(buttonIndex: Int): Filter[MouseButtonStateEvent] = e => e.buttonIndex == buttonIndex
    
    /**
     * This filter only accepts button events for the specific mouse button
     */
    def buttonFilter(button: MouseButton): Filter[MouseButtonStateEvent] = buttonFilter(button.buttonIndex)
}

/**
 * Mouse button events are generated whenever a mouse button state changes (pressed and released)
  * @param buttonIndex Target button index
  * @param isDown Whether target button is down
  * @param mousePosition Current mouse position
  * @param buttonStatus Current mouse button status
 * @author Mikko Hilpinen
 * @since 17.2.2017
 */
case class MouseButtonStateEvent(buttonIndex: Int, isDown: Boolean, mousePosition: Point,
                                 buttonStatus: MouseButtonStatus, isConsumed: Boolean) extends MouseEvent
{
    // COMPUTED PROPERTIES    ------------
    
    /**
      * @return Whether target mouse button is currently released / up
      */
    def isUp = !isDown
    
    /**
     * Whether the mouse button was just pressed down
     */
    def wasPressed = isDown
    
    /**
     * Whether the mouse button was just released from down state
     */
    def wasReleased = !isDown
    
    /**
     * The basic mouse button for this event or None if the event concerns a special button
     */
    def button = MouseButton.forIndex(buttonIndex)
    
    /**
     * Whether this event concerns the left mouse button
     */
    def isLeftMouseButton = isMouseButton(Left)
    
    /**
     * Whether this event concerns the right mouse button
     */
    def isRightMouseButton = isMouseButton(Right)
    
    /**
     * Whether this event concerns the middle mouse button
     */
    def isMiddleMouseButton = isMouseButton(Middle)
    
    /**
      * @return A consumed version of this event
      */
    def consumed = if (isConsumed) this else copy(isConsumed = true)
    
    
    // OTHER METHODS    ------------------
    
    /**
     * Checks whether this mouse event concerns the specified mouse button
     */
    def isMouseButton(button: MouseButton) = buttonIndex == button.buttonIndex
}