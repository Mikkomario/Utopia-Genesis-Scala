package utopia.genesis.event

import utopia.genesis.util.WaitUtil

/**
 * All actor related functions are called in a separate logic thread. This class offers a tool 
 * for just that.
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
class ActorThread(val minAPS: Int, val maxAPS: Int) extends Thread
{
    // ATTRIBUTES    ------------------------
    
    /**
     * Whether the thread has been forced to stop
     */
    var ended = false
    
    /**
     * The handler that is used for delivering the act events to the specific instances
     */
    val handler = new ActorHandler()
    
    /**
     * The minimum amount of milliseconds between each act method call
     */
    private val minIntervalMillis = if (maxAPS <= 0.0) 0 else 1000.0 / maxAPS
    
    /**
     * The maximum amount of milliseconds passed into the act method
     */
    private val maxIntervalMillis = if (minAPS <= 0.0) Double.MaxValue else 1000.0 / minAPS
    
    
    // IMPLEMENTED METHODS    ---------------
    
    override def run()
    {
        var lastActNanos = System.nanoTime()
        
        while (!ended)
        {
            val thisActNanos = System.nanoTime()
            val nextActNanos = thisActNanos + WaitUtil.nanosOf(minIntervalMillis)
            
            val millisPassed = WaitUtil.millisOf(thisActNanos - lastActNanos) min maxIntervalMillis
            lastActNanos = thisActNanos
            
            // Informs the instances in the handler
            if (handler.handlingState)
            {
                handler.act(millisPassed)
            }
            
            // Waits until the next act call
            WaitUtil.waitUntil(nextActNanos, this)
        }
    }
}