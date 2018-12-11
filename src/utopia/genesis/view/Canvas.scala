package utopia.genesis.view

import utopia.flow.util.TimeExtensions._

import javax.swing.JPanel
import utopia.genesis.shape.Vector3D
import utopia.genesis.view.ScalingPolicy.PROJECT
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ComponentListener
import java.awt.event.ComponentEvent
import utopia.genesis.view.ScalingPolicy.CROP
import utopia.genesis.util.WaitUtil
import utopia.genesis.event.DrawableHandler
import utopia.genesis.util.Drawer
import java.awt.event.HierarchyListener
import java.awt.event.HierarchyEvent
import utopia.genesis.shape.shape2D.Transformation
import utopia.genesis.shape.shape2D.Size
import java.time.Duration
import java.time.Instant
import utopia.flow.util.WaitUtils

/**
 * A Game panel works like any Swing panel except it's able to draw drawable object contents with a
 * certain framerate. The panel also rescales itself when the size changes to display only the 
 * specified game world area.
 * @author Mikko Hilpinen
 * @since 28.12.2016
 */
class Canvas(originalGameWorldSize: Size, val maxFPS: Int = 60, 
        val scalingPolicy: ScalingPolicy = PROJECT, var clearPrevious: Boolean = true) extends 
        JPanel(null) with HierarchyListener with ComponentListener
{
    // ATTRIBUTES    -----------------
    
    /**
     * The handler that keeps track of all the elements drawn by this panel
     */
    val handler = new DrawableHandler()
    
    private var _gameWorldSize = originalGameWorldSize
    def gameWorldSize = _gameWorldSize
    
    private var _prefferedGameWorldSize = originalGameWorldSize
    def prefferedGameWorldSize = _prefferedGameWorldSize
    def prefferedGameWorldSize_=(newSize: Size) = 
    {
        _prefferedGameWorldSize = newSize
        updateScaling()
    }
    
    private var _scaling = 1.0
    def scaling = _scaling
    
    private var refreshThread: Option[RefreshThread] = None
    
    
    // INITIAL CODE    ---------------
    
    setSize(originalGameWorldSize.toDimension)
    setBackground(Color.WHITE)
    addComponentListener(this)
    addHierarchyListener(this)
    
    
    // COMPUTED PROPERTIES    --------
    
    private def refreshInterval = if (maxFPS > 0) Some(Duration.ofMillis((1000.0 / maxFPS).toLong)) else None
    
    
    // IMPLEMENTED METHODS    --------
    
    override def paintComponent(g: Graphics)
    {
        super.paintComponent(g)

        val drawer = new Drawer(g.create().asInstanceOf[Graphics2D])
        
        // Clears the previous drawings
        if (clearPrevious)
        {
            val copy = drawer.copy()
            
            copy.graphics.clearRect(0, 0, getWidth, getHeight)
            copy.graphics.setColor(getBackground)
            copy.graphics.fillRect(0, 0, getWidth, getHeight)
            
            copy.dispose()
        }
        
        // Game world drawings are scaled, then drawn
        handler.draw(drawer.transformed(Transformation.scaling(scaling)))
        
        // Disposes the created drawers afterwards
        drawer.dispose()
    }
    
    override def hierarchyChanged(event: HierarchyEvent) = 
    {
        if (isShowing())
        {
            // Starts refreshing the panel
            if (refreshThread.isEmpty)
            {
                refreshThread = Some(new RefreshThread())
                refreshThread.get.setDaemon(true)
                refreshThread.get.start()
            }
        }
        else
        {
            // Stops refreshing the panel
            if (refreshThread.isDefined)
            {
                refreshThread.get.end()
                refreshThread = None
            }
        }
    }
    
    // Updates scaling whenever the component's size changes
    override def componentResized(event: ComponentEvent) = updateScaling()
    
    override def componentMoved(event: ComponentEvent) = Unit
    
    override def componentShown(event: ComponentEvent) = Unit
    
    override def componentHidden(event: ComponentEvent) = Unit
    
    
    // OTHER METHODS    --------------
    
    def updateScaling()
    {
        val size = Size of getSize()
        
        if (scalingPolicy == PROJECT)
        {
            _gameWorldSize = (prefferedGameWorldSize.toVector projectedOver size.toVector).toSize
        }
        else
        {
            val prefferedXYRatio = prefferedGameWorldSize.width / prefferedGameWorldSize.height
            val newXYRatio = size.width / size.height
            
            val preserveX = if (scalingPolicy == CROP) prefferedXYRatio <= newXYRatio else 
                                                       prefferedXYRatio > newXYRatio;
            
            if (preserveX)
            {
                _gameWorldSize = prefferedGameWorldSize * Vector3D(1, size.height / size.width, 1)
            }
            else
            {
                _gameWorldSize = prefferedGameWorldSize * Vector3D(size.width / size.height, 1, 1)
            }
        }
        
        _scaling = (size / gameWorldSize).x
    }
    
    
    // NESTED CLASSES    ------------
    
    private class RefreshThread extends Thread
    {
        // ATTRIBUTES    ------------
        
        private var _ended = false
        def ended = _ended
        
        
        // IMPLEMENTED METHODS    ---
        
        override def run()
        {
            while (!ended)
            {
                val nextDrawTime = refreshInterval.map(Instant.now() + _)
                repaint()
                nextDrawTime.foreach(WaitUtils.waitUntil(_, this))
            }
        }
        
        
        // OTHER METHODS    --------
        
        def end() = _ended = true
    }
}