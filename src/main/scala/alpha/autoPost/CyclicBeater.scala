package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 31, 2010
 * Time: 10:40:34 PM
 * To change this template use File | Settings | File Templates.
 */

class CyclicBeater(val interval:Long) extends Beater{
  override def shouldBeatNow(lastBeat:Long, now:Long)= now > lastBeat + interval
}