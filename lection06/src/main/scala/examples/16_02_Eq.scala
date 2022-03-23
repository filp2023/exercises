package examples

object Example12Eq1 {
  List(1, 2, 3).map(Option(_)).filter(item => item == 1)
}

trait Eq[A] {
  def eqv(a: A, b: A): Boolean
}

object EqInstances {
  implicit val intEq: Eq[Int] = new Eq[Int] {
    def eqv(a: Int, b: Int): Boolean = a == b
  }

  implicit val stringEq: Eq[String] = new Eq[String] {
    def eqv(a: String, b: String): Boolean = a == b
  }
}

object EqSyntax {

  implicit class EqOps[A](val a: A) extends AnyVal {
    def eqv(b: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, b)
    def ===(b: A)(implicit ev: Eq[A]): Boolean = eqv(b)
  }
}

object Example11Eq2 {
  import EqSyntax._
  import EqInstances._


  List(1, 2, 3).filter(item => item === 1)
}


object Example11Eq3 {
  import EqSyntax._
  import EqInstances._

  // List(1, 2, 3).map(Option(_)).filter(item => item === 1)
}
