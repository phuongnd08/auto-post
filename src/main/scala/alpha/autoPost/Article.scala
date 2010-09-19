package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 2, 2010
 * Time: 3:44:25 PM
 * To change this template use File | Settings | File Templates.
 */

object Article {
  val BBProcessor = ru.perm.kefir.bbcode.BBProcessorFactory.getInstance.create
}
case class Article(var title: String, var content: String) {
  def htmlContent = Article.BBProcessor.process(content)
}
