package utopia.genesis.event

import utopia.inception.handling.Handleable
import utopia.inception.util.Filter
import utopia.inception.util.AnyFilter

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
}