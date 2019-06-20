package utopia.genesis.util

object Arithmetic
{
	implicit class ArithMeticDouble(val d: Double) extends Arithmetic[Double, Double]
	{
		override def -(another: Double) = d - another
		
		override def *(mod: Double) = d * mod
		
		override def +(another: Double) = d + another
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