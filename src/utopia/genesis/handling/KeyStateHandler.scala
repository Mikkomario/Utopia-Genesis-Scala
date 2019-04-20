package utopia.genesis.handling

import utopia.genesis.event.KeyStateEvent
import utopia.inception.handling.{Handler, HandlerType}

case object KeyStateHandlerType extends HandlerType
{
	/**
	  * @return The class supported by this handler type
	  */
	override def supportedClass = classOf[KeyStateListener]
}

/**
  * Key state handlers distribute key state events among multiple listeners
  */
trait KeyStateHandler extends Handler[KeyStateListener] with KeyStateListener
{
	/**
	  * @return The type of this handler
	  */
	override def handlerType = KeyStateHandlerType
	
	/**
	  * This method will be called when the keyboard state changes
	  */
	override def onKeyState(event: KeyStateEvent) = handle { l => if (l.keyStateEventFilter(event)) l.onKeyState(event) }
}
