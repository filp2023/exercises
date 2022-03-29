package exercises07.ex01

import exercises07.data.NonEmptyList
import exercises07.typeclasses._

object Exercise01 {
  object Syntax {}

  object Instances {
    import Syntax._

    implicit val strMonoid = ???

    implicit val intMonoid = ???

    implicit val listInstances: Traverse[List] with Applicative[List] = ???

    implicit val optionInstances: Traverse[Option] with Applicative[Option] = ???

    implicit val nelInstances: Traverse[NonEmptyList] with Applicative[NonEmptyList] = ???

    implicit def listMonoid[A] = ???
  }
}
