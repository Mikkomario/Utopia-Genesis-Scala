package utopia.genesis.util

import java.awt.Point
import java.awt.geom.Point2D
import java.awt.Dimension

object Vector3D
{
    val identity = Vector3D(1, 1, 1)
    val zero = Vector3D()
    val unit = Vector3D(1)
    
    def of(point: Point) = Vector3D(point.getX, point.getY)
    def of(point: Point2D) = Vector3D(point.getX, point.getY)
    def of(dimension: Dimension) = Vector3D(dimension.getWidth, dimension.getHeight)
    
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
    
    def in2D = if (z == 0) this else Vector3D(x, y)
    
    def toUnit = this / length
    
    
    def toVector = Vector(x, y, z)
    
    def toPoint = new Point(x.toInt, y.toInt)
    
    def toDimension = new Dimension(x.toInt, y.toInt)
    
    
    def directionRads = calculateDirection(x, y)
    
    def directionDegs = MathUtil.degreesOfRadians(directionRads)
    
    def xDirectionRads = calculateDirection(z, y)
    
    def xDirectionDegs = MathUtil.degreesOfRadians(xDirectionRads)
    
    def yDirectionRads = calculateDirection(x, z)
    
    def yDirectionDegs = MathUtil.degreesOfRadians(yDirectionRads)
    
    
    def length = Math.sqrt(this dot this)
    
    def normal = if (x == 0 && y == 0 && z != 0) Vector3D(1) else normal2D
    
    def normal2D = Vector3D(-y, x).toUnit
    
    
    // OPERATORS    --------------------
    
    def unary_- = Vector3D(-x, -y, -z)
    
    def +(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
    
    def -(other: Vector3D) = this + (-other)
    
    def *(other: Vector3D) = Vector3D(x * other.x, y * other.y, z * other.z)
    
    def *(n: Double) = Vector3D(x * n, y * n, z * n)
    
    def /(other: Vector3D) = Vector3D(divided(x, other.x), divided(y, other.y), divided(z, other.z))
    
    def /(n: Double): Vector3D = this / Vector3D(n, n, n)
    
    
    // OTHER METHODS    ----------------
    
    def dot(other: Vector3D) = (this * other).toVector.reduceLeft { _ + _ }
    
    // = |a||b|sin(a, b)e, |e| = 1 (in this we skip the e)
    def crossProductLength(other: Vector3D) = 
    {
        val radDiff = 
        { 
            if (in2D == Vector3D.zero || other.in2D == Vector3D.zero) 
                yDirectionRads - other.yDirectionRads 
            else 
                directionRads - other.directionRads
        }
            
        length * other.length * Math.sin(radDiff)
    }
    
    def projectedOver(other: Vector3D) = other * (dot(other) / other.dot(other))
    
    def scalarProjection(other: Vector3D) = dot(other) / other.length
    
    def isParallelWith(other: Vector3D) = crossProductLength(other) == 0
    
    def isPerpendicularTo(other: Vector3D) = dot(other) == 0
    
    private def calculateDirection(x: Double, y: Double) = Math.atan2(x, y)
    
    // can't divide with 0 (will remain 0 or become 1)
    private def divided(a: Double, b: Double) = 
    {
        if (a == 0) { 0 }
        else if (b == 0) { 1 }
        else { a / b }
    }
}