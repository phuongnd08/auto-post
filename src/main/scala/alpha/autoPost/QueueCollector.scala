package alpha.autoPost

import actors.Actor

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 2, 2010
 * Time: 9:09:08 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueCollector extends Actor {
  def act {
    var queue: List[Queue] = Nil
    while (true) {
      receive {
        case config: Section => {
          val sites = Option(config.sites).getOrElse(List[Site]())
          sites.foreach(site => queue = queue ::: List(Queue(config, site)))
          println("" + sites.length + "site(s) of [" + config.name + "] queued")
        }
        case (config: Section, site: Site) => {
          queue = queue ::: List(Queue(config, site))
          println("[" + site.name + "] inside [" + config.name + "] queued")
        }
        case cmd: String => {
          // println("[cmd=" + cmd + "] received")
          cmd match {
            case "exit" => return
            case "get" => {
              if (queue.length > 0) {
                sender ! queue.head
                queue = queue.tail
              }
            }
            case "list" => {
              println("List of queues")
              queue.foreach(q => println("\t- " + q))
            }
          }
        }
        case _ =>
      }
    }
  }
}