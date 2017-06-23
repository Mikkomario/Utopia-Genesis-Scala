package utopia.genesis.generic

import utopia.flow.generic.ValueCaster
import scala.collection.immutable.HashSet
import utopia.flow.datastructure.immutable.Value
import utopia.flow.generic.DataType
import utopia.flow.generic.Conversion

import utopia.genesis.util.Extensions._
import utopia.genesis.generic.GenesisValue._
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Constant
import utopia.genesis.util.Vector3D
import utopia.flow.generic.VectorType
import utopia.flow.generic.ModelType
import utopia.genesis.util.Line
import utopia.genesis.util.Circle
import utopia.flow.generic.ConversionReliability._
import utopia.genesis.util.Transformation
import utopia.genesis.util.Bounds

import utopia.flow.generic.ValueConversions._

/**
 * This object handles casting of Genesis-specific data types
 * @author Mikko Hilpinen
 * @since 12.1.2017
 */
object GenesisValueCaster extends ValueCaster
{
    // ATTRIBUTES    --------------
    
    override lazy val conversions = HashSet[Conversion](
            Conversion(Vector3DType, VectorType, PERFECT), 
            Conversion(LineType, VectorType, PERFECT), 
            Conversion(Vector3DType, ModelType, PERFECT), 
            Conversion(LineType, ModelType, PERFECT), 
            Conversion(CircleType, ModelType, PERFECT), 
            Conversion(BoundsType, ModelType, PERFECT), 
            Conversion(TransformationType, ModelType, PERFECT), 
            Conversion(VectorType, Vector3DType, MEANING_LOSS), 
            Conversion(ModelType, Vector3DType, MEANING_LOSS), 
            Conversion(LineType, Vector3DType, DATA_LOSS), 
            Conversion(BoundsType, LineType, DATA_LOSS), 
            Conversion(VectorType, LineType, MEANING_LOSS), 
            Conversion(ModelType, LineType, MEANING_LOSS), 
            Conversion(ModelType, CircleType, MEANING_LOSS), 
            Conversion(LineType, BoundsType, DATA_LOSS), 
            Conversion(ModelType, BoundsType, MEANING_LOSS), 
            Conversion(ModelType, TransformationType, MEANING_LOSS))
    
    
    // IMPLEMENTED METHODS    -----
    
    override def cast(value: Value, toType: DataType) = 
    {
        val newContent = toType match 
        {
            case VectorType => vectorOf(value)
            case ModelType => modelOf(value)
            case Vector3DType => vector3DOf(value)
            case LineType => lineOf(value)
            case CircleType => circleOf(value)
            case BoundsType => boundsOf(value)
            case TransformationType => transformationOf(value)
            case _ => None
        }
        
        newContent.map { content => new Value(Some(content), toType) }
    }
    
    
    // OTHER METHODS    -----------
    
    private def vectorOf(value: Value): Option[Vector[Value]] = 
    {
        value.dataType match 
        {
            case Vector3DType => Some(value.vector3DOr().toVector.map { x => 
                    if (x ~== 0) 0.0.toValue else x.toValue });
            case LineType => 
            {
                val line = value.lineOr()
                Some(Vector(line.start, line.end))
            }
            case _ => None
        }
    }
    
    // TODO: Create a modelConvertible trait for this kind of operations?
    private def modelOf(value: Value): Option[Model[Constant]] = 
    {
        value.dataType match 
        {
            case Vector3DType => 
            {
                val vector = value.vector3DOr()
                Some(Model(Vector(
                        ("x", vector.x), 
                        ("y", vector.y), 
                        ("z", vector.z))))
            }
            case LineType => 
            {
                val line = value.lineOr()
                Some(Model(Vector(
                        ("start", line.start), 
                        ("end", line.end))))
            }
            case CircleType => 
            {
                val circle = value.circleOr()
                Some(Model(Vector(
                        ("origin", circle.origin), 
                        ("radius", circle.radius))))
            }
            case BoundsType => 
            {
                val rect = value.boundsOr()
                Some(Model(Vector(
                        ("position", rect.position), 
                        ("size", rect.size))))
            }
            case TransformationType => 
            {
                val t = value.transformationOr()
                Some(Model(Vector(
                        ("position", t.position), 
                        ("scaling", t.scaling), 
                        ("rotation", t.rotationRads), 
                        ("shear", t.shear))))
            }
            case _ => None
        }
    }
    
    private def vector3DOf(value: Value): Option[Vector3D] = 
    {
        value.dataType match 
        {
            case VectorType => Some(Vector3D(value(0).doubleOr(), value(1).doubleOr(), value(2).doubleOr()))
            case ModelType => Some(Vector3D(value("x").doubleOr(), value("y").doubleOr(), value("z").doubleOr()))
            case LineType => Some(value.lineOr().vector)
            case _ => None
        }
    }
    
    private def lineOf(value: Value): Option[Line] = 
    {
        value.dataType match 
        {
            case BoundsType => Some(value.boundsOr().diagonal)
            case VectorType => Some(Line(value(0).vector3DOr(), value(1).vector3DOr()))
            case ModelType => Some(Line(value("start").vector3DOr(), value("end").vector3DOr()))
            case _ => None
        }
    }
    
    private def circleOf(value: Value): Option[Circle] = 
    {
        if (value.dataType isOfType ModelType)
        {
            Some(Circle(value("origin").vector3DOr(), value("radius").doubleOr()))
        }
        else
        {
            None
        }
    }
    
    private def boundsOf(value: Value): Option[Bounds] = 
    {
        value.dataType match 
        {
            case LineType => Some(Bounds.aroundDiagonal(value.lineOr()))
            case ModelType => Some(Bounds(value("position").vector3DOr(), value("size").vector3DOr()))
            case _ => None
        }
    }
    
    private def transformationOf(value: Value): Option[Transformation] = 
    {
        if (value.dataType isOfType ModelType)
        {
            Some(Transformation(
                    value("position").vector3DOr(), 
                    value("scaling").vector3DOr(Vector3D.identity), 
                    value("rotation").doubleOr(), 
                    value("shear").vector3DOr()))
        }
        else
        {
            None
        }
    }
}