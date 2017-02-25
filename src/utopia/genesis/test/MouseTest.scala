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
import utopia.genesis.view.MainFrame
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.inception.handling.HandlerRelay
import utopia.genesis.util.Line
import utopia.genesis.event.MouseButtonStateListener
import utopia.genesis.event.MouseButtonStateEvent
import utopia.genesis.event.MouseEvent
import utopia.genesis.util.Transformation
import utopia.genesis.event.MouseWheelListener
import utopia.genesis.event.MouseWheelEvent

/**
 * This is a visual test for mouse event features. In the test, the two lines should point to the
 * mouse cursor whenever any mouse button is down. The two circles should change colour when the
 * mouse cursor hovers over the shapes. When the shapes are clicked, their colours should change.
 * Rotating the mouse wheel should increase / decrease the circle radius.
 * @author Mikko Hilpinen
 * @since 4.2.2017
 */
object MouseTest extends App
{
    class TestObject(position: Vector3D, radius: Double) extends Drawable with 
            MouseMoveListener with MouseButtonStateListener with MouseWheelListener
    {
        override val depth = 0
        
        private val area = Circle(Vector3D.zero, radius)
        
        private var lastMousePosition = Vector3D.zero
        private var mouseOver = false
        private var isOn = false
        private var transformation = Transformation.translation(position)
        
        override def draw(drawer: Drawer) = 
        {
            val copy = drawer.withColor(Some(if (isOn) Color.BLUE else if (mouseOver) Color.CYAN 
                    else Color.LIGHT_GRAY)).withAlpha(0.75);
            transformation(copy).draw(area)
            
            drawer.draw(Line(transformation.position, lastMousePosition))
        }
        
        override def onMouseMove(event: MouseMoveEvent) = 
        {
            lastMousePosition = event.mousePosition
            // TODO: Rename these transformation methods to something like 'toRelative' and 'toAbsolute'
            mouseOver = contains2D(event.mousePosition)
        }
        
        // It is possible to use super type filters in event filters, nice!
        override def mouseMoveEventFilter = MouseEvent.isLeftDownFilter
        
        // Only accepts mouse press events
        override def mouseButtonStateEventFilter = MouseButtonStateEvent.wasPressedFilter
        
        // Switches the state
        override def onMouseButtonState(event: MouseButtonStateEvent) = 
            if (contains2D(event.mousePosition)) isOn = !isOn;
        
        override def onMouseWheelRotated(event: MouseWheelEvent) = 
                transformation = transformation.scaled(1 + event.wheelTurn * 0.2);
        
        private def contains2D(point: Vector3D) = area.contains2D(transformation.invert(point))
    }
    
    // Creates the frame
    val gameWorldSize = Vector3D(800, 600)
    
    val canvas = new Canvas(gameWorldSize, 120)
    val frame = new MainFrame(canvas, gameWorldSize, "Mouse Test")
    
    // Creates event generators
    val actorThread = new ActorThread(10, 120)
    val mouseEventGen = new CanvasMouseEventGenerator(canvas)
    
    actorThread.handler += mouseEventGen
    
    val handlers = new HandlerRelay(actorThread.handler, mouseEventGen.moveHandler, 
            mouseEventGen.buttonStateHandler, mouseEventGen.wheelHandler, canvas.handler);
    
    // Creates test objects
    val area1 = new TestObject(gameWorldSize / 2, 128)
    val area2 = new TestObject(gameWorldSize / 2 + Vector3D(128), 64)
    
    handlers ++= (area1, area2)
    
    // Displays the frame
    frame.display()
    actorThread.start()
}