package utopia.genesis.handling.mutable

import utopia.genesis.handling
import utopia.genesis.handling.{Actor, ActorHandlerType}
import utopia.inception.handling.mutable.DeepHandler

object ActorHandler
{
    /**
      * @param actors Initial actors in the new handler
      * @return A new handler with specified actors
      */
    def apply(actors: TraversableOnce[Actor] = Vector()) = new ActorHandler(actors)
    
    /**
      * @param actor The initial actor
      * @return A new handler that contains the specified actor
      */
    def apply(actor: Actor) = new ActorHandler(Vector(actor))
    
    /**
      * @return A new handler with all of the provided actors
      */
    def apply(first: Actor, second: Actor, more: Actor*) = new ActorHandler(Vector(first, second) ++ more)
}

/**
 * ActorHandlers are used for calling act method of numerous Actors in succession. The handler also
 * makes sure that only actors which have the correct handling state are called
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
class ActorHandler(initialElements: TraversableOnce[Actor]) extends DeepHandler[Actor](ActorHandlerType, initialElements)
    with handling.ActorHandler