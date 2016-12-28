package utopia.genesis.util

import java.awt.Graphics2D
import java.awt.AlphaComposite

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