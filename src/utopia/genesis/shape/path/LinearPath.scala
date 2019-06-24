package utopia.genesis.shape.path

import utopia.genesis.util.Arithmetic

/**
  * This path provides a direct path between two values
  * @author Mikko Hilpinen
  * @since 20.6.2019, v2.1+
  */
case class LinearPath[P <: Arithmetic[P, P]](start: P, end: P) extends LinearPathLike[P]
{
	override def length = end.distanceFrom(start)
}
