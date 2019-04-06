package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.KeyTypedListener
import utopia.inception.handling.{Handleable, HandlerType}
import utopia.inception.handling.immutable.Handler

object KeyTypedHandler
{
	val empty = new KeyStateHandler(Vector(), None)
	
	def apply(elements: TraversableOnce[KeyTypedListener], parent: Option[Handleable] = None) = new KeyTypedHandler(elements, parent)
	
	def apply(element: KeyTypedListener, parent: Option[Handleable] = None) = new KeyTypedHandler(Vector(element), parent)
	
	def apply(first: KeyTypedListener, second: KeyTypedListener, more: KeyTypedListener*) = new KeyTypedHandler(Vector(first, second) ++ more, None)
}

/**
  * This is an immutable implementation of the KeyTypedHandler trait
  * @param initialElements Elements placed in this handler
  * @param parent Parent handleable instance. None if this handler should be independent.
  */
class KeyTypedHandler(initialElements: TraversableOnce[KeyTypedListener], val parent: Option[Handleable])
	extends Handler[KeyTypedListener](initialElements) with handling.KeyTypedHandler
{
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
