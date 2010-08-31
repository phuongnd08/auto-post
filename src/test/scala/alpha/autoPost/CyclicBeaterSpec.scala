package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class CyclicBeaterSpec extends Spec with MustMatchers with BeforeAndAfterEach{
  var beater: Beater = _

  override def beforeEach {
    beater = (new CyclicBeater(1000 * 60))
  }
  describe("shouldBeatNow") {
    it("must return true if the last beat is too far until now") {
      beater.shouldBeatNow(1, 2 + 1000 * 60) must be(true)
    }
    it("must return false if the last beat is too near until now") {
      beater.shouldBeatNow(1, 1000 * 60) must be(false)
    }
  }
}