package utopia.genesis.shape

object Axis2D
{
    /**
     * All possible values of this trait
     */
    val values: Vector[Axis2D] = Vector(X, Y)    
}

object Axis
{
    /**
     * All possible values of this trait
     */
    val values: Vector[Axis] = Axis2D.values :+ Z
}

/**
* An axis represents a direction
* @author Mikko Hilpinen
* @since 20.11.2018
**/
sealed trait Axis
{
    /**
     * An unit vector along this axis
     */
    def toUnitVector: Vector3D
    
    /**
     * A vector along this axis with the specified length
     */
    def apply(length: Double) = toUnitVector * length
}

/**
 * The Z-axis is used in 3 dimensional shapes to represent depth
 */
case object Z extends Axis
{
    def toUnitVector = Vector3D(0, 0, 1)    
}

/**
 * This trait is common for axes on the 2D-plane
 */
sealed trait Axis2D extends Axis
{
    /**
     * The axis perpendicular to this one
     */
    def perpendicular: Axis2D
}

/**
 * The X-axis typically represents object width / horizontal coordinates
 */
case object X extends Axis2D
{
    def toUnitVector = Vector3D(1)
    def perpendicular = Y
}

/**
 * The Y-axis typically represents object height / vertical coordinates
 */
case object Y extends Axis2D
{
    def toUnitVector = Vector3D(0, 1)    
    def perpendicular = X
}