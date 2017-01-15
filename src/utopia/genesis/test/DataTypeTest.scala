package utopia.genesis.test

import utopia.genesis.generic.GenesisValue._
import utopia.genesis.util.Vector3D
import utopia.genesis.util.Line
import utopia.genesis.util.Circle
import utopia.genesis.generic.GenesisDataType
import utopia.genesis.generic.GenesisValue
import utopia.flow.generic.VectorType
import utopia.genesis.generic.Vector3DType
import utopia.flow.generic.ModelType
import utopia.genesis.generic.LineType
import utopia.genesis.generic.CircleType
import utopia.flow.datastructure.immutable.Model
import utopia.genesis.util.Transformation
import utopia.genesis.generic.TransformationType

/**
 * This is a unit test for the new data type implementations
 * @author Mikko Hilpinen
 * @since 14.1.2017
 */
object DataTypeTest extends App
{
    GenesisDataType.setup()
    
    val vector1 = Vector3D(1, 1, 1)
    val vector2 = Vector3D(3)
    val line = Line(vector1, vector2)
    val circle = Circle(vector2, 12.25)
    val transformation = Transformation(vector2, vector2, math.Pi, vector1)
    
    val v1 = GenesisValue of vector1
    val v2 = GenesisValue of vector2
    val l = GenesisValue of line 
    val c = GenesisValue of circle
    val t = GenesisValue of transformation
    
    assert(v1.vectorOr().size == 3)
    assert(v1(0).doubleOr() == 1)
    assert(v1(2).doubleOr() == 1)
    assert(v1("x").doubleOr() == 1)
    assert(v1("y").doubleOr() == 1)
    
    assert(v2(0).doubleOr() == 3)
    assert(v2(1).doubleOr() == 0)
    assert(v2("z").doubleOr() == 0)
    
    assert(l.vector3DOr() ~== vector2 - vector1)
    assert(l(0).vector3DOr() ~== vector1)
    assert(l(1).vector3DOr() ~== vector2)
    assert(l("start").vector3DOr() ~== vector1)
    assert(l("end").vector3DOr() ~== vector2)
    
    assert(c("origin") == v2)
    assert(c("radius").doubleOr() == 12.25)
    
    assert(t("position") == v2)
    assert(t("scaling") == v2)
    assert(t("rotation").doubleOr() == math.Pi)
    assert(t("shear") == v1)
    
    assert(v1.castTo(VectorType).get.castTo(Vector3DType).get == v1)
    assert(v1.castTo(ModelType).get.castTo(Vector3DType).get == v1)
    
    assert(l.castTo(VectorType).get.castTo(LineType).get == l)
    assert(l.castTo(ModelType).get.castTo(LineType).get == l)
    
    assert(c.castTo(ModelType).get.castTo(CircleType).get == c)
    
    assert(t.castTo(ModelType).get.castTo(TransformationType).get == t)
    
    val model = Model(Vector(("vector", v1), ("line", l), ("circle", c), ("transformation", t)))
    println(model.toJSON)
    
    println("Success")
}