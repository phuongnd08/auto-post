package alpha.autoPost

import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Sep 19, 2010
 * Time: 2:34:35 PM
 * To change this template use File | Settings | File Templates.
 */

class ArticleSpec extends Spec with MustMatchers {
  describe("htmlContent") {
    it("must be in accordance with bbcode") {
      val article = Article("BBSample", "[b]Oi![/b]")
      article.htmlContent must be("<span style=\"font-weight:bold;\">Oi!</span>")
    }
  }

}