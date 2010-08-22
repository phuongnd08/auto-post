package alpha.autoPost

import java.io._
import scala.io._
import scala.collection.immutable.List
import org.yaml.snakeyaml.Yaml

class YamlLoader {
  lazy val yaml = new Yaml

  def getConfigs: List[Config] = {
    val configDir = new File(Environment.getJarPath + "/configs")
    var list: List[Config] = Nil
    for (f <- configDir.listFiles) {
      if (f.isDirectory)
        {
          val indexFile = new File(f.getCanonicalPath + "/index.yml")
          val inputStream = new FileInputStream(indexFile)
          var cfg = yaml.load(inputStream).asInstanceOf[Config]
          cfg.name = f.getName
          var contents: List[String] = Nil
          val contentDir = new File(f.getCanonicalPath + "/contents")
          println(contentDir)
          if (contentDir.exists)
            for (c <- contentDir.listFiles) {
              if (c.isFile)
                contents = Source.fromFile(c).mkString :: contents
            }
          cfg.contents = contents
          list = cfg :: list
        }
    }
    list
  }
}