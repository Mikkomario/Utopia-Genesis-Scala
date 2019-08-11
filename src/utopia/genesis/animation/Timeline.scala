package utopia.genesis.animation

import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * A chain of animations performed over a certain time period
  * @author Mikko Hilpinen
  * @since 11.8.2019, v2.1+
  */
case class Timeline[-Origin, +Reflection](events: Seq[(Animation[Origin, Reflection, _], FiniteDuration)],
										  delay: FiniteDuration = Duration.Zero)
{
	// ATTRIBUTES	----------------------
	
	/**
	  * The duration of the active portion of this timeline
	  */
	lazy val activeDuration = events.foldLeft(Duration.Zero) { (total, event) => total + event._2 }
	
	
	// COMPUTED	--------------------------
	
	/**
	  * The total duration of this timeline
	  */
	def duration = delay + activeDuration
	
	
	// OTHER	--------------------------
	
	/**
	  * Transforms an item based on the amount of passed time
	  * @param original Original item
	  * @param passedTime Amount of passed time
	  * @return Transformed item. None if there is no active transformation for the specified time
	  */
	def apply(original: Origin, passedTime: Duration) =
	{
		if (events.isEmpty)
			None
		else if (passedTime < delay)
			None
		else
		{
			// Finds the current event and uses that to transform the original item
			var timeLeft = passedTime - delay
			events.find { case (_, duration) =>
				if (timeLeft <= duration)
					true
				else
				{
					timeLeft -= duration
					false
				}
				
			}.map { case (event, duration) => event(original, timeLeft / duration) }
		}
	}
	
	/**
	  * @param newDelay New delay for this timeline
	  * @return A copy of this timeline with the new delay
	  */
	def withDelay(newDelay: FiniteDuration) = copy(delay = newDelay)
	
	/**
	  * @param delayChange Amount of duration the delay of this timeline is altered
	  * @return A copy of this timeline with altered delay
	  */
	def alterDelay(delayChange: FiniteDuration) = withDelay(delay + delayChange)
}
