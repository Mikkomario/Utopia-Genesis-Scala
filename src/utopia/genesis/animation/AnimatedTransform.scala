package utopia.genesis.animation

/**
  * Animations are transformations that can be repeated and are applied over time
  * @author Mikko Hilpinen
  * @since 11.8.2019, v2.1+
  */
trait AnimatedTransform[-Origin, +Reflection]
{
	// ABSTRACT	----------------------
	
	/**
	  * Transforms an item based on this animation and specified progress
	  * @param original The original item
	  * @param progress The progress on this animation [0, 1] where 0 is the beginning and 1 is the end
	  * @return Transformed item
	  */
	def apply(original: Origin, progress: Double): Reflection
}
