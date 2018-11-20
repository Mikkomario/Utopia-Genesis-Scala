package utopia.genesis.shape

import utopia.genesis.util.Extensions._

import utopia.genesis.util.ApproximatelyEquatable
import utopia.flow.util.Equatable
import utopia.genesis.shape.RotationDirection.Clockwise

object Rotation
{
    /**
     * A zero rotation
     */
    val zero = Rotation(0)
    
    /**
     * Converts a radian amount to a rotation
     */
    def ofRadians(rads: Double, direction: RotationDirection = Clockwise) = Rotation(rads, direction)
    
    /**
     * Converts a degree amount to a rotation
     */
    def ofDegrees(degrees: Double, direction: RotationDirection = Clockwise) = Rotation(degrees.toRadians, direction)
}

/**
* This class represents a rotation around a certain axis
* @author Mikko Hilpinen
* @since 21.11.2018
**/
case class Rotation(radians: Double, direction: RotationDirection = Clockwise) extends Equatable with 
        ApproximatelyEquatable[Rotation]
{
    // ATTRIBUTES    ---------------------
    
    /**
     * This rotation in degrees
     */
	lazy val degrees = radians.toDegrees
	
	
	// PROPS    --------------------------
	
	/**
	 * A radian double value of this rotation
	 */
	def toDouble = radians * direction.modifier
	
	/**
	 * This rotation as an angle from origin (right)
	 */
	def toAngle = new Angle(toDouble)
	
	
	// IMPLEMENTED    --------------------
	
	def properties = Vector(radians * direction.modifier)
	
	override def toString() = f"$degrees%1.2f degrees $direction"
	
	def ~==[B <: Rotation](other: B) = radians * direction.modifier ~== other.radians * direction.modifier
	
	
	// OPERATORS    ----------------------
	
	/**
	 * An opposite rotation
	 */
	def unary_- = Rotation(radians, direction.opposite)
	
	/**
	 * Combines two rotations
	 */
	def +(other: Rotation) = 
	{
	    if (direction == other.direction)
	        Rotation(radians + other.radians, direction)
	    else if (radians >= other.radians)
	        Rotation(radians - other.radians, direction)
	    else
	        Rotation(other.radians - radians, other.direction)
	}
	
	/**
	 * Subtracts a rotation from this one
	 */
	def -(other: Rotation) = this + (-other)
	
	/**
	 * Returns a multiplied version of this rotation
	 */
	def *(modifier: Double) = Rotation(radians * modifier, direction)
	
	/**
	 * Returns a divided version of this rotation
	 */
	def /(modifier: Double) = Rotation(radians / modifier, direction)
}