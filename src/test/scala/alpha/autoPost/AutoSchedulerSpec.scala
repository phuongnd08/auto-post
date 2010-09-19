package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import actors.Actor
import actors.Actor._
import org.scalatest.mock.MockitoSugar

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class AutoSchedulerSpec extends Spec with MustMatchers with BeforeAndAfterEach with MockitoSugar {
  var autoScheduler: Actor = _

  it("must exit upon receive exit signal") {
    autoScheduler = new AutoScheduler(new TimeProvider, () => Nil, null, 100)
    autoScheduler.start
    Thread.sleep(100)
    autoScheduler.getState must not be(State.Terminated)
    autoScheduler ! "exit"
    Thread.sleep(100)
    autoScheduler.getState must be(State.Terminated)
  }

  it("must queue the section if execution time reached") {
    import org.mockito._
    import Mockito._
    import Matchers._
    val timeProvider = this.mock[TimeProvider]
    when(timeProvider.current).thenReturn(1, 1000, 2000)
    val config1 = this.mock[Section]
    when(config1.shouldRunNow(1, 1000)).thenReturn(false)
    when(config1.shouldRunNow(1, 2000)).thenReturn(true)
    val config2 = this.mock[Section]
    when(config2.shouldRunNow(1, 1000)).thenReturn(true)
    when(config2.shouldRunNow(1000, 2000)).thenReturn(false)
    var receivedConfigs = List[Section]()
    val collector = actor {
      for (i <- 1 to 3)
        receiveWithin(100) {
          case config: Section => receivedConfigs = receivedConfigs ::: List(config)
          case _ =>
        }
    }
    autoScheduler = new AutoScheduler(timeProvider, () => List(config1, config2), collector, 50)
    autoScheduler.start
    while (collector.getState != State.Terminated) Thread.sleep(100)
    receivedConfigs must be (List(config2, config1))
  }

  override def afterEach {
    if (autoScheduler != null) autoScheduler ! "exit"
  }
}