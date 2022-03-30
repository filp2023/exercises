package part1

object Example12OtherTC extends App {

  trait Eq[A] {
    def eqv(x: A, y: A): Boolean
  }

  trait Ordering[A] {
    def lt(x: A, y: A): Boolean
  }

  trait Show[A] {
    def show(a: A): String
  }

  trait Parse[A] {
    def parse(s: String): Option[A]
  }

  trait Transformer[A, B] {
    def transform(a: A): B
  }

}
