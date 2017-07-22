package utopia.genesis.test

import utopia.genesis.generic.GenesisDataType
import utopia.genesis.util.Vector3D
import utopia.genesis.util.Circle
import utopia.genesis.util.Line

/**
 * This test makes sure circle and line class projection and collision algorithms are working 
 * properly
 * @author Mikko Hilpinen
 */
object CollisionTest extends App
{
    GenesisDataType.setup()
    
    val circle1 = Circle(Vector3D.zero, 2)
    val circle2 = Circle(Vector3D(3), 2)
    
    assert(circle1.projectedOver(Vector3D(1)) == Line(Vector3D(-2), Vector3D(2)))
    
    val mtv1 = circle1.collisionMtvWith(circle2)
    
    assert(mtv1.isDefined)
    assert(mtv1.get == Vector3D(-1))
    
    val collisionPoints1 = circle1.collisionPoints(mtv1.get).sortBy { _.y }
    
    assert(collisionPoints1.size == 2)
    
    val point1 = collisionPoints1(0)
    val point2 = collisionPoints1(1)
    
    println(collisionPoints1)
    
    assert(collisionPoints1.forall { _.x == 1.5 })
    
    println("Success!")
}