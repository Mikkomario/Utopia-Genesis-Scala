package utopia.genesis.event

import utopia.inception.handling.Handler
import utopia.inception.handling.HandlerType
import utopia.genesis.util.DepthRange
import java.awt.Graphics2D
import utopia.genesis.util.Drawer

case object DrawableHandlerType extends HandlerType(classOf[Drawable])

class DrawableHandler extends Handler[Drawable](DrawableHandlerType) with Drawable
{
    // IMPLEMENTED METHODS    -------------
    
    override def draw(drawer: Drawer) = 
    {
        // Draws all the elements inside the handler. 
        // If the depth order was wrong, fixes it for the next iteration
        var lastDepth = Int.MaxValue
        var sortDepth = false
        
        foreach(true, drawable => 
        {
            drawable.draw(drawer)
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
    }
}