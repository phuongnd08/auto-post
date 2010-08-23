package alpha.autoPost

import java.net.{ServerSocket, URL}
import java.io._
import scala.concurrent.ops._
import com.thoughtworks.selenium.{CommandProcessor, DefaultSelenium, Selenium}
import org.azeckoski.reflectutils.ReflectUtils

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:06:34 PM
 * To change this template use File | Settings | File Templates.
 */

object SeleniumWrapper {
  val SELENIUM_PORT = 4444
  val SELENIUM_HOST = "localhost"
  val SELENIUM_DEFAULT_BROWSER = "*firefox"
  var RECHECK_INTERVAL = 500

  protected def spawnJar(jarPath: String) {
    if ((new File(jarPath)).exists) {
      spawn {
        var process = Runtime.getRuntime.exec("java -jar " + jarPath)
        var input = new BufferedReader(new InputStreamReader(process.getInputStream));
        var line = ""
        while (line != null) {
          println(line)
          line = input.readLine
        }
        input.close()
      }
    }
    else throw new Exception(jarPath + " not found")
  }

  def startServer {
    println("def startServer")
    var serverKicked = false;
    while (!serverStarted) {
      if (!serverKicked) {
        val jarPath = Environment.getJarPath + "/tools/selenium-server.jar"
        spawnJar(jarPath)
      }
      serverKicked = true
      Thread.sleep(RECHECK_INTERVAL)
    }
  }

  def serverStarted: Boolean = {
    var socket: ServerSocket = null
    try {
      socket = new ServerSocket(SELENIUM_PORT);
      println("Server is not started")  
      return false
    } catch {
      case e: IOException => {
        println("Server started")          
        return true
      }
    } finally {
      if (socket != null)
        socket.close()
    }
  }

  def stopServer {
    new URL("http://localhost:" + SELENIUM_PORT + "/selenium-server/driver/?cmd=shutDownSeleniumServer").openConnection.getContent
    while (serverStarted) {
      Thread.sleep(RECHECK_INTERVAL)
    }
  }

  def execute(url: String)(operation: (CommandProcessor) => Unit) {
    val selenium = new DefaultSelenium(SELENIUM_HOST, SELENIUM_PORT, SELENIUM_DEFAULT_BROWSER, url)
    selenium.start()
    val cmdProcessor = ReflectUtils.getInstance.getFieldValue(selenium, "commandProcessor").asInstanceOf[CommandProcessor]
    operation(cmdProcessor)
    selenium.stop()
  }
}