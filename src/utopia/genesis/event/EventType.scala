package utopia.genesis.event

import utopia.flow.util.Equatable

/**
 * Event types can be compared and differentiated with each other. They also have a raw value which
 * allows one to use them in models
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
trait EventType extends Equatable
{
    def rawValue: Int
    
    override def properties = Vector(rawValue)
}