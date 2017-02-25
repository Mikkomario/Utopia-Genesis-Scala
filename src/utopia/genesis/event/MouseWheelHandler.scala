package utopia.genesis.event

import utopia.inception.handling.Handler
import utopia.inception.handling.HandlerType

case object MouseWheelHandlerType extends HandlerType(classOf[MouseWheelListener])

/**
 * This handler informs numerous mouse wheel listeners of mouse wheel events
 * @author Mikko Hilpinen
 * @since 20.2.2017
 */
class MouseWheelHandler extends Handler[MouseWheelListener](MouseWheelHandlerType) with MouseWheelListener
{
    def onMouseWheelRotated(event: MouseWheelEvent) = foreach(true, listener => 
    {
        if (listener.mouseWheelEventFilter(event))
        {
            listener.onMouseWheelRotated(event)
        }
        true
    })
}