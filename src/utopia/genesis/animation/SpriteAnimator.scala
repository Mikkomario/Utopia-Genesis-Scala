package utopia.genesis.animation

import utopia.genesis.image.{Image, Strip}
import utopia.genesis.shape.shape2D.{Point, Transformation}
import utopia.genesis.util.Drawer

import scala.concurrent.duration.Duration

// TODO: Create a transformable version separately (=> Make this a trait). Add transformable traits.
/**
  * This animator draws sprites
  * @author Mikko Hilpinen
  * @since 18.8.2019, v2.1+
  */
class SpriteAnimator(initialStrip: Strip, initialDuration: Duration, initialOrigin: Point,
					 private val getTransform: () => Transformation) extends Animator[Image]
{
	// ATTRIBUTES	---------------------
	
	private var _strip = initialStrip
	private var _duration = initialDuration
	private var _origin = initialOrigin
	
	
	// IMPLEMENTED	---------------------
	
	override def animationDuration = _duration
	
	override protected def apply(progress: Double) = _strip(progress)
	
	override protected def draw(drawer: Drawer, item: Image) = item.drawWith(drawer.transformed(getTransform()), _origin)
	
	
	// OTHER	------------------------
	
	/**
	  * Changes the strip drawn by this animator
	  * @param strip The new strip to be drawn
	  * @param duration Duration it takes to complete a single animation
	  * @param origin Sprite origin (relative, default = (0,0))
	  * @param preserveProgress Whether animation progress should be preserved (default = false)
	  */
	def set(strip: Strip, duration: Duration, origin: Point = Point.origin, preserveProgress: Boolean = false) =
	{
		val newProgress = if (preserveProgress) progress else 0.0
		_strip = strip
		_duration = duration
		_origin = origin
		progress = newProgress
	}
}
