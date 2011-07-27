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
trait Output{
  var outputTo:PrintStream=_
  def output(str:String)={
    outputTo.println(str)
  }
}
import scala.io._
trait Input{
  var inputFrom:{def readLine():String}=_
  def input():String={
    this.inputFrom.readLine()
  }
}

trait ConsoleOutputInput extends Output with Input{
  outputTo=System.out
  inputFrom=System.console
}
import scala.io.Source
trait StringOutputInput extends Output with Input{
  val outputToBuffer=new ByteArrayOutputStream()
  def outputedString=outputToBuffer.toString
  outputTo=new PrintStream(this.outputToBuffer)

  val inputFromString:String
  var readingIndex:Int=0
  inputFrom=new {
    def readLine():String={
      readingIndex=readingIndex+1
      Source.fromString(StringOutputInput.this.inputFromString).getLine(readingIndex)
    }
  }
}