package utopia.genesis.handling

import utopia.genesis.event.MouseMoveEvent
import utopia.inception.handling.Handleable
import utopia.inception.util.{AnyFilter, Filter}

/**
 * MouseMoveListeners are interested in receiving mouse move events
 * @author Mikko Hilpinen
 * @since 21.1.2017
 */
trait MouseMoveListener extends Handleable
{
    /**
     * This filter is applied over mouse move events the listener would receive. Only events
     * accepted by the filter are informed to the listener. The default implementation accepts
     * all incoming events.
     */
    def mouseMoveEventFilter: Filter[MouseMoveEvent] = AnyFilter
    
    /**
     * This method is used for informing the listener of new mouse events. This method should
     * only be called for events that are accepted by the listener's filter.
     * @param event The event that occurred.
     */
    def onMouseMove(event: MouseMoveEvent)
}
