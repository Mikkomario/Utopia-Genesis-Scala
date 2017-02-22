package utopia.genesis.test

import utopia.genesis.util.Vector3D
import utopia.genesis.event.KeyStateListener
import utopia.genesis.event.KeyStateEvent
import java.awt.event.KeyEvent
import utopia.genesis.event.Drawable
import utopia.genesis.util.Drawer
import utopia.genesis.util.Line
import utopia.genesis.util.Bounds
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.view.WindowKeyEventGenerator
import utopia.inception.handling.HandlerRelay
import java.awt.Color

/**
 * This is an interactive test for keyboard interactions
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
object KeyTest extends App
{
    class TestObject(startPosition: Vector3D) extends KeyStateListener
    {
        // ATTRIBUTES    -----------------
        
        private var _position = startPosition
        def position = _position
        
        // Is only interested in key presses
        override val keyStateEventFilter = KeyStateEvent.wasPressedFilter
        
        
        // IMPLEMENTED METHODS    --------
        
        override def onKeyState(event: KeyStateEvent) = 
        {
            event.index match
            {
                case KeyEvent.VK_UP => _position += Vector3D(0, -1)
                case KeyEvent.VK_RIGHT => _position += Vector3D(1)
                case KeyEvent.VK_DOWN => _position += Vector3D(0, 1)
                case KeyEvent.VK_LEFT => _position += Vector3D(-1)
            }
        }
    }
    
    class View(private val testObj: TestObject, private val gameWorldSize: Vector3D, 
            private val squareSide: Int) extends Drawable
    {
        // ATTRIBUTES    -----------------
        
        private val gridSquares = Vector3D(gameWorldSize.x.toInt / squareSide, 
                gameWorldSize.y.toInt / squareSide);
        private val gridSize = gridSquares * squareSide
        private val gridPosition = (gameWorldSize - gridSize) / 2
        
        private val squareSize = Vector3D(squareSide, squareSide)
        private val avatarSize = squareSize * 0.8
        
        def draw(drawer: Drawer) = 
        {
            // Draws the grid first
            for (x <- 0 to gridSquares.x.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + (Vector3D(squareSide) * x), gridSize.yProjection))
            }
            for (y <- 0 to gridSquares.y.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + (Vector3D(0, squareSide) * y), gridSize.xProjection))
            }
            
            // Then draws the object
            drawer.fillColor = Color.LIGHT_GRAY
            drawer.draw(Bounds(gridPosition + testObj.position * squareSide + 
                    (squareSize - avatarSize) / 2, avatarSize).toRoundedRectangle(0.2));
        }
    }
    
    /*
    class EventLogger extends KeyStateListener
    {
        override def onKeyState(event: KeyStateEvent) = println(s"${event.index} => ${event.isDown}")
    }*/
    
    
    val gameWorldSize = Vector3D(800, 600)
    
    val canvas = new Canvas(gameWorldSize, 120)
    val frame = new MainFrame(canvas, gameWorldSize, "KeyTest")
    
    val keyEventGen = new WindowKeyEventGenerator(frame)
    
    val handlers = new HandlerRelay(canvas.handler, keyEventGen.keyStateHandler)
    
    val testObj = new TestObject(Vector3D(3, 2))
    val view = new View(testObj, gameWorldSize, 48)
    
    handlers ++= (testObj, view/*, new EventLogger()*/)
    
    frame.display()
}