package examples

object Example04ExplicitFirst extends App {

  val list = List(1, 2, 3)

  class ListOps[A](private val xs: List[A]) extends AnyVal {
    def headOption: Option[A] =
      throw new Exception("Never called")
  }

  implicit def listToWrapper[A](xs: List[A]): ListOps[A] = new ListOps(xs)

  println(list.headOption)
    // prints 1
}
