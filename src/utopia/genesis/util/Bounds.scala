package utopia.genesis.util

import java.awt.geom.RoundRectangle2D
import utopia.flow.generic.ValueConvertible
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.BoundsType
import utopia.flow.generic.ModelConvertible
import utopia.flow.datastructure.immutable.Model
import utopia.flow.generic.ValueConversions._
import utopia.flow.datastructure.template
import utopia.flow.generic.FromModelFactory
import utopia.flow.datastructure.template.Property
import utopia.genesis.generic.GenesisValue._

object Bounds extends FromModelFactory[Bounds]
{
    // ATTRIBUTES    ----------------------
    
    /**
     * A zero bounds
     */
    val zero = Bounds(Point.origin, Size.zero)
    
    
    // OPERATORS    -----------------------
    
    override def apply(model: template.Model[Property]) = Some(
            Bounds(model("position").pointOr(), model("size").sizeOr()))
    
    
    // OTHER METHODS    -------------------
    
    /**
     * Creates a rectangle that contains the area between the two coordinates. The order 
     * of the coordinates does not matter.
     */
    def between(p1: Point, p2: Point) = 
    {
        val topLeft = Point.topLeft(p1, p2)
        val bottomRight = Point.bottomRight(p1, p2)
        
        Bounds(topLeft, (bottomRight - topLeft).toSize)
    }
    
    /**
     * Creates a set of bounds around a circle so that the whole sphere is contained.
     */
    def around(circle: Circle) = 
    {
        val r = Vector3D(circle.radius, circle.radius, circle.radius)
        Bounds(circle.origin - r, (r * 2).toSize)
    }
    
    /**
     * Creates a set of bounds that contains all of the provided bounds. Returns none if the provided 
     * collection is empty.
     */
    // TODO: Add an optional version separately
    def around(bounds: Traversable[Bounds]) =
    {
        if (bounds.isEmpty)
            None
        else
        {
            val topLeft = Point.topLeft(bounds.map{ _.topLeft })
            val bottomRight = Point.bottomRight(bounds.map { _.bottomRight })
            
            Some(between(topLeft, bottomRight))
        }
    }
    
    /**
     * Creates a rectangle around line so that the line becomes one of the rectangle's diagonals
     */
    def aroundDiagonal(diagonal: Line) = between(diagonal.start, diagonal.end)
}

/**
 * Bounds limit a certain rectangular area of space. The rectangle is defined by two points 
 * and the edges go along x and y axes.
 * @author Mikko Hilpinen
 * @since 13.1.2017
 */
// TODO: Separate into 2D and 3D shapes (rectangle and box)
case class Bounds(val position: Point, val size: Size) extends ShapeConvertible with 
        Area with ValueConvertible with ModelConvertible
{
    // COMPUTED PROPERTIES    ------------
    
    override def toShape = new java.awt.Rectangle(position.x.toInt, position.y.toInt, 
            width.toInt, height.toInt);
    
    override def toValue = new Value(Some(this), BoundsType)
    
    override def toModel = Model(Vector("position" -> position, "size" -> size))
    
    /**
     * The width of the rectangle / cube
     */
    def width = size.width
    
    /**
     * The height of the rectangle / cube
     */
    def height = size.height
    
    /**
     * The smallest (on all axes) coordinate that is contained within these bounds
     */
    def topLeft = position
    
    /**
     * The largest (on all axes) coordinate that is contained within these bounds
     */
    def bottomRight = position + size.toVector
    
    /**
     * The top right corner of this rectangle
     */
    def topRight = position + size.toVector.projectedOver(X)
    
    /**
     * The bottom left corner of this rectangle
     */
    def bottomLeft = position + size.toVector.projectedOver(Y)
    
    /**
     * The area of the x-y side of this rectangle in square pixels
     */
    def area = size.area
    
    /**
     * The diagonal line for this rectangle. Starts at the position coordinates and goes all the 
     * way to the opposite corner.
     */
    def diagonal = Line(topLeft, bottomRight)
    
    /**
     * The four corners of the this rectangle, in clockwise order starting from the
     * rectangle's position.
     */
    def corners = Vector(topLeft, topRight, bottomRight, bottomLeft)
    
    /**
     * The four corners of the x-y side of the rectangle, in clockwise order starting from the
     * rectangle's position. All of the corners are projected to the x-y -plane.
     */
    @deprecated("Please use corners instead", "v1.1.2")
    def corners2D = corners
    
    /**
     * The four edges of the x-y side of the rectangle, in clockwise order starting from the x-wise edge.
     * All of the edges are projected to the x-y -plane
     */
    def edges = Line.edgesForVertices(corners)
    
    
    // IMPLEMENTED METHODS    ----------
    
    // TODO: FIX for 2D
    override def contains(coordinate: Vector3D) = Vector3D.forall(coordinate, topLeft.toVector, { _ >= _ }) && 
            Vector3D.forall(coordinate, bottomRight.toVector, { _ <= _ })
    
    override def contains2D(coordinate: Vector3D) = contains(coordinate.in2D)
    
    
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
     * Checks whether the specified point is inside this rectangle
     */
    def contains(point: Point): Boolean = contains(point.toVector)
    
    /**
     * Checks whether the line completely lies within the rectangle bounds
     */
    def contains(line: Line): Boolean = contains(line.start) && contains(line.end)
    
    /**
     * Checks whether a set of bounds is contained within this bounds' area
     */
    def contains(bounds: Bounds): Boolean = contains(bounds.topLeft) && contains(bounds.bottomRight)
    
    /**
     * Checks whether a circle completely lies within the rectangle's bounds when the z-axis is 
     * ignored
     */
    def contains(circle: Circle): Boolean = contains(circle.origin) && 
            circleIntersection(circle).isEmpty;
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided circle
     */
    def circleIntersection(circle: Circle) = edges.flatMap { _.circleIntersection(circle) }
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided line. 
     * Both shapes are projected to the x-y plane before the check.
     */
    def lineIntersection(line: Line) = edges.flatMap { _.intersection(line) }
}