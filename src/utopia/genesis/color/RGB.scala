package utopia.genesis.color

import utopia.genesis.color.RGBChannel._
import scala.collection.immutable.HashMap

object RGB
{
	private val maxValue = 255
	
	def apply(r: Double, g: Double, b: Double) = withRatios(HashMap(Red -> r, Green -> g, Blue -> b))
	
	def apply(channel: RGBChannel, ratio: Double) = new RGB(HashMap(channel -> ratio))
	
	def red(ratio: Double) = RGB(Red, ratio)
	
	def green(ratio: Double) = RGB(Green, ratio)
	
	def blue(ratio: Double) = RGB(Blue, ratio)
	
	def gray(luminosity: Double) = withRatios(RGBChannel.values.map { _ -> luminosity }.toMap)
	
	def withRatios(ratios: Map[RGBChannel, Double]) = new RGB(ratios.mapValues(inRange).view.force)
	
	def withValues(values: Map[RGBChannel, Int]) = withRatios(values.mapValues { _.toDouble / maxValue })
	
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
	
	def red = apply(Red)
	
	def green = apply(Green)
	
	def blue = apply(Blue)
	
	def redValue = value(Red)
	
	def greenValue = value(Green)
	
	def blueValue = value(Blue)
	
	def values = ratios.mapValues { r => (r * RGB.maxValue).toInt }
	
	
	// IMPLEMENTED	--------------------
	
	override def toString = s"R: ${percent(Red)}%, G: ${percent(Green)}%, B: ${percent(Blue)}%"
	
	
	// OPERATORS	--------------------
	
	def apply(channel: RGBChannel): Double = ratios.getOrElse(channel, 0)
	
	
	// OTHER	------------------------
	
	def ratio(channel: RGBChannel) = apply(channel)
	
	def value(channel: RGBChannel) = (apply(channel) * RGB.maxValue).toInt
	
	def percent(channel: RGBChannel) = (apply(channel) * 100).toInt
}
