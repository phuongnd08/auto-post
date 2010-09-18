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
  val DefaultPort = 4444
  val DefaultHost = "localhost"
  val DefaultBrowser = "*firefox"
  var RecheckInterval = 2222
}

class SeleniumWrapper {
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


  def serverRunning: Boolean = {
    var socket: ServerSocket = null
    try {
      socket = new ServerSocket(SeleniumWrapper.DefaultPort);
      print(".")
      return false
    } catch {
      case e: IOException => {
        println("ok")
        return true
      }
    } finally {
      if (socket != null)
        socket.close()
    }
  }

  def startServer {
    val jarPath = Environment.getJarPath + "/tools/selenium-server.jar"
    spawnJar(jarPath)
    waitForServerStarted
  }

  def waitForServerStarted {
    while (!serverRunning) {
      Thread.sleep(SeleniumWrapper.RecheckInterval)
    }
  }

  def stopServer {
    if (serverRunning) {
      new URL("http://localhost:" + SeleniumWrapper.DefaultPort + "/selenium-server/driver/?cmd=shutDownSeleniumServer").openConnection.getContent
      waitForServerStopped
    }
    Thread.sleep(1450)
  }


  def waitForServerStopped {
    while (serverRunning) {
      Thread.sleep(SeleniumWrapper.RecheckInterval)
    }
  }


  def execute(url: String)(operation: (CommandProcessor) => Unit) {
    val selenium = new DefaultSelenium(SeleniumWrapper.DefaultHost, SeleniumWrapper.DefaultPort, SeleniumWrapper.DefaultBrowser, url)
    selenium.start()
    val cmdProcessor = ReflectUtils.getInstance.getFieldValue(selenium, "commandProcessor").asInstanceOf[CommandProcessor]
    operation(cmdProcessor)
    selenium.stop()
  }
}