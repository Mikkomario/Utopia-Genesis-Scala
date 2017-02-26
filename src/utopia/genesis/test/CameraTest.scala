package utopia.genesis.test

import utopia.genesis.util.Vector3D
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.inception.handling.HandlerRelay
import java.awt.Font
import utopia.genesis.event.Drawable
import utopia.genesis.util.Drawer
import utopia.genesis.util.Bounds
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.genesis.event.ActorThread

object CameraTest extends App
{
    class GridNumberDrawer(private val grid: GridDrawer) extends Drawable
    {
        private val font = new Font("Arial", 0, 14)
        
        override def draw(drawer: Drawer) = 
        {
            // Draws a number on each grid square
            for (x <- 0 until grid.squareAmounts.x.toInt; y <- 0 until grid.squareAmounts.y.toInt)
            {
                drawer.drawTextCentered((x + y + 1) + "", font, 
                        Bounds(grid.squarePosition(x, y), grid.squareSize))
            }
        }
    }
    
    val worldSize = Vector3D(800, 600)
    
    val canvas = new Canvas(worldSize, 120)
    val frame = new MainFrame(canvas, worldSize, "Camera Test")
    
    val actorThread = new ActorThread(20, 120)
    val mouseEventGen = new CanvasMouseEventGenerator(canvas)
    actorThread.handler += mouseEventGen
    
    val handlers = new HandlerRelay(canvas.handler, actorThread.handler, mouseEventGen.moveHandler)
    
    val grid = new GridDrawer(worldSize, Vector3D(80, 80))
    val camera = new MagnifierCamera(64)
    
    handlers += grid
    handlers += new GridNumberDrawer(grid)
    handlers += camera
    
    actorThread.start()
    frame.display()
}