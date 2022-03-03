package lecture.syntax

object Example14ComposeAndThen extends App {
  val f: Int => String = x =>  s"$x$x$x"
  val func2: String => Int = _.toInt


  val compose: Int => Int = func2.compose(f) // == func2(f(x))

  val composeExploded = { x: Int =>
    val x1 = f(x) // apply argument (func1)
    func2(x1)         // apply self (func2)
  }


  val andThen: Int => Int = f.andThen(func2) // == func2(f(x))

  val r = func2(f(1))
  println(compose(42))
  println(andThen(13))
}
