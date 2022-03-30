package part1

object Example08Encoding {
  // typeclass encoding

  trait Order[A] {
    def lt(x: A, y: A): Boolean
  }

  // any language
  def maximum[A](list: List[A])(order: Order[A]): Option[A] =
    list match {
      case Nil => None
      case x :: xs =>
        val max = xs.fold(x) { (a, y) =>
          if (order.lt(a, y)) y else a
        }

        Some(max)
    }

  val intOrder: Order[Int] = new Order[Int] {
    override def lt(x: Int, y: Int): Boolean = x < y
  }

  val ints = List(3, 7,  42, 31337)
  maximum(ints)(intOrder)

}
