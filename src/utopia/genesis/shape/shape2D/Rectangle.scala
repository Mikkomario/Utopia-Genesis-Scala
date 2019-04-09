package utopia.genesis.shape.shape2D

import java.awt.Polygon

import utopia.flow.generic.ValueConversions._
import utopia.genesis.generic.GenesisValue._
import utopia.flow.datastructure.immutable.{Model, Value}
import utopia.flow.datastructure.template
import utopia.flow.datastructure.template.Property
import utopia.flow.generic.{FromModelFactory, ModelConvertible, ValueConvertible}
import utopia.genesis.generic.RectangleType
import utopia.genesis.shape.Vector3D

object Rectangle extends FromModelFactory[Rectangle]
{
	val zero = Rectangle(Point.origin, Vector3D.zero, 0)
	
	override def apply(model: template.Model[Property]): Option[Rectangle] =
	{
		val topLeft = model("topLeft").point
		val topEdge = model("topEdge").vector3D
		val leftEdgeLength = model("leftEdgeLength").double
		
		if (topLeft.isDefined && topEdge.isDefined && leftEdgeLength.isDefined)
			Some(Rectangle(topLeft.get, topEdge.get, leftEdgeLength.get))
		else
			None
	}
}

/**
  * Rectangles are 2D shapes that have four sides and where each corner is 90 degrees
  * @param topLeft The top left corner of this rectangle
  * @param topEdge The top edge (vector) of this rectangle
  * @param leftEdgeLenght The length of the left edge of this rectangle
  */
case class Rectangle(topLeft: Point, topEdge: Vector3D, leftEdgeLenght: Double) extends ShapeConvertible with Area2D
	with ValueConvertible with ModelConvertible
{
	// ATTRIBUTES	-----------------
	
	/**
	  * The left side edge (vector) of this rectangle
	  */
	val leftEdge = topEdge.normal2D.withLength(leftEdgeLenght)
	
	
	// COMPUTED	---------------------
	
	/**
	  * @return The top right corner of this rectangle
	  */
	def topRight = topLeft + topEdge
	
	/**
	  * @return The bottom right corner of this rectangle
	  */
	def bottomLeft = topLeft + leftEdge
	
	/**
	  * @return The bottom right corner of this rectangle
	  */
	def bottomRight = bottomLeft + topEdge
	
	/**
	  * @return The top side of this rectangle (left to right)
	  */
	def top = Line(topLeft, topRight)
	
	/**
	  * @return The right side of this rectangle (top to bottom)
	  */
	def right = Line(topRight, bottomRight)
	
	/**
	  * @return The bottom side of this rectangle (right to left)
	  */
	def bottom = Line(bottomRight, bottomLeft)
	
	/**
	  * @return The left side of this rectangle (bottom to top)
	  */
	def left = Line(bottomLeft, topLeft)
	
	/**
	  * @return The width of this rectangle (not necessarily X-wise)
	  */
	def width = topEdge.length
	
	/**
	  * @return The height of this rectangle (not necessarily Y-wise)
	  */
	def height = leftEdge.length
	
	/**
	  * @return The size of this rectangle (rotation lost)
	  */
	def size = Size(width, height)
	
	/**
	  * @return The bounds of this rectangle (rotation lost)
	  */
	def bounds = Bounds(topLeft, size)
	
	/**
	  * @return The four corners of this rectangle. Starting from topLeft and going clockwise.
	  */
	def corners = Vector(topLeft, topRight, bottomRight, bottomLeft)
	
	/**
	  * @return The four sides of this rectangle. Starting from topLeft and going clockwise.
	  */
	def sides = Vector(top, right, bottom, left)
	
	
	// IMPLEMENTED	--------------------
	
	override implicit def toValue: Value = new Value(Some(this), RectangleType)
	
	override def toModel = Model(Vector(("topLeft", topLeft), ("topEdge", topEdge), ("leftEdgeLength", leftEdgeLenght)))
	
	override def toShape =
	{
		val c = corners
		val x = c.map { _.x.toInt }.toArray
		val y = c.map { _.y.toInt }.toArray
		
		new Polygon(x, y, c.size)
	}
	
	override def contains(point: Point) =
	{
		// Compares projections over top ("x") and left ("y") edges
		val v = (point - topLeft).toVector
		val xProj = v.projectedOver(topEdge)
		
		if (xProj.x < 0 || xProj.y < 0 || xProj.length > width)
			false
		else
		{
			val yProj = v.projectedOver(leftEdge)
			
			if (yProj.x < 0 || yProj.y < 0 || yProj.length > height)
				false
			else
				true
		}
	}
}
