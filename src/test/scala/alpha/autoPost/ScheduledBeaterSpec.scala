package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class ScheduledBeaterSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var beater: Beater = _

  override def beforeEach {
    beater = (new ScheduledBeater(10, 20))
  }
  describe("shouldBeatNow") {
    it("must return true if the last beat is before today 10:20 and now is after today 10:20") {
      val lastBeat = new DateTime(2010, 8, 31, 10, 0, 0, 0)
      val now = new DateTime(2010, 8, 31, 10, 21, 0, 0)
      beater.shouldBeatNow(lastBeat.getMillis, now.getMillis) must be(true)
    }
    it("must return false if the last beat and now is both after today 10:20") {
      val lastBeat = new DateTime(2010, 8, 31, 10, 21, 0, 0)
      val now = new DateTime(2010, 8, 31, 10, 22, 0, 0)
      beater.shouldBeatNow(lastBeat.getMillis, now.getMillis) must be(false)
    }
    it("must return false if last beat and now is both before today 10:20") {
      val lastBeat = new DateTime(2010, 8, 30, 10, 21, 0, 0)
      val now = new DateTime(2010, 8, 31, 10, 19, 0, 0)
      beater.shouldBeatNow(lastBeat.getMillis, now.getMillis) must be(false)
    }
  }
}