package utopia.genesis.event

/**
 * This trait is above all the different mouse movement event types
 * @author Mikko Hilpinen
 * @since 10.1.2017
 */
sealed trait MouseMoveEventType extends EventType

/**
 * These events are generated whenever the mouse cursor is moved
 */
case object Move extends MouseMoveEventType { val rawValue = 1 }
/**
 * These events are generated whenever the mouse cursor is moved over a specified area
 */
case object MoveOver extends MouseMoveEventType { val rawValue = 2 }
/**
 * These events are generated whenever the mouse enters a specified area
 */
case object Enter extends MouseMoveEventType { val rawValue = 3 }
/**
 * These events are generated whenever the mouse exits a specified area
 */
case object Exit extends MouseMoveEventType { val rawValue = 4 }