package utopia.genesis.test

import utopia.genesis.util.Drawer
import utopia.genesis.shape.Vector3D
import java.awt.Color

import utopia.flow.async.ThreadPool
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.genesis.event.MouseMoveEvent
import utopia.genesis.handling.mutable.{ActorHandler, DrawableHandler, MouseButtonStateHandler, MouseMoveHandler, MouseWheelHandler}
import utopia.genesis.handling.{ActorLoop, Drawable, MouseMoveListener}
import utopia.genesis.shape.shape2D.{Circle, Point, ShapeConvertible, Size}
import utopia.genesis.util.DepthRange
import utopia.inception.handling.immutable.Handleable
import utopia.inception.handling.mutable.HandlerRelay

import scala.concurrent.ExecutionContext

/**
 * This test tests the drawer's clipping functionality
 * @author Mikko Hilpinen
 * @since 25.2.2017
 */
object ClippingTest extends App
{
    class HiddenShapeDrawer(val shapes: Iterable[ShapeConvertible]) extends Drawable with MouseMoveListener with Handleable
    {
        override val drawDepth = DepthRange.foreground
        
        private val clipRadius = 64
        private var clip = Circle(Point.origin, clipRadius)
        
        def draw(drawer: Drawer) = 
        {
            drawer.withEdgeColor(None).draw(clip)
            
            val clipped = drawer.withColor(Some(Color.RED)).clippedTo(clip)
            shapes.foreach(clipped.draw)
        }
        
        def onMouseMove(event: MouseMoveEvent) = clip = Circle(event.mousePosition, clipRadius)
    }
	
	// Creates the handlers
    val worldSize = Size(800, 600)
    
	val drawHandler = DrawableHandler()
	val actorHandler = ActorHandler()
	val mouseMoveHandler = MouseMoveHandler()
	val mouseButtonHandler = MouseButtonStateHandler()
	val mouseWheelHandler = MouseWheelHandler()
	
	val handlers = HandlerRelay(drawHandler, actorHandler, mouseMoveHandler, mouseButtonHandler, mouseWheelHandler)
	
	// Creates the frame
	val canvas = new Canvas(drawHandler, worldSize)
    val frame = new MainFrame(canvas, worldSize, "Clipping Test")
    
	// Creates event generators
    val actorLoop = new ActorLoop(actorHandler, 20 to 120)
    val mouseGen = new CanvasMouseEventGenerator(canvas, mouseMoveHandler, mouseButtonHandler, mouseWheelHandler)
    
	actorHandler += mouseGen
	
	// Creates test objects
    handlers += new GridDrawer(worldSize, Size(64, 64))
    handlers += new HiddenShapeDrawer(Vector(
            Circle((worldSize / 2).toPoint, 32), Circle((worldSize * 0.7).toPoint, 64),
            Circle((worldSize.xProjection + Vector3D(- 64, 64)).toPoint, 32)))
    
	// Starts the program
	implicit val context: ExecutionContext = new ThreadPool("Test").executionContext
	
	canvas.startAutoRefresh()
	actorLoop.startAsync()
    frame.display()
}