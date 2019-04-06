package utopia.genesis.view

import java.awt.event.MouseListener
import java.awt.event.MouseWheelListener
import java.awt.event.MouseEvent

import utopia.genesis.shape.Vector3D
import utopia.genesis.event.MouseMoveEvent
import java.awt.Container
import java.awt.MouseInfo

import utopia.genesis.event.MouseButtonStateEvent
import utopia.genesis.event.MouseButtonStatus
import utopia.genesis.event.MouseWheelEvent
import utopia.genesis.shape.shape2D.Point
import java.time.Duration

import utopia.genesis.handling.{Actor, MouseButtonStateHandler, MouseMoveHandler, MouseWheelHandler}
import utopia.inception.handling.{HandlerType, Mortal}

import scala.ref.WeakReference

/**
 * This class listens to mouse status inside a canvas and generates new mouse events. This 
 * class implements the Actor trait so in order to work, it should be added to a working
 * actorHandler.
 * @author Mikko Hilpinen
 * @since 22.1.2017
 */
class CanvasMouseEventGenerator(c: Canvas, val moveHandler: MouseMoveHandler,
                                val buttonHandler: MouseButtonStateHandler, val wheelHandler: MouseWheelHandler) extends Actor with Mortal
{
    // ATTRIBUTES    -----------------
    
    private val canvas = WeakReference(c)
    
    private var lastMousePosition = Point.origin
    private var buttonStatus = MouseButtonStatus.empty
    
    
    // INITIAL CODE    ---------------
    
    // Starts listening for mouse events inside the canvas
    c.addMouseListener(new MouseEventReceiver())
    c.addMouseWheelListener(new MouseWheelEventReceiver())
    
    
    // IMPLEMENTED METHODS    --------
    
    override def parent = None
    
    override def allowsHandlingFrom(handlerType: HandlerType) = isActive
    
    // This generator dies once canvas is no longer reachable
    override def isDead = canvas.get.isEmpty
    
    override def act(duration: Duration) =
    {
        canvas.get.foreach
        {
            c =>
                // Checks for mouse movement
                val mousePosition = (pointInPanel(Point of MouseInfo.getPointerInfo.getLocation, c) / c.scaling).toPoint
                
                if (mousePosition != lastMousePosition)
                {
                    val event = new MouseMoveEvent(mousePosition, lastMousePosition, buttonStatus, duration)
                    lastMousePosition = mousePosition
                    moveHandler.onMouseMove(event)
                }
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
            buttonStatus += (e.getButton, true)
            buttonHandler.onMouseButtonState(new MouseButtonStateEvent(e.getButton, true, lastMousePosition, buttonStatus))
        }
        
        override def mouseReleased(e: MouseEvent) = 
        {
            buttonStatus += (e.getButton, false)
            buttonHandler.onMouseButtonState(new MouseButtonStateEvent(e.getButton, false, lastMousePosition, buttonStatus))
        }
        
        override def mouseClicked(e: MouseEvent) = Unit
        override def mouseEntered(e: MouseEvent) = Unit
        override def mouseExited(e: MouseEvent) = Unit
    }
    
    private class MouseWheelEventReceiver extends MouseWheelListener
    {
        override def mouseWheelMoved(e: java.awt.event.MouseWheelEvent) = 
                wheelHandler.onMouseWheelRotated(MouseWheelEvent(e.getWheelRotation, lastMousePosition, buttonStatus))
    }
}