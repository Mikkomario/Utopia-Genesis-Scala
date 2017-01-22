package utopia.genesis.util

import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.geom.AffineTransform
import scala.collection.mutable.Stack
import java.awt.Color
import java.awt.BasicStroke
import java.awt.Stroke

object Drawer
{
    /**
     * Creates a stroke with default settings
     */
    def defaultStroke(width: Double) = new BasicStroke(width.toFloat)
    /**
     * Creates a rounded stroke instance
     */
    def roundStroke(width: Double) = new BasicStroke(width.toFloat, BasicStroke.CAP_ROUND, 
            BasicStroke.JOIN_ROUND);
}

/**
 * A drawer instance uses a graphics instance to draw elements. Different transformations can be
 * applied to a drawer.
 * @author Mikko Hilpinen
 * @since 22.1.2017
 */
class Drawer(val graphics: Graphics2D)
{
    // ATTRIBUTES    -----------------
    
    /**
     * The color used when drawing shape edges
     */
    var edgeColor = Color.BLACK
    /**
     * The color used when filling a shape
     */
    var fillColor = Color.WHITE
    
    private var _alpha = 1.0
    /**
     * The drawing alpha or opacity of the drawer. Value of 1 makes the drawer non-transparent.
     * Value of 0 makes the drawer non-visible.
     */
    def alpha = _alpha
    def alpha_=(alpha: Double) = 
    {
        _alpha = alpha
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.toFloat))
    }
    
    private var transforms = Stack[AffineTransform]()
    
    
    // OTHER METHODS    --------------
    
    /**
     * Applies a new transformation over this drawer. This can be reversed using the
     * popTransformation method.
     * @param t The transformation applied over this drawer.
     */
    def pushTransform(t: Transformation) = 
    {
        transforms push graphics.getTransform
        graphics.transform(t.toAffineTransform)
    }
    
    /**
     * Reverts the transformation state to that before the last call of the pushTransform method. If
     * there were no transformations applied over this drawer, this method does nothing.
     */
    def popTransformation() = if (!transforms.isEmpty) graphics.setTransform(transforms.pop())
    
    /**
     * Draws a circle
     */
    def draw(circle: Circle) = 
    {
        val x = (circle.origin.x - circle.radius).toInt
        val y = (circle.origin.y - circle.radius).toInt
        
        graphics.setColor(fillColor)
        graphics.fillOval(x, y, circle.diameter.toInt, circle.diameter.toInt)
        graphics.setColor(edgeColor)
        graphics.drawOval(x, y, circle.diameter.toInt, circle.diameter.toInt)
    }
    
    def setStroke(stroke: Stroke) = graphics.setStroke(stroke)
}