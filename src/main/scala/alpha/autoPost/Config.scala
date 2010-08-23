package alpha.autoPost

import reflect.BeanInfo
import java.util.Map

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 12:10:31 PM
 * To change this template use File | Settings | File Templates.
 */

@BeanInfo
class Credential {var username: String = _; var password: String = _}
@BeanInfo
class Site{
  var name: String = _;
  def url = "http://" + name
  var loginSteps: Array[Array[String]] = _; var postSteps: Array[Array[String]] = _; var logoutSteps: Array[Array[String]] = _}
@BeanInfo
class RepeatSchedule {var every: String = _}

@BeanInfo
class Config {
  var name: String = _
  var info: Map[String, String] = _
  var sites: List[Site] = _
  var repeatSchedule: RepeatSchedule = _
  var fixedSchedule: Array[String] = _
  var contents: List[String] = _
}