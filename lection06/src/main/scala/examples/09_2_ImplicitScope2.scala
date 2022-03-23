package examples

object Example02ImplicitScope2 {

  case class Dollar(amount: Double)

  case class Euro(amount: Double)

//  object Dollar {
//    implicit def euroToDollar(euro: Euro): Dollar = Dollar(euro.amount * 1.13)
//  }

//  object Euro {
//    implicit def euroToDollar(euro: Euro): Dollar = Dollar(euro.amount * 1.13)
//  }

  // val dollar: Dollar = Euro(100)

}
