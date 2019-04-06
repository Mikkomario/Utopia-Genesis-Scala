package utopia.genesis.view

import utopia.flow.async.AsyncExtensions._
import utopia.flow.util.TimeExtensions._
import java.awt.Component
import java.awt.event.{ComponentAdapter, ComponentEvent, HierarchyEvent, HierarchyListener}
import java.time.{Duration, Instant}

import javax.swing.SwingUtilities
import utopia.flow.async.Loop
import utopia.flow.util.WaitTarget.{Until, WaitDuration}
import utopia.flow.util.WaitUtils

import scala.concurrent.Promise
import scala.ref.WeakReference

/**
  * This loop continuously repaints a single component
  * @param comp The target component
  * @param maxFPS Maximum frames (paints) per second (default = 60)
  */
class RepaintLoop(comp: Component, val maxFPS: Int = 60) extends Loop
{
	// ATTRIBUTES	-----------------
	
	private val component = WeakReference(comp)
	
	private var lastDrawTime = Instant.now()
	
	val refreshInterval = if (maxFPS > 0) Some(Duration.ofNanos((1000.0 / maxFPS * 1000000).toLong)) else None
	
	
	// INITIAL CODE	-----------------
	
	// Starts listening to component state changes
	comp.addComponentListener(new ComponentListener())
	
	
	// IMPLEMENTED	-----------------
	
	/**
	  * Paints the target component
	  */
	override protected def runOnce() =
	{
		// Waits until component is displayable
		while (!isBroken && component.get.exists { c => !c.isDisplayable || !c.isShowing })
		{
			WaitUtils.waitUntilNotified(waitLock)
		}
		
		lastDrawTime = Instant.now()
		
		if (component.get.exists { _.isDisplayable })
		{
			// Performs the redraw on swing thread
			val completion = Promise[Unit]()
			SwingUtilities.invokeLater(() =>
			{
				component.get.foreach { _.repaint() }
				completion.success()
			})
			
			// Waits until drawing is completed (has timeout for special cases)
			completion.future.waitFor(Duration.ofSeconds(5))
		}
		else if (component.get.isEmpty)
		{
			// Stops once the component is no longer held in memory
			stop()
		}
	}
	
	/**
	  * The time between the end of the current run and the start of the next one
	  */
	override protected def nextWaitTarget = refreshInterval.map {
		d => Until(lastDrawTime + d) } getOrElse WaitDuration(Duration.ZERO)
	
	
	// NESTED CLASSES	-----------------
	
	private class ComponentListener extends ComponentAdapter with HierarchyListener
	{
		override def hierarchyChanged(e: HierarchyEvent) =
		{
			WaitUtils.notify(waitLock)
		}
		
		override def componentShown(e: ComponentEvent) =
		{
			// When component is shown, reawakens the loop
			WaitUtils.notify(waitLock)
		}
	}
}
