package utopia.genesis.view

import java.awt.Window
import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import utopia.genesis.event.KeyStatus
import utopia.genesis.event.KeyLocation
import utopia.genesis.event.KeyLocation.Standard
import utopia.genesis.event.KeyStateHandler
import utopia.genesis.event.KeyStateEvent
import utopia.genesis.event.KeyTypedHandler
import utopia.genesis.event.KeyTypedEvent

/**
 * This class listens to key events within a specific window and generates keyboard events based on
 * the java.awt counterparts
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
class WindowKeyEventGenerator(window: Window)
{
    // ATTRIBUTES    ---------------------
    
    /**
     * The handler that informs objects about key state changes
     */
    val keyStateHandler = new KeyStateHandler()
    /**
     * The handler that informs objects about key typed events
     */
    val keyTypedHandler = new KeyTypedHandler()
    
    private var keyStatus = new KeyStatus()
    
    
    // INITIAL CODE    -------------------
    
    // Starts listening to key events inside the window
    window.addKeyListener(new KeyEventReceiver())
    
    
    // NESTED CLASSES    -----------------
    
    private class KeyEventReceiver extends KeyListener
    {
        // IMPLEMENTED METHODS    --------
        
        override def keyPressed(e: KeyEvent) = keyStateChanged(e, true)
        
        override def keyReleased(e: KeyEvent) = keyStateChanged(e, false)
        
        override def keyTyped(e: KeyEvent) = keyTypedHandler.onKeyTyped(
                new KeyTypedEvent(e.getKeyChar, keyStatus));
        
        
        // OTHER METHODS    --------------
        
        private def keyStateChanged(e: KeyEvent, newState: Boolean) = 
        {
            val location = KeyLocation.of(e.getKeyLocation).getOrElse(Standard)
            
            // Only reacts to status changes
            if (keyStatus(e.getExtendedKeyCode, location) != newState)
            {
                keyStatus += (e.getExtendedKeyCode, location, newState)
                keyStateHandler.onKeyState(new KeyStateEvent(e.getExtendedKeyCode, location, 
                        newState, keyStatus));
            }
        }
    }
}