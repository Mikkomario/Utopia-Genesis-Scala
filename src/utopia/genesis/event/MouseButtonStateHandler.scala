package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler

case object MouseButtonStateHandlerType extends HandlerType(classOf[MouseButtonStateListener])

/**
 * This handler class informs numerous mouse button listeners about mouse button events
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
class MouseButtonStateHandler extends Handler[MouseButtonStateListener](
        MouseButtonStateHandlerType) with MouseButtonStateListener
{
    override def onMouseButtonEvent(event: MouseButtonStateEvent) = foreach(true, listener => 
    {
        if (listener.mouseButtonStateEventFilter(event))
        {
            listener.onMouseButtonEvent(event)
        }
        true
    });
}