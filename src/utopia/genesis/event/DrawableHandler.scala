package utopia.genesis.event

import utopia.inception.handling.Handler
import utopia.inception.handling.HandlerType
import utopia.genesis.util.DepthRange
import java.awt.Graphics2D
import utopia.genesis.util.Drawer

case object DrawableHandlerType extends HandlerType(classOf[Drawable])

class DrawableHandler(override val depth: Int = DepthRange.default, 
        val customize: Option[Drawer => Drawer] = None) extends 
        Handler[Drawable](DrawableHandlerType) with Drawable
{
    // IMPLEMENTED METHODS    -------------
    
    override def draw(drawer: Drawer) = 
    {
        // May customise the graphics context
        val customGraphics = customize.map { _(drawer) }
        
        // Draws all the elements inside the handler. 
        // If the depth order was wrong, fixes it for the next iteration
        var lastDepth = Int.MaxValue
        var sortDepth = false
        
        foreach(true, drawable => 
        {
            drawable.draw(customGraphics.getOrElse(drawer))
            if (drawable.depth > lastDepth)
            {
                sortDepth = true
            }
            lastDepth = drawable.depth
            true
        })
        
        if (sortDepth)
        {
            sortWith({ _.depth >= _.depth })
        }
        
        // Clears the customised graphics context, if applicable
        customGraphics.foreach { _.dispose() }
    }
}