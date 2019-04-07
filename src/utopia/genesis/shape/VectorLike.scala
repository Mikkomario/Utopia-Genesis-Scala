package utopia.genesis.shape

trait VectorLike[Repr <: VectorLike[Repr]]
{
	def dimensions: Vector[Double]
	
	def newBuilder: VectorBuilder[Repr]
	
	def x = dimensions.applyOrElse(0, _ => 0.0)
	
	def y = dimensions.applyOrElse(1, _ => 0.0)
	
	def z = dimensions.applyOrElse(2, _ => 0.0)
	
	def xProjection = X(x)
	
	def yProjection = Y(y)
	
	def zProjection = Z(z)
	
	// TODO: Separate to Vector2DLike (with only X and Y) and Vector3DLike (with Z)
}

trait VectorBuilder[Repr <: VectorLike[Repr]]
{
	def buildFrom(dimensions: Vector[Double]): Repr
}
