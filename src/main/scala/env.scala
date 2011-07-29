package talk

import scala.io._
import java.io.{ByteArrayOutputStream, PrintStream}

trait Output {
  var outputTo: PrintStream = _

  def output(str: String) = {
    outputTo.println(str)
  }
}


trait Input {
  var inputFrom: {def readLine(): String} = _

  def input(): String = {
    this.inputFrom.readLine()
  }
}

trait Env extends Input with Output

class ConsoleOutputInput extends Env {
  outputTo = System.out
  inputFrom = System.console
}

import scala.io.Source

class StringOutputInput(val inputFromString: String) extends Env {
  val outputToBuffer = new ByteArrayOutputStream()

  def outputedString = outputToBuffer.toString

  outputTo = new PrintStream(this.outputToBuffer)
  var readingIndex: Int = 0
  inputFrom = new {
    def readLine(): String = {
      readingIndex = readingIndex + 1
      Source.fromString(StringOutputInput.this.inputFromString).getLine(readingIndex)
    }
  }
}