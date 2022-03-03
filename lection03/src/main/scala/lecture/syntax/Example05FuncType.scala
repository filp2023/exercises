package lecture.syntax

object Example05FuncType {

  object F0 {
    val func: (Int, Int) => Int = { (x: Int, y: Int) =>
      x + y
    }
    func(1, 2)
    func.apply(1,2)

    val func0: Function2[Int, Int, Int] =
      new Function2[Int, Int, Int] {
      override def apply(x: Int, y: Int): Int = x + y
    }

    func0(1, 2)
    func0.apply(1, 2)
  }

  // func ==== func0

  // A => B === Function1[A,B]
  // (A, B) => C === Function2[A,B,C]
}
