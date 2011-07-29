package talk

import org.specs2.mutable.Specification
import talk._
import scalaz._
import Scalaz._
import org.specs2.execute._

object TalkSpec extends Specification {
  "talk spec" in {
    "quetion has output and input" in {
      val question = new SimpleQuestion(None, "A simple question", {case input => input.success}) with StringOutputInput {
        override val inputFromString = "answer"
      }
      question.ask.toOption must beSome.which(_ == "answer")
    }
    "question can be validated before return" in {
      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".success
        case _ => Right("must be yes").fail
      }) with Retry[String] with StringOutputInput {
        override val inputFromString = "no\nyes"

      }
      question.ask.toOption must beSome.which(_ == "yes")
    }
    "question can be canceld" in {

      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".success
        case _ => Right("must be yes").fail
      }) with Retry[String] with Cancel[String] with StringOutputInput {
        override val inputFromString = "no\nnoe\ncancel"
      }
      question.ask.fail.toOption must beSome.which(_ == Left(Canceled()))
    }
    "question can be optional" in {
      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".some.success
        case _ => Right("must be yes").fail
      }) with Retry[Option[String]] with Optional[String] with StringOutputInput {
        override val inputFromString = "no\nnoe\nnone"
      }
      question.ask.toOption must beSome.which(_ == None)
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
