package utopia.genesis.handling

import utopia.genesis.event.{MouseButton, MouseButtonStateEvent}
import utopia.inception.handling.Handleable
import utopia.inception.util.{AnyFilter, Filter}

object MouseButtonStateListener
{
    /**
      * Creates a new simple mouse button state listener
      * @param function A function that will be called when event is received
      * @param filter A filter that determines, which events will come through (default = no filtering)
      * @return A mouse button state listener
      */
    def apply(function: MouseButtonStateEvent => Unit,
              filter: Filter[MouseButtonStateEvent] = AnyFilter): MouseButtonStateListener =
        new FunctionalMouseButtonStateListener(function, filter)
    
    /**
      * Creates a new simple mouse button state listener that is called on mouse button presses
      * @param button The button that should be pressed
      * @param f A function that will be called when the button is pressed
      * @return A new mouse button state listener
      */
    def onButtonPressed(button: MouseButton, f: MouseButtonStateEvent => Unit) = apply(f,
        MouseButtonStateEvent.wasPressedFilter && MouseButtonStateEvent.buttonFilter(button))
    
    /**
      * Creates a simple mouse button state listener that is called when left mouse button is pressed
      * @param f A function that will be called
      * @return A new mouse button state listener
      */
    def onLeftPressed(f: MouseButtonStateEvent => Unit) = onButtonPressed(MouseButton.Left, f)
    
    /**
      * Creates a simple mouse button state listener that is called when right mouse button is pressed
      * @param f A function that will be called
      * @return A new mouse button state listener
      */
    def onRightPressed(f: MouseButtonStateEvent => Unit) = onButtonPressed(MouseButton.Right, f)
    
    /**
      * Creates a new simple mouse button state listener that is called on mouse button releases
      * @param button The button that should be released
      * @param f A function that will be called when the button is released
      * @return A new mouse button state listener
      */
    def onButtonReleased(button: MouseButton, f: MouseButtonStateEvent => Unit) = apply(f,
        MouseButtonStateEvent.wasReleasedFilter && MouseButtonStateEvent.buttonFilter(button))
    
    /**
      * Creates a simple mouse button state listener that is called when left mouse button is released
      * @param f A function that will be called
      * @return A new mouse button state listener
      */
    def onLeftReleased(f: MouseButtonStateEvent => Unit) = onButtonReleased(MouseButton.Left, f)
    
    /**
      * Creates a simple mouse button state listener that is called when right mouse button is released
      * @param f A function that will be called
      * @return A new mouse button state listener
      */
    def onRightReleased(f: MouseButtonStateEvent => Unit) = onButtonReleased(MouseButton.Right, f)
}

/**
 * This trait is extended by classes which are interested in changes in mouse button statuses
 * @author Mikko Hilpinen
 * @since 18.2.2017
 */
trait MouseButtonStateListener extends Handleable
{
    // ABSTRACT ---------------------------
    
    /**
     * This method will be called in order to inform the listener about a new mouse button event
     * (a mouse button being pressed or released)
     * @param event The mouse event that occurred
     */
    def onMouseButtonState(event: MouseButtonStateEvent)
    
    /**
     * The filter applied to the incoming mouse button events. The listener will only be informed
     * about the events accepted by the filter. The default filter accepts all mouse button events.
     */
    def mouseButtonStateEventFilter: Filter[MouseButtonStateEvent] = AnyFilter
    
    
    // COMPUTED ---------------------------
    
    /**
      * @return Whether this instance is currently willing to receive events for mouse button state changes
      */
    def isReceivingMouseButtonStateEvents = allowsHandlingFrom(MouseButtonStateHandlerType)
}

private class FunctionalMouseButtonStateListener(val function: MouseButtonStateEvent => Unit,
                                                 val filter: Filter[MouseButtonStateEvent] = AnyFilter)
    extends MouseButtonStateListener with utopia.inception.handling.immutable.Handleable
{
    override def onMouseButtonState(event: MouseButtonStateEvent) = function(event)
    
    override def mouseButtonStateEventFilter = filter
}