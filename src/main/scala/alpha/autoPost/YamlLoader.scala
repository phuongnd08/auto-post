package alpha.autoPost

import java.io._
import scala.collection.immutable.List
import org.yaml.snakeyaml.Yaml

class YamlLoader {
  def getJarPath = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath

  lazy val yaml = new Yaml

  def getConfigs: List[Config] = {
    val configDir = new File(getJarPath + "/config")
    var list: List[Config] = Nil
    for (f <- configDir.listFiles) {
      if (f.isDirectory)
        {
          val indexFile = f.getCanonicalPath + "/index.yml"
          val inputStream = new FileInputStream(new File(indexFile))
          var cfg = yaml.load(inputStream).asInstanceOf[Config]
          cfg.name = f.getName
          list = cfg :: list
        }
    }
    list
  }
}