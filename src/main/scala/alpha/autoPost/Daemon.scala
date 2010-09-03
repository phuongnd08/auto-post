package alpha.autoPost

import actors.Actor

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 3, 2010
 * Time: 10:54:15 PM
 * To change this template use File | Settings | File Templates.
 */

object Daemon{
  val DefaultRefreshInterval = 1000
}

class Daemon(val proc: () => Unit, val refreshInterval: Int) extends Actor {
  def this(proc: () => Unit) = this(proc, Daemon.DefaultRefreshInterval)
  def act {
    while (true) {
      receiveWithin(refreshInterval) {
        case cmd: String => if (cmd == "exit") return
        case _ => {
          proc()
        }
      }
    }
  }
}