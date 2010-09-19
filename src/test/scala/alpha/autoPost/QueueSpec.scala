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
  var section: Section = _
  var queue: Queue = _
  var dumped: List[String] = _
  var site: Site = _

  override def beforeEach {
    section = new Section
    section.info = Map("username" -> "phuongnd08")
    section.articles = List(Article("News 1", "This is content of [b]news 1[/b]"))
    site = Site("5giay.vn")
    site.specificInfo = Map("password" -> "12345")
    site.loginSteps = Array(Array("open", "/"), Array("type", "username", "@username"), Array("type", "password", "@password"))
    site.postSteps = Array(Array("open", "/post"), Array("type", "title", "@title"), Array("type", "//body", "@content"))
    site.logoutSteps = Array(Array("open", "/logout"))
    section.sites = List(site)
    dumped = Nil
    queue = Queue(section, site)
  }
  describe("realizedVariable") {
    it("should substitue @title variable as real value") {
      Queue.realizedVariable("@title", site.info, section.articles(0)) must be("News 1")
    }
  }
  describe("realizedItem") {
    it("should realize all variable") {
      Queue.realizedItem("user @username @password @title => @content html => @htmlContent", site.info, section.articles(0)) must be("user phuongnd08 12345 News 1 => This is content of [b]news 1[/b] html => This is content of <span style=\"font-weight:bold;\">news 1</span>")
    }

  }
  describe("realizedCommand") {
    it("should realized all command") {
      Queue.realizedCommand(section.sites(0).postSteps(1), site.info, section.articles(0)) must be(Array[String]("type", "title", "News 1"))
    }
  }

  describe("run") {
    it("should execute all realized command in order and dump url out") {
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

        when(processor.getString(Matchers.eq("getLocation"), any[Array[String]] )).thenReturn("http://somewhere.com")
        queue.run(processor, s => dumped = s :: dumped)
        commands.reverse
      }
      executedCommands must be(List(
        List("open", "/"),
        List("type", "username", "phuongnd08"),
        List("type", "password", "12345"),
        List("open", "/post"),
        List("type", "title", "News 1"),
        List("type", "//body", "This is content of [b]news 1[/b]"),
        List("open", "/logout")
        ))

      dumped must be(List("http://somewhere.com"))
    }
    it("should exit gracefully on exception") {
      val processor = mock[CommandProcessor]
      import org.mockito._
      import stubbing._
      import invocation._
      import Mockito._
      import Matchers._
      var commands: List[List[String]] = Nil
      when(processor.doCommand(anyString, any[Array[String]])).thenThrow(new RuntimeException)
      queue.run(processor, null)
    }

  }
}