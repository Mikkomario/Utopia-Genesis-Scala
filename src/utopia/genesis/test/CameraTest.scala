package utopia.genesis.test

import utopia.genesis.shape.Vector3D
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import java.awt.Font

import utopia.genesis.util.Drawer
import utopia.genesis.shape.shape2D.Bounds
import utopia.genesis.view.CanvasMouseEventGenerator
import utopia.genesis.handling.Drawable


object CameraTest extends App
{
    /* TODO: Return and fix code after refactoring is done
    class GridNumberDrawer(private val grid: GridDrawer) extends Drawable
    {
        private val font = new Font("Arial", 0, 14)
        
        override def draw(drawer: Drawer) = 
        {
            // Draws a number on each grid square
            for (x <- 0 until grid.squareAmounts.x.toInt; y <- 0 until grid.squareAmounts.y.toInt)
            {
                drawer.drawTextCentered((y * grid.squareAmounts.x.toInt + x + 1) + "", font, 
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
    val numbers = new GridNumberDrawer(grid)
    val camera = new MagnifierCamera(64)
    
    handlers += grid
    handlers += numbers
    handlers += camera
    handlers += camera.drawHandler
    
    camera.drawHandler += grid
    camera.drawHandler += numbers
    
    actorThread.start()
    frame.display()*/
}