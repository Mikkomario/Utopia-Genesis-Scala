package utopia.genesis.event

import utopia.flow.util.TimeExtensions._

import utopia.genesis.util.WaitUtil
import java.time.Duration
import java.time.Instant
import utopia.flow.util.RichComparable
import utopia.flow.util.WaitUtils

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
    private val minInterval = if (maxAPS > 0.0) Some(Duration.ofMillis((1000.0 / maxAPS).toLong)) else None
    
    /**
     * The maximum amount of milliseconds passed into the act method
     */
    private val maxInterval = if (minAPS > 0.0) Some(Duration.ofMillis((1000.0 / minAPS).toLong)) else None
    
    
    // IMPLEMENTED METHODS    ---------------
    
    override def run()
    {
        var lastAct = Instant.now()
        
        while (!ended)
        {
            val thisAct = Instant.now()
            val nextAct = minInterval.map(thisAct + _)
            
            val lastActDuration = maxInterval.map(RichComparable.min(thisAct - lastAct, _)).getOrElse(thisAct - lastAct)
            lastAct = thisAct
            
            // Informs the instances in the handler
            if (handler.handlingState)
            {
                handler.act(lastActDuration)
            }
            
            // Waits until the next act call
            nextAct.foreach(WaitUtils.waitUntil(_, this))
        }
    }
}