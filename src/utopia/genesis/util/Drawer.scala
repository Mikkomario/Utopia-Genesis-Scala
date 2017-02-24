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
 * A drawer instance uses a graphics instance to draw elements. Different transformations and
 * settings can be applied to a drawer. The usual policy is to not modify the drawers directly but
 * always make new copies instead. The underlying graphics object should therefore only be altered
 * when this class doesn't provide a suitable interface to do that otherwise. Because a mutable
 * graphics object instance is made visible, this class does not have value semantics and should be
 * treated with care. It is usually better to pass on a copy of this object when its usage cannot be
 * controlled.
 * @author Mikko Hilpinen
 * @since 22.1.2017
 */
class Drawer(val graphics: Graphics2D, val fillColor: Paint = Color.WHITE, 
        val edgeColor: Paint = Color.BLACK)
{
    // TODO: Add rendering hints
    // TODO: Also make all of these variables values and use copy approach instead
    // TODO: Just remember to add a private list of children so that the dispose can be done recursively
    
    // ATTRIBUTES    ----------------------
    
    private var children = Vector[Drawer]()
    
    
    // OPERATORS    -----------------------
    
    /**
     * Creates a new drawer with the provided transformation applied. The transformation will be
     * applied over existing transformations. This operation cannot be reversed but the original
     * (unaltered) instance can still be used.
     */
    def +(transform: Transformation) = 
    {
        val drawer = copy()
        drawer.graphics.transform(transform.toAffineTransform)
        drawer
    }
    
    
    
    // OTHER METHODS    -------------------
    
    /**
     * Copies this drawer, creating another graphics context. Changing the other context doesn't
     * affect this one. This should be used when a lot of drawing is done and the graphics context 
     * should be returned to its original state.
     */
    def copy() = withColor(fillColor)
    
    /**
     * Disposes this graphics context and every single drawer created from this drawer instance. The
     * responsibility to dispose the drawer lies at the topmost user. Disposing drawers on lower
     * levels is fully optional. The drawer or any drawer created from this drawer cannot be used
     * after it has been disposed.
     */
    def dispose(): Unit = 
    {
        children.foreach { _.dispose() }
        graphics.dispose()
    }
    
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
     * Creates a new instance of this drawer with altered colours
     * @param edgeColor the colour / paint used for the drawn edges
     * @param fillColor the colour / paint used for filling the area
     */
    def withColor(fillColor: Paint, edgeColor: Paint = this.edgeColor) =
    {
        val drawer = new Drawer(graphics.create().asInstanceOf[Graphics2D], fillColor, edgeColor)
        children :+= drawer
        drawer
    }
            
    /**
     * Creates a copy of this context with altered alpha (opacity / transparency) value.
     * @param alpha Between 0 and 1. 1 Means that the drawn elements are fully visible / not
     * transparent while lower numbers increase the transparency.
     */
    def withAlpha(alpha: Double) = 
    {
        val drawer = copy()
        drawer.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.toFloat))
        drawer
    }
    
    /**
     * Copies this graphics context, changing the stroke style in the process. It is usually better
     * to pass a drawer instance with unmodified stroke where that is possible, since it is better
     * optimised in the lower level implementation.
     */
    def withStroke(stroke: Stroke) = 
    {
        val drawer = copy()
        drawer.graphics.setStroke(stroke)
        drawer
    }
    
    /**
     * Creates a new instance of this drawer that has a clipped drawing area. The operation cannot
     * be reversed but the original instance can still be used for drawing without clipping.
     */
    def clippedTo(shape: Shape) = 
    {
        val drawer = copy()
        drawer.graphics.clip(shape)
        drawer
    }
    
    /**
     * Creates a new instance of this drawer that has a clipped drawing area. The operation cannot
     * be reversed but the original instance can still be used for drawing without clipping.
     */
    def clippedTo(shape: ShapeConvertible): Drawer = clippedTo(shape)
}