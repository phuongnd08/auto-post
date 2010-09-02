package alpha.autoPost

import java.io._
import scala.io._
import scala.collection.immutable.List
import scala.xml.parsing.XhtmlParser

import org.yaml.snakeyaml.Yaml
import util.matching.Regex

class YamlLoader {
  lazy val yaml = new Yaml

  protected def readArticles(articlesDir: File) = {
    var articles: List[Article] = Nil
    for (c <- articlesDir.listFiles.sortBy(_.getName).reverse) {
      if (c.isFile)
        articles = Article(new Regex(".html?$").replaceFirstIn(c.getName, ""), Source.fromFile(c).mkString) :: articles
    }
    articles
  }

  protected def readSites(sitesDir: File) = {
    var sites: List[Site] = Nil
    for (d <- sitesDir.listFiles.sortBy(_.getName)) {
      if (d.isDirectory) {
        var site = new Site
        site.name = d.getName
        site.loginSteps = readSteps(new File(d.getCanonicalPath + "/loginSteps.html"))
        site.postSteps = readSteps(new File(d.getCanonicalPath + "/postSteps.html"))
        site.logoutSteps = readSteps(new File(d.getCanonicalPath + "/logoutSteps.html"))
        sites = site :: sites
      }
    }
    sites
  }

  protected def readSteps(stepsFile: File):Array[Array[String]] = {
    if (stepsFile.exists) {
      var result: List[Array[String]] = Nil
      val xml = XhtmlParser(Source.fromFile(stepsFile))
      for (row <- xml \"body" \ "table" \ "tbody" \ "tr"){
        var params: List[String] = Nil
        for (cell <- row \ "td"){
          params = cell.text :: params
        }
        result = params.reverse.toArray :: result
      }
      result.reverse.toArray
    }
    else Array()
  }

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
          val articlesDir = new File(f.getCanonicalPath + "/articles")
          if (articlesDir.exists)
            cfg.articles = readArticles(articlesDir)
          val sitesDir = new File(f.getCanonicalPath + "/sites")
          if (sitesDir.exists)
            cfg.sites = readSites(sitesDir)
          list = cfg :: list
        }
    }
    list.sortBy(cfg => cfg.name)
  }
}