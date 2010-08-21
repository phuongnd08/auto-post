import sbt._
import de.element34.sbteclipsify._

class AutoPostProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify 
{
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  lazy val hi = task { println("Hello World"); None }
  val specs = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5" % "test"
}
