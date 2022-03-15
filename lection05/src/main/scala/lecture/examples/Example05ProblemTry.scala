package fintech.lecture.examples

import scala.io.StdIn
import scala.util.Try

// calculator try 3/try
object Example05ProblemTry extends App {
  def calculate(a: Int, b: Int): Try[Int] =
    Try(a / b)


  println(calculate(1, 2)) // ok


  println(calculate(1, 0)) // runtime Failure
}
