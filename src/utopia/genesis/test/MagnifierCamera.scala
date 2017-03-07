package utopia.genesis.test

import utopia.genesis.event.Camera
import utopia.genesis.util.Vector3D
import utopia.genesis.util.Circle
import utopia.genesis.util.Transformation
import utopia.genesis.util.DepthRange
import utopia.genesis.event.Drawable
import utopia.genesis.util.Drawer
import utopia.genesis.event.MouseMoveListener
import utopia.genesis.event.MouseMoveEvent

/**
 * This camera magnifies the area under the mouse cursor
 * @author Mikko Hilpinen
 * @since 26.2.2017
 */
class MagnifierCamera(radius: Double) extends Camera with Drawable with MouseMoveListener
{
    // ATTRIBUTES    ------------------
    
    override val depth = DepthRange.top
    override val projectionArea = Circle(Vector3D.zero, radius).toShape
    
    private var absoluteTransform = Transformation.scaling(2)
    private var viewTransform = Transformation.scaling(0.75)
    
    
    // IMPLEMENTED PROPERTIES    -----
    
    override def projectionTransformation = absoluteTransform
    override def viewTransformation = viewTransform
    
    
    // IMPLEMENTED METHODS    --------
    
    // Draws the edges of the projection area
    override def draw(drawer: Drawer) = drawer.withColor(None).transformed(
            projectionTransformation).draw(projectionArea)
    
    // Moves the camera 'lens' to the mouse position
    override def onMouseMove(event: MouseMoveEvent) = 
    {
        absoluteTransform = absoluteTransform.withPosition(event.mousePosition)
        viewTransform = viewTransform.withPosition(event.mousePosition)
    }
}