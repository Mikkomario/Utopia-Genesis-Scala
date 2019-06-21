package utopia.genesis.shape.path

import utopia.genesis.util.Arithmetic

import scala.collection.immutable.VectorBuilder

/**
  * Bezier paths consists of multiple cubic bezier parts, each of which is connected smoothly with each other
  * @author Mikko Hilpinen
  * @since 19.6.2019, v2.1+
  */
object BezierPath
{
	/**
	  * Calculates a bezier path between the specified points
	  * @param points The points that form the path. Must be non-empty.
	  * @tparam P The type of path point
	  * @return A bezier path between paths
	  * @throws IllegalArgumentException If points is empty
	  */
	def apply[P <: Arithmetic[P, P]](points: Vector[P]) =
	{
		if (points.isEmpty)
			throw new IllegalArgumentException("Bezier path must be initialized with at least 1 point")
		else if (points.size == 1)
			EmptyPath(points.head)
		else if (points.size == 2)
			LinearPath(points.head, points(1))
		else
			CompoundPath(calculateBezier(points))
	}
	
	// NB: The code in this function is heavily based on: https://www.stkent.com/2015/07/03/building-smooth-paths-using-bezier-curves.html
	private def calculateBezier[P <: Arithmetic[P, P]](points: Vector[P]): Vector[CubicBezier[P]] =
	{
		val start = points.head
		val end = points.last
		val curveAmount = points.size - 1
		
		def baseTarget(index: Int) =
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
		
		def lowerDiagonal(index: Int): Double = if (index < curveAmount - 1) 1 else 2
		
		def mainDiagonal(index: Int): Double = if (index < 1) 2 else if (index < curveAmount - 1) 2 else 7
		
		// Calculates upper diagonals
		val upperDiagonals =
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
		
		// Calculates targets
		val targets =
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
		
		// Calculates the first set of control points
		val firstControlPoints =
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
		
		// Calculates all control points
		val controlPoints =
		{
			// Calculates the second half of control points (last point is added separately)
			val secondControlPointsBuffer = new VectorBuilder[P]
			(0 until firstControlPoints.size - 1).foreach { i => secondControlPointsBuffer +=
				points(i + 1) * 2 - firstControlPoints(i + 1) }
			secondControlPointsBuffer += (end + firstControlPoints.last) / 2
			
			// Returns the paired control points
			firstControlPoints.zip(secondControlPointsBuffer.result())
		}
		
		// Calculates the actual curves based on control points
		(0 until curveAmount).map
		{
			i =>
				val curveStart = points(i)
				val curveEnd = points(i + 1)
				val (c1, c2) = controlPoints(i)
				
				CubicBezier(curveStart, c1, c2, curveEnd)
		}.toVector
	}
}
