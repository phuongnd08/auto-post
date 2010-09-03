package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.joda.time.DateTime
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class ConfigSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var config: Config = _

  override def beforeEach {
    config = new Config
    config.name = "sample_1"
  }

  describe("getBeaters") {
    it("must return properly if both every and fixedSchedule are set") {
      config.every = "10:20"
      config.fixedSchedule = Array("5:20", "6:30")
      config.beaters must be(List(CyclicBeater((10 * 3600 + 20 * 60) * 1000), ScheduledBeater(5, 20), ScheduledBeater(6, 30)))
    }
    it("must return properly if neither of every or fixedSchedule presents") {
      config.every = null
      config.fixedSchedule = null
      config.beaters must be(List[Beater]())
    }
  }

  describe("shouldRunNow") {
    it("must be true if one beater shouldBeatNow") {
      config.every = "20:00"
      config.fixedSchedule = Array("5:20")
      config.shouldRunNow(
        new DateTime(2010, 9, 1, 5, 19, 0, 0).getMillis,
        new DateTime(2010, 9, 1, 5, 21, 0, 0).getMillis) must be(true)
    }

    it("must be false if none beater shouldBeatNow") {
      config.every = "20:00"
      config.fixedSchedule = Array("5:20")
      config.shouldRunNow(
        new DateTime(2010, 9, 1, 5, 21, 0, 0).getMillis,
        new DateTime(2010, 9, 1, 5, 31, 0, 0).getMillis) must be(false)
    }
  }

  describe("description") {
    it("must render proper description if all properties assigned") {
      config.every = "20:00"
      config.fixedSchedule = Array("5:20", "7:20")
      config.info = Map[String, String]("username" -> "phuong", "password" -> "123456", "rememberMe" -> "no")
      config.articles = List(Article("a", "A"), Article("b", "B"))
      config.sites = List(Site("5giay.vn"), Site("batdongsan.com"), Site("raovat.com"))
      config.description must be(List(
        "Configuration for sample_1",
        "\t- info",
        "\t\t+ username: phuong",
        "\t\t+ password: 123456",
        "\t\t+ rememberMe: no",
        "\t- repeated every 20:00",
        "\t- fixed schedules",
        "\t\t+ 5:20",
        "\t\t+ 7:20",
        "\t- sites",
        "\t\t+ 5giay.vn",
        "\t\t+ batdongsan.com",
        "\t\t+ raovat.com",
        "\t- articles",
        "\t\t+ a",
        "\t\t+ b"
        ))
    }

    it("must render proper description if many properties missed") {
      config.every = null
      config.articles = List[Article]()
      config.description must be(List(
        "Configuration for sample_1",
        "\t- no info",
        "\t- no repeated schedules",
        "\t- no fixed schedules",
        "\t- no sites",
        "\t- no articles"))
    }
  }

  describe("briefDescription") {
    it("should render proper brief description if sites assigned") {
      config.sites = List(Site("5giay.vn"), Site("batdongsan.com"), Site("raovat.com"))
      config.briefDescription must be(List(
        "sample_1",
        "\t- sites",
        "\t\t+ 5giay.vn",
        "\t\t+ batdongsan.com",
        "\t\t+ raovat.com"
        ))
    }

    it("should render proper brief description if sites not assigned") {
      config.sites = List()
      config.briefDescription must be(List(
        "sample_1",
        "\t- no sites"
        ))
    }

    it("should render proper brief description if sites assigned with empty list") {
      config.sites = null
      config.briefDescription must be(List(
        "sample_1",
        "\t- no sites"
        ))
    }
  }


  describe("siteByName") {
    it("must return None if no site found") {
      config.sites = null
      config.siteByName("some_name") must be(None)
    }
  }
}
