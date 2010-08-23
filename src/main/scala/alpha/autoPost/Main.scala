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