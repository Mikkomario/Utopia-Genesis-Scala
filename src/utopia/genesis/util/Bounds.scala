package utopia.genesis.util

import java.awt.geom.RoundRectangle2D

object Bounds
{
    /**
     * Creates a rectangle that contains the area between the two coordinates. The order 
     * of the coordinates does not matter.
     */
    def between(p1: Vector3D, p2: Vector3D) = 
    {
        val min = Vector3D.min(p1, p2)
        val max = Vector3D.max(p1, p2)
        
        Bounds(min, max - min)
    }
    
    /**
     * Creates a set of bounds around a circle so that the whole sphere is contained.
     */
    def around(circle: Circle) = 
    {
        val r = Vector3D(circle.radius, circle.radius, circle.radius)
        Bounds(circle.origin - r, r * 2)
    }
    
    /**
     * Creates a rectangle around line so that the line becomes one of the rectangle's diagonals
     */
    def aroundDiagonal(diagonal: Line) = between(diagonal.start, diagonal.end)
}

/**
 * Bounds limit a certain rectangular area of space. The rectangle is defined by two points 
 * and the edges go along x, y and z axes.
 * @author Mikko Hilpinen
 * @since 13.1.2017
 */
case class Bounds(val position: Vector3D, val size: Vector3D) extends ShapeConvertible with Area
{
    // COMPUTED PROPERTIES    ------------
    
    override def toShape = new java.awt.Rectangle(position.x.toInt, position.y.toInt, width.toInt, 
            height.toInt);
    
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
     * The diagonal line for this rectangle. Starts at the position coordinates and goes all the 
     * way to the opposite corner.
     */
    def diagonal = Line(position, position + size)
    
    /**
     * The four corners of the x-y side of the rectangle, in clockwise order starting from the
     * rectangle's position. All of the corners are projected to the x-y -plane.
     */
    def corners2D = 
    {
        val position2D = position.in2D
        val size2D = size.in2D
        Vector(position2D, position2D + size2D.copy(y = 0), position2D + size2D, 
                position2D + size2D.copy(x = 0))
    }
    
    /**
     * The four edges of the x-y side of the rectangle, in clockwise order starting from the x-wise edge.
     * All of the edges are projected to the x-y -plane
     */
    def edges2D = Line.edgesForVertices(corners2D)
    
    /**
     * A version of this rectangle that lies completely on the x-y plane
     */
    def in2D = Bounds(position.in2D, size.in2D)
    
    
    // IMPLEMENTED METHODS    ----------
    
    override def contains(coordinate: Vector3D) = coordinate >= position && coordinate <= position + size
    
    override def contains2D(coordinate: Vector3D) = in2D.contains(coordinate.in2D)
    
    
    // OTHER METHODS    ----------------
    
    /**
     * Creates a rounded rectangle based on this rectangle shape.
     * @param ruondingFactor How much the corners are rounded. 0 Means that the corners are not
     * rounded at all, 1 means that the corners are rounded as much as possible, so that the ends of
     * the shape become ellipsoid. Default value is 0.5
     */
    def toRoundedRectangle(roundingFactor: Double = 0.5) =
    {
        val rounding = math.min(width, height) * roundingFactor
        new RoundRectangle2D.Double(position.x, position.y, width, height, rounding, rounding)
    }
    
    /**
     * Checks whether the line completely lies within the rectangle bounds
     */
    def contains(line: Line): Boolean = contains(line.start) && contains(line.end)
    
    /**
     * Checks whether a set of bounds is contained within this bounds' area
     */
    def contains(bounds: Bounds): Boolean = contains(bounds.position) && contains(bounds.position + bounds.size)
    
    /**
     * Checks whether a line completely lies within the rectangle's bounds when the z-axis is
     * ignored
     */
    def contains2D(line: Line) = in2D.contains(line.in2D)
    
    /**
     * Checks whether a circle completely lies within the rectangle's bounds when the z-axis is 
     * ignored
     */
    def contains2D(circle: Circle): Boolean = contains2D(circle.origin) && 
            circleIntersection2D(circle).isEmpty;
    
    /**
     * Checks whether this set of bounds contains another bounds' area. Doesn't check the z-axis 
     * values for containment.
     */
    def contains2D(bounds: Bounds) = in2D.contains(bounds.in2D)
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided circle.
     * Both shapes are projected to the x-y plane before the check.
     */
    def circleIntersection2D(circle: Circle) = 
    {
        val circle2D = circle.in2D
        edges2D.flatMap { _.circleIntersection(circle2D) }
    }
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided line. 
     * Both shapes are projected to the x-y plane before the check.
     */
    def lineIntersection2D(line: Line) = 
    {
        val line2D = line.in2D
        edges2D.flatMap { _.intersection(line2D) }
    }
}