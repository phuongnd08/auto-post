package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito._
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.scalatest.concurrent.Conductor

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 24, 2010
 * Time: 10:34:08 PM
 * To change this template use File | Settings | File Templates.
 */

class SeleniumSmartControllerSpec extends Spec with MustMatchers with MockitoSugar with BeforeAndAfterEach {
  import Mockito._

  var smartController: SeleniumSmartController = _
  var timeProvider: TimeProvider = _
  var seleniumWrapper: SeleniumWrapper = _

  override def beforeEach {
    timeProvider = mock[TimeProvider]
    when(timeProvider.current).thenReturn(1)
    seleniumWrapper = mock[SeleniumWrapper]
    smartController = new SeleniumSmartController(timeProvider, seleniumWrapper, 20000)
  }

  describe("requestServer") {
    it("should start the server if not started") {
      when(seleniumWrapper.serverRunning).thenReturn(false)
      smartController.requestServer
      verify(seleniumWrapper).startServer
    }

    it("should change the lastUsed property") {
      when(timeProvider.current).thenReturn(10)
      smartController.requestServer
      smartController.getLastUsed must be(10)
    }

    it("should not start server in parallel") {
      import Matchers._
      when(seleniumWrapper.serverRunning).thenReturn(false, false)
      import org.mockito._
      import stubbing._
      import invocation._
      import Mockito._
      import Matchers._
      var time: List[Long] = Nil
      when(seleniumWrapper.startServer).thenAnswer(
        new Answer[AnyRef] {
          override def answer(invocation: InvocationOnMock): AnyRef = {
            time = (new java.util.Date).getTime :: time
            Thread.sleep(200)
            time = (new java.util.Date).getTime :: time
            return null
          }
        })
      val cor = new Conductor
      cor.thread("t1") {
        smartController.requestServer
      }

      cor.thread("t2") {
        smartController.requestServer
      }
      cor.conduct(100, 60)
      println(time)
      time(0) must be > time(1)
      time(1) must be > time(2)
      time(2) must be > time(3)
    }
  }
  describe("prune") {
    it("should not stop selenium server when server is just accessed") {
      when(seleniumWrapper.serverRunning).thenReturn(true, false)
      when(timeProvider.current).thenReturn(20000)
      smartController.prune
      verify(seleniumWrapper, never).stopServer
    }

    it("should stop selenium server when server is not accessed for long time") {
      when(seleniumWrapper.serverRunning).thenReturn(true, false)
      when(timeProvider.current).thenReturn(20002)
      smartController.prune
      verify(seleniumWrapper).stopServer
    }
  }

}