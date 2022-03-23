package examples

object Example03Priorities2 {

  case class Dollar(amount: Double)
  case class Euro(amount: Double)

  implicit def convert(d: Dollar): Euro = Euro(d.amount)
  implicit def duplicated(d: Dollar): Euro = Euro(d.amount * 10)

  // val euro: Euro = Dollar(1)
}
