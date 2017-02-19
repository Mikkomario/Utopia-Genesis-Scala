package utopia.genesis.event

import utopia.genesis.util.Vector3D
import utopia.genesis.util.Area
import utopia.inception.util.Filter
import utopia.genesis.event.MouseButton._

object MouseEvent
{
    /**
     * This filter only accepts mouse events where the left mouse button is pressed down
     */
    val isLeftDownFilter = buttonStatusFilter(Left)
    
    /**
     * This filter only accepts mouse events where the right mouse button is pressed down
     */
    val isRightDownFilter = buttonStatusFilter(Right)
    
    /**
     * This filter only accepts mouse events where the middle mouse button is pressed down
     */
    val isMiddleDownFilter = buttonStatusFilter(Middle)
    
    /**
     * This filter only accepts mouse events where the mouse cursor is over the specified area
     */
    def isOverAreaFilter(area: Area) = new Filter[MouseEvent]({ _.isOverArea(area) })
    
    /**
     * This filter only accepts events where a mouse button with the specified index has the
     * specified status (down (true) or up (false))
     */
    def buttonStatusFilter(buttonIndex: Int, requiredStatus: Boolean) = new Filter[MouseEvent](
            { _.buttonStatus(buttonIndex) == requiredStatus });
    
    /**
     * This filter only accepts events where a mouse button has the
     * specified status (down (true) or up (false))
     * @param requiredStatus The status the button must have in order for the event to be included.
     * Defaults to true (down)
     */
    def buttonStatusFilter(button: MouseButton, requiredStatus: Boolean = true): Filter[MouseEvent] = 
            buttonStatusFilter(button.buttonIndex, requiredStatus);
}

/**
 * This class contains the common properties shared between different mouse event types
 * @author Mikko Hilpinen
 * @since 19.2.2017
 */
class MouseEvent(val mousePosition: Vector3D, val buttonStatus: MouseButtonStatus)
{
    // OTHER METHODS    -----------------
    
    /**
     * Checks whether the mouse cursor is currently over the specified area
     */
    def isOverArea(area: Area) = area.contains2D(mousePosition)
}