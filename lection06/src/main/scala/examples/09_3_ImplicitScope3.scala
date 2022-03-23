package examples

object Example02ImplicitScope3 extends App {

  case class Dollar(amount: Double)

  case class Euro(amount: Double)

//  object Dollar {
//    implicit def euroToDollar(euro: Euro): Dollar = Dollar(euro.amount * 1.13)
//  }

  def sum(xs: List[Dollar]): Dollar = Dollar(xs.foldLeft(0d)(_ + _.amount))

  // println(sum(List(Euro(1d), Euro(2d))))

}
