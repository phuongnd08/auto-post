package alpha.autoPost

import com.thoughtworks.selenium.CommandProcessor
import util.matching.Regex
import scala.collection.mutable.Map
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 2, 2010
 * Time: 7:22:50 PM
 * To change this template use File | Settings | File Templates.
 */

object Queue {
  def variableRegex = new Regex("\\@(\\w+)")

  def realizedVariable(variable: String, info: Map[String, String], article: Article): String = {
    if (variable.startsWith("@")) {
      val variableName = variable.substring(1)
      return variableName match {
        case "title" => article.title
        case "content" => article.content
        case "htmlContent" => article.htmlContent
        case _ => if (info.contains(variableName)) info(variableName) else variable
      }
    }
    variable
  }

  def realizedItem(item: String, info: Map[String, String], article: Article) = {
    variableRegex.replaceAllIn(item, `match` => realizedVariable(`match`.matched, info, article))
  }

  def realizedCommand(command: Array[String], info: Map[String, String], article: Article): Array[String] = {
    command.map(cmd => realizedItem(cmd, info, article))
  }
}

case class Queue(val section: Section, val site: Site) {
  def run(processor: CommandProcessor, dump: String => Unit) {
    def safeExecute(steps: Array[Array[String]], a: Article, dumpLocation: Boolean): Boolean = {
      Option(steps).getOrElse(Array[Array[String]]()).map(Queue.realizedCommand(_, site.info, a))
              .foreach(arr => {
        try {
          processor.doCommand(arr.head, arr.tail)
        } catch {
          case ex: Exception => {
            println("Exception while trying to run [" + arr.mkString(", ") + "]")
            ex.printStackTrace
            println("Execution on [" + site.name + "] of [" + section.name + "] terminated")
            return false
          }
        }
      })
      if (dumpLocation)
        dump(processor.getString("getLocation", Array[String]()))
      true
    }
    section.articles.foreach(a => {
      for (steps <- Array(site.loginSteps, site.postSteps, site.logoutSteps))
        if (!safeExecute(steps, a, steps eq site.postSteps)) return
    })
  }

  override def toString = "[" + site.name + "]@[" + section.name + "]"
}