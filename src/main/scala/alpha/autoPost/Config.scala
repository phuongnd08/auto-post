package alpha.autoPost

import reflect.BeanInfo

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
class SiteDescription {var url: String = _; var loginSteps: Array[String] = _; var postSteps: Array[String] = _; var logoutSteps: Array[String] = _}
@BeanInfo
class RepeatSchedule {var every: String = _}

@BeanInfo
class Config {
  var name: String = _
  var credential: Credential = _
  var siteDescriptions: Array[SiteDescription] = _
  var repeatSchedule: RepeatSchedule = _
  var fixedSchedule: Array[String] = _
  var contents: List[String] = _
}