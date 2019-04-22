package utopia.genesis.view

import java.awt.event.{KeyEvent, KeyListener}

import utopia.genesis.event.KeyLocation.Standard
import utopia.genesis.event.{KeyLocation, KeyStateEvent, KeyStatus, KeyTypedEvent}
import utopia.genesis.handling.{KeyStateListener, KeyTypedListener}

/**
  * This key listener converts java.awt.KeyEvents to various Utopia Genesis key events
  * @author Mikko Hilpinen
  * @since 6.4.2019
  */
class ConvertingKeyListener(val keyStateHandler: KeyStateListener, val keyTypedHandler: KeyTypedListener) extends KeyListener
{
	// ATTRIBUTES	------------------
	
	private var _keyStatus = KeyStatus.empty
	
	/**
	  * @return The current key status in this listener
	  */
	def keyStatus = _keyStatus
	
	
	// IMPLEMENTED METHODS    --------
	
	override def keyPressed(e: KeyEvent) = keyStateChanged(e, true)
	
	override def keyReleased(e: KeyEvent) = keyStateChanged(e, false)
	
	override def keyTyped(e: KeyEvent) = keyTypedHandler.onKeyTyped(KeyTypedEvent(e.getKeyChar, _keyStatus))
	
	
	// OTHER METHODS    --------------
	
	private def keyStateChanged(e: KeyEvent, newState: Boolean) =
	{
		val location = KeyLocation.of(e.getKeyLocation).getOrElse(Standard)
		
		// Only reacts to status changes
		if (_keyStatus(e.getExtendedKeyCode, location) != newState)
		{
			_keyStatus += (e.getExtendedKeyCode, location, newState)
			keyStateHandler.onKeyState(new KeyStateEvent(e.getExtendedKeyCode, location, newState, _keyStatus))
		}
	}
}
