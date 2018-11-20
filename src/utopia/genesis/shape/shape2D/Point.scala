package utopia.genesis.shape.shape2D

import utopia.genesis.util.Extensions._
import utopia.flow.generic.ValueConversions._

import java.awt.geom.Point2D
import scala.collection.immutable.HashMap
import utopia.flow.generic.ValueConvertible
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.PointType
import utopia.flow.generic.ModelConvertible
import utopia.flow.datastructure.immutable.Model
import utopia.flow.generic.FromModelFactory
import utopia.flow.datastructure.template.Property
import utopia.genesis.util.ApproximatelyEquatable
import utopia.genesis.shape.Axis
import utopia.genesis.shape.X
import utopia.genesis.shape.Y
import utopia.genesis.shape.Vector3D
import utopia.genesis.shape.Axis2D

object Point extends FromModelFactory[Point]
{
    val origin = Point(0, 0)
    
    def apply(model: utopia.flow.datastructure.template.Model[Property]) = Some(
            Point(model("x").doubleOr(), model("y").doubleOr()))
    
    /**
     * Converts an awt point to Utopia point
     */
    def of(point: java.awt.Point) = Point(point.getX, point.getY)
    
    /**
     * Converts an awt geom point to Utopia point
     */
    def of(point: Point2D) = Point(point.getX, point.getY)
    
    /**
     * Converts a coordinate map into a point
     */
    def of(map: Map[Axis, Double]) = Point(map.get(X).getOrElse(0), map.get(Y).getOrElse(0))
    
    /**
     * A combination of the points with minimum x and y coordinates
     */
    def topLeft(a: Point, b: Point) = Point(Math.min(a.x, b.x), Math.min(a.y, b.y))
    
    /**
     * A combination of the points with minimum x and y coordinates. None if collection is empty
     */
    def topLeftOption(points: TraversableOnce[Point]) = points.reduceLeftOption(topLeft)
    
    /**
     * A combination of the points with minimum x and y coordinates
     */
    def topLeft(points: TraversableOnce[Point]): Point = topLeftOption(points).getOrElse(origin)
    
    /**
     * A combination of the points with maximum x and y coordinates
     */
    def bottomRight(a: Point, b: Point) = Point(Math.max(a.x, b.x), Math.max(a.y, b.y))
    
    /**
     * A combination of the points with maximum x and y coordinates. None if collection is empty
     */
    def bottomRightOption(points: TraversableOnce[Point]) = points.reduceLeftOption(bottomRight)
    
    /**
     * A combination of the points with maximum x and y coordinates
     */
    def bottomRight(points: TraversableOnce[Point]): Point = bottomRightOption(points).getOrElse(origin)
}

/**
* A point represents a coordinate pair in a 2 dimensional space
* @author Mikko Hilpinen
* @since 20.11.2018
**/
case class Point(x: Double, y: Double) extends ApproximatelyEquatable[Point] with 
        ValueConvertible with ModelConvertible
{
    def toValue = new Value(Some(this), PointType)
    
    def toModel = Model.fromMap(HashMap("x" -> x, "y" -> y))
    
    /**
     * A map of this point's coordinates
     */
	def toMap = HashMap(X -> x, Y -> y)
	
	/**
	 * A vector representation of this point
	 */
	def toVector = Vector3D(x, y)
	
	/**
	 * An awt representation of this point
	 */
	def toAwtPoint = new java.awt.Point(x.toInt, y.toInt)
	
	/**
	 * An awt geom representation of this point
	 */
	def toAwtPoint2D = new Point2D.Double(x, y)
	
	def ~==[B <: Point](other: B) = (x ~== other.x) && (y ~== other.y)
	
	/**
	 * A translated position
	 */
	def +(vector: Vector3D) = Point(x + vector.x, y + vector.y)
	
	/**
	 * A translated position
	 */
	def -(vector: Vector3D) = this.+(-vector)
	
	/**
	 * A vector between these points
	 */
	def -(other: Point) = toVector - other.toVector
	
	/**
	 * Connects this point with another, forming a line
	 */
	def lineTo(other: Point) = Line(this, other)
	
	/**
	 * This point's coordinate on the specified axis
	 */
	def along(axis: Axis2D) = 
	{
        axis match 
        {
            case X => x 
            case Y => y
        }
	}
}