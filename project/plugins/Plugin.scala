import sbt._

class MySbtProjectPlugins(info: ProjectInfo) extends PluginDefinition(info) {
  lazy val eclipse = "de.element34" % "sbt-eclipsify" % "0.6.0"
  lazy val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  lazy val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1-SNAPSHOT"
  lazy val proguard = "org.scala-tools.sbt" % "sbt-proguard-plugin" % "0.0.2"  
}
