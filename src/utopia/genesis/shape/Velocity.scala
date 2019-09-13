package utopia.genesis.shape

import scala.concurrent.duration.Duration
import utopia.genesis.util.Arithmetic
import utopia.flow.util.TimeExtensions._

object Velocity
{
	/**
	  * A zero velocity
	  */
	val zero = Velocity(Vector3D.zero, 1.seconds)
}

/**
  * Used for tracking speed
  * @author Mikko Hilpinen
  * @since 11.9.2019, v2.1+
  * @param transition The amount of transition over 'duration'
  * @param duration The duration of change
  */
case class Velocity(transition: Vector3D, override val duration: Duration) extends Change[Vector3D, Velocity]
	with Arithmetic[Velocity, Velocity] with Dimensional[LinearVelocity]
{
	// COMPUTED	-----------------
	
	/**
	  * @return Direction of this velocity vector
	  */
	def direction = transition.direction
	
	/**
	  * @return A copy of this velocity without z-axis movement
	  */
	def in2D = if (transition.z == 0) this else copy(transition = transition.in2D)
	
	/**
	  * @return A linear copy of this velocity, based on transition amount / length
	  */
	def linear = LinearVelocity(transition.length, duration)
	
	
	// IMPLEMENTED	-------------
	
	override def amount = transition
	
	override def *(mod: Double) = Velocity(transition * mod, duration)
	
	override def +(other: Velocity) = Velocity(transition + other(duration), duration)
	
	override def -(other: Velocity) = this + (-other)
	
	override def toString = s"$perMilliSecond / ms"
	
	def along(axis: Axis) = LinearVelocity(transition.along(axis), duration)
	
	
	// OTHER	-----------------
	
	/**
	  * @param time Target duration
	  * @return An acceleration to increase this amount of velocity in specified time
	  */
	def acceleratedIn(time: Duration) = Acceleration(this, time)
}
