package utopia.genesis.test

import utopia.genesis.event.Drawable
import java.awt.Graphics2D
import utopia.genesis.util.Vector3D
import java.awt.Color
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.util.Drawer
import utopia.genesis.util.Circle
import utopia.genesis.util.ShapeConvertible
import utopia.genesis.util.Bounds

/**
 * This test tests the basic canvas drawing
 * @author Mikko Hilpinen
 * @since 29.12.2016
 */
object CanvasTest extends App
{
    private class TestDrawable(val shape: ShapeConvertible, override val depth: Int) extends Drawable
    {
        override def draw(drawer: Drawer) = drawer.withColor(Color.RED).draw(shape)
    }
    
    val gameWorldSize = Vector3D(800, 600)
    val canvas = new Canvas(gameWorldSize)
    
    canvas.handler += new TestDrawable(Circle(gameWorldSize / 2, 96), 0)
    canvas.handler += new TestDrawable(Circle(gameWorldSize / 2, 16), -100)
    canvas.handler += new TestDrawable(Circle(Vector3D.zero, gameWorldSize.x), 100)
    canvas.handler += new TestDrawable(Bounds(gameWorldSize * 0.2, gameWorldSize * 0.6), 50)
    
    val frame = new MainFrame(canvas, gameWorldSize, "CanvastTest")
    frame.display()
}