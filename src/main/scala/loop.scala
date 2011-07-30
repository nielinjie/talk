package talk

import java.io._
import talk._
import scalaz._
import Scalaz._

class Loop(parent: Option[Session], promoter: String, actions: PartialFunction[String, Response[_]]) extends Question[Any](parent, promoter) {
  val quitKey = "quit"
  val unknownCommand: PartialFunction[String, Response[Unit]] = {
    case _ => "unknown command".asFail
  }
  val quitCommand: PartialFunction[String, Response[Quit.type]] = {
    case a if a == quitKey => Quit.success
  }

  override def doWithAnswer(answerInput: String): Response[Any] = {
    actions.orElse(quitCommand).orElse(unknownCommand)(answerInput)
  }

  override def ask(implicit env: Env): Response[Any] = {
    env.output(this.question)
    val result = doWithAnswer(env.input())
    env.output(result.toString)
    if (!(result == Quit.success)) {
      ask(env)
    } else {
      result
    }
  }
}