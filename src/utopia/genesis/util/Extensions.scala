package utopia.genesis.util

/**
 * This object contains some implicit extensions introduced in Genesis
 * @author Mikko Hilpinen
 * @since 24.12.2016
 */
object Extensions
{
    implicit class DoubleWithAlmostEquals(val d: Double) extends AnyVal
    {
        def ~==(d2: Double) = (d -d2).abs < 0.00001
    }
}