package talk

//class Response[+AT]
//class ParentProcessed extends Response[Nothing]
//case class Answered[AT](answer:AT) extends Response[AT]
//object NotAnswered extends Response[Nothing]
//case class InvalidAnswer(message:String) extends Response[Nothing]
import scalaz._
import Scalaz._
package object talk {
  case class Canceled
  type TalkError = Either[Canceled,String]
  type Response[AT]=Validation[TalkError,AT]
  implicit  def string2Err(string:String):TalkError=Right(string)
  implicit def canceled2Err(cancel:Canceled):TalkError=Left(cancel)
}