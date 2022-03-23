package examples

object Example01StartedSample1 {

  val javaList = new java.util.ArrayList[Int]()
  javaList.add(1)
  javaList.add(2)
  javaList.add(3)

//  javaList.headOption
//    gives error "value headOption is not a member of java.util.ArrayList[Int]"
}

object Example01StartedSample2 extends App {
  import Example01StartedSample1._

  class JavaListOps[A](private val xs: java.util.List[A]) extends AnyVal {
    def headOption: Option[A] =
      if (xs.isEmpty)
        None
      else
        Some(xs.get(0))
  }

  implicit def javalistToWrapper[A](xs: java.util.List[A]): JavaListOps[A] = new JavaListOps(xs)

  //import scala.jdk.CollectionConverters._

  println(javaList.headOption)
}

object Example01StartedExample3 {
  // DON'T DO THIS
  implicit def value2option[A](value: A): Option[A] = Some(value)

  // DON'T DO THIS
  implicit def value2either[A](value: A): Either[Nothing, A] = Right(value)
}
