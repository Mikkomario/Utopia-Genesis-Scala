package utopia.genesis.test

import utopia.genesis.event.ActorThread
import utopia.genesis.handling.{Actor, ActorHandlerType}
import utopia.genesis.util.WaitUtil

object ActorTest extends App
{
    class TestActor extends Actor
    {
        var millisCounted = 0.0
        
        override def act(durationMillis: Double) = millisCounted += durationMillis
    }
    
    val actor1 = new TestActor()
    val actor2 = new TestActor()
    
    val thread = new ActorThread(20, 60)
    thread.handler ++= (actor1, actor2)
    
    assert(actor1.millisCounted == 0)
    assert(actor2.millisCounted == 0)
    
    thread.start()
    WaitUtil.waitMillis(1000, this)
    
    actor2.specifyHandlingState(ActorHandlerType, false)
    
    val millis1 = actor1.millisCounted
    val millis2 = actor2.millisCounted
    
    assert(millis1 > 500)
    assert(millis1 < 1500)
    assert(millis2 > 500)
    assert(millis2 < 1500)
    
    WaitUtil.waitMillis(1000, this)
    
    assert(actor1.millisCounted > millis1 + 500)
    assert(actor2.millisCounted < millis2 + 500)
    
    thread.ended = true
    
    println("Success")
}