package alpha.autoPost

import collection.mutable.Map
import collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 2, 2010
 * Time: 3:42:17 PM
 * To change this template use File | Settings | File Templates.
 */

case class Site(var name: String) {
  def this() = this (null)

  def url = "http://" + name

  var section: Section = _
  var loginSteps: Array[Array[String]] = _
  var postSteps: Array[Array[String]] = _
  var logoutSteps: Array[Array[String]] = _
  var specificInfo: Map[String, String] = _

  def info: Map[String, String] = {
    var map = Option(section).getOrElse(Section()).info
    if (map != null)
      Map[String, String]() ++ map ++ specificInfo
    else
      specificInfo.clone
  }

  def description: List[String] = {
    var list = List("Description for " + name)
    list = "\t- url: " + url :: list
    def phaseDescription(phaseName: String, steps: Array[Array[String]]): List[String] = {
      var phaseList: List[String] = Nil
      if ((steps != null) && (steps.length > 0)) {
        phaseList = "\t- " + phaseName :: phaseList
        steps.foreach(s => phaseList = "\t\t+ " + List(s.head, s.tail.mkString(", ")).mkString(" ") :: phaseList)
      } else {
        phaseList = "\t- no " + phaseName :: phaseList
      }
      phaseList
    }

    list = phaseDescription("login steps", loginSteps) ::: list
    list = phaseDescription("post steps", postSteps) ::: list
    list = phaseDescription("logout steps", logoutSteps) ::: list
    list.reverse
  }
}