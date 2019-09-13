package utopia.genesis.shape

import utopia.flow.util.RichComparable
import utopia.genesis.util.Arithmetic
import utopia.flow.util.TimeExtensions._

import scala.concurrent.duration.Duration

object LinearAcceleration
{
	/**
	  * A acceleration with 0 amount
	  */
	val zero = LinearAcceleration(LinearVelocity.zero, 1.seconds)
}

/**
  * Used for tracking acceleration without direction
  * @author Mikko Hilpinen
  * @since 13.9.2019, v2.1+
  */
case class LinearAcceleration(override val amount: LinearVelocity, override val duration: Duration) extends
	Change[LinearVelocity, LinearAcceleration] with Arithmetic[LinearAcceleration, LinearAcceleration]
	with RichComparable[LinearAcceleration]
{
	// COMPUTED	-----------------------
	
	/**
	  * @return Whether this acceleration is positive
	  */
	def isPositive = amount.isPositive
	
	/**
	  * @return This acceleration if positive or a zero acceleration
	  */
	def positive = if (isPositive) this else LinearAcceleration(LinearVelocity.zero, duration)
	
	
	// IMPLEMENTED	-------------------
	
	override def *(mod: Double) = LinearAcceleration(amount * mod, duration)
	
	override def +(another: LinearAcceleration) = LinearAcceleration(amount + another(duration), duration)
	
	override def -(another: LinearAcceleration) = this + (-another)
	
	override def toString = s"${perMilliSecond.amount} / ms^2"
	
	override def compareTo(o: LinearAcceleration) = perMilliSecond.compareTo(o.perMilliSecond)
	
	
	// OTHER	-----------------------
	
	/**
	  * @param direction Target direction
	  * @return A directed version of this acceleration
	  */
	def withDirection(direction: Angle) = Acceleration(amount.withDirection(direction), duration)
}
