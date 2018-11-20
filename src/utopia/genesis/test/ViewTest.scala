package utopia.genesis.test

import utopia.genesis.view.MainFrame
import utopia.genesis.shape.Vector3D
import javax.swing.JPanel

object ViewTest extends App
{
    val frame = new MainFrame(new JPanel(), Vector3D(640, 480), "ViewTest")
    frame.display()
    
    println("Success")
}