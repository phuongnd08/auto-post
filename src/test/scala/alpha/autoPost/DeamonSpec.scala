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

class DaemonSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var daemon: Daemon = _
  var counter = 0

  override def beforeEach {
    daemon = new Daemon(() => counter += 1, 100)
    daemon.start
  }

  it("must exit upon receive exit signal") {
    Thread.sleep(50)
    daemon.getState must be(State.TimedBlocked)
    daemon ! "exit"
    Thread.sleep(50)
    daemon.getState must be(State.Terminated)
  }

  it("must increase counter as it progress") {
    Thread.sleep(350)
    daemon ! "exit"
    counter must be (3)
  }
}