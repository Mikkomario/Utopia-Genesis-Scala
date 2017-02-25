package utopia.genesis.event

import utopia.inception.util.Filter
import utopia.inception.util.AnyFilter
import utopia.inception.handling.Handleable

/**
 * These listeners are interested in receiving events whenever the keyboard state changes
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
trait KeyStateListener extends Handleable
{
    /**
     * This method will be called when the keyboard state changes
     */
    def onKeyState(event: KeyStateEvent)
    
    /**
     * This listener will only be called for events accepted by this filter. By default the filter
     * accepts all key state events.
     */
    def keyStateEventFilter: Filter[KeyStateEvent] = AnyFilter
}