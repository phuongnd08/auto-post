package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 31, 2010
 * Time: 10:40:47 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class Beater {
  def shouldBeatNow(lastBeat: Long, now: Long): Boolean
}

object Beater {
  def strToInterval(str: String) = {
    val hourAndMinute = strToHourAndMinute(str)
    (hourAndMinute.hour * 3600 + hourAndMinute.minute * 60) * 1000
  }

  def strToHourAndMinute(str: String): {val hour: Int; val minute: Int} = {
    val parts = str.split(":")
    new {
      val hour = parts(0).toInt;
      val minute = parts(1).toInt
    }
  }
}