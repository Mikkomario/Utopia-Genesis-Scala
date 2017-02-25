package utopia.genesis.event

import utopia.inception.handling.Handleable
import utopia.inception.util.AnyFilter
import utopia.inception.util.Filter

/**
 * This trait is extended by classes which are interested in changes in mouse button statuses
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
trait MouseButtonStateListener extends Handleable
{
    /**
     * This method will be called in order to inform the listener about a new mouse button event 
     * (a mouse button being pressed or released)
     * @param event The mouse event that occurred
     */
    def onMouseButtonState(event: MouseButtonStateEvent)
    
    /**
     * The filter applied to the incoming mouse button events. The listener will only be informed
     * about the events accepted by the filter. The default filter accepts all mouse button events.
     */
    def mouseButtonStateEventFilter: Filter[MouseButtonStateEvent] = AnyFilter
}