import reflect.Manifest
import sbt._
import de.element34.sbteclipsify._
import util.Marshal

class AutoPostProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify with IdeaProject
{
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  var biblioMirror = "Biblio Mirror" at "http://mirrors.ibiblio.org/pub/mirrors/maven2/"
  val mavenRepo = "Maven Repo" at "http://repo2.maven.org/maven2"
  lazy val copyConfig = task {
    FileUtilities.sync(info.projectPath / "configs",
      info.projectPath / "target" / "scala_2.8.0" / "classes" / "configs", log)
  }

  override def testAction = super.testAction dependsOn (copyConfig)

  val specs = "org.scalatest" % "scalatest" % "1.2" % "test"
  var snakeYaml = "org.yaml" % "snakeyaml" % "1.7"
  var jodaTime = "joda-time" %"joda-time" % "1.6.1"
}
