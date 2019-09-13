package utopia.genesis.shape

import utopia.flow.util.RichComparable

import scala.concurrent.duration.Duration
import utopia.flow.util.TimeExtensions._
import utopia.genesis.util.Arithmetic

object LinearVelocity
{
	/**
	  * A zero speed
	  */
	val zero = LinearVelocity(0, 1.seconds)
}

/**
  * Used for tracking speed but not direction
  * @author Mikko Hilpinen
  * @since 11.9.2019, v2.1+
  */
case class LinearVelocity(override val amount: Double, override val duration: Duration) extends
	Change[Double, LinearVelocity] with Arithmetic[LinearVelocity, LinearVelocity] with RichComparable[LinearVelocity]
{
	// COMPUTED	------------------------
	
	/**
	  * @return Whether this velocity is positive
	  */
	def isPositive = amount >= 0
	
	/**
	  * @return A non-negative copy of this velocity
	  */
	def positive = if (isPositive) this else LinearVelocity(0, duration)
	
	
	// IMPLEMENTED	--------------------
	
	override def *(mod: Double) = LinearVelocity(amount * mod, duration)
	
	override def toString = s"$perMilliSecond / ms"
	
	def +(another: LinearVelocity) = LinearVelocity(amount + another(duration), duration)
	
	override def compareTo(o: LinearVelocity) = perMilliSecond.compareTo(o.perMilliSecond)
	
	def -(another: LinearVelocity) = this + (-another)
	
	
	// OTHER	------------------------
	
	/**
	  * Adds direction component to this velocity
	  * @param direction Direction of this velocity
	  * @return A directional velocity
	  */
	def withDirection(direction: Angle) = Velocity(Vector3D.lenDir(amount, direction), duration)
}
