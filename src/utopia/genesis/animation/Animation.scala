package utopia.genesis.animation

import utopia.genesis.shape.path.Path

/**
  * Animations are transformations that can be repeated and are applied over time
  * @author Mikko Hilpinen
  * @since 11.8.2019, v2.1+
  */
trait Animation[-Origin, +Reflection, Step]
{
	// ABSTRACT	----------------------
	
	/**
	  * @return The path used for specifying the individual steps of this animation
	  */
	def path: Path[Step]
	
	/**
	  * Transforms an item based on this animation
	  * @param original The original item
	  * @param step The targeted animation step
	  * @return Transformed item
	  */
	protected def transform(original: Origin, step: Step): Reflection
	
	
	// OTHER	---------------------
	
	/**
	  * Transforms an item based on this animation and specified progress
	  * @param original The original item
	  * @param progress The progress on this animation
	  * @return Transformed item
	  */
	def apply(original: Origin, progress: Double) = transform(original, path(progress))
}
