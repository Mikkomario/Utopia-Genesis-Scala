package utopia.genesis.generic

import utopia.genesis.shape.Vector3D
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.shape.shape2D.Line
import utopia.genesis.shape.shape2D.Circle
import utopia.genesis.shape.shape2D.Size
import utopia.genesis.shape.shape2D.Bounds
import utopia.genesis.shape.shape2D.Transformation
import utopia.genesis.shape.shape2D.Point

object GenesisValue
{
    /**
     * Wraps a 3d vector into a value
     */
    @deprecated("Replaced with the ValueConvertible trait. Use Vector3D#toValue instead", "v1.1")
    def of(vector: Vector3D) = new Value(Some(vector), Vector3DType)
    /**
     * Wraps a line into a value
     */
    @deprecated("Replaced with the ValueConvertible trait. Use Line#toValue instead", "v1.1")
    def of(line: Line) = new Value(Some(line), LineType)
    /**
     * Wraps a circle into a value
     */
    @deprecated("Replaced with the ValueConvertible trait. Use Circle#toValue instead", "v1.1")
    def of(circle: Circle) = new Value(Some(circle), CircleType)
    /**
     * Wraps a set of bounds into a value
     */
    @deprecated("Replaced with the ValueConvertible trait. Use Bounds#toValue instead", "v1.1")
    def of(rect: Bounds) = new Value(Some(rect), BoundsType)
    /**
     * Wraps a transformation into a value
     */
    @deprecated("Replaced with the ValueConvertible trait. Use Transformation#toValue instead", "v1.1")
    def of(transformation: Transformation) = new Value(Some(transformation), TransformationType)
    
    
    implicit class GValue(val v: Value) extends AnyVal
    {
        /**
         * A 3D vector value of this value. None if the value couldn't be casted.
         */
        def vector3D = v.objectValue(Vector3DType).map { _.asInstanceOf[Vector3D] }
        
        /**
         * A 2D point value of this value. None if this value couldn't be casted
         */
        def point = v.objectValue(PointType).map { _.asInstanceOf[Point] }
        
        /**
         * A line value of this value. None if the value couldn't be casted.
         */
        def line = v.objectValue(LineType).map { _.asInstanceOf[Line] }
        
        /**
         * A size value of this value. None if this value couldn't be casted
         */
        def size = v.objectValue(SizeType).map { _.asInstanceOf[Size] }
        
        /**
         * A circle value of this value. None if the value couldn't be casted.
         */
        def circle = v.objectValue(CircleType).map { _.asInstanceOf[Circle] }
        
        /**
         * A rectangle value of this value. None if the value couldn't be casted.
         */
        def bounds = v.objectValue(BoundsType).map { _.asInstanceOf[Bounds] }
        
        /**
         * A transformation value of this value. None if the value couldn't be casted.
         */
        def transformation = v.objectValue(TransformationType).map { _.asInstanceOf[Transformation] }
        
        /**
         * The vector value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default vector value. Defaults to a zero vector.
         */
        def vector3DOr(default: => Vector3D = Vector3D.zero) = vector3D.getOrElse(default)
        
        /**
         * The point value of this value, or the provided default value in case casting failed
         */
        def pointOr(default: => Point = Point.origin) = point.getOrElse(default)
        
        /**
         * The line value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default line value. Defaults to a line from zero to zero.
         */
        def lineOr(default: => Line = Line(Point.origin, Point.origin)) = line.getOrElse(default)
        
        /**
         * The size value of this value, or the provided default value if casting failed
         */
        def sizeOr(default: => Size = Size.zero) = size.getOrElse(default)
        
        /**
         * The circle value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default circle value. Defaults to a circle at zero origin with zero
         * radius.
         */
        def circleOr(default: => Circle = Circle(Point.origin, 0)) = circle.getOrElse(default)
        
        /**
         * The rectangle value of this value, or the provided default value in case the value
         * couldn't be cast.
         * @param default the default rectangle value. Defaults to rectangle with zero position and
         * size.
         */
        def boundsOr(default: => Bounds = Bounds.zero) = bounds.getOrElse(default)
        
        /**
         * The transformation value of this value, or the provided default value in case the value
         * couldn't be cast.
         * @param default The default transformation value. Defaults to identity transformation,
         * which doesn't modify an object's state
         */
        def transformationOr(default: => Transformation = Transformation.identity) = 
                transformation.getOrElse(default)
    }
}