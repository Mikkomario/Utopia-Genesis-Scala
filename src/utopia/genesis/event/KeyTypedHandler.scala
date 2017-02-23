package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler

case object KeyTypedHandlerType extends HandlerType(classOf[KeyTypedListener])

/**
 * This handler informs multiple listeners of key typed events
 * @author Mikko Hilpinen
 * @since 23.2.2017
 */
class KeyTypedHandler extends Handler[KeyTypedListener](KeyTypedHandlerType) with KeyTypedListener
{
    def onKeyTyped(event: KeyTypedEvent) = foreach(true, listener => 
    {
        listener.onKeyTyped(event)
        true
    })
}