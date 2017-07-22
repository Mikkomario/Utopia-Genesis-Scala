package utopia.genesis.util

import java.awt.Graphics2D
import java.awt.AlphaComposite
import utopia.flow.datastructure.immutable.Value
import utopia.genesis.generic.Vector3DType
import utopia.genesis.generic.LineType
import utopia.genesis.generic.CircleType
import scala.collection.mutable.ListBuffer

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
    
    implicit class SeqWithDistinctMap[T](val s: Seq[T]) extends AnyVal
    {
        def withDistinct(compare: (T, T) => Boolean) = 
        {
            val buffer = ListBuffer[T]()
            s.foreach { element => if (!buffer.exists { compare(_, element) }) buffer += element }
            buffer.toVector
        }
    }
    
    implicit class FailableIterable[T](val c: Iterable[T]) extends AnyVal
    {
        /**
         * This function maps values like a normal map function, but terminates immediately if 
         * None is returned by the transform function
         * @return The mapped collection or none if mapping failed for any element
         */
        def mapOrFail[B](f: T => Option[B]): Option[Vector[B]] = 
        {
            val iterator = c.iterator
            val buffer = Vector.newBuilder[B]
            
            while (iterator.hasNext)
            {
                val result = f(iterator.next())
                if (result.isDefined)
                {
                    buffer += result.get
                }
                else
                {
                    return None
                }
            }
            
            Some(buffer.result())
        }
    }
    
    @deprecated("Replaced with the new drawer class", "v0.3")
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