package alpha.autoPost

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Aug 22, 2010
 * Time: 6:15:04 PM
 * To change this template use File | Settings | File Templates.
 */

object Environment{
  def getJarPath = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath  
}