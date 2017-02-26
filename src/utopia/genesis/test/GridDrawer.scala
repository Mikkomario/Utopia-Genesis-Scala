package utopia.genesis.test

import utopia.genesis.util.Vector3D
import utopia.genesis.event.Drawable
import utopia.genesis.util.Drawer
import java.awt.Color
import utopia.genesis.util.Line

/**
 * This object simply draws a gird to the center of the game world
 * @author Mikko Hilpinen
 * @since 25.2.2017
 */
class GridDrawer(worldSize: Vector3D, val squareSize: Vector3D) extends Drawable
{
    // ATTRIBUTES    -----------------
    
    /**
     * How many squares there are on each axis
     */
    val squareAmounts = (worldSize / squareSize).floor
    /**
     * The size of the grid
     */
    private val size = squareAmounts * squareSize
    /**
     * The position of the top left corner of the grid
     */
    private val position = (worldSize - size) / 2
    
    
    // IMPLEMENTED METHODS    --------
    
    def draw(drawer: Drawer) = 
    {
        val copy = drawer.withEdgeColor(Some(Color.LIGHT_GRAY))
        
        for (x <- 0 to squareAmounts.x.toInt)
        {
            copy.draw(Line.ofVector(squarePosition(x, 0), size.yProjection))
        }
        for (y <- 0 to squareAmounts.y.toInt)
        {
            copy.draw(Line.ofVector(squarePosition(0, y), size.xProjection))
        }
    }
    
    
    // OTHER METHODS    -------------
    
    /**
     * The top left corner of a square in the grid
     * @param x The x-index of the square
     * @param y The y-index of the square
     */
    def squarePosition(x: Int, y: Int) = position + squareSize * Vector3D(x, y)
    
    /**
     * The center point of a square in the grid
     * @param x the x-index of the square
     * @param y the y-index of the square
     */
    def squareCenter(x: Int, y: Int) = squarePosition(x, y) + squareSize * 0.5
}