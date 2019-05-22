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
	
	override def distribute(event: Event): Boolean = event.distributeAmong(handleView().toVector) {
		(l, e) => if (eventFilterFor(l)(e)) inform(l, e) else false }
}
