package alpha.autoPost

import collection.mutable.Map
import actors.Actor

object AutoScheduler {
  val DefaultInterval = 1000
}
class AutoScheduler(val timeProvider: TimeProvider, val getConfigs: () => List[Section], val collector: Actor, val interval: Int) extends Actor {
  def this(timeProvider: TimeProvider, getConfigs: () => List[Section], collector: Actor) = this (timeProvider, getConfigs, collector, AutoScheduler.DefaultInterval)

  def act {
    val lastBeat = Map[Section, Long]()
    while (true) {
      val now = timeProvider.current
      getConfigs().filter(config => !lastBeat.contains(config))
              .foreach(config => lastBeat(config) = now)
      getConfigs().filter(config => config.shouldRunNow(lastBeat(config), now))
              .foreach(config => {collector ! config; lastBeat(config) = now})
      receiveWithin(interval) {
        case cmd: String => if (cmd == "exit") {
          return
        }
        case _ =>
      }
    }
  }
}