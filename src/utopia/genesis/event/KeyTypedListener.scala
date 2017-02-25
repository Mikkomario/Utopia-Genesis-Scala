package utopia.genesis.event

import utopia.inception.handling.Handleable

/**
 * These listeners are interested in receiving key typed events
 * @author Mikko Hilpinen
 * @since 23.2.2017
 */
trait KeyTypedListener extends Handleable
{
    /**
     * This method will be called in order to inform the instance of new key typed events
     */
    def onKeyTyped(event: KeyTypedEvent)
}