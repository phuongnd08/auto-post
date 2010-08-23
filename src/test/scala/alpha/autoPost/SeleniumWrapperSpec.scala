package alpha.autoPost

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class SeleniumWrapperSpec extends Spec with MustMatchers {
  describe("control selenium server") {
    it("must start stop server properly") {
      SeleniumWrapper.serverStarted must be(false)
      SeleniumWrapper.startServer
      SeleniumWrapper.serverStarted must be(true)
      SeleniumWrapper.stopServer
      SeleniumWrapper.serverStarted must be(false)
    }
  }

  describe("run selenium command") {
    println("Start Server")
    println("SeleniumWrapper.serverStarted = " + SeleniumWrapper.serverStarted)
    SeleniumWrapper.startServer
    try {
      println("Start Testing")
      SeleniumWrapper.execute("http://google.com.vn") {
        processor => {
          processor.doCommand("open", Array("/"))
          processor.getString("getTitle", Array()) must be("Google")
        }
      }
      println("End Testing")
    }
    finally {
      println("Stop Server")
      SeleniumWrapper.stopServer
    }
  }
}