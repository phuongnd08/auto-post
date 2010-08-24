package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 24, 2010
 * Time: 10:16:42 PM
 * To change this template use File | Settings | File Templates.
 */

class SeleniumSmartController(env: {val timeProvider: TimeProvider; val seleniumWrapper: SeleniumWrapper}, val maxServerIdle: Int){
  protected var lastUsed = env.timeProvider.current
  def getLastUsed=lastUsed
  def requestServer  {
    lastUsed = env.timeProvider.current
    if (!env.seleniumWrapper.serverStarted)
      env.seleniumWrapper.startServer
  }

  def prune{
    println("lastUsed =" + lastUsed)
    println("env.timeProvider.current ="+env.timeProvider.current)
    println("maxServerIdle ="+maxServerIdle)
    if (env.timeProvider.current - lastUsed> maxServerIdle)
      env.seleniumWrapper.stopServer 
  }
}