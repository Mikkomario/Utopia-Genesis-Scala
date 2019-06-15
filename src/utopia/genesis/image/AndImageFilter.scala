package utopia.genesis.image
import java.awt.image.BufferedImage

/**
  * This filter transforms an image using two separate filters
  * @author Mikko Hilpinen
  * @since 15.6.2019, v2.1+
  */
case class AndImageFilter(first: ImageFilter, second: ImageFilter) extends ImageFilter
{
	override def apply(source: BufferedImage) = second(first(source))
}
