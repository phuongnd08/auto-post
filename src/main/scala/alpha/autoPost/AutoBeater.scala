package alpha.autoPost

class AutoBeater(env: {val timeProvider: TimeProvider; def configs: List[Config]; }) {
  val lastAccess = Map[String, Long]()

  def beat {
    for (config <- env.configs) {
      if (lastAccess.contains(config.name)) {
        val lastAccessTimeStamp = lastAccess(config.name)
        if (config.repeatSchedule.every != null) {
        }
      } else {
        lastAccess(config.name) = env.timeProvider.current
      }
    }
  }
}