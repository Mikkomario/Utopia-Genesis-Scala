package utopia.genesis.handling

import utopia.genesis.event.MouseMoveEvent
import utopia.inception.handling.{Handler, HandlerType}

case object MouseMoveHandlerType extends HandlerType
{
	/**
	  * @return The class supported by this handler type
	  */
	override def supportedClass = classOf[MouseMoveListener]
}

/**
  * MouseMoveHandlers distribute mouse move events among multiple listeners
  */
trait MouseMoveHandler extends Handler[MouseMoveListener] with MouseMoveListener
{
	/**
	  * @return The type of this handler
	  */
	override def handlerType = MouseMoveHandlerType
	
	override def onMouseMove(event: MouseMoveEvent) = handle {
		l => if (l.mouseMoveEventFilter(event)) l.onMouseMove(event) }
}
