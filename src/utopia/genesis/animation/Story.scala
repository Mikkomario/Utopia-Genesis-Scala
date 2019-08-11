package utopia.genesis.animation

import utopia.flow.util.CollectionExtensions._

import scala.concurrent.duration.Duration

/**
  * Used for transforming an instance using possibly multiple timelines and layers of transformations
  * @author Mikko Hilpinen
  * @since 11.8.2019, v2.1+
  */
trait Story[-Origin, Reflection]
{
	// ABSTRACT	--------------------
	
	def timelines: Seq[Timeline[Reflection, Reflection]]
	
	
	// COMPUTED	--------------------
	
	def start = timelines.map { _.delay }.minOption.getOrElse(Duration.Undefined)
	
	def end = timelines.map { _.duration }.maxOption.getOrElse(Duration.Undefined)
}
