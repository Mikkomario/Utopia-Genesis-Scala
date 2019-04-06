package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.KeyStateListener
import utopia.inception.handling.{Handleable, HandlerType}
import utopia.inception.handling.immutable.Handler

object KeyStateHandler
{
	/**
	  * An empty key state handler
	  */
	val empty = new KeyStateHandler(Vector(), None)
	
	/**
	  * @param elements Elements for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(elements: TraversableOnce[KeyStateListener], parent: Option[Handleable] = None) = new KeyStateHandler(elements, parent)
	
	/**
	  * @param element an element for this handler
	  * @return A new handler
	  */
	def apply(element: KeyStateListener) = new KeyStateHandler(Vector(element), None)
	
	/**
	  * @return A handler with all of the provided elements
	  */
	def apply(first: KeyStateListener, second: KeyStateListener, more: KeyStateListener*): KeyStateHandler = apply(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the KeyStateHandler trait
  * @param initialElements Listerners placed in this handler
  * @param parent A parent handleable. None if this handler should be independent
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class KeyStateHandler(initialElements: TraversableOnce[KeyStateListener], val parent: Option[Handleable])
	extends Handler[KeyStateListener](initialElements) with handling.KeyStateHandler
{
	/**
	  * @param handlerType The type of handler doing the handling
	  * @return Whether this handleable instance may be called by a handler of the target handler type,
	  *         provided the handler supports this handleable instance
	  */
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
