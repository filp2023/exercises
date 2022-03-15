package fintech.lecture.examples

// meet dat Option: NOTHING OR VALUE
object Example1Problem extends App {
  def calculate(a: Int, b: Int): Int =
    a / b


  println(calculate(1, 2)) // ok


  println(calculate(1, 0)) // runtime exception

}
