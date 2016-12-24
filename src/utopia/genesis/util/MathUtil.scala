package utopia.genesis.util

/**
 * This object offers some utility methods for mathematical functions
 * @author Mikko Hilpinen
 * @since 24.12.2016
 */
object MathUtil
{
    /**
     * Makes sure a degree amount is between 0 (inclusive) and 360 (exlusive). For example, -10
     * becomes 350
     * @param degrees The amount of degrees
     * @return The provided degree amount cast to range [0, 360[
     * @deprecated Please use Scala's richDouble implementation instead
     */
    @deprecated
    def degreesToRange(degrees: Double) = 
    {
        val temp = degrees % 360
        if (temp < 0) temp + 360 else temp
    }
    
    /**
     * Converts from radians to degrees. The value is cast to range [0, 360[
     * @param radians The direction in radians
     * @return the direction in degrees
     * @deprecated Please use Scala's richDouble implementation instead
     */
    @deprecated
    def degreesOfRadians(radians: Double) = degreesToRange(Math.toDegrees(radians))
}