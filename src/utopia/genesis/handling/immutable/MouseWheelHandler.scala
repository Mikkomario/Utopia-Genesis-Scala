package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseWheelListener
import utopia.inception.handling.immutable.Handler
import utopia.inception.handling.{Handleable, HandlerType}

object MouseWheelHandler
{
	/**
	  * An empty key state handler
	  */
	val empty = new MouseWheelHandler(Vector(), None)
	
	/**
	  * @param elements Elements for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(elements: TraversableOnce[MouseWheelListener], parent: Option[Handleable] = None) = new MouseWheelHandler(elements, parent)
	
	/**
	  * @param element an element for this handler
	  * @param parent Handleable this handler is dependent from (default = None = independent)
	  * @return A new handler
	  */
	def apply(element: MouseWheelListener, parent: Option[Handleable] = None) = new MouseWheelHandler(Vector(element), parent)
	
	/**
	  * @return A handler with all of the provided elements
	  */
	def apply(first: MouseWheelListener, second: MouseWheelListener, more: MouseWheelListener*): MouseWheelHandler = apply(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the MouseWheelHandler trait
  * @param initialElements Listerners placed in this handler
  * @param parent A parent handleable. None if this handler should be independent
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseWheelHandler(initialElements: TraversableOnce[MouseWheelListener], val parent: Option[Handleable])
	extends Handler[MouseWheelListener](initialElements) with handling.MouseWheelHandler
{
	/**
	  * @param handlerType The type of handler doing the handling
	  * @return Whether this handleable instance may be called by a handler of the target handler type,
	  *         provided the handler supports this handleable instance
	  */
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
