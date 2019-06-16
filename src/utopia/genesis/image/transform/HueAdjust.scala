package utopia.genesis.image.transform

import utopia.genesis.color.Color
import utopia.genesis.shape.Angle

/**
  * This color transform adjusts hue for a specific range of colors, the effect is smooth based on hue distance from
  * target
  * @author Mikko Hilpinen
  * @since 16.6.2019, v2.1+
  * @param sourceHue The targeted source hue angle
  * @param effectRange The hue range (angle) of the effect (how wide range of colors are affected)
  * @param targetHue The hue new hue for the specified source hue. Adjacent hues will receive a diminished effect.
  */
case class HueAdjust(sourceHue: Angle, effectRange: Angle, targetHue: Angle) extends PixelTransform
{
	override def apply(original: Color) =
	{
		// Checks whether pixel is within effect range
		val hueDifference = (original.hueAngle - sourceHue).positive.toAngle
		
		if (hueDifference < effectRange / 2)
		{
			// Range modifier [0, 1] where 1 is exactly target hue and 0 is at the edge
			val rangeMod = 1 - (hueDifference.toRadians / (effectRange.toRadians / 2))
			// Uses a sin function for the effectiveness (based on range modifier)
			// (sin(2 * x * PI - PI) + 1) / 2
			val effectMod = (Math.sin(2 * rangeMod * Math.PI - Math.PI) + 1) / 2
			
			// Applies effect (rotation)
			val maxRotation = targetHue - sourceHue
			original + (maxRotation * effectMod)
		}
		else
			original
	}
}
