package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseButtonStateListener
import utopia.inception.handling.immutable.Handler
import utopia.inception.handling.{Handleable, HandlerType}


object MouseButtonStateHandler
{
	/**
	  * An empty key state handler
	  */
	val empty = new MouseButtonStateHandler(Vector(), None)
	
	/**
	  * @param elements Elements for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(elements: TraversableOnce[MouseButtonStateListener], parent: Option[Handleable] = None) = new MouseButtonStateHandler(elements, parent)
	
	/**
	  * @param element an element for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(element: MouseButtonStateListener, parent: Option[Handleable] = None) = new MouseButtonStateHandler(Vector(element), parent)
	
	/**
	  * @return A handler with all of the provided elements
	  */
	def apply(first: MouseButtonStateListener, second: MouseButtonStateListener, more: MouseButtonStateListener*): MouseButtonStateHandler = apply(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the MouseButtonStateHandler trait
  * @param initialElements Listerners placed in this handler
  * @param parent A parent handleable. None if this handler should be independent
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseButtonStateHandler(initialElements: TraversableOnce[MouseButtonStateListener], val parent: Option[Handleable])
	extends Handler[MouseButtonStateListener](initialElements) with handling.MouseButtonStateHandler
{
	/**
	  * @param handlerType The type of handler doing the handling
	  * @return Whether this handleable instance may be called by a handler of the target handler type,
	  *         provided the handler supports this handleable instance
	  */
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
