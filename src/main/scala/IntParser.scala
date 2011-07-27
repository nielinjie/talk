package talk
object IntParser{
  import scala.util.parsing.combinator._
  import scala.collection.immutable.Range
  class ChoiseParser extends JavaTokenParsers{
    def ints=repsep(intsUnit,",")^^{
      case intsUnits:List[List[Int]]=>
        intsUnits.flatten.removeDuplicates
    }
    def intsUnit=(intRange | int) ^^{
      case intRange:Range=>intRange.toList
      case int:Int=>List(int)
    }
    def intRange= (int~"-"~int)^^{
      case inta~"-"~intb =>
        if(inta<=intb)
          Range.inclusive(inta,intb)
        else
          Range.inclusive(intb, inta)
    }
    def int=decimalNumber ^^{
      case int=>
        int.toInt
    }
  }
  val parser=new ChoiseParser()
  def parse(string:String):Parsers#ParseResult[List[Int]]={
    parser.parseAll(parser.ints,string)
  }
 
}