package utopia.genesis.test

import utopia.genesis.util.Vector3D

import utopia.genesis.util.Extensions._

object VectorTest extends App
{
    val v1 = Vector3D(1, 1)
    
    assert(v1 == Vector3D(1, 1))
    assert(v1 * 1 == v1)
    assert(v1 * Vector3D.identity == v1)
    assert(v1 / 1 == v1)
    assert(v1 / Vector3D.identity == v1)
    assert(v1.length > 1)
    assert(v1 - v1 == Vector3D.zero)
    assert(v1.toUnit.length ~== 1)
    
    assert(v1.scalarProjection(Vector3D(1)) == 1)
    assert(v1.projectedOver(Vector3D(1)) == Vector3D(1))
    
    assert(v1 isParallelWith Vector3D(2, 2))
    assert(v1 isParallelWith Vector3D(-1, -1))
    assert(v1 isPerpendicularTo Vector3D(1, -1))
    
    assert(v1 == Vector3D(1) + Vector3D(0, 1))
    
    assert(Vector3D.lenDirRads(1, 0) == Vector3D(1))
    assert(Vector3D.lenDirDegs(1, 90) ~== Vector3D(0, 1))
    assert(Vector3D.lenDirDegs(1, 180) ~== Vector3D(-1))
    
    val v2 = Vector3D(1)
    
    assert(v2.rotatedDegs(90) ~== Vector3D(0, 1))
    
    
    assert(v1.angleDifferenceRads(Vector3D(1)).toDegrees ~== 45)
    assert((Vector3D(1) angleDifferenceRads Vector3D(0, 1)).toDegrees ~== 90)
    
    // (-1, 7, 0) x (-5, 8, 0) = (0, 0, 27)
    println(Vector3D(-1, 7) cross Vector3D(-5, 8))
    assert(Vector3D(-1, 7) cross Vector3D(-5, 8) ~== Vector3D(0, 0, 27))
    // (-1, 7, 4) x (-5, 8, 4) = (-4, -16, 27)
    println(Vector3D(-1, 7, 4) cross Vector3D(-5, 8, 4))
    assert(Vector3D(-1, 7, 4) cross Vector3D(-5, 8, 4) ~== Vector3D(-4, -16, 27))
    // (10, 0, 0) x (10, 0, -2) = (0, 20, 0)
    println(Vector3D(10, 0, 0) cross Vector3D(10, 0, -2))
    assert(Vector3D(10, 0, 0) cross Vector3D(10, 0, -2) ~== Vector3D(0, 20, 0))
    
    println("Success")
}