package utopia.genesis.test

import utopia.genesis.util.Line
import utopia.genesis.util.Vector3D
import utopia.genesis.util.Circle

/**
 * This test is for some intersection methods and other shape (line, circle) specific methods
 */
object ShapeTest extends App
{
    val line1 = Line(Vector3D.zero, Vector3D(10))
    val line2 = Line(Vector3D(0, 1), Vector3D(2, -1))
    val line3 = Line(Vector3D(0, 0, 1), Vector3D(10, 0, 1))
    val line4 = Line(Vector3D(0, 0, 2), Vector3D(10, 0, -2))
    
    assert(line1(0) ~== line1.start)
    assert(line1(1) ~== line1.end)
    
    val intersection12 = line1 intersection line2
    
    assert(intersection12.isDefined)
    assert(intersection12.get ~== Vector3D(1, 0))
    
    assert(line1.intersection(line3).isEmpty)
    assert(line1.intersection(line4).isDefined)
    
    val line5 = Line(Vector3D(1, 2, 0), Vector3D(1, 1, 0))
    assert(line1.intersection(line5, false).isDefined)
    assert(line1.intersection(line5, true).isEmpty)
    assert(line5.intersection(line1, true).isEmpty)
    
    val circle1 = Circle(Vector3D(3), 2)
    
    assert(line1.circleIntersection(circle1, false).size == 2)
    assert(line2.circleIntersection(circle1).size == 1)
    assert(line5.circleIntersection(circle1).isEmpty)
    assert(line5.circleIntersection(circle1, false).size == 1)
    
    println("Success")
}