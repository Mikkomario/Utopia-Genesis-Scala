package utopia.genesis.test

import utopia.genesis.view.GameFrame
import utopia.genesis.util.Vector3D

object ViewTest extends App
{
    val frame = new GameFrame(Vector3D(640, 480), "ViewTest")
    
    println("Success")
}