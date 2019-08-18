package utopia.genesis.animation

import scala.concurrent.duration.Duration

/**
  * This transformation has a time element
  * @author Mikko Hilpinen
  * @since 18.8.2019, v2.1+
  */
trait TimedTransform[-Origin, +Reflection] extends AnimatedTransform[Origin, Reflection]
{
	// ABSTRACT	-------------------------
	
	/**
	  * @return The duration of this transform
	  */
	def duration: Duration
	
	
	// OTHER	-------------------------
	
	/**
	  * @param original Original animated instance
	  * @param passedTime Amount of passed time since animation start
	  * @return Animation's progress at specified time
	  */
	def apply(original: Origin, passedTime: Duration): Reflection = apply(original, passedTime / duration)
	
	/**
	  * @param original Original animated instance
	  * @param passedTime Amount of passed time since animation start
	  * @return Animation's progress at specified time. If time is larger than the duration of this transform,
	  *         this transform is repeated.
	  */
	def repeating(original: Origin, passedTime: Duration) =
	{
		val d = duration.toNanos
		apply(original, (passedTime.toNanos % d) / d.toDouble)
	}
}
