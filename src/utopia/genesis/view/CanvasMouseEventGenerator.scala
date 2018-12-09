package utopia.genesis.view

import java.awt.event.MouseListener
import java.awt.event.MouseWheelListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import utopia.genesis.shape.Vector3D
import utopia.genesis.event.MouseMoveEvent
import utopia.genesis.event.MouseMoveHandler
import utopia.genesis.event.Actor
import java.awt.Container
import java.awt.MouseInfo
import utopia.genesis.event.MouseButtonStateHandler
import utopia.genesis.event.MouseButtonStateEvent
import utopia.genesis.event.MouseButtonStatus
import utopia.genesis.event.MouseWheelHandler
import utopia.genesis.event.MouseWheelEvent
import utopia.genesis.shape.shape2D.Point

/**
 * This class listens to mouse status inside a canvas and generates new mouse events. This 
 * class implements the Actor trait so in order to work, it should be added to a working
 * actorHandler.
 * @author Mikko Hilpinen
 * @since 22.1.2017
 */
class CanvasMouseEventGenerator(val canvas: Canvas) extends Actor
{
    // ATTRIBUTES    -----------------
    
    /**
     * This handler informs mouse move listeners about the new mouse move events
     */
    val moveHandler = new MouseMoveHandler()
    /**
     * This handler informs mouse button listeners when a mouse button is pressed or released
     */
    val buttonStateHandler = new MouseButtonStateHandler()
    /**
     * This handler informs mouse wheel listeners when the mouse wheel is rotated
     */
    val wheelHandler = new MouseWheelHandler()
    
    private var lastMousePosition = Point.origin
    private var buttonStatus = new MouseButtonStatus()
    
    
    // INITIAL CODE    ---------------
    
    // Starts listening for mouse events inside the canvas
    canvas.addMouseListener(new MouseEventReceiver())
    canvas.addMouseWheelListener(new MouseWheelEventReceiver())
    
    
    // IMPLEMENTED METHODS    --------
    
    override def act(durationMillis: Double) = 
    {
        // Checks for mouse movement
        val mousePosition = (pointInPanel(Point of MouseInfo.getPointerInfo.getLocation, 
                canvas) / canvas.scaling).toPoint;
        
        if (mousePosition != lastMousePosition)
        {
            val event = new MouseMoveEvent(mousePosition, lastMousePosition, buttonStatus, durationMillis)
            lastMousePosition = mousePosition
            moveHandler.onMouseMove(event)
        }
    }
    
    
    // OTHER METHODS    --------------
    
    private def pointInPanel(point: Point, panel: Container): Vector3D = 
    {
        val relativePoint = point - (Point of panel.getLocation)
        val parent = panel.getParent
        
        if (parent == null) relativePoint else pointInPanel(relativePoint.toPoint, parent)
    }
    
    
    // NESTED CLASSES    ------------
    
    private class MouseEventReceiver extends MouseListener
    {
        override def mousePressed(e: MouseEvent) = 
        {
            buttonStatus += e.getButton -> true
            buttonStateHandler.onMouseButtonState(new MouseButtonStateEvent(e.getButton, true, 
                    lastMousePosition, buttonStatus))
        }
        
        override def mouseReleased(e: MouseEvent) = 
        {
            buttonStatus += e.getButton -> false
            buttonStateHandler.onMouseButtonState(new MouseButtonStateEvent(e.getButton, false, 
                    lastMousePosition, buttonStatus))
        }
        
        override def mouseClicked(e: MouseEvent) = Unit
        override def mouseEntered(e: MouseEvent) = Unit
        override def mouseExited(e: MouseEvent) = Unit
    }
    
    private class MouseWheelEventReceiver extends MouseWheelListener
    {
        override def mouseWheelMoved(e: java.awt.event.MouseWheelEvent) = 
                wheelHandler.onMouseWheelRotated(new MouseWheelEvent(e.getWheelRotation, 
                lastMousePosition, buttonStatus));
    }
}