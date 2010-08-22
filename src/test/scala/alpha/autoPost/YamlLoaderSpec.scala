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

  describe("getJarPath") {
    it("must return current execution path") {
      yamlLoader.getJarPath must be === "/home/phuongnd08/code/auto-post/target/scala_2.8.0/classes/"
    }
  }


  describe("getConfigs") {
    it("must return sample config") {
      configList must not be (null)
      configList must have length (2)
      configList(0).name must be("sample_1")
      configList(1).name must be("sample_2")
    }

    describe("sample config 1") {
      it("must have correct credential") {
        sampleConfig.credential must not be (null)
        sampleConfig.credential.username must be("phuongnd08")
        sampleConfig.credential.password must be("1234567")
      }
      it("must have correct site descriptions"){
        sampleConfig.siteDescriptions must not be (null)
        sampleConfig.siteDescriptions must have length (2)
        val s1 = sampleConfig.siteDescriptions(0)
        s1.url must be ("http://batdongsan.com")
        s1.loginSteps must be (Array("openAndWait \"/login\"", "type \"username\", @username", "type \"password\", @password"))
        s1.postSteps must be (Array("openAndWait \"/post\"", "type \"save_date\", \"100\"", "click \"category\", \"1\"", "type \"umbala\", @content"))
        s1.logoutSteps must be (Array("openAndWait \"/logout\""))
      }
      it("must have correct repeated schedule"){
        val rs = sampleConfig.repeatSchedule
        rs must not be (null)
        rs.every must be ("2 hours")
      }
      it("must have correct fixed schedule"){
        val fs = sampleConfig.fixedSchedule
        fs must not be (Array("05:55", "10:55"))
      }
      it("must have correct contents"){
        var cnt = sampleConfig.contents
        cnt must not be (null)
        cnt must be (List("<strong>News No2</strong>", "<strong>News No1</strong>"))
      }
    }
  }
}
// vim: set ts=4 sw=4 et:
