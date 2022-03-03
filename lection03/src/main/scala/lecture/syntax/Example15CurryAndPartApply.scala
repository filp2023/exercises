package lecture.syntax

object Example15CurryAndPartApply extends App {
  val func12 = (x: Int, y: Int) =>  s"$x$x$x$y"
  val func2 = (_: String).toInt

  val func11 = func12(_: Int, 0) // fixed y

  val x: Int => Int => String = func12.curried
  val func1: Int => String = x(0) // fixed x


  val printFromLib = (format: String, number: Int, t: Int)  => {
    s"$number with $format"
  }

  val betterUse: String => Int => Int => String =
    printFromLib.curried

  val r = betterUse("format")
  val rr = r(1)(1)
  val ttt: (Int, Int) => String = printFromLib("fomr", _, _)
  val withOneFormant: Int => Int => String = betterUse("format")

  val tttt = printFromLib("f", _, _)
  println(func11(2))
  println(func1(2))
}
