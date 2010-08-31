package alpha.autoPost

class AutoBeater(env: {val timeProvider: TimeProvider; def configs: List[Config]; }) {
  val lastAccess = Map[String, Long]()
  def beat {
    for (config <- env.configs) {
      if (lastAccess.contains(config.name)) {
        val lastAccessTimeStamp = lastAccess(config.name)
        if (config.repeatSchedule.every != Nothing) {
          val every = strToTimestamp(config.repeatSchedule.every)
        }
      } else {
        lastAccess(config.name) = env.timeProvider.current
      }
    }
  }

  def strToTimestamp(str: String) {
    val parts = str.split("\\s+")
    var result = 0
    for (i <- 1 until parts.size / 2) {
      val number = parts(2 * i - 2).toInt
      var unit = parts(2 * i - 1).toLowerCase
      if (unit.matches("\\ahour(s?)\\z")) {result += number * 3600}
      else if (unit.matches("\\aminute(s?)\\z")) {result += number * 60}
      else if (unit.matches("\\asecond(s?)\\z")) {result += number}
    }
    return result * 1000
  }
}
