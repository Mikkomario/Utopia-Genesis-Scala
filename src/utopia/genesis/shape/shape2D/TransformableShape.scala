package utopia.genesis.shape.shape2D

/**
 * Transformations can be applied to transformable shapes / elements
 * @tparam T The type of object that results from a transformation
 * @author Mikko Hilpinen
 * @since 9.7.2017
 */
trait TransformableShape[T]
{
    /**
     * Transforms this instance with the specified transformation
     */
    def transformedWith(transformation: Transformation): T
}