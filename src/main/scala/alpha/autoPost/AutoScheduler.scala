package alpha.autoPost

class AutoScheduler(env: {val timeProvider: TimeProvider; def configs: List[Config]; }) {
  val lastBeat = Map[String, Long]()

  def configsToExecute {
    env.configs.filter(config => !lastBeat.contains(config.name))
            .foreach(config => lastBeat(config.name) = env.timeProvider.current)
    env.configs.filter(config => lastBeat.contains(config.name))
            .filter(config => config.shouldRunNow(lastBeat(config.name), env.timeProvider.current))
  }

  def recordLastExecution(config:Config)
  {
    lastBeat(config.name) = env.timeProvider.current
  }
}