package alpha.autoPost

import java.io._
import scala.io._
import collection.JavaConversions._
import scala.collection.immutable.List
import scala.collection.mutable.Map
import scala.xml.parsing.XhtmlParser

import org.yaml.snakeyaml.Yaml
import util.matching.Regex

object YamlLoader {
  val DefaultEncoding = "UTF-8"
}

class YamlLoader {
  lazy val yaml = new Yaml

  protected def readArticles(articlesDir: File) = {
    var articles: List[Article] = Nil
    for (c <- articlesDir.listFiles.sortBy(_.getName).reverse) {
      if (c.isFile)
        articles = Article(new Regex(".html?$").replaceFirstIn(c.getName, ""), Source.fromFile(c)(YamlLoader.DefaultEncoding).mkString) :: articles
    }
    articles
  }

  protected def readSites(sitesDir: File) = {
    var sites: List[Site] = Nil
    for (d <- sitesDir.listFiles.sortBy(_.getName)) {
      if (d.isDirectory) {
        var site = new Site
        site.name = d.getName
        site.loginSteps = readSiteSteps(new File(d.getCanonicalPath + "/loginSteps.html"))
        site.postSteps = readSiteSteps(new File(d.getCanonicalPath + "/postSteps.html"))
        site.logoutSteps = readSiteSteps(new File(d.getCanonicalPath + "/logoutSteps.html"))
        site.specificInfo = readSpecificInfo(new File(d.getCanonicalPath + "/info.yml"))
        sites = site :: sites
      }
    }
    sites                    
  }

  protected def readSiteSteps(stepsFile: File): Array[Array[String]] = {
    if (stepsFile.exists) {
      var result: List[Array[String]] = Nil
      val xml = XhtmlParser(Source.fromFile(stepsFile)(YamlLoader.DefaultEncoding))
      for (row <- xml \ "body" \ "table" \ "tbody" \ "tr") {
        var params: List[String] = Nil
        for (cell <- row \ "td") {
          params = cell.text :: params
        }
        result = params.reverse.toArray :: result
      }
      result.reverse.toArray
    }
    else Array()
  }

  protected def readSpecificInfo(infoFile: File): Map[String, String] = {
    if (infoFile.exists) {
      val inputStream = new FileInputStream(infoFile)
      val map = yaml.load(inputStream).asInstanceOf[java.util.Map[String, String]]
      return map
    }
    return Map[String, String]()
  }

  def getSections: List[Section] = {
    val configDir = new File(Environment.getJarPath + "/sections")
    var list: List[Section] = Nil
    for (f <- configDir.listFiles) {
      if (f.isDirectory)
        {
          val indexFile = new File(f.getCanonicalPath + "/index.yml")
          val inputStream = new FileInputStream(indexFile)
          var cfg = yaml.load(inputStream).asInstanceOf[Section]
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
