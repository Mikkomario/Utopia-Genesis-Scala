package utopia.genesis.handling

import utopia.genesis.event.MouseButtonStateEvent
import utopia.inception.handling.{Handler, HandlerType}

case object MouseButtonStateHandlerType extends HandlerType
{
	/**
	  * @return The class supported by this handler type
	  */
	override def supportedClass = classOf[MouseButtonStateListener]
}

/**
  * Key state handlers distribute key state events among multiple listeners
  */
trait MouseButtonStateHandler extends Handler[MouseButtonStateListener] with MouseButtonStateListener
{
	/**
	  * @return The type of this handler
	  */
	override def handlerType = MouseButtonStateHandlerType
	
	/**
	  * This method will be called in order to inform the listener about a new mouse button event
	  * (a mouse button being pressed or released)
	  * @param event The mouse event that occurred
	  */
	override def onMouseButtonState(event: MouseButtonStateEvent) = handle {
		l => if (l.mouseButtonStateEventFilter(event)) l.onMouseButtonState(event) }
}
