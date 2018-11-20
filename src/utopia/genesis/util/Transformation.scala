package utopia.genesis.util

import java.awt.geom.AffineTransform
import utopia.genesis.util.Extensions._
import utopia.flow.generic.ValueConvertible
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.TransformationType
import utopia.flow.generic.ModelConvertible
import utopia.flow.datastructure.immutable.Model
import utopia.flow.generic.ValueConversions._
import utopia.flow.generic.FromModelFactory
import utopia.flow.datastructure.template
import utopia.flow.datastructure.template.Property
import utopia.genesis.generic.GenesisValue._

object Transformation extends FromModelFactory[Transformation]
{
    // ATTRIBUTES    -----------------
    
    /**
     * This transformation preserves the state of the target without transforming it
     */
    val identity = Transformation()
    
    
    // OPERATORS    -----------------
    
    override def apply(model: template.Model[Property]) = Some(Transformation(
            model("position").vector3DOr(), model("scaling").vector3DOr(Vector3D.identity), 
            model("rotation").doubleOr(), model("shear").vector3DOr()))
    
    
    // OTHER METHODS    --------------
    
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
     * radians. Rotation is made in clockwise direction.
     */
    def rotationRads(amountRads: Double) = Transformation(rotationRads = amountRads)
    
    /**
     * This transformation rotates the target around the zero origin by the provided amount of
     * degrees. Rotation is made in clockwise direction.
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
// TODO: Rename position to translation
case class Transformation(val position: Vector3D = Vector3D.zero, 
        val scaling: Vector3D = Vector3D.identity, val rotationRads: Double = 0, 
        val shear: Vector3D = Vector3D.zero, val useReverseOrder: Boolean = false) extends 
        ValueConvertible with ModelConvertible
{
    // COMPUTED PROPERTIES    -------
    
    override def toValue = new Value(Some(this), TransformationType)
    
    override def toModel = Model(Vector("position" -> position, "scaling" -> scaling, 
            "rotation" -> rotationRads, "shear" -> shear));
    
    /**
     * How much the target is rotated in degrees (clockwise)
     */
    def rotationDegs = rotationRads.toDegrees
    
    /**
     * This transformation represented as an affine transform instance. A new instance is generated 
     * on each call since it is mutable while transformation is not
     */
    def toAffineTransform = 
    {
        val t = new AffineTransform()
        
        // Reverse transformations do the transforms in reverse order too
        if (useReverseOrder)
        {
            t.shear(shear.x, shear.y)
            t.scale(scaling.x, scaling.y)
            t.rotate(rotationRads)
            t.translate(position.x, position.y)
        }
        else
        {
            t.translate(position.x, position.y)
            t.rotate(rotationRads)
            t.scale(scaling.x, scaling.y)
            t.shear(shear.x, shear.y)
        }
        t
    }
    
    /**
     * Converts this transform instance into an affine transform and inverts it
     */
    def toInvertedAffineTransform = 
    {
        val t = toAffineTransform
        
        // If the transformation can't be inverted, simply inverts the position
        if (t.getDeterminant != 0)
        {
            t.invert()
            t
        }
        else
        {
            Transformation(-position).toAffineTransform
        }
    }
    
    /**
     * The translation component of this transformation
     */
    def translationTransformation = Transformation.translation(position)
    
    /**
     * The scaling component of this transformation
     */
    def scalingTransformation = Transformation.scaling(scaling)
    
    /**
     * The rotation component of this transformation (clockwise)
     */
    def rotationTransformation = Transformation.rotationRads(rotationRads)
    
    /**
     * The shear component of this transformation
     */
    def shearTransformation = Transformation.shear(shear)
    
    
    // OPERATORS    -----------------
    
    /**
     * Inverts this transformation
     */
    def unary_- = Transformation(-position, Vector3D.identity / scaling, -rotationRads, -shear, 
            !useReverseOrder);
    
    /**
     * Combines the two transformations together. The applied translation is not depended of the 
     * scaling or rotation of this transformation. If you want the results of applying first this
     * transformation and then the second, use apply(Transformation) instead.
     */
    def +(other: Transformation) = Transformation(position + other.position, 
            scaling * other.scaling, rotationRads + other.rotationRads, shear + other.shear, useReverseOrder);
    
    /**
     * Negates a transformation from this transformation
     */
    // def -(other: Transformation) = this + (-other)
    
    /**
     * Checks whether the two transformations are practically (approximately) identical with each
     * other
     */
    def ~==(other: Transformation) = (position ~== other.position) && (scaling ~== other.scaling) && 
            (rotationRads ~== other.rotationRads) && (shear ~== other.shear)
    
    /**
     * Transforms a <b>relative</b> point <b>into an absolute</b> point
     * @param relative a relative point that will be transformed
     */
    def apply(relative: Point) = Point of toAffineTransform.transform(relative.toAwtPoint2D, null)
    
    /**
     * Transforms a shape <b>from relative space to absolute space</b>
     */
    def apply[B](relative: TransformableShape[B]) = relative.transformedWith(this)
    
    /**
     * Combines the two transformations together. The end result is effectively same as transforming 
     * the target with the provided transformation, then with this transformation.<p>
     * Please notice that the scaling and rotation affect the scaling and translation applied (for
     * example, adding translation of (1, 0, 0) to a transformation with
     * zero position and scaling of 2 will create a transformation with (2, 0, 0) position and
     * scaling 2
     */
    def apply(other: Transformation): Transformation = (this + other).withPosition(apply(
            other.position.toPoint));
    
    
    // OTHER METHODS    -------------
    
    /**
     * Inverse transforms an <b>absolute</b> coordinate point <b>into relative</b> space
     * @param absolute a vector in absolute world space
     * @return The absolute point in relative world space
     */
    def invert(absolute: Vector3D) = Vector3D of toInvertedAffineTransform.transform(
            absolute.toPoint2D, null);
    
    /**
     * Transforms a shape <b>from absolute space to relative space</b>
     */
    def invert[B](absolute: TransformableShape[B]) = absolute.transformedWith(-this)
    
    /**
     * Converts an absolute coordinate into a relative one. Same as calling invert(Vector3D)
     */
    def toRelative(absolute: Vector3D) = invert(absolute)
    
    /**
     * Converts an absolute shape to a relative one. Same as calling invert(...)
     */
    def toRelative[B](absolute: TransformableShape[B]) = invert(absolute)
    
    /**
     * Converts a relative coordinate into an absolute one. Same as calling apply(Point)
     */
    def toAbsolute(relative: Point) = apply(relative)
    
    /**
     * Converts a relative shape to an absolute one. Same as calling apply(...)
     */
    def toAbsolute[B](relative: TransformableShape[B]) = apply(relative)
    
    /**
     * Rotates the transformation around an absolute origin point
     * @param rotationRads the amount of radians the transformation is rotated (clockwise)
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def absoluteRotatedRads(rotationRads: Double, origin: Point) = 
            withPosition(position.rotatedRads(rotationRads, origin.toVector).toPoint).rotatedRads(rotationRads);
    
    /**
     * Rotates the transformation around a relative origin point
     * @param rotationRads the amount of radians the transformation is rotated (clockwise)
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def relativeRotatedRads(rotationRads: Double, origin: Point) = absoluteRotatedRads(
            rotationRads, apply(origin));
    
    /**
     * Rotates the transformation around an absolute origin point
     * @param rotationRads the amount of degrees the transformation is rotated (clockwise)
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def absoluteRotatedDegs(rotationDegs: Double, origin: Point) = absoluteRotatedRads(
            rotationDegs.toRadians, origin);
    
    /**
     * Rotates the transformation around an relative origin point
     * @param rotationRads the amount of degrees the transformation is rotated (clockwise)
     * @param origin the point of origin around which the transformation is rotated
     * @return the rotated transformation
     */
    def relativeRotatedDegs(rotationDegs: Double, origin: Point) = relativeRotatedRads(
            rotationDegs.toRadians, origin);
    
    /**
     * Copies this transformation, giving it a new position
     */
    def withPosition(position: Point) = copy(position = position.toVector)
    
    /**
     * Copies this transformation, giving it a new scaling
     */
    def withScaling(scaling: Vector3D) = copy(scaling = scaling)
    
    /**
     * Copies this transformation, giving it a new scaling
     */
    def withScaling(scaling: Double): Transformation = withScaling(Vector3D(scaling, scaling, scaling))
    
    /**
     * Copies this transformation, giving it a new rotation (clockwise)
     */
    def withRotationRads(rotationRads: Double) = copy(rotationRads = rotationRads)
    
    /**
     * Copies this transformation, giving it a new rotation (clockwise)
     */
    def withRotationDegs(rotationDegs: Double) = withRotationRads(rotationDegs.toRadians)
    
    /**
     * Copies this transformation, giving it a new shearing
     */
    def withShear(shear: Vector3D) = copy(shear = shear)
    
    /**
     * Copies this transformation, changing the translation by the provided amount
     */
    // TODO: Add withTranslation and use it in cases like these
    def translated(translation: Vector3D) = withPosition(position.toPoint + translation)
    
    /**
     * Copies this transformation, changing the scaling by the provided amount
     */
    def scaled(scaling: Vector3D) = withScaling(this.scaling * scaling)
    
    /**
     * Copies this transformation, changing the scaling by the provided amount
     */
    def scaled(scaling: Double) = withScaling(this.scaling * scaling)
    
    /**
     * Copies this transformation, changing the rotation by the provided amount (clockwise)
     */
    def rotatedRads(rotationRads: Double) = withRotationRads(this.rotationRads + rotationRads)
    
    /**
     * Copies this transformation, changing the rotation by the provided amount (clockwise)
     */
    def rotatedDegs(rotationDegs: Double) = rotatedRads(rotationDegs.toRadians)
    
    /**
     * Copies this transformation, changing the shearing by the provided amount
     */
    def sheared(shearing: Vector3D) = withShear(shear + shearing)
}