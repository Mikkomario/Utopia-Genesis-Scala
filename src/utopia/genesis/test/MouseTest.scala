package utopia.genesis.test

import utopia.genesis.util.Circle
import utopia.genesis.event.Drawable
import utopia.genesis.util.Drawer
import java.awt.Color
import utopia.genesis.event.MouseMoveListener
import utopia.genesis.event.MouseMoveEvent
import utopia.genesis.event.ActorThread
import utopia.genesis.util.Vector3D
import utopia.genesis.view.Canvas
import utopia.genesis.view.GameFrame
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.inception.handling.HandlerRelay
import utopia.genesis.util.Line
import utopia.genesis.event.MouseButtonStateListener
import utopia.genesis.event.MouseButtonStateEvent
import utopia.genesis.event.MouseEvent

/**
 * This is a visual test for mouse event features
 * @author Mikko Hilpinen
 * @since 4.2.2017
 */
object MouseTest extends App
{
    class TestObject(val area: Circle) extends Drawable with MouseMoveListener with MouseButtonStateListener
    {
        override val depth = 0
        
        private var lastMousePosition = Vector3D.zero
        private var mouseOver = false
        private var isOn = false
        
        override def draw(drawer: Drawer) = 
        {
            drawer.fillColor = if (isOn) Color.BLUE else if (mouseOver) Color.CYAN else Color.LIGHT_GRAY
            drawer.draw(area)
            
            drawer.draw(Line(area.origin, lastMousePosition))
        }
        
        // TODO: Add area trait that has containment
        override def onMouseMove(event: MouseMoveEvent) = 
        {
            lastMousePosition = event.mousePosition
            mouseOver = event.isOverArea(area)
        }
        
        // It is possible to use super type filters in event filters, nice!
        override def mouseMoveEventFilter = MouseEvent.isLeftDownFilter
        
        // Only accepts mouse press events
        override def mouseButtonStateEventFilter = MouseButtonStateEvent.wasPressedFilter
        
        // Switches the state
        override def onMouseButtonState(event: MouseButtonStateEvent) = 
            if (area.contains(event.mousePosition)) isOn = !isOn;
    }
    
    // Creates the frame
    val gameWorldSize = Vector3D(800, 600)
    
    val canvas = new Canvas(gameWorldSize, 120)
    val frame = new GameFrame(canvas, gameWorldSize, "Mouse Test")
    
    // Creates event generators
    val actorThread = new ActorThread(10, 120)
    val mouseEventGen = new CanvasMouseEventGenerator(canvas)
    
    actorThread.handler += mouseEventGen
    
    val handlers = new HandlerRelay(actorThread.handler, mouseEventGen.moveHandler, 
            mouseEventGen.buttonStateHandler, canvas.handler);
    
    // Creates test objects
    val area1 = new TestObject(Circle(gameWorldSize / 2, 128))
    val area2 = new TestObject(Circle(gameWorldSize / 2 + Vector3D(128), 64))
    
    handlers ++= (area1, area2)
    
    // Displays the frame
    frame.display()
    actorThread.start()
}