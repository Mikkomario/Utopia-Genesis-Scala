package utopia.genesis.event

/**
 * This events are fired whenever the user types something on the keyboard. Unlike Keystate events,
 * these events focus less on the keyboard state changes and
 * more on the characters typed by the user.
 * @author Mikko Hilpinen
 * @since 23.2.2017
 */
class KeyTypedEvent(val typedChar: Char, val keyStatus: KeyStatus)