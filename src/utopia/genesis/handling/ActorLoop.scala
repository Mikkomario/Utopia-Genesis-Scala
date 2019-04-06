package utopia.genesis.handling

import java.time.{Duration, Instant}

import utopia.flow.async.Loop
import utopia.flow.util.TimeExtensions._
import utopia.flow.util.WaitTarget.Until

class ActorLoop(private val handler: ActorHandler, val apsRange: Range) extends Loop
{
	// ATTRIBUTES	-------------------
	
	/**
	  * @return The minimum interval between act calls
	  */
	def minInterval = apsToDuration(apsRange.end)
	
	/**
	  * @return The maximum interval passed to act calls (actual interval may be longer)
	  */
	def maxInterval = apsToDuration(apsRange.start)
	
	private var lastActStarted = Instant.now()
	
	
	// IMPLEMENTED	-------------------
	
	/**
	  * Calls act(...) of all associated Actors
	  */
	override protected def runOnce() =
	{
		val actStarted = Instant.now()
		// MaxAPS may affect calculations so that the real time lapse is not used
		// This will result in program "slowdown"
		val sinceLastAct = (actStarted - lastActStarted) min maxInterval
		lastActStarted = actStarted
		
		handler.act(sinceLastAct)
	}
	
	/**
	  * The time between the end of the current run and the start of the next one
	  */
	override protected def nextWaitTarget = Until(lastActStarted + minInterval)
	
	
	// OTHER	------------------------
	
	private def apsToDuration(aps: Int) = if (aps <= 0) Duration.ZERO else Duration.ofNanos((1000.0 / aps * 1000000).toLong)
}
