package utopia.genesis.test

import utopia.genesis.shape.{Vector3D, X, Y}
import utopia.genesis.event.KeyStateEvent
import java.awt.event.KeyEvent

import utopia.genesis.util.Drawer
import utopia.genesis.shape.shape2D.{Bounds, Line, Point, Size}
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import java.awt.Color

import utopia.genesis.event.KeyTypedEvent
import utopia.genesis.handling.{Drawable, KeyStateListener, KeyTypedListener}
import utopia.inception.handling.immutable.Handleable

/**
 * This is an interactive test for keyboard interactions. The square character should move according
 * to singular key presses (one square per press). Any characters typed on the keyboard should
 * appear on the console.
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
object KeyTest extends App
{
    class TestObject(startPosition: Point) extends KeyStateListener with Handleable
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
                case _ => 
            }
        }
    }
    
    class View(private val testObj: TestObject, private val gameWorldSize: Size,
			   private val squareSide: Int) extends Drawable
    {
        // ATTRIBUTES    -----------------
        
        private val gridSquares = Size(gameWorldSize.width.toInt / squareSide,
                gameWorldSize.height.toInt / squareSide)
        private val gridSize = gridSquares * squareSide
        private val gridPosition = ((gameWorldSize - gridSize) / 2).toPoint
		
        private val squareSize = Size(squareSide, squareSide)
        private val avatarSize = squareSize * 0.8
        
        def draw(drawer: Drawer) = 
        {
            // Draws the grid first
            for (x <- 0 to gridSquares.width.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + X(squareSide * x), gridSize.toVector.yProjection))
            }
            for (y <- 0 to gridSquares.height.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + Y(squareSide * y), gridSize.toVector.xProjection));
            }
            
            // Then draws the object
            drawer.withColor(Some(Color.LIGHT_GRAY), Some(Color.DARK_GRAY)).draw(
                    Bounds(gridPosition + testObj.position * squareSide + 
                    (squareSize - avatarSize) / 2, avatarSize).toRoundedRectangle(0.5));
        }
    }
    
    class KeyTypePrinter extends KeyTypedListener
    {
        override def onKeyTyped(event: KeyTypedEvent) = print(event.typedChar)
    }
    
    
    val gameWorldSize = Vector3D(800, 600)
    
    val canvas = new Canvas(gameWorldSize, 120)
    val frame = new MainFrame(canvas, gameWorldSize, "KeyTest")
    
    val keyEventGen = new WindowKeyEventGenerator(frame)
    
    val handlers = new HandlerRelay(canvas.handler, keyEventGen.keyStateHandler, 
            keyEventGen.keyTypedHandler);
    
    val testObj = new TestObject(Vector3D(3, 2))
    val view = new View(testObj, gameWorldSize, 48)
    
    handlers ++= (testObj, view, new KeyTypePrinter())
    
    frame.display()
}