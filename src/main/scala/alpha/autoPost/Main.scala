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
    println("  reload : Reload the sections")
    println("  list : List available sections")
    println("  detail sample_1: Print the detail information for sample_1(if exists)")
    println("  detail sample_1 5giay.vn: Print the detail information for 5giay.vn inside sample_1(if exists)")
    println("  test sample_1: Try to post into all site of inside sample_1")
    println("  test sample_1 5giay.vn: try posting into a 5giay.vn using articles inside sample_1")
    println("  exit : quit the program")
  }

  def printHint {
    println("Type help to know how to use")
  }

  var sections: List[Section] = _
  lazy val yamlLoader: YamlLoader = new YamlLoader

  def reload {
    sections = yamlLoader.getSections
    println("Sections loaded")
  }

  def list {
    println("Available sections")
    sections.map(c => c.briefDescription.mkString("\n")).foreach(println)
  }

  protected def conductIfConfigExists(name: String)(body: Section => Unit) {
    var config = sections.find((config) => config.name == name)
    if (config != None) {
      body(config.get)
    }
    else
      println("Section of name " + name + " not found")
  }

  protected def conductIfSiteExists(config: Section, siteName: String)(body: Site => Unit) {
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

  val DumpFile = "queue-collector.log"
  def dump(string: String) {
    val fw = new java.io.FileWriter(Environment.getJarPath + "/" + DumpFile, true)
    fw.write(string + "\n")
    fw.flush
    fw.close
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

  def queueConfiguration(config: Section) {
    queueCollector ! config
  }

  def queueSite(config: Section, site: Site) {
    queueCollector ! (config, site)
  }

  var queueCollector: Actor = _
  var queueProcessors: List[Actor] = _
  var scheduler: Actor = _
  var daemons: List[Actor] = _
  val timeProvider = new TimeProvider
  val seleniumWrapper = new SeleniumWrapper
  val seleniumSmartController = new SeleniumSmartController(timeProvider, seleniumWrapper)

  def prepareActors {
    queueCollector = new QueueCollector
    queueProcessors = (for (i <- 1 to 2) yield new QueueProcessor("processor-" + i, queueCollector,
      queue => {
        seleniumSmartController.requestServer
        println("Start process[" + queue.site.name + "]@[" + queue.section.name + "]")
        seleniumWrapper.execute(queue.site.url) {(processor) => queue.run(processor, dump)}
      })).toList

    println("queueProcessors.length=" + queueProcessors.length)
    scheduler = new AutoScheduler(new TimeProvider, () => sections, queueCollector)
    daemons = List(new Daemon(() => seleniumSmartController.prune))
  }

  def actors = scheduler :: queueCollector :: queueProcessors

  def startAllActors {
    actors.foreach(a => a.start)
  }

  def stopAllActors {
    actors.foreach(a => a ! "exit");
  }

  def backlog(args: Array[String]) {queueCollector ! "list"}

  def main(args: Array[String]) {
    printHint
    println("Loading resources from [" + Environment.getJarPath + "]")
    reload
    prepareActors
    startAllActors

    while (true) {
      print("> ")
      val line = readLine
      val command = line.split(" ").head
      val args = line.split(" ").tail
      command match {
        case "help" => printHelp
        case "exit" => {
          stopAllActors
          seleniumWrapper.stopServer
          return
        }
        case "list" => list
        case "reload" => reload
        case "detail" => detail(args)
        case "test" => test(args)
        case "backlog" => backlog(args)
        case "" =>
        case _ => println("What the hell are you trying to do?")
      }
    }
  }
}