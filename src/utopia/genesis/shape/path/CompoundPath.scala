package utopia.genesis.shape.path

object CompoundPath
{
	/**
	  * Combines multiple paths
	  * @param first The first path
	  * @param second The second path
	  * @param more more paths
	  * @tparam P The type of paths
	  * @return A new compound path
	  */
	def apply[P](first: Path[P], second: Path[P], more: Path[P]*): CompoundPath[P] = CompoundPath(Vector(first, second) ++ more)
}

/**
  * Compound paths consist of multiple smaller path segments
  * @author Mikko Hilpinen
  * @since 20.6.2019, v2.1+
  */
case class CompoundPath[+P](parts: Vector[Path[P]]) extends Path[P]
{
	// INITIAL CODE	-------------------
	
	if (parts.isEmpty)
		throw new IllegalArgumentException("Compund path must be initialized with at least a single part")
	
	
	// IMPLEMENTED	-------------------
	
	override def start = parts.head.start
	
	override def end = parts.last.end
	
	override def apply(t: Double) =
	{
		// Handles cases where t is out of bounds
		if (t <= 0)
			parts.head(t * parts.size)
		else if (t >= 1)
			parts.last((t - (parts.size - 1) / parts.size) * parts.size)
		else
		{
			// Finds the curve that contains the target point and the 't' in that curve
			val partIndex = (t * parts.size).toInt
			val partT = (t - partIndex / parts.size) * parts.size
			
			parts(partIndex)(partT)
		}
	}
}
