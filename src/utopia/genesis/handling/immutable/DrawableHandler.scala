package utopia.genesis.handling.immutable

import utopia.genesis.handling
import utopia.genesis.handling.Drawable
import utopia.genesis.util.{DepthRange, Drawer}
import utopia.inception.handling.{Handleable, HandlerType}
import utopia.inception.handling.immutable.Handler

object DrawableHandler
{
	/**
	  * An empty drawable handler
	  */
	val empty = new DrawableHandler(Vector(), DepthRange.default, None, None)
	
	/**
	  * @param elements The elements in the handler. Will be depth sorted.
	  * @param drawDepth The draw depth of the handler (default = 0)
	  * @param customizer A function that customizes drawers used by the handler (default = None = No customization)
	  * @param parent A handleable this handler is dependent from (default = None = handler is independent)
	  * @return A new handler
	  */
	def apply(elements: Seq[Drawable], drawDepth: Int = DepthRange.default, customizer: Option[Drawer => Drawer] = None,
			  parent: Option[Handleable] = None) = new DrawableHandler(elements, drawDepth, customizer, parent)
	
	/**
	  * @param element The drawable element in the handler
	  * @return A new handler
	  */
	def apply(element: Drawable): DrawableHandler = apply(Vector(element))
	
	/**
	  * @return A drawable handler with all specified elements
	  */
	def apply(first: Drawable, second: Drawable, more: Drawable*): DrawableHandler = apply(Vector(first, second) ++ more)
}

/**
  * This is an immutable implementation of the DrawableHandler trait
  * @param initialElements The initial drawable elements. Will be sorted based on drawing depth.
  * @param drawDepth The draw depth of this handler
  * @param customizer A function for customizing drawers used by this handler. None if no customization should be done
  * @param parent A handleable this handler depends from. None if this handler should act independently
  */
class DrawableHandler(initialElements: Seq[Drawable], drawDepth: Int, val customizer: Option[Drawer => Drawer],
					  val parent: Option[Handleable])
	extends Handler[Drawable](initialElements.sortWith { _.drawDepth > _.drawDepth })
		with handling.DrawableHandler
{
	/**
	  * Draws the drawable instance using a specific graphics object. The graphics transformations
	  * should always be set back to original after drawing
	  * @param drawer The drawer object used for drawing this instance
	  */
	override def draw(drawer: Drawer) =
	{
		// May use a custom drawer
		val customDrawer = customizer.map { _(drawer) }
		handle { _.draw(customDrawer getOrElse drawer) }
		customDrawer.foreach { _.dispose() }
	}
	
	/**
	  * @param handlerType The type of handler doing the handling
	  * @return Whether this handleable instance may be called by a handler of the target handler type,
	  *         provided the handler supports this handleable instance
	  */
	override def allowsHandlingFrom(handlerType: HandlerType) = handlerType == this.handlerType
}
