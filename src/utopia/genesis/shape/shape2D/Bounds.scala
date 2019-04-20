package utopia.genesis.shape.shape2D

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
import utopia.genesis.shape.Vector3D
import utopia.genesis.shape.X

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
        val topLeft = p1 topLeft p2
        val bottomRight = p1 bottomRight p2
        
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
    def aroundOption(bounds: Traversable[Bounds]) = 
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
     * Creates a bounds instance that contains all specified bounds. Will throw on empty collection
     */
    def around(bounds: Traversable[Bounds]) = aroundOption(bounds).get
    
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
case class Bounds(position: Point, size: Size) extends Rectangular with ValueConvertible with ModelConvertible
{
    // COMPUTED PROPERTIES    ------------
    
    /**
     * An awt counterpart of these bounds
     */
    def toAwt = new java.awt.Rectangle(position.x.toInt, position.y.toInt, width.toInt, height.toInt)
    
    /**
     * The diagonal line for this rectangle. Starts at the position coordinates and goes all the 
     * way to the opposite corner.
     */
    def diagonal = Line(topLeft, bottomRight)
    
    
    // IMPLEMENTED METHODS    ----------
    
    def topLeft = position
    
    override def width = size.width
    
    override def height = size.height
    
    override def toValue = new Value(Some(this), BoundsType)
    
    override def toModel = Model(Vector("position" -> position, "size" -> size))
    
    override def toShape = toAwt
    
    override def leftLength = size.height
    
    override def top = X(size.width)
    
    override def contains(point: Point) = point.x >= topLeft.x && point.y >= topLeft.y &&
            point.x <= bottomRight.x && point.y <= bottomRight.y
    
    
    // OTHER METHODS    ----------------
    
    /**
     * Creates a rounded rectangle based on this rectangle shape.
     * @param roundingFactor How much the corners are rounded. 0 Means that the corners are not
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
    def contains(bounds: Bounds): Boolean = contains(bounds.topLeft) && contains(bounds.bottomRight)
    
    /**
     * Checks whether a circle completely lies within the rectangle's bounds when the z-axis is 
     * ignored
     */
    def contains(circle: Circle): Boolean = contains(circle.origin) && circleIntersection(circle).isEmpty
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided circle
     */
    def circleIntersection(circle: Circle) = sides.flatMap { _.circleIntersection(circle) }
    
    /**
     * Finds the intersection points between the edges of this rectangle and the provided line. 
     * Both shapes are projected to the x-y plane before the check.
     */
    def lineIntersection(line: Line) = sides.flatMap { _.intersection(line) }
}