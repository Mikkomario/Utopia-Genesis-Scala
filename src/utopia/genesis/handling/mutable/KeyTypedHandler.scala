package utopia.genesis.handling.mutable

import utopia.genesis.handling

import utopia.genesis.handling.KeyTypedListener
import utopia.inception.handling.mutable.DeepHandler

object KeyTypedHandler
{
	def apply(elements: TraversableOnce[KeyTypedListener] = Vector()) = new KeyTypedHandler(elements)
	
	def apply(element: KeyTypedHandler) = new KeyTypedHandler(Vector(element))
	
	def apply(first: KeyTypedListener, second: KeyTypedListener, more: KeyTypedListener*) = new KeyTypedHandler(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the KeyTypedHandler trait
  * @param initialElements The elements initially placed in this handler
  */
class KeyTypedHandler(initialElements: TraversableOnce[KeyTypedListener])
	extends DeepHandler[KeyTypedListener](initialElements) with handling.KeyTypedHandler
