package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import actors.Actor._
import org.scalatest.mock.MockitoSugar
import com.thoughtworks.selenium.CommandProcessor
import scala.collection.mutable.Map
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueSpec extends Spec with MustMatchers with BeforeAndAfterEach with MockitoSugar {
  var config: Config = _
  var queue: Queue = _

  override def beforeEach {
    config = new Config
    config.info = Map("username" -> "phuongnd08", "password" -> "12345")
    config.articles = List(Article("News 1", "This is content of news 1"))
    val s1 = Site("5giay.vn")
    s1.loginSteps = Array(Array("open", "/"), Array("type", "username", "@username"), Array("type", "password", "@password"))
    s1.postSteps = Array(Array("open", "/post"), Array("type", "title", "@title"), Array("type", "body", "body\\\\@content"))
    s1.logoutSteps = Array(Array("open", "/logout"))
    config.sites = List(s1)
    queue = Queue(config, s1)
  }
  describe("realizedVariable") {
    it("should substitue @title variable as real value") {
      Queue.realizedVariable("@title", config.info, config.articles(0)) must be("News 1")
    }
  }
  describe("realizedItem") {
    it("should realize all variable") {
      Queue.realizedItem("user @username @password @title => @content", config.info, config.articles(0)) must be("user phuongnd08 12345 News 1 => This is content of news 1")
    }

  }
  describe("realizedCommand") {
    it("should realized all command") {
      Queue.realizedCommand(config.sites(0).postSteps(1), config.info, config.articles(0)) must be(Array[String]("type", "title", "News 1"))
    }
  }

  describe("run") {
    it("should execute all realized command in order") {
      def executedCommands: List[List[String]] = {
        val processor = mock[CommandProcessor]
        import org.mockito._
        import stubbing._
        import invocation._
        import Mockito._
        import Matchers._
        var commands: List[List[String]] = Nil
        when(processor.doCommand(anyString, any[Array[String]])).thenAnswer(
          new Answer[String] {
            override def answer(invocation: InvocationOnMock): String = {
              val args = invocation.getArguments.asInstanceOf[Array[AnyRef]]
              val command = (args(0).asInstanceOf[String] :: args(1).asInstanceOf[Array[String]].toList).map(s => s.toString)
              commands = command :: commands
              return null
            }
          })

        queue.run(processor)
        commands.reverse
      }
      executedCommands must be(List(
        List("open", "/"),
        List("type", "username", "phuongnd08"),
        List("type", "password", "12345"),
        List("open", "/post"),
        List("type", "title", "News 1"),
        List("type", "body", "body\\\\This is content of news 1"),
        List("open", "/logout")
        ))
    }
  }
}