package utopia.genesis.test

import utopia.genesis.event.Drawable
import java.awt.Graphics2D
import utopia.genesis.util.Vector3D
import java.awt.Color
import utopia.genesis.view.Canvas
import utopia.genesis.view.GameFrame

/**
 * This test tests the basic canvas drawing
 * @author Mikko Hilpinen
 * @since 29.12.2016
 */
object CanvasTest extends App
{
    private class TestDrawable(val position: Vector3D, val r: Int, val depth: Int) extends Drawable
    {
        override def draw(g: Graphics2D) = 
        {
            g.setColor(Color.RED)
            g.fillOval(position.x.toInt - r, position.y.toInt -r, r*2, r*2)
            g.setColor(Color.BLACK)
            g.drawOval(position.x.toInt - r, position.y.toInt -r, r*2, r*2)
        }
    }
    
    val gameWorldSize = Vector3D(800, 600)
    val canvas = new Canvas(gameWorldSize)
    
    canvas.handler += new TestDrawable(gameWorldSize / 2, 96, 0)
    canvas.handler += new TestDrawable(gameWorldSize / 2, 16, -100)
    canvas.handler += new TestDrawable(Vector3D.zero, gameWorldSize.x.toInt, 100)
    
    val frame = new GameFrame(canvas, gameWorldSize, "CanvastTest")
    frame.display()
}