package utopia.genesis.generic

import utopia.genesis.util.Vector3D
import utopia.flow.generic.DataType
import utopia.genesis.util.Line
import utopia.genesis.util.Circle
import utopia.flow.generic.AnyType
import utopia.flow.generic.EnvironmentNotSetupException
import utopia.flow.generic.ConversionHandler

/**
 * Vectors are used for representing motion, force and coordinates
 */
object Vector3DType extends DataType("Vector3D", classOf[Vector3D]) with GenesisDataType
/**
 * Lines are geometric 3D shapes that have a start and an end point
 */
object LineType extends DataType("Line", classOf[Line]) with GenesisDataType
/**
 * Circles are geometric shapes that have an origin and a radius
 */
object CircleType extends DataType("Circle", classOf[Circle]) with GenesisDataType

/**
 * This class is used for introducing and managing Genesis-specific data types
 * @author Mikko Hilpinen
 * @since 10.1.2017 
 */
object GenesisDataType
{
    private var isSetup = false
    
    /**
     * Sets up the Genesis-specific data type features, as well as the Flow data type features. 
     * This method should be called before using any of the data types introduced in this project.
     */
    def setup() = 
    {
        isSetup = true
        
        DataType.setup()
        DataType.introduceTypes(Vector3DType, LineType, CircleType)
        ConversionHandler.addCaster(GenesisValueCaster)
    }
}

sealed trait GenesisDataType
{
    if (!GenesisDataType.isSetup)
    {
        throw new EnvironmentNotSetupException(
                "GenesisDataType.setup() must be called before using this data type.")
    }
}