package talk

import org.specs2.mutable.Specification
import talk._
import scalaz._
import Scalaz._

object TalkSpec extends Specification {
  "talk spec" in {
    "quetion has output and input" in {
      val question = new SimpleQuestion(None, "A simple question") with StringOutputInput {
        override val inputFromString = "answer"
      }
      question.ask.toOption must beSome.which(_ == "answer")
    }
    "question can be validated before return" in {
      val question = new SimpleQuestion(None, "A simple question") with Retry[String] with StringOutputInput {
        override val inputFromString = "no\nyes"

        override def doWithAnswer(answer: String): Response[String] = {
          if (answer == "yes") {
            "yes".success
          } else {

            Right("must be yes").fail
          }
        }
      }
      question.ask.toOption must beSome.which(_ == "yes")
    }
    "question can be canceld" in {
      val question = new SimpleQuestion(None, "A simple question") with Retry[String] with Cancel[String] with StringOutputInput {
        override val inputFromString = "no\nnoe\ncancel"

        override def doWithAnswer(answer: String): Response[String] = {
          println("in concept")
          if (answer == "yes") {
            "yes".success
          } else {
            Right("must be yes").fail
          }
        }
      }
      question.ask.fail.toOption must beSome.which(_ == Left(Canceled()))
    }
    //    "question can be validate" in {
    //      class Validater extends Transfer[String]#TransferService {
    //        override def apply(input: Either[String, Transfer[String]#MT]): Either[Transfer[String]#MT, Response[String]] = {
    //          input match {
    //            case Left(string) => if (string == "fack") Right(InvalidAnswer("fack answer")) else Right(Answered(string))
    //          }
    //        }
    //      }
    //      class question extends SimpleQuestion(None, "a validated question") {
    //        override val transferServices = List(new Validater())
    //      }
    //      object fackAnswer extends question with StringOutputInput {
    //        override val inputFromString = "fack"
    //      }
    //      fackAnswer.ask must beEqualTo(InvalidAnswer("fack answer"))
    //      object realAnswer extends question with StringOutputInput {
    //        override val inputFromString = "real"
    //      }
    //      realAnswer.ask must beEqualTo(Answered("real"))
    //    }
    //    "question can be retry when validate faild" in {
    //      class Validater extends Transfer[String]#TransferService {
    //        override def apply(input: Either[String, Transfer[String]#MT]): Either[Transfer[String]#MT, Response[String]] = {
    //          input match {
    //            case Left(string) => if (string == "fack") Right(InvalidAnswer("fack answer")) else Right(Answered(string))
    //          }
    //        }
    //      }
    //      class question extends SimpleQuestion(None, "a validated question") with Retry[String] {
    //        override val transferServices = List(new Validater())
    //      }
    //      object fackThenRealAnswer extends question with StringOutputInput {
    //        override val inputFromString = "fack\nreal"
    //      }
    //      fackThenRealAnswer.ask must beEqualTo(Answered("real"))
    //    }
    //    "question can be set to optional" in {
    //      class question extends SimpleQuestion(None, "a validated question") {
    //        override val transferServices = List(new Optional(), new CopyTransfer())
    //      }
    //      object canceledAnswer extends question with StringOutputInput {
    //        override val inputFromString = ""
    //      }
    //      canceledAnswer.ask must beEqualTo(NotAnswered)
    //    }
    //    "question is compositable" in {
    //      "questions as sequence" in {
    //
    //      }
    //    }
    //    "default question types".isSpecifiedBy(
    //      SingleChioseSpec,MultipleChoiseSpec
    //    )
    //    object SingleChioseSpec extends Specification{
    //
    //    }
    //    object MultipleChoiseSpec extends Specification{
    //
    //    }
  }
}
