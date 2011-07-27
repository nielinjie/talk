package talk

import java.io._
abstract class Question[AT](parent:Option[Session],val question:String) extends Session(parent) with Start with Transfer[AT] with Output with Input{
  private var _answer:Response[AT]=_
  private var _answered:Boolean=false
  override def start()={
    ask()
  }
  def printQuestion()={
    output(this.question)
  }

  def ask():Response[AT]={
    printQuestion()
    this._answer=transfer(input())
    this._answered=true
    this._answer
  }
  def answer:Response[AT]={
    if(this._answered )
      this._answer
    else
      this.ask()
  }
}

class SimpleQuestion(parent:Option[Session],question:String) extends Question[String](parent,question){
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
trait Retry[AT] extends Question[AT]{
  override def ask():Response[AT]={
    super.ask() match{
      case InvalidAnswer(message)=>
        output(message)
        output("retry - ")
        ask()
      case re:Any=>re
    }
  }
}

trait HasOptions[IT]{
  val options:List[IT]
  def optionsToString()={
    this.options.zipWithIndex.map(x=>x._2.toString+". "+x._1.toString).reduceLeft(_+"\n"+_)
  }
}

class IndexParser[AT] extends Transfer[AT]#TransferService{
  override def apply(inputOrMr:Either[String,Transfer[AT]#MT]):Either[Transfer[AT]#MT,Response[AT]]={
    inputOrMr match{
      case Left(input)=>parse(input)
      case Right(mr:String) =>parse(mr)
      case _=> Right(InvalidAnswer("Index Parser failed"))
    }
  }
  def parse(string:String):Either[List[Int],InvalidAnswer]={
    import scala.util.parsing.combinator._
    IntParser.parse(string) match{
      case su:Parsers#Success[List[Int]]=>{
          Left(su.result)
        }
      case _=>Right(InvalidAnswer("Parse error"))
    }
  }
}
abstract class IndexValidate[IT] extends Transfer[List[IT]]#TransferService with HasOptions[IT]{
  override def apply(mr:Either[String,Transfer[List[IT]]#MT]):Either[Transfer[List[IT]]#MT,Response[List[IT]]]={
    mr match{
      case Right(intList:List[Int])=>{
          if(intList.forall(_<this.options.size))
            Left(intList)
          else
            Right(InvalidAnswer("Out of index"))
        }
      case _ =>Right(InvalidAnswer("illegal input"))
    }
  }
}
class CopyTransfer extends Transfer[String]#TransferService{
  override def apply(input:Either[String,Transfer[String]#MT]):Either[Transfer[String]#MT,Response[String]]={
    input match{
      case Left(string)=> Right(Answered(string))
    }
  }
}
class Optional extends Transfer[Nothing]#TransferService {
  val cancelString=""
  override def apply(input:Either[String,Transfer[Nothing]#MT]):Either[Transfer[Nothing]#MT,Response[Nothing]]={
    input match{
      case Left(string)=>if(string==cancelString) Right(NotAnswered) else Left(string)
      case Right(mr:String)=>if(mr==cancelString) Right(NotAnswered) else Left(mr)
      case _ => Left(input)
    }
  }
}

trait  Transfer[AT]{
  type MT=Any
  type TransferService=(Either[String,MT]=>Either[MT,Response[AT]])
  val transferServices:List[TransferService]=List()
  def transfer(input:String):Response[AT]={
    var middleResult:Either[String,MT]=Left(input)
    transferServices.foreach({
        transferService:TransferService=>
        transferService(middleResult)
        match {
          case Right(response)=> return response
          case Left(mr)=>middleResult=Right(mr)
        }
      })
    middleResult match{
      case Right(mt)=>mt match{
          case a:AT=> Answered(a)
          case _=> InvalidAnswer("input can not be transfered to answer")
        }
      case _=> InvalidAnswer("have you set transfer services?")
    }
  }
}

