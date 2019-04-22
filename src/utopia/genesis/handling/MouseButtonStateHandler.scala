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
	
	override def onMouseButtonState(event: MouseButtonStateEvent) =
	{
		if (event.isConsumed)
		{
			// If event is already consumed, doesn't worry about changing event state
			handle { l => if (l.mouseButtonStateEventFilter(event)) l.onMouseButtonState(event) }
			true
		}
		else
		{
			// Handles items until the event is consumed, after which continues with a consumed event
			val view = handleView()
			val consumedAtIndex = view.indexWhere {
				l => if (l.mouseButtonStateEventFilter(event)) l.onMouseButtonState(event) else false }
			if (consumedAtIndex >= 0)
			{
				val consumedEvent = event.consumed
				view.drop(consumedAtIndex + 1).foreach {
					l => if (l.mouseButtonStateEventFilter(consumedEvent)) l.onMouseButtonState(consumedEvent) }
				true
			}
			else
				false
		}
	}
}
