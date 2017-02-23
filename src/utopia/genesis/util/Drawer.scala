package utopia.genesis.util

import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.geom.AffineTransform
import scala.collection.mutable.Stack
import java.awt.Color
import java.awt.BasicStroke
import java.awt.Stroke
import java.awt.Shape
import java.awt.Paint

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
    // TODO: Or, make drawer completely immutable? Overkill? Having to dispose the 
    // instances is a bit of a pain though -> what if they disposed themselves (like a tree where 
    // the topmost instance would handle the disposing)?
    // TODO: Add copy and dispose
    // TODO: Add clipping (on a new graphics object)
    // TODO: Apparently default stroke is much more efficient. Should probably only use a copy when 
    // changing the stroke
    
    // ATTRIBUTES    -----------------
    
    /**
     * The color used when drawing shape edges
     */
    var edgeColor: Paint = Color.BLACK
    /**
     * The color used when filling a shape
     */
    var fillColor: Paint = Color.WHITE
    
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
     * Copies this drawer, creating another graphics context. Changing the other context doesn't
     * affect this one. This should be used when a lot of drawing is done and the graphics context 
     * should be returned to its original state. Remember to dispose the new context using the
     * dispose() -method, however. The creator of the context always has the responsibility to
     * dispose it also.
     */
    def copy() = new Drawer(graphics.create().asInstanceOf[Graphics2D])
    
    /**
     * Disposes this graphics context. This should be called for all drawer instances created by
     * copy() or similar methods.
     */
    def dispose() = graphics.dispose()
    
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
     * Draws and fills a shape
     */
    def draw(shape: Shape) = 
    {
        graphics.setPaint(fillColor)
        graphics.fill(shape)
        graphics.setPaint(edgeColor)
        graphics.draw(shape)
    }
    
    /**
     * Draws a shape convertible instance as this drawer would draw a shape
     */
    def draw(shape: ShapeConvertible): Unit = draw(shape.toShape)
    
    /**
     * Copies this graphics context, changing the stroke style in the process. Remember to call
     * dispose() after use.<br>
     * This method copies the drawer instance because the use of default stroke is more optimised in
     * java and therefore should be preserved in the original instance where possible.
     */
    def withStroke(stroke: Stroke) = 
    {
        val drawer = copy()
        drawer.graphics.setStroke(stroke)
        drawer
    }
    
    /**
     * Clips the drawable area to a specified shape. This creates a new drawer so that the clipping
     * area can be reset by disposing the copied drawer and using the original.
     */
    def clippedTo(shape: Shape) = 
    {
        val drawer = copy()
        drawer.graphics.clip(shape)
        drawer
    }
    
    /**
     * Clips the drawable area to a specified shape. This creates a new drawer so that the clipping
     * area can be reset by disposing the copied drawer and using the original.
     */
    def clippedTo(shape: ShapeConvertible): Drawer = clippedTo(shape)
}