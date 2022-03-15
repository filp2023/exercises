package fintech.lecture.examples

import scala.io.StdIn
import scala.util.Try

object Example07ProblemEither extends App {
  def calculate(a: Int, b: Int): Either[String, Int] =
    Try(a / b).fold(th => Left(th.getMessage), v => Right(v))


  println(calculate(1, 2)) // ok


  println(calculate(1, 0)) // Left(exception message)
}
