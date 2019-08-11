package utopia.genesis.animation

/**
  * Animations are items that have different states based on (time) progress
  * @author Mikko Hilpinen
  * @since 11.8.2019, v2.1+
  */
trait Animation[+A]
{
	/**
	  * Finds a state of this animation
	  * @param progress Progress over this animation
	  * @return The state of this animation at the specified point
	  */
	def apply(progress: Double): A
}
