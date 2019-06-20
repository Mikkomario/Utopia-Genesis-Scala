package utopia.genesis.shape.path

import utopia.genesis.util.{Combinable, Scalable}

/**
  * This path provides a direct path between two values
  * @author Mikko Hilpinen
  * @since 20.6.2019, v2.1+
  */
case class LinearPath[P <: Scalable[P] with Combinable[P, P]](start: P, end: P) extends LinearPathLike[P]
