/*
 */

package talk

class Session(val parent:Option[Session]) {

}
class Step(parent:Option[Session]) extends Session(parent)
import java.io._
trait Start{
  def start()
}
