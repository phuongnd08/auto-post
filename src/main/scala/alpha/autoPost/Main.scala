package alpha.autoPost

import java.io._
import scala.io._
import scala.collection.immutable.List
import scala.xml.parsing.XhtmlParser

import org.yaml.snakeyaml.Yaml

object Main {
  def printHelp {
    println("Auto Post Application")
    println("Written by phuongnd08")
    println("Command available")
    println("  help : Display this help")
    println("  reload : Reload the configuration")
    println("  list : List available configuration")
    println("  detail 5giay.vn : Print the detail configuration for 5giay.vn (if exists)")
    println("  test 5giay.vn: try posting into a 5giay.vn")
    println("  exit : quit the program")
  }

  def printHint {
    println("Type help to know how to use")
  }

  var configurations: List[Config] = _
  lazy val yamlLoader: YamlLoader = new YamlLoader

  def loadConfiguration {
    configurations = yamlLoader.getConfigs
    println("Configuration loaded")
  }

  def listConfiguration {
    println("Available configurations")
    for (c <- configurations) {
      println("- " + c.name)
    }
  }

  def runConfiguration(name: String) {
    var c = configurations.find((config) => config.name == name)
    if (c != None) runConfiguration(c.get)
    else println("Configuration of name " + name + " not found")
  }

  def runConfiguration(config: Config) {
    println("Start executing " + config.name)
    println("End executing " + config.name)
  }

  def main(args: Array[String]) {
    printHint
    val dataIn = new BufferedReader(new InputStreamReader(System.in))
    while (true) {
      print("> ")
      val line = dataIn.readLine
      line match {
        case "help" => printHelp
        case "exit" => return
        case "list" => println("Let's list all configuration")
        case _ => println("What the hell are you trying to do?")
      }
    }
  }
}