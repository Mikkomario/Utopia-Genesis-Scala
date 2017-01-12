package utopia.genesis.util

import java.awt.Graphics2D
import java.awt.AlphaComposite
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.Vector3DType
import utopia.genesis.generic.LineType
import utopia.genesis.generic.CircleType

/**
 * This object contains some implicit extensions introduced in Genesis
 * @author Mikko Hilpinen
 * @since 24.12.2016
 */
object Extensions
{
    implicit class DoubleWithAlmostEquals(val d: Double) extends AnyVal
    {
        def ~==(d2: Double) = (d -d2).abs < 0.00001
    }
    
    // TODO: Implement Value of -methods too
    implicit class GenesisValue(val v: Value) extends AnyVal
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
    
    implicit class ExtendedGraphics(val g: Graphics2D) extends AnyVal
    {
        /**
         * Changes the drawing opacity / alpha for the future drawing
         * @param alpha The alpha value [0, 1]
         */
        def setAlpha(alpha: Double) = g.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha.toFloat))
    }
}