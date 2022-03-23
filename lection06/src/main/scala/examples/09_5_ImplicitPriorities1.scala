package examples

object Example03Priorities1 extends App {

  case class Dollar(amount: Double)
  case class Euro(amount: Double)

  object Dollar {
    implicit def convertX2(d: Dollar): Euro = Euro(d.amount * 2)
  }

  implicit def convertX1(d: Dollar): Euro = Euro(d.amount)

  val euro: Euro = Dollar(1)
  println(euro) // prints Euro(1.0)
}
