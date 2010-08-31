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

class BeaterSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  describe("strToHourAndMinute") {
    it("must return proper hour and minute") {
      val hourAndMinute = Beater.strToHourAndMinute("10:20")
      hourAndMinute.hour must be (10)
      hourAndMinute.minute must be (20)
    }
  }
  describe("strToInterval"){
    it("must return proper interval"){
      Beater.strToInterval("0:15") must be (15*60*1000)
      Beater.strToInterval("1:15") must be (3600 * 1000 + 15*60*1000)
    }
  }
}