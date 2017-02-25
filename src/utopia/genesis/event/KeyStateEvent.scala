package utopia.genesis.event

import java.awt.event.KeyEvent
import utopia.inception.util.Filter

object KeyStateEvent
{
    /**
     * This event filter only accepts events caused by key presses
     */
    val wasPressedFilter = new Filter[KeyStateEvent]({ _.isDown })
    
    /**
     * This event filter only accepts events caused by key releases
     */
    val wasReleasedFilter = new Filter[KeyStateEvent]({ !_.isDown })
    
    /**
     * This event filter only accepts events for the specified key index
     */
    def keyFilter(index: Int) = new Filter[KeyStateEvent]({ _.index == index })
    
    /**
     * This event filter only accepts events for the specified key index + location
     */
    def keyFilter(index: Int, location: KeyLocation) = new Filter[KeyStateEvent](event => 
            { event.index == index && event.location == location});
    
    /**
     * This event filter only accepts events for the specified character key
     */
    def keyFilter(char: Char) = new Filter[KeyStateEvent]({ _.isCharacter(char) })
    
    /**
     * This event filter only accepts events for the specified key indices
     */
    def keyFilter(firstIndex: Int, otherIndices: Int*) = new Filter[KeyStateEvent](event => 
            { event.index == firstIndex || otherIndices.contains(event.index) });
}

/**
 * This event is used for informing instances when a pressed state changes for a single keyboard 
 * button
 * @author Mikko Hilpinen
 * @since 21.2.2017
 */
class KeyStateEvent(val index: Int, val location: KeyLocation, val isDown: Boolean, 
        val keyStatus: KeyStatus)
{
    /**
     * Checks whether the event concerns a specific character key
     */
    def isCharacter(char: Char) = index == KeyEvent.getExtendedKeyCodeForChar(char)
}