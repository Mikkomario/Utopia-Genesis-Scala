package utopia.genesis.event

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
    
    /**
     * Whether this event concerns the left mouse button
     */
    def isLeftMouseButton = buttonIndex == 1
    
    /**
     * Whether this event concerns the right mouse button
     */
    def isRightMouseButton = buttonIndex == 3
    
    /**
     * Whether this event concerns the middle mouse button
     */
    def isMiddleMouseButton = buttonIndex == 2
}