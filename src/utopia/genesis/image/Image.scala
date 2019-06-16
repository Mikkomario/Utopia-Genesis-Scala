package utopia.genesis.image

import java.awt.image.{BufferedImage, BufferedImageOp}
import java.nio.file.Path

import javax.imageio.ImageIO
import utopia.flow.datastructure.mutable.Lazy
import utopia.genesis.color.Color
import utopia.genesis.shape.{Vector3D, VectorLike}
import utopia.genesis.shape.shape2D.{Area2D, Bounds, Point, Size, Transformation}
import utopia.genesis.util.Drawer

import scala.util.Try

object Image
{
	/**
	  * Creates a new image
	  * @param image The original buffered image source
	  * @param scaling The scaling applied to the image
	  * @return A new image
	  */
	def apply(image: BufferedImage, scaling: Vector3D = Vector3D.identity): Image = Image(image, scaling,
		Lazy(PixelTable.fromBufferedImage(image)))
	
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
			apply(result)
	}
}

/**
  * This is a wrapper for the buffered image class
  * @author Mikko Hilpinen
  * @since 15.6.2019, v2.1+
  */
case class Image private(private val source: BufferedImage, scaling: Vector3D,
						 private val _pixels: Lazy[PixelTable])
{
	// COMPUTED	--------------------
	
	/**
	  * @return The pixels in this image
	  */
	def pixels = _pixels.get
	
	/**
	  * @return The width of this image in pixels
	  */
	def width = source.getWidth * scaling.x
	
	/**
	  * @return The height of this image in pixels
	  */
	def height = source.getHeight * scaling.y
	
	/**
	  * @return The size of this image in pixels
	  */
	def size = Size(width, height)
	
	/**
	  * @return A copy of this image that isn't scaled above 100%
	  */
	def downscaled = if (scaling.dimensions2D.exists { _ > 1 }) withScaling(scaling.map { _ min 1 }) else this
	
	/**
	  * @return A copy of this image that isn't scaled below 100%
	  */
	def upscaled = if (scaling.dimensions2D.exists { _ < 1 }) withScaling(scaling.map { _ max 1 }) else this
	
	/**
	  * @return A copy of this image with original (100%) scaling
	  */
	def withOriginalSize = if (scaling == Vector3D.identity) this else withScaling(1)
	
	/**
	  * @return A copy of this image where x-axis is reversed
	  */
	def flippedHorizontally = mapPixelTable { _.flippedHorizontally }
	
	/**
	  * @return A copy of this image where y-axis is reversed
	  */
	def flippedVertically = mapPixelTable { _.flippedVertically }
	
	
	// OPERATORS	----------------
	
	/**
	  * Scales this image
	  * @param scaling The scaling factor
	  * @return A scaled version of this image
	  */
	def *(scaling: VectorLike[_]): Image = withScaling(this.scaling * scaling)
	
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
	def /(divider: VectorLike[_]): Image = withScaling(scaling / divider)
	
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
		area.within(Bounds(Point.origin, size)).map { _ / scaling }.map {
			a => Image(source.getSubimage(a.x.toInt, a.y.toInt, a.width.toInt, a.height.toInt), scaling) }.getOrElse(
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
		drawer.transformed(Transformation.translation(position.toVector - origin).scaled(scaling)).drawImage(source)
	
	/**
	  * @param scaling A scaling modifier applied to the original image
	  * @return A scaled version of this image
	  */
	def withScaling(scaling: Vector3D) = copy(scaling = scaling)
	
	/**
	  * @param scaling A scaling modifier applied to the original image
	  * @return A scaled version of this image
	  */
	def withScaling(scaling: Double): Image = withScaling(Vector3D(scaling, scaling))
	
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
	def mapPixelTable(f: PixelTable => PixelTable) =
	{
		val newPixels = f(pixels)
		Image(newPixels.toBufferedImage, scaling, Lazy(newPixels))
	}
	
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
		(c, p) => if (area.contains(p * scaling)) f(c) else c }
	
	/**
	  * Applies a bufferedImageOp to this image, producing a new image
	  * @param op The operation that is applied
	  * @return A new image with operation applied
	  */
	def filterWith(op: BufferedImageOp) =
	{
		val destination = new BufferedImage(source.getWidth, source.getHeight, source.getType)
		op.filter(source, destination)
		Image(destination, scaling)
	}
}

private class NoImageReaderAvailableException(message: String) extends RuntimeException(message)