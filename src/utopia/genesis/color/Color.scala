package utopia.genesis.color

import scala.language.implicitConversions

object Color
{
	// ATTRIBUTES	---------------------
	
	/**
	  * A black color (0, 0, 0)
	  */
	val black = Color(0, 0, 0)
	/**
	  * A white color (1, 1, 1)
	  */
	val white = Color(1, 1, 1)
	/**
	  * A red color (1, 0, 0)
	  */
	val red = Color(1, 0, 0)
	/**
	  * A green color (0, 1, 0)
	  */
	val green = Color(0, 1, 0)
	/**
	  * A blue color (0, 0, 1)
	  */
	val blue = Color(0, 0, 1)
	/**
	  * A yellow color (1, 1, 0)
	  */
	val yellow = Color(1, 1, 0)
	/**
	  * A magenta color (1, 0, 1)
	  */
	val magenta = Color(1, 0, 1)
	/**
	  * A cyan color (0, 1, 1)
	  */
	val cyan = Color(0, 1, 1)
	
	
	// IMPLICITS	---------------------
	
	/**
	  * Implicitly converts an awt color to color
	  * @param awtColor An awt color
	  * @return A color
	  */
	implicit def fromAwt(awtColor: java.awt.Color): Color = Color(Right(RGB.withValues(awtColor.getRed,
		awtColor.getGreen, awtColor.getBlue)), awtColor.getAlpha / 255.0)
	
	
	// OPERATORS	--------------------
	
	/**
	  * Creates a new color
	  * @param r Red ratio [0, 1]
	  * @param g Green ratio [0, 1]
	  * @param b Blue ratio [0, 1]
	  * @param alpha Alpha ratio [0, 1]
	  * @return A new color
	  */
	def apply(r: Double, g: Double, b: Double, alpha: Double = 1.0): Color = Color(Right(RGB(r, g, b)), 0.0 max alpha min 1.0)
	
	
	// OTHER	-----------------------
	
	/**
	  * @param luminosity Luminosity factor [0, 1] where 0 is black and 1 is white
	  * @return A grayscale color with specified luminosity
	  */
	def gray(luminosity: Double) = apply(luminosity, luminosity, luminosity)
}

/**
  * This class represents color with either RGB or HSL
  * @author Mikko Hilpinen
  * @since 24.4.2019, v1+
  */
case class Color private(private val data: Either[HSL, RGB], alpha: Double) extends RGBLike[Color] with HSLLike[Color]
{
	// ATTRIBUTES	----------------------
	
	/**
	  * A HSL representation of this color
	  */
	lazy val hsl: HSL = data.fold(c => c, _.toHSL)
	/**
	  * An RGB representation of this color
	  */
	lazy val rgb: RGB = data.fold(_.toRGB, c => c)
	
	
	// COMPUTED	--------------------------
	
	/**
	  * @return Whether this color has transparency
	  */
	def isTransparent = alpha < 1
	
	/**
	  * @return The alpha percentage of this color [0, 100]
	  */
	def alphaPercentage = (alpha * 100).toInt
	
	/**
	  * @return An awt representation of this color
	  */
	def toAwt = new java.awt.Color(rgb.redValue, rgb.blueValue, rgb.greenValue, (alpha * 255).toInt)
	
	
	// IMPLEMENTED	----------------------
	
	override def ratios = rgb.ratios
	
	override def withRatios(newRatios: Map[RGBChannel, Double]) = withRGB(RGB.withRatios(newRatios))
	
	override def hue = hsl.hue
	
	override def saturation = hsl.saturation
	
	override def luminosity = hsl.luminosity
	
	def withHue(hue: Double) = withHSL(hsl.withHue(hue))
	
	def withSaturation(saturation: Double) = withHSL(hsl.withSaturation(saturation))
	
	def withLuminosity(luminosity: Double) = withHSL(hsl.withLuminosity(luminosity))
	
	override def toString =
	{
		val base = data.fold(_.toString, _.toString)
		if (isTransparent)
			s"$base, $alphaPercentage% Alpha"
		else
			base
	}
	
	
	// OTHER	---------------------------
	
	/**
	  * @param newAlpha New alpha value [0, 1]
	  * @return A copy of this color with specified alpha
	  */
	def withAlpha(newAlpha: Double) = copy(alpha = 0.0 max newAlpha min 1.0)
	
	/**
	  * @param mod An alpha modifier
	  * @return A copy of this color with modified alpha
	  */
	def timesAlpha(mod: Double) = withAlpha(alpha * mod)
	
	/**
	  * @param f A mapping function
	  * @return A copy of this color with mapped alpha
	  */
	def mapAlpha(f: Double => Double) = withAlpha(f(alpha))
	
	/**
	  * @param hsl A new HSL value
	  * @return A copy of this color with provided HSL value
	  */
	def withHSL(hsl: HSL) = copy(data = Left(hsl))
	
	/**
	  * @param rgb A new RGB value
	  * @return A copy of this color with provided RGB value
	  */
	def withRGB(rgb: RGB) = copy(data = Right(rgb))
	
	/**
	  * @param f A mapping function
	  * @return A copy of this color with mapped HSL
	  */
	def mapHSL(f: HSL => HSL) = withHSL(f(hsl))
	
	/**
	  * @param f a mapping function
	  * @return A copy of this color with mapped RGB
	  */
	def mapRGB(f: RGB => RGB) = withRGB(f(rgb))
}
