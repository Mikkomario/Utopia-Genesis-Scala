package utopia.genesis.image

import java.awt.image.BufferedImage

/**
  * These filters are used for transforming image data
  * @author Mikko Hilpinen
  * @since 15.6.2019, v2.1+
  */
trait ImageFilter
{
	// ABSTRACT	-------------------
	
	/**
	  * Transforms an image
	  * @param source The source image
	  * @return The transformed image
	  */
	def apply(source: BufferedImage): BufferedImage
	
	
	// OPERATORS	---------------
	
	/**
	  * Combines two image filters
	  * @param another Another image filter
	  * @return A combination of these filters where the second filter is applied after the first one
	  */
	def +(another: ImageFilter) = AndImageFilter(this, another)
}
