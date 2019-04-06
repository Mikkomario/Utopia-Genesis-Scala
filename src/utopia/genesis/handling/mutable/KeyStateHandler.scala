package utopia.genesis.handling.mutable

import utopia.genesis.handling

import utopia.genesis.handling.KeyStateListener
import utopia.inception.handling.mutable.DeepHandler

object KeyStateHandler
{
	def apply(elements: TraversableOnce[KeyStateListener] = Vector()) = new KeyStateHandler(elements)
	
	def apply(element: KeyStateListener) = new KeyStateHandler(Vector(element))
	
	def apply(first: KeyStateListener, second: KeyStateListener, more: KeyStateListener*) = new KeyStateHandler(Vector(first, second) ++ more)
}

/**
  * This ia a mutable implementation of the key state handler interface
  * @param initialElements The elements initially placed in this handler
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class KeyStateHandler(initialElements: TraversableOnce[KeyStateListener])
	extends DeepHandler[KeyStateListener](initialElements) with handling.KeyStateHandler
