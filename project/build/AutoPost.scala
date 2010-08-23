import reflect.Manifest
import sbt._
import de.element34.sbteclipsify._
import util.Marshal

class AutoPostProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify with IdeaProject
{
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  var biblioMirror = "Biblio Mirror" at "http://mirrors.ibiblio.org/pub/mirrors/maven2/"
  val mavenRepo = "Maven Repo" at "http://repo2.maven.org/maven2"
  val azeckoskiRepo = "Azeckoski Repo" at "https://source.sakaiproject.org/maven2"
  lazy val copySupplement = task {
    FileUtilities.sync(info.projectPath / "configs",
      info.projectPath / "target" / "scala_2.8.0" / "classes" / "configs", log)
    FileUtilities.copyFile(info.projectPath / "lib_managed" / "scala_2.8.0" / "test" / "selenium-server-1.0.3-standalone.jar",
      info.projectPath / "target" / "scala_2.8.0" / "classes" / "tools" / "selenium-server.jar", log)
  }

  override def testAction = super.testAction dependsOn (copySupplement)

  val specs = "org.scalatest" % "scalatest" % "1.2" % "test"
  var snakeYaml = "org.yaml" % "snakeyaml" % "1.7"
  var jodaTime = "joda-time" %"joda-time" % "1.6.1"
  var seleniumJavaDriver = "org.seleniumhq.selenium.client-drivers" % "selenium-java-client-driver" % "1.0.2"
  var seleniumServer = "org.seleniumhq.selenium.server" % "selenium-server" % "1.0.3"  % "test" classifier "standalone"
  var reflectionUtils = "org.azeckoski" % "reflectutils" % "0.9.14"

//  def mainClass = "alpha.autoPost.Main"
}
