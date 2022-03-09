package exercises04.calculator

import scala.Integral.Implicits.infixIntegralOps

class Calculator[T: Integral] {
  def isZero(t: T): Boolean =
    t == implicitly[Integral[T]].zero

  def calculate(expr: Expr[T]): Result[T] = ???
}
