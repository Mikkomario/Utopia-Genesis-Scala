package utopia.genesis.test

import utopia.genesis.util.Drawer
import utopia.genesis.shape.Vector3D
import java.awt.Color

import utopia.genesis.shape.shape2D.Line
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.genesis.event.MouseMoveEvent
import utopia.genesis.handling.{Drawable, MouseMoveListener}
import utopia.genesis.shape.shape2D.Circle
import utopia.genesis.util.DepthRange
import utopia.genesis.shape.shape2D.ShapeConvertible

/**
 * This test tests the drawer's clipping functionality
 * @author Mikko Hilpinen
 * @since 25.2.2017
 */
object ClippingTest extends App
{   
    /* TODO: Return and fix code after refactoring is done
    class HiddenShapeDrawer(val shapes: Iterable[ShapeConvertible]) 
            extends Drawable with MouseMoveListener
    {
        override val depth = DepthRange.foreground
        
        private val clipRadius = 64
        private var clip = Circle(Vector3D.zero, clipRadius)
        
        def draw(drawer: Drawer) = 
        {
            drawer.withEdgeColor(None).draw(clip)
            
            val clipped = drawer.withColor(Some(Color.RED)).clippedTo(clip)
            shapes.foreach(clipped.draw)
        }
        
        def onMouseMove(event: MouseMoveEvent) = clip = Circle(event.mousePosition, clipRadius)
    }
    
    val worldSize = Vector3D(800, 600)
    val canvas = new Canvas(worldSize, 120)
    val frame = new MainFrame(canvas, worldSize, "Clipping Test")
    
    val actorThread = new ActorThread(20, 120)
    val mouseGen = new CanvasMouseEventGenerator(canvas)
    actorThread.handler += mouseGen
    
    val handlers = new HandlerRelay(actorThread.handler, canvas.handler, mouseGen.moveHandler)
    
    handlers += new GridDrawer(worldSize, Vector3D(64, 64))
    handlers += new HiddenShapeDrawer(Vector(
            Circle(worldSize / 2, 32), Circle(worldSize * 0.7, 64), 
            Circle(worldSize.xProjection + Vector3D(- 64, 64), 32)));
    
    actorThread.start()
    frame.display()
    * */
}