package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseMoveListener
import utopia.inception.handling.immutable.Handler
import utopia.inception.handling.{Handleable, HandlerType}

object MouseMoveHandler
{
	/**
	  * An empty key state handler
	  */
	val empty = new MouseMoveHandler(Vector(), None)
	
	/**
	  * @param elements Elements for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(elements: TraversableOnce[MouseMoveListener], parent: Option[Handleable] = None) = new MouseMoveHandler(elements, parent)
	
	/**
	  * @param element an element for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(element: MouseMoveListener, parent: Option[Handleable] = None) = new MouseMoveHandler(Vector(element), parent)
	
	/**
	  * @return A handler with all of the provided elements
	  */
	def apply(first: MouseMoveListener, second: MouseMoveListener, more: MouseMoveListener*): MouseMoveHandler = apply(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the MouseMoveHandler trait
  * @param initialElements Listerners placed in this handler
  * @param parent A parent handleable. None if this handler should be independent
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseMoveHandler(initialElements: TraversableOnce[MouseMoveListener], val parent: Option[Handleable])
	extends Handler[MouseMoveListener](initialElements) with handling.MouseMoveHandler
{
	/**
	  * @param handlerType The type of handler doing the handling
	  * @return Whether this handleable instance may be called by a handler of the target handler type,
	  *         provided the handler supports this handleable instance
	  */
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
