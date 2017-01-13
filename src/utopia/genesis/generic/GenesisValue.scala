package utopia.genesis.generic

import utopia.genesis.util.Vector3D
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.util.Line
import utopia.genesis.util.Circle

object GenesisValue
{
    /**
     * Wraps a 3d vector into a value
     */
    def of(vector: Vector3D) = new Value(Some(vector), Vector3DType)
    /**
     * Wraps a line into a value
     */
    def of(line: Line) = new Value(Some(line), LineType)
    /**
     * Wraps a circle into a value
     */
    def of(circle: Circle) = new Value(Some(circle), CircleType)
    
    
    implicit class GValue(val v: Value) extends AnyVal
    {
        /**
         * A 3D vector value of this value. None if the value couldn't be casted.
         */
        def vector3D = v.objectValue(Vector3DType).map { _.asInstanceOf[Vector3D] }
        
        /**
         * A line value of this value. None if the value couldn't be casted.
         */
        def line = v.objectValue(LineType).map { _.asInstanceOf[Line] }
        
        /**
         * A circle value of this value. None if the value couldn't be casted.
         */
        def circle = v.objectValue(CircleType).map { _.asInstanceOf[Circle] }
        
        /**
         * The vector value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default vector value. Defaults to a zero vector.
         */
        def vector3DOr(default: Vector3D = Vector3D.zero) = vector3D.getOrElse(default)
        
        /**
         * The line value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default line value. Defaults to a line from zero to zero.
         */
        def lineOr(default: Line = Line(Vector3D.zero, Vector3D.zero)) = line.getOrElse(default)
        
        /**
         * The circle value of this value, or the provided default value in case the value couldn't
         * be cast.
         * @param default The default circle value. Defaults to a circle at zero origin with zero
         * radius.
         */
        def circleOr(default: Circle = Circle(Vector3D.zero, 0)) = circle.getOrElse(default)
    }
}