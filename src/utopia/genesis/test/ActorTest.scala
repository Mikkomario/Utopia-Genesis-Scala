package utopia.genesis.test

import utopia.flow.util.TimeExtensions._

import utopia.genesis.event.Actor
import utopia.genesis.event.ActorThread
import utopia.genesis.event.ActorHandlerType
import java.time.Duration
import utopia.flow.util.WaitUtils

object ActorTest extends App
{
    class TestActor extends Actor
    {
        var millisCounted = 0.0
        
        override def act(duration: Duration) = millisCounted += duration.toPreciseMillis
    }
    
    val actor1 = new TestActor()
    val actor2 = new TestActor()
    
    val thread = new ActorThread(20, 60)
    thread.handler ++= (actor1, actor2)
    
    assert(actor1.millisCounted == 0)
    assert(actor2.millisCounted == 0)
    
    thread.start()
    WaitUtils.wait(Duration.ofSeconds(1), this)
    
    actor2.specifyHandlingState(ActorHandlerType, false)
    
    val millis1 = actor1.millisCounted
    val millis2 = actor2.millisCounted
    
    assert(millis1 > 500)
    assert(millis1 < 1500)
    assert(millis2 > 500)
    assert(millis2 < 1500)
    
    WaitUtils.wait(Duration.ofSeconds(1), this)
    
    assert(actor1.millisCounted > millis1 + 500)
    assert(actor2.millisCounted < millis2 + 500)
    
    thread.ended = true
    
    println("Success")
}