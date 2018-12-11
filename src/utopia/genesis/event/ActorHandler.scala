package utopia.genesis.event

import utopia.inception.handling.Handler
import java.time.Duration

/**
 * ActorHandlers are used for calling act method of numerous Actors in succession. The handler also
 * makes sure that only actors which have the correct handling state are called
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
class ActorHandler extends Handler[Actor](ActorHandlerType) with Actor
{
    // IMPLEMENTED METHODS    --------
    
    def act(duration: Duration) = foreach(true, actor => { actor.act(duration); true })
}