package utopia.genesis.util

/**
 * This utility object provides few utility methods for thread waiting
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
object WaitUtil
{
    /**
     * Waits on the provided lock object until a certain nanotime. This method handles the necessary
     * synchronisation
     * @param nanoTime The time at which the lock is released
     * @param lock the locking object that is the thead waits on
     */
    def waitUntil(nanoTime: Long, lock: AnyRef) = 
    {
        var waitMillis = millisOf(nanoTime - System.nanoTime())
        
        while (waitMillis > 0)
        {
            lock.synchronized
            {
                val completeWaitMillis = waitMillis.toLong
                val remainingNanos = nanosOf(waitMillis - completeWaitMillis).toInt
                
                try
                {
                    lock.wait(completeWaitMillis, remainingNanos)
                    waitMillis = 0
                }
                catch 
                {
                    case _: InterruptedException => waitMillis = millisOf(nanoTime - System.nanoTime())
                }
            }
        }
    }
    
    /**
     * Converts milliseconds to nanoseconds
     */
    def nanosOf(millis: Double) = (millis * 1000000).toLong
    
    /**
     * Converts nanoseconds to milliseconds
     */
    def millisOf(nanos: Long) = nanos / 1000000.0
}