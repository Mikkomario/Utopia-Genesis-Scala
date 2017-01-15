package utopia.genesis.util

import utopia.genesis.util.Extensions._

/**
 * Circles are shapes that are formed by an origin and a radius
 * @author Mikko Hilpinen
 * @since 1.1.2017
 */
case class Circle(val origin: Vector3D, radius: Double)
{
    // OPERATORS    -------------------
    
    /**
     * Scales the circle's radius by the provided amount
     */
    def *(d: Double) = copy(radius = radius * d)
    
    
    // OTHER METHODS    ---------------
    
    /**
     * Checks whether the circle contains the provided point
     */
    def contains(point: Vector3D) = (point - origin).length <= radius
    
    /**
     * Checks whether the other circle is contained within this circle's area
     */
    def contains(other: Circle) = (origin - other.origin).length <= radius - other.radius
    
    /**
     * Checks whether the two circles intersect with each other
     */
    def intersectsWith(other: Circle) = (origin - other.origin).length <= radius + other.radius
    
    /**
     * Projects the circle over a certain axis
     */
    def projectedOver(axis: Vector3D) = Line(origin- axis.withLength(radius), 
            origin + axis.withLength(radius));
    
    // check sphere intersection 
    // http://stackoverflow.com/questions/5048701/finding-points-of-intersection-when-two-spheres-intersect
    
    /**
     * Finds the intersection points between this and another circle
     * @return Empty vector if there is no intersection (no contact or containment), one point if 
     * there is only a single intersection point, two points otherwise
     */
    def circleIntersection(other: Circle) = 
    {
        // References: http://paulbourke.net/geometry/circlesphere/
        // Distance vector D with length of d
        val distanceVector = other.origin - this.origin
        val distance = distanceVector.length
        
        // If there is containment (d < |r0 - r1|), there are no collision points
	    // Also, if the circles are identical, there are infinite number of collision 
		// points (they cannot be calculated)
        if (distance > radius + other.radius || distance < (radius - other.radius).abs || this == other)
        {
            Vector[Vector3D]()
        }
        else
        {
            /* We can form triangles using points P0, P1, P2 and P3(s)
			 * Where
			 * 		P0 = Center of the first circle
			 * 		P1 = Center of the second circle
			 * 		P2 = A point at the intersection of D and a line formed by the collision points
			 * 		P3 = The collision points at each side of P2
			 * 
			 * From this we get
			 * 		a^2 + h^2 = r0^2 (Pythagoras)
			 * 		b^2 + h^2 = r1^2 (Pythagoras)
			 * 		d = a + b (P2 divides D in two)
			 * Where
			 * 		a = |P2 - P0|
			 * 		b = |P2 - P1|
			 * 		r0 = first circle radius
			 * 		r1 = second circle radius
			 * 		d = |P0 - P1|
			 * 
			 * From these we get
			 * a = (r0^2 - r1^2 + d^2) / (2*d)
			 */
            val a = (math.pow(radius, 2) - math.pow(other.radius, 2) + math.pow(distance, 2)) / 
                    (2 * distance);
            /*
			 * From this we can solve h (|P3 - P2|) with
			 * 		h^2 = r0^2 - a^2 
			 * 	->	h = sqrt(r0^2 - a^2)
			 */
            val h = math.sqrt(math.pow(radius, 2) - math.pow(a, 2))
            /*
			 * We can also solve P2 with
			 * 		P2 = P0 + a*(P1 - P0) / d
			 * 	->	P2 = P0 + D * (a / d)
			 */
            val P2 = origin + distanceVector.withLength(a)
            
            // If may be that there is only a single collision point on the distance vector
            if (h ~== 0)
            {
                Vector(P2)
            }
            else
            {
                /*
        		 * From we see that H (P3 - P2) is perpendicular to D and has length h.
        		 * From these we can calculate
        		 * 		P3 = P2 +- H
        		 */
                val heightVector = distanceVector.normal2D.withLength(h)
                Vector(P2 + heightVector, P2 - heightVector)
            }
        }
    }
}