package utopia.genesis.shape.shape2D

import java.awt.{Polygon, Shape}

import utopia.flow.util.CollectionExtensions._
import utopia.genesis.shape.Vector3D

import scala.collection.immutable.VectorBuilder

/**
  * This trait is extended by 2D shapes that have 3 or more corners
  * @author Mikko Hilpinen
  * @since 14.4.2019, v2+
  */
trait Polygonic extends ShapeConvertible with Projectable with Area2D
{
	// ABSTRACT	----------------
	
	/**
	  * @return The corners of this shape, in clockwise order
	  */
	def corners: Vector[Point]
	
	
	// COMPUTED	----------------
	
	/**
	  * @return The sides of this shape, in clocwise order
	  */
	def sides =
	{
		val c = corners
		if (c.size < 2)
			Vector()
		else
		{
			val buffer = new VectorBuilder[Line]()
			var start = c.head
			
			c.tail.foreach
			{
				corner =>
					buffer += Line(start, corner)
					start = corner
			}
			
			buffer.result()
		}
	}
	
	/**
	  * @return The edges of this shape in clockwise order. Same as sides, except in vector form
	  */
	def edges = sides.map { _.vector }
	
	/**
	  * @return The collision axes that should be considered when testing this instance
	  */
	def collisionAxes = edges.distinctWith { _ isParallelWith _ }.map { _.normal2D }
	
	/**
	  * @return The bounds around this polygonic instance
	  */
	def bounds =
	{
		val c = corners
		val topLeft = Point.topLeft(c)
		val bottomRigth = Point.bottomRight(c)
		
		Bounds(topLeft, (bottomRigth - topLeft).toSize)
	}
	
	
	// IMPLEMENTED	------------
	
	override def toShape: Shape =
	{
		val c = corners
		val x = c.map { _.x.toInt }.toArray
		val y = c.map { _.y.toInt }.toArray
		
		new Polygon(x, y, c.size)
	}
	
	override def projectedOver(axis: Vector3D) =
	{
		val projectedCorners = corners.map { _.toVector.projectedOver(axis).toPoint }
		val start = projectedCorners.min
		val end = projectedCorners.max
		
		Line(start, end)
	}
	
	override def contains(point: Point) = collisionAxes.forall { containsProjection(point, _) }
	
	
	// OTHER	---------------
	
	/**
	  * Calculates the minimum translation vector that would get these two projectable shapes out of
	  * a collision situation
	  * @param other another polygonic instance
	  * @return The minimum translation vector that gets these two shapes out of a collision situation
	  * or none if there is no collision
	  */
	def collisionMtvWith(other: Polygonic): Option[Vector3D] = collisionMtvWith(other,
		(collisionAxes ++ other.collisionAxes).distinctWith { _ isParallelWith _ })
}
