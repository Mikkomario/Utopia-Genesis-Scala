package utopia.genesis.event

import utopia.inception.handling.HandlerType
import utopia.inception.handling.Handler

case object KeyStateHandlerType extends HandlerType(classOf[KeyStateListener])

/**
 * This handler is used for informing numerous key state listeners of keyboard state changes
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
class KeyStateHandler extends Handler[KeyStateListener](KeyStateHandlerType) with KeyStateListener
{
    def onKeyState(event: KeyStateEvent) = foreach(true, listener => 
    {
        if (listener.keyStateEventFilter(event))
        {
            listener.onKeyState(event)
        }
        true
    })
}