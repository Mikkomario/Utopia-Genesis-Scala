package utopia.genesis.event

import utopia.inception.util.Filter

object Consumable
{
	val notConsumedFilter: Filter[Consumable[_]] = !_.isConsumed
}

/**
  * These items may be consumed. Usually used with events when one wants to inform other listeners that the event
  * has already been 'used'
  * @author Mikko Hilpinen
  * @since 10.5.2019, v2.1+
  */
trait Consumable[+Repr]
{
	// ABSTRACT	---------------------
	
	/**
	  * @return Whether this item has been consumed already
	  */
	def isConsumed: Boolean
	/**
	  * @return A consumed version of this item
	  */
	def consumed: Repr
}
