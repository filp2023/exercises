package fintech.lecture.examples

import scala.io.StdIn

// calculator try 2/option
object Example03ProblemOpt extends App {
  def calculate(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  println(calculate(1, 2)) // ok

  println(calculate(1, 0)) // ok, it's just None
}
