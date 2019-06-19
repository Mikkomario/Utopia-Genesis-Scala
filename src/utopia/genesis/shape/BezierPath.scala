package utopia.genesis.shape

import scala.collection.immutable.VectorBuilder

/**
  * This path consists of multiple cubic bezier parts, each of which is connected smoothly with each other
  * @author Mikko Hilpinen
  * @since 19.6.2019, v2.1+
  */
// NB: The code in this class is heavily based on: https://www.stkent.com/2015/07/03/building-smooth-paths-using-bezier-curves.html
case class BezierPath[P <: VectorLike[P]](points: Vector[P]) extends Path[P]
{
	// INITIAL CODE	--------------------
	
	if (points.size < 3)
		throw new IllegalArgumentException("a Bezier path must be initialized with at least 3 points")
	
	private val curves =
	{
		val controls = controlPoints
		(0 until curveAmount).map
		{
			i =>
				val curveStart = points(i)
				val curveEnd = points(i + 1)
				val (c1, c2) = controls(i)
				
				CubicBezier(curveStart, c1, curveEnd, c2)
		}.toVector
	}
	
	
	// IMPLEMENTED	--------------------
	
	override def start = points.head
	
	override def end = points.last
	
	override def apply(t: Double) =
	{
		// Handles cases where t is out of bounds
		if (t <= 0)
			start
		else if (t >= 1)
			end
		else
		{
			// Finds the curve that contains the target point and the 't' in that curve
			val curveIndex = (t * curves.size).toInt
			val curveT = t - curveIndex / curves.size
			
			curves(curveIndex)(curveT)
		}
	}
	
	
	// COMPUTED	-----------------------
	
	private def curveAmount = points.size - 1
	
	private def controlPoints =
	{
		// Forward sweep to prepare first half of control points
		val upper = upperDiagonals
		val targets = calculateTargets(upper)
		
		// Backward sweep to finish of the first half of control points
		val firstControlPoints = calculateFirstControlPoints(upper, targets)
		
		// Calculates the second half of control points (last point is added separately)
		val secondControlPointsBuffer = new VectorBuilder[P]
		(0 until firstControlPoints.size - 1).foreach { i => secondControlPointsBuffer +=
			points(i + 1) * 2 - firstControlPoints(i + 1) }
		secondControlPointsBuffer += (end + firstControlPoints.last) / 2
		
		// Returns the paired control points
		firstControlPoints.zip(secondControlPointsBuffer.result())
	}
	
	private def upperDiagonals =
	{
		val upperDiagonalBuffer = new VectorBuilder[Double]
		
		// Adds first point
		var lastNewUpperDiagonal = 1 / mainDiagonal(0)
		upperDiagonalBuffer += lastNewUpperDiagonal
		
		// Adds the rest of the points
		(1 until (curveAmount - 1)).foreach
		{
			i =>
				lastNewUpperDiagonal = 1 / (mainDiagonal(i) - lowerDiagonal(i - 1) * lastNewUpperDiagonal)
				upperDiagonalBuffer += lastNewUpperDiagonal
		}
		
		upperDiagonalBuffer.result()
	}
	
	
	// OTHER	----------------------
	
	private def baseTarget(index: Int) =
	{
		// Case: First point
		if (index < 1)
			start + (points(1) * 2)
		// Case: One of middle points
		else if (index < curveAmount - 1)
			(points(index) * 2 + points(index + 1)) * 2
		// Case: Final point
		else
			points(curveAmount - 1) * 8 + points(curveAmount)
	}
	
	private def lowerDiagonal(index: Int): Double = if (index < curveAmount - 1) 1 else 2
	
	private def mainDiagonal(index: Int): Double = if (index < 1) 2 else if (index < curveAmount - 1) 2 else 7
	
	private def calculateTargets(upperDiagonals: Vector[Double]) =
	{
		val targetBuffer = new VectorBuilder[P]
		
		// Adds the first point
		var lastTarget = baseTarget(0) * (1 / mainDiagonal(0))
		targetBuffer += lastTarget
		
		// Adds rest of the points
		(1 until curveAmount).foreach
		{
			i =>
				val targetScale = 1 / (mainDiagonal(i) - lowerDiagonal(i - 1) * upperDiagonals(i - 1))
				lastTarget = (baseTarget(i) - (lastTarget * lowerDiagonal(i - 1))) * targetScale
				targetBuffer += lastTarget
		}
		
		targetBuffer.result()
	}
	
	private def calculateFirstControlPoints(upperDiagonals: Vector[Double], targets: Vector[P]) =
	{
		// Last point (The points are added in reverse order)
		val firstControlPointsBuffer = new VectorBuilder[P]
		var lastControlPoint = targets.last
		firstControlPointsBuffer += lastControlPoint
		
		// Rest of the points
		(curveAmount - 2 to 0 by -1).foreach
		{
			i =>
				lastControlPoint = targets(i) - (lastControlPoint * upperDiagonals(i))
				firstControlPointsBuffer += lastControlPoint
		}
		
		firstControlPointsBuffer.result().reverse
	}
}
