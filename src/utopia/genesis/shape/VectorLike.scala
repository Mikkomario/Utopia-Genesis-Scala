package utopia.genesis.shape

import scala.collection.immutable.{HashMap, VectorBuilder}
import utopia.genesis.shape.Axis._
import utopia.flow.util.CollectionExtensions._
import utopia.genesis.util.{Arithmetic, Distance}

object VectorLike
{
	/**
	  * Forms the average of the provided vectorLike elements. Elements must be non-empty.
	  * @param elements Elements
	  * @tparam A The result type
	  * @return The average of the provided VectorLike elements
	  */
	def average[A <: VectorLike[A]](elements: Traversable[A]) =
	{
		val total = elements.reduce { _ + _ }
		total / elements.size
	}
}

/**
  * This trait is implemented by simple shape classes that can be represented as an vector of double numbers, each
  * matching an axis (X, Y, Z, ...)
  * @tparam Repr the concrete implementing class
  */
trait VectorLike[+Repr <: VectorLike[Repr]] extends Arithmetic[VectorLike[_], Repr] with Distance with Dimensional[Double]
	with VectorProjectable[Vector3D]
{
	// ABSTRACT	---------------------
	
	/**
	  * @return The X, Y, Z ... dimensions of this vectorlike instance. No specific length required, however.
	  */
	def dimensions: Vector[Double]
	
	/**
	  * Builds a new vectorlike instance from the provided dimensions
	  * @param dimensions A set of dimensions
	  * @return A parsed version of the dimensions
	  */
	def buildCopy(dimensions: Vector[Double]): Repr
	
	
	// IMPLEMENTED	-----------------
	
	def along(axis: Axis) =
	{
		val index = axis match
		{
			case X => 0
			case Y => 1
			case Z => 2
		}
		dimensions.getOrElse(index, 0)
	}
	
	override def length = math.sqrt(this dot this)
	
	override def +(other: VectorLike[_]) = combineWith(other, { _ + _ })
	
	override def -(other: VectorLike[_]) = combineWith(other, { _ - _ })
	
	override def *(n: Double) = map { _ * n }
	
	def projectedOver(other: Vector3D) = other * (dot(other) / other.dot(other))
	
	override def xProjection = X(x)
	
	override def yProjection = Y(y)
	
	override def zProjection = Z(z)
	
	
	// COMPUTED	---------------------
	
	/**
	  * @return The x and y -dimensions of this vectorlike element
	  */
	def dimensions2D = dimensions.take(2)
	
	/**
	  * A coordinate map representation of this vectorlike element
	  */
	def toMap = HashMap(X -> x, Y -> y, Z -> z)
	
	/**
	  * a copy of this element where the coordinate values have been cut to integer numbers.
	  * This operation always rounds the numbers down, never up.
	  */
	def floor = map(math.floor)
	
	/**
	  * a copy of this element where the coordinate values have been increased to whole integer
	  * numbers. This operation always rounds the numbers up, never down.
	  */
	def ceil = map(math.ceil)
	
	/**
	  * a copy of this element where the coordinates have been rounded to nearest integer / long
	  * numbers.
	  */
	def rounded = map { math.round(_).toDouble }
	
	/**
	  * A version of this vector where all values are at least 0
	  */
	def positive = map { _ max 0 }
	
	
	// OPERATORS	----------------------
	
	/**
	  * @param x X translation
	  * @param more Y, Z, ... translation
	  * @return A translated version of this element
	  */
	def +(x: Double, more: Double*) = combineDimensions(x +: more, { _ + _ })
	
	/**
	  * @param adjust Translation on target axis
	  * @param axis Target axis
	  * @return A copy of this element with one dimension translated
	  */
	def +(adjust: Double, axis: Axis) = map(_ + adjust, axis)
	
	/**
	  * @param adjust Translation on target axis
	  * @param axis Target axis
	  * @return A copy of this element with one dimension translated
	  */
	def -(adjust: Double, axis: Axis) = this + (-adjust, axis)
	
	/**
	  * @param x X translation (negative)
	  * @param more Y, Z, ... translation (negative)
	  * @return A translated version of this element
	  */
	def -(x: Double, more: Double*) = combineDimensions(x +: more, { _ - _ })
	
	/**
	  * @param other Another vectorlike element
	  * @return This element multiplied on each axis of the provided element
	  */
	def *(other: VectorLike[_]) = combineWith(other, { _ * _ })
	
	/**
	  * @param n A multiplier for specified axis
	  * @param axis Target axis
	  * @return A copy of this element with one dimension multiplied
	  */
	def *(n: Double, axis: Axis) = map(_ * n, axis)
	
	/**
	  * @param other Another vectorlike element
	  * @return This element divided on each axis of the provided element. Dividing by 0 is ignored
	  */
	def /(other: VectorLike[_]) = combineWith(other, { case (a, b) => if (b == 0) a else a / b })
	
	/**
	  * @param n A divider for target axis
	  * @param axis Target axis
	  * @return This element divided on specified axis
	  */
	def /(n: Double, axis: Axis) = if (n == 0) buildCopy(dimensions) else map(_ / n, axis)
	
	
	// OTHER	--------------------------
	
	/**
	  * The dot product between this and another vector
	  */
	def dot(other: VectorLike[_]) = (this * other).dimensions.sum
	
	/**
	  * Maps all dimensions of this vectorlike element
	  * @param f A mapping function
	  * @return A mapped version of this element
	  */
	def map(f: Double => Double) = buildCopy(dimensions.map(f))
	
	/**
	  * Transforms a coordinate of this vectorlike element and returns the transformed element
	  * @param f The map function that maps a current coordinate into a new coordinate
	  * @param along the axis that specifies the mapped coordinate
	  * @return A copy of this element with the mapped coordinate
	  */
	def map(f: Double => Double, along: Axis) =
	{
		val myDimensions = dimensions
		val mapIndex = along match
		{
			case X => 0
			case Y => 1
			case Z => 2
		}
		
		if (myDimensions.size <= mapIndex)
			buildCopy(dimensions)
		else
		{
			val firstPart = myDimensions.take(mapIndex) :+ f(myDimensions(mapIndex))
			buildCopy(firstPart ++ myDimensions.drop(mapIndex + 1))
		}
	}
	
	/**
	  * Merges this vectorlike element with another element using a merge function. Dimensions not present in one of the
	  * elements will be treated as 0
	  * @param other Another vectorlike element
	  * @param merge A merge function
	  * @return A new element with merged or copied dimensions
	  */
	def combineWith(other: VectorLike[_], merge: (Double, Double) => Double) = combineDimensions(other.dimensions, merge)
	
	private def combineDimensions(dimensions: Seq[Double], merge: (Double, Double) => Double) =
	{
		val myDimensions = this.dimensions
		val otherDimensions = dimensions
		
		val builder = new VectorBuilder[Double]()
		for (i <- 0 until (myDimensions.size min otherDimensions.size) )
		{
			builder += merge(myDimensions(i), otherDimensions(i))
		}
		for (i <- otherDimensions.size until myDimensions.size) { builder += merge(myDimensions(i), 0) }
		for (i <- myDimensions.size until otherDimensions.size) { builder += merge(0, otherDimensions(i)) }
		
		buildCopy(builder.result())
	}
	
	/**
	  * @param other Another vectorlike element
	  * @return The minimum combination of these two elements where each dimension is taken from the smaller alternative
	  */
	def min(other: VectorLike[_]) = combineWith(other, { _ min _ })
	
	/**
	  * The top left corner of a bounds between these two elements. In other words,
	  * creates a vector that has the smallest available value on each axis from the two candidates
	  * @param other Another element
	  * @return a minimum of these two elements on each axis
	  */
	def topLeft(other: VectorLike[_]) = this min other
	
	/**
	  * @param other Another vectorlike element
	  * @return A maximum combination of these two elements where each dimension is taken from the larger alternative
	  */
	def max(other: VectorLike[_]) = combineWith(other, { _ max _ })
	
	/**
	  * The bottom right corner of a bounds between the two vertices. In other words,
	  * creates a vector that has the largest available value on each axis from the two candidates
	  * @param other Another element
	  * @return A maximum of these two elements on each axis
	  */
	def bottomRight(other: VectorLike[_]) = combineWith(other, { _ max _ })
}