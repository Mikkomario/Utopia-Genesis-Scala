package utopia.genesis.test

import utopia.genesis.generic.GenesisDataType
import utopia.genesis.shape.Vector3D
import utopia.genesis.shape.shape2D.Circle
import utopia.genesis.util.Extensions._
import utopia.genesis.shape.shape2D.Line

/**
 * This test makes sure circle and line class projection and collision algorithms are working 
 * properly
 * @author Mikko Hilpinen
 */
object CollisionTest extends App
{
    /* TODO: Return and fix code after refactoring is done
    GenesisDataType.setup()
    
    val circle1 = Circle(Vector3D.zero, 2)
    val circle2 = Circle(Vector3D(3), 2)
    
    assert(circle1.projectedOver(Vector3D(1)) == Line(Vector3D(-2), Vector3D(2)))
    
    val mtv1 = circle1.collisionMtvWith(circle2)
    
    assert(mtv1.isDefined)
    assert(mtv1.get == Vector3D(-1))
    
    val collisionPoints1 = circle1.circleIntersection(circle2).sortBy { _.y }
    
    assert(collisionPoints1.size == 2)
    
    val point1 = collisionPoints1(0)
    val point2 = collisionPoints1(1)
    
    println(collisionPoints1)
    
    assert(collisionPoints1.forall { _.x == 1.5 })
    assert(point1.y < 0)
    assert(point1.y > -3)
    assert(point2.y > 0)
    assert(point2.y < 3)
    
    val line1 = Line(Vector3D(1.5, -3), Vector3D(1.5, 3))
    
    assert(line1.projectedOver(Vector3D(0, 1)) == Line(Vector3D(0, -3), Vector3D(0, 3)))
    assert(line1.projectedOver(Vector3D(1)) == Line(Vector3D(1.5), Vector3D(1.5)))
    assert(line1.collisionAxes.size == 2)
    assert(line1.collisionAxes.exists { _ isParallelWith Vector3D(1) })
    
    val mtv2 = circle1.collisionMtvWith(line1, line1.collisionAxes)
    
    assert(mtv2.isDefined)
    assert(mtv2.get == Vector3D(-0.5))
    
    val collisionPoints2 = line1.circleIntersection(circle1, true).sortBy { _.y }
    
    println(collisionPoints2)
    assert(collisionPoints2 ~== collisionPoints1)
    
    println("Success!")*/
}