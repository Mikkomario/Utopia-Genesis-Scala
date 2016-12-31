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
    
    println("Success")
}