package utopia.genesis.util

import utopia.genesis.util.Extensions._

object Line
{
    /**
     * Creates a new line from position and vector combo
     * @param position The starting position of the line
     * @param vector The vector portion of the line
     * @return A line with the provided position and vector part
     */
    def ofVector(position: Vector3D, vector: Vector3D) = Line(position, position + vector)    
}

/**
 * A line consists of a start and an end point. Basically it is a vector with a certain position.
 * @author Mikko Hilpinen
 * @since 13.12.2016
 */
case class Line(val start: Vector3D, val end: Vector3D)
{
    // ATTRIBUTES    -------------------
    
    /**
     * The vector portion of this line (position information not included)
     */
    lazy val vector = end - start
    
    
    // COMPUTED PROPERTIES    ----------
    
    /**
     * This line with inverted direction
     */
    def inverted = Line(end, start)
    
    
    // OPERATORS    --------------------
    
    /**
     * Checks if the two lines are practically (approximately) identical
     */
    def ~==(other: Line) = (start ~== other.start) && (end ~== other.end)
    
    /**
     * Finds a position on this line
     * @param t The length parameter. t = 1 will result in the end point of the line. If t is 
     * between 0 and 1, the resulting point will lie on the line segment
     * @return a point along the line
     */
    def apply(t: Double) = start + vector * t
    
    /**
     * Calculates the intersection point between this and another line
     * @param other the other line
     * @param onlyPointsInSegment Should the intersection be limited to line segment area. Defaults
     * to true
     * @return The intersection between the two lines or None if there is no intersection
     */
    def intersection(other: Line, onlyPointsInSegment: Boolean = true) = 
    {
        // a (V1 x V2) = (P2 - P1) x V2
        // Where P is the start point and Vs are the vector parts
        val leftVector = vector cross other.vector
        val rightVector = (other.start - start) cross other.vector
        
        // If the left hand side vector is a zero vector, there is no collision
        // The two vectors must also be parallel
        if ((leftVector ~== Vector3D.zero) || !(leftVector isParallelWith rightVector))
        {
            None
        }
        else
        {
            // a = |right| / |left|, negative if they have opposite directions
            val a = if (leftVector.directionRads ~== rightVector.directionRads) 
                rightVector.length / leftVector.length else -rightVector.length / leftVector.length;
            
            val intersectionPoint = apply(a)
            
            if (onlyPointsInSegment)
            {
                if (a >= 0 && a <= 1 && intersectionPoint.isBetween(other.start, other.end)) 
                    Some(intersectionPoint) else None
            }
            else
            {
                Some(intersectionPoint)
            }
        }
    }
    
    // Do a sphere intersection https://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection
    
    /**
     * Calculates the intersection points between a line and a sphere in 3D. There are from 0 to 2
     * intersection points
     * @param circle The circle / sphere this line may intersect with
     * @param onlyPointsInSegment Determines whether only points that lie in this specific line 
     * segment should be included. If false, finds the intersection points as if the line had 
     * infinite length. Defaults to true.
     * @return The intersection points between this line (segment) and the circle
     */
    def circleIntersection(circle: Circle, onlyPointsInSegment: Boolean = true) = 
    {
        /* Circle Equation: |x - c|^2 = r^2 
         * where x is a point on the circle, c is circle origin and r is circle radius
         * 
         * Line Equation: x = o + vt
         * where x is point on the line, o is the line start point and v is the line's vector
         * portion. t is a scale for the vector portion ([0, 1] are on the line segment)
         * 
         * From these we get: |o + vt - c|^2 = r^2
         * -> ... -> t^2(v . v) + 2t(v . (o - c)) + (o - c) . (o - c) - r^2 = 0
         * And if we add L = o - c...
         * 
         * Using quadratic formula we get the terms
         * a = v . v
         * b = 2(v . L)
         * c = L . L - r^2
         */
        val distanceVector = start - circle.origin
        val a = vector dot vector
        val b = 2 * (vector dot distanceVector)
        val c = (distanceVector dot distanceVector) - math.pow(circle.radius, 2)
        
        // The discriminant portion of the equation determines the amount of intersection points (0 - 2)
        // d = b^2 - 4 * a * c
        val discriminant = math.pow(b, 2) - 4 * a * c
        println(discriminant)
        
        if (discriminant < 0)
        {
            Vector[Vector3D]()
        }
        else
        {
            var intersectionPoints = Vector[Vector3D]()
            
            // t = (-b +- sqrt(d)) / 2a
            val tEnter = (-b - math.sqrt(discriminant)) / (2 * a)
            println(tEnter)
            if (!onlyPointsInSegment || (tEnter >= 0 && tEnter <= 1))
            {
                intersectionPoints :+= apply(tEnter)
            }
            
            if (discriminant ~== 0)
            {
                Vector(tEnter)
            }
            else
            {
                val tExit = (-b + math.sqrt(discriminant) / (2 * a))
                println(tExit)
                if (!onlyPointsInSegment || (tExit >= 0 && tExit <= 1))
                {
                    intersectionPoints :+= apply(tExit)
                }
            }
            
            println(intersectionPoints)
            intersectionPoints
        }
    }
    
    /*
     * Finds the intersection points between this line and a circle. Only works in 2D.
     * @param circle a circle
     * @param onlyPointsInSegment determines whether only points between this line's start and end
     * point should be included. Defaults to true.
     * @return The intersection points between this line and the circle. Empty if there is no
     * intersection, one point if the line is tangential to the circle or starts / ends inside the
     * circle. Enter point and exit point (in that order) in case the line traverses through the 
     * circle
     */
    /*
    def circleIntersection(circle: Circle, onlyPointsInSegment: Boolean = true) = 
    {
        /*
		 * Terms for the quadratic equation
		 * --------------------------------
		 * 
		 * a = (x1 - x0)^2 + (y1 - y0)^2
		 * b = 2 * (x1 - x0) * (x0 - cx) + 2 * (y1 - y0) * (y0 - cy)
		 * c = (x0 - cx)^2 + (y0 - cy)^2 - r^2
		 * 
		 * Where (x1, y1) is the end point, (x0, y0) is the starting point, (cx, cy) is the 
		 * circle origin and r is the circle radius
		 * 
		 * Vx = (x1 - x0), The transition vector (end - start)
		 * Vy = (y1 - y0)
		 * 
		 * Lx = (x0 - cx), The transition vector from the circle origin to the line start
		 * Ly = (y0 - cy)	(start - origin)
		 * 
		 * With this added:
		 * a = Vx^2  +  Vy^2
		 * b = 2 * Vx * Lx  +  2 * Vy * Ly
		 * c = Lx^2  +  Ly^2  -  r^2
		 */
        val L = start - circle.origin
        
        val a = math.pow(vector.x, 2) + math.pow(vector.y, 2)
        val b = 2 * vector.x * L.x + 2 * vector.y * L.y
        val c = math.pow(L.x, 2) + math.pow(L.y, 2) - math.pow(circle.radius, 2)
        
        /*
		 * The equation
		 * ------------
		 * 
		 * t = (-b +- sqrt(b^2 - 4*a*c)) / (2*a)
		 * Where t is the modifier for the intersection points [0, 1] would be on the line
		 * Where b^2 - 4*a*c is called the discriminant and where a != 0
		 * 
		 * If The discriminant is negative, there is no intersection
		 * If the discriminant is 0, there is a single intersection point
		 * Otherwise there are two
		 */
        var intersectionPoints = Vector[Vector3D]()
        
        val discriminant = math.pow(b, 2) - 4 * a * c
        
        if (a != 0 && discriminant >= 0)
        {
            val discriminantRoot = math.sqrt(discriminant)
            val tEnter = (-b - discriminantRoot) / (2 * a)
            
            /*
			 * The intersection points
			 * -----------------------
			 * 
			 * The final intersection points are simply
			 * start + t * V
			 * Where start is the line start position, and V is the line translation vector 
			 * (end - start)
			 */
            if (!onlyPointsInSegment || (tEnter >= 0 && tEnter <= 1))
            {
                intersectionPoints :+= apply(tEnter)
            }
            
            if (!(discriminant ~== 0))
            {
                val tExit = (-b + discriminantRoot) / (2 * a)
                
                if (!onlyPointsInSegment || (tExit >= 0 && tExit <= 1))
                {
                    intersectionPoints :+= apply(tExit)
                }
            }
        }
        
        intersectionPoints
    }*/
}