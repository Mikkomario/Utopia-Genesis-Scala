package utopia.genesis.handling.mutable

import utopia.genesis.handling
import utopia.genesis.handling.MouseWheelListener
import utopia.inception.handling.mutable.DeepHandler


object MouseWheelHandler
{
	def apply(elements: TraversableOnce[MouseWheelListener] = Vector()) = new MouseWheelHandler(elements)
	
	def apply(element: MouseWheelListener) = new MouseWheelHandler(Vector(element))
	
	def apply(first: MouseWheelListener, second: MouseWheelListener, more: MouseWheelListener*) = new MouseWheelHandler(Vector(first, second) ++ more)
}

/**
  * This ia a mutable implementation of the MouseWheelHandler interface
  * @param initialElements The elements initially placed in this handler
  * @author Mikko Hilpinen
  * @since 6.4.2019, v2+
  */
class MouseWheelHandler(initialElements: TraversableOnce[MouseWheelListener])
	extends DeepHandler[MouseWheelListener](initialElements) with handling.MouseWheelHandler
