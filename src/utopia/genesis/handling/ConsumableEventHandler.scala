package utopia.genesis.handling

import utopia.genesis.event.Consumable
import utopia.inception.handling.Handleable

/**
  * These handlers distribute consumable events
  * @author Mikko Hilpinen
  * @since 10.5.2019, v1+
  */
trait ConsumableEventHandler[Listener <: Handleable, Event <: Consumable[Event]] extends EventHandler[Listener, Event]
{
	// ABSTRACT	---------------------
	
	/**
	  * Informs a listener about an event
	  * @param listener A listener
	  * @param event An event
	  * @return Whether the event should be marked as consumed afterwards
	  */
	override protected def inform(listener: Listener, event: Event): Boolean
	
	
	// OTHER	--------------------
	
	override def distribute(event: Event): Boolean =
	{
		if (event.isConsumed)
		{
			// If event is already consumed, doesn't worry about changing event state
			super.distribute(event)
			true
		}
		else
		{
			// Handles items until the event is consumed, after which continues with a consumed event
			val view = handleView()
			val consumedAtIndex = view.indexWhere { l => if (eventFilterFor(l)(event)) inform(l, event) else false }
			if (consumedAtIndex >= 0)
			{
				val consumedEvent = event.consumed
				view.drop(consumedAtIndex + 1).foreach {
					l => if (eventFilterFor(l)(consumedEvent)) inform(l, consumedEvent) }
				true
			}
			else
				false
		}
	}
}
