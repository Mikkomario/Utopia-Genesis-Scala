package utopia.genesis.test

import java.awt.Graphics2D

import utopia.genesis.shape.Vector3D
import java.awt.Color

import utopia.genesis.handling.Drawable
import utopia.genesis.view.Canvas
import utopia.genesis.view.MainFrame
import utopia.genesis.util.Drawer
import utopia.genesis.shape.shape2D.Circle
import utopia.genesis.shape.shape2D.ShapeConvertible
import utopia.genesis.shape.shape2D.Bounds

/**
 * This test tests the basic canvas drawing
 * @author Mikko Hilpinen
 * @since 29.12.2016
 */
object CanvasTest extends App
{
    /* TODO: Return and fix code after refactoring is done
    private class TestDrawable(val shape: ShapeConvertible, override val depth: Int) extends Drawable
    {
        override def draw(drawer: Drawer) = drawer.withColor(Some(Color.RED)).draw(shape)
    }
    
    val gameWorldSize = Vector3D(800, 600)
    val canvas = new Canvas(gameWorldSize)
    
    canvas.handler += new TestDrawable(Circle(gameWorldSize / 2, 96), 0)
    canvas.handler += new TestDrawable(Circle(gameWorldSize / 2, 16), -100)
    canvas.handler += new TestDrawable(Circle(Vector3D.zero, gameWorldSize.x), 100)
    canvas.handler += new TestDrawable(Bounds(gameWorldSize * 0.2, gameWorldSize * 0.6), 50)
    
    val frame = new MainFrame(canvas, gameWorldSize, "CanvastTest")
    frame.display()*/
}