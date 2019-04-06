package utopia.genesis.test

import utopia.genesis.shape.Vector3D
import utopia.genesis.event.KeyStateEvent
import java.awt.event.KeyEvent

import utopia.genesis.util.Drawer
import utopia.genesis.shape.shape2D.Line
import utopia.genesis.shape.shape2D.Bounds
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.view.WindowKeyEventGenerator
import utopia.inception.handling.HandlerRelay
import java.awt.Color

import utopia.genesis.event.KeyTypedListener
import utopia.genesis.event.KeyTypedEvent
import utopia.genesis.handling.{Drawable, KeyStateListener}

/**
 * This is an interactive test for keyboard interactions. The square character should move according
 * to singular key presses (one square per press). Any characters typed on the keyboard should
 * appear on the console.
 * @author Mikko Hilpinen
 * @since 22.2.2017
 */
object KeyTest extends App
{
    /* TODO: Return and fix code after refactoring is done
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
                case _ => 
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
                drawer.draw(Line.ofVector(gridPosition + (Vector3D(squareSide) * x), 
                        gridSize.yProjection));
            }
            for (y <- 0 to gridSquares.y.toInt)
            {
                drawer.draw(Line.ofVector(gridPosition + (Vector3D(0, squareSide) * y), 
                        gridSize.xProjection));
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
    
    frame.display()*/
}