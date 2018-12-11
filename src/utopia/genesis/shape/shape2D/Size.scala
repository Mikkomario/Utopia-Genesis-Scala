package utopia.genesis.shape.shape2D

import utopia.genesis.util.Extensions._
import utopia.flow.generic.ValueConversions._
import java.awt.Dimension
import utopia.flow.generic.ValueConvertible
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.SizeType
import utopia.flow.generic.ModelConvertible
import utopia.flow.datastructure.immutable.Model
import scala.collection.immutable.HashMap
import utopia.flow.generic.FromModelFactory
import utopia.flow.datastructure.template.Property
import utopia.genesis.util.ApproximatelyEquatable
import utopia.genesis.shape.Vector3D
import utopia.genesis.shape.Axis2D
import utopia.genesis.shape.X
import utopia.genesis.shape.Y
import java.awt.Insets

object Size extends FromModelFactory[Size]
{
    /**
     * A zero size
     */
    val zero = Size(0, 0)
    
    def apply(model: utopia.flow.datastructure.template.Model[Property]) = Some(
            Size(model("width").doubleOr(), model("height").doubleOr()));
    
    /**
     * Converts an awt dimension into size
     */
    def of(dimension: Dimension) = Size(dimension.width, dimension.height)
    
    /**
     * Converts awt insets into a size
     */
    def of(insets: Insets) = Size(insets.left + insets.right, insets.top + insets.bottom)
}

/**
* This class is used for representing 2 dimensional size of an area, doesn't specify position
* @author Mikko Hilpinen
* @since 20.11.2018
**/
case class Size(width: Double, height: Double) extends ApproximatelyEquatable[Size] 
        with ValueConvertible with ModelConvertible
{
    /**
     * The area of this size (width * height)
     */
    def area = width * height
    
    def toValue = new Value(Some(this), SizeType)
    
    def toModel = Model.fromMap(HashMap("width" -> width, "height" -> height))
    
    /**
     * A vector representation of this size
     */
    def toVector = Vector3D(width, height)
    
    /**
     * An awt representation of this size
     */
	def toDimension = new Dimension(width.toInt, height.toInt)
    
    /**
     * A non-negative version of this size
     */
    def positive = if (width >= 0 && height >= 0) this else Size(Math.max(width, 0), Math.max(height, 0))
    
    /**
     * A increased version of this size
     */
    def +(vector: Vector3D) = (toVector + vector).toSize
    
    /**
     * An increased version of this size
     */
    def +(other: Size): Size = this + other.toVector
    
    /**
     * A decreased version of this size
     */
    def -(vector: Vector3D) = this + (-vector)
    
    /**
     * A decreased version of this size
     */
    def -(other: Size): Size = this - other.toVector
    
    /**
     * A multiplied version of this size
     */
    def *(vector: Vector3D) = (toVector * vector).toSize
    
    /**
     * A multiplied version of this size
     */
    def *(modifier: Double) = Size(width * modifier, height * modifier)
    
    /**
     * A divided version of this size
     */
    def /(vector: Vector3D) = (toVector / vector).toSize
    
    /**
     * A divided version of this size
     */
    def /(modifier: Double) = Size(width / modifier, height / modifier)
    
    /**
     * A scaling modifier from the second size to this size
     */
    def /(other: Size) = toVector / other.toVector
    
    def ~==[B <: Size](other: B) = (width ~== other.width) && (height ~== other.height)
    
    /**
     * The length of a side of this size along the specified axis
     */
    def lengthAlong(axis: Axis2D) = 
    {
        axis match 
        {
            case X => width
            case Y => height
        }
    }
}