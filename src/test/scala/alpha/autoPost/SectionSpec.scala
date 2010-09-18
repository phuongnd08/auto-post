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

class SectionSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var section: Section = _

  override def beforeEach {
    section = new Section
    section.name = "sample_1"
  }

  describe("getBeaters") {
    it("must return properly if both every and fixedSchedule are set") {
      section.every = "10:20"
      section.fixedSchedule = Array("5:20", "6:30")
      section.beaters must be(List(CyclicBeater((10 * 3600 + 20 * 60) * 1000), ScheduledBeater(5, 20), ScheduledBeater(6, 30)))
    }
    it("must return properly if neither of every or fixedSchedule presents") {
      section.every = null
      section.fixedSchedule = null
      section.beaters must be(List[Beater]())
    }
  }

  describe("shouldRunNow") {
    it("must be true if one beater shouldBeatNow") {
      section.every = "20:00"
      section.fixedSchedule = Array("5:20")
      section.shouldRunNow(
        new DateTime(2010, 9, 1, 5, 19, 0, 0).getMillis,
        new DateTime(2010, 9, 1, 5, 21, 0, 0).getMillis) must be(true)
    }

    it("must be false if none beater shouldBeatNow") {
      section.every = "20:00"
      section.fixedSchedule = Array("5:20")
      section.shouldRunNow(
        new DateTime(2010, 9, 1, 5, 21, 0, 0).getMillis,
        new DateTime(2010, 9, 1, 5, 31, 0, 0).getMillis) must be(false)
    }
  }

  describe("description") {
    it("must render proper description if all properties assigned") {
      section.every = "20:00"
      section.fixedSchedule = Array("5:20", "7:20")
      section.info = Map[String, String]("username" -> "phuong", "password" -> "123456", "rememberMe" -> "no")
      section.articles = List(Article("a", "A"), Article("b", "B"))
      section.sites = List(Site("5giay.vn"), Site("batdongsan.com"), Site("raovat.com"))
      section.description must be(List(
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
      section.every = null
      section.articles = List[Article]()
      section.description must be(List(
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
      section.sites = List(Site("5giay.vn"), Site("batdongsan.com"), Site("raovat.com"))
      section.briefDescription must be(List(
        "sample_1",
        "\t- sites",
        "\t\t+ 5giay.vn",
        "\t\t+ batdongsan.com",
        "\t\t+ raovat.com"
        ))
    }

    it("should render proper brief description if sites not assigned") {
      section.sites = List()
      section.briefDescription must be(List(
        "sample_1",
        "\t- no sites"
        ))
    }

    it("should render proper brief description if sites assigned with empty list") {
      section.sites = null
      section.briefDescription must be(List(
        "sample_1",
        "\t- no sites"
        ))
    }
  }


  describe("siteByName") {
    it("must return None if no site found") {
      section.sites = null
      section.siteByName("some_name") must be(None)
    }
  }

  describe("assign Sites") {
    it("should assign section to site") {
      section.sites = List(Site("5giay.vn"))
      section.sites(0).section must be(section)
    }
  }
}
