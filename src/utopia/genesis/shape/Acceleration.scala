package utopia.genesis.shape

import utopia.genesis.util.Arithmetic
import utopia.flow.util.TimeExtensions._

import scala.concurrent.duration.Duration

object Acceleration
{
	/**
	  * A zero acceleration
	  */
	val zero = Acceleration(Velocity.zero, 1.seconds)
}

/**
  * Represents a change in velocity over a time period
  * @author Mikko Hilpinen
  * @since 13.9.2019, v2.1+
  */
case class Acceleration(override val amount: Velocity, override val duration: Duration) extends
	Change[Velocity, Acceleration] with Arithmetic[Acceleration, Acceleration] with Dimensional[LinearAcceleration]
{
	// COMPUTED	-----------------------
	
	/**
	  * @return Direction of this acceleration
	  */
	def direction = amount.direction
	
	/**
	  * @return A 2D copy of this acceleration (where z-acceleration is 0)
	  */
	def in2D = if (amount.transition.z == 0) this else copy(amount = amount.in2D)
	
	/**
	  * @return This acceleration without the direction component
	  */
	def linear = LinearAcceleration(amount.linear, duration)
	
	
	// IMPLEMENTED	-------------------
	
	override def -(another: Acceleration) = this + (-another)
	
	override def *(mod: Double) = Acceleration(amount * mod, duration)
	
	override def +(another: Acceleration) = Acceleration(amount + another(duration), duration)
	
	override def toString = s"${perMilliSecond.transition} / ms^2"
	
	override def along(axis: Axis) = LinearAcceleration(amount.along(axis), duration)
}
