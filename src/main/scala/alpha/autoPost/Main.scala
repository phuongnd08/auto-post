package alpha.autoPost

import java.io._
import scala.io._
import scala.collection.immutable.List
import scala.xml.parsing.XhtmlParser

import org.yaml.snakeyaml.Yaml
import actors.Actor
import actors.Actor._


object Main {
  def printHelp {
    println("Auto Post Application")
    println("Written by phuongnd08")
    println("Command available")
    println("  help : Display this help")
    println("  reload : Reload the configuration")
    println("  list : List available configuration")
    println("  detail sample_1: Print the detail configuration for sample_1(if exists)")
    println("  detail sample_1 5giay.vn: Print the detail configuration for 5giay.vn inside sample_1(if exists)")
    println("  test sample_1: Try to post into all site of inside sample_1")
    println("  test sample_1 5giay.vn: try posting into a 5giay.vn using configuration inside sample_1")
    println("  exit : quit the program")
  }

  def printHint {
    println("Type help to know how to use")
  }

  var configurations: List[Config] = _
  lazy val yamlLoader: YamlLoader = new YamlLoader

  def reload {
    configurations = yamlLoader.getConfigs
    println("Configuration loaded")
  }

  def list {
    println("Available configurations")
    configurations.map(c => c.briefDescription.mkString("\n")).foreach(println)
  }

  protected def conductIfConfigExists(name: String)(body: Config => Unit) {
    var config = configurations.find((config) => config.name == name)
    if (config != None) {
      body(config.get)
    }
    else
      println("Configuration of name " + name + " not found")
  }

  protected def conductIfSiteExists(config: Config, siteName: String)(body: Site => Unit) {
    var site = config.siteByName(siteName)
    if (site != None) {
      body(site.get)
    }
    else
      println("Site of name " + siteName + " not found inside " + config.name)
  }

  protected def conductIfNumberOfArgumentsInRange(lo: Int, hi: Int, args: Array[String])(body: => Unit) {
    if (args.length >= lo && args.length <= hi) body
    else println("Invalid number of arguments")
  }

  def detail(args: Array[String]) {
    conductIfNumberOfArgumentsInRange(1, 2, args) {
      conductIfConfigExists(args(0)) {
        config => {
          if (args.length == 1) {
            println(config.description.mkString("\n"))
          } else
            conductIfSiteExists(config, args(1)) {site => println(site.description.mkString("\n"))}
        }
      }
    }
  }

  def test(args: Array[String]) {
    conductIfNumberOfArgumentsInRange(1, 2, args) {
      conductIfConfigExists(args(0)) {
        config => {
          if (args.length == 1)
            queueConfiguration(config)
          else
            conductIfSiteExists(config, args(1)) {site => queueSite(config, site)}
        }
      }
    }
  }


  def queueConfiguration(name: String) {
    conductIfConfigExists(name) {c => queueConfiguration(c)}
  }

  def queueConfiguration(config: Config) {
    queueCollector ! config
  }

  def queueSite(config: Config, site: Site) {
    queueCollector ! (config, site)
  }

  var queueCollector: Actor = _

  def main(args: Array[String]) {
    printHint
    reload
    queueCollector = new QueueCollector

    val queueProcessor = new QueueProcessor(queueCollector,
      queue => println("Processor received [" + queue.site.name + "]@[" + queue.config.name + "]"))

    var actors = List(queueCollector, queueProcessor)
    actors.foreach(a => a.start)
    while (true) {
      print("> ")
      val line = readLine
      val command = line.split(" ").head
      val args = line.split(" ").tail
      command match {
        case "help" => printHelp
        case "exit" => {
          actors.foreach(a => a ! "exit");
          return
        }
        case "list" => list
        case "reload" => reload
        case "detail" => detail(args)
        case "test" => test(args)
        case _ => println("What the hell are you trying to do?")
      }
    }
  }
}