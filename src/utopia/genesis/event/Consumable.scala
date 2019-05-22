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
	/**
	  * @return This item as 'Repr'
	  */
	def me: Repr
	
	
	// OTHER	--------------------
	
	/**
	  * Handles this consumable item using a number of possibly consuming operations
	  * @param hasNext A function that returns whether there are still operations left
	  * @param take Performs a single operation on this consumable item. Returns whether this was consumed.
	  * @return Whether this event should now be considered consumed
	  */
	def handleWith(hasNext: => Boolean, take: Repr => Boolean) =
	{
		if (isConsumed)
		{
			while (hasNext) { take(me) }
			true
		}
		else
		{
			var wasConsumed = false
			var event = me
			while (hasNext)
			{
				if (wasConsumed)
					take(event)
				else if (take(event))
				{
					wasConsumed = true
					event = consumed
				}
			}
			wasConsumed
		}
	}
	
	/**
	  * Distributes this event among multiple listeners that may consume this event
	  * @param listeners The listeners
	  * @param call A function that informs a single listener about this event
	  * @tparam L The type of the listeners
	  * @return Whether this event should now be considered consumed
	  */
	def distributeAmong[L](listeners: Seq[L])(call: (L, Repr) => Boolean) =
	{
		var nextIndex = 0
		def hasNext = nextIndex < listeners.size
		def take(c: Repr) =
		{
			nextIndex += 1
			call(listeners(nextIndex - 1), c)
		}
		
		handleWith(hasNext, take)
	}
}
