package utopia.genesis.test

import utopia.genesis.view.MainFrame
import javax.swing.JPanel
import utopia.genesis.shape.shape2D.Size

object ViewTest extends App
{
    val frame = new MainFrame(new JPanel(), Size(640, 480), "ViewTest")
    frame.display()
    
    println("Success")
}