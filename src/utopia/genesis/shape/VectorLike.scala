package utopia.genesis.shape

import scala.collection.immutable.{HashMap, VectorBuilder}

import utopia.flow.util.CollectionExtensions._

/**
  * This trait is implemented by simple shape classes that can be represented as an vector of double numbers, each
  * matching an axis (X, Y, Z, ...)
  * @tparam Repr the concrete implementing class
  */
trait VectorLike[Repr <: VectorLike[_]]
{
	// TYPES	---------------------
	
	type AnyVectorLike = VectorLike[_ <: VectorLike[_]]
	
	
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
	
	
	// COMPUTED	---------------------
	
	/**
	  * @return The x-component of this vectorlike element
	  */
	def x = dimensions.getOrElse(0, 0)
	
	/**
	  * @return The y-component of this vectorlike element
	  */
	def y = dimensions.getOrElse(1, 0)
	
	/**
	  * @return The z-component of this vectorlike element (0 if this element doesn't have a z component)
	  */
	def z = dimensions.getOrElse(2, 0)
	
	/**
	  * @return A projection of this vectorlike element as a vector along the x-axis (only contains x-component)
	  */
	def xProjection = X(x)
	
	/**
	  * @return A projection of this vectorlike element as a vector along the y-axis (only contains y-component)
	  */
	def yProjection = Y(y)
	
	/**
	  * @return A projection of this vectorlike element as a vector along the z-axis (only contains z-component)
	  */
	def zProjection = Z(z)
	
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
	  * This vectorlike element inverted
	  */
	def unary_- = map {-_}
	
	/**
	  * @param other Another vectorlike element
	  * @return The combination of these two elements
	  */
	def +(other: VectorLike[_]) = combineWith(other, { _ + _ })
	
	/**
	  * @param x X translation
	  * @param more Y, Z, ... translation
	  * @return A translated version of this element
	  */
	def +(x: Double, more: Double*) = combineDimensions(x +: more, { _ + _ })
	
	/**
	  * @param other Another vectorlike element
	  * @return The subtraction of these two elements
	  */
	def -(other: AnyVectorLike) = this + (-other)
	
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
	  * @param n A multiplier for all axes
	  * @return This element multiplied on all axes with the specified multiplier
	  */
	def *(n: Double) = map { _ * n }
	
	/**
	  * @param other Another vectorlike element
	  * @return This element divided on each axis of the provided element. Dividing by 0 is ignored
	  */
	def /(other: VectorLike[_]) = combineWith(other, { case (a, b) => if (b == 0) a else a / b })
	
	/**
	  * @param n A divider for all axes
	  * @return This element divided on all axes with the speficied divider
	  */
	def /(n: Double) = if (n == 0) buildCopy(dimensions) else map { _ / n }
	
	
	// OTHER	--------------------------
	
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
	  * A coordinate of this element along the specified axis
	  */
	def along(axis: Axis) = axis match
	{
		case X => x
		case Y => y
		case Z => z
	}
	
	/**
	  * @param axis Target axis
	  * @return The length of this element along the axis perpendicular to the provided axis
	  */
	def perpendicularTo(axis: Axis2D) = along(axis.perpendicular)
	
	/**
	  * @param axis Target axis
	  * @return A vector projection of this element over the specified axis
	  */
	def projectedOver(axis: Axis) = axis match
	{
		case X => xProjection
		case Y => yProjection
		case Z => zProjection
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
	  * The top left corner of a bounds between these two elements. In other words,
	  * creates a vector that has the smallest available value on each axis from the two candidates
	  * @param other Another element
	  * @return a minimum of these two elements on each axis
	  */
	def topLeft(other: VectorLike[_]) = combineWith(other, { _ min _ })
	
	/**
	  * The bottom right corner of a bounds between the two vertices. In other words,
	  * creates a vector that has the largest available value on each axis from the two candidates
	  * @param other Another element
	  * @return A maximum of these two elements on each axis
	  */
	def bottomRight(other: VectorLike[_]) = combineWith(other, { _ max _ })
}