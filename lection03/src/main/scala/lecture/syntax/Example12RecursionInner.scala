package lecture.syntax

import scala.annotation.tailrec

object Example12RecursionInner extends App {

  def factorialWhile(n: Int): Int = {
    var result = 1
    var number = n
    while (number > 1) {
      result *= number
      number -= 1
    }
    result
  }



//  @tailrec
  def factorialRec(n: Int): Int =
    if (n == 1 || n == 0) 1
    else factorialRec(n - 1) * n

  /*
    factorialRec(3)
    3 * factorialRec(2)
    3 * (2 * factorialRec(1))
    3 * (2 * 1)
    3 * 2
    6
  */


  @tailrec
  def factorialTailRec(acc: Int, n: Int): Int =
    if (n == 0) acc
    else factorialTailRec(acc * n, n - 1)
}
