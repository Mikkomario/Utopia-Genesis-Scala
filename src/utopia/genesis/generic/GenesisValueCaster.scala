package utopia.genesis.generic

import utopia.flow.generic.ValueCaster
import scala.collection.immutable.HashSet
import utopia.flow.datastructure.immutable.Value
import utopia.flow.generic.DataType
import utopia.flow.generic.Conversion

import utopia.genesis.util.Extensions._
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Constant

/**
 * This object handles casting of Genesis-specific data types
 * @author Mikko Hilpinen
 * @since 12.1.2017
 */
object GenesisValueCaster extends ValueCaster
{
    // ATTRIBUTES    --------------
    
    // TODO: Implement
    override lazy val conversions = HashSet[Conversion]()
    
    
    // IMPLEMENTED METHODS    -----
    
    // TODO: Implement
    override def cast(value: Value, toType: DataType) = 
    {
        Some(value)
    }
    
    
    // OTHER METHODS    -----------
    
    private def vectorOf(value: Value): Option[Vector[Value]] = 
    {
        value.dataType match
        {
            // Near 0 values are rounded when casting a vector
            case Vector3DType => Some(value.vector3DOr().toVector.map { x => 
                    if (x ~== 0) Value.of(0.0) else Value.of(x) })
            case _ => None
        }
    }
    
    private def modelOf(value: Value): Option[Model[Constant]] = 
    {
        value.dataType match 
        {
            case LineType => 
            {
                val line = value.lineOr()
                Some(Model(Vector(/*("start", Value of line.start)*/)))
            }
            case _ => None
        }
    }
}