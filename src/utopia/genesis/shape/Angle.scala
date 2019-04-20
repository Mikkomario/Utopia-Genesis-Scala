package utopia.genesis.shape

import utopia.flow.util.Equatable
import utopia.genesis.util.Extensions._
import utopia.genesis.util.ApproximatelyEquatable
import utopia.genesis.shape.RotationDirection.Counterclockwise
import utopia.genesis.shape.RotationDirection.Clockwise

object Angle
{
    // ATTRIBUTES    -----------------------------
    
    /**
     * Angle that points to the left (180 degrees)
     */
    val left = ofRadians(math.Pi)
    
    /**
     * Angle that points to the right (0 degrees)
     */
    val right = ofRadians(0)
    
    /**
     * Angle that points up (270 degrees)
     */
    val up = ofRadians(3 * math.Pi / 2)
    
    /**
     * Angle that points down (90 degrees)
     */
    val down = ofRadians(math.Pi / 2)
    
    
    // FACTORIES    ------------------------------
    
    /**
     * Converts a radian angle to an angle instance
     */
    def ofRadians(radians: Double) = new Angle(radians)
    
    /**
     * Converts a degrees angle to an angle instance (some inaccuracy may occur since the value 
     * is converted to radians internally)
     */
    def ofDegrees(degrees: Double) = new Angle(degrees.toRadians)
}

/**
 * This class is used for storing a double value representing an angle (0 to 2*Pi radians). This 
 * class makes sure the angle stays in bounds and can be operated properly. Please note that 
 * Angle does NOT represent rotation.
 * @author Mikko Hilpinen
 * @since 30.6.2017
 */
class Angle(rawRadians: Double) extends Equatable with ApproximatelyEquatable[Angle]
{
    // ATTRIBUTES    ------------------
    
    /**
     * This angle in radians. Between 0 and 2*Pi
     */
    val toRadians = { val downscaled = rawRadians % (2 * math.Pi)
        if (downscaled < 0) downscaled + 2 * math.Pi else downscaled }
    
    
    // COMPUTED PROPERTIES    --------
    
    /**
     * This angle in degrees. between 0 and 360
     */
    lazy val toDegrees = toRadians.toDegrees
    
    override def properties = Vector(toRadians)
    
    override def toString = f"$toDegrees%1.2f degrees"
    
    def toRotation = Rotation(toRadians)
    
    
    // OPERATORS    ------------------
    
    /**
     * The necessary rotation from the other angle to the this angle. Returns the shortest 
     * route, which means that the value is always between -Pi and Pi
     */
    def -(other: Angle) = 
    {
        val rawValue = toRadians - other.toRadians
        if (rawValue > math.Pi)
        {
            // > 180 degrees positive -> < 180 degrees negative
            Rotation(2 * math.Pi - rawValue, Counterclockwise)
        }
        else if (rawValue < -math.Pi)
        {
            // > 180 degrees negative -> < 180 degrees positive
            Rotation(rawValue + 2 * math.Pi, Clockwise)
        }
        else
        {
            // Negative values are returned as positive counter-clockwise rotation
            if (rawValue < 0)
                Rotation(-rawValue, Counterclockwise)
            else
                Rotation(rawValue, Clockwise)
        }
    }
    
    /**
     * Applies a rotation (radians) to this angle in clockwise direction
     */
    def +(rotationRads: Double) = Angle.ofRadians(toRadians + rotationRads)
    
    /**
     * Applies a rotation to this angle
     */
    def +(rotation: Rotation): Angle = this + rotation.toDouble
    
    /**
     * Applies a rotation (radians) to this angle in counter-clockwise direction
     */
    def -(rotationRads: Double) = this + (-rotationRads)
    
    /**
     * Applies a negative rotation to this angle
     */
    def -(rotation: Rotation): Angle = this.-(rotation.toDouble)
    
    /**
     * Compares two angles without the requirement of being exactly equal
     */
    def ~==[B <: Angle](other: B) = toRadians ~== other.toRadians
}