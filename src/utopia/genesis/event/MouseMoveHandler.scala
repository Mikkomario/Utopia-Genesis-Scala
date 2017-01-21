package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler
import utopia.inception.event.AnyEventFilter

case object MouseMoveHandlerType extends HandlerType(classOf[MouseMoveListener])

/**
 * This handler keeps listeners informed about mouse move events
 * @author Mikko Hilpinen
 * @since 21.1.2017
 */
class MouseMoveHandler extends Handler[MouseMoveListener](MouseMoveHandlerType) with MouseMoveListener
{
    // ATTRIBUTES    --------------
    
    override val mouseMoveEventFilter = new AnyEventFilter[MouseMoveEvent]()
    
    
    // IMPLEMENTED METHODS    -----
    
    override def onMouseMoveEvent(event: MouseMoveEvent) = foreach(true, listener => 
    {
        if (listener.mouseMoveEventFilter(event))
        {
            listener.onMouseMoveEvent(event)
        }
        true
    })
}