package utopia.genesis.event

import utopia.genesis.util.Vector3D
import utopia.inception.event.Event
import utopia.flow.datastructure.immutable.Model
import utopia.flow.datastructure.immutable.Value

object MouseMoveEvent
{
    val eventTypeAttName = "type"
    val mousePositionAttName = "position"
    val durationMillisAttName = "duration"
}

/**
 * These events are generated when the mouse cursor moves
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
class MouseMoveEvent(val eventType: MouseMoveEventType, val mousePosition: Vector3D, 
        val durationMillis: Double) extends Event
{
    // TODO: Add mouse position after vector3D is a suitable value type
    override val identifiers = Model.apply(Vector(
            (MouseMoveEvent.eventTypeAttName, Value of eventType.rawValue), 
            (MouseMoveEvent.durationMillisAttName, Value of durationMillis)));
}