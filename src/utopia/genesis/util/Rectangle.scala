package utopia.genesis.util

import java.awt.geom.RoundRectangle2D

/**
 * A rectangle limits a certain rectangular area of space. The rectangle is defined by two points 
 * and the edges go along x, y and z axes.
 * @author Mikko Hilpinen
 * @since 13.1.2017
 */
case class Rectangle(val position: Vector3D, val size: Vector3D) extends ShapeConvertible
{
    // COMPUTED PROPERTIES    ------------
    
    override def toShape = new java.awt.Rectangle(position.x.toInt, position.y.toInt, width.toInt, height.toInt)
    
    /**
     * The width of the rectangle / cube
     */
    def width = size.x
    
    /**
     * The height of the rectangle / cube
     */
    def height = size.y
    
    /**
     * The depth (dimension along z-axis) of the cube
     */
    def depth = size.z
    
    /**
     * The area of the x-y side of this rectangle in square pixels
     */
    def area = width * height
    
    /**
     * The area of the whole surface of this 3D cube in square pixels
     */
    def surfaceArea = 2 * width * height + 2 * height * depth + 2 * width * depth
    
    /**
     * The volume inside this 3D cube in cubic pixels
     */
    def volume = width * height * depth
    
    /**
     * The corners of the x-y side of the rectangle, in clockwise order starting from the
     * rectangle's position
     */
    def corners2D = Vector(position, position + size.copy(y = 0, z = 0), 
            position + size.copy(z = 0), position + size.copy(x = 0, z = 0));
    
    /**
     * The edges of the x-y side of the rectangle, in clockwise order starting from the x-wise edge
     */
    def edges2D = Line.edgesForVertices(corners2D)
    
    /**
     * A version of this rectangle that lies completely on the x-y plane
     */
    def in2D = Rectangle(position.in2D, size.in2D)
    
    
    // OTHER METHODS    ----------------
    
    /**
     * Creates a rounded rectangle based on this rectangle shape.
     * @param ruondingFactor How much the corners are rounded. 0 Means that the corners are not
     * rounded at all, 1 means that the corners are rounded as much as possible, so that the ends of
     * the shape become ellipsoid. Default value is 0.25
     */
    def toRoundedRectangle(roundingFactor: Double = 0.25) =
    {
        val rounding = math.min(width, height) * roundingFactor / 2
        new RoundRectangle2D.Double(position.x, position.y, width, height, rounding, rounding)
    }
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided circle.
     * Both shapes are projected to the x-y plane before the check.
     */
    def circleIntersection2D(circle: Circle) = 
    {
        val circle2D = circle.in2D
        edges2D.flatMap { _.circleIntersection(circle2D) }
    }
}