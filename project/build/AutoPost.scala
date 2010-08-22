import reflect.Manifest
import sbt._
import de.element34.sbteclipsify._
import util.Marshal

class AutoPostProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify with IdeaProject
{
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  val snakeRepo = "Snake Yaml Repository" at "http://repo2.maven.org/maven2"
  lazy val copyConfig = task {
    FileUtilities.sync(info.projectPath / "config",
      info.projectPath / "target" / "scala_2.8.0" / "classes" / "config", log)
  }

  override def testAction = super.testAction dependsOn (copyConfig)

  lazy val hi = task {println("Hello World"); None}
  val specs = "org.scalatest" % "scalatest" % "1.2" % "test"
  var snakeYaml = "org.yaml" % "snakeyaml" % "1.7"
}
