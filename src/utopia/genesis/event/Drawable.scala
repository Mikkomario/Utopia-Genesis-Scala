package utopia.genesis.event

import utopia.inception.handling.Handleable
import java.awt.Graphics2D

/**
 * Drawable instances can be drawn on a canvas using a graphics object and support depth sorting
 * @author Mikko Hilpinen
 * @since 28.12.2016
 */
trait Drawable extends Handleable
{
    /**
     * Draws the drawable instance using a specific graphics object. The graphics transformations 
     * should always be set back to original after drawing
     * @param g The graphics object used in the drawing
     */
    def draw(g: Graphics2D)
    
    /**
     * The drawing depth of the drawable instance. The higher the depth, the 'deeper' it will be 
     * drawn. Instances with less depth are drawn atop of those with higher depth.
     */
    def depth: Int
}