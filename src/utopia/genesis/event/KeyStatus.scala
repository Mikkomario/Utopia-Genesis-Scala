package utopia.genesis.event

import scala.collection.immutable.HashMap
import java.awt.event.KeyEvent

/**
 * This immutable class is used for keeping record of keyboard button states. The class has value
 * semantics. This class uses the java.awt.KeyEvent enhanced key codes as indices.
 * @author Mikko Hilpinen
 * @since 21.2.2017
 */
class KeyStatus(private val status: Map[Int, Map[KeyLocation, Boolean]] = 
        HashMap[Int, Map[KeyLocation, Boolean]]())
{
    // COMPUTED PROPERTIES    -------
    
    /**
     * Whether the left arrow key is down
     */
    def left = apply(KeyEvent.VK_LEFT)
    
    /**
     * Whether the right arrow key is down
     */
    def right = apply(KeyEvent.VK_RIGHT)
    
    /**
     * Whether the up arrow key is down
     */
    def up = apply(KeyEvent.VK_UP)
    
    /**
     * Whether the down arrow key is down
     */
    def down = apply(KeyEvent.VK_DOWN)
    
    /**
     * Whether the space bar is down
     */
    def space = apply(KeyEvent.VK_SPACE)
    
    
    // OPERATORS    -----------------
    
    /**
     * Checks the status of a single key index. Returns if the key is down /
     * pressed at any key location.
     * @return whether a key matching the index is down at any location
     */
    def apply(index: Int) = status.get(index).exists { _.exists { _._2 } }
    
    /**
     * Checks the status of a single key at a specific key location
     * @return Whether the key with the specified index and location is currently down / pressed
     */
    def apply(index: Int, location: KeyLocation) = status.get(index).exists { _.getOrElse(location, false) }
    
    /**
     * Checks the status of a key indicated by a specific character
     * @return Whether a key indicated by the character is currently down / pressed
     */
    def apply(char: Char): Boolean = apply(KeyEvent.getExtendedKeyCodeForChar(char))
    
    /**
     * Changes the status of a single button in a single location
     * @param status The index of the button, the location of the button and the new status of the
     * button
     * @return An updated key status instance
     */
    def +(status: Tuple3[Int, KeyLocation, Boolean]) = 
    {
        val oldButtonStatus = this.status.getOrElse(status._1, HashMap[KeyLocation, Boolean]())
        val newButtonStatus = oldButtonStatus + (status._2 -> status._3)
        
        new KeyStatus(this.status + (status._1 -> newButtonStatus))
    }
}