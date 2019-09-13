package utopia.genesis.util

import scala.language.implicitConversions

object Arithmetic
{
	implicit def arithmeticDoubleBack(d: ArithMeticDouble): Double = d.d
	
	implicit class ArithMeticDouble(val d: Double) extends Arithmetic[ArithMeticDouble, ArithMeticDouble] with Distance
	{
		override def length = d
		
		override def -(another: ArithMeticDouble) = d - another
		
		override def *(mod: Double) = d * mod
		
		override def +(another: ArithMeticDouble) = d + another
	}
}

/**
  * These elements can mostly be treated like numbers
  * @author Mikko Hilpinen
  * @since 20.6.2019, v2.1+
  */
trait Arithmetic[-N, +Repr] extends Scalable[Repr] with Combinable[N, Repr]
{
	/**
	  * @param another Another item
	  * @return A subtraction of these items
	  */
	def -(another: N): Repr
}