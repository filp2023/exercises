package lecture.syntax

import scala.util.{Failure, Random, Success, Try}

object Example02Pure {

  object ImpureCafe {

    trait CreditCard

    case class Coffee(price: Double = 1.0)

    trait Payments {
      def charge(cc: CreditCard, p: Double): Unit = ???
    }

    class Cafe {
      def buyCoffee(cc: CreditCard, p: Payments): Coffee = {
        val cup = Coffee()
        p.charge(cc, cup.price)
        cup
      }
    }

  }

  object FunctionalCafe {

    trait CreditCard

    case class Coffee(price: Double = 1.0)

    case class Charge(cc: CreditCard, amount: Double) {
      def combine(other: Charge): Try[Charge] =
        if (cc == other.cc)
          Success(Charge(cc, amount + other.amount))
        else
          Failure(new Exception("Can't combine charges to different cards"))
    }

    class FunctionalCafe {
      def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
        val cup = Coffee()
        (cup, Charge(cc, cup.price))
      }
    }

  }

}