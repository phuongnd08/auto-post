package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import actors.Actor._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueProcessorSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var processor: QueueProcessor = _

  override def beforeEach {
  }
  it("should send get to collector") {
    var received = false
    val collector = actor {
      receiveWithin(1000) {
        case cmd: String => received = cmd == "get"
        case _ =>
      }
    }
    processor = new QueueProcessor("test_1", collector, q => {})
    processor.start
    while (collector.getState != State.Terminated) Thread.sleep(100)
    received must be(true)
  }

  it("should invoke process when receive a Queue") {
    var invoked = false
    processor = new QueueProcessor("test_2", actor {}, q => invoked = true)
    processor.start
    processor ! Queue(null, null)
    Thread.sleep(100)
    invoked must be(true)
  }

  override def afterEach {
    if (processor != null)
      processor ! "exit"
    processor = null
  }
}