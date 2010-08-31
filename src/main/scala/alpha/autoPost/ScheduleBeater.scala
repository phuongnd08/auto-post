package alpha.autoPost

import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 31, 2010
 * Time: 10:40:10 PM
 * To change this template use File | Settings | File Templates.
 */

class ScheduledBeater(val hour:Int, val minute:Int) extends Beater{
  def shouldBeatNow(lastBeat:Long, now:Long) = {
    val correctMoment = new DateTime(now).withHourOfDay(hour).withMinuteOfHour(minute).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
    lastBeat < correctMoment && correctMoment < now
  }
}