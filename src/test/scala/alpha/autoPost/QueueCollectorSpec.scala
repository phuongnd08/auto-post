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

class QueueCollectorSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var collector: QueueCollector = _

  override def beforeEach {
    collector = new QueueCollector
    collector.start
  }
  it("should send back all queued configuration") {
    val s1 = Section()
    s1.sites = List(Site("google.com"), Site("vnexpress.net"))
    var s2 = Section()
    s2.sites = List(Site("batdongsan.com"), Site("nhada.net"))
    var s3 = Section()
    collector ! s1
    collector ! (s2, s2.sites(0))
    collector ! s3
    var list = List[Queue]()
    val receiver = actor {
      for (i <- 1 to 4) {
        collector ! "get"
        receiveWithin(100) {
          case q: Queue => list = q :: list
          case _ =>
        }
      }
    }
    while (receiver.getState != State.Terminated) {Thread.sleep(100)}
    list.reverse must be(List(
      Queue(s1, Site("google.com")),
      Queue(s1, Site("vnexpress.net")),
      Queue(s2, Site("batdongsan.com"))
      ))
  }

  override def afterEach {
    collector ! "exit"
  }
}