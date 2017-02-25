package utopia.genesis.event

import scala.collection.immutable.HashMap
import utopia.genesis.event.MouseButton._

/**
 * This immutable data collection save the down state of multiple mouse buttons. The class has value
 * semantics.
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
class MouseButtonStatus(private val status: Map[Int, Boolean] = HashMap[Int, Boolean]())
{
    // COMPUTED PROPERTIES    ------
    
    /**
     * Whether the left mouse button is currently being held down
     */
    def isLeftDown = apply(Left)
    
    /**
     * Whether the right mouse button is currently being held down
     */
    def isRightDown = apply(Right)
    
    /**
     * Whether the middle mouse button is currently being held down
     */
    def isMiddleDown = apply(Middle)
    
    
    // OPERATORS    ----------------
    
    /**
     * Retrieves the status of a mouse button with the provided button index
     * @param buttonIndex the index of the button
     * @return whether the button is currently pressed down
     */
    def apply(buttonIndex: Int) = status.getOrElse(buttonIndex, false)
    
    /**
     * Retrieves the status of a mouse button
     * @param button The mouse button the status is for
     * @return whether the button is currently pressed down
     */
    def apply(button: MouseButton): Boolean = apply(button.buttonIndex)
    
    /**
     * Creates a new mouse button status with the specified button status
     * @param newStatus the index of the button and whether the button is currently held down
     * @return new status with the specified status
     */
    def +(newStatus: Tuple2[Int, Boolean]) = if (apply(newStatus._1) == newStatus._2) this else 
            new MouseButtonStatus(status + newStatus);
    
    /**
     * Creates a new mouse button status with the specified button status
     * @param buttonIndex the index of the button
     * @param status The new status for the button
     * @return new status with the specified status
     */
    def +(buttonIndex: Int, status: Boolean): MouseButtonStatus = this + (buttonIndex -> status)
    
    /**
     * Creates a new mouse button status with the specified button status
     * @param button The targeted button
     * @param status whether the button is currently held down
     * @return new status with the specified status
     */
    def +(button: MouseButton, status: Boolean): MouseButtonStatus = this + (button.buttonIndex, status)
}