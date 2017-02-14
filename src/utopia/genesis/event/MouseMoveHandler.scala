package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler

case object MouseMoveHandlerType extends HandlerType(classOf[MouseMoveListener])

/**
 * This handler keeps listeners informed about mouse move events
 * @author Mikko Hilpinen
 * @since 21.1.2017
 */
class MouseMoveHandler extends Handler[MouseMoveListener](MouseMoveHandlerType) with MouseMoveListener
{
    // IMPLEMENTED METHODS    -----
    
    override def onMouseMove(event: MouseMoveEvent) = foreach(true, listener => 
    {
        if (listener.mouseMoveEventFilter(event))
        {
            listener.onMouseMove(event)
        }
        true
    })
}