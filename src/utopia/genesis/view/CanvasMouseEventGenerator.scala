package utopia.genesis.view

import java.awt.event.MouseListener
import java.awt.event.MouseWheelListener
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseMotionListener
import utopia.genesis.util.Vector3D
import utopia.genesis.event.MouseMoveEvent
import utopia.genesis.event.MouseMoveHandler
import utopia.genesis.event.Actor
import java.awt.Container
import java.awt.MouseInfo

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
    
    private var lastMousePosition = Vector3D.zero
    
    
    // IMPLEMENTED METHODS    --------
    
    override def act(durationMillis: Double) = 
    {
        // Checks for mouse movement
        val mousePosition = pointInPanel(Vector3D of MouseInfo.getPointerInfo.getLocation, 
                canvas) / canvas.scaling;
        
        if (mousePosition != lastMousePosition)
        {
            val event = new MouseMoveEvent(mousePosition, lastMousePosition, durationMillis)
            lastMousePosition = mousePosition
            moveHandler.onMouseMove(event)
        }
    }
    
    
    // OTHER METHODS    --------------
    
    private def pointInPanel(point: Vector3D, panel: Container): Vector3D = 
    {
        val relativePoint = point - (Vector3D of panel.getLocation)
        val parent = panel.getParent
        
        if (parent == null) relativePoint else pointInPanel(relativePoint, parent)
    }
    
    
    /*
    private class MouseEventReceiver extends MouseListener with MouseWheelListener
    {
        override def mousePressed(e: MouseEvent)= Unit
        override def mouseReleased(e: MouseEvent) = Unit
        override def mouseWheelMoved(e: MouseWheelEvent) = Unit
        
        override def mouseClicked(e: MouseEvent) = Unit
        override def mouseEntered(e: MouseEvent) = Unit
        override def mouseExited(e: MouseEvent) = Unit
    }
    */
}