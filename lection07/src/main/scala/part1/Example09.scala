package part1

object Example09 {
  // typeclass encoding with implicits

  trait Order[A] {
    def lt(x: A, y: A): Boolean
  }

  def maximum[A](list: List[A])(implicit order: Order[A]): Option[A] =
    list match {
      case Nil => None
      case x :: xs =>
        val max = xs.fold(x) { (a, y) =>
          if (order.lt(a, y)) y else a
        }

        Some(max)
    }

  implicit val intOrder: Order[Int] = new Order[Int] {
    override def lt(x: Int, y: Int): Boolean = x < y
  }

  val ints = List(3, 7,  42, 31337)
  maximum(ints)


  // + синтаксис
}
