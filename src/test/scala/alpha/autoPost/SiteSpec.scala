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

class SiteSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var site: Site = _

  override def beforeEach {
    site = new Site
    site.name = "5giay.vn"
  }

  describe("description") {
    it("must describe properly all steps filled") {
      site.loginSteps = Array(Array("open", "/"), Array("clickAndWait", "button", "3000"))
      site.postSteps = Array(Array("post", "/thread"), Array("clickAndWait", "submit", "2000"))
      site.logoutSteps = Array(Array("open", "/logout"), Array("clickAndWait", "button", "3000"))
      site.description must be (List(
        "Description for 5giay.vn",
        " - url: http://5giay.vn",
        " - login steps",
        "  + open /",
        "  + clickAndWait button, 3000",
        " - post steps",
        "  + post /thread",
        "  + clickAndWait submit, 2000",
        " - logout steps",
        "  + open /logout",
        "  + clickAndWait button, 3000"))
    }

    it("must describe properly if many steps missed"){
      site.loginSteps = Array()
      site.postSteps = null
      site.logoutSteps = Array(Array("open", "/logout"))
      site.description must be (List(
        "Description for 5giay.vn",
        " - url: http://5giay.vn",
        " - no login steps",
        " - no post steps",
        " - logout steps",
        "  + open /logout"))
    }
  }
}