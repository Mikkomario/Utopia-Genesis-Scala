package utopia.genesis.test

import utopia.genesis.shape.{X, Y}
import utopia.genesis.event.KeyStateEvent
import java.awt.event.KeyEvent

import utopia.genesis.util.{Drawer, FPS}
import utopia.genesis.shape.shape2D.{Bounds, Line, Point, Size}
import utopia.genesis.view.{Canvas, ConvertingKeyListener, MainFrame}
import java.awt.Color

import utopia.flow.async.ThreadPool
import utopia.genesis.event.KeyTypedEvent
import utopia.genesis.handling.mutable.{DrawableHandler, KeyStateHandler, KeyTypedHandler}
import utopia.genesis.handling.{Drawable, KeyStateListener, KeyTypedListener}
import utopia.inception.handling.immutable.Handleable
import utopia.inception.handling.mutable.HandlerRelay

import scala.concurrent.ExecutionContext

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
                case KeyEvent.VK_UP => _position += Y(-1)
                case KeyEvent.VK_RIGHT => _position += X(1)
                case KeyEvent.VK_DOWN => _position += Y(1)
                case KeyEvent.VK_LEFT => _position += X(-1)
                case _ => 
            }
        }
    }
    
    class View(private val testObj: TestObject, private val gameWorldSize: Size,
			   private val squareSide: Int) extends Drawable with Handleable
    {
        // ATTRIBUTES    -----------------
        
        private val gridSquares = Size(gameWorldSize.width.toInt / squareSide, gameWorldSize.height.toInt / squareSide)
        private val gridSize = gridSquares * squareSide
        private val gridPosition = ((gameWorldSize - gridSize) / 2).toPoint
		
        private val squareSize = Size(squareSide, squareSide)
        private val avatarSize = squareSize * 0.8
        
        def draw(drawer: Drawer) = 
        {
            // Draws the grid first
            for (x <- 0 to gridSquares.width.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + X(squareSide * x), gridSize.yProjection))
            }
            for (y <- 0 to gridSquares.height.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + Y(squareSide * y), gridSize.xProjection))
            }
            
            // Then draws the object
            drawer.withColor(Some(Color.LIGHT_GRAY), Some(Color.DARK_GRAY)).draw(
                    Bounds(gridPosition + testObj.position * squareSide + 
                    (squareSize - avatarSize) / 2, avatarSize).toRoundedRectangle())
        }
    }
    
    class KeyTypePrinter extends KeyTypedListener with Handleable
    {
        override def onKeyTyped(event: KeyTypedEvent) = print(event.typedChar)
    }
	
	// Creates the handlers first
	val drawHandler = DrawableHandler()
	val keyStateHandler = KeyStateHandler()
	val keyTypedHandler = KeyTypedHandler()
 
	val handlers = HandlerRelay(drawHandler, keyStateHandler, keyTypedHandler)
 
	// Next creates the test objects
	val gameWorldSize = Size(800, 600)
	val testObj = new TestObject(Point(3, 2))
	val view = new View(testObj, gameWorldSize, 48)
	
	handlers ++= (testObj, view, new KeyTypePrinter())
	
	// Lastly creates the view elements and starts the program
    implicit val context: ExecutionContext = new ThreadPool("Test").executionContext
	
	val canvas = new Canvas(drawHandler, gameWorldSize)
    val frame = new MainFrame(canvas, gameWorldSize, "KeyTest")
    
    val keyEventGen = new ConvertingKeyListener(keyStateHandler, keyTypedHandler)
	frame.addKeyListener(keyEventGen)
    
    canvas.startAutoRefresh(FPS(120))
    frame.display()
}