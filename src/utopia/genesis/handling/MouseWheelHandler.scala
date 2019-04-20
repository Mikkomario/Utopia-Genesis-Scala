package utopia.genesis.handling

import utopia.genesis.event.MouseWheelEvent
import utopia.inception.handling.{Handler, HandlerType}

case object MouseWheelHandlerType extends HandlerType
{
	/**
	  * @return The class supported by this handler type
	  */
	override def supportedClass = classOf[MouseWheelListener]
}

trait MouseWheelHandler extends Handler[MouseWheelListener] with MouseWheelListener
{
	/**
	  * @return The type of this handler
	  */
	override def handlerType = MouseWheelHandlerType
	
	/**
	  * This method is called whenever the mouse wheel rotates
	  */
	override def onMouseWheelRotated(event: MouseWheelEvent) = handle {
		l => if (l.mouseWheelEventFilter(event)) l.onMouseWheelRotated(event) }
}
