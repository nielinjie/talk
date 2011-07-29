package talk

import org.specs2.mutable.Specification
import talk._
import scalaz._
import Scalaz._

object TalkSpec extends Specification {
  "talk spec" in {
    "quetion has output and input" in {
      implicit val env: Env = new StringOutputInput("answer")
      val question = new SimpleQuestion(None, "A simple question", {
        case input => input.success
      })
      question.ask must equalTo ("answer".success)
    }
    "question can be validated before return" in {
      implicit val env: Env = new StringOutputInput("no\nyes")
      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".success
        case _ => "must be yes".asFail
      }) with Retry[String]
      question.ask must equalTo ("yes".success)
    }
    "question can be canceld" in {
      implicit val env: Env = new StringOutputInput("no\nnoe\ncancel")

      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".success
        case _ => "must be yes".asFail
      }) with Retry[String] with Cancel[String]
      question.ask must equalTo (Canceled:Response[String])
    }
    "question can be optional" in {
      implicit val env: Env = new StringOutputInput("no\nnoe\nnone")

      val question = new SimpleQuestion(None, "A simple question", {
        case "yes" => "yes".some.success
        case _ => "must be yes".asFail
      }) with Retry[Option[String]] with Optional[String]
      question.ask must equalTo(None.success)
    }
    "question is compositable" in {
      "totally success" in {
        implicit val env:Env = new StringOutputInput("foo\nbar")
        val question = new SimpleQuestion(None, "add me" , {
        case a => a.success
      })
      val result=for {
        a<-question.ask
        b<-question.ask
      } yield a+b
        result must equalTo("foobar".success)
      }
      "something wrong" in {
        implicit val env:Env = new StringOutputInput("foo\nbar")
        val question = new SimpleQuestion(None, "add me" , {
        case a if a=="foo" => a.success
        case _ => "not cool".asFail
      })
      val result=for {
        a<-question.ask
        b<-question.ask
      } yield a+b
        result must equalTo("not cool".asFail)
      }

    }
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
