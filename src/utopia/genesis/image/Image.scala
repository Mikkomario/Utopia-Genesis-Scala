package utopia.genesis.image

import java.awt.image.BufferedImage
import java.nio.file.Path

import javax.imageio.ImageIO
import utopia.genesis.color.Color
import utopia.genesis.shape.{Vector3D, VectorLike}
import utopia.genesis.shape.shape2D.{Area2D, Bounds, Point, Size, Transformation}
import utopia.genesis.util.Drawer

import scala.util.Try

object Image
{
	/**
	  * Reads an image from a file
	  * @param path The path the image is read from
	  * @return The read image wrapped in Try
	  */
	def readFrom(path: Path) = Try
	{
		val result = ImageIO.read(path.toFile)
		if (result == null)
			throw new NoImageReaderAvailableException("Cannot read image from file: " + path.toString)
		else
			result
	}
}

/**
  * This is a wrapper for the buffered image class
  * @author Mikko Hilpinen
  * @since 15.6.2019, v2.1+
  */
case class Image(private val source: BufferedImage, sourceScaling: Vector3D = Vector3D.identity)
{
	// ATTRIBUTES	----------------
	
	/**
	  * The pixels that form this image (a single vector)
	  */
	lazy val pixels = PixelTable.fromBufferedImage(source)
	
	
	// COMPUTED	--------------------
	
	/**
	  * @return The width of this image in pixels
	  */
	def width = source.getWidth * sourceScaling.x
	
	/**
	  * @return The height of this image in pixels
	  */
	def height = source.getHeight * sourceScaling.y
	
	/**
	  * @return The size of this image in pixels
	  */
	def size = Size(width, height)
	
	/**
	  * @return A copy of this image that isn't scaled above 100%
	  */
	def downscaled = if (sourceScaling.dimensions2D.exists { _ > 1 }) Image(source, sourceScaling.map { _ min 1 }) else this
	
	/**
	  * @return A copy of this image that isn't scaled below 100%
	  */
	def upscaled = if (sourceScaling.dimensions2D.exists { _ < 1 }) Image(source, sourceScaling.map { _ max 1 }) else this
	
	
	// OPERATORS	----------------
	
	/**
	  * Scales this image
	  * @param scaling The scaling factor
	  * @return A scaled version of this image
	  */
	def *(scaling: VectorLike[_]): Image = Image(source, sourceScaling * scaling)
	
	/**
	  * Scales this image
	  * @param scaling The scaling factor
	  * @return A scaled version of this image
	  */
	def *(scaling: Double): Image = this * Vector3D(scaling, scaling)
	
	/**
	  * Downscales this image
	  * @param divider The dividing factor
	  * @return A downscaled version of this image
	  */
	def /(divider: VectorLike[_]): Image = Image(source, sourceScaling / divider)
	
	/**
	  * Downscales this image
	  * @param divider The dividing factor
	  * @return A downscaled version of this image
	  */
	def /(divider: Double): Image = this / Vector3D(divider, divider)
	
	
	// OTHER	--------------------
	
	/**
	  * Takes a sub-image from this image (meaning only a portion of this image)
	  * @param area The relative area that is cut from this image
	  * @return The portion of this image within the relative area
	  */
	def subImage(area: Bounds) =
	{
		area.within(Bounds(Point.origin, size)).map { _ / sourceScaling }.map {
			a => Image(source.getSubimage(a.x.toInt, a.y.toInt, a.width.toInt, a.height.toInt), sourceScaling) }.getOrElse(
			Image(new BufferedImage(0, 0, source.getType)))
	}
	
	/**
	  * Converts this one image into a strip containing multiple parts. The splitting is done horizontally.
	  * @param numberOfParts The number of separate parts within this image
	  * @param marginBetweenParts The horizontal margin between the parts within this image in pixels (default = 0)
	  * @return A stip containing numberOfParts images, which all are sub-images of this image
	  */
	def split(numberOfParts: Int, marginBetweenParts: Int = 0) =
	{
		val subImageWidth = (width - marginBetweenParts * (numberOfParts - 1)) / numberOfParts
		val subImageSize = Size(subImageWidth, height)
		Strip((0 until numberOfParts).map {
			index => subImage(Bounds(Point(index * (subImageWidth + marginBetweenParts), 0), subImageSize)) }.toVector)
	}
	
	/**
	  * Draws this image using a specific drawer
	  * @param drawer A drawer
	  * @param position The position where this image's origin is drawn (default = (0, 0))
	  * @param origin The relative origin of this image (default = (0, 0) = top left corner)
	  * @return Whether this image was fully drawn
	  */
	def drawWith(drawer: Drawer, position: Point = Point.origin, origin: Point = Point.origin) =
		drawer.transformed(Transformation.translation(position.toVector - origin).scaled(sourceScaling)).drawImage(source)
	
	/**
	  * @param newSize The target size for this image
	  * @return A copy of this image scaled to match the target size (dimensions might not be preserved)
	  */
	def withSize(newSize: Size) = this * (newSize / size)
	
	/**
	  * Scales this image, preserving shape.
	  * @param area An area
	  * @return A copy of this image that matches the specified area, but may be larger if shape preservation demands it.
	  */
	def filling(area: Size) = this * (area / size).dimensions2D.max
	
	/**
	  * Scales this image, preserving shape.
	  * @param area An area
	  * @return A copy of this image that matches the specified area, but may be smaller if shape preservation demands it.
	  */
	def fitting(area: Size) = this * (area / size).dimensions2D.min
	
	/**
	  * @param area Target area (maximum)
	  * @return A copy of this image that is smaller or equal to the target area. Shape is preserved.
	  */
	def smallerThan(area: Size) = if (size.fitsInto(area)) this else fitting(area)
	
	/**
	  * @param area Target area (minimum)
	  * @return A copy of this image that is larger or equal to the target area. Shape is preserved.
	  */
	def largerThan(area: Size) = if (area.fitsInto(size)) this else filling(area)
	
	/**
	  * @param f A mapping function for pixel tables
	  * @return A copy of this image with mapped pixels
	  */
	def mapPixelTable(f: PixelTable => PixelTable) = Image(f(pixels).toBufferedImage, sourceScaling)
	
	/**
	  * @param f A function that maps pixel colors
	  * @return A copy of this image with mapped pixels
	  */
	def mapPixels(f: Color => Color) = mapPixelTable { _.map(f) }
	
	/**
	  * @param f A function that maps pixel colors, also taking relative pixel coordinate
	  * @return A copy of this image with mapped pixels
	  */
	def mapPixelsWithIndex(f: (Color, Point) => Color) = mapPixelTable { _.mapWithIndex(f) }
	
	/**
	  * @param area The mapped relative area
	  * @param f A function that maps pixel colors
	  * @return A copy of this image with pixels mapped within the target area
	  */
	def mapArea(area: Area2D)(f: Color => Color) = mapPixelsWithIndex {
		(c, p) => if (area.contains(p * sourceScaling)) f(c) else c }
}

private class NoImageReaderAvailableException(message: String) extends RuntimeException(message)