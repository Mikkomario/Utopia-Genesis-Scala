package utopia.genesis.view

import java.awt.Window
import java.awt.event.KeyListener
import java.awt.event.KeyEvent

/**
 * This class listens to key events within a specific window and generates keyboard events based on
 * the java.awt counterparts
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
class WindowKeyEventGenerator(window: Window)
{
    // NESTED CLASSES    -----------------
    
    private class KeyEventReceiver extends KeyListener
    {
        override def keyPressed(e: KeyEvent) = Unit
        override def keyReleased(e: KeyEvent) = Unit
        override def keyTyped(e: KeyEvent) = Unit
    }
}