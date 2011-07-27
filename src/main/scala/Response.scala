package talk

class Response[+AT]
class ParentProcessed extends Response[Nothing]
case class Answered[AT](answer:AT) extends Response[AT]
object NotAnswered extends Response[Nothing]
case class InvalidAnswer(message:String) extends Response[Nothing]