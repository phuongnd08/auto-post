package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}

class YamlLoaderSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var yamlLoader: YamlLoader = _

  var configList: List[Config] = _
  var sampleConfig: Config = _

  override def beforeEach {
    yamlLoader = new YamlLoader
    configList = yamlLoader.getConfigs
    sampleConfig = configList(0)
  }
  describe("getConfigs") {
    it("must return sample config") {
      configList must not be (null)
      configList must have length (2)
      configList(0).name must be("sample_1")
      configList(1).name must be("sample_2")
    }

    describe("sample config 1") {
      it("must have correct info") {
        val info = sampleConfig.info
        info must not be (null)
        info.get("username") must be("phuongnd08")
        info.get("password") must be("1234567")
        info.get("title") must be("Morgage news")
      }
      it("must have correct site descriptions") {
        val sites = sampleConfig.sites
        sites must not be (null)
        sites must have length (2)
        sites(0).name must be ("batdongsan.com")
        sites(1).name must be ("5giay.vn")
        val s1 = sites(1)
        s1.url must be("http://5giay.vn")
        s1.loginSteps must be(Array(
          Array("open", "/index.php", ""),
          Array("type", "navbar_username", "@username"),
          Array("type", "navbar_password", "@password"),
          Array("clickAndWait", "//input[@value='Ðăng Nhập']", "")))

        s1.postSteps must be(Array(
          Array("openAndWait", "/newthread.php?do=newthread&f=44", ""),
          Array("select", "prefixid", "label=HCM -"),
          Array("click", "//option[@value='HCM']", ""),
          Array("type", "subject", "@title"),
          Array("selectFrame", "vB_Editor_001_iframe", ""),
          Array("type", "//body", "@content")))
                 
        s1.logoutSteps must be(Array(
          Array("open", "/", ""),
          Array("click", "link=Thoát", ""),
          Array("assertConfirmation", "Bạn có chắc chắn là bạn thoát khỏi diễn đàn không ?", "")))
      }
      it("must have correct repeated schedule") {
        configList(0).every must be("00:01")
        configList(1).every must be(null)
      }
      it("must have correct fixed schedule") {
        configList(0).fixedSchedule must be (null)
        configList(1).fixedSchedule must be (Array("22:20", "22:24"))
      }
      it("must have correct articles") {
        var articles = sampleConfig.articles
        articles must not be (null)
        articles must be(List(
          Article("news_1", "<strong>News No1</strong>"),
          Article("news_2", "<strong>News No2</strong>")))
      }
    }
  }
}
// vim: set ts=4 sw=4 et:
