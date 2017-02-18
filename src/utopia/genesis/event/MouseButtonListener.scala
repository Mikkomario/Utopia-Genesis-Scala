package utopia.genesis.event

import utopia.inception.handling.Handleable
import utopia.inception.util.AnyFilter
import utopia.inception.util.Filter

object MouseButtonListener
{
    /**
     * The event filter used by the mouse button listeners by default. This filter accepts 
     * all mouse button events.
     */
    val defaultEventFilter = new AnyFilter[MouseButtonEvent]()    
}

/**
 * This trait is extended by classes which are interested in changes in mouse button statuses
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
trait MouseButtonListener extends Handleable
{
    /**
     * This method will be called in order to inform the listener about a new mouse button event 
     * (a mouse button being pressed or released)
     * @param event The mouse event that occurred
     */
    def onMouseButtonEvent(event: MouseButtonEvent)
    
    /**
     * The filter applied to the incoming mouse button events. The listener will only be informed
     * about the events accepted by the filter. The default filter accepts all mouse button events.
     */
    def mouseButtonEventFilter: Filter[MouseButtonEvent] = MouseButtonListener.defaultEventFilter
}