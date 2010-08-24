package alpha.autoPost

import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 24, 2010
 * Time: 9:42:02 PM
 * To change this template use File | Settings | File Templates.
 */

class TimeProvider{
  def current = (new DateTime).getMillis
}