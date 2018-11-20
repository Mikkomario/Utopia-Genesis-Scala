package utopia.genesis.test

import utopia.genesis.generic.GenesisValue._
import utopia.genesis.shape.Vector3D
import utopia.genesis.shape.shape2D.Line
import utopia.genesis.shape.shape2D.Circle
import utopia.genesis.generic.GenesisDataType
import utopia.genesis.generic.GenesisValue
import utopia.flow.generic.VectorType
import utopia.genesis.generic.Vector3DType
import utopia.flow.generic.ModelType
import utopia.genesis.generic.LineType
import utopia.genesis.generic.CircleType
import utopia.flow.datastructure.immutable.Model
import utopia.genesis.generic.TransformationType
import utopia.genesis.generic.BoundsType
import utopia.flow.parse.JSONReader

/**
 * This is a unit test for the new data type implementations
 * @author Mikko Hilpinen
 * @since 14.1.2017
 */
object DataTypeTest extends App
{
    /* TODO: Return and fix code after refactoring is done
    GenesisDataType.setup()
    
    val vector1 = Vector3D(1, 1, 1)
    val vector2 = Vector3D(3)
    val line = Line(vector1, vector2)
    val circle = Circle(vector2, 12.25)
    val rectangle = Bounds(vector2, vector1)
    val transformation = Transformation(vector2, vector2, math.Pi, vector1)
    
    val v1 = vector1.toValue
    val v2 = vector2.toValue
    val l = line.toValue
    val c = circle.toValue
    val r = rectangle.toValue
    val t = transformation.toValue
    
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
    
    assert(r("position") == v2)
    assert(r("size") == v1)
    assert(r.vector3DOr() ~== v1.vector3DOr())
    
    assert(t("position") == v2)
    assert(t("scaling") == v2)
    assert(t("rotation").doubleOr() == math.Pi)
    assert(t("shear") == v1)
    
    assert(v1.castTo(VectorType).get.castTo(Vector3DType).get == v1)
    assert(v1.castTo(ModelType).get.castTo(Vector3DType).get == v1)
    
    assert(l.castTo(VectorType).get.castTo(LineType).get == l)
    assert(l.castTo(ModelType).get.castTo(LineType).get == l)
    
    assert(c.castTo(ModelType).get.castTo(CircleType).get == c)
    
    assert(r.castTo(LineType).get.castTo(BoundsType).get == r)
    assert(r.castTo(ModelType).get.castTo(BoundsType).get == r)
    
    assert(t.castTo(ModelType).get.castTo(TransformationType).get == t)
    
    val model = Model(Vector(("vector", v1), ("line", l), ("circle", c), ("rectangle", r), 
            ("transformation", t)));
    println(model.toJSON)
    
    // Tests JSON parsing
    assert(Vector3D.fromJSON(v1.vector3DOr().toJSON) == v1.vector3D)
    assert(Line.fromJSON(l.lineOr().toJSON) == l.line)
    assert(Circle.fromJSON(c.circleOr().toJSON) == c.circle)
    assert(Bounds.fromJSON(r.boundsOr().toJSON) == r.bounds)
    assert(Transformation.fromJSON(t.transformationOr().toJSON) == t.transformation)
    
    println("Success")*/
}