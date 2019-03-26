package utopia.genesis.shape

import utopia.flow.datastructure

import java.awt.geom.Point2D
import java.awt.Dimension
import utopia.genesis.util.Extensions._
import utopia.flow.generic.ValueConvertible
import utopia.genesis.generic.Vector3DType
import utopia.flow.datastructure.immutable.Value
import utopia.flow.generic.ModelConvertible
import utopia.flow.datastructure.immutable.Model
import utopia.flow.generic.ValueConversions._
import utopia.flow.generic.FromModelFactory
import utopia.flow.datastructure.template.Property
import scala.collection.immutable.HashMap
import scala.Vector
import utopia.genesis.util.ApproximatelyEquatable
import utopia.genesis.shape.shape2D.Point
import utopia.genesis.shape.shape2D.Size

object Vector3D extends FromModelFactory[Vector3D]
{
    // ATTRIBUTES    --------------------
    
    /**
     * The identity vector. Scaling or dividing with this vector will not affect other vectors.
     */
    val identity = Vector3D(1, 1, 1)
    /**
     * The zero vector (0, 0, 0)
     */
    val zero = Vector3D()
    /**
     * A vector with the length of 1
     */
    val unit = Vector3D(1)
    
    
    // OPERATORS    ---------------------
    
    override def apply(model: datastructure.template.Model[Property]) = Some(Vector3D(
            model("x").doubleOr(), model("y").doubleOr(), model("z").doubleOr()));
    
    
    // OTHER METHODS    -----------------
    
    /**
     * Creates a new vector with specified length and direction
     */
    def lenDir(length: Double, direction: Angle) = Vector3D(
            math.cos(direction.toRadians) * length, math.sin(direction.toRadians) * length);
    
    /**
     * Creates a new vector with certain length and direction using radian units
     * @param length The length of the new vector
     * @param directionRads The direction of the new vector in radians
     * @return A vector with provided direction and length
     */
    @deprecated("Replaced with the new Angle class and lenDir method", "v1.1.2")
    def lenDirRads(length: Double, directionRads: Double) = Vector3D(
            math.cos(directionRads) * length, math.sin(directionRads) * length)
    /**
     * Creates a new vector with certain length and direction using degree units
     * @param length the length of the new vector
     * @param directionRads The direction of the new vector in degrees
     * @return A vector with provided direction and length
     */
    @deprecated("Replaced with the new Angle class and lenDir method", "v1.1.2")
    def lenDirDegs(length: Double, directionDegs: Double) = lenDirRads(length, directionDegs.toRadians)
    
    /**
     * Converts a point into a vector
     */
    @deprecated("Please use the new Point class instead", "v1.1.2")
    def of(point: java.awt.Point) = Vector3D(point.getX, point.getY)
    /**
     * Converts a point into a vector
     */
    @deprecated("Please use the new Point class instead", "v1.1.2")
    def of(point: Point2D) = Vector3D(point.getX, point.getY)
    /**
     * Converts a dimension into a vector (width, height)
     */
    @deprecated("Please use the new Size class instead", "v1.1.2")
    def of(dimension: Dimension) = Vector3D(dimension.getWidth, dimension.getHeight)
    
    /**
     * Converts a coordinate map into a vector
     */
    def of(map: Map[Axis, Double]) = Vector3D(map.get(X).getOrElse(0), map.get(Y).getOrElse(0), 
            map.get(Z).getOrElse(0))
    
    /**
     * Calculates a surface normal for two vectors. If this normal was called n, both n and -n are 
     * normals for this surface
     */
    def surfaceNormal(a: Vector3D, b: Vector3D) = 
    {
        val u = a.toUnit
        val v = b.toUnit
        
        /*
		    Nx = UyVz - UzVy
			Ny = UzVx - UxVz
			Nz = UxVy - UyVx
		 */
        Vector3D(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x)
    }
    
    /**
     * Calculates the average point of the provided vectors
     * @return The average point of the provided vectors
     * @throws UnsupportedOperationException If the collection is empty
     */
    @throws(classOf[UnsupportedOperationException])
    def average(vectors: Traversable[Vector3D]) = vectors.reduceLeft { _ + _ } / vectors.size
    
    /**
     * Calculates the average point of the provided vectors
     * @return The average point of the provided vectors. None if collection is empty
     */
    def averageOption(vectors: Traversable[Vector3D]) = 
    {
        if (vectors.isEmpty)
            None
        else
            vectors.reduceLeftOption(_ + _).map(_ / vectors.size)
    }
    
    /**
     * Returns the smaller of the two vectors
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def min(v1: Vector3D, v2: Vector3D) = if (v2 < v1) v2 else v1
    
    /**
     * Returns the smallest of the provided vectors or None if no vectors were provided
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def min(vectors: Traversable[Vector3D]): Option[Vector3D] = vectors.reduceOption(min)
    
    /**
     * Returns the larger of the two vectors
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def max(v1: Vector3D, v2: Vector3D) = if (v2 > v1) v2 else v1
    
    /**
     * Returns the largest of the provided vectors or None if no vectors were provided
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def max(vectors: Traversable[Vector3D]): Option[Vector3D] = vectors.reduceOption(max)
    
    /**
     * The top left corner of a bounds between the two vertices. In other words, 
     * creates a vector that has the smallest available value on each axis from the two candidates
     */
    def topLeft(first: Vector3D, second: Vector3D) = combine(first, second, (a, b) => if (a <= b) a else b)
    
    /**
     * The top left corner of a bounds around the vertices. In other words, 
     * creates a vector that has the smallest available value on each axis from all the candidates. 
     * None if the provided collection is empty
     */
    def topLeft(vectors: Traversable[Vector3D]): Option[Vector3D] = vectors.reduceOption(topLeft)
    
    /**
     * The bottom right corner of a bounds between the two vertices. In other words, 
     * creates a vector that has the largest available value on each axis from the two candidates
     */
    def bottomRight(first: Vector3D, second: Vector3D) = combine(first, second, (a, b) => if (a >= b) a else b)
    
    /**
     * The bottom right corner of a bounds around the vertices. In other words, 
     * creates a vector that has the largest available value on each axis from all the candidates. 
     * None if the provided collection is empty
     */
    def bottomRight(vectors: Traversable[Vector3D]): Option[Vector3D] = vectors.reduceOption(bottomRight)
    
    /**
     * Combines two vectors into a third vector using a binary operator
     * @param first The first vector used at the left hand side of the operator
     * @param second the second vector used at the right hand side of the operator
     * @param f The binary operator that determines the coordinates of the returned vector
     * @return A vector with values combined from the two vectors using the provided operator
     */
    def combine(first: Vector3D, second: Vector3D, f: (Double, Double) => Double) = 
    {
        val v1 = first.toVector
        val v2 = second.toVector
        
        val combo = for { i <- 0 to 2 } yield f(v1(i), v2(i))
        Vector3D(combo(0), combo(1), combo(2))
    }
    
    /**
     * Performs a check over two vectors using a binary operator. Returns true if the condition 
     * holds for any coordinate pair between the two vectors.
     * @param first The first vector used at the left hand side of the operator
     * @param second the second vector used at the right hand side of the operator
     * @param condition The binary operator that determines the return value of the function
     * @return True if the condition returned true for any two coordinates, false otherwise
     */
    def exists(first: Vector3D, second: Vector3D, condition: (Double, Double) => Boolean): Boolean = 
    {
        val v1 = first.toVector
        val v2 = second.toVector
        
        for (i <- 0 to (v1.size - 1))
        {
            if (condition(v1(i), v2(i))) { return true }
        }
        
        false
    }
    
    /**
     * Performs a check over two vectors using a binary operator. Returns true if the condition 
     * holds for all coordinate pair between the two vectors.
     * @param first The first vector used at the left hand side of the operator
     * @param second the second vector used at the right hand side of the operator
     * @param condition The binary operator that determines the return value of the function
     * @return True if the condition returned true for every two coordinates, false otherwise
     */
    def forall(first: Vector3D, second: Vector3D, condition: (Double, Double) => Boolean) = 
            !exists(first, second, { !condition(_, _) });
}

/**
 * This class represents a vector in 3 dimensional space. A vector has a direction and length but 
 * no specified origin point. Vectors have value semantics and are immutable.
 * @author Mikko Hilpinen
 * @since 24.12.2016
 */
case class Vector3D(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) extends 
        ValueConvertible with ModelConvertible with ApproximatelyEquatable[Vector3D]
{
    // COMPUTED PROPERTIES    ----------
    
    override def toValue = new Value(Some(this), Vector3DType)
    
    override def toModel = Model.fromMap(HashMap("x" -> x, "y" -> y, "z" -> z).filterNot { 
            case (_, value) => value ~== 0.0 });
    
    /**
     * A coordinate of this vector along the specified axis
     */
    def along(axis: Axis) = 
    {
        axis match 
        {
            case X => x
            case Y => y
            case Z => z
        }
    }
    
    /**
     * A coordinate map representation of this vector
     */
    def toMap = HashMap(X -> x, Y -> y, Z -> z)
    
    /**
     * a copy of this vector where the coordinate values have been cut to integer numbers.
     * This operation always rounds the numbers down, never up.
     */
    def floor = map(math.floor)
    
    /**
     * a copy of this vector where the coordinate values have been increased to whole integer
     * numbers. This operation always rounds the numbers up, never down.
     */
    def ceil = map(math.ceil)
    
    /**
     * a copy of this vector where the coordinates have been rounded to nearest integer / long
     * numbers.
     */
    def rounded = map { math.round(_) }
    
    /**
     * A projection of this vector that only contains the x-component
     */
    def xProjection = Vector3D(x)
    
    /**
     * A projection of this vector that only contains the y-component
     */
    def yProjection = Vector3D(0, y)
    
    /**
     * A projection of this vector that only contains the z-component
     */
    def zProjection = Vector3D(0, 0, z)
    
    /**
     * A projection of this vector for the specified axis
     */
    def projectedAlong(axis: Axis) = 
    {
        axis match 
        {
            case X => xProjection
            case Y => yProjection
            case Z => zProjection
        }
    }
    
    /**
     * This vector without the z component
     */
    def in2D = if (z == 0) this else Vector3D(x, y)
    
    /**
     * This vector with length of 1
     */
    def toUnit = this / length
    
    /**
     * The x, y and z components of this vector
     */
    def toVector = Vector(x, y, z)
    
    /**
     * Converts this vector to a point
     */
    def toPoint = Point(x, y)
    
    /**
     * Converts this vector to a size
     */
    def toSize = Size(x, y)
    
    /**
     * Converts this vector to a more precise point
     */
    @deprecated("Please use the new Point class in between", "v1.1.2")
    def toPoint2D = new Point2D.Double(x, y)
    
    /**
     * Converts this vector to a dimension
     */
    @deprecated("Please use the new size class in between", "v1.1.2")
    def toDimension = new Dimension(x.toInt, y.toInt)
    
    /**
     * This vector's direction on the x-y plane
     */
    def direction = Angle ofRadians calculateDirection(x, y)
    
    /**
     * This vector's direction on the z-y plane
     */
    def xDirection = Angle ofRadians calculateDirection(z, y)
    
    /**
     * This vector's direction on the x-z plane
     */
    def yDirection = Angle ofRadians calculateDirection(x, z)
    
    /**
     * Calculates this vectors direction around the specified axis
     */
    def directionAround(axis: Axis) = 
    {
        axis match 
        {
            case X => xDirection
            case Y => yDirection
            case Z => direction
        }
    }
    
    /**
     * This vector's direction on the x-y plane in radians
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def directionRads = calculateDirection(x, y)
    
    /**
     * This vector's direction on the x-y plane in degrees
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def directionDegs = directionRads.toDegrees
    
    /**
     * This vector's direction on the z-y plane in radians
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def xDirectionRads = calculateDirection(z, y)
    
    /**
     * This vector's direction on the z-y plane in degrees
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def xDirectionDegs = xDirectionRads.toDegrees
    
    /**
     * This vector's direction on the x-z plane in radians
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def yDirectionRads = calculateDirection(x, z)
    
    /**
     * This vector's direction on the x-z plane in degrees
     */
    @deprecated("Replaced with the new Angle class and direction property", "v1.1.2")
    def yDirectionDegs = yDirectionRads.toDegrees
    
    
    /**
     * The length of this vector
     */
    def length = math.sqrt(this dot this)
    
    /**
     * A normal for this vector
     */
    def normal = if (x == 0 && y == 0 && z != 0) Vector3D(1) else normal2D
    
    /**
     * A 2D normal for this vector
     */
    def normal2D = Vector3D(-y, x).toUnit
    
    /**
     * A version of this vector where all values are at least 0
     */
    def positive = Vector3D(x max 0, y max 0, z max 0)
    
    
    // OPERATORS    --------------------
    
    /**
     * This vector inverted
     */
    def unary_- = map {-_}
    
    def +(other: Vector3D) = Vector3D.combine(this, other, { _ + _ })
    
    /**
     * This vector with increased length
     */
    def +(n: Double) = withLength(length + n)
    
    /**
     * Creates a set of bounds based on this position and a certain size
     */
    @deprecated("Please use the Point and Size classes instead of awt counterparts", "v1.1.2")
    def +(size: Dimension) = new java.awt.Rectangle(toPoint.toAwtPoint, size)
    
    def -(other: Vector3D) = this + (-other)
    
    /**
     * This vector with decreased length (the direction may change to opposite)
     */
    def -(n: Double) = this + (-n)
    
    def *(other: Vector3D) = Vector3D.combine(this, other, { _ * _ })
    
    def *(n: Double) = map { _ * n }
    
    def /(other: Vector3D) = Vector3D.combine(this, other, divided)
    
    def /(n: Double): Vector3D = this / Vector3D(n, n, n)
    
    /**
     * Compares the two vectors and determines whether this one is smaller. Checks the axes in 
     * following order: x > y > z
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def <(other: Vector3D) = 
    {
        if (x == other.x)
        {
            if (y == other.y)
            {
                z < other.z
            }
            else
            {
                y < other.y
            }
        }
        else 
        {
            x < other.x
        }
    }
    
    /**
     * Compares the two vectors and determines whether this one is larger. Checks the axes in 
     * following order: x > y > z
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def >(other: Vector3D) = !(this <= other)
    
    /**
     * Compares the two vectors and determines whether this one is smaller or equal. Checks the axes in 
     * following order: x > y > z
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def <=(other: Vector3D) = this == other || this < other
    
    /**
     * Compares the two vectors and determines whether this one is larger or equal. Checks the axes in 
     * following order: x > y > z
     */
    @deprecated("The comparison methods don't work as they should heuristically speaking", "v1.1.2")
    def >=(other: Vector3D) = !(this < other)
    
    /**
     * Checks whether two vectors are approximately equal
     */
    override def ~==[B <: Vector3D](other: B) = Vector3D.forall(this, other, { _ ~== _ })
    
    
    // OTHER METHODS    ----------------
    
    /**
     * The dot product between this and another vector
     */
    def dot(other: Vector3D) = (this * other).toVector.reduceLeft { _ + _ }
    
    /**
     * The cross product between this and another vector. The cross product is parallel with a
     * normal for a surface created by these two vectors
     */
    def cross(other: Vector3D) = Vector3D.surfaceNormal(this, other).withLength(crossProductLength(other))
    
    /**
     * The length of the cross product of these two vectors. |a||b|sin(a, b)
     */
    // = |a||b|sin(a, b)e, |e| = 1 (in this we skip the e)
    def crossProductLength(other: Vector3D) = length * other.length * math.sin(angleDifferenceRads(other))
    
    /**
     * Projects this vector over the another vector. The projected vector will be parallel to the
     * provided vector parameter
     */
    def projectedOver(other: Vector3D) = other * (dot(other) / other.dot(other))
    
    /**
     * Projects this vector over an axis. The projected vector will be parallel to the
     * provided axis
     */
    def projectedOver(axis: Axis): Vector3D = projectedOver(axis.toUnitVector)
    
    /**
     * Calculates the scalar projection of this vector over the other vector. This is the same as 
     * the length of this vector's projection over the other vector
     */
    def scalarProjection(other: Vector3D) = dot(other) / other.length
    
    /**
     * Checks whether this vector is parallel with another vector (has same or opposite direction)
     */
    def isParallelWith(other: Vector3D) = crossProductLength(other) ~== 0.0
    
    /**
     * Checks whether this vector is perpendicular to another vector (ie. (1, 0) vs. (0, 1))
     */
    def isPerpendicularTo(other: Vector3D) = dot(other) ~== 0.0
    
    /**
     * Calculates the directional difference between the two vectors in radians. The difference is 
     * absolute (always positive) and doesn't specify the direction of the difference.
     */
    def angleDifferenceRads(other: Vector3D) = 
    {
        // This vector is used as the 'x'-axis, while a perpendicular vector is used as the 'y'-axis
        // The other vector is then measured against these axes
        val x = other projectedOver this
        val y = other - x
        
        math.atan2(y.length, x.length).abs
    }
    
    /*
     * Checks whether this vector is between the two vector values in every coordinate axis
     * (inclusive)
     * @return Whether the coordinates of this vector are in the area formed by the two coordinates
     */
    // def isBetween(a: Vector3D, b: Vector3D) = Vector3D.forall(this, Vector3D.min(a, b), { _ >= _ }) && 
    //        Vector3D.forall(this, Vector3D.max(a, b), { _ <= _ });
    
    /**
     * Creates a new vector with the same direction with this vector
     * @param length The length of the new vector
     */
    def withLength(length: Double) = toUnit * length
    
    /**
     * Creates a new vector with the same length as this vector
     * @param direction The direction of the new vector
     */
    def withDirection(direction: Angle) = Vector3D.lenDir(length, direction)
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-y plane, in radians
     */
    @deprecated("Replaced with the new Angle class and withDirection method", "v1.1.2")
    def withDirectionRads(directionRads: Double) = Vector3D.lenDirRads(length, directionRads)
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-y plane, in degrees
     */
    @deprecated("Replaced with the new Angle class and withDirection method", "v1.1.2")
    def withDirectionDegs(directionDegs: Double) = Vector3D.lenDirDegs(length, directionDegs)
    
    /**
     * Creates a new vector with the same length as this vector
     * @param the new direction of the vector on the x-z plane
     */
    def withYDirection(direction: Angle) = 
    {
        val zRotated = in2D.withDirection(direction)
        Vector3D(zRotated.x, y, zRotated.y)
    }
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-z plane, in radians
     */
    @deprecated("Replaced with the new Angle class and withYDirection method", "v1.1.2")
    def withYDirectionRads(directionRads: Double) = 
    {
        val zRotated = in2D.withDirectionRads(directionRads)
        Vector3D(zRotated.x, y, zRotated.y)
    }
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-z plane, in degrees
     */
    @deprecated("Replaced with the new Angle class and withYDirection method", "v1.1.2")
    def withYDirectionDegs(directionDegs: Double) = withYDirectionRads(directionDegs.toRadians)
    
    /**
     * Rotates the vector around a certain origin point
     * @rotationRads The amount of rotation in radians
     * @param origin The point this vector is rotated around (defaults to zero)
     * @return The rotated vector
     */
    def rotated(rotation: Rotation, origin: Vector3D = Vector3D.zero) = 
    {
        val separator = this - origin
        origin + Vector3D.lenDir(separator.length, separator.direction + rotation.toDouble).copy(z = z)
    }
    
    /**
     * Rotates the vector around a certain origin point
     * @rotationRads The amount of rotation in radians
     * @param origin The point this vector is rotated around (defaults to zero)
     * @return The rotated vector
     */
    def rotatedRads(rotationRads: Double, origin: Vector3D = Vector3D.zero) = rotated(Rotation ofRadians rotationRads)
    
    /**
     * Rotates the vector around a certain origin point
     * @rotationRads The amount of rotation in degrees
     * @param origin The point this vector is rotated around (default to zero)
     * @return The rotated vector
     */
    def rotatedDegs(rotationDegs: Double, origin: Vector3D = Vector3D.zero) = rotated(Rotation ofDegrees rotationDegs)
    
    /**
     * Transforms the coordinates of this vector and returns the transformed vector
     * @param f The map function that maps the current coordinates into new coordinates
     * @return A vector with the mapped coordinates
     */
    def map(f: Double => Double) = Vector3D(f(x), f(y), f(z))
    
    /**
     * Transforms a coordinate of this vector and returns the transformed vector
     * @param f The map function that maps a current coordinate into a new coordinate
     * @param along the axis that specifies the mapped coordinate
     * @return A vector with the mapped coordinate
     */
    def map(f: Double => Double, along: Axis) = 
    {
        along match 
        {
            case X => Vector3D(f(x), y, z)
            case Y => Vector3D(x, f(y), z)
            case Z => Vector3D(x, y, f(z))
        }
    }
    
    private def calculateDirection(x: Double, y: Double) = math.atan2(y, x)
    
    // can't divide with 0 (the number is kept as it is)
    private def divided(a: Double, b: Double) = if (b == 0) a else a / b
}