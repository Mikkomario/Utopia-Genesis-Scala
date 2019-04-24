package utopia.genesis.color

import utopia.genesis.color.RGBChannel._
import utopia.flow.util.CollectionExtensions._
import scala.collection.immutable.HashMap

object RGB
{
	private val maxValue = 255
	
	/**
	  * Creates a new RGB color
	  * @param r Red ratio [0, 1]
	  * @param g Green ratio [0, 1]
	  * @param b Blue ratio [0, 1]
	  * @return A new RGB color
	  */
	def apply(r: Double, g: Double, b: Double) = withRatios(HashMap(Red -> r, Green -> g, Blue -> b))
	
	/**
	  * Creates a single channel RGB color
	  * @param channel Color channel
	  * @param ratio Color ratio [0, 1]
	  * @return A new RGB color
	  */
	def apply(channel: RGBChannel, ratio: Double) = new RGB(HashMap(channel -> ratio))
	
	/**
	  * Creates a red color
	  * @param ratio Ratio / saturation [0, 1]
	  * @return A new RGB Color
	  */
	def red(ratio: Double) = RGB(Red, ratio)
	
	/**
	  * Creates a green color
	  * @param ratio Ratio / saturation [0, 1]
	  * @return A new RGB Color
	  */
	def green(ratio: Double) = RGB(Green, ratio)
	
	/**
	  * Creates a blue color
	  * @param ratio Ratio / saturation [0, 1]
	  * @return A new RGB Color
	  */
	def blue(ratio: Double) = RGB(Blue, ratio)
	
	/**
	  * Creates a grayscale color
	  * @param luminosity luminosity [0, 1], where 0 is black and 1 is white
	  * @return A new RGB Color
	  */
	def gray(luminosity: Double) = withRatios(RGBChannel.values.map { _ -> luminosity }.toMap)
	
	/**
	  * Creates a new RGB with specified color ratios
	  * @param ratios Ratios per channel [0, 1]
	  * @return A new RGB color
	  */
	def withRatios(ratios: Map[RGBChannel, Double]) = new RGB(ratios.mapValues(inRange).view.force)
	
	/**
	  * Creates a new RGB with color values
	  * @param values Values per channel [0, 255]
	  * @return A new RGB color
	  */
	def withValues(values: Map[RGBChannel, Int]) = withRatios(values.mapValues { _.toDouble / maxValue })
	
	/**
	  * Creates a new RGB with color values
	  * @param r Red value [0, 255]
	  * @param g Green value [0, 255]
	  * @param b Blue value [0, 255]
	  * @return A new RGB color
	  */
	def withValues(r: Int, g: Int, b: Int): RGB = withValues(HashMap(Red -> r, Green -> g, Blue -> b))
	
	private def inRange(ratio: Double) = 0.0 max ratio min 1.0
}

/**
  * An RGB represents a color with red, blue and green
  * @author Mikko Hilpinen
  * @since 24.4.2019, v1+
  */
case class RGB private(ratios: Map[RGBChannel, Double])
{
	// COMPUTED	------------------------
	
	/**
	  * @return The red component of this color [0, 1]
	  */
	def red = apply(Red)
	
	/**
	  * @return The green component of this color [0, 1]
	  */
	def green = apply(Green)
	
	/**
	  * @return The blue component of this color [0, 1]
	  */
	def blue = apply(Blue)
	
	/**
	  * @return The red value of this color [0, 255]
	  */
	def redValue = value(Red)
	
	/**
	  * @return The green value of this color [0, 255]
	  */
	def greenValue = value(Green)
	
	/**
	  * @return The blue value of this color [0, 255]
	  */
	def blueValue = value(Blue)
	
	/**
	  * @return The color values of this RGB [0, 255] each
	  */
	def values = ratios.mapValues { r => (r * RGB.maxValue).toInt }
	
	/**
	  * @return An inverted version of this RGB (where black is white)
	  */
	def inverted = new RGB(ratios.mapValues { 1 - _ })
	
	def maxRatio = ratios.values.max
	
	def minRatio = RGBChannel.values.map(apply).min
	
	/*
	def hue =
	{
		val max = maxRatio
		val min = minRatio
		
		if (max == min)
			0.0
		else if (max == red)
			((60 * (green - blue) / (max - min)) + 360) % 360
		else if (max == green)
			(60 * (blue - red) / (max - min)) + 120
		else
			(60 * (red - green) / (max - min)) + 240
	}
	
	def luminance = (maxRatio + minRatio) / 2
	
	def saturation =
	{
		val max = maxRatio
		val min = minRatio
		
		if (max == min)
			0
		else if (l <= 0.5)
			(max - min) / (max + min)
		else
			(max - min) / (2 - max - min)
	}*/
	
	
	// IMPLEMENTED	--------------------
	
	override def toString = s"R: ${percent(Red)}%, G: ${percent(Green)}%, B: ${percent(Blue)}%"
	
	
	// OPERATORS	--------------------
	
	/**
	  * Finds the ratio for a single color channel
	  * @param channel A color channel
	  * @return The color ratio for that channel [0, 1]
	  */
	def apply(channel: RGBChannel): Double = ratios.getOrElse(channel, 0)
	
	/**
	  * Combines this RGB with another by adding the color values together
	  * @param other Another RGB
	  * @return A combination of these two RGB's
	  */
	def +(other: RGB) = RGB.withRatios(ratios.mergedWith(other.ratios, _ + _))
	
	/**
	  * Combines this RGB with another by subtracting the color values
	  * @param other Another RGB
	  * @return A subtraction of these RGB's
	  */
	def -(other: RGB) = RGB.withRatios(ratios.map { case (k, v) => k -> (v - other(k)) })
	
	
	// OTHER	------------------------
	
	/**
	  * Finds a color ratio for a color channel
	  * @param channel Target color channel
	  * @return Color ratio for that channel [0, 1]
	  */
	def ratio(channel: RGBChannel) = apply(channel)
	
	/**
	  * Finds a color value for a color channel
	  * @param channel Target color channel
	  * @return Color value for that channel[0, 255]
	  */
	def value(channel: RGBChannel) = (apply(channel) * RGB.maxValue).toInt
	
	/**
	  * Finds a color saturation % for a channel
	  * @param channel Target channel
	  * @return Color saturation % [0, 100]
	  */
	def percent(channel: RGBChannel) = (apply(channel) * 100).toInt
	
	/**
	  * Creates a copy of this RGB with modified color ratio
	  * @param channel Target channel
	  * @param ratio New color ratio [0, 1]
	  * @return A new RGB
	  */
	def withRatio(channel: RGBChannel, ratio: Double) = new RGB(ratios + (channel -> RGB.inRange(ratio)))
	
	/**
	  * Creates a copy of this RGB with modified color value
	  * @param channel Target channel
	  * @param value New color value [0, 255]
	  * @return A new RGB
	  */
	def withValue(channel: RGBChannel, value: Int) = withRatio(channel, value / RGB.maxValue.toDouble)
	
	/**
	  * Creates a copy of this RGB with modified red color ratio
	  * @param ratio New color ratio [0, 1]
	  * @return A new RGB
	  */
	def withRed(ratio: Double) = withRatio(Red, ratio)
	
	/**
	  * Creates a copy of this RGB with modified green color ratio
	  * @param ratio New color ratio [0, 1]
	  * @return A new RGB
	  */
	def withGreen(ratio: Double) = withRatio(Green, ratio)
	
	/**
	  * Creates a copy of this RGB with modified blue color ratio
	  * @param ratio New color ratio [0, 1]
	  * @return A new RGB
	  */
	def withBlue(ratio: Double) = withRatio(Blue, ratio)
	
	/**
	  * @param another Another RGB
	  * @return A minimum between these two colors on each RGB channel
	  */
	def min(another: RGB) = mergeWith(another, _ min _)
	/**
	  * @param another Another RGB
	  * @return A maximum between these two colors on each RGB channel
	  */
	def max(another: RGB) = mergeWith(another, _ max _)
	/**
	  * @param another Another RGB
	  * @return An average between these two colors on each RGB channel
	  */
	def average(another: RGB) = mergeWith(another, (a, b) => (a + b) / 2)
	
	private def mergeWith(another: RGB, f: (Double, Double) => Double) = RGB.withRatios(
		RGBChannel.values.map { c => c -> f(apply(c), another(c)) }.toMap)
}
