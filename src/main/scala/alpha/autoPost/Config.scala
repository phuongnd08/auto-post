package alpha.autoPost

import reflect.BeanInfo
import java.util.Map
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 12:10:31 PM
 * To change this template use File | Settings | File Templates.
 */

@BeanInfo
case class Config {
  var name: String = _
  var info: Map[String, String] = _
  var sites: List[Site] = _
  var every: String = _
  var fixedSchedule: Array[String] = _

  def siteByName(name: String): Option[Site] = {
    Option(sites).getOrElse(List[Site]()).find(s => s.name == name)
  }

  def beaters: List[Beater] = {
    var list = List[Beater]()
    if (every != null) {
      list = new CyclicBeater(Beater.strToInterval(every)) :: list
    }
    list ::: Option(fixedSchedule).getOrElse(Array[String]()).toList.map(s => new ScheduledBeater(Beater.strToHourAndMinute(s)))
  }

  def shouldRunNow(lastRun: Long, now: Long): Boolean = {
    beaters.exists(beater => beater.shouldBeatNow(lastRun, now))
  }

  var articles: List[Article] = _

  def description: List[String] = {
    var list = List("Configuration for " + name)
    if (info != null) {
      list = "\t- info" :: list
      for ((k, v) <- info) {list = ("\t\t+ " + k + ": " + v) :: list}
    } else list = "\t- no info" :: list
    if (every != null)
      list = "\t- repeated every " + every :: list
    else list = "\t- no repeated schedules" :: list

    def getList(name: String, sequence: List[AnyRef])(getText: AnyRef => String): List[String] = {
      var list: List[String] = Nil
      if (sequence != null && sequence.length > 0) {
        list = "\t- " + name :: list
        list = sequence.map(item => "\t\t+ " + getText(item)).reverse ::: list
      } else list = "\t- no " + name :: list
      list
    }

    list = getList("fixed schedules", Option(fixedSchedule).getOrElse(Array[String]()).toList) {fs => fs.asInstanceOf[String]} ::: list
    println("assign sites")
    list = getList("sites", sites) {s => s.asInstanceOf[Site].name} ::: list
    list = getList("articles", articles) {a => a.asInstanceOf[Article].title} ::: list
    list.reverse
  }

  def briefDescription: List[String] = {
    var list = List(name)
    if (sites != null && sites.length > 0) {
      list = "\t- sites" :: list
      sites.foreach(s => list = "\t\t+ " + s.name :: list)
    } else list = "\t- no sites" :: list
    list.reverse
  }
}