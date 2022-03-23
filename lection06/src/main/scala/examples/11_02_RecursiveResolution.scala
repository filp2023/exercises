package examples

object Example07RecursiveResolution1 {

  trait Order[A] {
    def lt(a1: A, a2: A): Boolean
  }

  implicit def listIntOrder: Order[List[Int]] = ???

  implicit def listStringOrder: Order[List[String]] = ???

  implicit def listDoubleOrder[K]: Order[List[Double]] = ???
}

object Example07RecursiveResolution2 {
  import Example07RecursiveResolution1._

  implicit def listOrder[A](implicit order: Order[A]): Order[List[A]] = new Order[List[A]] {
    override def lt(list1: List[A], list2: List[A]): Boolean = ???
  }

  implicit val doubleOrder: Order[Double] = new Order[Double] {
    override def lt(a1: Double, a2: Double): Boolean = a1 < a2
  }

  implicitly[Order[List[Double]]]

//  implicitly[Order[List[Double]]]
//
//    is replaced by
//
//  listOrder[Double](implicitly[Order[Double]])
//
//    is replaced by
//
//  listOrder[Double](doubleOrder)
}
