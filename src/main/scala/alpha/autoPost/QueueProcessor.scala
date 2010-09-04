package alpha.autoPost

import actors.Actor

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 2, 2010
 * Time: 9:09:08 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueProcessor(val name: String, val collector: Actor, val process: (Queue) => Unit) extends Actor {
  def act {
    while (true) {
      collector ! "get"
      receiveWithin(1000) {
        case queue: Queue => {
          println("[" + name + "]Received queue")
          process(queue)
        }
        case cmd: String => {
          cmd match {
            case "exit" => return
            case _ =>
          }
        }
        case _ =>
      }
    }
  }
}