package utopia.genesis.animation

import scala.concurrent.duration.{Duration, FiniteDuration}

object TimedAnimation
{
	/**
	  * Wraps an animation by giving it duration
	  * @param animation An animation
	  * @param duration Targeted animation duration
	  * @tparam A Type of animation result
	  * @return A new timed animation
	  */
	def wrap[A](animation: Animation[A], duration: Duration) = TimedAnimationWrapper(animation, duration)
}

/**
  * This type of animation has a specific duration
  * @author Mikko Hilpinen
  * @since 13.8.2019, v2.1+
  */
trait TimedAnimation[+A, +Repr] extends Animation[A]
{
	// ABSTRACT	----------------------
	
	/**
	  * @return The duration this animation takes to complete
	  */
	def duration: Duration
	
	/**
	  * @param newDuration Target duration
	  * @return A copy of this animation with specified duration
	  */
	def withDuration(newDuration: Duration): Repr
	
	
	// OTHER	-----------------------
	
	/**
	  * @param timePoint The amount of time passed in this animation
	  * @return This animation's state at specified time point
	  */
	def apply(timePoint: FiniteDuration): A = apply(timePoint / duration)
	
	/**
	  * @param multiplier Animation speed multiplier
	  * @return A copy of this animation with multiplied animation speed
	  */
	def timesSpeed(multiplier: Double) = withDuration(duration / multiplier)
}

case class TimedAnimationWrapper[+A](animation: Animation[A], duration: Duration) extends
	TimedAnimation[A, TimedAnimationWrapper[A]]
{
	override def withDuration(newDuration: Duration) = TimedAnimationWrapper(animation, newDuration)
	
	override def apply(progress: Double) = animation(progress)
}