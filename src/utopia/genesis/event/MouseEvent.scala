package utopia.genesis.event

/**
 * This object holds the different values for mouse buttons that can generate events
 */
object MouseButton 
{
    /**
     * The left mouse button
     */
    val left = 1
    /**
     * The right mouse button
     */
    val right = 2
    /**
     * The middle mouse button
     */
    val middle = 3
}

/**
 * This object holds the different values for the roles of events mouse events can have
 */
object MouseEventType
{
    /**
     * Button events are caused by mouse button presses and releases
     */
    val button = 1
    /**
     * Movement events are generated when a mouse's position changes
     */
    val movement = 2
    /**
     * Wheel events are generated when the mouse wheel rotates
     */
    val wheel = 3
}

/**
 * This object holds the different values for the types of mouse button events
 */
object MouseButtonEventType
{
    val pressed = 1
    val released = 2
    val down = 3
}

/**
 * Mouse events are generated through user interaction and informed to various listeners
 * @author Mikko Hilpinen
 * @since 8.1.2017
 */
class MouseEvent
{
    
}