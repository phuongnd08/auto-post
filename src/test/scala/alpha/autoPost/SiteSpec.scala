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
        "\t- url: http://5giay.vn",
        "\t- login steps",
        "\t\t+ open /",
        "\t\t+ clickAndWait button, 3000",
        "\t- post steps",
        "\t\t+ post /thread",
        "\t\t+ clickAndWait submit, 2000",
        "\t- logout steps",
        "\t\t+ open /logout",
        "\t\t+ clickAndWait button, 3000"))
    }

    it("must describe properly if many steps missed"){
      site.loginSteps = Array()
      site.postSteps = null
      site.logoutSteps = Array(Array("open", "/logout"))
      site.description must be (List(
        "Description for 5giay.vn",
        "\t- url: http://5giay.vn",
        "\t- no login steps",
        "\t- no post steps",
        "\t- logout steps",
        "\t\t+ open /logout"))
    }
  }
}