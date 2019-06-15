package utopia.genesis.image

import java.awt.image.BufferedImage
import java.nio.file.Path

import javax.imageio.ImageIO
import utopia.genesis.shape.{Vector3D, VectorLike}
import utopia.genesis.shape.shape2D.{Bounds, Point, Size, Transformation}
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
		area.within(Bounds(Point.origin, size)).map { _.mapPosition { _ / sourceScaling }.mapSize { _ / sourceScaling } } .map{
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
	
	// def smallerThan(area: Size) = if (size.fi)
}

private class NoImageReaderAvailableException(message: String) extends RuntimeException(message)