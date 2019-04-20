package utopia.genesis.event

import java.awt.event.KeyEvent
import utopia.inception.util.Filter

object KeyStateEvent
{
    /**
     * This event filter only accepts events caused by key presses
     */
    val wasPressedFilter: Filter[KeyStateEvent] = e => e.isDown
    
    /**
     * This event filter only accepts events caused by key releases
     */
    val wasReleasedFilter: Filter[KeyStateEvent] = e => e.isReleased
    
    /**
     * This event filter only accepts events for the specified key index
     */
    def keyFilter(index: Int): Filter[KeyStateEvent] = e => e.index == index
    
    /**
     * This event filter only accepts events for the specified key index + location
     */
    def keyFilter(index: Int, location: KeyLocation): Filter[KeyStateEvent] = e => e.index == index && e.location == location
    
    /**
     * This event filter only accepts events for the specified character key
     */
    def charFilter(char: Char): Filter[KeyStateEvent] = e => e.isCharacter(char)
    
    /**
      * @param acceptedChars Characters that are accepted by the filter
      * @return Event filter that only accepts events concerning specified characters
      */
    def charsFilter(acceptedChars: Seq[Char]): Filter[KeyStateEvent] = e => acceptedChars.exists(e.isCharacter)
    
    /**
     * This event filter only accepts events for the specified key indices
     */
    def keysFilter(firstIndex: Int, secondIndex: Int, moreIndices: Int*): Filter[KeyStateEvent] =
        keysFilter(Vector(firstIndex, secondIndex) ++ moreIndices)
    
    /**
      * @param acceptedKeys Keys that are accepted by the filter
      * @return Event filter that only accepts events concerning specified keys
      */
    def keysFilter(acceptedKeys: Seq[Int]): Filter[KeyStateEvent] = e => acceptedKeys.contains(e.index)
}

/**
 * This event is used for informing instances when a pressed state changes for a single keyboard 
 * button
 * @author Mikko Hilpinen
 * @since 21.2.2017
 */
case class KeyStateEvent(index: Int, location: KeyLocation, isDown: Boolean, keyStatus: KeyStatus)
{
    /**
     * Checks whether the event concerns a specific character key
     */
    def isCharacter(char: Char) = index == KeyEvent.getExtendedKeyCodeForChar(char)
    
    /**
      * @return Whether the key was just released
      */
    def isReleased = !isDown
}