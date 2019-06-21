package utopia.genesis.shape.path

import utopia.genesis.util.Arithmetic

/**
  * Bezier functions can be used for calculating smooth paths between two points. This cubic implementation uses
  * two control points, which define the shape of the path.
  * @author Mikko Hilpinen
  * @since 19.6.2019, v2.1+
  * @param start The curve starting point
  * @param controlStart The first control point
  * @param controlEnd The second control point
  * @param end The curve end point
  */
case class CubicBezier[P <: Arithmetic[P, P]](override val start: P, controlStart: P, controlEnd: P, override val end: P)
	extends Path[P]
{
	override def apply(t: Double) =
	{
		// Mathematic function from: https://en.wikipedia.org/wiki/B%C3%A9zier_curve
		// B(t) = (1-t)^3 P0 + 3(1-t)^2 tP1 + 3(1-t)t^2 P2 + t^3 P3
		val p0Term = start * Math.pow(1 - t, 3)
		val p1Term = controlStart * t * 3 * Math.pow(1 - t, 2)
		val p2Term = controlEnd * Math.pow(t, 2) * 3 * (1 - t)
		val p3Term = end * Math.pow(t, 3)
		
		p0Term + p1Term + p2Term + p3Term
	}
}
