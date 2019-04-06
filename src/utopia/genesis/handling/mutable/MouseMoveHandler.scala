package utopia.genesis.handling.mutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseMoveListener
import utopia.inception.handling.mutable.DeepHandler

object MouseMoveHandler
{
	def apply(elements: TraversableOnce[MouseMoveListener] = Vector()) = new MouseMoveHandler(elements)
	
	def apply(element: MouseMoveListener) = new MouseMoveHandler(Vector(element))
	
	def apply(first: MouseMoveListener, second: MouseMoveListener, more: MouseMoveListener*) = new MouseMoveHandler(Vector(first, second) ++ more)
}

/**
  * This ia a mutable implementation of the MouseMoveHandler interface
  * @param initialElements The elements initially placed in this handler
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseMoveHandler(initialElements: TraversableOnce[MouseMoveListener])
	extends DeepHandler[MouseMoveListener](initialElements) with handling.MouseMoveHandler
