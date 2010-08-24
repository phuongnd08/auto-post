package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import org.scalatest.{BeforeAndAfterEach, Spec}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 24, 2010
 * Time: 10:34:08 PM
 * To change this template use File | Settings | File Templates.
 */

class SeleniumSmartControllerSpec extends Spec with MustMatchers with MockitoSugar with BeforeAndAfterEach{
  import Mockito._

  var smartController: SeleniumSmartController = _
  var timeProvider: TimeProvider = _
  var seleniumWrapper: SeleniumWrapper = _

  override def beforeEach{
    timeProvider = mock[TimeProvider]
    when(timeProvider.current).thenReturn(1)
    seleniumWrapper = mock[SeleniumWrapper]
    smartController = new SeleniumSmartController(this, 20000)
  }

  describe("requestServer"){
    it("should start the server if not started"){
      when(seleniumWrapper.serverStarted).thenReturn(false)
      smartController.requestServer
      verify(seleniumWrapper).startServer
    }

    it("should change the lastUsed property"){
      when(timeProvider.current).thenReturn(10)
      smartController.requestServer
      smartController.getLastUsed must be (10)
    }
  }
  describe("prune"){
    it("should not stop selenium server when server is just accessed"){
      when(timeProvider.current).thenReturn(20000)
      smartController.prune
      verify(seleniumWrapper, never).stopServer
    }

    it("should stop selenium server when server is not accessed for long time"){
      when(timeProvider.current).thenReturn(20002)
      smartController.prune
      verify(seleniumWrapper).stopServer
    }
  }

}