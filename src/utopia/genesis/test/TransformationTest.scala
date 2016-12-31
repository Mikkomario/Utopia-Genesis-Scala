package utopia.genesis.test

import utopia.genesis.util.Transformation
import utopia.genesis.util.Vector3D

import utopia.genesis.util.Extensions._

/**
 * This test tests the basic transformation class features
 */
object TransformationTest extends App
{
    val translation = Transformation.translation(Vector3D(10))
    val scaling = Transformation.scaling(2)
    val rotation = Transformation.rotationDegs(90)
    
    assert(rotation.rotationRads ~== Math.PI / 2)
    
    assert((-translation).position.x == -10)
    assert((-scaling).scaling.x == 0.5)
    assert((-rotation).rotationDegs ~== -90)
    
    assert(translation - translation == Transformation.identity)
    assert(scaling - scaling == Transformation.identity)
    assert(rotation - rotation == Transformation.identity)
    
    assert(scaling + Transformation.identity == scaling)
    
    val pos = Vector3D(10)
    
    assert(translation(pos).x == 20)
    assert(scaling(pos).x == 20)
    assert(rotation(pos).y == 10)
    
    assert(rotation(translation(pos)) == rotation(translation)(pos))
    
    val combo = translation + rotation + scaling
    
    assert(combo.invert(combo(pos)) == pos)
    
    val rotated = translation.absoluteRotatedDegs(90, Vector3D(20))
    assert(rotated.position ~== Vector3D(20, -10))
    assert(rotated.rotationDegs ~== 90)
    
    println("Success")
}