package part1

import part1.Example02_TypeClass.{Monoid, sumIntMonoid}

object Example03TCUsage {

  def combineAll[A](list: List[A])(m: Monoid[A]): A =
    list.foldLeft(m.empty) { (x,y) =>
      m.combine(x,y)
    }

  val ints = List(3, 7, 42, 31337)
  combineAll(ints)(sumIntMonoid)

}
