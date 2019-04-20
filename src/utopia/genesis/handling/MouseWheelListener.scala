package utopia.genesis.handling

import utopia.genesis.event.MouseWheelEvent
import utopia.inception.handling.Handleable
import utopia.inception.util.{AnyFilter, Filter}

/**
 * This trait is implemented by classes which are interested in being notified when the mouse wheel
 * is rotated
 */
trait MouseWheelListener extends Handleable
{
    /**
     * This method is called whenever the mouse wheel rotates
     */
    def onMouseWheelRotated(event: MouseWheelEvent)
    
    /**
     * This filter is applied to the events incoming to the listener. Only events accepted by this
     * filter are informed to the instance. The default filter accepts all mouse wheel events.
     */
    def mouseWheelEventFilter: Filter[MouseWheelEvent] = AnyFilter
    
    /**
      * @return Whether this instance is willing to receive mouse wheel events
      */
    def isReceivingMouseWheelEvents = allowsHandlingFrom(MouseWheelHandlerType)
}