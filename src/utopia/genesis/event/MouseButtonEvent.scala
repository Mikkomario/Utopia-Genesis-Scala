package utopia.genesis.event

import utopia.genesis.event.MouseButton._
import utopia.inception.util.Filter

object MouseButtonEvent
{
    /**
     * This filter only accepts button events where the mouse button was just pressed down
     */
    val wasPressedFilter = new Filter[MouseButtonEvent]({ _.wasPressed })
    
    /**
     * This filter only accepts button events where the mouse button was just released from down
     * state
     */
    val wasReleasedFilter = new Filter[MouseButtonEvent]({ _.wasReleased })
    
    /**
     * This filter only accepts button events for the specific button index
     */
    def mouseButtonFilter(buttonIndex: Int) = new Filter[MouseButtonEvent](
            { _.buttonIndex == buttonIndex });
    
    /**
     * This filter only accepts button events for the specific mouse button
     */
    def mouseButtonFilter(button: MouseButton): Filter[MouseButtonEvent] = mouseButtonFilter(button.buttonIndex)
}

/**
 * Mouse button events are generated whenever a mouse button state changes (pressed and released)
 * @author Mikko Hilpinen
 * @since 17.2.2017
 */
class MouseButtonEvent(val buttonIndex: Int, val isDown: Boolean, val wasDown: Boolean, 
        val durationMillis: Double)
{
    // COMPUTED PROPERTIES    ------------
    
    /**
     * Whether the mouse button was just pressed down
     */
    def wasPressed = isDown && !wasDown
    
    /**
     * Whether the mouse button was just released from down state
     */
    def wasReleased = !isDown && wasDown
    
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
    
    
    // OTHER METHODS    ------------------
    
    /**
     * Checks whether this mouse event concerns the specified mouse button
     */
    def isMouseButton(button: MouseButton) = buttonIndex == button.buttonIndex
}