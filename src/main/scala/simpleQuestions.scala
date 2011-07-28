package talk

import talk._
import scalaz._
import Scalaz._

class SimpleQuestion(parent:Option[Session],question:String) extends Question[String](parent,question){
  override def doWithAnswer(answerInput:String):Response[String] ={
    answerInput.success
  }
}
class RepeatQuestion(parent:Option[Session],question:String) extends Question[String](parent,question){
  override def doWithAnswer(answerInput:String):Response[String]={
    if(answerInput==question) question.success else Right("no match").fail
  }
}
