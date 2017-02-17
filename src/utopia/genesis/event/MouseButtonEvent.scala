package utopia.genesis.event

import utopia.genesis.event.MouseButton._

/**
 * Mouse button events are generated whenever a mouse button state changes
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
    
    def button = 
    
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