package utopia.genesis.util

import scala.language.implicitConversions

object Arithmetic
{
	implicit def arithmeticDoubleBack(d: ArithMeticDouble) = d.d
	
	implicit class ArithMeticDouble(val d: Double) extends Arithmetic[ArithMeticDouble, ArithMeticDouble]
	{
		override def -(another: ArithMeticDouble) = d - another
		
		override def *(mod: Double) = d * mod
		
		override def +(another: ArithMeticDouble) = d + another
		
		override def distanceFrom(another: ArithMeticDouble) = d - another
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
	
	/**
	  * @param another Another item
	  * @return The distance starting from another item to this item
	  */
	def distanceFrom(another: N): Double
}