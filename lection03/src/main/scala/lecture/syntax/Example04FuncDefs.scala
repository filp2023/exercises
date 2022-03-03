package lecture.syntax

object Example04FuncDefs {
  object F0 {
    val func: (Int, Int) => (Int) =
    { (x: Int, y: Int) => x + y }

    func(1, 2)
  }

  object F1 {
    val func = { (x: Int, y: Int) => x + y }
    func(1, 2)
  }

  object F2 {
    val func: (Int, Int) => (Int) = { (x, y) => x + y }
    func(1, 2)
  }

  object F3 {
    val func: (Int, Int) => (Int) = ( _ + _ )

    func(1, 2)
  }

  object F4 {
    val func: (Int, Int) => (Int) =  _ + _

    func(1, 2)
  }

  object F5 {
    val func = (_: Int) + (_: Int)

    func(1, 2)
  }
}
