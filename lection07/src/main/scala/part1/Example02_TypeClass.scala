package part1

object Example02_TypeClass {

  // TypeClass - trait, параметризованный одним или несколькими типами
  // Эти типы должны присутствовать в сигнатурах методов

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  // Тайп-классы могут формировать иерархию (в данном случае через наследование)
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }


  // Чтобы показать что Int - это Monoid, нужно реализовать методы из Monoid
  val sumIntMonoid: Monoid[Int] = new Monoid[Int] {
    override def empty: Int = 0
    override def combine(x: Int, y: Int): Int = x + y
  }


  case class Package(stuff: String)
  // Чтобы показать что Package - это Semigroup, нужно реализовать методы из Semigroup
  val packageSemigroup: Semigroup[Package] = new Semigroup[Package] {
    override def combine(x: Package, y: Package): Package =
      Package(x.stuff + ", " + y.stuff)
  }

}
