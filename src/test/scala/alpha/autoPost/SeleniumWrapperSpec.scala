package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.concurrent.Conductor
import org.scalatest.{BeforeAndAfterEach, Spec}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class SeleniumWrapperSpec extends Spec with MustMatchers with BeforeAndAfterEach{
  var seleniumWrapper: SeleniumWrapper = _

  override def beforeEach{
    seleniumWrapper = new SeleniumWrapper
  }

  describe("control selenium server") {
    it("must start stop server properly") {
      seleniumWrapper.serverStarted must be(false)
      seleniumWrapper.startServer
      seleniumWrapper.serverStarted must be(true)
      seleniumWrapper.stopServer
      seleniumWrapper.serverStarted must be(false)
    }
  }

  describe("run selenium command") {
    it("should execute command properly") {
      println("Start Server")
      println("SeleniumWrapper.serverStarted = " + seleniumWrapper.serverStarted)
      seleniumWrapper.startServer
      try {
        println("Start Testing")
        seleniumWrapper.execute("http://google.com.vn") {
          processor => {
            processor.doCommand("open", Array("/"))
            processor.getString("getTitle", Array()) must be("Google")
          }
        }
        println("End Testing")
      }
      finally {
        println("Stop Server")
        seleniumWrapper.stopServer
      }
    }

    it("should execute command properly in PARALELL") {
      println("Start Server")
      seleniumWrapper.startServer
      try {
        val cor = new Conductor
        cor.thread("google stuff") {
          seleniumWrapper.execute("http://google.com.vn") {
            processor => {
              processor.doCommand("open", Array("/"))
              processor.getString("getTitle", Array()) must be("Google")
            }
          }
        }

        cor.thread("vnexpess stuff") {
          seleniumWrapper.execute("http://vnexpress.net") {
            processor => {
              processor.doCommand("open", Array("/"))
              processor.getString("getTitle", Array()) must be("VnExpress - Daily News")
            }
          }
        }

        cor.conduct(100, 60)
      }
      finally {
        println("Stop Server")
        seleniumWrapper.stopServer
      }
    }
  }
}