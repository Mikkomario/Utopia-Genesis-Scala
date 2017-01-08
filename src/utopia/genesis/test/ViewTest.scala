package utopia.genesis.test

import utopia.genesis.view.GameFrame
import utopia.genesis.util.Vector3D
import javax.swing.JPanel

object ViewTest extends App
{
    val frame = new GameFrame(new JPanel(), Vector3D(640, 480), "ViewTest")
    frame.display()
    
    println("Success")
}