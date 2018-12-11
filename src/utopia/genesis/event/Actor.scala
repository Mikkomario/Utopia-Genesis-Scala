package utopia.genesis.event

import utopia.inception.handling.Handleable
import java.time.Duration

/**
 * Actors are instances that are called between certain intervals to perform some logical, 
 * (duration based) operations.
 * @author Mikko Hilpinen
 * @since 23.12.2016
 */
trait Actor extends Handleable
{
    /**
     * This method will be called at a short intervals (usually >= 60 times a second). The instance
     * should update it's status here. This method should only be called when the object's handling
     * state allows it.
     * @param duration The amount of time passed since the last act call
     */
    def act(duration: Duration)
}