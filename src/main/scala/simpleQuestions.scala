package talk

import talk._
import scalaz._
import Scalaz._

class SimpleQuestion[AT](parent: Option[Session], question: String, val doWith: PartialFunction[String, Response[AT]]) extends Question[AT](parent, question) {
  override def doWithAnswer(answerInput: String): Response[AT] = {
    doWith(answerInput)
  }
}

trait Retry[AT] extends Question[AT] {
  abstract override def ask(): Response[AT] = {
    output(this.question)
    val result = doWithAnswer(input())
    if (result.isFailure && !(result == Left(Canceled()).fail)) {
      output("try again")
      ask()
    } else {
      result
    }
  }
}

trait BeforeInterceptor[AT] extends SimpleQuestion[AT] {
  val before: PartialFunction[String, Response[AT]]

  abstract override def doWithAnswer(answerInput: String): Response[AT] = {
    this.before.orElse(doWith)(answerInput)
  }
}

trait Cancel[AT] extends BeforeInterceptor[AT] {
  val cancelKeyword: String = "cancel"
  override val before: PartialFunction[String, Response[AT]]= {
    case a if a == cancelKeyword => Left(Canceled()).fail
  }
}

trait Optional[AT] extends BeforeInterceptor[Option[AT]] {
  val noneKey: String = "none"
  override val before: PartialFunction[String, Response[Option[AT]]] = {
    case a if a == noneKey => None.success
  }
}