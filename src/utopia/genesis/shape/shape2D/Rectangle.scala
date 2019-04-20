package utopia.genesis.shape.shape2D

import utopia.flow.generic.ValueConversions._
import utopia.genesis.generic.GenesisValue._
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.template
import utopia.flow.datastructure.template.Property
import utopia.flow.generic.{FromModelFactory, ModelConvertible}
import utopia.genesis.shape.Vector3D

object Rectangle extends FromModelFactory[Rectangle]
{
	val zero = Rectangle(Point.origin, Vector3D.zero, 0)
	
	override def apply(model: template.Model[Property]): Option[Rectangle] =
	{
		val topLeft = model("topLeft").point
		val topEdge = model("top").vector3D
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
  * @param top The top vector of this rectangle
  * @param leftLength The length of the left edge of this rectangle
  */
case class Rectangle(topLeft: Point, top: Vector3D, leftLength: Double) extends Rectangular with ModelConvertible
{
	// ATTRIBUTES	-----------------
	
	override lazy val left = super.left
	
	
	// COMPUTED	---------------------
	
	/**
	  * @return The size of this rectangle (rotation lost)
	  */
	def toSize = Size(width, height)
	
	/**
	  * @return The bounds of this rectangle (rotation lost)
	  */
	def toBounds = Bounds(topLeft, toSize)
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector(("topLeft", topLeft), ("top", top), ("leftEdgeLength", leftLength)))
}
