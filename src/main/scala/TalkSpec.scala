package talk

import org.specs._
object TalkSpec extends Specification {
  "talk spec".isSpecifiedBy(
    SessionSpec,
    QuestionSpec
  )
  object SessionSpec extends Specification{
  }
  object QuestionSpec extends Specification{

    "quetion has output and input" in {
      
      val question=new SimpleQuestion(None,"A simple question") with StringOutputInput {
        override val inputFromString="answer"
        override val transferServices=List(new CopyTransfer())
      }
      question.ask must beEqualTo(Answered("answer"))
      question.outputedString must include("A simple question")
    }
    "question can be validate" in{
      class Validater extends Transfer[String]#TransferService{
        override def apply(input:Either[String,Transfer[String]#MT]):Either[Transfer[String]#MT,Response[String]]={
          input match{
            case Left(string)=> if(string=="fack") Right(InvalidAnswer("fack answer")) else Right(Answered(string))
          }
        }
      }
      class question extends SimpleQuestion(None, "a validated question") {
          override val transferServices=List(new Validater())
      }
      object fackAnswer extends question with StringOutputInput{
        override val inputFromString="fack"
      }
      fackAnswer.ask must beEqualTo(InvalidAnswer("fack answer"))
      object realAnswer extends question with StringOutputInput{
        override val inputFromString="real"
      }
      realAnswer.ask must beEqualTo(Answered("real"))
    }
    "question can be retry when validate faild" in{
      class Validater extends Transfer[String]#TransferService{
        override def apply(input:Either[String,Transfer[String]#MT]):Either[Transfer[String]#MT,Response[String]]={
          input match{
            case Left(string)=> if(string=="fack") Right(InvalidAnswer("fack answer")) else Right(Answered(string))
          }
        }
      }
      class question extends SimpleQuestion(None, "a validated question") with Retry[String]{
          override val transferServices=List(new Validater())
      }
      object fackThenRealAnswer extends question with StringOutputInput{
        override val inputFromString="fack\nreal"
      }
      fackThenRealAnswer.ask must beEqualTo(Answered("real"))
    }
    "question can be set to optional" in {
      class question extends SimpleQuestion(None, "a validated question"){
          override val transferServices=List(new Optional(),new CopyTransfer())
      }
      object canceledAnswer extends question with StringOutputInput{
        override val inputFromString=""
      }
      canceledAnswer.ask must beEqualTo(NotAnswered)
    }
    "question is compositable" can {
      "questions as sequence" in {

      }
    }
    "default question types".isSpecifiedBy(
      SingleChioseSpec,MultipleChoiseSpec
    )
    object SingleChioseSpec extends Specification{

    }
    object MultipleChoiseSpec extends Specification{
      
    }
  }
}
