package utopia.genesis.event

import utopia.genesis.shape.Vector3D

/**
 * This mouse event listener is interested to continually receive events while the mouse cursor is
 * hovering over the instance. In order to work, the listener needs to receive both mouse movement
 * and action events
 */
trait MouseOverListener extends MouseMoveListener with Actor
{
    // ATTRIBUTES    -----------
    
    private var _mousePosition = Vector3D.zero
    /**
     * The last mouse coordinates recorded by this listener
     */
    protected def mousePosition = _mousePosition
    
    
    // ABSTRACT METHODS    ----
    
    /**
     * This method will be constantly called while the mouse cursor remains over a specified area
     * @param durationMillis The duration since the last update, in milliseconds
     */
    def onMouseOver(durationMillis: Double)
    
    /**
     * This method is used for determining whether a specified coordinate is considered to be 
     * 'over' this instance.
     * @position a position that is tested
     */
    def contains(position: Vector3D): Boolean
    
    
    // IMPLEMENTED METHODS    -
    
    override def onMouseMove(event: MouseMoveEvent) = _mousePosition = event.mousePosition
    
    override def act(durationMillis: Double) = if (contains(mousePosition)) onMouseOver(durationMillis)
}