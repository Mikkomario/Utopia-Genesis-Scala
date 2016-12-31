package utopia.genesis.util

import java.awt.geom.AffineTransform

object Transformation
{
    /**
     * This transformation preserves the state of the target without transforming it
     */
    val identity = Transformation()
    
    /**
     * This transformation moves the coordinates of the target by the provided amount
     */
    def translation(amount: Vector3D) = Transformation(position = amount)
    
    /**
     * This transformation scales the target by the provided amount
     */
    def scaling(amount: Vector3D) = Transformation(scaling = amount)
    
    /**
     * This transformation scales the target by the provided amount. Each coordinate is scaled with
     * the same factor 
     */
    def scaling(amount: Double) = Transformation(scaling = Vector3D(amount, amount, amount))
    
    /**
     * This transformation rotates the target around the zero origin by the provided amount of
     * radians
     */
    def rotationRads(amountRads: Double) = Transformation(rotationRads = amountRads)
    
    /**
     * This transformation rotates the target around the zero origin by the provided amount of
     * degrees
     */
    def rotationDegs(amountDegs: Double) = rotationRads(amountDegs.toRadians)
    
    /**
     * This transformation shears the target by the provided amount
     */
    def shear(amount: Vector3D) = Transformation(shear = amount)
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
    
    /**
     * How much the target is rotated in degrees
     */
    def rotationDegs = rotationRads.toDegrees
    
    /**
     * Converts this transform instance into an affine transform instance
     */
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
    
    /**
     * Inverts this transformation
     */
    def unary_- = Transformation(-position, Vector3D.identity / scaling, -rotationRads, -shear)
    
    /**
     * Combines the two transformations together. The applied translation is not depended of the 
     * scaling or rotation of this transformation. If you want the results of applying first this
     * transformation and then the second, use apply(Transformation) instead.
     */
    def +(other: Transformation) = Transformation(position + other.position, 
            scaling * other.scaling, rotationRads + other.rotationRads, shear + other.shear);
    
    /**
     * Negates a transformation from this transformation
     */
    def -(other: Transformation) = this + (-other)
    
    /**
     * Transforms a <b>relative</b> vector <b>into absolute</b> vector
     * @param relative a relative vector that will be transformed
     */
    def apply(relative: Vector3D) = Vector3D of toAffineTransform.transform(relative.toPoint2D, null)
    
    /**
     * Combines the two transformations together. The end result is effectively same as transforming 
     * the target with this transformation, then with the provided transformation.<p>
     * Please notice that the scaling and rotation affect the scaling and translation applied (for
     * example, adding translation of (1, 0, 0) to a transformation with
     * zero position and scaling of 2 will create a transformation with (2, 0, 0) position and
     * scaling 2
     */
    def apply(other: Transformation): Transformation = (this + other).withPosition(apply(
            other.position));
    
    
    // OTHER METHODS    -------------
    
    /**
     * Inverse transforms an <b>absolute</b> coordinate point <b>into relative</b> space
     * @param absolute a vector in absolute world space
     * @return The absolute point in relative world space
     */
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
    
    /**
     * Rotates the transformation around an absolute origin point
     * @param rotationRads the amount of radians the transformation is rotated
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def absoluteRotatedRads(rotationRads: Double, origin: Vector3D) = 
            withPosition(position.rotatedRads(rotationRads, origin)).rotatedRads(rotationRads);
    
    /**
     * Rotates the transformation around a relative origin point
     * @param rotationRads the amount of radians the transformation is rotated
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def relativeRotatedRads(rotationRads: Double, origin: Vector3D) = absoluteRotatedRads(
            rotationRads, apply(origin));
    
    /**
     * Rotates the transformation around an absolute origin point
     * @param rotationRads the amount of degrees the transformation is rotated
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def absoluteRotatedDegs(rotationDegs: Double, origin: Vector3D) = absoluteRotatedRads(
            rotationDegs.toRadians, origin);
    
    /**
     * Rotates the transformation around an relative origin point
     * @param rotationRads the amount of degrees the transformation is rotated
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def relativeRotatedDegs(rotationDegs: Double, origin: Vector3D) = relativeRotatedRads(
            rotationDegs.toRadians, origin);
    
    /**
     * Copies this transformation, giving it a new position
     */
    def withPosition(position: Vector3D) = copy(position = position)
    
    /**
     * Copies this transformation, giving it a new scaling
     */
    def withScaling(scaling: Vector3D) = copy(scaling = scaling)
    
    /**
     * Copies this transformation, giving it a new scaling
     */
    def withScaling(scaling: Double): Transformation = withScaling(Vector3D(scaling, scaling, scaling))
    
    /**
     * Copies this transformation, giving it a new rotation
     */
    def withRotationRads(rotationRads: Double) = copy(rotationRads = rotationRads)
    
    /**
     * Copies this transformation, giving it a new rotation
     */
    def withRotationDegs(rotationDegs: Double) = withRotationRads(rotationDegs.toRadians)
    
    /**
     * Copies this transformation, giving it a new shearing
     */
    def withShear(shear: Vector3D) = copy(shear = shear)
    
    /**
     * Copies this transformation, changing the translation by the provided amount
     */
    def translated(translation: Vector3D) = withPosition(position + translation)
    
    /**
     * Copies this transformation, changing the scaling by the provided amount
     */
    def scaled(scaling: Vector3D) = withScaling(this.scaling * scaling)
    
    /**
     * Copies this transformation, changing the scaling by the provided amount
     */
    def scaled(scaling: Double) = withScaling(this.scaling * scaling)
    
    /**
     * Copies this transformation, changing the rotation by the provided amount
     */
    def rotatedRads(rotationRads: Double) = withRotationRads(this.rotationRads + rotationRads)
    
    /**
     * Copies this transformation, changing the rotation by the provided amount
     */
    def rotatedDegs(rotationDegs: Double) = rotatedRads(rotationDegs.toRadians)
    
    /**
     * Copies this transformation, changing the shearing by the provided amount
     */
    def sheared(shearing: Vector3D) = withShear(shear + shearing)
}