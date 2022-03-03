package lecture.syntax

import scala.util.Try

object Example17CollectRecover extends App {

  println(Try {
    0 / 2
  }.recover {
    case x: ArithmeticException => -1
  })




  println(List(1,2,3,4)
    .filter{_ % 3 == 0}
    .map(_ * 2)
  )

  println(List(1,2,3,4)
    .collect{
      case x if x % 3 == 0 => x * 2
      case x if x % 2 == 0 => x + 2

    }
  )

  def a[T](l: T ) = 1
  val b: Int => Int = _ + 1
  println(
    List(1,"two",3,"four")
      .collect{case x: String => x * 2}
  )

}
