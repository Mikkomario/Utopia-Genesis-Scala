package utopia.genesis.view

import javax.swing.JFrame
import javax.swing.JPanel
import utopia.genesis.util.Vector3D
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Toolkit
import java.awt.Dimension
import scala.collection.immutable.HashMap
import java.awt.event.ComponentListener
import java.awt.event.ComponentEvent
import java.awt.Component

/**
 * This class is used for displaying game contents in a frame. The implementation supports
 * borderless window (fullscreen) and resizing while keeping the aspect ratio.
 * @author Mikko Hilpinen
 * @since 25.12.2016
 */
class MainFrame(initialContent: Component, val originalSize: Vector3D, title: String, 
        borderless: Boolean = false, val usePadding: Boolean = true) extends JFrame with ComponentListener
{
    // ATTRIBUTES    ------------
    
    private var _content = initialContent
    def content = _content
    def content_=(newContent: Component) = 
    {
        remove(_content)
        _content = newContent
        add(newContent, BorderLayout.CENTER)
        updateContentSize()
    }
    
    private var paddings: Map[String, JPanel] = HashMap()
    
    
    // INITIAL CODE    ----------
    
    { // Sets up the frame
        if (borderless) { setUndecorated(true) }
        
        setTitle(title)
        setLayout(new BorderLayout())
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        
        getContentPane().setBackground(Color.BLACK)
        setVisible(true)
        
        val insets = getInsets()
        setSize(originalSize.x.toInt + insets.left + insets.right, 
                originalSize.y.toInt + insets.top + insets.bottom);
                
        setVisible(false)
        add(initialContent, BorderLayout.CENTER)
        
        addComponentListener(this)
    }
    
    
    // IMPLEMENTED METHODS    ---
    
    override def componentResized(event: ComponentEvent) = 
    {
        if (event.getComponent == this)
        {
            updateContentSize()
        }
    }
    
    override def componentMoved(event: ComponentEvent) = Unit
    override def componentShown(event: ComponentEvent) = Unit
    override def componentHidden(event: ComponentEvent) = Unit
    
    
    // OTHER METHODS    ---------
    
    /**
     * Makes the frame fill the whole screen
     * @param showTaskBar Whether room should be left for the task bar. Defaults to false.
     */
    def setFullScreen(showTaskBar: Boolean = false) = 
    {
        var newSize = Toolkit.getDefaultToolkit.getScreenSize
        var position = Vector3D.zero
        
        if (showTaskBar)
        {
            val insets = Toolkit.getDefaultToolkit.getScreenInsets(getGraphicsConfiguration)
            newSize = new Dimension(newSize.width - insets.left - insets.right, 
                                    newSize.height - insets.top - insets.bottom)
            position = Vector3D(insets.left, insets.top)
        }
        
        setBounds(position + newSize)
    }
    
    /**
     * Displays the frame
     */
    def display() = setVisible(true)
    
    private def updateContentSize()
    {
        val insets = getInsets
        val actualSize = Vector3D(getWidth - insets.left - insets.right, 
                                 getHeight - insets.top - insets.bottom);
        
        if (usePadding)
        {
            val scaling = actualSize / originalSize
            
            // Calculates the content size and padding
            if (scaling.x > scaling.y)
            {
                val mainPanelSize = originalSize * scaling.y
                content.setSize(mainPanelSize.toDimension)
                
                val paddingSize = Vector3D((actualSize.x - mainPanelSize.x) / 2, actualSize.y)
                setPadding(paddingSize.toDimension, BorderLayout.WEST, BorderLayout.EAST)
            }
            else if (scaling.y > scaling.x)
            {
                val mainPanelSize = originalSize * scaling.x
                content.setSize(mainPanelSize.toDimension)
                
                val paddingSize = Vector3D(actualSize.x, (actualSize.y - mainPanelSize.y) / 2)
                setPadding(paddingSize.toDimension, BorderLayout.NORTH, BorderLayout.SOUTH)
            }
            else
            {
                content.setSize((originalSize * scaling).toDimension)
                if (!paddings.isEmpty) { paddings = HashMap() }
            }
        }
        else
        {
            content.setSize(actualSize.toDimension)
        }
    }
    
    private def setPadding(size: Dimension, directions: String*)
    {
        // Removes any padding that is not set
        paddings.foreach { case (dir, padding) => if (!directions.contains(dir)) remove(padding) }
        paddings = paddings.filterKeys { directions.contains(_) }
        
        // Modifies / adds the paddings
        for (direction <- directions)
        {
            val padding = paddings.getOrElse(direction, 
            { 
                val newPadding = new JPanel(null)
                add(newPadding, direction)
                paddings += direction -> newPadding
                
                newPadding.setOpaque(true)
                newPadding.setVisible(true)
                newPadding.setBackground(Color.BLACK)
                
                newPadding
            })
            
            padding.setPreferredSize(size)
            padding.setMaximumSize(size)
            padding.setMinimumSize(size)
            padding.setSize(size)
        }
    }
}