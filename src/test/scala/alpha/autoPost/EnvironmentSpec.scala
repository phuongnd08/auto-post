package alpha.autoPost

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:16:21 PM
 * To change this template use File | Settings | File Templates.
 */

class EnvironmentSpec extends Spec with MustMatchers {
  describe("getJarPath") {
    it("must return current execution path") {
      Environment.getJarPath must be === "/home/phuongnd08/code/auto-post/target/scala_2.8.0/classes/"
    }
  }
}