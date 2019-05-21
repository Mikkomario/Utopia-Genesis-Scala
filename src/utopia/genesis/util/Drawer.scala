package utopia.genesis.util

import java.awt.{AlphaComposite, BasicStroke, Font, Graphics, Graphics2D, Paint, Shape, Stroke}
import java.awt.geom.AffineTransform

import utopia.genesis.color.Color
import utopia.genesis.shape.shape2D.{Bounds, Point, ShapeConvertible, Transformation}

object Drawer
{
    /**
     * Creates a stroke with default settings
     */
    def defaultStroke(width: Double) = new BasicStroke(width.toFloat)
    /**
     * Creates a rounded stroke instance
     */
    def roundStroke(width: Double) = new BasicStroke(width.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    
    /**
      * Creates a new temporary drawer for the duration of the operation by wrapping an awt graphics instance.
      * Disposes the drawer afterwards.
      * @param graphics A graphics instance
      * @param f A function that uses the drawer
      */
    def use(graphics: Graphics)(f: Drawer => Unit) = new Drawer(graphics.create().asInstanceOf[Graphics2D]).disposeAfter(f)
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
class Drawer(val graphics: Graphics2D, val fillColor: Option[Paint] = Some(java.awt.Color.WHITE),
             val edgeColor: Option[Paint] = Some(java.awt.Color.BLACK))
{
    // TODO: Add rendering hints
    
    // ATTRIBUTES    ----------------------
    
    private var children = Vector[Drawer]()
    
    
    // COMPUTED ---------------------------
    
    /**
      * @return A version of this drawer that only fills shapes. May return this drawer.
      */
    def onlyFill = if (edgeColor.isDefined) copy(fillColor, None) else this
    
    /**
      * @return A version of this drawer that only draws edges of shapes. May return this drawer.
      */
    def onlyEdges = if (fillColor.isDefined) copy(None, edgeColor) else this
    
    /**
      * @return A version of this drawer that only draws edges of shapes. May return this drawer.
      */
    def noFill = onlyEdges
    
    /**
      * @return A version of this drawer that only fills shapes. May return this drawer.
      */
    def noEdges = onlyFill
    
    
    // OTHER METHODS    -------------------
    
    /**
      * Performs an operation, then disposes this drawer
      * @param operation An operation
      * @tparam U Arbitary result type
      */
    def disposeAfter[U](operation: Drawer => U) =
    {
        operation(this)
        dispose()
    }
    
    /**
      * Creates a temporary copy of this drawer and performs an operation with it, after which it's disposed
      * @param operation Operation performed with the drawer
      * @tparam U Arbitary result type
      */
    def withCopy[U](operation: Drawer => U) = copy().disposeAfter(operation)
    
    /**
     * Copies this drawer, creating another graphics context. Changing the other context doesn't
     * affect this one. This should be used when a lot of drawing is done and the graphics context 
     * should be returned to its original state.
     */
    def copy(): Drawer = copy(fillColor, edgeColor)
    
    /**
     * Disposes this graphics context and every single drawer created from this drawer instance. The
     * responsibility to dispose the drawer lies at the topmost user. Disposing drawers on lower
     * levels is fully optional. This drawer or any drawer created from this drawer cannot be used
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
        if (fillColor.isDefined)
        {
            graphics.setPaint(fillColor.get)
            graphics.fill(shape)
        }
        if (edgeColor.isDefined)
        {
            graphics.setPaint(edgeColor.get)
            graphics.draw(shape)
        }
    }
    
    /**
     * Draws a shape convertible instance as this drawer would draw a shape
     */
    def draw(shape: ShapeConvertible): Unit = draw(shape.toShape)
    
    /**
     * Draws a piece of text so that it is centered in a set of bounds
     */
    def drawTextCentered(text: String, font: Font, bounds: Bounds)
    {
        // Sets the color, preferring edge color
        edgeColor.orElse(fillColor).foreach { graphics.setPaint }
        
        graphics.setFont(font)
        val metrics = graphics.getFontMetrics
        
        val x = bounds.position.x + (bounds.width - metrics.stringWidth(text)) / 2
        val y = bounds.position.y + (bounds.height - metrics.getHeight) / 2 + metrics.getAscent
        
        graphics.drawString(text, x.toInt, y.toInt)
    }
    
    /**
      * Draws a piece of text
      * @param text The text that is drawn
      * @param font Font used
      * @param topLeft The top left position of the text
      */
    def drawText(text: String, font: Font, topLeft: Point) =
    {
        // Sets the color, preferring edge color
        edgeColor.orElse(fillColor).foreach { graphics.setPaint }
        graphics.setFont(font)
        val metrics = graphics.getFontMetrics
        graphics.drawString(text, topLeft.x.toInt, topLeft.y.toInt + metrics.getAscent)
    }
    
    /**
     * Creates a transformed copy of this
     * drawer so that it will use the provided transformation to draw relative 
     * elements into absolute space
     */
    def transformed(transform: AffineTransform) = 
    {
        val drawer = copy()
        drawer.graphics.transform(transform)
        drawer
    }
    
    /**
     * Creates a transformed copy of this
     * drawer so that it will use the provided transformation to draw relative 
     * elements into absolute space
     */
    def transformed(transform: Transformation): Drawer = transformed(transform.toAffineTransform)
    
    /*
     * Creates a transformed copy of this drawer so that it reads from absolute world space and 
     * projects them differently on another absolute world space
     * @param from The transformation with which the data is read
     * @param to The transformation with which the data is projected (like in other transform
     * methods)
     */
    //def transformed(from: Transformation, to: Transformation): Drawer = 
    //        transformed(from.toInvertedAffineTransform).transformed(to);
    
    /**
     * Creates a new instance of this drawer with altered colours
     * @param fillColor the colour / paint used for filling the area
     * @param edgeColor the colour / paint used for the drawn edges. By default stays the same as it
     * was in the original
     */
    def withColor(fillColor: Option[Paint], edgeColor: Option[Paint] = this.edgeColor) = copy(fillColor, edgeColor)
    
    /**
      * Creates a new instance of this drawer with altered colours
      * @param fillColor the colour / paint used for filling the area
      * @param edgeColor the colour / paint used for the drawn edges
      */
    def withColor(fillColor: Paint, edgeColor: Paint): Drawer = withColor(Some(fillColor), Some(edgeColor))
    
    /**
      * Creates a new instance of this drawer with altered colours
      * @param fillColor the colour / paint used for filling the area
      * @param edgeColor the colour / paint used for the drawn edges
      */
    def withColor(fillColor: Color, edgeColor: Color): Drawer = withColor(fillColor.toAwt, edgeColor.toAwt)
    
    /**
     * Creates a new instance of this drawer with altered edge colour
     */
    def withEdgeColor(edgeColor: Option[Paint]) = withColor(fillColor, edgeColor)
    
    /**
      * @param color The new fill color
      * @return A version of this drawer with specified fill color
      */
    def withFillColor(color: Paint) = withColor(Some(color))
    
    /**
      * @param color The new fill color
      * @return A version of this drawer with specified fill color
      */
    def withFillColor(color: Color) = withColor(Some(color.toAwt))
    
    /**
      * @param color The new edge drawing color
      * @return A version of this drawer with specified edge color
      */
    def withEdgeColor(color: Paint): Drawer = withEdgeColor(Some(color))
    
    /**
      * @param color The new edge drawing color
      * @return A version of this drawer with specified edge color
      */
    def withEdgeColor(color: Color): Drawer = withEdgeColor(color.toAwt)
    
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
    def clippedTo(shape: ShapeConvertible): Drawer = clippedTo(shape.toShape)
    
    private def copy(fillColor: Option[Paint], edgeColor: Option[Paint]) = 
    {
        val drawer = new Drawer(graphics.create().asInstanceOf[Graphics2D], fillColor, edgeColor)
        children :+= drawer
        
        drawer
    }
}