package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler

case object MouseButtonHandlerType extends HandlerType(classOf[MouseButtonListener])

/**
 * This handler class informs numerous mouse button listeners about mouse button events
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
class MouseButtonHandler extends Handler[MouseButtonListener](MouseButtonHandlerType) with 
        MouseButtonListener
{
    override def onMouseButtonEvent(event: MouseButtonEvent) = foreach(true, listener => 
    {
        if (listener.mouseButtonEventFilter(event))
        {
            listener.onMouseButtonEvent(event)
        }
        true
    });
}