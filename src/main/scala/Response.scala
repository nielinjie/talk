package talk

//class Response[+AT]
//class ParentProcessed extends Response[Nothing]
//case class Answered[AT](answer:AT) extends Response[AT]
//object NotAnswered extends Response[Nothing]
//case class InvalidAnswer(message:String) extends Response[Nothing]

import scalaz._
import Scalaz._

package object talk {

  case object Canceled

  type TalkError = Either[Canceled.type, String]
  type Response[AT] = Validation[TalkError, AT]
  //  trait ResponseHelpers[AT] {
  //    def failStr (string:String):Response[AT]
  //    def cancel
  //  }
  implicit def string2Helper(string: String) = new {
    def asFail[AT]: Response[AT] = Right(string).fail
  }

  implicit def canceled2Response[AT](cancel: Canceled.type): Response[AT] = Left(cancel).fail

}