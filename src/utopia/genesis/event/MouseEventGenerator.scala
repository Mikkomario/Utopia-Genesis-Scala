package utopia.genesis.event

import utopia.genesis.util.Vector3D

/**
 * This class is used for managing mouse event generation process. The instance records the provided 
 * mouse state and reacts to informed changes by generating new mouse events. This class does not 
 * read mouse status from any external input source and must be informed via specific methods.<br>
 * You can inform multiple instances about the events using listener handler classes.
 * @author Mikko Hilpinen
 * @since 21.1.2017
 */
class MouseEventGenerator(val moveListener: MouseMoveListener)
{
    // ATTRIBUTES    ----------------
    
    private var mousePosition = Vector3D.zero
}