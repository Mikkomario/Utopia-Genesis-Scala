package utopia.genesis.util

import java.awt.geom.AffineTransform

object Transformation
{
    val identity = Transformation()    
}

/**
 * Transformations represent an object's state in a world, it's angle, position, scaling, etc.
 * Transformations can be applied over each other or points, lines, etc.
 * @author Mikko Hilpinen
 * @since 29.12.2016
 */
case class Transformation(val position: Vector3D = Vector3D.zero, 
        val scaling: Vector3D = Vector3D.identity, val rotationRads: Double = 0, 
        val shear: Vector3D = Vector3D.zero)
{
    // COMPUTED PROPERTIES    -------
    
    def rotationDegs = rotationRads.toDegrees
    
    def toAffineTransform = 
    {
        val t = new AffineTransform()
        t.translate(position.x, position.y)
        t.rotate(rotationRads)
        t.scale(scaling.x, scaling.y)
        t.shear(shear.x, shear.y)
        t
    }
    
    
    // OPERATORS    -----------------
    
    def unary_- = Transformation(-position, Vector3D.identity / scaling, -rotationRads, -shear)
    
    def +(other: Transformation) = Transformation(position + other.position, 
            scaling * other.scaling, rotationRads + other.rotationRads, shear + other.shear);
    
    def -(other: Transformation) = this + (-other)
    
    def apply(relative: Vector3D) = Vector3D of toAffineTransform.transform(relative.toPoint2D, null)
    
    def apply(other: Transformation): Transformation = (this + other).withPosition(apply(
            other.position))
    
    
    // OTHER METHODS    -------------
    
    def invert(absolute: Vector3D) = 
    {
        val t = toAffineTransform
        
        if (t.getDeterminant != 0)
        {
            t.invert()
            Vector3D of t.transform(absolute.toPoint2D, null)
        }
        // If the transformation can't be inverted, simply inverts the position
        else
        {
            Transformation(-position)(absolute)
        }
    }
    
    def withPosition(position: Vector3D) = copy(position = position)
    
    def withScaling(scaling: Vector3D) = copy(scaling = scaling)
    
    def withScaling(scaling: Double): Transformation = withScaling(Vector3D(scaling, scaling, 1))
    
    def withRotationRads(rotationRads: Double) = copy(rotationRads = rotationRads)
    
    def withRotationDegs(rotationDegs: Double) = withRotationRads(rotationDegs.toRadians)
    
    def withShear(shear: Vector3D) = copy(shear = shear)
    
    def translated(translation: Vector3D) = withPosition(position + translation)
    
    def scaled(scaling: Vector3D) = withScaling(this.scaling * scaling)
    
    def scaled(scaling: Double) = withScaling(this.scaling * scaling)
    
    def rotatedRads(rotationRads: Double) = withRotationRads(this.rotationRads + rotationRads)
    
    def rotatedDegs(rotationDegs: Double) = rotatedRads(rotationDegs.toRadians)
    
    def sheared(shearing: Vector3D) = withShear(shear + shearing)
}