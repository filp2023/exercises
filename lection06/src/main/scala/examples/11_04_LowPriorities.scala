package examples

object ShowExamples {
  trait Show[A] {
    def show(a: A): String
  }

  object Show {
    def apply[A](implicit inst: Show[A]): Show[A] =
      inst
  }

  object Instances extends LowPriorityInstances {
    implicit def deriveListShow[A: Show]: Show[List[A]] =
      _.map(Show[A].show).mkString(",")
  }

  trait LowPriorityInstances {
    implicit def baseListShow[A]: Show[List[A]] =
      _.toString
  }
}

object Run extends App {
  import ShowExamples.Instances._
  import ShowExamples._

  implicit val strShow: Show[String] = identity
  // implicit val intShow: Show[Int] = _.toString

  println(Show[List[String]].show(List("a", "b", "c")))
  println(Show[List[Int]].show(List(1, 2, 3)))
}
