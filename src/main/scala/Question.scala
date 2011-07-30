package talk

import java.io._
import talk._
import scalaz._
import Scalaz._

abstract class Question[AT](parent: Option[Session], val question: String) extends Session(parent){

  def ask(implicit env:Env): Response[AT] = {
    env.output(this.question)
    doWithAnswer(env.input())
  }

  def doWithAnswer(answerInput: String): Response[AT]
}

//class SingleChoise[AT](parent:Option[Session],question:String,val options:List[AT])
//extends Question[AT](parent,question) with HasOptions[AT]{
//  val multi=new MultipleChoise(parent,question,options)
//  override def printQuestion()={
//    super.printQuestion()
//    output("Choise ONE from following")
//    output(optionsToString)
//  }
//  override def transfer(input:String):Response[AT]={
//    multi.transfer(input) match{
//      case Answered(answers)=>{
//          println(answers)
//          answers match{
//            case List(answer)=>Answered(answer)
//            case _=>InvalidAnswer("Too much or none selected")
//          }
//        }
//      case ia:InvalidAnswer=>ia
//      case _=> InvalidAnswer("exception")
//    }
//  }
//}
//
//class MultipleChoise[IT](parent:Option[Session],question:String,override val options:List[IT])
//extends Question[List[IT]](parent,question)  with IndexValidate[IT]{
//  override def printQuestion()={
//    output(this.question)
//    output("Choise ONE or MORE from following")
//    output(optionsToString)
//
//  }
//  override def onTransfer(middleResult:MT):Response[List[IT]]={
//    new Answered(middleResult.map(this.options.apply(_)))
//  }
//}

//trait HasOptions[IT]{
//  val options:List[IT]
//  def optionsToString()={
//    this.options.zipWithIndex.map(x=>x._2.toString+". "+x._1.toString).reduceLeft(_+"\n"+_)
//  }
//}
//
//class IndexParser[AT] extends Transfer[AT]#TransferService{
//  override def apply(inputOrMr:Either[String,Transfer[AT]#MT]):Either[Transfer[AT]#MT,Response[AT]]={
//    inputOrMr match{
//      case Left(input)=>parse(input)
//      case Right(mr:String) =>parse(mr)
//      case _=> Right(InvalidAnswer("Index Parser failed"))
//    }
//  }
//  def parse(string:String):Either[List[Int],InvalidAnswer]={
//    import scala.util.parsing.combinator._
//    IntParser.parse(string) match{
//      case su:Parsers#Success[List[Int]]=>{
//          Left(su.result)
//        }
//      case _=>Right(InvalidAnswer("Parse error"))
//    }
//  }
//}
//abstract class IndexValidate[IT] extends Transfer[List[IT]]#TransferService with HasOptions[IT]{
//  override def apply(mr:Either[String,Transfer[List[IT]]#MT]):Either[Transfer[List[IT]]#MT,Response[List[IT]]]={
//    mr match{
//      case Right(intList:List[Int])=>{
//          if(intList.forall(_<this.options.size))
//            Left(intList)
//          else
//            Right(InvalidAnswer("Out of index"))
//        }
//      case _ =>Right(InvalidAnswer("illegal input"))
//    }
//  }
//}
//class CopyTransfer extends Transfer[String]#TransferService{
//  override def apply(input:Either[String,Transfer[String]#MT]):Either[Transfer[String]#MT,Response[String]]={
//    input match{
//      case Left(string)=> Right(Answered(string))
//    }
//  }
//}
//class Optional extends Transfer[Nothing]#TransferService {
//  val cancelString=""
//  override def apply(input:Either[String,Transfer[Nothing]#MT]):Either[Transfer[Nothing]#MT,Response[Nothing]]={
//    input match{
//      case Left(string)=>if(string==cancelString) Right(NotAnswered) else Left(string)
//      case Right(mr:String)=>if(mr==cancelString) Right(NotAnswered) else Left(mr)
//      case _ => Left(input)
//    }
//  }
//}
//


