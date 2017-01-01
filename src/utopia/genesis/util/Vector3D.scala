package utopia.genesis.util

import java.awt.Point
import java.awt.geom.Point2D
import java.awt.Dimension

import utopia.genesis.util.Extensions._
import java.awt.Rectangle

object Vector3D
{
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
    
    /**
     * Creates a new vector with certain length and direction using radian units
     * @param length The length of the new vector
     * @param directionRads The direction of the new vector in radians
     * @return A vector with provided direction and length
     */
    def lenDirRads(length: Double, directionRads: Double) = Vector3D(
            math.cos(directionRads) * length, math.sin(directionRads) * length)
    /**
     * Creates a new vector with certain length and direction using degree units
     * @param length the length of the new vector
     * @param directionRads The direction of the new vector in degrees
     * @return A vector with provided direction and length
     */
    def lenDirDegs(length: Double, directionDegs: Double) = lenDirRads(length, directionDegs.toRadians)
    
    /**
     * Converts a point into a vector
     */
    def of(point: Point) = Vector3D(point.getX, point.getY)
    /**
     * Converts a point into a vector
     */
    def of(point: Point2D) = Vector3D(point.getX, point.getY)
    /**
     * Converts a dimension into a vector (width, height)
     */
    def of(dimension: Dimension) = Vector3D(dimension.getWidth, dimension.getHeight)
    
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
            !exists(first, second, { !condition(_, _) })
}

/**
 * This class represents a vector in 3 dimensional space. A vector has a direction and length but 
 * no specified origin point. Vectors have value semantics and are immutable.
 * @author Mikko Hilpinen
 * @since 24.12.2016
 */
case class Vector3D(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0)
{
    // COMPUTED PROPERTIES    ----------
    
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
    def toPoint = new Point(x.toInt, y.toInt)
    
    /**
     * Converts this vector to a more precise point
     */
    def toPoint2D = new Point2D.Double(x, y)
    
    /**
     * Converts this vector to a dimension
     */
    def toDimension = new Dimension(x.toInt, y.toInt)
    
    
    /**
     * This vector's direction on the x-y plane in radians
     */
    def directionRads = calculateDirection(x, y)
    
    /**
     * This vector's direction on the x-y plane in degrees
     */
    def directionDegs = directionRads.toDegrees
    
    /**
     * This vector's direction on the z-y plane in radians
     */
    def xDirectionRads = calculateDirection(z, y)
    
    /**
     * This vector's direction on the z-y plane in degrees
     */
    def xDirectionDegs = xDirectionRads.toDegrees
    
    /**
     * This vector's direction on the x-z plane in radians
     */
    def yDirectionRads = calculateDirection(x, z)
    
    /**
     * This vector's direction on the x-z plane in degrees
     */
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
    def +(size: Dimension) = new Rectangle(toPoint, size)
    
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
     * Checks whether two vectors are approximately equal
     */
    def ~==(other: Vector3D) = Vector3D.forall(this, other, { _ ~== _ })
    
    
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
     * Calculates the scalar projection of this vector over the other vector. This is the same as 
     * the length of this vector's projection over the other vector
     */
    def scalarProjection(other: Vector3D) = dot(other) / other.length
    
    /**
     * Checks whether this vector is parallel with another vector (has same or opposite direction)
     */
    def isParallelWith(other: Vector3D) = crossProductLength(other) ~== 0
    
    /**
     * Checks whether this vector is perpendicular to another vector (ie. (1, 0) vs. (0, 1))
     */
    def isPerpendicularTo(other: Vector3D) = dot(other) ~== 0
    
    /**
     * Calculates the directional difference between the two vectors in radians
     */
    def angleDifferenceRads(other: Vector3D) = 
    {
        // This vector is used as the 'x'-axis, while a perpendicular vector is used as the 'y'-axis
        // The other vector is then measured against these axes
        val x = other projectedOver this
        val y = other - x
        
        math.atan2(y.length, x.length).abs
    }
    
    /**
     * Checks whether this vector is between the two vector values in every coordinate axis
     * (inclusive)
     * @param min The minimum vector
     * @param max The maximum vector
     * @return Whether the coordinates of this vector are between the min and max coordinates
     * respectively
     */
    def isBetween(min: Vector3D, max: Vector3D) = Vector3D.forall(this, min, { _ >= _ }) && 
            Vector3D.forall(this, max, { _ <= _ })
    
    /**
     * Creates a new vector with the same direction with this vector
     * @param length The length of the new vector
     */
    def withLength(length: Double) = toUnit * length
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-y plane, in radians
     */
    def withDirectionRads(directionRads: Double) = Vector3D.lenDirRads(length, directionRads)
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-y plane, in degrees
     */
    def withDirectionDegs(directionDegs: Double) = Vector3D.lenDirDegs(length, directionDegs)
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-z plane, in radians
     */
    def withYDirectionRads(directionRads: Double) = 
    {
        val zRotated = in2D.withDirectionRads(directionRads)
        Vector3D(zRotated.x, y, zRotated.y)
    }
    
    /**
     * Creates a new vector with the same length with this vector
     * @param directionRads The vector's new direction on the x-z plane, in degrees
     */
    def withYDirectionDegs(directionDegs: Double) = withYDirectionRads(directionDegs.toRadians)
    
    /**
     * Rotates the vector around a certain origin point
     * @rotationRads The amount of rotation in radians
     * @param origin The point this vector is rotated around (defaults to zero)
     * @return The rotated vector
     */
    def rotatedRads(rotationRads: Double, origin: Vector3D = Vector3D.zero) = 
    {
        val separator = this - origin
        
        origin + Vector3D.lenDirRads(separator.length, 
                separator.directionRads + rotationRads).copy(z = z)
    }
    
    /**
     * Rotates the vector around a certain origin point
     * @rotationRads The amount of rotation in degrees
     * @param origin The point this vector is rotated around (default to zero)
     * @return The rotated vector
     */
    def rotatedDegs(rotationDegs: Double, origin: Vector3D = Vector3D.zero) = 
            rotatedRads(rotationDegs.toRadians, origin)
    
    /**
     * Transforms the coordinates of this vector and returns the transformed vector
     * @param f The map function that maps the current coordinates into new coordinates
     * @return A vector with the mapped coordinates
     */
    def map(f: Double => Double) = Vector3D(f(x), f(y), f(z))
    
    private def calculateDirection(x: Double, y: Double) = math.atan2(y, x)
    
    // can't divide with 0 (the number is kept as it is)
    private def divided(a: Double, b: Double) = if (b == 0) a else a / b
}