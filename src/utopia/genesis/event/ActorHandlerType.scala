package utopia.genesis.event

import utopia.inception.handling.HandlerType

/**
 * This is the handler type instance for all handlers that operate on Actor instances
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
case object ActorHandlerType extends HandlerType(classOf[Actor])