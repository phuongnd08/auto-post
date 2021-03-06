package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 24, 2010
 * Time: 10:16:42 PM
 * To change this template use File | Settings | File Templates.
 */

object SeleniumSmartController {
  val DefaultMaxServerIdle = 5 * 60 * 1000
}

class SeleniumSmartController(val timeProvider: TimeProvider, val seleniumWrapper: SeleniumWrapper, val maxServerIdle: Int) {
  def this(timeProvider: TimeProvider, seleniumWrapper: SeleniumWrapper) = this (timeProvider, seleniumWrapper, SeleniumSmartController.DefaultMaxServerIdle)

  protected var lastUsed = timeProvider.current
  protected var serverKickedOn = false

  def getLastUsed = lastUsed

  def requestServer {
    lastUsed = timeProvider.current
    turnServerOnOff(true)
  }

  def turnServerOnOff(on: Boolean) = synchronized {
    if (on)
      {if (!seleniumWrapper.serverRunning) seleniumWrapper.startServer}
    else
      {if (seleniumWrapper.serverRunning) seleniumWrapper.stopServer}
  }


  def prune {
    println("lastUsed =" + lastUsed)
    println("env.timeProvider.current =" + timeProvider.current)
    println("maxServerIdle =" + maxServerIdle)
    if (timeProvider.current - lastUsed > maxServerIdle) {
      turnServerOnOff(false)
    }
  }
}