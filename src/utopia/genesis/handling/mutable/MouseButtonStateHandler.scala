package utopia.genesis.handling.mutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseButtonStateListener
import utopia.inception.handling.mutable.DeepHandler


object MouseButtonStateHandler
{
	def apply(elements: TraversableOnce[MouseButtonStateListener] = Vector()) = new MouseButtonStateHandler(elements)
	
	def apply(element: MouseButtonStateListener) = new MouseButtonStateHandler(Vector(element))
	
	def apply(first: MouseButtonStateListener, second: MouseButtonStateListener, more: MouseButtonStateListener*) = new MouseButtonStateHandler(Vector(first, second) ++ more)
}

/**
  * This ia a mutable implementation of the MouseButtonStateHandler interface
  * @param initialElements The elements initially placed in this handler
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseButtonStateHandler(initialElements: TraversableOnce[MouseButtonStateListener])
	extends DeepHandler[MouseButtonStateListener](initialElements) with handling.MouseButtonStateHandler
