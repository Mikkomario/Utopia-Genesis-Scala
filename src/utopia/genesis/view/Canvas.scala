package utopia.genesis.view

import javax.swing.JPanel
import utopia.genesis.util.Vector3D
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

/**
 * A Game panel works like any Swing panel except it's able to draw drawable object contents with a
 * certain framerate. The panel also rescales itself when the size changes to display only the 
 * specified game world area.
 * @author Mikko Hilpinen
 * @since 28.12.2016
 */
class Canvas(originalGameWorldSize: Vector3D, val maxFPS: Int = 60, 
        val scalingPolicy: ScalingPolicy = PROJECT, var clearPrevious: Boolean = true) extends 
        JPanel(null) with ComponentListener
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
    def prefferedGameWorldSize_=(newSize: Vector3D) = 
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
    
    
    // COMPUTED PROPERTIES    --------
    
    private def refreshIntervalMillis = if (maxFPS <= 0) 0 else 1000.0 / maxFPS
    
    
    // IMPLEMENTED METHODS    --------
    
    override def paintComponent(g: Graphics)
    {
        super.paintComponent(g)
        
        val g2d = g.asInstanceOf[Graphics2D]
        val originalTransformation = g2d.getTransform
        
        // Clears the previous drawings
        if (clearPrevious)
        {
            g2d.clearRect(0, 0, getWidth, getHeight)
            g2d.setColor(getBackground)
            g2d.fillRect(0, 0, getWidth, getHeight)
        }
        
        // Game world drawings are scaled
        g2d.scale(scaling, scaling)
        
        g2d.setColor(Color.BLACK)
        handler.draw(new Drawer(g2d))
        
        g2d.setTransform(originalTransformation)
    }
    
    override def componentResized(event: ComponentEvent) = updateScaling()
    
    override def componentMoved(event: ComponentEvent) = Unit
    
    override def componentShown(event: ComponentEvent) = 
    {
        // Starts refreshing the panel
        if (refreshThread.isEmpty)
        {
            refreshThread = Some(new RefreshThread())
            refreshThread.get.setDaemon(true)
            refreshThread.get.start()
        }
    }
    
    override def componentHidden(event: ComponentEvent) = 
    {
        // Stops refreshing the panel
        if (refreshThread.isDefined)
        {
            refreshThread.get.end()
            refreshThread = None
        }
    }
    
    
    // OTHER METHODS    --------------
    
    def updateScaling()
    {
        val size = Vector3D of getSize()
        
        if (scalingPolicy == PROJECT)
        {
            _gameWorldSize = prefferedGameWorldSize projectedOver size
        }
        else
        {
            val prefferedXYRatio = prefferedGameWorldSize.x / prefferedGameWorldSize.y
            val newXYRatio = size.x / size.y
            
            val preserveX = if (scalingPolicy == CROP) prefferedXYRatio <= newXYRatio else 
                                                       prefferedXYRatio > newXYRatio;
            
            if (preserveX)
            {
                _gameWorldSize = prefferedGameWorldSize * Vector3D(1, size.y / size.x, 1)
            }
            else
            {
                _gameWorldSize = prefferedGameWorldSize * Vector3D(size.x / size.y, 1, 1)
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
                val nextDrawNanos = System.nanoTime() + WaitUtil.nanosOf(refreshIntervalMillis)
                repaint()
                WaitUtil.waitUntil(nextDrawNanos, this)
            }
        }
        
        
        // OTHER METHODS    --------
        
        def end() = _ended = true
    }
}